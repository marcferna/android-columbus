package com.codepath.columbus.columbus.fragments.museum;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.adapters.MuseumItemAdapter;
import com.codepath.columbus.columbus.models.Museum;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.actionbarpulltorefresh.extras.actionbarsherlock.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class MuseumListFragment extends Fragment {

    private PullToRefreshLayout mPullToRefreshLayout;
    private MuseumItemAdapter museumItemAdapter;
    private ListView lvMuseums;
    private ArrayList<Museum> museums;

    public MuseumListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_museum_list, container, false);

        mPullToRefreshLayout = (PullToRefreshLayout)v.findViewById(R.id.ptr_layout);

        // Now setup the PullToRefreshLayout
        ActionBarPullToRefresh.from(this.getActivity())
                .allChildrenArePullable()
                .listener(new OnRefreshListener() {
                    @Override
                    public void onRefreshStarted(View view) {
                        // refresh the museum list
                        mPullToRefreshLayout.setRefreshComplete();
                    }
                })
        .setup(mPullToRefreshLayout);

        lvMuseums = (ListView)v.findViewById(R.id.lvMuseums);

        museums = new ArrayList<Museum>();
        museumItemAdapter = new MuseumItemAdapter(getActivity(),R.layout.museum_item,museums);
        lvMuseums.setAdapter(museumItemAdapter);

        // fetch museum list
        ParseQuery<Museum> query = ParseQuery.getQuery(Museum.class);
        // First try to find from the cache and only then go to network
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK); // or CACHE_ONLY
        query.orderByAscending("name");
        query.findInBackground(new FindCallback<Museum>() {
            @Override
            public void done(List<Museum> museums, ParseException e) {
                // clear old data
                museumItemAdapter.clear();

                museumItemAdapter.addAll(museums);
            }
        });

        return v;
    }


}
