package com.ferenc.pamp.presentation.custom.bank_account;

import com.ferenc.pamp.data.model.auth.TokenRequest;
import com.ferenc.pamp.data.model.common.BankAccount;
import com.ferenc.pamp.presentation.base.BaseModel;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;
import com.ferenc.pamp.presentation.base.content.ContentView;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * Created by
 * Ferenc on 2018.01.04..
 */

public interface BankAccountContract {
    interface View extends ContentView, BaseView<Presenter> {
        void setCountry(String _country);

        void openCountryPicker(String _selectedCountry);

        void showSuccessEndFlowScreen();
    }

    interface Presenter extends BasePresenter {
        void clickedValidate(String _iban);

        void clickedCountry();

        void setSelectedCountry(String _country);
    }

    interface BankAccountModel extends BaseModel {
        Observable<BankAccount> attachBankAccount(TokenRequest request);
    }

}
