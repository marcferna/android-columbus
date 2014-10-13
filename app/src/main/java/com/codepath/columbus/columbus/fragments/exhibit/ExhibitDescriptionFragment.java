package com.codepath.columbus.columbus.fragments.exhibit;



import android.os.Bundle;
import android.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.codepath.columbus.columbus.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ExhibitDescriptionFragment extends SherlockFragment {

  TextView tvDescription;

  public ExhibitDescriptionFragment() {
      // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
      // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_exhibit_description, container, false);
    tvDescription = (TextView) v.findViewById(R.id.tvDescription);
    tvDescription.setMovementMethod (ScrollingMovementMethod.getInstance());

    return v;
  }


}
