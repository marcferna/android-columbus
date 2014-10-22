package com.codepath.columbus.columbus.activities;

import android.app.ActionBar;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.fragments.exhibit.ExhibitHeaderFragment;
import com.codepath.columbus.columbus.fragments.exhibit_list.ExhibitListFragment;
import com.codepath.columbus.columbus.models.Museum;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;

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
      actionBar.setTitle(museumNickname + " Exhibits");
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
