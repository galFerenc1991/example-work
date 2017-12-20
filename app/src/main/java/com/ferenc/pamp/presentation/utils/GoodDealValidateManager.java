package com.ferenc.pamp.presentation.utils;

import com.ferenc.pamp.data.model.home.good_deal.GoodDealRequest;

/**
 * Created by
 * Ferenc on 2017.11.30..
 */
public abstract class GoodDealValidateManager {

    public static boolean validate(GoodDealRequest _goodDeal) {
        int errCodeName = ValidationManager.validateText(_goodDeal.getProduct());
        int errCodeDescription = ValidationManager.validateText(_goodDeal.getDescription());
        int errCodePrice = ValidationManager.validateDooble(String.valueOf(_goodDeal.getPrice()));
        int errCodeCloseDate = ValidationManager.validateLongNumber(String.valueOf(_goodDeal.getClosingDate()));
        int errCodeDeliveryPlace = ValidationManager.validateText(_goodDeal.getDeliveryAddress());
        int errCodeStartEndDate = ValidationManager.validateLongNumber(String.valueOf(_goodDeal.getDeliveryStartDate()));
        int errCodePriceDescription = ValidationManager.validateText(String.valueOf(_goodDeal.getUnit()));

        if (errCodeName == ValidationManager.OK && errCodeDescription == ValidationManager.OK && errCodePrice == ValidationManager.OK && errCodeCloseDate == ValidationManager.OK && errCodeDeliveryPlace == ValidationManager.OK && errCodeStartEndDate == ValidationManager.OK && errCodePriceDescription == ValidationManager.OK) {
            return true;
        } else {
            return false;
        }
    }
}
