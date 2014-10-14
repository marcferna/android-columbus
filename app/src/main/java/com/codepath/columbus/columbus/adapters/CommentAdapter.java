package com.codepath.columbus.columbus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.models.Comment;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by marc on 10/14/14.
 */
public class CommentAdapter extends ArrayAdapter<Comment> {

  private static class ViewHolder {
    ImageView ivProfile;
    TextView tvUsername;
    TextView tvCreatedAt;
    TextView tvComment;
    RatingBar rbRating;
  }

  public CommentAdapter(Context context, ArrayList<Comment> comments) {
    super(context, 0, comments);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // Get the data item for this position
    Comment comment = getItem(position);

    ViewHolder viewHolder;
    if (convertView == null) {
      viewHolder = new ViewHolder();
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_comment, parent, false);
      // Get resources from view to populate
      viewHolder.tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
      viewHolder.tvCreatedAt = (TextView) convertView.findViewById(R.id.tvCreatedAt);
      viewHolder.tvComment = (TextView) convertView.findViewById(R.id.tvComment);
      viewHolder.rbRating = (RatingBar) convertView.findViewById(R.id.rbRating);
      viewHolder.ivProfile = (ImageView) convertView.findViewById(R.id.ivProfile);
      convertView.setTag(viewHolder);
    }
    else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    // Clear recycled view
    viewHolder.ivProfile.setImageResource(0);

    // Populate resources
    ImageLoader imageLoader = ImageLoader.getInstance();
    imageLoader.displayImage(comment.getGoogleUserAvatarUrl(), viewHolder.ivProfile);
    viewHolder.tvUsername.setText(comment.getName());
//    viewHolder.tvCreatedAt.setText(comment.getCreatedAt().toString());
    viewHolder.tvComment.setText(comment.getCommentBody());
    viewHolder.rbRating.setRating(comment.getRating());

    return convertView;
  }

}
