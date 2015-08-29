package com.locastio.akaashvani.services;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by ketan on 29/08/15.
 */
public class UserGroupAPI {

    public interface Callback {
        public void didRetrievedMyGroups(List<ParseObject> groupList);

        public void didRetrieveGrpFailed(String s);
    }

    Callback callback;

    public UserGroupAPI(Callback callback) {
//        super(_activity);
        this.callback = callback;

    }

    public void getMyGroups() {
        ParseUser user = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserGroup");

        query.include("group");
        query.whereEqualTo("user", user);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> userGroupList, ParseException e) {
                if (e == null) {
                    Log.d("Groups", "Retrieved " + userGroupList.size() + " groups");
                    callback.didRetrievedMyGroups(userGroupList);
                } else {
                    Log.d("Groups", "Error: " + e.getMessage());
                }
            }
        });
    }

}
