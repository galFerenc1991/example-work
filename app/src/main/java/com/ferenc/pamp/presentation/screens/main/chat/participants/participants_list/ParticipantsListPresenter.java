package com.ferenc.pamp.presentation.screens.main.chat.participants.participants_list;

import android.content.Context;

import com.ferenc.pamp.data.model.common.User;
import com.ferenc.pamp.presentation.screens.main.chat.participants.participants_adapter.ParticipantsDH;
import com.ferenc.pamp.presentation.utils.GoodDealResponseManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shonliu on 12/22/17.
 */

public class ParticipantsListPresenter implements ParticipantsListContract.Presenter {

    private ParticipantsListContract.View mView;
    private GoodDealResponseManager mGoodDealResponseManager;
    private List<ParticipantsDH> mParticipantsDH;
    private Context mContext;

    public ParticipantsListPresenter(ParticipantsListContract.View _view, GoodDealResponseManager _goodDealResponseManager, Context _context) {
        mView = _view;
        mGoodDealResponseManager = _goodDealResponseManager;
        mContext = _context;
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

        mParticipantsDH = new ArrayList<>();
        for (User participant : mGoodDealResponseManager.getGoodDealResponse().recipients)
            mParticipantsDH.add(new ParticipantsDH(participant, mContext));

        mView.setParticipantsList(mParticipantsDH);
    }

    @Override
    public void unsubscribe() {

    }


}
