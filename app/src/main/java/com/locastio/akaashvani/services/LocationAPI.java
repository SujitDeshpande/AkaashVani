package com.locastio.akaashvani.services;

import android.location.Location;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.parse.ParseUser;

/**
 * Created by ketan on 29/08/15.
 */
public class LocationAPI {

    private Firebase mFirebaseRefLocation;
    private static final String FIREBASE_URL = "https://akaashvaani.firebaseio.com/";

    public LocationAPI(Callback callback) {
        this.callback = callback;
        mFirebaseRefLocation = new Firebase(FIREBASE_URL).child("location");
    }

    // The callback interface
    public interface Callback {
        void didDataChanged(DataSnapshot dataSnapshot);
        void didCancelled();
    }
    Callback callback;


    public void updateLocationOfUser(ParseUser parseUser, Location location) {
//        UserLocation userLocation = new UserLocation("objId", location);

        Firebase mUserFirebaseRefLocation = mFirebaseRefLocation.child(""+parseUser.getObjectId());
        mUserFirebaseRefLocation.setValue(location);
//        mUserFirebaseRefLocation.push().setValue(location);

    }

    public void trackUserLocation(ParseUser parseUser) {
        Firebase mUserFirebaseRefLocation = mFirebaseRefLocation.child(""+parseUser.getObjectId());
        mUserFirebaseRefLocation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    public void removeTrackingForUser(ParseUser parseUser) {
        Firebase mUserFirebaseRefLocation = mFirebaseRefLocation.child(""+parseUser.getObjectId());
//        mUserFirebaseRefLocation.removeEventListener();

    }
}
