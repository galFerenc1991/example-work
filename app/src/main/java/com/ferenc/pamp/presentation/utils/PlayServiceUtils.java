package com.ferenc.pamp.presentation.utils;

import android.app.Activity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.androidannotations.annotations.EBean;

/**
 * Created by
 * Ferenc on 2017.11.30..
 */

@EBean(scope = EBean.Scope.Singleton)
public class PlayServiceUtils {

    public boolean checkPlayServices(Activity activity) {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(activity);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(activity, result, 9000).show();
            }

            return false;
        }

        return true;
    }
}