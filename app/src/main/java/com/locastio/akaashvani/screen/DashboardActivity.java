package com.locastio.akaashvani.screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.locastio.akaashvani.BaseActivity;
import com.locastio.akaashvani.R;
import com.locastio.akaashvani.adapter.GroupRecycleViewAdapter;
import com.locastio.akaashvani.data.Group;
import com.locastio.akaashvani.data.UserGroup;
import com.locastio.akaashvani.services.UserGroupAPI;
import com.locastio.akaashvani.util.AkashVaniUtility;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends BaseActivity implements UserGroupAPI.Callback {

    // recycler view
    private RecyclerView mGrpRecyclerView;
    // adapter for showing uses grp
    private GroupRecycleViewAdapter mGrpAdapter;
    // fab button to v=cretae grp
    private FloatingActionButton mCreateGrpFab;
    // grp temporary array list
    private ArrayList<Group> mGroupsArrayList = new ArrayList<Group>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        System.out.println("on ************8  cretae ");

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getUsersGroupApi();

    }

    /**
     * gettings users grp
     */
    private void getUsersGroupApi() {

        if (AkashVaniUtility.checkNetworkConnection(DashboardActivity.this)) {
            showProgressDialog(DashboardActivity.this, "Getting user group ...");
            UserGroupAPI userGroupAPI = new UserGroupAPI(this);
            userGroupAPI.getMyGroups();
        } else {
            Toast.makeText(DashboardActivity.this, R.string.network_issue, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *  initializing UI componenets
     */
    private void initializeUiComponents() {

        mGrpRecyclerView = (RecyclerView) findViewById(R.id.group_recyclerview);

        mGrpAdapter = new GroupRecycleViewAdapter(DashboardActivity.this, mGroupsArrayList);

        mGrpRecyclerView.setLayoutManager(new LinearLayoutManager(DashboardActivity.this));
        mGrpRecyclerView.setAdapter(mGrpAdapter);

        mCreateGrpFab = (FloatingActionButton) findViewById(R.id.create_grp_fab);
        mCreateGrpFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "Fab Clicked .", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DashboardActivity.this, CreateGroupActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void didRetrievedMyGroups(List<ParseObject> userGroupList) {

        dismissProgressDialog();
        if (userGroupList != null && userGroupList.size() > 0) {
            mGroupsArrayList = new ArrayList<Group>();
            for (ParseObject userGroupObj : userGroupList) {
                UserGroup userGroup = (UserGroup) userGroupObj;
                Group group = userGroup.getGroup();

//                group.fetchInBackground();
//                try {
//                    group.fetchIfNeeded();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
                mGroupsArrayList.add(group);
            }

            initializeUiComponents();
        } else {
            Toast.makeText(DashboardActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void didRetrieveGrpFailed(String s) {
        dismissProgressDialog();
        Toast.makeText(DashboardActivity.this, s, Toast.LENGTH_SHORT).show();
    }
}
