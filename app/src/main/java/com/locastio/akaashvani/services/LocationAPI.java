package com.locastio.akaashvani.services;

import android.location.Location;
import android.support.annotation.NonNull;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.locastio.akaashvani.data.User;
import com.locastio.akaashvani.data.UserLocation;
import com.parse.ParseUser;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by ketan on 29/08/15.
 */
public class LocationAPI {

    private Firebase mFirebaseRefLocation;
    private static final String FIREBASE_URL = "https://akaashvani.firebaseio.com/";

    public LocationAPI(Callback callback) {
        this.callback = callback;
        mFirebaseRefLocation = new Firebase(FIREBASE_URL).child("location");
    }

    // The callback interface
    public interface Callback {
        void didRegister();
        void didLogin(ParseUser user);
        void didFailed();
    }
    Callback callback;


    public void updateLocationOfUser(User user, Location location) {
        Firebase mUserFirebaseRefLocation = mFirebaseRefLocation.child(""+user.getObjectId());
        mUserFirebaseRefLocation.push().setValue(location);
    }


}
