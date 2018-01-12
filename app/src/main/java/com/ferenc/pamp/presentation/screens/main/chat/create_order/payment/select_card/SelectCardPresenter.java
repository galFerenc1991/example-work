package com.ferenc.pamp.presentation.screens.main.chat.create_order.payment.select_card;

import com.ferenc.pamp.data.model.home.orders.OrderRequest;
import com.ferenc.pamp.presentation.utils.GoodDealResponseManager;
import com.ferenc.pamp.presentation.utils.ToastManager;

import io.reactivex.disposables.CompositeDisposable;

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
    private int mQuantity;

    public SelectCardPresenter(SelectCardContract.View _view, GoodDealResponseManager _goodDealResponseManager, SelectCardContract.Model _model, SelectCardContract.CreateOrderModel _createOrderModel, int _quantity) {
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
                }, throwable -> {
                    mView.hideProgress();
                    ToastManager.showToast("Create OReder ERROR: " + throwable.getMessage());
                }));

    }

    @Override
    public void unsubscribe() {

    }
}
