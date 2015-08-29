package com.locastio.akaashvani.data;

import android.location.Location;
import android.util.Log;

/**
 * Created by ketan on 29/08/15.
 */
public class UserLocation {

    private String userObjectId;
    private Location location;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private UserLocation() {
    }

    public UserLocation(String userObjectId, Location location) {
        this.userObjectId = userObjectId;
        this.location = location;
    }

    public String getUserObjectId() {
        return userObjectId;
    }

    public Location getLocation() {
        return location;
    }

}
