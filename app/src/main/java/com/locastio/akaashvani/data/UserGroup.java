package com.locastio.akaashvani.data;

import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by ketan on 29/08/15.
 */
public class UserGroup extends ParseObject {

    // User
    public ParseUser getUser() {
        return (ParseUser)getParseObject("user");
    }
    public void setUser(ParseUser value) {
        put("user", value);
    }

    // Group
    public Group getGroup() {
        return (Group)getParseObject("group");
    }
    public void setGroup(Group value) {
        put("group", value);
    }


}
