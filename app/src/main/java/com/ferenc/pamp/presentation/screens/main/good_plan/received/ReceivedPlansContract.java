package com.ferenc.pamp.presentation.screens.main.good_plan.received;

import com.ferenc.pamp.data.model.base.ListResponse;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.presentation.base.BaseModel;
import com.ferenc.pamp.presentation.base.BaseView;
import com.ferenc.pamp.presentation.base.content.ContentView;
import com.ferenc.pamp.presentation.base.refreshable.RefreshablePresenter;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by
 * Ferenc on 2017.11.21..
 */

public interface ReceivedPlansContract {
    interface View extends ContentView, BaseView<Presenter> {
        void setReceivedGoodPlanList(List<GoodDealResponse> _receivedGoodPlansList);

        void addReceivedGoodPlanList(List<GoodDealResponse> _receivedGoodPlansList);

        void sharePlayStoreLincInSMS();
    }

    interface Presenter extends RefreshablePresenter {
        void sharePlayStoreLincInSMS();

        void loadNextPage();
    }

    interface Model extends BaseModel {
        Observable<ListResponse<GoodDealResponse>> getReceivedGoodDeal(int _page);

    }
}
