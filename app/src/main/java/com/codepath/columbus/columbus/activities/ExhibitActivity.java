package com.codepath.columbus.columbus.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.fragments.exhibit.ExhibitContentFragment;
import com.codepath.columbus.columbus.fragments.exhibit.ExhibitHeaderFragment;
import com.codepath.columbus.columbus.models.Exhibit;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;


public class ExhibitActivity extends SherlockFragmentActivity {

  // Fragments
  private ExhibitHeaderFragment headerFragment;
  private ExhibitContentFragment contentFragment;

  private String exhibitId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_exhibit);
    exhibitId = "mGsoKsYPuF";
    fetchExhibit();
  }

  public void fetchExhibit() {
    ParseQuery<Exhibit> query = ParseQuery.getQuery(Exhibit.class);
    query.whereEqualTo("objectId", exhibitId);
    query.getFirstInBackground(new GetCallback<Exhibit>() {
      @Override
      public void done(Exhibit result, ParseException e) {
        if (e == null) {
          setFragments(result);
        } else {
          e.printStackTrace();
        }
      }
    });
  }

  public void setFragments(Exhibit exhibit) {
    headerFragment = ExhibitHeaderFragment.newInstance(exhibit);
    contentFragment = ExhibitContentFragment.newInstance(exhibit);

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.add(R.id.headerLayout, headerFragment);
    fragmentTransaction.add(R.id.contentLayout, contentFragment);
    fragmentTransaction.commit();
  }
}
