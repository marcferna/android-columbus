package com.codepath.columbus.columbus.activities;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibit_list);
        String museumId = getIntent().getStringExtra("museumId");
        //museumId = "zguAHcyS7S";
        Log.i("INFO", "activity museum id=" + museumId);

        exhibitListFragment = ExhibitListFragment.newInstance(museumId);
        loadFragment();
    }

    private void loadFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.flListContainer, exhibitListFragment);
        ft.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.exhibit_list, menu);
        return true;
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
        return super.onOptionsItemSelected(item);
    }
}
