# overlay-android-bug
Demo of a bug in Android 7.1.2 (Nougat) on a Nexus 6P

## The issue

Since Android M (23), there's a special security setting to allow apps to draw on top of other apps. Since Android 7.1.2, requesting the state of this setting always fails on the Nexus 6P.

## Shortest possible demo

1. Through the settings, allow your app to draw on top of other apps,
2. Call `Settings.canDrawOverlays(context)`,
3. It will return `false`, even with the permission granted.

