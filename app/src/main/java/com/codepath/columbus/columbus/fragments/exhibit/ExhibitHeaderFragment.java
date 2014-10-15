package com.codepath.columbus.columbus.fragments.exhibit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.adapters.ImageSlideAdapter;
import com.codepath.columbus.columbus.models.Exhibit;
import com.codepath.columbus.columbus.utils.CirclePageIndicator;
import com.codepath.columbus.columbus.utils.PageIndicator;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ExhibitHeaderFragment extends ExhibitFragment {

  // UI References
  private ViewPager viewPager;
  private PageIndicator pageIndicator;

  public static ExhibitHeaderFragment newInstance(Exhibit exhibit) {
    ExhibitHeaderFragment fragment = new ExhibitHeaderFragment();
    fragment.init(exhibit);
    return fragment;
  }

  public ExhibitHeaderFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_exhibit_header, container, false);
    setImageCarousel(view);
    return view;
  }

  public void setImageCarousel(View view) {
    List<String> images = exhibit.getImageUrls();

    if (images != null && images.size() > 0) {
      viewPager = (ViewPager) view.findViewById(R.id.view_pager);
      viewPager.setAdapter(new ImageSlideAdapter(activity, images));
    }

    if (images != null && images.size() > 1) {
      pageIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
      pageIndicator.setViewPager(viewPager, 0);
    }
  }
}
