package com.codeforfun.himanshu.votetochange.Adapters.ViewPagerAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codeforfun.himanshu.votetochange.Fragments.MainActivity.Home;
import com.codeforfun.himanshu.votetochange.Fragments.MainActivity.MyElection;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new Home();
        }
        else if (position == 1)
        {
            fragment = new MyElection();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Home";
        }
        else if (position == 1) {
            title = "My Election";
        }
        return title;
    }
}