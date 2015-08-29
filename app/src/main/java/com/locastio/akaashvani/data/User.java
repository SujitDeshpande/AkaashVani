package com.locastio.akaashvani.data;

import android.app.Activity;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

/**
 * Created by ketan on 29/08/15.
 */
public class User {

    // The callback interface
    public interface Callback {
        void didRegister();
        void didLogin(ParseUser user);
        void didFailed();
    }
    Callback callback;

    public User(Callback callback) {
//        super(_activity);
        this.callback = callback;

    }

    public void registerUser(String userName, String password, String email, String phone) {
        ParseUser user = new ParseUser();
        user.setUsername(userName);//"my name");
        user.setPassword(password);//"my pass");
        user.setEmail(email);//"email@example.com");

// other fields can be set just like with ParseObject
        user.put("phone", phone);//"650-253-0000");

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    if (callback != null) {
                        callback.didRegister();
                    }
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    if (callback != null) {
                        callback.didFailed();
                    }
                }
            }
        });
    }

    public void login(String userName, String password) {
        ParseUser.logInInBackground(userName, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
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
