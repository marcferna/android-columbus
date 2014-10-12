package com.codepath.columbus.columbus.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.adapters.MuseumItemAdapter;
import com.codepath.columbus.columbus.com.codepath.columbus.columbus.models.Museum;

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

        // add some test data
        Museum museum1 = new Museum();
        museum1.name="MOMA";
        museum1.url="http://upload.wikimedia.org/wikipedia/commons/3/3a/British_Museum_from_NE_2.JPG";
        museums.add(museum1);

        Museum museum2 = new Museum();
        museum2.name="Museum of Natural History";
        museum2.url="http://upload.wikimedia.org/wikipedia/commons/8/89/Field_Museum_of_Natural_History.jpg";
        museums.add(museum2);

        Museum museum3 = new Museum();
        museum3.name="Museum of Arts";
        museum3.url="http://upload.wikimedia.org/wikipedia/commons/f/f6/Philadephia_Museum_of_Art.jpg";
        museums.add(museum3);

        museumItemAdapter.notifyDataSetChanged();
        return v;
    }


}
