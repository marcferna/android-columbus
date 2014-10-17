package com.codepath.columbus.columbus.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;

import java.util.Date;

/**
 * Created by marc on 10/14/14.
 */
@ParseClassName("Comment")
public class Comment extends ParseObject {

  private String name;

  private String googleUserAvatarUrl;

  private String commentBody;

  private float rating;

  private Date createdAt;

  private Exhibit exhibit;

  public Comment() {

  }

  public String getName() {
    return getString("name");
  }

  public void setName(String name) {
    put("name", name);
  }

  public String getGoogleUserAvatarUrl() {
    return getString("googleUserAvatarUrl");
  }

  public void setGoogleUserAvatarUrl(String googleUserAvatarUrl) {
    put("googleUserAvatarUrl", googleUserAvatarUrl);
  }

  public String getCommentBody() {
    return getString("commentBody");
  }

  public void setCommentBody(String commentBody) {
    put("commentBody", commentBody);
  }

  public float getRating() {
    return getLong("rating");
  }

  public void setRating(float rating) {
    put("rating", rating);
  }

  public Date getCreatedAt() {
    return getDate("imageUrl");
  }

  public ParseRelation<Exhibit> getExhibit() {
    return getRelation("exhibit");
  }

  public void setExhibit(Exhibit exhibit) {
    put("exhibit", exhibit);
  }
}
