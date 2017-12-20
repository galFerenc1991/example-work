package com.ferenc.pamp.presentation.screens.main.chat;

import com.ferenc.pamp.data.model.home.good_deal.GoodDealRequest;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.presentation.utils.GoodDealManager;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by
 * Ferenc on 2017.12.17..
 */

public class ChatPresenter implements ChatContract.Presenter {

    private ChatContract.View mView;
    private GoodDealManager mGoodDealManager;
    private GoodDealResponse mGoodDealResponse;
    private CompositeDisposable mCompositeDisposable;

    public ChatPresenter(ChatContract.View _view, GoodDealManager _goodDealManager, GoodDealResponse _goodDealResponse) {
        this.mView = _view;
        this.mGoodDealManager = _goodDealManager;
        this.mGoodDealResponse = _goodDealResponse;
        this.mCompositeDisposable = new CompositeDisposable();

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }


    @Override
    public void clickedShare() {
        mGoodDealManager.saveGoodDeal(new GoodDealRequest.Builder()
                .setProduct(mGoodDealResponse.product)
                .setDescription(mGoodDealResponse.description)
                .setPrice(mGoodDealResponse.price)
                .setUnit(mGoodDealResponse.unit)
                .setQuantity(mGoodDealResponse.quantity)
                .setClosingDate(mGoodDealResponse.closingDate)
                .setDeliveryAddress(mGoodDealResponse.deliveryAddress)
                .setDeliveryStartDate(mGoodDealResponse.deliveryStartDate)
                .setDeliveryEndDate(mGoodDealResponse.deliveryEndDate)
                .build());
        mView.shareGoodDeal();
    }

    @Override
    public void clickedSettings() {
        mView.openSettingsDialog();
    }

    @Override
    public void clickedParticipants() {
        mView.ShowParticipants();
    }


    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
