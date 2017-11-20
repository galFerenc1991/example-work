package com.ferenc.pamp.presentation.screens.auth.sign_up.country_picker;

import com.ferenc.pamp.data.model.auth.CountryList;
import com.ferenc.pamp.presentation.base.BaseModel;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;
import com.ferenc.pamp.presentation.base.content.ContentView;
import com.ferenc.pamp.presentation.screens.auth.sign_up.country_picker.adapter.CountryDH;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by
 * Ferenc on 2017.11.20..
 */

public interface CountryContract {
    interface View extends ContentView, BaseView<Presenter> {
        void setCountryList(List<CountryDH> list);
        void updateItem(CountryDH item, int position);
        void returnResult(String country);
    }

    interface Presenter extends BasePresenter {
        void selectItem(CountryDH item, int position);
        void pickCountry();
    }

    interface Model extends BaseModel {
        Observable<List<String>> getCountryList();
    }
}
