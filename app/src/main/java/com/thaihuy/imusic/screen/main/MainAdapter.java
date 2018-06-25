package com.thaihuy.imusic.screen.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.thaihuy.imusic.screen.home.HomeFragment;
import com.thaihuy.imusic.screen.mysong.MySongFragment;
import com.thaihuy.imusic.screen.search.SearchFragment;

public class MainAdapter extends FragmentPagerAdapter {
    private static int TAB_COUNT = 3;

    MainAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case MainActivity.Tab.HOME:
                return new HomeFragment();
            case MainActivity.Tab.MY_SONG:
                return new MySongFragment();
            case MainActivity.Tab.SEARCH:
                return new SearchFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }
}
