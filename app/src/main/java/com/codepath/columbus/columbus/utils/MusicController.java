package com.codepath.columbus.columbus.utils;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.MediaController;

/**
 * Created by marc on 10/16/14.
 */
public class MusicController extends MediaController {

  public MusicController(Context c) {
    super(c);
  }

  public void hide() {}

  @Override
  public boolean dispatchKeyEvent(KeyEvent event) {
    int keyCode = event.getKeyCode();
    if(keyCode == KeyEvent.KEYCODE_BACK){
      ((Activity) getContext()).finish();
      return true;
    }
    return super.dispatchKeyEvent(event);
  }
}
