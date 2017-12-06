package com.ferenc.pamp.presentation.utils;

import com.ferenc.pamp.presentation.base.models.GoodDeal;

/**
 * Created by
 * Ferenc on 2017.11.30..
 */
public abstract class GoodDealValidateManager {

    public static boolean validate(GoodDeal _goodDeal) {
        int errCodeName = ValidationManager.validateText(_goodDeal.getName());
        int errCodeDescription = ValidationManager.validateText(_goodDeal.getDescription());
        int errCodePrice = ValidationManager.validateNumber(String.valueOf(_goodDeal.getPrice()));
        int errCodeCloseDate = ValidationManager.validateLongNumber(String.valueOf(_goodDeal.getCloseDate()));
        int errCodeDeliveryPlace = ValidationManager.validateText(_goodDeal.getDeliveryPlace());
        int errCodeStartEndDate = ValidationManager.validateLongNumber(String.valueOf(_goodDeal.getStartDelivery()));
        int errCodePriceDescription = ValidationManager.validateText(String.valueOf(_goodDeal.getPriceDescription()));

        if (errCodeName == ValidationManager.OK && errCodeDescription == ValidationManager.OK && errCodePrice == ValidationManager.OK && errCodeCloseDate == ValidationManager.OK && errCodeDeliveryPlace == ValidationManager.OK && errCodeStartEndDate == ValidationManager.OK && errCodePriceDescription == ValidationManager.OK) {
            return true;
        } else {
            return false;
        }
    }
}
