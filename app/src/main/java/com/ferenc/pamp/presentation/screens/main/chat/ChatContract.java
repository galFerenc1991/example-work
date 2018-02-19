package com.ferenc.pamp.presentation.screens.main.chat;

import com.ferenc.pamp.data.model.home.good_deal.GoodDealRequest;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.presentation.base.BaseModel;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;

import io.reactivex.Observable;

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

        void hideSettings();

        void hideShareButton();

        void showChat();

        void showProgress();

        void hideProgress();

        void initFromWhere(int _fromWhere);

        void setTitle(String _title);

        void hideParticipantTextView();

        void showSharePopUp();
    }

    interface Presenter extends BasePresenter {
        void clickedShare();

        void clickedSettings();

        void setParticipants();

        void clickedParticipants();

    }

    interface Model extends BaseModel {
        Observable<GoodDealResponse> getDialId(String _dealId);
    }
}
