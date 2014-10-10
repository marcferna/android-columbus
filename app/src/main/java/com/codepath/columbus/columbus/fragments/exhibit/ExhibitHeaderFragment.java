package com.codepath.columbus.columbus.fragments.exhibit;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.adapters.ImageSlideAdapter;
import com.codepath.columbus.columbus.utils.CirclePageIndicator;
import com.codepath.columbus.columbus.utils.PageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ExhibitHeaderFragment extends Fragment {

  // UI References
  private ViewPager viewPager;
  PageIndicator pageIndicator;

  List<String> images;
  FragmentActivity activity;


  public ExhibitHeaderFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    images = new ArrayList<String>();
    images.add("http://upload.wikimedia.org/wikipedia/commons/8/89/Field_Museum_of_Natural_History.jpg");
    images.add("http://upload.wikimedia.org/wikipedia/commons/8/89/Field_Museum_of_Natural_History.jpg");
    images.add("http://upload.wikimedia.org/wikipedia/commons/8/89/Field_Museum_of_Natural_History.jpg");

    View view = inflater.inflate(R.layout.fragment_exhibit_header, container, false);
    viewPager = (ViewPager) view.findViewById(R.id.view_pager);
    viewPager.setAdapter(new ImageSlideAdapter(activity, images));

    pageIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
    pageIndicator.setViewPager(viewPager, 0);

    return view;
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
  }
}
