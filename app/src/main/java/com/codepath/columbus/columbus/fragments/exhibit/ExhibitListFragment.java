package com.codepath.columbus.columbus.fragments.exhibit;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.adapters.ExhibitListAdapter;
import com.codepath.columbus.columbus.com.codepath.columbus.columbus.models.Exhibit;

import java.util.ArrayList;

public class ExhibitListFragment extends Fragment {
    private ListView lvExhibitList;
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
        lvExhibitList = (ListView) v.findViewById(R.id.lvExhibitList);
        lvExhibitList.setAdapter(aExhibits);
        aExhibits.add(Exhibit.testInit());
        return v;
    }

}
