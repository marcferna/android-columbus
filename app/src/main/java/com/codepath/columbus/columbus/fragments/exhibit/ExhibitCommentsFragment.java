package com.codepath.columbus.columbus.fragments.exhibit;



import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.adapters.CommentAdapter;
import com.codepath.columbus.columbus.models.Comment;
import com.codepath.columbus.columbus.models.Museum;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ExhibitCommentsFragment extends SherlockFragment {

  private ListView lvComments;
  private ArrayList<Comment> comments;
  private CommentAdapter commentAdapter;

  private Activity activity;

  public ExhibitCommentsFragment() {
      // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_exhibit_comments, container, false);
    lvComments = (ListView) v.findViewById(R.id.lvComments);
    comments = new ArrayList<Comment>();
    commentAdapter = new CommentAdapter(activity, comments);
    commentAdapter.notifyDataSetChanged();
    lvComments.setAdapter(commentAdapter);

    ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
    query.findInBackground(new FindCallback<Comment>() {
      @Override
      public void done(List<Comment> results, ParseException e) {
        commentAdapter.clear();

        commentAdapter.addAll(results);
      }
    });


    return v;
  }


}
