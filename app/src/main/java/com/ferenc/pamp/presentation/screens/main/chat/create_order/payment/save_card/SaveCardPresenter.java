package com.ferenc.pamp.presentation.screens.main.chat.create_order.payment.save_card;

import android.widget.Toast;

import com.ferenc.pamp.data.api.exceptions.ConnectionLostException;
import com.ferenc.pamp.data.model.base.GeneralMessageResponse;
import com.ferenc.pamp.data.model.home.orders.OrderRequest;
import com.ferenc.pamp.presentation.utils.GoodDealResponseManager;
import com.ferenc.pamp.presentation.utils.ToastManager;
import com.google.gson.Gson;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import retrofit2.HttpException;

/**
 * Created by
 * Ferenc on 2017.12.25..
 */

public class SaveCardPresenter implements SaveCardContract.Presenter {

    private SaveCardContract.View mView;
    private SaveCardContract.Model mModel;
    private GoodDealResponseManager mGoodDealResponseManager;
    private CompositeDisposable mCompositeDisposable;
    private boolean mSaveCard;
    private double mQuantity;
    private String mStripeToken;

    public SaveCardPresenter(SaveCardContract.View _view,
                             SaveCardContract.Model _model,
                             GoodDealResponseManager _goodDealResponseManager,
                             double _quantity,
                             String _stripeToken) {
        this.mView = _view;
        this.mModel = _model;
        this.mGoodDealResponseManager = _goodDealResponseManager;
        this.mCompositeDisposable = new CompositeDisposable();
        this.mQuantity = _quantity;
        this.mStripeToken = _stripeToken;

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void createOrder() {
        mView.showProgressMain();
        mCompositeDisposable.add(mModel.createOrder(new OrderRequest.Builder()
                .setDealId(mGoodDealResponseManager.getGoodDealResponse().id)
                .setQuantity(mQuantity)
                .setCardSave(mSaveCard)
                .setCardToken(mStripeToken)
                .build())
                .subscribe(order -> {
                    mView.hideProgress();
                    mView.openSuccessCreatedCardScreen();
                }, throwableConsumer));
    }

    private Consumer<Throwable> throwableConsumer = throwable -> {
        throwable.printStackTrace();
        mView.hideProgress();
        if (throwable instanceof ConnectionLostException) {
            ToastManager.showToast("Connection Lost");
        } else if (throwable instanceof HttpException) {
            int errorCode = ((HttpException) throwable).response().code();
            switch (errorCode) {
                case 400:
                    Gson gson = new Gson();
                    GeneralMessageResponse _data = gson.fromJson( ((HttpException) throwable).response().errorBody().string(), GeneralMessageResponse.class);
                    ToastManager.showToast(_data.getMessage());            }
        } else {
            ToastManager.showToast("Something went wrong");
        }
    };

    @Override
    public void setSaveCardInProfile(boolean isSave) {
        mSaveCard = isSave;
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
