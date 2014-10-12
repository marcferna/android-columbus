package com.codepath.columbus.columbus.models;

import java.util.ArrayList;
import java.util.List;

public class Exhibit extends Object {
    private String exhibitId;
    private String name;
    private String shortDescription;
    private ArrayList<String> imageUrls;

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

    public Exhibit() {
        super();
        imageUrls = new ArrayList<String>();
    }

    /* Method to init test data */
    public static Exhibit testInit() {
        Exhibit exhibit = new Exhibit();
        exhibit.name = "exhibit name";
        exhibit.shortDescription = "short description";
        exhibit.imageUrls.add("http://upload.wikimedia.org/wikipedia/commons/8/89/Field_Museum_of_Natural_History.jpg");
        return exhibit;
    }
}
