package com.ferenc.pamp.domain;

import com.ferenc.pamp.data.api.Rest;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealRequest;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.data.service.AuthService;
import com.ferenc.pamp.data.service.UserService;
import com.ferenc.pamp.presentation.screens.main.propose.share.ShareContract;
import com.ferenc.pamp.presentation.utils.SharedPrefManager_;
import com.ferenc.pamp.presentation.utils.SignedUserManager;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by
 * Ferenc on 2017.12.01..
 */
@EBean(scope = EBean.Scope.Singleton)
public class UserRepository extends NetworkRepository  {

    @Bean
    protected Rest rest;
    @Bean
    protected SignedUserManager mSignedUserManager;

    @Pref
    protected SharedPrefManager_ mSharedPrefManager;

    private UserService userService;

    @AfterInject
    protected void initServices() {
        userService = rest.getUserService();
    }

//    @Override
//    public Observable<List<String>> get() {
//        return getNetworkObservable(authService.getCountryList())
//                .flatMap(countryList -> {
//                    List<String> country = Arrays.asList(countryList.getCountry());
//                    return Observable.just(country);
//                });
//    }
}
