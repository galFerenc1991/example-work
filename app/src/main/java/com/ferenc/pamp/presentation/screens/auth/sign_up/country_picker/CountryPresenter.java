package com.ferenc.pamp.presentation.screens.auth.sign_up.country_picker;

import com.ferenc.pamp.data.model.auth.Country;
import com.ferenc.pamp.presentation.screens.auth.sign_up.country_picker.adapter.CountryDH;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by
 * Ferenc on 2017.11.20..
 */

public class CountryPresenter implements CountryContract.Presenter {

    private CountryContract.View mView;
    private CountryContract.Model mModel;
    private CompositeDisposable mCompositeDisposable;
    private String mCountry;
    private int selectedPos;

    public CountryPresenter(CountryContract.View _view, CountryContract.Model _model, String _country) {
        this.mView = _view;
        this.mModel = _model;
        this.mCountry = _country;
        mCompositeDisposable = new CompositeDisposable();

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mView.showProgressMain();
        mCompositeDisposable.add(mModel.getCountryList()
                .flatMap(countryList -> {
                    List<CountryDH> countryDHList = new ArrayList<>();
                    String header = " ";
                    boolean isCurrent = false;

                    for (String country : countryList) {
                        if (!country.startsWith(header)) {
                            header = String.valueOf(country.charAt(0));
                            countryDHList.add(new CountryDH(header));
                        }
                        isCurrent = country.equalsIgnoreCase(mCountry);
                        if (isCurrent) {
                            selectedPos = countryDHList.size();
                            mCountry = country;
                        }
                        countryDHList.add(new CountryDH(new Country(country), isCurrent));
                    }
                    return Observable.just(countryDHList);
                })
                .subscribe(countryDHList -> {
                    mView.hideProgress();
                    mView.setCountryList(countryDHList);
                }, throwable -> {
                    mView.hideProgress();
                }));
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void selectItem(CountryDH item, int position) {
        if (item.header == null) {
            if (selectedPos != position) {
                //diselect prev item
                mView.updateItem(new CountryDH(new Country(mCountry)), selectedPos);
                //select current item
                mCountry = item.country.getName();
                selectedPos = position;
                mView.updateItem(new CountryDH(new Country(mCountry), true), selectedPos);
            }
        }
    }

    @Override
    public void pickCountry() {
        mView.returnResult(mCountry);
    }
}
