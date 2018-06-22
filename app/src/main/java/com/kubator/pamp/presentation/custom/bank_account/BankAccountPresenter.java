package com.kubator.pamp.presentation.custom.bank_account;

import android.util.Log;

import com.kubator.pamp.PampApp_;
import com.kubator.pamp.data.model.home.bank_account.Address;
import com.kubator.pamp.data.model.home.bank_account.BankAccountRequest;
import com.kubator.pamp.presentation.utils.Constants;
import com.kubator.pamp.presentation.utils.SignedUserManager;
import com.kubator.pamp.presentation.utils.ToastManager;
import com.kubator.pamp.presentation.utils.ValidationManager;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.BankAccount;
import com.stripe.android.model.Token;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by
 * Ferenc on 2018.01.04..
 */

public class BankAccountPresenter implements BankAccountContract.Presenter {

    private BankAccountContract.View mView;
    private BankAccountContract.BankAccountModel mBankAccountModel;
    private SignedUserManager mSignedUserManager;
    private String mCountry;
    private CompositeDisposable mCompositeDisposable;
    private String mCountryCode;
    private Calendar mBirthDate;
    private String mCountryFromAddress = "";
    private String mCity = "";
    private String mStreet = "";
    private String mPostalCode = "";
    private boolean isUpdate;

    public BankAccountPresenter(BankAccountContract.View _view,
                                BankAccountContract.BankAccountModel _bankAccountModel,
                                SignedUserManager _signedUserManager) {
        this.mView = _view;
        mBankAccountModel = _bankAccountModel;
        mSignedUserManager = _signedUserManager;
        mCompositeDisposable = new CompositeDisposable();

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        com.kubator.pamp.data.model.common.BankAccount bankAccount = mSignedUserManager.getCurrentUser().getBankAccount();
        if (bankAccount != null) {
            Address address = bankAccount.getAddress();
//            mCountryFromAddress = address.getCountry();
//            mCity = address.getCity();
//            mStreet = address.getLine1();
//            mPostalCode = address.getPostalCode();

            mView.setCountry(bankAccount.getCountry());
            mView.setUserBirthDay(convertServerDateToString(bankAccount.getDateOfBirthday()));
            mView.setSelectedAddress(address.getPostalCode() + ", " + address.getLine1() + ", " + address.getCity() + ", " + address.getCountry());
            mView.setIBan(bankAccount.getCountry() + "*****" + bankAccount.getLast4());
        }
    }

    private String getDateInString(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy", Locale.FRANCE);
        return sdf.format(calendar.getTime());
    }

