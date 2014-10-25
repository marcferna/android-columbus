package com.codepath.columbus.columbus.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.fragments.exhibit_list.ExhibitListFragment;
import com.codepath.columbus.columbus.fragments.exhibit_list.ExhibitListSearchFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class ExhibitListActivity extends SherlockFragmentActivity {
    private ExhibitListFragment exhibitListFragment;

    private String museumNickname;
    private String museumId;

    @Override
    protected void attachBaseContext(Context newBase) {
      super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibit_list);
        museumId = getIntent().getStringExtra("museumId");
        String museumUUID = getIntent().getStringExtra("museumUUID");
        museumNickname = getIntent().getStringExtra("museumNickname");
        //museumUUID = "8492e75f-4fd6-469d-b132-043fe94921d8";
        Log.i("INFO", "activity museum id=" + museumId + "; uuid=" + museumUUID);

        exhibitListFragment = ExhibitListFragment.newInstance(museumId, museumUUID);
        setActionBar();
        loadFragment();
    }

    public void setActionBar() {
      ActionBar actionBar = getActionBar();
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setTitle(museumNickname + " Exhibits");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.exhibit_list, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search_exhibits);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchExhibits(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    public void searchExhibits(String query) {
        Intent i = new Intent(ExhibitListActivity.this, SearchActivity.class);
        i.putExtra("museumId", museumId);
        i.putExtra("queryString", query);
        i.putExtra("museumNickname", museumNickname);
        startActivity(i);

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
