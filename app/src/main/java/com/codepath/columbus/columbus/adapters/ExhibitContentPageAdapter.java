package com.codepath.columbus.columbus.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.columbus.columbus.fragments.exhibit.ExhibitCommentsFragment;
import com.codepath.columbus.columbus.fragments.exhibit.ExhibitDescriptionFragment;
import com.codepath.columbus.columbus.models.Exhibit;

/**
 * Created by marc on 10/12/14.
 */
public class ExhibitContentPageAdapter extends FragmentPagerAdapter {

  private static int NUM_ITEMS = 2;

  // Fragments
  private ExhibitDescriptionFragment descriptionFragment;
  private ExhibitCommentsFragment commentsFragment;

  private Exhibit exhibit;

  public ExhibitContentPageAdapter(FragmentManager fm, Exhibit exhibit) {
    super(fm);
    this.exhibit = exhibit;
  }

  @Override
  public Fragment getItem(int i) {
    switch(i){
      case 0:
        if (descriptionFragment == null){
          descriptionFragment = ExhibitDescriptionFragment.newInstance(exhibit);
        }
        return descriptionFragment;
      case 1:
        if (commentsFragment == null){
          commentsFragment = ExhibitCommentsFragment.newInstance(exhibit);
        }
        return commentsFragment;
      default:
        return null;
    }
  }

  @Override
  public int getCount() {
    return NUM_ITEMS;
  }

  @Override
  public CharSequence getPageTitle(int position) {
    switch(position){
      case 0:
        return "DESCRIPTION";
      case 1:
        return "COMMENTS";
    }
    return "";
  }
}
