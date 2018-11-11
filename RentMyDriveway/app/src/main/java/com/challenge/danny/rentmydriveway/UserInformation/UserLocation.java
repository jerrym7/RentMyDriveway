package com.challenge.danny.rentmydriveway.UserInformation;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Danny on 10/7/18.
 */

public class UserLocation {
    private double latitude;
    private double longitude;
    private String userUniqueID;

    public UserLocation() {}

    public UserLocation(String userUniqueID, double latitude, double longitude) {
        this.userUniqueID = userUniqueID;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getUserUniqueID() {
        return userUniqueID;
    }

    public void setUserUniqueID(String userUniqueID) {
        this.userUniqueID = userUniqueID;
    }

}
