package com.ferenc.pamp.presentation.screens.main.chat.create_order.payment.save_card;

/**
 * Created by
 * Ferenc on 2017.12.25..
 */

public class SaveCardPresenter implements SaveCardContract.Presenter {

    private SaveCardContract.View mView;

    public SaveCardPresenter(SaveCardContract.View _view) {
        this.mView = _view;

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void clickedValidate() {
        mView.openSuccessCreatedCardScreen();
    }

    @Override
    public void setSaveCardInProfile(boolean isSave) {

    }

    @Override
    public void unsubscribe() {

    }
}
