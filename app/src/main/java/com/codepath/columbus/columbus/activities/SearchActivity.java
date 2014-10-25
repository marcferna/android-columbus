package com.codepath.columbus.columbus.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.widget.Toast;

import com.actionbarsherlock.view.MenuItem;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.fragments.exhibit_list.ExhibitListSearchFragment;

public class SearchActivity extends SherlockFragmentActivity {
    private ExhibitListSearchFragment exhibitListSearchFragment;
    private String query;
    private String museumNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        String museumId = getIntent().getStringExtra("museumId");
        query = getIntent().getStringExtra("queryString");
        museumNickname = getIntent().getStringExtra("museumNickname");
        Toast.makeText(this, "museum name=" + museumNickname, Toast.LENGTH_SHORT).show();

        exhibitListSearchFragment = ExhibitListSearchFragment.newInstance(museumId, query);
        setActionBar();
        loadFragment();
    }


    public void setActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(museumNickname + " : \"" + query + "\"");
    }

    private void loadFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.flListSearchContainer, exhibitListSearchFragment);
        ft.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == android.R.id.home) {
            // app icon in action bar clicked; goto parent activity.
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
