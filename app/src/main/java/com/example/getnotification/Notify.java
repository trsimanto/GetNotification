package com.example.getnotification;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;


public class Notify extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
