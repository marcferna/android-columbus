package com.codepath.columbus.columbus.fragments.exhibit_list;


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
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.activities.ExhibitActivity;
import com.codepath.columbus.columbus.adapters.ExhibitListAdapter;
import com.codepath.columbus.columbus.models.Exhibit;
import com.codepath.columbus.columbus.models.Museum;
import com.codepath.columbus.columbus.utils.ExhibitDistanceComparator;
import com.codepath.columbus.columbus.utils.PtrStickyListHeadersListView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ExhibitListSearchFragment extends SherlockFragment {
    private PtrStickyListHeadersListView lvExhibitListSearch;
    private ArrayList<Exhibit> exhibits;
    private ArrayAdapter<Exhibit> aExhibits;
    private String museumId;
    private String queryString;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        exhibits = new ArrayList<Exhibit>();
        aExhibits = new ExhibitListAdapter(context, exhibits);
        museumId = getArguments().getString("museumId");
        queryString = getArguments().getString("query");
    }

    public static ExhibitListSearchFragment newInstance(String museumId, String query) {
        ExhibitListSearchFragment fragmentExhibit = new ExhibitListSearchFragment();
        Bundle args = new Bundle();
        args.putString("museumId", museumId);
        args.putString("query", query);
        fragmentExhibit.setArguments(args);
        return fragmentExhibit;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search_list, container, false);
        lvExhibitListSearch = (PtrStickyListHeadersListView) v.findViewById(R.id.lvExhibitListSearch);
        lvExhibitListSearch.setAdapter((se.emilsjolander.stickylistheaders.StickyListHeadersAdapter) aExhibits);

        setupListViewListeners();
        fetchExhibitsFromParse();

        // sort exhibits based on distance
        Collections.sort(exhibits, new ExhibitDistanceComparator());

        return v;
    }

    private void setupListViewListeners() {
        lvExhibitListSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("DEBUG", "firing detailed activity");
                // bring up edit activity with items[position]
                Intent i = new Intent(context, ExhibitActivity.class);
                Exhibit selectedExhibit = (Exhibit) exhibits.get(position);
                i.putExtra("exhibitId", selectedExhibit.getObjectId());
                i.putExtra("exhibitName", selectedExhibit.getName());
                context.startActivity(i);
            }
        });
    }

    private void fetchExhibitsFromParse() {
        // Fetch museum object, then fetch the corresponding exhibits
        Log.i("INFO", "querying exhibits for museum id=" + museumId);
        ParseQuery<Museum> query = ParseQuery.getQuery(Museum.class);
        query.whereEqualTo("objectId", museumId);
        query.getFirstInBackground(new GetCallback<Museum>() {
            @Override
            public void done(Museum result, ParseException e) {
                if (e == null) {
                    ParseQuery.getQuery(Exhibit.class)
                            .whereEqualTo("museum", result)
                            .whereContains("searchName", queryString.toLowerCase())
                            .findInBackground(new FindCallback<Exhibit>() {
                                @Override
                                public void done(List<Exhibit> exhibits1, ParseException e) {
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

}


