package com.ferenc.pamp.presentation.screens.main;

import com.ferenc.pamp.data.model.home.good_deal.ConnectGoodDealResponse;
import com.ferenc.pamp.presentation.base.BaseModel;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;

import io.reactivex.Observable;

/**
 * Created by
 * Ferenc on 2017.12.07..
 */

public interface MainContract {
    interface View extends BaseView<Presenter> {

        void openProposedFlow();

        void openChat(String _id);
    }

    interface Presenter extends BasePresenter {
        void connectGoodDeal(String _id);
    }

    interface Model extends BaseModel {
        Observable<ConnectGoodDealResponse> connectGoodDeal(String _id);
    }
}
