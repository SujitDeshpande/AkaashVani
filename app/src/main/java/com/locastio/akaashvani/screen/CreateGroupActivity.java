package com.locastio.akaashvani.screen;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.locastio.akaashvani.BaseActivity;
import com.locastio.akaashvani.R;
import com.locastio.akaashvani.adapter.ContactsRecycleViewAdapter;
import com.locastio.akaashvani.util.AkashVaniUtility;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mContactRecyclerView;
    private ContactsRecycleViewAdapter mContactsAdapter;
    private String phoneNumber = "";
    private List<String> mContactsList = new ArrayList<String>();
    private EditText mPhoneNumberEditText;
    private Button mFindButton;
    private EditText mGroupNameEditText;
    private Button mCreateButton;
    private List<String> mTempContactsList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

//        getNumber(this.getContentResolver());

        initializeUiComponents();
    }


    private synchronized void getNumber(ContentResolver cr) {
        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        // use the cursor to access the contacts
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            // get display name
            phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            // get phone number
            System.out.println(".................." + phoneNumber);

            mContactsList.add(phoneNumber);
        }
    }

    /**
     *
     */
    private void initializeUiComponents() {

        mGroupNameEditText = (EditText) findViewById(R.id.grp_name_grp_editText2);
        mPhoneNumberEditText = (EditText) findViewById(R.id.phone_create_grp_editText2);

        mFindButton = (Button) findViewById(R.id.find_button);
        mFindButton.setOnClickListener(this);

        mCreateButton = (Button) findViewById(R.id.create_button);
        mCreateButton.setOnClickListener(this);

        mCreateButton.setEnabled(false);

        mContactRecyclerView = (RecyclerView) findViewById(R.id.contacts_recyclerview);
        mContactRecyclerView.setLayoutManager(new LinearLayoutManager(CreateGroupActivity.this));
//
        mContactsAdapter = new ContactsRecycleViewAdapter(CreateGroupActivity.this, mContactsList);
        mContactRecyclerView.setAdapter(mContactsAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_button:

                if (AkashVaniUtility.checkNetworkConnection(CreateGroupActivity.this)) {
                    if (localValidation()) {
//                        for (int i = 0; i < mContactsList.size(); i++) {
//                            if (mContactsList.get(i).equals(mPhoneNumberEditText.getText().toString())) {
//
//                            }
//                        }

                        mCreateButton.setEnabled(true);

                        mTempContactsList.add(mPhoneNumberEditText.getText().toString());

                        mContactsAdapter.addNewContactNumber(mPhoneNumberEditText.getText().toString());

                        mPhoneNumberEditText.setText("");
//                        for (int i = 0; i < mTempContactsList.size(); i++) {
//
//                            if (mTempContactsList.get(i).equalsIgnoreCase(mPhoneNumberEditText.getText().toString())) {
//                                Toast.makeText(CreateGroupActivity.this, "This number is already added.", Toast.LENGTH_SHORT).show();
//                            } else {
//
//                                mContactsAdapter.addNewContactNumber(mPhoneNumberEditText.getText().toString());
//                            }
//                        }

                        System.out.println("Size :" + mContactsList.size());
                    }
                } else {
                    Toast.makeText(CreateGroupActivity.this, "Please check your network connection.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * @return
     */
    private boolean localValidation() {

        if (!TextUtils.isEmpty(mPhoneNumberEditText.getText().toString())
                && (mPhoneNumberEditText.getText().toString().length() == 10)
                && (!TextUtils.isEmpty(mGroupNameEditText.getText().toString()))) {
            mPhoneNumberEditText.setError(null);
            mGroupNameEditText.setError(null);
            return true;
        } else if (mPhoneNumberEditText.getText().toString().length() < 10) {
            mPhoneNumberEditText.setError("Please enter 10 digit mobile number.");
            return false;
        } else if (TextUtils.isEmpty(mGroupNameEditText.getText().toString())) {
            mGroupNameEditText.setError("Please enter group name.");
            return false;
        } else {
            return false;
        }
    }


/**
 * @return
 */
//    private List<String> getGroupData(ContentResolver cr) {
//        List<String> data = new ArrayList<>();
//        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
//        // use the cursor to access the contacts
//        while (phones.moveToNext()) {
//            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//            // get display name
//            phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//            // get phone number
//            System.out.println(".................." + phoneNumber);
//        }
//        return data;
//    }

}
