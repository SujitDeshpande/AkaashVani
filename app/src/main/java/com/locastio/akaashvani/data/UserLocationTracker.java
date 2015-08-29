package com.locastio.akaashvani.data;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.parse.ParseUser;

import java.util.HashMap;

/**
 * Created by ketan on 29/08/15.
 */
public class UserLocationTracker {

    private Firebase mUserFirebaseRefLocation;
    private static final String FIREBASE_URL = "https://akaashvani.firebaseio.com/";
    private User user;
    public UserLocationTracker(Callback callback, User user) {
        this.callback = callback;
        this.user = user;
        Firebase mFirebaseRefLocation = new Firebase(FIREBASE_URL).child("location");
        mUserFirebaseRefLocation = mFirebaseRefLocation.child("" + user.getObjectId());

    }

    // The callback interface
    public interface Callback {
        void onDataChange();
        void onCancelled();
    }
    Callback callback;

    HashMap<String, Firebase> listTrackedUsers = new HashMap<String, Firebase>();
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            callback.onDataChange();
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            callback.onCancelled();
        }
    };
    public void trackUserLocation() {
        mUserFirebaseRefLocation.addValueEventListener(valueEventListener);
    }
    public void removeTrackingForUser() {
        mUserFirebaseRefLocation.removeEventListener(valueEventListener);
    }
}
