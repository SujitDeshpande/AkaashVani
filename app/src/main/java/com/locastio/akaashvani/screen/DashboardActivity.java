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
import com.locastio.akaashvani.Information;
import com.locastio.akaashvani.R;
import com.locastio.akaashvani.adapter.GroupRecycleViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends BaseActivity {

    private RecyclerView mGrpRecyclerView;
    private GroupRecycleViewAdapter mGrpAdapter;
    private FloatingActionButton mCreateGrpFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initializeUiComponents();
    }

    /**
     *
     */
    private void initializeUiComponents() {

        mGrpRecyclerView = (RecyclerView) findViewById(R.id.group_recyclerview);

        mGrpAdapter = new GroupRecycleViewAdapter(DashboardActivity.this, getGroupData());

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

    /**
     * @return
     */
    private List<Information> getGroupData() {
        List<Information> data = new ArrayList<>();
        int[] icons = {R.drawable.airballoon, R.drawable.airballoon, R.drawable.airballoon, R.drawable.airballoon};
        String[] titles = {"Title 1", "Title 2", "Title 3", "Title 4"};
        for (int i = 0; (i < titles.length); i++) {
            Information current = new Information();
//            current.iconID = icons[i];
            current.title = titles[i];
            data.add(current);
        }
        return data;
    }

}
