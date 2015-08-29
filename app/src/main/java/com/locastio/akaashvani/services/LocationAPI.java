package com.locastio.akaashvani.services;

import android.location.Location;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.locastio.akaashvani.data.User;
import com.locastio.akaashvani.data.UserLocation;
import com.parse.ParseUser;

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
//        UserLocation userLocation = new UserLocation("objId", location);

        Firebase mUserFirebaseRefLocation = mFirebaseRefLocation.child(""+user.getObjectId());
        mUserFirebaseRefLocation.push().setValue(location);

    }

    public void trackUserLocation(User user) {
        Firebase mUserFirebaseRefLocation = mFirebaseRefLocation.child(""+user.getObjectId());
        mUserFirebaseRefLocation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    public void removeTrackingForUser(User user) {
        Firebase mUserFirebaseRefLocation = mFirebaseRefLocation.child(""+user.getObjectId());
//        mUserFirebaseRefLocation.removeEventListener();

    }
}