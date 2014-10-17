package com.codepath.columbus.columbus.fragments.exhibit;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.actionbarsherlock.app.SherlockFragment;
import com.codepath.columbus.columbus.models.Exhibit;

/**
 * Created by marc on 10/14/14.
 */
public abstract class ExhibitFragment extends SherlockFragment {

  static String EXHIBIT_BUNDLE_KEY = "exhibit";

  protected Exhibit exhibit;
  protected FragmentActivity activity;

  protected void init(Exhibit exhibit) {
    Bundle bundle = new Bundle();
    bundle.putParcelable(EXHIBIT_BUNDLE_KEY, exhibit);
    this.setArguments(bundle);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
    exhibit = getArguments().getParcelable(EXHIBIT_BUNDLE_KEY);
  }
}
