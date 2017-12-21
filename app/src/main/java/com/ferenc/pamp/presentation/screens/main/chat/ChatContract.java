package com.ferenc.pamp.presentation.screens.main.chat;

import com.ferenc.pamp.data.model.home.good_deal.GoodDealRequest;
import com.ferenc.pamp.presentation.base.BaseModel;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;

/**
 * Created by
 * Ferenc on 2017.12.17..
 */

public interface ChatContract {
    interface View extends BaseView<Presenter> {
        void shareGoodDeal();

        void openSettingsDialog();

        void showParticipants();

        void setParticipants(String participants);
    }

    interface Presenter extends BasePresenter {
        void clickedShare();

        void clickedSettings();

        void setParticipants();

        void clickedParticipants();

    }

    interface Model extends BaseModel {

    }
}
