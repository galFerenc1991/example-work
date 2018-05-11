package com.kubator.pamp.presentation.utils;

import android.text.TextUtils;

import com.kubator.pamp.data.model.home.good_deal.GoodDealResponse;
import com.google.gson.Gson;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * Created by
 * Ferenc on 2017.12.18..
 */

@EBean(scope = EBean.Scope.Singleton)
public class GoodDealResponseManager {


    private Gson gson = new Gson();

    @Pref
    protected SharedPrefManager_ prefManager;

    public void saveGoodDealResponse(GoodDealResponse _goodDealResponse) {
        if (_goodDealResponse == null) {
            clearGoodDealResponse();
        } else {
            prefManager
                    .edit()
                    .getGoodDealResponse()
                    .put(gson.toJson(_goodDealResponse))
                    .apply();
        }
    }

    public GoodDealResponse getGoodDealResponse() {
        String goodDealResponseStr = prefManager.getGoodDealResponse().get();
        return TextUtils.isEmpty(goodDealResponseStr)
                ? new GoodDealResponse()
                : gson.fromJson(goodDealResponseStr, GoodDealResponse.class);
    }


    public void clearGoodDealResponse() {
        prefManager
                .edit()
                .getGoodDealResponse()
                .remove()
                .apply();
    }
}
