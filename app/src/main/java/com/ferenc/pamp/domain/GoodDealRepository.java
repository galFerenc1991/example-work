package com.ferenc.pamp.domain;

import com.ferenc.pamp.data.api.Rest;
import com.ferenc.pamp.data.api.RestConst;
import com.ferenc.pamp.data.model.base.ListResponse;
import com.ferenc.pamp.data.model.home.good_deal.ConnectGoodDealResponse;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealCancelResponse;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealRequest;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.data.model.home.orders.OrdersList;
import com.ferenc.pamp.data.service.GoodDealService;
import com.ferenc.pamp.data.service.UserService;
import com.ferenc.pamp.presentation.screens.main.MainContract;
import com.ferenc.pamp.presentation.screens.main.chat.ChatContract;
import com.ferenc.pamp.presentation.screens.main.chat.messenger.MessengerContract;
import com.ferenc.pamp.presentation.screens.main.chat.orders.OrderContract;
import com.ferenc.pamp.presentation.screens.main.good_plan.proposed.ProposedPlansContract;
import com.ferenc.pamp.presentation.screens.main.good_plan.received.ReceivedPlansContract;
import com.ferenc.pamp.presentation.screens.main.propose.delivery.delivery_place.DeliveryPlaceContract;
import com.ferenc.pamp.presentation.screens.main.propose.share.ShareContract;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.GoodDealResponseManager;
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
public class GoodDealRepository extends NetworkRepository implements ShareContract.Model
        , ProposedPlansContract.Model
        , ReceivedPlansContract.Model
        , MainContract.Model
        , MessengerContract.GoodDealModel
        , DeliveryPlaceContract.Model
        , OrderContract.GoodDealModel
        , ChatContract.Model {

    @Bean
    protected Rest rest;
    @Bean
    protected SignedUserManager mSignedUserManager;

    @Bean
    protected GoodDealResponseManager mGoodDealResponseManager;

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
        return userService.getSavedUserContact();
    }

    @Override
    public Observable<List<String>> getUsedUserAddresses() {
        return userService.getSavedUserAddresses();
    }

    @Override
    public Observable<GoodDealResponse> createGoodDeal(GoodDealRequest request) {
        return getNetworkObservable(goodDealService.createGoodDeal(request));
    }

    @Override
    public Observable<GoodDealResponse> resendGoodDeal(GoodDealRequest request) {
        return getNetworkObservable(goodDealService.resendGoodDeal(request.getId(), request));
    }

    @Override
    public Observable<ConnectGoodDealResponse> connectGoodDeal(String _id) {
        return getNetworkObservable(goodDealService.connectGoodDeal(_id));
    }

    @Override
    public Observable<ListResponse<GoodDealResponse>> getProposedGoodDeal(int _page) {
        return getNetworkObservable(goodDealService.getGoodDeals(RestConst.PROPOSED_GOOD_DEAL_LIST_REQUEST_PARAMETER, _page, RestConst.ITEMS_PER_PAGE));
    }

    @Override
    public Observable<ListResponse<GoodDealResponse>> getReceivedGoodDeal(int _page) {
        return getNetworkObservable(goodDealService.getGoodDeals(RestConst.RECEIVED_GOOD_DEAL_LIST_REQUEST_PARAMETER, _page, RestConst.ITEMS_PER_PAGE));
    }

    @Override
    public Observable<GoodDealCancelResponse> cancelGoodDeal(String _id) {
        return getNetworkObservable(goodDealService.cancelGoodDeal(_id)
                .flatMap(goodDealCancelResponse -> {
                    GoodDealResponse goodDealResponse = mGoodDealResponseManager.getGoodDealResponse();
                    goodDealResponse.state = Constants.STATE_CANCELED;
                    mGoodDealResponseManager.saveGoodDealResponse(goodDealResponse);
                    return Observable.just(goodDealCancelResponse);
                }));
    }

    @Override
    public Observable<GoodDealResponse> updateGoodDeal(String _id, GoodDealRequest request) {
        return getNetworkObservable(goodDealService.updateGoodDeal(_id, request)
                .flatMap(goodDealResponse -> {
                    goodDealResponse.recipients = mGoodDealResponseManager.getGoodDealResponse().recipients;
                    mGoodDealResponseManager.saveGoodDealResponse(goodDealResponse);
                    return Observable.just(goodDealResponse);
                }));
    }

    @Override
    public Observable<GoodDealCancelResponse> confirmDeal(String _dealId, OrdersList list) {
        return getNetworkObservable(goodDealService.confirmDeal(_dealId, list));
    }

    @Override
    public Observable<GoodDealResponse> getDialId(String _dealId) {
        return getNetworkObservable(goodDealService.getDealById(_dealId)
                .flatMap(goodDealResponse -> {
                    mGoodDealResponseManager.saveGoodDealResponse(goodDealResponse);
                    return Observable.just(goodDealResponse);
                }));
    }
}
