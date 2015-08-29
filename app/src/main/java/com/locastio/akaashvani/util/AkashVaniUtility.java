package com.locastio.akaashvani.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Agoel on 29-08-2015.
 */
public class AkashVaniUtility {

    /**
     * Verify that network connection is available before making a request.
     */
    public static boolean checkNetworkConnection(Context context) {
        boolean isNetworkAvailable = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifi = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifi != null && wifi.isConnected()) {
                isNetworkAvailable = true;
            } else {
                NetworkInfo mobileData = connectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (mobileData != null && mobileData.isConnected()) {
                    isNetworkAvailable = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isNetworkAvailable;
    }
}
