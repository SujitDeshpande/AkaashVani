package com.locastio.akaashvani.services;

import android.util.Log;

import com.locastio.akaashvani.data.Group;
import com.parse.FindCallback;
import com.parse.GetCallback;
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
        public void didRetrievedUserGroups(List<ParseObject> userGroupObjList, Group group);
        public void didRetrieveGrpFailed(String s);
        public void didRetrieveUserGroupFailed(String s);
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

    public void getUserGroupsOfGroupId(String groupId) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
        query.whereEqualTo("objectId", groupId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    Group group = (Group)parseObject;
                    getUserGroupsOfGroup(group);
                } else {
                    Log.d("Groups", "Error: " + e.getMessage());
                    callback.didRetrieveGrpFailed("");
                }
            }
        });

    }
    public void getUserGroupsOfGroup(final Group group) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserGroup");

        query.include("user");
        query.whereEqualTo("group", group);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> userGroupList, ParseException e) {
                if (e == null) {
                    Log.d("Groups", "Retrieved " + userGroupList.size() + " groups");
                    callback.didRetrievedUserGroups(userGroupList, group);
                } else {
                    Log.d("Groups", "Error: " + e.getMessage());
                    callback.didRetrieveGrpFailed("");
                }
            }
        });
    }


}
