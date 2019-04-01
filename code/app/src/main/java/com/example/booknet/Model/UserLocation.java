package com.example.booknet.Model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class UserLocation implements Serializable {

    private double latitude;
    private double longitude;
    private String note;

    public UserLocation() {
        latitude = 0;
        longitude = 0;
        note = "";
    }

    public UserLocation(float latitude, float longitude, String note) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.note = note;
    }

    public UserLocation(double latitude, double longitude, String note) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.note = note;
    }

    public UserLocation(LatLng location, String note) {
        this.latitude = location.latitude;
        this.longitude = location.longitude;
        this.note = note;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public LatLng getLocation() {
        return new LatLng(latitude, longitude);
    }

    public String getNote() {
        return note;
    }

    /**
     * Checks whether the location has been initialized to an actual value.
     * Currently considers lat/long values of 0,0 to be not set
     * (Those coords are in the middle of the Atlantic Ocean so it is likely fine.)
     *
     * @return
     */
    public boolean locationSet() {
        if (latitude == 0 && longitude == 0) {
            return false;
        } else {
            return true;
        }
    }

}
