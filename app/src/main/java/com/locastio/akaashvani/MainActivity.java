package com.locastio.akaashvani;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import tabs.SlidingTabLayout;

public class MainActivity extends AppCompatActivity {


    private ViewPager mPager;
    private SlidingTabLayout mTabLayout;
    private Toolbar toolbar;

    private static final int LOGIN_REQUEST = 0;
    private ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        this.checkAndStartActivity();


        NavigationDrawerFragment navigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        navigationDrawerFragment.setup((DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        mPager = (ViewPager) findViewById(R.id.view_pager);
        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mTabLayout = (SlidingTabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setDistributeEvenly(true);
        //mTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mTabLayout.setViewPager(mPager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
        Log.d("k10", "Activity Result called");
        if (requestCode == LOGIN_REQUEST) {
            if (resultCode == RESULT_OK) {
                Log.d("k10","login success");
                this.currentUser = ParseUser.getCurrentUser();
                this.checkAndStartActivity();
            }
        }
    }

    private Boolean checkAndStartActivity() {
        if (this.currentUser == null) {
            // User clicked to log in.
            ParseLoginBuilder loginBuilder = new ParseLoginBuilder(
                    MainActivity.this);
            Intent parseLoginIntent = loginBuilder.setParseLoginEnabled(false)
                    .setFacebookLoginEnabled(true)
                    .setTwitterLoginEnabled(false)
                    .build();
            startActivityForResult(parseLoginIntent, LOGIN_REQUEST);
            return false;
//        } else {
//            if (this.currentUser.isFirstTimeProfileSetupComplete()) {
//                return true;
//            } else {
//                Intent settingActivity = new Intent(this, SettingActivity.class);
//                startActivity(settingActivity);
//                return false;
//            }
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    class MyPagerAdapter extends FragmentPagerAdapter {

        String[] tabs = {"About", "Location", "Chat"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public Fragment getItem(int position) {
            MyFragment myFragment = MyFragment.getInstance(position);
            return myFragment;
        }

        @Override
        public int getCount() {
            return tabs.length;
        }
    }

    public static class MyFragment extends Fragment {
        private TextView textView;

        public static MyFragment getInstance(int position) {
            MyFragment myFragment = new MyFragment();
            Bundle args = new Bundle();
            args.putInt("position", position);
            myFragment.setArguments(args);
            return myFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.fragment_tabs, container, false);
            textView = (TextView) layout.findViewById(R.id.fragment_tbs);
            Bundle bundle = getArguments();
            textView.setText("The Page Currently Selected is: " + bundle.getInt("position"));
            Log.i("onCreateView", "The Page Currently Selected is: " + bundle.getInt("position"));
//            Log.i("onCreateView", "The Page Currently Selected is: " + savedInstanceState.getInt("position"));
            return layout;
        }
    }
}
