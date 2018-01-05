package com.ferenc.pamp.presentation.custom.bank_account;

import android.util.Log;

import com.ferenc.pamp.PampApp_;
import com.ferenc.pamp.data.model.auth.TokenRequest;
import com.ferenc.pamp.presentation.utils.ToastManager;
import com.ferenc.pamp.presentation.utils.ValidationManager;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.BankAccount;
import com.stripe.android.model.Token;

import java.util.Locale;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by
 * Ferenc on 2018.01.04..
 */

public class BankAccountPresenter implements BankAccountContract.Presenter {

    private BankAccountContract.View mView;
    private BankAccountContract.BankAccountModel mBankAccountModel;
    private String mCountry;
    private CompositeDisposable mCompositeDisposable;
    private String mCountryCode;
    private String mIban;

    public BankAccountPresenter(BankAccountContract.View _view, BankAccountContract.BankAccountModel _bankAccountModel) {
        this.mView = _view;
        mBankAccountModel = _bankAccountModel;
        mCompositeDisposable = new CompositeDisposable();

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void clickedValidate(String _iban) {
        mIban = _iban;
        int errCodeIban = ValidationManager.validateText(mIban);
        int errCodeCountryCode = ValidationManager.validateText(mCountryCode);
        if (errCodeIban == ValidationManager.OK && errCodeCountryCode == ValidationManager.OK) {
            mView.showProgressMain();
            Stripe stripe = new Stripe(PampApp_.getInstance());
            stripe.setDefaultPublishableKey("pk_test_JjnCq5JreSkRqrcqgInmTDAn");
//        BankAccount bankAccount = new BankAccount("DE89370400440532013000", "de", "eur", "");
            BankAccount bankAccount = new BankAccount(mIban, mCountryCode, "eur", "");
            stripe.createBankAccountToken(bankAccount, new TokenCallback() {
                @Override
                public void onError(Exception error) {
                    mView.hideProgress();
//                Log.e("Stripe Error",error.getMessage());
                    ToastManager.showToast("Error" + error.getMessage());
                    Log.d("Error", error.getLocalizedMessage());
                }

                @Override
                public void onSuccess(Token token) {
//                Log.e("Bank Token", token.getId());
                    ToastManager.showToast("Success");
                    attachBankAccount(token.getId());
                }
            });
        } else {
            ToastManager.showToast("Something went wrong");
        }

    }

    private void attachBankAccount(String _token) {
        mCompositeDisposable.add(mBankAccountModel.attachBankAccount(new TokenRequest(_token))
                .subscribe(bankAccount -> {
                    mView.hideProgress();
                    mView.showSuccessEndFlowScreen();
                    ToastManager.showToast("Attach Bank Account Success:");
                }, throwable -> {
                    mView.hideProgress();
                    ToastManager.showToast("Attach bank Account Error");
                }));
    }

    @Override
    public void setSelectedCountry(String country) {
        mCountry = country;
        mView.setCountry(country);

        String[] isoCountries = Locale.getISOCountries();
        for (String _country : isoCountries) {
            Locale locale = new Locale("en", _country);
            String name = locale.getCountry();
            String displayName = locale.getDisplayCountry(Locale.ENGLISH);

            if (displayName.toUpperCase().equals(country)) {
                mCountryCode = name;
            }

        }
    }

    @Override
    public void clickedCountry() {
        mView.openCountryPicker(mCountry);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
