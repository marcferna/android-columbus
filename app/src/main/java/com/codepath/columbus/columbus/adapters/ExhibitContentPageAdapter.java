package com.codepath.columbus.columbus.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.columbus.columbus.fragments.exhibit.ExhibitCommentsFragment;
import com.codepath.columbus.columbus.fragments.exhibit.ExhibitDescriptionFragment;

/**
 * Created by marc on 10/12/14.
 */
public class ExhibitContentPageAdapter extends FragmentPagerAdapter {

  private static int NUM_ITEMS = 2;
  public ExhibitDescriptionFragment descriptionFragment;
  public ExhibitCommentsFragment commentsFragment;

  public ExhibitContentPageAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int i) {
    switch(i){
      case 0:
        if (descriptionFragment == null){
          descriptionFragment = new ExhibitDescriptionFragment();
        }
        return descriptionFragment;
      case 1:
        if (commentsFragment == null){
          commentsFragment = new ExhibitCommentsFragment();
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
