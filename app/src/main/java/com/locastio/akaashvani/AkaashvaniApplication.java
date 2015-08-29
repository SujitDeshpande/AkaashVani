package com.locastio.akaashvani;

import android.app.Application;

import com.firebase.client.Firebase;
import com.locastio.akaashvani.data.Group;
import com.locastio.akaashvani.data.User;
import com.locastio.akaashvani.data.UserGroup;
import com.locastio.akaashvani.preference.PreferenceUtil;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by ketan on 29/08/15.
 */
public class AkaashvaniApplication extends Application {

    private static final String TAG = AkaashvaniApplication.class.getSimpleName();

    private static AkaashvaniApplication mApplication;


    @Override
    public void onCreate() {
        super.onCreate();

        init();

        // initialize a Shared Preference
        PreferenceUtil.init(this, TAG);

        Parse.initialize(this);

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        Firebase.setAndroidContext(this);

        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Group.class);
        ParseObject.registerSubclass(UserGroup.class);


//        ParseFacebookUtils.initialize(this);

    }

    /**
     * create a application instance
     */
    private void init() {
        if (mApplication == null) {
            mApplication = AkaashvaniApplication.this;
        }
    }

    /**
     * get Application context from Application class
     *
     * @return
     */
    public static AkaashvaniApplication getApplication() {
        return mApplication;
    }

}
