package com.ferenc.pamp.presentation.screens.main.chat.create_order.payment.select_card;

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
 * Ferenc on 2017.12.22..
 */

public class SelectCardPresenter implements SelectCardContract.Presenter {

    private SelectCardContract.View mView;
    private SelectCardContract.Model mModel;
    private GoodDealResponseManager mGoodDealResponseManager;
    private SelectCardContract.CreateOrderModel mCreateOrderModel;
    private CompositeDisposable mCompositeDisposable;
    private double mQuantity;

    public SelectCardPresenter(SelectCardContract.View _view,
                               GoodDealResponseManager _goodDealResponseManager,
                               SelectCardContract.Model _model,
                               SelectCardContract.CreateOrderModel _createOrderModel,
                               double _quantity) {
        this.mView = _view;
        this.mGoodDealResponseManager = _goodDealResponseManager;
        this.mModel = _model;
        this.mCreateOrderModel = _createOrderModel;
        this.mQuantity = _quantity;
        this.mCompositeDisposable = new CompositeDisposable();

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mView.showProgressMain();
        mCompositeDisposable.add(mModel.getUserProfile()
                .subscribe(user -> {
                    mView.hideProgress();
                    if (user.getCard() != null)
                        mView.setCardNumber("**** " + user.getCard().getLast4(), user.getCard().getBrand());
                }, throwable -> {
                    mView.hideProgress();
                    ToastManager.showToast("GEtUSer: " + throwable.getMessage());
                }));
    }

    @Override
    public void createOrder() {
        mView.showProgressMain();
        mCompositeDisposable.add(mCreateOrderModel.createOrder(new OrderRequest.Builder()
                .setQuantity(mQuantity)
                .setDealId(mGoodDealResponseManager.getGoodDealResponse().id)
                .build())
                .subscribe(order -> {
                    mView.showProgressMain();
                    mView.showEndFlowCreateOrder();
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
                    ToastManager.showToast(_data.getMessage());
            }
        } else {
            ToastManager.showToast("Something went wrong");
        }
    };

    @Override
    public void unsubscribe() {

    }
}
