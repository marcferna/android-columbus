package com.codepath.columbus.columbus.adapters;

/**
 * Created by marc on 10/9/14.
 */
import java.util.List;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.codepath.columbus.columbus.R;
import com.nostra13.universalimageloader.core.ImageLoader;



public class ImageSlideAdapter extends PagerAdapter {
  ImageLoader imageLoader = ImageLoader.getInstance();
  FragmentActivity activity;
  List<String> images;

  public ImageSlideAdapter(FragmentActivity activity, List<String> images) {
    this.activity = activity;
    this.images = images;
  }

  @Override
  public int getCount() {
    return images.size();
  }

  @Override
  public View instantiateItem(ViewGroup container, final int position) {
    LayoutInflater inflater = (LayoutInflater) activity
                                                   .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    View view = inflater.inflate(R.layout.image_carousel_item, container, false);

    ImageView mImageView = (ImageView) view.findViewById(R.id.image_display);


    imageLoader.displayImage(images.get(position), mImageView);
    container.addView(view);
    return view;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((View) object);
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }
}
