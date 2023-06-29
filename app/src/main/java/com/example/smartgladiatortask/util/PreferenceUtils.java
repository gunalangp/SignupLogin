package com.example.smartgladiatortask.util;


/**
 * This class is use to create get set methods to store and access SharedPreference data.
 */
public class PreferenceUtils {

    public PreferenceUtils() {
        throw new IllegalStateException("Preference Utils");
    }

    private static final String PREF_USER_LOGGED_IN = "pref_user_logged_in";
    private static final String PREF_USER_ID = "pref_user_id";


    public static boolean isUserLoggedIn() {
        return PreferencesManager.getBoolean(PREF_USER_LOGGED_IN, false);
    }

    public static void setUserLoggedIn(boolean prefUserLoggedIn) {
        PreferencesManager.putBoolean(PREF_USER_LOGGED_IN, prefUserLoggedIn);
    }

    public static void setPrefUserName(String token) {
        PreferencesManager.putString(PREF_USER_ID, token);
    }
    public static String getPrefUserName() {
        return PreferencesManager.getString(PREF_USER_ID, null);
    }



}
