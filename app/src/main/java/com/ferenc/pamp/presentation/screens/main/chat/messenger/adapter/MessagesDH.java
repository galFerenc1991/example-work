package com.ferenc.pamp.presentation.screens.main.chat.messenger.adapter;

import com.ferenc.pamp.data.model.common.User;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.data.model.message.MessageResponse;
import com.ferenc.pamp.presentation.utils.Constants;
import com.michenko.simpleadapter.RecyclerDH;

/**
 * Created by shonliu on 12/13/17.
 */

public class MessagesDH implements RecyclerDH {

    private MessageResponse mMessageResponse;
    private GoodDealResponse mGoodDealResponse;
    private User mMyUser;


    public MessagesDH(MessageResponse _messageResponse, GoodDealResponse _goodDealResponse, User _myUser) {
        mMessageResponse = _messageResponse;
        mGoodDealResponse = _goodDealResponse;
        mMyUser = _myUser;
    }

    public MessagesDH(MessageResponse _messageResponse) {
        mMessageResponse = _messageResponse;
    }


    public MessageResponse getMessageResponse() {
        return mMessageResponse;
    }

    public GoodDealResponse getGoodDealResponse() {
        return mGoodDealResponse;
    }

    public User getMyUser() {
        return mMyUser;
    }

    public int getMsgGroupType() {
        return typeDistributor();
    }

    private int typeDistributor() {
        switch (mMessageResponse.code) {
            case Constants.M1_GOOD_DEAL_DIFFUSION:
                return Constants.M1_MSG_GROUP_TYPE;
            case Constants.M2_PRODUCT_ORDERING:
            case Constants.M3_ORDER_CHANGING:
            case Constants.M4_ORDER_CANCELLATION:
                return Constants.M2_M3_M4_MSG_GROUP_TYPE;
            case Constants.M5_GOOD_DEAL_DELIVERY_DATE_CHANGED:
            case Constants.M6_GOOD_DEAL_CLOSING_DATE_CHANGED:
            case Constants.M9_CLOSING_DATE:
            case Constants.M12_DELIVERY_DATE:
                return Constants.M5_M6_M9_M12_MSG_GROUP_TYPE;
            case Constants.M8_GOOD_DEAL_CANCELLATION:
            case Constants.M10_GOOD_DEAL_CLOSING:
                return Constants.M8_M10_MSG_GROUP_TYPE;
            case Constants.M11_1_GOOD_DEAL_CONFIRMATION:
            case Constants.M11_2_GOOD_DEAL_CONFIRMATION_REJECTED:
            case Constants.M11_3_GOOD_DEAL_CONFIRMATION_APPLYED:
                return Constants.M11_1_M11_2_M11_3_MSG_GROUP_TYPE;
            case "":
                return Constants.DEFAULT_MSG_GROUP_TYPE;
            default:
                throw new RuntimeException("MessagesDH :: typeDistributor [Can find needed group type]");
        }
    }
}
