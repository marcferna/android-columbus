package com.codepath.columbus.columbus.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.models.Comment;
import com.codepath.columbus.columbus.models.Exhibit;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class ExhibitCreateCommentActivity extends Activity {

  SharedPreferences sharedPreferences;

  String exhibitId;
  Exhibit exhibit;

  // UI Elements
  RatingBar rbExhibitRating;
  TextView tvCommentBody;
  Button btPost;
  Button btCancel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_exhibit_create_comment);
    exhibitId = getIntent().getStringExtra("exhibitId");
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    fetchExhibit();
    setViews();
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
    btPost = (Button) findViewById(R.id.btPost);
    btPost.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        postCommentAction();
      }
    });

    btCancel = (Button) findViewById(R.id.btCancel);
    btCancel.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        cancelCommentAction();
      }
    });
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

  private void cancelCommentAction() {
    Intent data = new Intent();
    setResult(RESULT_CANCELED, data);
    finish();
  }

  private String getUserName() {
    return sharedPreferences.getString("username", "Unknown");
  }


  public String getUserAvatar() {
    return sharedPreferences.getString("imageURL", "https://lh4.googleusercontent.com/-KJFl04DeV8Y/AAAAAAAAAAI/AAAAAAAAAAA/YQZ6Fv1VWzw/photo.jpg");
  }
}
