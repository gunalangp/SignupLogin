package com.example.smartgladiatortask.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.text.TextUtils;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences.PrefKeyEncryptionScheme;
import androidx.security.crypto.EncryptedSharedPreferences.PrefValueEncryptionScheme;
import androidx.security.crypto.MasterKeys;

import com.google.gson.Gson;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class PreferencesManager {
    private static Gson mGson;
    private static EncryptedSharedPreferences mSharedPreferences;
    private Context mContext;
    private String mName;

    public PreferencesManager(Context context) {
        this.mContext = context;
        mGson = new Gson();
    }

    public PreferencesManager setName(String name) {
        this.mName = name;
        return this;
    }

    public void init() {
        if (this.mContext != null) {
            if (TextUtils.isEmpty(this.mName)) {
                this.mName = this.mContext.getPackageName();
            }

            try {
                String masterKeyAlias = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
                    masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);
                }
                mSharedPreferences = (EncryptedSharedPreferences)EncryptedSharedPreferences.create(this.mName, masterKeyAlias, this.mContext, PrefKeyEncryptionScheme.AES256_SIV, PrefValueEncryptionScheme.AES256_GCM);
            } catch (IOException | GeneralSecurityException var3) {
                Log.e("PreferencesManager", (String) Objects.requireNonNull(var3.getMessage()));
            }

        }
    }

    public static void putString(String key, String value) {
        if (mSharedPreferences != null) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(key, value);
            editor.apply();
        }
    }

    public static String getString(String key, String defValue) {
        return mSharedPreferences == null ? defValue : mSharedPreferences.getString(key, defValue);
    }

    public static String getString(String key) {
        return getString(key, "");
    }

    public static void putStringSet(String key, Set<String> values) {
        if (mSharedPreferences != null) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putStringSet(key, values);
            editor.apply();
        }
    }

    public static Set<String> getStringSet(String key, Set<String> defValues) {
        return mSharedPreferences == null ? defValues : mSharedPreferences.getStringSet(key, defValues);
    }

    public static Set<String> getStringSet(String key) {
        return getStringSet(key, new HashSet());
    }

    public static void putInt(String key, int value) {
        if (mSharedPreferences != null) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putInt(key, value);
            editor.apply();
        }
    }

    public static int getInt(String key, int defValue) {
        return mSharedPreferences == null ? defValue : mSharedPreferences.getInt(key, defValue);
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static void putFloat(String key, float value) {
        if (mSharedPreferences != null) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putFloat(key, value);
            editor.apply();
        }
    }

    public static float getFloat(String key, float defValue) {
        return mSharedPreferences == null ? defValue : mSharedPreferences.getFloat(key, defValue);
    }

    public static float getFloat(String key) {
        return getFloat(key, 0.0F);
    }

    public static void putLong(String key, long value) {
        if (mSharedPreferences != null) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putLong(key, value);
            editor.apply();
        }
    }

    public static long getLong(String key, long defValue) {
        return mSharedPreferences == null ? defValue : mSharedPreferences.getLong(key, defValue);
    }

    public static long getLong(String key) {
        return getLong(key, 0L);
    }

    public static void putBoolean(String key, boolean value) {
        if (mSharedPreferences != null) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(key, value);
            editor.apply();
        }
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return mSharedPreferences == null ? defValue : mSharedPreferences.getBoolean(key, defValue);
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static void putObject(String key, Object value) {
        if (mGson != null && value != null) {
            putString(key, mGson.toJson(value));
        }
    }

    public static <T> T getObject(String key, Class<T> type) {
        return mSharedPreferences != null && mGson != null ? mGson.fromJson(getString(key), type) : null;
    }

    public static void remove(String key) {
        if (mSharedPreferences != null) {
            mSharedPreferences.edit().remove(key).apply();
        }
    }

    public static void clear() {
        if (mSharedPreferences != null) {
            Iterator var0 = mSharedPreferences.getAll().keySet().iterator();

            while(var0.hasNext()) {
                String key = (String)var0.next();
                mSharedPreferences.edit().remove(key).apply();
            }

        }
    }
}
