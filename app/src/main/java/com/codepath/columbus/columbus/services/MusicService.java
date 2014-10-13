package com.codepath.columbus.columbus.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.app.Notification;
import android.app.PendingIntent;

import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.activities.ExhibitActivity;

/**
 * Created by marc on 10/13/14.
 */
public class MusicService extends Service implements
    MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener {

  //media player
  private MediaPlayer player;

  private final IBinder musicBinder = new MusicBinder();

  private static final int NOTIFY_ID = 1;

  private MusicServiceCallbacks callbacks;

  // Exhibit properties
  private String exhibitId;
  private String exhibitName;

  @Override
  public void onCreate(){
    super.onCreate();
    player = new MediaPlayer();
    initMusicPlayer();
  }

  @Override
  public void onDestroy() {
    stopForeground(true);
  }

  public void initMusicPlayer(){
    //set player properties
    player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
    player.setAudioStreamType(AudioManager.STREAM_MUSIC);

    player.setOnPreparedListener(this);
    player.setOnCompletionListener(this);
    player.setOnErrorListener(this);
  }

  @Override
  public IBinder onBind(Intent arg0) {
    return musicBinder;
  }

  @Override
  public boolean onUnbind(Intent intent){
    player.stop();
    player.release();
    return false;
  }

  @Override
  public void onCompletion(MediaPlayer mp) {
    // Dismiss the ongoing notification
  }

  @Override
  public boolean onError(MediaPlayer mp, int what, int extra) {
    mp.reset();
    return false;
  }

  @Override
  public void onPrepared(MediaPlayer mp) {
    Intent notIntent = new Intent(this, ExhibitActivity.class);
    notIntent.putExtra("exhibitId", exhibitId);

    notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    PendingIntent pendInt = PendingIntent.getActivity(this, 0, notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    Notification.Builder builder = new Notification.Builder(this);
    builder.setContentIntent(pendInt)
           .setSmallIcon(R.drawable.ic_launcher)
           .setTicker(exhibitName)
           .setOngoing(true)
           .setContentTitle("Playing Audio Tour")
           .setContentText(exhibitName);

    Notification not = builder.build();

    startForeground(NOTIFY_ID, not);
    callbacks.mediaPlayerPrepared();
  }

  public void setCallbacks(MusicServiceCallbacks callbacks) {
    this.callbacks = callbacks;
  }

  public void playSong(String songUrl){
    player.reset();
    Uri songUri = Uri.parse(songUrl);
    try{
      player.setDataSource(getApplicationContext(), songUri);
    }
    catch(Exception e){
      Log.e("MUSIC SERVICE", "Error setting data source", e);
    }
    player.prepareAsync();
  }

  public void setExhibitId(String exhibitId) {
    this.exhibitId = exhibitId;
  }

  public void setExhibitName(String exhibitName) {
    this.exhibitName = exhibitName;
  }

  public class MusicBinder extends Binder {
    public MusicService getService() {
      return MusicService.this;
    }
  }

  public int getCurrentPosition(){
    return player.getCurrentPosition();
  }

  public int getDuration(){
    return player.getDuration();
  }

  public boolean isPlaying(){
    return player.isPlaying();
  }

  public void pause(){
    player.pause();
  }

  public void seekTo(int posn){
    player.seekTo(posn);
  }

  public void start(){
    player.start();
  }

  public interface MusicServiceCallbacks {
    void mediaPlayerPrepared();
  }
}
