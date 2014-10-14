package com.codepath.columbus.columbus.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

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

  public void setCreatedAt(Date createdAt) {
    put("createdAt", createdAt);
  }
}
