package com.locastio.akaashvani.services;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.parse.ParseUser;

/**
 * Created by ketan on 30/08/15.
 */
public class LocationTrackerAPI {
    private Firebase mFirebaseRefLocation;
    private static final String FIREBASE_URL = "https://akaashvaani.firebaseio.com/";

    public LocationTrackerAPI(Callback callback) {
        this.callback = callback;
        mFirebaseRefLocation = new Firebase(FIREBASE_URL).child("location");
    }

    // The callback interface
    public interface Callback {
        void didDataChanged(DataSnapshot dataSnapshot, ParseUser parseUser);
        void didCancelledLocationTrackingOfUser(ParseUser parseUser);
    }
    Callback callback;

    ParseUser parseUser;
    public void trackUserLocation(final ParseUser parseUser_) {
        this.parseUser = parseUser_;
        Firebase mUserFirebaseRefLocation = mFirebaseRefLocation.child(""+parseUser.getObjectId());
        mUserFirebaseRefLocation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.didDataChanged(dataSnapshot, parseUser);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                callback.didCancelledLocationTrackingOfUser(parseUser);
            }
        });
    }
    public void removeTrackingForUser() {
        Firebase mUserFirebaseRefLocation = mFirebaseRefLocation.child(""+parseUser.getObjectId());
//        mUserFirebaseRefLocation.removeEventListener();

    }
}
