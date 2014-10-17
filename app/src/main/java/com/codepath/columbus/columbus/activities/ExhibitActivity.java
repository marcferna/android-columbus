package com.codepath.columbus.columbus.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.fragments.exhibit.ExhibitContentFragment;
import com.codepath.columbus.columbus.fragments.exhibit.ExhibitHeaderFragment;
import com.codepath.columbus.columbus.models.Comment;
import com.codepath.columbus.columbus.models.Exhibit;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;


public class ExhibitActivity extends SherlockFragmentActivity {

  // Fragments
  private ExhibitHeaderFragment headerFragment;
  private ExhibitContentFragment contentFragment;

  private String exhibitId;

  private static int CREATE_COMMENT_REQUEST = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_exhibit);
    exhibitId = getIntent().getStringExtra("exhibitId");
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

  private void setFragments(Exhibit exhibit) {
    headerFragment = ExhibitHeaderFragment.newInstance(exhibit);
    contentFragment = ExhibitContentFragment.newInstance(exhibit);

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.add(R.id.headerLayout, headerFragment);
    fragmentTransaction.add(R.id.contentLayout, contentFragment);
    fragmentTransaction.commit();
  }

  private void launchCreateCommentActivity() {
    Intent intent = new Intent(this, ExhibitCreateCommentActivity.class);
    intent.putExtra("exhibitId", exhibitId);
    startActivityForResult(intent, CREATE_COMMENT_REQUEST);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getSupportMenuInflater().inflate(R.menu.exhibit, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.menu_item_create_comment) {
      // Launch the add comment intent
      launchCreateCommentActivity();
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == RESULT_OK && requestCode == CREATE_COMMENT_REQUEST) {
      // Extract the comment from the intent data
      String commentId = data.getStringExtra("commentId");
      ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
      // First try to find from the cache and only then go to network
      query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK); // or CACHE_ONLY
      // Execute the query to find the object with ID
      query.getInBackground(commentId, new GetCallback<Comment>() {
        public void done(Comment comment, ParseException e) {
          if (e == null) {
            contentFragment.addComment(comment);
            Toast.makeText(ExhibitActivity.this, "Comment posted!", Toast.LENGTH_SHORT).show();
          }
        }
      });
    }
  }
}
