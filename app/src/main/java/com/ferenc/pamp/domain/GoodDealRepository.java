package com.ferenc.pamp.domain;

import com.ferenc.pamp.data.api.Rest;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealRequest;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.data.service.GoodDealService;
import com.ferenc.pamp.data.service.UserService;
import com.ferenc.pamp.presentation.screens.main.propose.share.ShareContract;
import com.ferenc.pamp.presentation.utils.SharedPrefManager_;
import com.ferenc.pamp.presentation.utils.SignedUserManager;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by
 * Ferenc on 2017.12.01..
 */
@EBean(scope = EBean.Scope.Singleton)
public class GoodDealRepository extends NetworkRepository implements ShareContract.Model {

    @Bean
    protected Rest rest;
    @Bean
    protected SignedUserManager mSignedUserManager;

    @Pref
    protected SharedPrefManager_ mSharedPrefManager;

    private GoodDealService goodDealService;
    private UserService userService;

    @AfterInject
    protected void initServices() {
        goodDealService = rest.getGoodDealService();
        userService = rest.getUserService();
    }

    @Override
    public Observable<List<String>> getUsedUserContact() {
        return getNetworkObservable(userService.getSavedUserContact());
    }

    @Override
    public Observable<GoodDealResponse> createGoodDeal(GoodDealRequest request) {
        return getNetworkObservable(goodDealService.createGoodDeal(request));
    }
}
