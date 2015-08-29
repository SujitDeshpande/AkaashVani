package com.locastio.akaashvani.services;

import android.util.Log;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by ketan on 29/08/15.
 */
public class UserAPI {

    // The callback interface
    public interface Callback {
        void didRegister(ParseUser user);

        void didLogin(ParseUser user);

        void didFailed();
    }

    Callback callback;

    public UserAPI(Callback callback) {
//        super(_activity);
        this.callback = callback;

    }

    public void registerUser(String phone, String password, String fullName) {
        final ParseUser user = new ParseUser();
        user.setUsername(phone);//"my name");
        user.setPassword(password);//"my pass");
        user.put("fullname", fullName);
//        user.setEmail(email);//"email@example.com");

// other fields can be set just like with ParseObject
//        user.put("phone", phone);//"650-253-0000");

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    Log.d("k10", "registration success");
                    if (callback != null) {
                        callback.didRegister(user);
                    }
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    Log.d("k10", "registration failed");
                    if (callback != null) {
                        callback.didFailed();
                    }
                }
            }
        });
    }

    public void login(String phone, String password) {
        ParseUser.logInInBackground(phone, password, new LogInCallback() {
            public void done(ParseUser parseUser, ParseException e) {
                ParseUser user = parseUser;
                if (user != null) {
                    // Hooray! The user is logged in.
                    if (callback != null) {
                        callback.didLogin(user);
                    }
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    if (callback != null) {
                        callback.didFailed();
                    }
                }
            }
        });
    }

}
