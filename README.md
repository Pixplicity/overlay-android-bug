# overlay-android-bug
Demo of a bug in Android 7.1.2 (Nougat) on a Nexus 6P

[Follow the issue on the issuetracker](https://issuetracker.google.com/issues/37885328)

## The issue

Since Android M (23), there's a special security setting to allow apps to draw on top of other apps. Since Android 7.1.2, requesting the state of this setting always fails on the Nexus 6P.

## Shortest possible demo

1. Through the settings, allow your app to draw on top of other apps,
2. Call `Settings.canDrawOverlays(context)`,
3. It will return `false`, even with the permission granted.

## Screenshots

<img src="https://github.com/Pixplicity/overlay-android-bug/blob/master/screenshots/screenshot1.png?raw=true" width="300" /> <img src="https://github.com/Pixplicity/overlay-android-bug/blob/master/screenshots/screenshot2.png?raw=true" width="300" />

## Models

**Does not work:**

- Nexus 6P 7.1.2-3783593

**Works:**

- Pixel 7.1.2-3766409
- Nexus 5 6.0.2-3437181

Feel free to submit pull requests if you find more failing OS versions or models, and [please star the issue](https://issuetracker.google.com/issues/37885328) if you are affected.

## Details

The internals of `Settings.java` show a failure in the following code:

```java
        AppOpsManager appOpsMgr = (AppOpsManager)context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = AppOpsManager.MODE_DEFAULT;
        if (makeNote) {
            mode = appOpsMgr.noteOpNoThrow(appOpsOpCode, uid, callingPackage);
        } else {
            mode = appOpsMgr.checkOpNoThrow(appOpsOpCode, uid, callingPackage);
        }

        switch (mode) {
            case AppOpsManager.MODE_ALLOWED:
                return true;

            case AppOpsManager.MODE_DEFAULT:
                // this is the default operating mode after an app's installation
                // In this case we will check all associated static permission to see
                // if it is granted during install time.
                for (String permission : permissions) {
                    if (context.checkCallingOrSelfPermission(permission) == PackageManager
                            .PERMISSION_GRANTED) {
                        // if either of the permissions are granted, we will allow it
                        return true;
                    }
                }

            default:
                // this is for all other cases trickled down here...
                if (!throwException) {
                    return false;
                }
        }
```

In the above, `checkOpNoThrow` will always return `MODE_IGNORED` instead of the expected `MODE_ALLOWED`, meaning:

```java
    /**
     * Result from {@link #checkOp}, {@link #noteOp}, {@link #startOp}: the given caller is
     * not allowed to perform the given operation, and this attempt should
     * <em>silently fail</em> (it should not cause the app to crash).
     */
    public static final int MODE_IGNORED = 1;
```
