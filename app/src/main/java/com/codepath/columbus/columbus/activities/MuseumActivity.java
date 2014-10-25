package com.codepath.columbus.columbus.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.adapters.MuseumPagerAdapter;
import com.codepath.columbus.columbus.utils.RoundedAvatarDrawable;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.viewpagerindicator.TabPageIndicator;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MuseumActivity extends SherlockFragmentActivity {

    MuseumPagerAdapter pagerAdapter;
    MenuItem loginItem;

    @Override
    protected void attachBaseContext(Context newBase) {
      super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.museum, menu);
        loginItem = menu.getItem(0);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_login:
                Intent i = new Intent(this,LoginActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
        String title = "<font color=\""+getResources().getColor(R.color.actionbar_title_color)+"\">Colombus</font>";
        actionBar.setTitle(Html.fromHtml(title));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // set the profile image if signed in
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String imageURL = sharedPreferences.getString("imageURL",null);

        if (imageURL != null && loginItem != null){
            // by default the profile url gives 50x50 px image only
            imageURL = imageURL.substring(0,imageURL.length() - 2)+ 80;

            // set the profile image
            ImageLoader.getInstance().loadImage(imageURL,new ImageLoadingListener() {
                public void onLoadingStarted(String imageUri, View view) {
                }

                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                }

                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    loginItem.setIcon(new RoundedAvatarDrawable(loadedImage));
                }

                public void onLoadingCancelled(String imageUri, View view) {
                }
            });
        }
    }
}
