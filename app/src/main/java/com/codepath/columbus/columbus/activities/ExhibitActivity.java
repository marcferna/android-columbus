package com.codepath.columbus.columbus.activities;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.View;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.fragments.exhibit.ExhibitContentFragment;
import com.codepath.columbus.columbus.fragments.exhibit.ExhibitHeaderFragment;
import com.codepath.columbus.columbus.models.Comment;
import com.codepath.columbus.columbus.models.Exhibit;
import com.codepath.columbus.columbus.services.MusicService;
import com.codepath.columbus.columbus.utils.MusicController;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class ExhibitActivity extends SherlockFragmentActivity implements MediaPlayerControl, MusicService.MusicServiceCallbacks {

  // Fragments
  private ExhibitHeaderFragment headerFragment;
  private ExhibitContentFragment contentFragment;

  // UI Elements
  private ProgressBar pbBufferProgress;
  private TextView tvLoadingMediaPlayer;

  private String exhibitId;
  private String exhibitName;
  private Exhibit exhibit;

  private MusicController controller;
  private MusicService musicService;
  private Intent playIntent;
  private boolean musicBound = false;
  private boolean paused = false;

  private static int CREATE_COMMENT_REQUEST = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_exhibit);
    exhibitId = getIntent().getStringExtra("exhibitId");
    exhibitName = getIntent().getStringExtra("exhibitName");
    setActionBar();
    setViews();
    fetchExhibit();
    setController();
  }

  @Override
  public void onPause() {
    super.onPause();
    paused = true;
  }

  @Override
  public void onResume() {
    super.onResume();
    if(paused){
      setController();
      paused = false;
    }
  }

  @Override
  public void onStop() {
    controller.hide();
    super.onStop();
  }

  @Override
  public void onDestroy() {
    unbindService(musicConnection);
    stopService(playIntent);
    controller.hide();
    musicService = null;
    super.onDestroy();
  }

  public void setActionBar() {
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    String title = "<font color=\""+getResources().getColor(R.color.actionbar_title_color)+"\">"+exhibitName+"</font>";
    actionBar.setTitle(Html.fromHtml(title));
  }

  public void setViews() {
    pbBufferProgress = (ProgressBar) findViewById(R.id.pbMediaPlayerPreparation);
    tvLoadingMediaPlayer = (TextView) findViewById(R.id.tvLoadingMediaPlayer);
  }

  public void fetchExhibit() {
    ParseQuery<Exhibit> query = ParseQuery.getQuery(Exhibit.class);
    query.whereEqualTo("objectId", exhibitId);
    query.getFirstInBackground(new GetCallback<Exhibit>() {
      @Override
      public void done(Exhibit result, ParseException e) {
        if (e == null) {
          exhibit = result;
          setFragments();
          setMusicService();
        } else {
          e.printStackTrace();
        }
      }
    });
  }

  private void setMusicService() {
    if(playIntent == null && exhibit != null){
      playIntent = new Intent(ExhibitActivity.this, MusicService.class);
      bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
      startService(playIntent);
    }
  }

  private void setFragments() {
    headerFragment = ExhibitHeaderFragment.newInstance(exhibit);
    contentFragment = ExhibitContentFragment.newInstance(exhibit);

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.headerLayout, headerFragment);
    fragmentTransaction.replace(R.id.contentLayout, contentFragment);
    fragmentTransaction.commit();
  }

  private void setController(){
    controller = new MusicController(this);
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
    switch (id) {
      case R.id.menu_item_create_comment:
        // Launch the add comment intent
        launchCreateCommentActivity();
        return true;

      case android.R.id.home:
        // app icon in action bar clicked; goto parent activity.
        this.finish();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void launchCreateCommentActivity() {
    Intent intent = new Intent(this, ExhibitCreateCommentActivity.class);
    intent.putExtra("exhibitId", exhibitId);
    startActivityForResult(intent, CREATE_COMMENT_REQUEST);
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
          } else {
            Toast.makeText(ExhibitActivity.this, "Sorry, there was an error posting your comment, please try again.", Toast.LENGTH_SHORT).show();
          }
        }
      });
    }
  }

  private ServiceConnection musicConnection = new ServiceConnection(){

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
      // get service
      musicService = binder.getService();
      musicBound = true;

      // set the binder properties
      musicService.setCallbacks(ExhibitActivity.this);
      musicService.setExhibitId(exhibitId);
      musicService.setExhibitName(exhibit.getName());


      // play the audio guide
      musicService.playSong(exhibit.getAudioUrl());
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      musicBound = false;
    }
  };

  // Music service callbacks
  @Override
  public void mediaPlayerPrepared() {
    // hide progress elements
    hideLoadingElements();

    // setup the controller
    controller.setMediaPlayer(this);
    controller.setAnchorView(findViewById(R.id.playerLayout));

    // show the controller
    Handler handler = new Handler();
    handler.post(new Runnable() {
      public void run() {
        controller.setEnabled(true);
        controller.show();
      }
    });
  }

  private void hideLoadingElements() {
    // hide the progress bar
    pbBufferProgress.setVisibility(View.GONE);
    // hide the loading text
    tvLoadingMediaPlayer.setVisibility(View.GONE);
  }

  // Media player controls

  @Override
  public void start() {
    musicService.start();
  }

  @Override
  public void pause() {
    musicService.pause();
  }

  @Override
  public int getDuration() {
    if(musicService != null && musicBound && musicService.isPlaying()) {
      return musicService.getDuration();
    }
    return 0;
  }

  @Override
  public int getCurrentPosition() {
    if(musicService != null && musicBound && musicService.isPlaying()) {
      return musicService.getCurrentPosition();
    }
    return 0;
  }

  @Override
  public void seekTo(int pos) {
    musicService.seekTo(pos);
  }

  @Override
  public boolean isPlaying() {
    if (musicService != null && musicBound) {
      return musicService.isPlaying();
    }
    return false;
  }

  @Override
  public int getBufferPercentage() {
    return 0;
  }

  @Override
  public boolean canPause() {
    return true;
  }

  @Override
  public boolean canSeekBackward() {
    return false;
  }

  @Override
  public boolean canSeekForward() {
    return false;
  }

  @Override
  public int getAudioSessionId() {
    return 0;
  }
}
