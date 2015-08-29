package com.locastio.akaashvani;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;

/**
 * Created by ketan on 29/08/15.
 */
public class AkaashvaniApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this);

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        ParseFacebookUtils.initialize(this);

    }
}
