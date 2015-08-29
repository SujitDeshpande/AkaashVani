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
import com.locastio.akaashvani.util.AkashVaniUtility;
import com.parse.ParseUser;

public class RegistrationActivity extends BaseActivity implements View.OnClickListener, UserAPI.Callback {

    // name edit text
    private EditText mNameEdittext;
    // phone number edit ttext
    private EditText mPhoneNumber;
    // password edit ttext
    private EditText mPasswordEdittext;
    // confirm paswsord edit text
    private EditText mConfirmPasswordEdittext;
    // register button
    private Button mRegisterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initializeUiComponents();
    }

    /**
     * initialize ui copmponenets
     */
    private void initializeUiComponents() {
        mNameEdittext = (EditText) findViewById(R.id.name_register_editText2);
        mPhoneNumber = (EditText) findViewById(R.id.phone_register_editText2);
        mPasswordEdittext = (EditText) findViewById(R.id.password_register_editText3);
        mConfirmPasswordEdittext = (EditText) findViewById(R.id.confirm_password_register_editText3);

        mRegisterBtn = (Button) findViewById(R.id.register_button);
        mRegisterBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_button:

                if (AkashVaniUtility.checkNetworkConnection(RegistrationActivity.this)) {
                    if (localValidation()) {
                        UserAPI userAPI = new UserAPI(this);
                        userAPI.registerUser(mPhoneNumber.getText().toString(), mPasswordEdittext.getText().toString(), mNameEdittext.getText().toString());
                    }
                } else {
                    Toast.makeText(RegistrationActivity.this, "Please check your network connection.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * filed ui local validation
     *
     * @return
     */
    private boolean localValidation() {
        if (!TextUtils.isEmpty(mNameEdittext.getText().toString())
                && (!TextUtils.isEmpty(mPhoneNumber.getText().toString()))
                && (mConfirmPasswordEdittext.getText().toString().equals(mPasswordEdittext.getText().toString()))
                && (!TextUtils.isEmpty(mConfirmPasswordEdittext.getText().toString()))
                && (!TextUtils.isEmpty(mPasswordEdittext.getText().toString()))) {

            return true;
        } else {
            Toast.makeText(RegistrationActivity.this, "Please fill mandatory fields.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void didRegister(ParseUser user) {
        if (user != null) {
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
//            PreferenceUtil.getInstance().setStringValue("USER_");
        }
    }

    @Override
    public void didLogin(ParseUser user) {

    }

    @Override
    public void didRetriveUser(ParseUser user) {

    }

    @Override
    public void didFailed() {

    }
}
