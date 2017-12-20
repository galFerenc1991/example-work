package com.ferenc.pamp.presentation.screens.main.chat.orders;

/**
 * Created by
 * Ferenc on 2017.12.15..
 */

public class OrderPresenter implements OrderContract.Presenter {

    private OrderContract.View mView;

    public OrderPresenter(OrderContract.View _view) {
        this.mView = _view;

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}
