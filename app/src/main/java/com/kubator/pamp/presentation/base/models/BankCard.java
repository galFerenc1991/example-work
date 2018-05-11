package com.kubator.pamp.presentation.base.models;

/**
 * Created by
 * Ferenc on 2017.12.28..
 */

public class BankCard {
    private String cardNumber;
    private int cardExpMonth;
    private int cardExpYear;
    private String cardCVC;

    public BankCard(String cardNumber, int cardExpMonth, int cardExpYear, String cardCVC) {
        this.cardNumber = cardNumber;
        this.cardExpMonth = cardExpMonth;
        this.cardExpYear = cardExpYear;
        this.cardCVC = cardCVC;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public int getCardExpMonth() {
        return cardExpMonth;
    }

    public int getCardExpYear() {
        return cardExpYear;
    }

    public String getCardCVC() {
        return cardCVC;
    }
}
