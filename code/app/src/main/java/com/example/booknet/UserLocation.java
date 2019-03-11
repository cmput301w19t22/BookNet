package com.example.booknet;

import android.location.Location;

public class UserLocation extends Location{

    private Location userLocation;

    public UserLocation(){
        super("");
    }

    public UserLocation(Location location){
        super("");
    }

    public Location getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;
    }
}
