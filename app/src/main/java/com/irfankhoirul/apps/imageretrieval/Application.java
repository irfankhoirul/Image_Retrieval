package com.irfankhoirul.apps.imageretrieval;

import com.firebase.client.Firebase;

/**
 * Created by Irfan Khoirul on 6/26/2016.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        // other setup code
    }
}
