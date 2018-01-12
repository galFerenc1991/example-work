package com.ferenc.pamp.presentation.utils;

import com.ferenc.pamp.R;

/**
 * Created by
 * Ferenc on 2018.01.09..
 */

public abstract class CreditCardImageManager {

    private static int drawable = R.drawable.ic_unknown;

    public static int getBrandImage(String _brand) {
        switch (_brand) {
            case Constants.AMERICAN_EXPRESS:
                drawable = R.drawable.ic_american_express;
                break;
            case Constants.DINERS_CLUB:
                drawable = R.drawable.ic_diners;
                break;
            case Constants.DISCOVER:
                drawable = R.drawable.ic_discover;
                break;
            case Constants.JCB:
                drawable = R.drawable.ic_jcb;
                break;
            case Constants.VISA:
                drawable = R.drawable.ic_visa;
                break;
            case Constants.MASTERCARD:
                drawable = R.drawable.ic_mastercard;
                break;
        }
        return drawable;
    }
}
