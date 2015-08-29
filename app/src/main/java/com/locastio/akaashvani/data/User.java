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
public class User extends ParseUser {

    // name
    public String getPhone() {
        return getString("phone");
    }
    public void setPhone(String value) {
        put("phone", value);
    }

    // owner
    public String getPassword() {
        return getString("password");
    }
    public void setPassword(String value) {
        put("password", value);
    }

}
