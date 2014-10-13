package com.codepath.columbus.columbus.fragments.exhibit;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.codepath.columbus.columbus.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ExhibitCommentsFragment extends SherlockFragment {


    public ExhibitCommentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exhibit_comments, container, false);
    }


}
