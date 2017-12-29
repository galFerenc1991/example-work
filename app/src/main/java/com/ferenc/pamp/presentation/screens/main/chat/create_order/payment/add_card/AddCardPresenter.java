package com.ferenc.pamp.presentation.screens.main.chat.create_order.payment.add_card;

import com.ferenc.pamp.data.model.auth.TokenRequest;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.data.model.home.orders.OrderRequest;
import com.ferenc.pamp.presentation.base.models.BankCard;
import com.ferenc.pamp.presentation.base.models.ExpDate;
import com.ferenc.pamp.presentation.utils.GoodDealResponseManager;
import com.ferenc.pamp.presentation.utils.ToastManager;
import com.ferenc.pamp.presentation.utils.ValidationManager;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by
 * Ferenc on 2017.12.24..
 */

public class AddCardPresenter implements AddCardContract.Presenter {

    private AddCardContract.View mView;
    private AddCardContract.CreateCardModel mCreateCardModel;
    private CompositeDisposable mCompositeDisposable;
    private GoodDealResponseManager mGoodDealResponseManager;
    private String mCardNumber;
    private String mCardExpirationDate;
    private int mCardExpMonth;
    private int mCardExpYear;
    private String mCardCVV;
    private int mQuantity;

    public AddCardPresenter(AddCardContract.View _view, AddCardContract.CreateCardModel _model, GoodDealResponseManager _goodDealResponseManager, int _quantity) {
        this.mView = _view;
        this.mCreateCardModel = _model;
        this.mGoodDealResponseManager = _goodDealResponseManager;
        this.mCompositeDisposable = new CompositeDisposable();
        this.mQuantity = _quantity;

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void clickedCardNumber(int _requestCode) {
        mView.openCardNumberInputScreen(_requestCode);
    }

    @Override
    public void clickedExpirationDate(int _requestCode) {
        mView.openCardExpirationInputScreen(_requestCode);
    }

    @Override
    public void clickedCardCVV(int _requestCode) {
        mView.openCardCVVInputScreen(_requestCode);
    }

    @Override
    public void saveCardNumber(String _number) {
        mCardNumber = _number;
        mView.setCardNumber(_number);
    }

    @Override
    public void saveExpirationDate(ExpDate _expDate) {
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
        int errCodeCardNumber = ValidationManager.validateText(mCardNumber);
        int errCodeCardExpirationDate = ValidationManager.validateText(mCardExpirationDate);
        int errCodeCardCVV = ValidationManager.validateText(mCardCVV);

        if (errCodeCardNumber == ValidationManager.OK
                && errCodeCardExpirationDate == ValidationManager.OK
                && errCodeCardCVV == ValidationManager.OK) {
            mView.getTokenWithStripe(new BankCard(mCardNumber, mCardExpMonth, mCardExpYear, mCardCVV));
        } else {
            ToastManager.showToast("All fields required");
        }
    }

    @Override
    public void createOrder(String _token) {
        mView.showProgressMain();
        mCompositeDisposable.add(mCreateCardModel.createCard(new TokenRequest(_token), new OrderRequest.Builder()
                .setDealId(mGoodDealResponseManager.getGoodDealResponse().id)
                .setQuantity(mQuantity)
                .build())
                .subscribe(card -> {
                    mView.hideProgress();
                    GoodDealResponse goodDealResponse = mGoodDealResponseManager.getGoodDealResponse();
                    goodDealResponse.hasOrders = true;
                    mGoodDealResponseManager.saveGoodDealResponse(goodDealResponse);
                    mView.openSetNewCardScreen(card.getBrand(), "****" + card.getLast4());
                }, throwable -> {
                    ToastManager.showToast("Error ");
                }));
    }

    @Override
    public void unsubscribe() {

    }
}
