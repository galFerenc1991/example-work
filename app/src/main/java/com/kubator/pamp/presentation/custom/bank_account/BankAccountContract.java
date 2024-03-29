package com.kubator.pamp.presentation.custom.bank_account;

import com.kubator.pamp.data.model.common.BankAccount;
import com.kubator.pamp.data.model.home.bank_account.BankAccountRequest;
import com.kubator.pamp.presentation.base.BaseModel;
import com.kubator.pamp.presentation.base.BasePresenter;
import com.kubator.pamp.presentation.base.BaseView;
import com.kubator.pamp.presentation.base.content.ContentView;

import java.util.Calendar;

import io.reactivex.Observable;

/**
 * Created by
 * Ferenc on 2018.01.04..
 */

public interface BankAccountContract {
    interface View extends ContentView, BaseView<Presenter> {
        void setCountry(String _country);

        void openCountryPicker(String _selectedCountry);

        void openAutocompletePlaceScreen();


        void opedDatePicker(Calendar _calendar);

        void setUserBirthDay(String _birthDay);

        void showSuccessEndFlowScreen();

        void setSelectedAddress(String _fullAddress);

        void setIBan(String _iBan);
    }

    interface Presenter extends BasePresenter {
        void clickedValidate(String _iban);

        void clickedCountry();

        void clickedAddress();

        void clickedBirthDay();

        void setBirthDay(Calendar _calendar);

        void setSelectedAddress(String _fullAddress, String _country, String _city, String _street, String _postalCode);

        void setSelectedCountry(String _country);
    }

    interface BankAccountModel extends BaseModel {
        Observable<BankAccount> attachBankAccount(BankAccountRequest request);
        Observable<BankAccount> updateBankAccount(BankAccountRequest request);
    }

}
