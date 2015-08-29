package com.locastio.akaashvani.services;

import android.text.TextUtils;
import android.util.Log;

import com.locastio.akaashvani.data.Group;
import com.locastio.akaashvani.data.User;
import com.locastio.akaashvani.data.UserGroup;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by ketan on 29/08/15.
 */
public class UserGroupAPI {

    public interface Callback {
        public void didAddUserGroup(UserGroup userGroup);
        public void didRetrievedMyGroups(List<UserGroup> groupList);
        public void didFailed();
    }
    Callback callback;

    public UserGroupAPI(Callback callback) {
//        super(_activity);
        this.callback = callback;

    }

    public void addUserGroup(User user, Group group) {

        final UserGroup userGroup = new UserGroup();
        userGroup.setUser(user);
        userGroup.setGroup(group);
        userGroup.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    if (callback != null) {
                        callback.didAddUserGroup(userGroup);
                    }
                } else {
                    if (callback != null) {
                        callback.didFailed();
                    }
                }
            }
        });



    }

    public void getMyGroups() {
        User user = (User)ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserGroup");


        query.whereEqualTo("user", user);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> userGroupList, ParseException e) {
                if (e == null) {
                    Log.d("Groups", "Retrieved " + userGroupList.size() + " groups");
                } else {
                    Log.d("Groups", "Error: " + e.getMessage());
                }
            }
        });
    }

}
