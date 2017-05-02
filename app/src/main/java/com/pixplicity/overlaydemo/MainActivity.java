package com.pixplicity.overlaydemo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final int REQUEST_CODE_OVERLAY = 1;

    private TextView mTvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvStatus = (TextView) findViewById(R.id.tv_status);

        Button btSettings = (Button) findViewById(R.id.bt_settings);
        btSettings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // Open the settings page for this app:
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getApplication().getPackageName()));
                startActivityForResult(intent, REQUEST_CODE_OVERLAY);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // On the Nexus 6P, this will be false even when the permission is granted:
        boolean canDraw = Settings.canDrawOverlays(this);

        String state = getString(canDraw ? R.string.yes : R.string.no);
        mTvStatus.setText(state);
    }
}
