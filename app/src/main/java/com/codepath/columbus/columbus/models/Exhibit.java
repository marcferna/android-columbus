package com.codepath.columbus.columbus.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public class Exhibit extends Object {

    private String exhibitId;
    private String name;
    private String shortDescription;
    private ArrayList<String> imageUrls;
    private int distance;

    public String getExhibitId() {
        return exhibitId;
    }

    public String getName() {
        return name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public List<String> getImageUrls() {
        return imageUrls;
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
        exhibit.name = "This is exhibit name";
        exhibit.shortDescription = "Here is a short description of this exhibit, which will talk about it's creation, " +
                "it's history and its' value proposition";
        exhibit.imageUrls.add("http://upload.wikimedia.org/wikipedia/commons/8/89/Field_Museum_of_Natural_History.jpg");
        exhibit.distance = distance;
        return exhibit;
    }

}



