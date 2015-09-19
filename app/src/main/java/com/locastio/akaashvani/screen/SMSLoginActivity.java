package com.locastio.akaashvani.screen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.locastio.akaashvani.BaseActivity;
import com.locastio.akaashvani.R;
import com.locastio.akaashvani.services.UserAPI;
import com.locastio.akaashvani.services.UserAPI.Callback;
import com.locastio.akaashvani.view.LoginButton;
import com.parse.ParseUser;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;

public class SMSLoginActivity extends BaseActivity implements UserAPI.Callback {

    private static final String TWITTER_KEY = "CKEihbEh3LgzDuaQov81oIKP7";
    private static final String TWITTER_SECRET = "emKomVQZTmzTRMBaG0axVoK7JRYhIMB5wpz4HxhT9Li4xAlM48";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        final UserAPI userAPI = new UserAPI((Callback) this);
        Fabric.with(this, new TwitterCore(authConfig), new Digits());
        setContentView(R.layout.activity_smslogin);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        final LoginButton digitsButton = (LoginButton) findViewById(R.id.phone_button);
        digitsButton.setBackgroundColor(Color.BLUE);

        digitsButton.setCallback(new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                // Do something with the session and phone number
                Toast.makeText(SMSLoginActivity.this,
                        "Authentication Successful for " + phoneNumber, Toast.LENGTH_SHORT).show();
                showProgressDialog(SMSLoginActivity.this, "Logging In");
                //phoneNumber = phoneNumber.substring(3, phoneNumber.length());
                userAPI.login(phoneNumber , "1234");
            }

            @Override
            public void failure(DigitsException exception) {
                // Do something on failure
                Toast.makeText(SMSLoginActivity.this, "Error",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_smslogin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void didRegister(ParseUser user) {

    }

    @Override
    public void didLogin(ParseUser user) {
        dismissProgressDialog();
        if (user != null) {

            Intent intent = new Intent(SMSLoginActivity.this, DashboardActivity.class);
            startActivity(intent);
            //finish();
        }
    }

    @Override
    public void didRetriveUser(ParseUser user) {

    }

    @Override
    public void didFailed() {
        dismissProgressDialog();
        Toast.makeText(SMSLoginActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void didFailed(String str) {

    }
}
