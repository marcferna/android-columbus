package com.codepath.columbus.columbus.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Exhibit")
public class Exhibit extends ParseObject implements Parcelable {

    private String name;
    private String descriptionLong;
    private String descriptionShort;
    private ArrayList<String> imageUrls;
    private String beaconId;
    private long ratingAverage;
    private double distance;

    public Exhibit() {

    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getDescriptionLong() {
        return getString("descriptionLong");
    }

    public void setDescriptionLong(String descriptionLong) {
        put("descriptionLong", descriptionLong);
    }

    public String getDescriptionShort() {
        return getString("descriptionShort");
    }

    public void setDescriptionShort(String descriptionShort) {
        put("descriptionShort", descriptionShort);
    }

    public List<String> getImageUrls() {
        return getList("imageUrls");
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        put("imageUrls", imageUrls);
    }

    public String getBeaconId() {
        return getString("beaconId");
    }

    public void setBeaconId(String beaconId) {
        put("beaconId", beaconId);
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

  /* Method to init test data */
  public static Exhibit dummyObject(int distance) {
    Exhibit exhibit = (Exhibit) ParseObject.create("Exhibit");
    exhibit.setName("This is exhibit name");
    exhibit.setDescriptionShort("Here is a short description of this exhibit, which will talk about it's creation, " +
            "it's history and its' value proposition");
    exhibit.setDescriptionLong("Here is a short description of this exhibit, which will talk about it's creation, " +
                                    "it's history and its' value proposition");
    ArrayList<String> images = new ArrayList<String>();
    images.add("http://upload.wikimedia.org/wikipedia/commons/8/89/Field_Museum_of_Natural_History.jpg");
    exhibit.setImageUrls(images);
    return exhibit;
  }

  /* Make Exhibit implement Parcelable */

  public Exhibit(Parcel in){
    name = in.readString();
    descriptionShort = in.readString();
    descriptionLong = in.readString();
    imageUrls = in.readArrayList(String.class.getClassLoader());
    beaconId = in.readString();
    ratingAverage = in.readLong();
  }

  @Override
  public int describeContents(){
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int flags) {
    parcel.writeString(name);
    parcel.writeString(descriptionShort);
    parcel.writeString(descriptionLong);
    parcel.writeStringList(imageUrls);
    parcel.writeString(beaconId);
    parcel.writeLong(ratingAverage);
  }

  public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
    public Exhibit createFromParcel(Parcel in) {
      return new Exhibit(in);
    }

    public Exhibit[] newArray(int size) {
      return new Exhibit[size];
    }
  };
}



