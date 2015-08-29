package com.locastio.akaashvani.services;

import android.text.TextUtils;

import com.locastio.akaashvani.data.Group;
import com.locastio.akaashvani.data.UserGroup;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by ketan on 29/08/15.
 */
public class GroupAPI {
    // The callback interface
    public interface Callback {
        public void didAddGroup(Group group);
        public void didDeleteGroup();
        public void didFailed();
    }
    Callback callback;

    public GroupAPI(Callback callback) {
//        super(_activity);
        this.callback = callback;

    }

    public Group addGroup(String name) {

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
//        group.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null) {
//                    if (callback != null) {
//                        UserGroup userGroup = new UserGroup();
//                        userGroup.setUser(user);
//                        userGroup.setGroup(group);
//                        try {
//                            userGroup.save();
//                        } catch (ParseException e1) {
//                            e1.printStackTrace();
//                        }
//                        callback.didAddGroup(group);
//                    }
//                } else {
//                    if (callback != null) {
//                        callback.didFailed();
//                    }
//                }
//            }
//        });



    }

    public boolean addGroup(String groupName, List<ParseUser> userList) {
        Group group = this.addGroup(groupName);
        if (group == null) {
            return false;
        }
        for (ParseUser user: userList) {
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
                        callback.didDeleteGroup();
                    }
                } else {
                    if (callback != null) {
                        callback.didFailed();
                    }
                }
            }
        });
    }

}
