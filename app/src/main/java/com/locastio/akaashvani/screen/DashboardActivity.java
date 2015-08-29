package com.locastio.akaashvani.screen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.locastio.akaashvani.Information;
import com.locastio.akaashvani.R;
import com.locastio.akaashvani.adapter.GroupRecycleViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private RecyclerView mGrpRecyclerView;
    private GroupRecycleViewAdapter mGrpAdapter;

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
