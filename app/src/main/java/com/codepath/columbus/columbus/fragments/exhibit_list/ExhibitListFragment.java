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
import java.util.Observable;
import java.util.Observer;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import uk.co.senab.actionbarpulltorefresh.extras.actionbarsherlock.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class ExhibitListFragment extends Fragment implements Observer {
    private PtrStickyListHeadersListView lvExhibitList;
    private ArrayList<Exhibit> exhibits;
    private ArrayAdapter<Exhibit> aExhibits;
    private String museumId;
    private Context context;
    private PullToRefreshLayout mPullToRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        exhibits = new ArrayList<Exhibit>();
        aExhibits = new ExhibitListAdapter(context, exhibits);
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
        setupOnRefreshListener(v);
        fetchExhibitsFromParse();

        // sort exhibits based on distance
        Collections.sort(exhibits, new ExhibitDistanceComparator());

        return v;
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
                        mPullToRefreshLayout.setRefreshComplete();
                    }
                })
                .setup(mPullToRefreshLayout);
    }

    private void fetchExhibitsFromParse() {
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
        //Log.i("INFO", "Found beacon uuid=" + beacon.getProximityUUID());
        //Log.i("INFO", "num exhibits = " + exhibits.size());

        // find the exhibit this beacon matches
        String beaconID = beacon.getProximityUUID() + ":" + beacon.getMajor() + ":" + beacon.getMinor();
        Exhibit exhibit = findExhibitByBeaconId(beaconID);
        if(exhibit != null) {
            // TODO: is this the right function to compute distance?
            double distance = Utils.computeAccuracy(beacon);
            Log.i("INFO", "distance is " + distance); // + "; difference=" + Math.abs(exhibit.getDistance() - distance));
            exhibit.setDistance(distance);
        }
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

  @Override
  public void update(Observable observable, Object data) {
    updateDistances((List<Beacon>) data);
  }
}


