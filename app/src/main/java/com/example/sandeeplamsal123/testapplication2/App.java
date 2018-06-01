package com.example.sandeeplamsal123.testapplication2;

import android.app.Application;

public class App extends Application {

    private App applicationInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationInstance = this;
    }


    public Application getApplication() {
        return applicationInstance;
    }


}
