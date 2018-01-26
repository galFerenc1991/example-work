package com.ferenc.pamp.presentation.screens.main.propose.delivery.delivery_place;

import android.util.Log;

import com.ferenc.pamp.presentation.screens.main.propose.delivery.delivery_place.delivery_place_adapter.DeliveryPlaceDH;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by
 * Ferenc on 2017.11.29..
 */

public class DeliveryPlacePresenter implements DeliveryPlaceContract.Presenter {

    private DeliveryPlaceContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private DeliveryPlaceContract.Model mModel;
    private List<DeliveryPlaceDH> mDeliveryPlaceList;
    private boolean mIsRebroadcast;


    public DeliveryPlacePresenter(DeliveryPlaceContract.View _view,
                                  DeliveryPlaceContract.Model _model,
                                  boolean _isRebroadcast) {
        this.mView = _view;
        this.mCompositeDisposable = new CompositeDisposable();
        this.mModel = _model;
        this.mIsRebroadcast = _isRebroadcast;
        this.mDeliveryPlaceList = new ArrayList<>();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mView.showProgressBar();
        mCompositeDisposable.add(mModel.getUsedUserAddresses()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(addressList -> {
                    mView.hideProgressBar();
                    for (String address : addressList) {
                        mDeliveryPlaceList.add(new DeliveryPlaceDH(address, mIsRebroadcast));
                    }
                    mView.setContactAdapterList(mDeliveryPlaceList);
                }, throwable -> {
                    mView.hideProgressBar();
                    Log.d("getUsedUserAddresses", "Error " + throwable.getMessage());
                }));

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
    public void selectItem(DeliveryPlaceDH item, int position) {
        mView.returnPlace(item.getCountry());
    }


    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
