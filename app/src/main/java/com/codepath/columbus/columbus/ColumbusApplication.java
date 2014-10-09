package com.codepath.columbus.columbus;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.StrictMode;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * Created by marc on 10/9/14.
 */
public class ColumbusApplication extends Application{

  public static final boolean DEVELOPER_MODE = false;

  @SuppressWarnings("unused")
  public void onCreate() {
    super.onCreate();
    if (DEVELOPER_MODE
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
      StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                                     .detectAll().penaltyDialog().build());
      StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                                 .detectAll().penaltyDeath().build());
    }

    initImageLoader(getApplicationContext());
  }

  public static void initImageLoader(Context context) {
    DisplayImageOptions options = new DisplayImageOptions.Builder()
                                      .showImageForEmptyUri(R.drawable.ic_launcher) // TODO: Change this
                                      .showImageOnFail(R.drawable.ic_launcher).resetViewBeforeLoading() // TODO: Change this
                                      .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY)
                                      .bitmapConfig(Bitmap.Config.RGB_565)
                                      .displayer(new FadeInBitmapDisplayer(300)).build();

    ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2)
                                          .denyCacheImageMultipleSizesInMemory()
                                          .discCacheFileNameGenerator(new Md5FileNameGenerator())
                                          .defaultDisplayImageOptions(options)
                                          .tasksProcessingOrder(QueueProcessingType.LIFO).build();

    ImageLoader.getInstance().init(config);
  }
}
