package com.codepath.columbus.columbus.models;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

@ParseClassName("Museum")
public class Museum extends ParseObject{

    private String name;

    private String imageUrl;

    private String nickname;

    private ParseGeoPoint location;

    private String description;

    private String address;

    private String beaconUUID;

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name",name);
    }

    public String getImageUrl() {
        return getString("imageUrl");
    }

    public void setImageUrl(String imageUrl) {
        put("imageUrl",imageUrl);
    }

    public String getNickname() {
        return getString("nickname");
    }

    public void setNickname(String nickname) {
        put("nickname",nickname);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }

    public void setLocation(ParseGeoPoint location) {
        put("location",location);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description",description);
    }

    public String getAddress() {
        return getString("address");
    }

    public void setAddress(String address) {
        put("address",address);
    }

    public String getBeaconUUID() {
        return getString("beaconUUID");
    }

    public void setBeaconUUID(String address) {
        put("beaconUUID", beaconUUID);
    }
}
