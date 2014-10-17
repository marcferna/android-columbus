package com.codepath.columbus.columbus.fragments.exhibit;

import android.os.Bundle;
import android.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.models.Exhibit;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ExhibitDescriptionFragment extends ExhibitFragment {

  // UI Elements
  private TextView tvDescription;

  public static ExhibitDescriptionFragment newInstance(Exhibit exhibit) {
    ExhibitDescriptionFragment fragment = new ExhibitDescriptionFragment();
    fragment.init(exhibit);
    return fragment;
  }

  public ExhibitDescriptionFragment() {
      // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
      // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_exhibit_description, container, false);
    setDescription(view);
    return view;
  }

  public void setDescription(View view) {
    tvDescription = (TextView) view.findViewById(R.id.tvDescription);
    tvDescription.setText(exhibit.getDescriptionLong());
    tvDescription.setMovementMethod (ScrollingMovementMethod.getInstance());
  }


}
