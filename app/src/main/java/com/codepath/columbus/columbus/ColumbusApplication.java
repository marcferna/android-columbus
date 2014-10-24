package com.codepath.columbus.columbus;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.StrictMode;

import com.codepath.columbus.columbus.models.Comment;
import com.codepath.columbus.columbus.models.Exhibit;
import com.codepath.columbus.columbus.models.Museum;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.parse.Parse;
import com.parse.ParseObject;

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

    setupParse();
  }

  public static void initImageLoader(Context context) {
    DisplayImageOptions options = new DisplayImageOptions.Builder()
                                      .showImageForEmptyUri(R.drawable.icon_launcher_hollow) // TODO: Change this
                                      .showImageOnFail(R.drawable.icon_error).resetViewBeforeLoading() // TODO: Change this
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

  public void setupParse(){
    // Register parse models
    ParseObject.registerSubclass(Museum.class);
    ParseObject.registerSubclass(Exhibit.class);
    ParseObject.registerSubclass(Comment.class);

    // initialize parse with columbus application id/client_key
    Parse.initialize(this,"OAQsicQdL1q6JImFqg0bwAO5fxCzRYYFTXrzF1ih","PqgCWGd36DNxEzQmazyBManGsJZs4RPyKHn2QUd2");
  }
}
