package com.locastio.akaashvani.screen;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.locastio.akaashvani.BaseActivity;
import com.locastio.akaashvani.R;

public class RegistrationActivity extends BaseActivity implements View.OnClickListener {

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

        mRegisterBtn = (Button)findViewById(R.id.register_button);
        mRegisterBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_button:
                break;
        }
    }
}
