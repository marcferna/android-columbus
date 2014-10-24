package com.codepath.columbus.columbus.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;

import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.fragments.exhibit_list.ExhibitListFragment;


public class ExhibitListActivity extends FragmentActivity {
    private ExhibitListFragment exhibitListFragment;

    private String museumNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibit_list);
        String museumId = getIntent().getStringExtra("museumId");
        String museumUUID = getIntent().getStringExtra("museumUUID");
        museumNickname = getIntent().getStringExtra("museumNickname");
        //museumId = "zguAHcyS7S";
        //museumUUID = "8492e75f-4fd6-469d-b132-043fe94921d8";
        Log.i("INFO", "activity museum id=" + museumId + "; uuid=" + museumUUID);

        exhibitListFragment = ExhibitListFragment.newInstance(museumId, museumUUID);
        setActionBar();
        loadFragment();
    }

    public void setActionBar() {
      ActionBar actionBar = getActionBar();
      actionBar.setDisplayHomeAsUpEnabled(true);

      String title = "<font color=\""+getResources().getColor(R.color.actionbar_title_color)+"\">"+museumNickname + " Exhibits </font>";
      actionBar.setTitle(Html.fromHtml(title));
    }

    @Override
    public void onBackPressed() {
      super.onBackPressed();
      overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void loadFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.flListContainer, exhibitListFragment);
        ft.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
          case android.R.id.home:
            // app icon in action bar clicked; goto parent activity.
            this.finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
