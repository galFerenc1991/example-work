package com.kubator.pamp.presentation.screens.main.chat.create_order.payment.add_card;

import com.kubator.pamp.data.model.auth.TokenRequest;
import com.kubator.pamp.data.model.common.Card;
import com.kubator.pamp.presentation.base.BaseModel;
import com.kubator.pamp.presentation.base.BasePresenter;
import com.kubator.pamp.presentation.base.BaseView;
import com.kubator.pamp.presentation.base.content.ContentView;
import com.kubator.pamp.presentation.base.models.BankCard;
import com.kubator.pamp.presentation.base.models.BankCardNumber;
import com.kubator.pamp.presentation.base.models.ExpDate;

import io.reactivex.Observable;

/**
 * Created by
 * Ferenc on 2017.12.24..
 */

public interface AddCardContract {

    interface View extends ContentView, BaseView<Presenter> {
        void openCardNumberInputScreen(int _requestCode, BankCardNumber _bankCardNumber);

        void openCardExpirationInputScreen(int _requestCode, ExpDate _expDate);

        void openCardCVVInputScreen(int _requestCode, String _cardCVV);

        void setCardNumber(String _cardNumber);

        void setExpirationDate(String _expirationDate);

        void setCVV(String _cvv);

        void openSetNewCardScreen(String _cardType, String _last4, String _token, double _quantity);

        void getTokenWithStripe(BankCard _bankCard);

        void closeAddCardScreen(Card _card);
    }

    interface Presenter extends BasePresenter {
        void clickedCardNumber(int _requestCode);

        void clickedExpirationDate(int _requestCode);

        void clickedCardCVV(int _requestCode);

        void saveCardNumber(BankCardNumber _number);

        void saveExpirationDate(ExpDate _expDate);

        void saveCVV(String _cvv);

        void clickedValidate();

        void createCard(String _token, String _brand, String _last4);

    }

    interface Model extends BaseModel {
        Observable<Card> createCard(TokenRequest request);
    }

}
