package com.kubator.pamp.presentation.utils;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by
 * Ferenc on 2017.11.09..
 */

@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface SharedPrefManager {

    @DefaultString("")
    String getAccessToken();

    String getUserProfile();

    String getGoodDeal();

    String getGoodDealResponse();

    @DefaultBoolean(false)
    boolean getIsViewedTutorial();

}
