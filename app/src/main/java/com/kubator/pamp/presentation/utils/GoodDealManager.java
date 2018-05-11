package com.kubator.pamp.presentation.utils;

import android.text.TextUtils;

import com.kubator.pamp.data.model.home.good_deal.GoodDealRequest;
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

    public void saveGoodDeal(GoodDealRequest _goodDealRequest) {
        if (_goodDealRequest == null) {
            clearGoodDeal();
        } else {
            prefManager
                    .edit()
                    .getGoodDeal()
                    .put(gson.toJson(_goodDealRequest))
                    .apply();
        }
    }

    public GoodDealRequest getGoodDeal() {
        String goodDealStr = prefManager.getGoodDeal().get();
        return TextUtils.isEmpty(goodDealStr)
                ? new GoodDealRequest()
                : gson.fromJson(goodDealStr, GoodDealRequest.class);
    }


    public void clearGoodDeal() {
        prefManager
                .edit()
                .getGoodDeal()
                .remove()
                .apply();
    }
}
