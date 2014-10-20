package com.codepath.columbus.columbus.utils;

import android.content.Context;
import android.os.RemoteException;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by marc on 10/19/14.
 */
public class ColumbusBeaconManager extends Observable implements BeaconManager.MonitoringListener, BeaconManager.RangingListener{

  private static ColumbusBeaconManager instance = null;

  private Context context;

  private BeaconManager beaconManager;
  private final String beaconUUID = "8492e75f-4fd6-469d-b132-043fe94921d8";
  private static Region beaconsRegionToScan;
  private List<Beacon> beaconsInRange = new ArrayList<Beacon>();

  public static ColumbusBeaconManager sharedBeaconManager(Context context){
    if(instance == null) {
      instance = new ColumbusBeaconManager(context);
    }
    return instance;
  }

  private ColumbusBeaconManager(Context context) {
    this.context = context;
  }

  public void init() {
    beaconsRegionToScan = new Region("MuseumsRegion", beaconUUID /*UUID*/, null /*major*/, null /*minor*/);
    // Configure BeaconManager.
    // Start ranging for museum beacons in onStart(), set callback listener for discovered beacons in onCreate()
    beaconManager = new BeaconManager(context);
    try {
      beaconManager.startMonitoring(beaconsRegionToScan);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  public List<Beacon> getBeaconsInRange() {
    return beaconsInRange;
  }

  // BeaconManager monitoring listener methods
  @Override
  public void onEnteredRegion(Region region, List<Beacon> beacons) {
    try {
      beaconManager.startRanging(region);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onExitedRegion(Region region) {
    try {
      beaconManager.stopRanging(region);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
    beaconsInRange = beacons;
    setChanged();
    notifyObservers(beacons);
  }
}
