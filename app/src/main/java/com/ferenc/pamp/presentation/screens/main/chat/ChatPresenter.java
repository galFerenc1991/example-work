package com.ferenc.pamp.presentation.screens.main.chat;

import android.text.TextUtils;

import com.ferenc.pamp.data.model.common.User;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealRequest;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.GoodDealManager;
import com.ferenc.pamp.presentation.utils.GoodDealResponseManager;
import com.ferenc.pamp.presentation.utils.ToastManager;
import com.ferenc.pamp.presentation.utils.ValidationManager;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by
 * Ferenc on 2017.12.17..
 */

public class ChatPresenter implements ChatContract.Presenter {

    private ChatContract.View mView;
    private GoodDealManager mGoodDealManager;
    private GoodDealResponse mGoodDealResponse;
    private GoodDealResponseManager mGoodDealResponseManager;
    private CompositeDisposable mCompositeDisposable;
    private ChatContract.Model mModel;
    private String mDealId;

    public ChatPresenter(ChatContract.View _view,
                         GoodDealManager _goodDealManager,
                         GoodDealResponseManager _goodDealResponseManager,
                         ChatContract.Model _model,
                         String _dealId) {
        this.mView = _view;
        this.mGoodDealManager = _goodDealManager;
        this.mGoodDealResponse = _goodDealResponseManager.getGoodDealResponse();
        this.mCompositeDisposable = new CompositeDisposable();
        this.mGoodDealResponseManager = _goodDealResponseManager;
        this.mModel = _model;
        this.mDealId = _dealId;

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mView.setTitle(mGoodDealResponseManager.getGoodDealResponse().title);
        if (mDealId != null) {
            mView.showProgress();
            mCompositeDisposable.add(mModel.getDialId(mDealId)
                    .subscribe(goodDealResponse -> {
                        mView.hideProgress();
                        setParticipants();
                        mView.showChat();
                    }, throwable -> {
                        mView.hideProgress();
                        ToastManager.showToast("Deal load error");
                    }));
        } else {
            setParticipants();
            mView.showChat();
        }
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
        if (!mGoodDealResponseManager.getGoodDealResponse().state.equals(Constants.STATE_CANCELED))
            mView.openSettingsDialog();
        else {
            ToastManager.showToast("Good Deal Canceled! You can not modified it!");
            mView.hideSettings();
        }
    }

    @Override
    public void setParticipants() {
        ArrayList<String> recipients = new ArrayList<>();
        if (mGoodDealResponse.recipients != null) {
            for (User user : mGoodDealResponse.recipients) {
                recipients.add(user.getFirstName());
            }
            mView.setParticipants(TextUtils.join(", ", recipients));
        }
    }

    @Override
    public void clickedParticipants() {
        mView.showParticipants();
    }


    @Override
    public void unsubscribe() {
        mGoodDealManager.clearGoodDeal();
        mGoodDealResponseManager.clearGoodDealResponse();
        mCompositeDisposable.clear();

    }
}
