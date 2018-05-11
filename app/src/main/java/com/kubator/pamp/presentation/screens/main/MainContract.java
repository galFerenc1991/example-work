package com.kubator.pamp.presentation.screens.main;

import com.kubator.pamp.data.model.home.good_deal.ConnectGoodDealResponse;
import com.kubator.pamp.presentation.base.BaseModel;
import com.kubator.pamp.presentation.base.BasePresenter;
import com.kubator.pamp.presentation.base.BaseView;

import io.reactivex.Observable;

/**
 * Created by
 * Ferenc on 2017.12.07..
 */

public interface MainContract {
    interface View extends BaseView<Presenter> {

        void openProposedFlow();

        void openChat(String _id);

        void refreshReceivedList();
    }

    interface Presenter extends BasePresenter {
        void connectGoodDeal(String _id);
    }

    interface Model extends BaseModel {
        Observable<ConnectGoodDealResponse> connectGoodDeal(String _id);
    }
}
