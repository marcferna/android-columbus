package com.codepath.columbus.columbus.fragments.exhibit;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.columbus.columbus.R;
import com.codepath.columbus.columbus.adapters.CommentAdapter;
import com.codepath.columbus.columbus.models.Comment;
import com.codepath.columbus.columbus.models.Exhibit;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ExhibitCommentsFragment extends ExhibitFragment {

  // Adapter
  private CommentAdapter commentAdapter;

  // UI Elements
  private ListView lvComments;

  public static ExhibitCommentsFragment newInstance(Exhibit exhibit) {
    ExhibitCommentsFragment fragment = new ExhibitCommentsFragment();
    fragment.init(exhibit);
    return fragment;
  }

  public ExhibitCommentsFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_exhibit_comments, container, false);
    setCommentsListVew(view);
    fetchComments();
    return view;
  }

  public void setCommentsListVew(View view) {
    lvComments = (ListView) view.findViewById(R.id.lvComments);
    commentAdapter = new CommentAdapter(activity, new ArrayList<Comment>());
    commentAdapter.notifyDataSetChanged();
    lvComments.setAdapter(commentAdapter);
  }

  public void fetchComments() {
    ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
    query.whereEqualTo("exhibit", exhibit);
    query.orderByDescending("createdAt");
    query.findInBackground(new FindCallback<Comment>() {
      @Override
      public void done(List<Comment> results, ParseException e) {
        if (e == null) {
          commentAdapter.clear();
          commentAdapter.addAll(results);
        } else {
          e.printStackTrace();
        }
      }
    });
  }

  public void addComment(Comment comment) {
    commentAdapter.insert(comment, 0);
  }
}
