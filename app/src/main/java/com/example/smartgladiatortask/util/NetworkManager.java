package com.example.smartgladiatortask.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

@SuppressLint("WrongConstant")
public class NetworkManager {
    public NetworkManager() {
    }

    public static String STR_CONNECTIVITY = "connectivity";

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(STR_CONNECTIVITY);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        if (activeNetwork != null) {
            if (activeNetwork.getType() == 1) {
                return true;
            }
            return activeNetwork.getType() == 0;
        }

        return false;
    }

    public static boolean isWiFiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(STR_CONNECTIVITY);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        return activeNetwork != null && activeNetwork.getType() == 1;
    }

    public static boolean isGPRSConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(STR_CONNECTIVITY);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        return activeNetwork != null && activeNetwork.getType() == 0;
    }

    public static void openWirelessSettings(Context context) {
        context.startActivity((new Intent("android.settings.WIRELESS_SETTINGS")).setFlags(268435456));
    }
}
