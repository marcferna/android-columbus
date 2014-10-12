package com.codepath.columbus.columbus.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.columbus.columbus.fragments.MuseumListFragment;
import com.codepath.columbus.columbus.fragments.MuseumMapFragment;

public class MuseumPagerAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 2;
    public MuseumListFragment listFragment;
    public MuseumMapFragment mapsFragment;

    public MuseumPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
       switch(i){
           case 0:
               if (listFragment == null){
                   listFragment = new MuseumListFragment();
               }
               return listFragment;
           case 1:
               if (mapsFragment == null){
                   mapsFragment = new MuseumMapFragment();
               }
                return mapsFragment;
           default:
               return null;
       }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0:
                return "LIST";
            case 1:
        }		return "MAP";
    }
}
