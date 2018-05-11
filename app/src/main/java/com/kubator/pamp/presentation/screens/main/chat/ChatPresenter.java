package com.kubator.pamp.presentation.screens.main.chat;

import android.text.TextUtils;

import com.kubator.pamp.data.model.common.User;
import com.kubator.pamp.data.model.home.good_deal.GoodDealRequest;
import com.kubator.pamp.data.model.home.good_deal.GoodDealResponse;
import com.kubator.pamp.presentation.utils.Constants;
import com.kubator.pamp.presentation.utils.GoodDealManager;
import com.kubator.pamp.presentation.utils.GoodDealResponseManager;
import com.kubator.pamp.presentation.utils.SignedUserManager;
import com.kubator.pamp.presentation.utils.ToastManager;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

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
    private SignedUserManager mSignedUserManager;

    public ChatPresenter(ChatContract.View _view,
                         GoodDealManager _goodDealManager,
                         GoodDealResponseManager _goodDealResponseManager,
                         ChatContract.Model _model,
                         String _dealId,
                         SignedUserManager _signedUserManager) {
        this.mView = _view;
        this.mGoodDealManager = _goodDealManager;
        this.mGoodDealResponse = _goodDealResponseManager.getGoodDealResponse();
        this.mCompositeDisposable = new CompositeDisposable();
        this.mGoodDealResponseManager = _goodDealResponseManager;
        this.mModel = _model;
        this.mDealId = _dealId;
        this.mSignedUserManager = _signedUserManager;

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        if (mDealId != null) {
            mView.showProgress();
            mCompositeDisposable.add(mModel.getDialId(mDealId).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread())
                    .subscribe(goodDealResponse -> {
                        mGoodDealResponse = goodDealResponse;
                        mView.setTitle(mGoodDealResponse.title);
                        mView.hideProgress();
                        if (mSignedUserManager.getCurrentUser().getId().equals(goodDealResponse.owner.getId()))
                            mView.initFromWhere(Constants.ITEM_TYPE_REUSE);
                        else {
                            mView.initFromWhere(Constants.ITEM_TYPE_RE_BROADCAST);
                            if (mGoodDealResponse.state.equals(Constants.STATE_PROGRESS))
                                doTimeoutFiveSecond(goodDealResponse.reviewed);
                            else mView.hideShareButton();
                        }
                        setParticipants();
                        mView.showChat();
                    }, throwable -> {
                        mView.hideProgress();
                        ToastManager.showToast("Deal load error");
                    }));
        } else {
            mView.initFromWhere(7777);
            setParticipants();
            mView.showChat();
        }
    }

    private void doTimeoutFiveSecond(boolean reviewed) {
        mCompositeDisposable.add(Observable.just(reviewed)
                .delay(15, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if (!aBoolean) mView.showSharePopUp();
                }));
    }


    @Override
    public void clickedShare() {
        if (mGoodDealResponse.state.equals(Constants.STATE_PROGRESS)) {
            mGoodDealManager.saveGoodDeal(new GoodDealRequest.Builder()
                    .setID(mGoodDealResponse.id)
                    .setProduct(mGoodDealResponse.product)
                    .setDescription(mGoodDealResponse.description)
                    .setPrice(mGoodDealResponse.price)
                    .setUnit(mGoodDealResponse.unit)
                    .setQuantity(mGoodDealResponse.quantity)
                    .setClosingDate(mGoodDealResponse.closingDate)
                    .setDeliveryStartDate(mGoodDealResponse.deliveryStartDate)
                    .setDeliveryEndDate(mGoodDealResponse.deliveryEndDate)
                    .build());
            mView.shareGoodDeal();
        } else {
            ToastManager.showToast("Good Deal CLOSED! You can not resend CLOSED GOOD DEAL!!!");
            mView.hideShareButton();
        }
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
            if (mGoodDealResponse.recipients.size() > 0) {
                for (User user : mGoodDealResponse.recipients)
                    recipients.add(user.getFirstName());
                mView.setParticipants(TextUtils.join(", ", recipients));
            } else mView.hideParticipantTextView();
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
