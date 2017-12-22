package com.ferenc.pamp.presentation.screens.main.chat.participants.participants_list;

import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;
import com.ferenc.pamp.presentation.screens.main.chat.participants.participants_adapter.ParticipantsDH;

import java.util.List;

/**
 * Created by shonliu on 12/22/17.
 */

public interface ParticipantsListContract {

    interface View extends BaseView<Presenter> {

        void setParticipantsList(List<ParticipantsDH> _list);

    }

    interface Presenter extends BasePresenter {


    }
}
