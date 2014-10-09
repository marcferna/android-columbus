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

  private static final long ANIM_VIEWPAGER_DELAY = 5000;
  private static final long ANIM_VIEWPAGER_DELAY_USER_VIEW = 10000;

  // UI References
  private ViewPager viewPager;
  PageIndicator pageIndicator;

  List<String> images;
  boolean stopSliding = false;

  private Runnable animateViewPager;
  private Handler handler;

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

    View view = inflater.inflate(R.layout.fragment_exhibit_header, container, false);
    viewPager = (ViewPager) view.findViewById(R.id.view_pager);
    pageIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);

    pageIndicator.setOnPageChangeListener(new PageChangeListener());
    viewPager.setOnPageChangeListener(new PageChangeListener());
    viewPager.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        v.getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction()) {

          case MotionEvent.ACTION_CANCEL:
            break;

          case MotionEvent.ACTION_UP:
            // calls when touch release on ViewPager
            if (images != null && images.size() != 0) {
              stopSliding = false;
              runnable(images.size());
              handler.postDelayed(animateViewPager,
                                     ANIM_VIEWPAGER_DELAY_USER_VIEW);
            }
            break;

          case MotionEvent.ACTION_MOVE:
            // calls when ViewPager touch
            if (handler != null && stopSliding == false) {
              stopSliding = true;
              handler.removeCallbacks(animateViewPager);
            }
            break;
        }
        return false;
      }
    });

    images = new ArrayList<String>();
    images.add("http://upload.wikimedia.org/wikipedia/commons/8/89/Field_Museum_of_Natural_History.jpg");
    images.add("http://upload.wikimedia.org/wikipedia/commons/8/89/Field_Museum_of_Natural_History.jpg");
    images.add("http://upload.wikimedia.org/wikipedia/commons/8/89/Field_Museum_of_Natural_History.jpg");
    viewPager.setAdapter(new ImageSlideAdapter(activity, images));

    pageIndicator.setViewPager(viewPager);

    return view;
  }

  @Override
  public void onResume() {
    if (images == null) {
//      sendRequest();
    } else {
      viewPager.setAdapter(new ImageSlideAdapter(activity, images));

      pageIndicator.setViewPager(viewPager);
      runnable(images.size());
      //Re-run callback
      handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
    }
    super.onResume();
  }


  @Override
  public void onPause() {
    if (handler != null) {
      //Remove callback
      handler.removeCallbacks(animateViewPager);
    }
    super.onPause();
  }


  private class PageChangeListener implements ViewPager.OnPageChangeListener {

    @Override
    public void onPageScrollStateChanged(int state) {
      if (state == ViewPager.SCROLL_STATE_IDLE) {
        if (images != null) {

        }
      }
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int arg0) {
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
  }

  public void runnable(final int size) {
    handler = new Handler();
    animateViewPager = new Runnable() {
      public void run() {
        if (!stopSliding) {
          if (viewPager.getCurrentItem() == size - 1) {
            viewPager.setCurrentItem(0);
          } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
          }
          handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
        }
      }
    };
  }
}
