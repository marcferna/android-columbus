package com.codepath.columbus.columbus.fragments.exhibit_list;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.adapters.ExhibitListAdapter;
import com.codepath.columbus.columbus.models.Exhibit;
import com.codepath.columbus.columbus.models.Museum;
import com.codepath.columbus.columbus.utils.ExhibitDistanceComparator;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ExhibitListFragment extends Fragment {
    private StickyListHeadersListView lvExhibitList;
    private ArrayList<Exhibit> exhibits;
    private ArrayAdapter<Exhibit> aExhibits;
    private String museumId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exhibits = new ArrayList<Exhibit>();
        aExhibits = new ExhibitListAdapter(getActivity(), exhibits);
        museumId = getArguments().getString("museumId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_exhibit_list, container, false);
        lvExhibitList = (StickyListHeadersListView) v.findViewById(R.id.lvExhibitList);
        lvExhibitList.setAdapter((se.emilsjolander.stickylistheaders.StickyListHeadersAdapter) aExhibits);

        //addDummyData();

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

        Collections.sort(exhibits, new ExhibitDistanceComparator());

        return v;
    }


    public static ExhibitListFragment newInstance(String museumId) {
        ExhibitListFragment fragmentExhibit = new ExhibitListFragment();
        Bundle args = new Bundle();
        args.putString("museumId", museumId);
        fragmentExhibit.setArguments(args);
        return fragmentExhibit;
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
