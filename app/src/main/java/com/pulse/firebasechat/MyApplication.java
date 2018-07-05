package com.pulse.firebasechat;

import android.app.Application;

import com.firebase.client.Firebase;

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";


    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
