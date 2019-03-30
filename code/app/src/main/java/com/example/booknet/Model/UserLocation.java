package com.example.booknet.Model;

import com.google.android.gms.maps.model.LatLng;

public class UserLocation {

    private LatLng location;
    private String note;

    public UserLocation() {
        location = new LatLng(0,0);
        note = "";
    }

    public UserLocation(LatLng location, String note) {
        this.location = location;
        this.note = note;
    }


    public LatLng getLocation() {
        return location;
    }

    public String getNote() {
        return note;
    }
}
