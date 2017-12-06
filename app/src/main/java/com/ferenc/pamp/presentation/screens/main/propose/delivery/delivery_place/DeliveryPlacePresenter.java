package com.ferenc.pamp.presentation.screens.main.propose.delivery.delivery_place;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by
 * Ferenc on 2017.11.29..
 */

public class DeliveryPlacePresenter implements DeliveryPlaceContract.Presenter {

    private DeliveryPlaceContract.View mView;
    private CompositeDisposable mCompositeDisposable;

    public DeliveryPlacePresenter(DeliveryPlaceContract.View _view) {
        this.mView = _view;
        this.mCompositeDisposable = new CompositeDisposable();

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void clickedBack() {
        mView.closeScreen();
    }

    @Override
    public void clickedSelectPlace() {
        mView.openAutocompletePlaceScreen();
    }

    @Override
    public void placeSelected(String _selectedPlace) {
        mView.returnPlace(_selectedPlace);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
