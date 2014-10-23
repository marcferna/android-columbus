package com.codepath.columbus.columbus.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.adapters.MuseumPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;


public class MuseumActivity extends SherlockFragmentActivity {

    MuseumPagerAdapter pagerAdapter;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.museum, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_login:
                Intent i = new Intent(this,LoginActivity.class);
                startActivity(i);
                return true;
            default:
                return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum);

        setActionBar();

        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        pagerAdapter = new MuseumPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(pagerAdapter);

        TabPageIndicator titleIndicator = (TabPageIndicator)findViewById(R.id.titles);
        titleIndicator.setViewPager(vpPager);
    }


    public void setActionBar() {
        ActionBar actionBar = getActionBar();
        String title = "<font color=\""+getResources().getColor(R.color.actionbar_title_color)+"\">Museums</font>";
        actionBar.setTitle(Html.fromHtml(title));
    }
}
