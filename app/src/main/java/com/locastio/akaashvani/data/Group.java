package com.locastio.akaashvani.data;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by ketan on 29/08/15.
 */
@ParseClassName("Group")
public class Group extends ParseObject {

//    Group() {
//
//    }

    // name
    public String getName() {
        return getString("name");
    }
    public void setName(String value) {
        put("name", value);
    }

    // owner
    public ParseUser getOwner() {
        return (ParseUser)getParseObject("owner");
    }
    public void setOwner(ParseUser value) {
        put("owner", value);
    }


//    // The callback interface
//    public interface Callback {
//        void didAddGroup();
//        void didFailed();
//    }
//    Callback callback;
//
//    public Group(Callback callback) {
//        this.callback = callback;
//    }
//
//    public void addGroup(String groupName, User owner) {
//        ParseObject group = new ParseObject("Group");
//        group.put("name", groupName);
//        group.put("owner", owner);
//        group.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null) {
//                    callback.didAddGroup();
//                } else {
//                    callback.didFailed();
//                }
//            }
//        });
//    }

}
