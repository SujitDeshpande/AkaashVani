package com.locastio.akaashvani.screen;

import android.content.ContentResolver;
import android.content.Intent;
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
import com.locastio.akaashvani.services.GroupAPI;
import com.locastio.akaashvani.services.UserAPI;
import com.locastio.akaashvani.util.AkashVaniUtility;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupActivity extends BaseActivity implements View.OnClickListener, UserAPI.Callback, GroupAPI.Callback {

    private RecyclerView mContactRecyclerView;
    private ContactsRecycleViewAdapter mContactsAdapter;
    private String phoneNumber = "";
    private List<ParseUser> mContactsList = new ArrayList<ParseUser>();
    private EditText mPhoneNumberEditText;
    private Button mFindButton;
    private EditText mGroupNameEditText;
    private Button mCreateButton;
    private List<ParseUser> mTempUserList = new ArrayList<ParseUser>();

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

//            mContactsList.add(phoneNumber);
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

                        showProgressDialog(CreateGroupActivity.this, "Finding user ...");
                        UserAPI userAPI = new UserAPI(this);
                        userAPI.getUser(mPhoneNumberEditText.getText().toString());

//                        for (int i = 0; i < mContactsList.size(); i++) {
//                            if (mContactsList.get(i).equals(mPhoneNumberEditText.getText().toString())) {
//
//                            }
//                        }


                    }
                } else {
                    Toast.makeText(CreateGroupActivity.this, R.string.network_issue, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.create_button:

                if (AkashVaniUtility.checkNetworkConnection(CreateGroupActivity.this)) {
                    showProgressDialog(CreateGroupActivity.this, "Creating group ...");
                    GroupAPI groupAPI = new GroupAPI(this);
                    boolean bool_grpCreated = groupAPI.createGroup(mGroupNameEditText.getText().toString(), mTempUserList);
                    dismissProgressDialog();

                    if (bool_grpCreated) {
                        Intent intent = new Intent(CreateGroupActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(CreateGroupActivity.this, "Group not created, something went wrong.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(CreateGroupActivity.this, R.string.network_issue, Toast.LENGTH_SHORT).show();
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

    @Override
    public void didRegister(ParseUser user) {

    }

    @Override
    public void didLogin(ParseUser user) {

    }

    @Override
    public void didRetriveUser(ParseUser user) {

        dismissProgressDialog();
        if (user != null) {
            mCreateButton.setEnabled(true);

            mTempUserList.add(user);

            mContactsAdapter.addNewContactNumber(user);

            mPhoneNumberEditText.setText("");

            System.out.println("Size :" + mContactsList.size());
        }
    }

    @Override
    public void didFailed() {

    }


    @Override
    public void didDeleteGroup(String s) {

    }

    @Override
    public void didGroupFailed(String str) {

    }

    @Override
    public void didFailed(String str) {
        dismissProgressDialog();
        mPhoneNumberEditText.setText("");
        Toast.makeText(CreateGroupActivity.this, str, Toast.LENGTH_SHORT).show(); // user not found
    }

}
