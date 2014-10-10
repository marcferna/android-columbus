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

import com.codepath.columbus.columbus.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ExhibitContentFragment extends Fragment {

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
    // ViewPager and its adapters use support library
    // fragments, so use getSupportFragmentManager.
    mDemoCollectionPagerAdapter =
        new ExhibitContentPagerAdapter(activity.getSupportFragmentManager());
    viewPager = (ViewPager) v.findViewById(R.id.vpExhibitContent);
    viewPager.setAdapter(ExhibitContentPagerAdapter);
  }


  // Since this is an object collection, use a FragmentStatePagerAdapter,
  // and NOT a FragmentPagerAdapter.
  public class ExhibitContentPagerAdapter extends FragmentPagerAdapter {
    public ExhibitContentPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public int getCount() {
      return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
      return ArrayListFragment.newInstance(position);
    }
  }


}