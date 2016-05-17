package com.dangdoan.atomparser;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.firebase.client.Firebase;

/**
 * Created by dangdoan on 5/13/16.
 */
public class AtomParserApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this);
        Firebase.setAndroidContext(this);
    }
}
