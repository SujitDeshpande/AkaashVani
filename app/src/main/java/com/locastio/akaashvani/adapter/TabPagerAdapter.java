package com.locastio.akaashvani.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.locastio.akaashvani.screen.fragment.ChatFragment;
import com.locastio.akaashvani.screen.fragment.Locationfragment;

//import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Agoel on 30-08-2015.
 */
public class TabPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    private final String mGroupObjId;
    private String tabTitles[] = new String[]{"Location", "Chat"};
    private Context context;

    public TabPagerAdapter(FragmentManager fm, Context context, String strGroupObjId) {
        super(fm);
        this.context = context;
        mGroupObjId = strGroupObjId;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return Locationfragment.newInstance("" + position, "location");
            case 1:
                return ChatFragment.newInstance("" +position, mGroupObjId);
            default:
                return Locationfragment.newInstance("" + position, "location");
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}

