package com.codepath.columbus.columbus.fragments.exhibit_list;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.activities.ExhibitActivity;
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

public class ExhibitListFragment extends SherlockFragment {
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        exhibits = new ArrayList<Exhibit>();
        aExhibits = new ExhibitListAdapter(context, exhibits);
        museumId = getArguments().getString("museumId");
        museumUUID = getArguments().getString("museumUUID");
        refresh = true;

        // Start ranging for museum beacons in onStart(), set callback listener for discovered beacons in onCreate()
        // Configure BeaconManager.
        beaconsRegionToScan = new Region("regionId", museumUUID /*UUID*/, null /*major*/, null /*minor*/);
        beaconManager = new BeaconManager(context);
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            // Results are not delivered on UI thread.
            @Override
            public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {
                if(refresh) {
                    Log.i("INFO", "Ranged beacons: " + beacons);
                    updateDistances(beacons);
                    refresh = false;
                }
            }
        });
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_exhibit_list, container, false);
        lvExhibitList = (PtrStickyListHeadersListView) v.findViewById(R.id.lvExhibitList);
        lvExhibitList.setAdapter((se.emilsjolander.stickylistheaders.StickyListHeadersAdapter) aExhibits);

        //addDummyData();
        setupListViewListeners();
        setupOnRefreshListener(v);
        fetchExhibitsFromParse();

        // sort exhibits based on distance
        Collections.sort(exhibits, new ExhibitDistanceComparator());

        return v;
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

    private void setupListViewListeners() {
        lvExhibitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("DEBUG", "firing detailed activity");
                // bring up edit activity with items[position]
                Intent i = new Intent(context, ExhibitActivity.class);
                Exhibit selectedExhibit = (Exhibit) exhibits.get(position);
                i.putExtra("exhibitId", selectedExhibit.getObjectId());
                i.putExtra("exhibitName", selectedExhibit.getName());
                Activity activity = (Activity) context;
                activity.startActivity(i);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void setupOnRefreshListener(View v) {
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
    }


    private void fetchExhibitsFromParse() {
        // Fetch museum object, then fetch the corresponding exhibits
        Log.i("INFO", "querying exhibits for museum id=" + museumId);
        ParseQuery<Museum> query = ParseQuery.getQuery(Museum.class);
        // First try to find from the cache and only then go to network
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK); // or CACHE_ONLY
        query.whereEqualTo("objectId", museumId);
        query.getFirstInBackground(new GetCallback<Museum>() {
            @Override
            public void done(Museum result, ParseException e) {
                if (e == null) {
                    ParseQuery<Exhibit> query = ParseQuery.getQuery(Exhibit.class);
                    // First try to find from the cache and only then go to network
                    query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK); // or CACHE_ONLY
                    query.getQuery(Exhibit.class)
                            .whereEqualTo("museum", result)
                            .findInBackground(new FindCallback<Exhibit>() {
                                @Override
                                public void done(List<Exhibit> exhibits1, ParseException e) {
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
    }

    private void updateDistances(final List<Beacon> beacons) {
        // TODO: do I need to run this on UI thread
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Beacons reported here are already sorted by estimated distance between device and beacon.
                getActivity().getActionBar().setSubtitle("Found beacons: " + beacons.size());

                // reset distance of each beacon to zero
                for(Exhibit exhibit: exhibits) {
                    exhibit.setDistance(0);
                }
                // update distance based on discovery
                for (Beacon rangedBeacon : beacons) {
                    updateDistanceForBeacon(rangedBeacon);
                }
                aExhibits.notifyDataSetChanged();
            }
        });
    }

    private void updateDistanceForBeacon(Beacon beacon) {
        // find the exhibit this beacon matches
        String beaconID = beacon.getProximityUUID() + ":" + beacon.getMajor() + ":" + beacon.getMinor();
        Exhibit exhibit = findExhibitByBeaconId(beaconID);
        if(exhibit != null) {
            double distance = Utils.computeAccuracy(beacon);
            Log.i("INFO", "distance is " + distance);
            exhibit.setDistance(distance);
        }
    }


    private Exhibit findExhibitByBeaconId(String beaconID) {
        for(Exhibit exhibit: exhibits) {
            if(exhibit.getBeaconId() != null) {
                Log.i("INFO", "comparing with " + exhibit.getBeaconId());
                if (exhibit.getBeaconId().equalsIgnoreCase(beaconID)) {
                    return exhibit;
                }
            }
        }
        return null;
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


