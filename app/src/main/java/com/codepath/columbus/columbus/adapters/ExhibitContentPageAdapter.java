package com.codepath.columbus.columbus.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.columbus.columbus.fragments.exhibit.ExhibitCommentsFragment;
import com.codepath.columbus.columbus.fragments.exhibit.ExhibitDescriptionFragment;
import com.codepath.columbus.columbus.fragments.exhibit.ExhibitFragment;
import com.codepath.columbus.columbus.models.Exhibit;

import java.util.List;

/**
 * Created by marc on 10/12/14.
 */
public class ExhibitContentPageAdapter extends FragmentPagerAdapter {

  private List<ExhibitFragment> contentFragments;

  public ExhibitContentPageAdapter(FragmentManager fm, List<ExhibitFragment> contentFragments) {
    super(fm);
    this.contentFragments = contentFragments;
  }

  @Override
  public Fragment getItem(int i) {
    if (i < contentFragments.size()) {
      return contentFragments.get(i);
    }
    return null;
  }

  @Override
  public int getCount() {
    return contentFragments.size();
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
