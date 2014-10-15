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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exhibits = new ArrayList<Exhibit>();
        aExhibits = new ExhibitListAdapter(getActivity(), exhibits);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_exhibit_list, container, false);
        lvExhibitList = (StickyListHeadersListView) v.findViewById(R.id.lvExhibitList);
        lvExhibitList.setAdapter((se.emilsjolander.stickylistheaders.StickyListHeadersAdapter) aExhibits);

        //addDummyData();

        // Get first museum temporarily until we add onclickhandler to MuseumActivity
        ParseQuery.getQuery(Museum.class).findInBackground(new FindCallback<Museum>() {
            @Override
            public void done(List<Museum> museums, ParseException e) {
                Museum firstMuseum = museums.get(0);
                Log.i("DEBUG", "First museum=" + firstMuseum.get("name"));
                ParseQuery.getQuery(Exhibit.class).whereEqualTo("museum", firstMuseum).findInBackground(new FindCallback<Exhibit>() {
                    @Override
                    public void done(List<Exhibit> exhibits1, ParseException e) {
                        // clear old data
                        Log.i("DEBUG", "Found " + exhibits1.size() + " exhibits");
                        aExhibits.clear();
                        aExhibits.addAll(exhibits1);
                    }
                });
            }
        });


        Collections.sort(exhibits, new ExhibitDistanceComparator());

        return v;
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
