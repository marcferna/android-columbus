package com.codepath.columbus.columbus.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.adapters.MuseumPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;


public class MuseumActivity extends SherlockFragmentActivity {

    MuseumPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum);

        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        pagerAdapter = new MuseumPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(pagerAdapter);

        TabPageIndicator titleIndicator = (TabPageIndicator)findViewById(R.id.titles);
        titleIndicator.setViewPager(vpPager);
    }

}
