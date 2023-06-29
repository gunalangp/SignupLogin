package com.example.smartgladiatortask;


import androidx.multidex.MultiDexApplication;

import com.example.smartgladiatortask.util.PreferencesManager;
import com.google.gson.Gson;


public class MainApplication extends MultiDexApplication {


    public static MainApplication mApplication;
    private static Gson gson;


    public static MainApplication getInstance() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PreferencesManager preferencesManager = new PreferencesManager(this);
        preferencesManager.init();
    }

    public static Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

}