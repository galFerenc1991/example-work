package com.ferenc.pamp;

import android.app.Application;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.facebook.stetho.Stetho;

import org.androidannotations.annotations.EApplication;
import org.androidannotations.annotations.SystemService;

/**
 * Created by
 * Ferenc on 2017.11.09..
 */

@EApplication
public class PampApp extends Application {
    @SystemService
    protected ConnectivityManager mConnectivityManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }

    public boolean hasInternetConnection() {
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
