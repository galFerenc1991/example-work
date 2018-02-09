package com.ferenc.pamp.presentation.screens.main.chat.create_order.payment.add_card;

import com.ferenc.pamp.data.model.auth.TokenRequest;
import com.ferenc.pamp.presentation.base.models.BankCard;
import com.ferenc.pamp.presentation.base.models.BankCardNumber;
import com.ferenc.pamp.presentation.base.models.ExpDate;
import com.ferenc.pamp.presentation.utils.ToastManager;
import com.ferenc.pamp.presentation.utils.ValidationManager;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by
 * Ferenc on 2017.12.24..
 */

public class AddCardPresenter implements AddCardContract.Presenter {

    private AddCardContract.View mView;
    private AddCardContract.Model mModel;
    private CompositeDisposable mCompositeDisposable;
    private String mCardNumberFull;
    private String mCardExpirationDate;
    private int mCardExpMonth;
    private int mCardExpYear;
    private String mCardCVV;
    private double mQuantity;
    private boolean mWithEditProfile;
    private BankCardNumber mBankCardNumber;
    private ExpDate mExpDate;


    public AddCardPresenter(AddCardContract.View _view,
                            double _quantity,
                            AddCardContract.Model _model,
                            boolean _withEditProfile) {
        this.mView = _view;
        this.mModel = _model;
        this.mCompositeDisposable = new CompositeDisposable();
        this.mQuantity = _quantity;
        mCardCVV = "";
        mWithEditProfile = _withEditProfile;

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void clickedCardNumber(int _requestCode) {
        mView.openCardNumberInputScreen(_requestCode, mBankCardNumber);
    }

    @Override
    public void clickedExpirationDate(int _requestCode) {
        mView.openCardExpirationInputScreen(_requestCode, mExpDate);
    }

    @Override
    public void clickedCardCVV(int _requestCode) {
        mView.openCardCVVInputScreen(_requestCode, mCardCVV);
    }

    @Override
    public void saveCardNumber(BankCardNumber _number) {
        mBankCardNumber = _number;
        mCardNumberFull = _number.getFirst4() + _number.getSecond4() + _number.getThird4() + _number.getFourth4();
        mView.setCardNumber(mCardNumberFull);
    }

    @Override
    public void saveExpirationDate(ExpDate _expDate) {
        mExpDate = _expDate;
        mCardExpirationDate = String.valueOf(_expDate.getCardExpMonth()) + "/" + String.valueOf(_expDate.getCardExpYear());
        mCardExpMonth = _expDate.getCardExpMonth();
        mCardExpYear = _expDate.getCardExpYear();
        mView.setExpirationDate(mCardExpirationDate);
    }

    @Override
    public void saveCVV(String _cvv) {
        mCardCVV = _cvv;
        mView.setCVV(_cvv);
    }

    @Override
    public void clickedValidate() {
        int errCodeCardNumber = ValidationManager.validateText(mCardNumberFull);
        int errCodeCardExpirationDate = ValidationManager.validateText(mCardExpirationDate);
        int errCodeCardCVV = ValidationManager.validateText(mCardCVV);

        if (errCodeCardNumber == ValidationManager.OK
                && errCodeCardExpirationDate == ValidationManager.OK
                && errCodeCardCVV == ValidationManager.OK) {
            mView.getTokenWithStripe(new BankCard(mCardNumberFull, mCardExpMonth, mCardExpYear, mCardCVV));
        } else {
            ToastManager.showToast("All fields required");
        }
    }

    @Override
    public void createCard(String _token, String _brand, String _last4) {
        if (mWithEditProfile) {
            mView.showProgressMain();
            mCompositeDisposable.add(mModel.createCard(new TokenRequest(_token))
                    .subscribe(card -> {
                        mView.hideProgress();
                        mView.closeAddCardScreen(card);
                    }, throwable -> {
                        mView.hideProgress();
                        ToastManager.showToast("Add card Fragment Error");
                    }));
        } else {
            mView.openSetNewCardScreen(_brand, "****" + _last4, _token, mQuantity);
        }
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
