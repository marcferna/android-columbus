package com.codepath.columbus.columbus.fragments.exhibit_list;


import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.adapters.ExhibitListAdapter;
import com.codepath.columbus.columbus.models.Exhibit;
import com.codepath.columbus.columbus.models.Museum;
import com.codepath.columbus.columbus.utils.ExhibitDistanceComparator;
import com.codepath.columbus.columbus.utils.PtrStickyListHeadersListView;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import uk.co.senab.actionbarpulltorefresh.extras.actionbarsherlock.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class ExhibitListFragment extends Fragment {
    private PtrStickyListHeadersListView lvExhibitList;
    private ArrayList<Exhibit> exhibits;
    private ArrayAdapter<Exhibit> aExhibits;
    private String museumId;
    private String museumUUID;
    private BeaconManager beaconManager;
    private static Region beaconsRegionToScan;
    private Context context;
    private PullToRefreshLayout mPullToRefreshLayout;
    private static boolean refresh;
    private ListView lvExhibitsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        exhibits = new ArrayList<Exhibit>();
        aExhibits = new ExhibitListAdapter(context, exhibits);
        museumId = getArguments().getString("museumId");
        museumUUID = getArguments().getString("museumUUID");
        refresh = true;

        beaconsRegionToScan = new Region("regionId", museumUUID /*UUID*/, null /*major*/, null /*minor*/);
        // Configure BeaconManager.
        // Start ranging for museum beacons in onStart(), set callback listener for discovered beacons in onCreate()
        beaconManager = new BeaconManager(context);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            // Results are not delivered on UI thread.
            @Override
            public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {
                if(!refresh) {
                    //Log.i("INFO", "no refresh yet");
                    return;
                }
                Log.i("INFO", "Ranged beacons: " + beacons);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Note that beacons reported here are already sorted by estimated
                        // distance between device and beacon.
                        getActivity().getActionBar().setSubtitle("Found beacons: " + beacons.size());
                        for (Beacon rangedBeacon : beacons) {
                            updateDistance(rangedBeacon);
                        }
                    }
                });
                refresh = false;
            }
        });

    }


    private void updateDistance(Beacon beacon) {
        Log.i("INFO", "Found beacon uuid=" + beacon.getProximityUUID());
        Log.i("INFO", "num exhibits = " + exhibits.size());

        // XXX: reset distance to zero

        // find the exhibit this beacon matches
        String beaconID = beacon.getProximityUUID() + ":" + beacon.getMajor() + ":" + beacon.getMinor();
        Exhibit exhibit = findExhibitByBeaconId(beaconID);
        if(exhibit != null) {
            // update distance
            // TODO: is this the right function to compute distance?
            double distance = Utils.computeAccuracy(beacon);
            Log.i("INFO", "distance is " + distance + "; difference=" + Math.abs(exhibit.getDistance() - distance));
            if(Math.abs(exhibit.getDistance() - distance) > 0.1) {
                exhibit.setDistance(distance);
            }
        }
        aExhibits.notifyDataSetChanged();
    }


    private Exhibit findExhibitByBeaconId(String beaconID) {
        for(Exhibit exhibit: exhibits) {
            Log.i("INFO", "comparing with " + exhibit.getBeaconId());
            if(exhibit.getBeaconId().equalsIgnoreCase(beaconID)) {
                return exhibit;
            }
        }
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_exhibit_list, container, false);
        lvExhibitList = (PtrStickyListHeadersListView) v.findViewById(R.id.lvExhibitList);
        lvExhibitList.setAdapter((se.emilsjolander.stickylistheaders.StickyListHeadersAdapter) aExhibits);

        //addDummyData();
        mPullToRefreshLayout = (PullToRefreshLayout)v.findViewById(R.id.layout_exhibit_list);

        // Now setup the PullToRefreshLayout
        ActionBarPullToRefresh.from(getActivity())
                .allChildrenArePullable()
                .useViewDelegate(PtrStickyListHeadersListView.class, lvExhibitList)
                .listener(new OnRefreshListener() {
                    @Override
                    public void onRefreshStarted(View view) {
                        // refresh the museum list
                        Log.i("INFO", "called pull to refresh");
                        refresh = true;
                        mPullToRefreshLayout.setRefreshComplete();
                    }
                })
                .setup(mPullToRefreshLayout);

        // Fetch museum object, then fetch the corresponding exhibits
        Log.i("INFO", "query for museum id=" + museumId);
        ParseQuery<Museum> query = ParseQuery.getQuery(Museum.class);
        query.whereEqualTo("objectId", museumId);
        query.getFirstInBackground(new GetCallback<Museum>() {
            @Override
            public void done(Museum result, ParseException e) {
                if (e == null) {
                    ParseQuery.getQuery(Exhibit.class)
                            .whereEqualTo("museum", result)
                            .findInBackground(new FindCallback<Exhibit>() {
                                @Override
                                public void done(List<Exhibit> exhibits1, ParseException e) {
                                    // clear old data
                                    Log.i("INFO", "Found " + exhibits1.size() + " exhibits");
                                    refresh = true;
                                    aExhibits.clear();
                                    aExhibits.addAll(exhibits1);
                                }
                            });
                } else {
                    e.printStackTrace();
                }
            }
        });

        Collections.sort(exhibits, new ExhibitDistanceComparator());

        return v;
    }


    public static ExhibitListFragment newInstance(String museumId, String museumUUID) {
        ExhibitListFragment fragmentExhibit = new ExhibitListFragment();
        Bundle args = new Bundle();
        args.putString("museumId", museumId);
        args.putString("museumUUID", museumUUID);
        fragmentExhibit.setArguments(args);
        return fragmentExhibit;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if device supports Bluetooth Low Energy.
        if (!beaconManager.hasBluetooth()) {
            Toast.makeText(context, "Device does not have Bluetooth Low Energy", Toast.LENGTH_LONG).show();
            return;
        }

        // TODO: allow enabling bluetooth using intents
        if (!beaconManager.isBluetoothEnabled()) {
            Toast.makeText(context, "Please enable bluetooth to see nearest exhibit data", Toast.LENGTH_LONG).show();
            return;
        }

        getActivity().getActionBar().setSubtitle("Scanning...");
        // reset beacons list
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    Toast.makeText(context, "Starting ranging", Toast.LENGTH_SHORT).show();
                    beaconManager.startRanging(beaconsRegionToScan);
                } catch (RemoteException e) {
                    Toast.makeText(context, "Cannot start ranging", Toast.LENGTH_LONG).show();
                    Log.e("ERROR", "Cannot start ranging", e);
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            Toast.makeText(context, "Stopping ranging", Toast.LENGTH_SHORT).show();
            beaconManager.stopRanging(beaconsRegionToScan);
        } catch (RemoteException e) {
            Log.e("ERROR", "Cannot stop but it does not matter now", e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        beaconManager.disconnect();
    }


    private void addDummyData() {
        aExhibits.add(Exhibit.dummyObject(0));
        aExhibits.add(Exhibit.dummyObject(100));
        aExhibits.add(Exhibit.dummyObject(900));
        aExhibits.add(Exhibit.dummyObject(500));
        aExhibits.add(Exhibit.dummyObject(0));
        aExhibits.add(Exhibit.dummyObject(0));
        aExhibits.add(Exhibit.dummyObject(300));
        aExhibits.add(Exhibit.dummyObject(0));
        aExhibits.add(Exhibit.dummyObject(0));
        aExhibits.add(Exhibit.dummyObject(0));
        aExhibits.add(Exhibit.dummyObject(300));
        aExhibits.add(Exhibit.dummyObject(800));
    }
}


