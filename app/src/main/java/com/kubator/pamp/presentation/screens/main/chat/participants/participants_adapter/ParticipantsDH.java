package com.kubator.pamp.presentation.screens.main.chat.participants.participants_adapter;

import android.content.Context;

import com.kubator.pamp.data.model.common.User;
import com.michenko.simpleadapter.RecyclerDH;

/**
 * Created by shonliu on 12/22/17.
 */

public class ParticipantsDH implements RecyclerDH {

    private User mUser;
    private Context mContext;

    public ParticipantsDH(User _user, Context _context) {
        mUser = _user;
        mContext = _context;
    }

    public User getUser() {
        return mUser;
    }

    public Context getContext() {
        return mContext;
    }
}
