package com.ferenc.pamp.presentation.utils;

import android.text.TextUtils;

import com.ferenc.pamp.presentation.base.models.GoodDeal;
import com.google.gson.Gson;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * Created by
 * Ferenc on 2017.11.28..
 */
@EBean(scope = EBean.Scope.Singleton)
public class GoodDealManager {

    private Gson gson = new Gson();

    @Pref
    protected SharedPrefManager_ prefManager;

    public void saveGoodDeal(GoodDeal goodDeal) {
        if (goodDeal == null) {
            clearGoodDeal();
        } else {
            prefManager
                    .edit()
                    .getGoodDeal()
                    .put(gson.toJson(goodDeal))
                    .apply();
        }
    }

    public GoodDeal getGoodDeal() {
        String goodDealStr = prefManager.getGoodDeal().get();
        return TextUtils.isEmpty(goodDealStr)
                ? new GoodDeal()
                : gson.fromJson(goodDealStr, GoodDeal.class);
    }


    public void clearGoodDeal() {
        prefManager
                .edit()
                .getGoodDeal()
                .remove()
                .apply();
    }
}
