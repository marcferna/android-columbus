package com.codepath.columbus.columbus.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

@ParseClassName("Exhibit")
public class Exhibit extends ParseObject {

    private String objectId;
    private String name;
    private String descriptionLong;
    private String descriptionShort;
    private ArrayList<String> imageUrls;
    private String beaconId;
    private int distance;

    @Override
    public String getObjectId() {
        return getString("objectId");
    }

    @Override
    public void setObjectId(String objectId) {
        put("objectId", objectId);
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

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public Exhibit() {
        super();
        imageUrls = new ArrayList<String>();
    }

    /* Method to init test data */
    public static Exhibit dummyObject(int distance) {
        Exhibit exhibit = new Exhibit();
        exhibit.name = "Exhibit name";
        exhibit.descriptionShort = "Here is a short description of this exhibit, which will talk about it's creation, " +
                "it's history and its' value proposition";
        exhibit.imageUrls.add("http://upload.wikimedia.org/wikipedia/commons/8/89/Field_Museum_of_Natural_History.jpg");
        exhibit.distance = distance;
        return exhibit;
    }

}



