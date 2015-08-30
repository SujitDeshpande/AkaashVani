package com.locastio.akaashvani.services;

import android.text.TextUtils;

import com.locastio.akaashvani.data.Group;
import com.locastio.akaashvani.data.UserGroup;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by ketan on 29/08/15.
 */
public class GroupAPI {
    // The callback interface
    public interface Callback {
        public void didDeleteGroup(String s);
        public void didGroupFailed(String s);
    }
    Callback callback;

    public GroupAPI(Callback callback) {
//        super(_activity);
        this.callback = callback;

    }

    private Group addGroup(String name) {

        final ParseUser user = ParseUser.getCurrentUser();
        if (user == null) {
            throw new RuntimeException("User needs to be signed up first.");
//            return;
        }
        if (TextUtils.isEmpty(name)) {
            throw  new RuntimeException("Group name cannot be empty");
        }

        final Group group = new Group();
        group.setName(name);
        group.setOwner(user);
        try {
            group.save();
            return group;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;

    }

    public boolean createGroup(String groupName, List<ParseUser> userList) {
        Group group = this.addGroup(groupName);
        if (group == null) {
            return false;
        }
        UserGroup currentUserGroup = new UserGroup();
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUserGroup.setUser(currentUser);
        currentUserGroup.setGroup(group);
        currentUserGroup.saveInBackground();

        for (ParseUser user: userList) {
            if (user.getObjectId() == currentUser.getObjectId()) {
                continue;
            }
            UserGroup userGroup = new UserGroup();
            userGroup.setUser(user);
            userGroup.setGroup(group);
            userGroup.saveInBackground();
        }
        return true;
    }


    public void deleteGroup(Group group) {
        group.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    if (callback != null) {
                        callback.didDeleteGroup("Group deleted successfully.");
                    }
                } else {
                    if (callback != null) {
                        callback.didGroupFailed("Group creation failed.");
                    }
                }
            }
        });
    }

}
