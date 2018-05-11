package com.kubator.pamp.presentation.screens.main.chat.messenger.adapter;

import android.content.Context;

import com.kubator.pamp.data.model.common.User;
import com.kubator.pamp.data.model.home.good_deal.GoodDealResponse;
import com.kubator.pamp.data.model.message.MessageResponse;
import com.michenko.simpleadapter.RecyclerDH;

/**
 * Created by shonliu on 12/13/17.
 */

public class MessagesDH implements RecyclerDH {

    private MessageResponse mMessageResponse;
    private GoodDealResponse mGoodDealResponse;
    private User mMyUser;
    private Context mContext;
    private int mCode;


    public MessagesDH(MessageResponse _messageResponse, GoodDealResponse _goodDealResponse, User _myUser, Context _context, int _code) {
        mMessageResponse = _messageResponse;
        mGoodDealResponse = _goodDealResponse;
        mMyUser = _myUser;
        mContext = _context;
        mCode = _code;
    }

    MessageResponse getMessageResponse() {
        return mMessageResponse;
    }

    GoodDealResponse getGoodDealResponse() {
        return mGoodDealResponse;
    }

    User getMyUser() {
        return mMyUser;
    }

    Context getContext() {
        return mContext;
    }

    public int getMsgGroupType() {
        return mCode;
    }
}
