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
    private BeaconManager beaconManager;
    // TODO: set proximityUUID to be museum's UUID here
    private static Region beaconsRegionToScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibit_list);
        String museumId = getIntent().getStringExtra("museumId");
        museumId = "zguAHcyS7S";
        Log.i("INFO", "activity museum id=" + museumId);
        beaconsRegionToScan = new Region("regionId", null /*UUID*/, null /*major*/, null /*minor*/);

        exhibitListFragment = ExhibitListFragment.newInstance(museumId);
        loadFragment();

        // Configure BeaconManager.
        // Start ranging for museum beacons in onStart(), set callback listener for discovered beacons in onCreate()
        beaconManager = new BeaconManager(this);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            // Results are not delivered on UI thread.
            @Override
            public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {
                Log.i("INFO", "Ranged beacons: " + beacons);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Note that beacons reported here are already sorted by estimated
                        // distance between device and beacon.
                        getActionBar().setSubtitle("Found beacons: " + beacons.size());
                        // TODO: update distance in exhibits
                    }
                });
            }
        });
    }

    private void loadFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.flListContainer, exhibitListFragment);
        ft.commit();
    }


    @Override
    protected void onStart() {
        super.onStart();

        // Check if device supports Bluetooth Low Energy.
        if (!beaconManager.hasBluetooth()) {
            Toast.makeText(this, "Device does not have Bluetooth Low Energy", Toast.LENGTH_LONG).show();
            return;
        }

        // TODO: allow enabling bluetooth using intents
        if (!beaconManager.isBluetoothEnabled()) {
            Toast.makeText(this, "Please enable bluetooth to see nearest exhibit data", Toast.LENGTH_LONG).show();
            return;
        }

        getActionBar().setSubtitle("Scanning...");
        // reset beacons list
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override public void onServiceReady() {
                try {
                    Toast.makeText(ExhibitListActivity.this, "Starting ranging", Toast.LENGTH_SHORT).show();
                    beaconManager.startRanging(beaconsRegionToScan);
                } catch (RemoteException e) {
                    Toast.makeText(ExhibitListActivity.this, "Cannot start ranging", Toast.LENGTH_LONG).show();
                    Log.e("ERROR", "Cannot start ranging", e);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            Toast.makeText(this, "Stopping ranging", Toast.LENGTH_SHORT).show();
            beaconManager.stopRanging(beaconsRegionToScan);
        } catch (RemoteException e) {
            Log.e("ERROR", "Cannot stop but it does not matter now", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.disconnect();
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