    private String convertServerDateToString(long _dateInMillis) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(_dateInMillis);
        return getDateInString(date);
    }

    @Override
    public void clickedValidate(String _iban) {
        mView.showProgressMain();

//        int errCodeIban = ValidationManager.validateIban(_iban);
//        int errCodeCountryCode = ValidationManager.validateText(mCountryCode);
//        int errCodeCity = ValidationManager.validateName(mCity);
//        if (errCodeIban == ValidationManager.OK &&
//                errCodeCountryCode == ValidationManager.OK &&
//                errCodeCity == ValidationManager.OK &&
//                mBirthDate != null) {
//
//            mView.showProgressMain();
//
//        } else {
//            ToastManager.showToast("Please fill all required field");
//        }

        int errCodeIban = ValidationManager.validateText(_iban);
        int errCodeCountryCode = ValidationManager.validateText(mCountryCode);
        int errCodeCity = ValidationManager.validateName(mCity);

        if (mSignedUserManager.getCurrentUser().getBankAccount() != null) {
            if (errCodeIban == ValidationManager.OK && errCodeCountryCode == ValidationManager.OK) {
                getStripeToken(_iban, true);
            } else updateBankAccount("");

        } else {
            if (errCodeIban == ValidationManager.OK &&
                    errCodeCountryCode == ValidationManager.OK &&
                    errCodeCity == ValidationManager.OK &&
                    mBirthDate != null) {
                getStripeToken(_iban, false);
            } else {
                mView.hideProgress();
                ToastManager.showToast("Please fill all required fields");
            }
        }

    }

    private void getStripeToken(String _iban, boolean isUpdate) {
        Stripe stripe = new Stripe(PampApp_.getInstance());
        stripe.setDefaultPublishableKey(Constants.STRIPE_LIVE_KEY);
//        stripe.setDefaultPublishableKey(Constants.STRIPE_TEST_KEY);
//        BankAccount bankAccount = new BankAccount("DE89370400440532013000", "de", "eur", "");
        BankAccount bankAccount = new BankAccount(_iban, mCountryCode, "eur", "");
        stripe.createBankAccountToken(bankAccount, new TokenCallback() {
            @Override
            public void onError(Exception error) {
                mView.hideProgress();
                ToastManager.showToast("Error" + error.getMessage());
                Log.d("Error", error.getLocalizedMessage());
            }

            @Override
            public void onSuccess(Token token) {
                ToastManager.showToast("Success");
                if (isUpdate) updateBankAccount(token.getId());
                else attachBankAccount(token.getId());
            }
        });
    }

    @Override
    public void setSelectedAddress(String _fullAddress, String _country, String _city, String _street, String _postalCode) {
        this.mCountryFromAddress = _country;
        this.mCity = _city;
        this.mStreet = _street;
        this.mPostalCode = _postalCode;
        mView.setSelectedAddress(_fullAddress);
    }

    private void attachBankAccount(String _token) {
        mCompositeDisposable.add(mBankAccountModel.attachBankAccount(new BankAccountRequest(_token, mBirthDate.getTimeInMillis(),
                new Address.Builder()
                        .setCountry(mCountryFromAddress)
                        .setCity(mCity)
                        .setStreet(mStreet)
                        .setPostalCode(mPostalCode)
                        .build()))
                .subscribe(bankAccount -> {
                    mView.hideProgress();
                    mView.showSuccessEndFlowScreen();
                    ToastManager.showToast("Attach Bank Account Success:");
                }, throwable -> {
                    mView.hideProgress();
                    ToastManager.showToast("Attach bank Account Error");
                }));

    }

    private void updateBankAccount(String _token) {
        int errCodeToken = ValidationManager.validateName(_token);
        int errCodeCity = ValidationManager.validateName(mCity);

        if (errCodeToken == ValidationManager.EMPTY &&
                errCodeCity == ValidationManager.EMPTY &&
                mBirthDate == null) {
            ToastManager.showToast("You do not change any parameter");
        } else {
//            BankAccountRequest bankAccountRequest = new BankAccountRequest();
//                bankAccountRequest.setDateOfBirthday(mBirthDate.getTimeInMillis());
//                bankAccountRequest.setToken(_token);
//                bankAccountRequest.setAddress();

            boolean isAddressOk = !mCountryFromAddress.equals("")
                    && !mCity.equals("")
                    && !mStreet.equals("")
                    && !mPostalCode.equals("");

            BankAccountRequest bankAccountRequest = new BankAccountRequest(
                    !_token.equals("") ? _token : null,
                    mBirthDate != null ? mBirthDate.getTimeInMillis() : -1,
                    isAddressOk ? new Address.Builder()
                            .setCountry(mCountryFromAddress)
                            .setCity(mCity)
                            .setStreet(mStreet)
                            .setPostalCode(mPostalCode)
                            .build() : null);

            mCompositeDisposable.add(mBankAccountModel.updateBankAccount(bankAccountRequest)
                    .subscribe(bankAccount -> {
                        mView.hideProgress();
                        mView.showSuccessEndFlowScreen();
                        ToastManager.showToast("Update Bank Account Success:");
                    }, throwable -> {
                        mView.hideProgress();
                        ToastManager.showToast("Update bank Account Error");
                    }));
        }
    }

    @Override
    public void setSelectedCountry(String country) {
        mCountry = country;

        String[] isoCountries = Locale.getISOCountries();
        for (String _country : isoCountries) {
            Locale locale = new Locale("en", _country);
            String name = locale.getCountry();
            String displayName = locale.getDisplayCountry(Locale.ENGLISH);

            if (displayName.toUpperCase().equals(country)) {
                if (!name.equals(mCountryCode)) {
                    isUpdate = true;
                }
                mCountryCode = name;
                mView.setCountry(mCountryCode);
            }

        }
    }

    @Override
    public void clickedCountry() {
        mView.openCountryPicker(mCountry);
    }

    @Override
    public void clickedAddress() {
        mView.openAutocompletePlaceScreen();
    }

    private String getCloseDateInString(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM 'at' yyyy", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    @Override
    public void setBirthDay(Calendar _calendar) {
//        if (_calendar.getTimeInMillis() != mBirthDate.getTimeInMillis())
        mBirthDate = _calendar;
        mView.setUserBirthDay(getCloseDateInString(_calendar));

    }

    @Override
    public void clickedBirthDay() {
        mView.opedDatePicker(mBirthDate == null ? Calendar.getInstance() : mBirthDate);

    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
