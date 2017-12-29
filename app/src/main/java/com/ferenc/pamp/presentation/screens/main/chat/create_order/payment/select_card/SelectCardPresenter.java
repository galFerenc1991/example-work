package com.ferenc.pamp.presentation.screens.main.chat.create_order.payment.select_card;

/**
 * Created by
 * Ferenc on 2017.12.22..
 */

public class SelectCardPresenter implements SelectCardContract.Presenter {

    private SelectCardContract.View mView;

    public SelectCardPresenter(SelectCardContract.View _view) {
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
