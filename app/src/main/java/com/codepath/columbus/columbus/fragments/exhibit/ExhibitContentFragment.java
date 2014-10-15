package com.codepath.columbus.columbus.fragments.exhibit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.adapters.ExhibitContentPageAdapter;
import com.codepath.columbus.columbus.models.Exhibit;
import com.viewpagerindicator.TabPageIndicator;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ExhibitContentFragment extends ExhibitFragment {

  // Adapter
  private ExhibitContentPageAdapter pagerAdapter;

  // UI Elements
  private ViewPager viewPager;

  public static ExhibitContentFragment newInstance(Exhibit exhibit) {
    ExhibitContentFragment fragment = new ExhibitContentFragment();
    fragment.init(exhibit);
    return fragment;
  }

  public ExhibitContentFragment() {
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
    View view = inflater.inflate(R.layout.fragment_exhibit_content, container, false);
    setViewPager(view);
    return view;
  }

  public void setViewPager(View view) {
    viewPager = (ViewPager) view.findViewById(R.id.vpPager);
    pagerAdapter = new ExhibitContentPageAdapter(activity.getSupportFragmentManager(), exhibit);
    viewPager.setAdapter(pagerAdapter);

    TabPageIndicator titleIndicator = (TabPageIndicator) view.findViewById(R.id.titles);
    titleIndicator.setViewPager(viewPager);
  }
}