package com.locastio.akaashvani.screen;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.locastio.akaashvani.BaseActivity;
import com.locastio.akaashvani.R;
import com.locastio.akaashvani.services.UserAPI;
import com.parse.ParseUser;

public class LoginActivity extends BaseActivity implements View.OnClickListener, UserAPI.Callback {

    // phone eduit text
    private EditText mPhoneEditText;
    // password euit text
    private EditText mPasswordEditText;
    // login button
    private Button mLoginButton;
    private Button mNewRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeUiComponenets();
    }

    /**
     * initializing ui components
     */
    private void initializeUiComponenets() {
        mPhoneEditText = (EditText) findViewById(R.id.phone_login_editText2);
        mPasswordEditText = (EditText) findViewById(R.id.password_login_editText3);

        mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(this);

        mNewRegisterButton = (Button) findViewById(R.id.newuser_register_button);
        mNewRegisterButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:

                if (localValidation()) {
                    UserAPI userAPI = new UserAPI(this);
                    userAPI.login(mPhoneEditText.getText().toString(), mPasswordEditText.getText().toString());
                }

                break;
            case R.id.newuser_register_button:

                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * local validation for fields
     *
     * @return
     */
    private boolean localValidation() {

        if (!TextUtils.isEmpty(mPhoneEditText.getText().toString()) &&
                (mPhoneEditText.getText().toString().length() == 10) &&
                !TextUtils.isEmpty(mPasswordEditText.getText().toString())) {
            return true;
        } else {
            Toast.makeText(LoginActivity.this, "Please fill mandatory fields.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void didRegister(ParseUser user) {

    }

    @Override
    public void didLogin(ParseUser user) {

        if (user != null) {

            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void didFailed() {
        Toast.makeText(LoginActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
    }
}
