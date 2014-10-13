package com.codepath.columbus.columbus.fragments.exhibit;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.adapters.ExhibitContentPageAdapter;
import com.viewpagerindicator.TabPageIndicator;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ExhibitContentFragment extends SherlockFragment {

  ExhibitContentPageAdapter pagerAdapter;

  static final int NUM_ITEMS = 10;

  private FragmentActivity activity;
  private ViewPager viewPager;

  public ExhibitContentFragment() {
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
      // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_exhibit_content, container, false);

    viewPager = (ViewPager) v.findViewById(R.id.vpPager);
    pagerAdapter = new ExhibitContentPageAdapter(activity.getSupportFragmentManager());
    viewPager.setAdapter(pagerAdapter);

    TabPageIndicator titleIndicator = (TabPageIndicator) v.findViewById(R.id.titles);
    titleIndicator.setViewPager(viewPager);

    return v;
  }
}