package com.sdsmdg.pulkit.callingtext;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;


public class TabsPagerAdapter extends FragmentPagerAdapter {

    View view;

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new ContactListFragment();
            case 1:
                return new HistoryFragment();
            case 2:
                return new NewFragment();
            case 3:
                return new FavouriteFragment();
        }

        return null;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Contacts";
            case 1:
                return "History";
            case 2:
                return "New";
            case 3:
                return "Favourite";
        }
        return null;
    }
    @Override
    public int getCount() {
        return 4;
    }

}
