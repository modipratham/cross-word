package com.example.crossword.app;

import android.app.Application;

import com.example.crossword.helpers.Preferences;

public class CrosswordApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Preferences.initPrefs(this);
    }
}
