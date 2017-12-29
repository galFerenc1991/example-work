package com.ferenc.pamp.presentation.screens.main.chat.create_order.payment.add_card;

import com.ferenc.pamp.data.model.auth.TokenRequest;
import com.ferenc.pamp.data.model.common.Card;
import com.ferenc.pamp.data.model.home.orders.Order;
import com.ferenc.pamp.data.model.home.orders.OrderRequest;
import com.ferenc.pamp.presentation.base.BaseModel;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;
import com.ferenc.pamp.presentation.base.content.ContentView;
import com.ferenc.pamp.presentation.base.models.BankCard;
import com.ferenc.pamp.presentation.base.models.ExpDate;

import io.reactivex.Observable;

/**
 * Created by
 * Ferenc on 2017.12.24..
 */

public interface AddCardContract {

    interface View extends ContentView, BaseView<Presenter> {
        void openCardNumberInputScreen(int _requestCode);

        void openCardExpirationInputScreen(int _requestCode);

        void openCardCVVInputScreen(int _requestCode);

        void setCardNumber(String _cardNumber);

        void setExpirationDate(String _expirationDate);

        void setCVV(String _cvv);

        void openSetNewCardScreen(String _cardType, String _last4, String _token, int _quantity);

        void getTokenWithStripe(BankCard _bankCard);
    }

    interface Presenter extends BasePresenter {
        void clickedCardNumber(int _requestCode);

        void clickedExpirationDate(int _requestCode);

        void clickedCardCVV(int _requestCode);

        void saveCardNumber(String _number);

        void saveExpirationDate(ExpDate _expDate);

        void saveCVV(String _cvv);

        void clickedValidate();

        void createCard(String _token, String _brand, String _last4);

    }

}
