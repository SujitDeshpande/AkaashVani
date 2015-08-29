package com.locastio.akaashvani;

import android.app.Application;

import com.firebase.client.Firebase;
import com.locastio.akaashvani.data.Group;
import com.locastio.akaashvani.data.User;
import com.locastio.akaashvani.data.UserGroup;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;

/**
 * Created by ketan on 29/08/15.
 */
public class AkaashvaniApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);

        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Group.class);
        ParseObject.registerSubclass(UserGroup.class);


        Parse.initialize(this);
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        ParseFacebookUtils.initialize(this);

    }
}
