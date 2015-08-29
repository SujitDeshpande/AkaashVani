package com.locastio.akaashvani.screen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.locastio.akaashvani.BaseActivity;
import com.locastio.akaashvani.MainActivity;
import com.locastio.akaashvani.R;
import com.locastio.akaashvani.preference.PreferenceKey;
import com.locastio.akaashvani.preference.PreferenceUtil;

public class Splashactivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashactivity);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String authToken = PreferenceUtil.getInstance().getStringValue(PreferenceKey.KEY_AUTH_TOKEN, "");
                Intent intent = null;
                if (!TextUtils.isEmpty(authToken)) {
                    intent = new Intent(Splashactivity.this, MainActivity.class);

                } else {
                    intent = new Intent(Splashactivity.this, LoginActivity.class);

                }
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {

        super.onStop();
    }

}
