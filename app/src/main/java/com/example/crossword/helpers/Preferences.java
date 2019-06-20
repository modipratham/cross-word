package com.example.crossword.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Map;

public class Preferences {

    public static SharedPreferences prefs;

    public static void initPrefs(Context context) {
        if (context instanceof Activity) {
            throw new IllegalArgumentException("Please pass Application Context");
        } else if (prefs == null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

    public synchronized static boolean hasPreference(String key) {
        return prefs.contains(key);
    }

    public synchronized static void putInt(String key, int value) {
        prefs.edit().putInt(key, value).commit();
    }

    public synchronized static int getInt(String key, int defaultValue) {
        if (prefs == null) return defaultValue;
        return prefs.getInt(key, defaultValue);
    }

    public synchronized static void putLong(String key, long value) {
        prefs.edit().putLong(key, value).commit();
    }

    public synchronized static long getLong(String key, long defaultValue) {
        if (prefs == null) return defaultValue;
        return prefs.getLong(key, defaultValue);
    }

    public synchronized static void putFloat(String key, float value) {
        prefs.edit().putFloat(key, value).commit();
    }

    public synchronized static float getFloat(String key, float defaultValue) {
        if (prefs == null) return defaultValue;
        return prefs.getFloat(key, defaultValue);
    }

    public synchronized static void putBoolean(String key, boolean value) {
        prefs.edit().putBoolean(key, value).commit();
    }

    public synchronized static boolean getBoolean(String key) {
        if (prefs == null) return false;
        return prefs.getBoolean(key, false);
    }

    public synchronized static void putString(String key, String value) {
        prefs.edit().putString(key, value).commit();
    }

    public synchronized static String getString(String key, String defaultValue) {
        if (prefs == null) return defaultValue;
        return prefs.getString(key, defaultValue);
    }

    public Map<String, ?> getAll() {
        if (prefs == null) return null;
        return prefs.getAll();
    }

    public synchronized static void removePreference(String key) {
        if (prefs == null) return;
        if (prefs.contains(key)) {
            prefs.edit().remove(key).commit();
        }
    }

    public synchronized static boolean prefContains(String key) {
        return prefs != null && prefs.contains(key);
    }

}
