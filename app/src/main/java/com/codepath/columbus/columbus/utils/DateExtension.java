package com.codepath.columbus.columbus.utils;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by marc on 10/19/14.
 */
public class DateExtension extends Date {

  private Date date;

  public DateExtension(Date date) {
    this.date = date;
  }

  // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
  // Mon Oct 20 00:17:43 GMT+00:00 2014
  public String getRelativeTimeAgo(String dateFormat) {
    //String twitterFormat = "EEE MMM d H:m:s ZZZZZ yyyy";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
    simpleDateFormat.setLenient(true);

    String relativeDate = this.date.toString();
    try {
      long dateMillis = simpleDateFormat.parse(this.date.toString()).getTime();
      relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS)
                              .toString();
    } catch (ParseException e) {
      e.printStackTrace();
    }

    relativeDate = relativeDate.replace(" seconds ago", "s");
    relativeDate = relativeDate.replace(" second ago", "s");
    relativeDate = relativeDate.replace(" minutes ago", "m");
    relativeDate = relativeDate.replace(" minute ago", "m");
    relativeDate = relativeDate.replace(" hours ago", "h");
    relativeDate = relativeDate.replace(" hour ago", "h");
    relativeDate = relativeDate.replace(" days ago", "d");
    relativeDate = relativeDate.replace(" day ago", "d");
    relativeDate = relativeDate.replace(" weeks ago", "w");
    relativeDate = relativeDate.replace(" week ago", "w");

    return relativeDate;
  }
}
