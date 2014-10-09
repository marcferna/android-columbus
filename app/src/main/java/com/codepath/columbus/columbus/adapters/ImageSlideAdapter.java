package com.codepath.columbus.columbus.adapters;

/**
 * Created by marc on 10/9/14.
 */
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.codepath.columbus.columbus.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;


public class ImageSlideAdapter extends PagerAdapter {
  ImageLoader imageLoader = ImageLoader.getInstance();
  DisplayImageOptions options;
  private ImageLoadingListener imageListener;
  FragmentActivity activity;
  List<String> images;

  public ImageSlideAdapter(FragmentActivity activity, List<String> images) {
    this.activity = activity;
    this.images = images;
    options = new DisplayImageOptions.Builder()
                  .showImageOnFail(R.drawable.ic_launcher) // TODO: change this
                  .showStubImage(R.drawable.ic_launcher)
                  .showImageForEmptyUri(R.drawable.ic_launcher).cacheInMemory() // TODO: change this
                  .cacheOnDisc().build();

    imageListener = new ImageDisplayListener();
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


    imageLoader.displayImage(images.get(position), mImageView, options, imageListener);
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

  private static class ImageDisplayListener extends
      SimpleImageLoadingListener {

    static final List<String> displayedImages = Collections
                                                    .synchronizedList(new LinkedList<String>());

    @Override
    public void onLoadingComplete(String imageUri, View view,
                                  Bitmap loadedImage) {
      if (loadedImage != null) {
        ImageView imageView = (ImageView) view;
        boolean firstDisplay = !displayedImages.contains(imageUri);
        if (firstDisplay) {
          FadeInBitmapDisplayer.animate(imageView, 500);
          displayedImages.add(imageUri);
        }
      }
    }
  }
}
