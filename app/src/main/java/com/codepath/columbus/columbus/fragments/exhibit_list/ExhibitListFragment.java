package com.codepath.columbus.columbus.fragments.exhibit_list;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.adapters.ExhibitListAdapter;
import com.codepath.columbus.columbus.models.Exhibit;
import com.codepath.columbus.columbus.models.ExhibitDistanceComparator;

import java.util.ArrayList;
import java.util.Collections;

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

        addDummyData();
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
