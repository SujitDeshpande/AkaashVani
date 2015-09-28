package com.locastio.akaashvani.screen;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.locastio.akaashvani.BaseActivity;
import com.locastio.akaashvani.R;
import com.locastio.akaashvani.adapter.TabPagerAdapter;
import com.locastio.akaashvani.screen.fragment.ChatFragment;
import com.locastio.akaashvani.screen.fragment.Locationfragment;

public class TabLocationChatActivity extends BaseActivity implements Locationfragment.OnFragmentInteractionListener, ChatFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_location_chat);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
//        TextView toolbarTextView = (TextView) toolbar.findViewById(R.id.toolbar_heading);
//        toolbarTextView.setText(getIntent().getStringExtra("groupName"));
//        setSupportActionBar(toolbar);

        setToolBarComponents();

        String strGroupObjId = getIntent().getStringExtra("groupObjId");

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager(),
                TabLocationChatActivity.this , strGroupObjId));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
