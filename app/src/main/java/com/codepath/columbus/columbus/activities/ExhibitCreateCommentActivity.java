package com.codepath.columbus.columbus.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.models.Comment;
import com.codepath.columbus.columbus.models.Exhibit;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class ExhibitCreateCommentActivity extends SherlockActivity {

  SharedPreferences sharedPreferences;

  String exhibitId;
  Exhibit exhibit;

  // UI Elements
  RatingBar rbExhibitRating;
  TextView tvCommentBody;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_exhibit_create_comment);
    exhibitId = getIntent().getStringExtra("exhibitId");
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    fetchExhibit();
    setViews();
    setActionBar();
  }

  public void setActionBar() {
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setTitle("Comment");
  }

  private void fetchExhibit() {
    ParseQuery<Exhibit> query = ParseQuery.getQuery(Exhibit.class);
    // First try to find from the cache and only then go to network
    query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK); // or CACHE_ONLY
    // Execute the query to find the object with ID
    query.getInBackground(exhibitId, new GetCallback<Exhibit>() {
      public void done(Exhibit exhibit, ParseException e) {
        if (e == null) {
          ExhibitCreateCommentActivity.this.exhibit = exhibit;
          setViews();
        }
      }
    });
  }

  private void setViews() {
    rbExhibitRating = (RatingBar) findViewById(R.id.rbExhibitRating);
    tvCommentBody = (TextView) findViewById(R.id.tvCommentBody);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getSupportMenuInflater().inflate(R.menu.create_comment, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    switch (id) {
      case R.id.menu_item_submit_comment:
        // Launch the add comment intent
        postCommentAction();
        return true;

      case android.R.id.home:
        // app icon in action bar clicked; goto parent activity.
        this.finish();
        overridePendingTransition(R.anim.zoom_in, R.anim.slide_out_right);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    overridePendingTransition(R.anim.zoom_in, R.anim.slide_out_right);
  }

  private void postCommentAction() {
    final Comment comment = new Comment();
    comment.setCommentBody(tvCommentBody.getText().toString());
    comment.setRating(rbExhibitRating.getRating());
    comment.setName(getUserName());
    comment.setGoogleUserAvatarUrl(getUserAvatar());
    comment.setExhibit(exhibit);
    comment.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        if (e == null) {
          Intent data = new Intent();
          data.putExtra("commentId", comment.getObjectId());
          setResult(RESULT_OK, data);
          finish();
        } else {
          e.printStackTrace();
        }
      }
    });
  }

  private String getUserName() {
    return sharedPreferences.getString("username", "Unknown");
  }

  public String getUserAvatar() {
    return sharedPreferences.getString("imageURL", "https://lh4.googleusercontent.com/-KJFl04DeV8Y/AAAAAAAAAAI/AAAAAAAAAAA/YQZ6Fv1VWzw/photo.jpg");
  }
}
