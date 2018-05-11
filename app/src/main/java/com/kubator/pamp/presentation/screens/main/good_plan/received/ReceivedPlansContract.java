package com.kubator.pamp.presentation.screens.main.good_plan.received;

import com.kubator.pamp.data.model.base.ListResponse;
import com.kubator.pamp.data.model.home.good_deal.GoodDealResponse;
import com.kubator.pamp.presentation.base.BaseModel;
import com.kubator.pamp.presentation.base.BaseView;
import com.kubator.pamp.presentation.base.content.ContentView;
import com.kubator.pamp.presentation.base.refreshable.RefreshablePresenter;

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

        void openReBroadcastFlow();

        void sharePlayStoreLincInSMS();

        void checkSendSMSPermission();

        boolean isSendSMSPermissionNotGranted();
    }

    interface Presenter extends RefreshablePresenter {
        void sharePlayStoreLincInSMS();

        void loadNextPage();
    }

    interface Model extends BaseModel {
        Observable<ListResponse<GoodDealResponse>> getReceivedGoodDeal(int _page);

    }
}
