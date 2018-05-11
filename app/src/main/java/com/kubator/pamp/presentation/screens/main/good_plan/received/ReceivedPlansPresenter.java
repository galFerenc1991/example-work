package com.kubator.pamp.presentation.screens.main.good_plan.received;


import com.kubator.pamp.data.api.exceptions.ConnectionLostException;
import com.kubator.pamp.presentation.screens.main.chat.chat_relay.ReceivedRefreshRelay;
import com.kubator.pamp.presentation.screens.main.good_plan.received.receive_relay.ReceiveRelay;
import com.kubator.pamp.presentation.screens.main.good_plan.warning_relay.WarningRelay;
import com.kubator.pamp.presentation.utils.Constants;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by
 * Ferenc on 2017.11.21..
 */

public class ReceivedPlansPresenter implements ReceivedPlansContract.Presenter {

    private ReceivedPlansContract.View mView;
    private ReceivedPlansContract.Model mModel;
    private CompositeDisposable mCompositeDisposable;
    private ReceiveRelay mReceiveRelay;
    private ReceivedRefreshRelay mReceiveRefreshRelay;
    private WarningRelay mWarningRelay;

    private int page;
    private int totalPages = Integer.MAX_VALUE;
    private boolean needRefresh;

    public ReceivedPlansPresenter(ReceivedPlansContract.View _view,
                                  ReceivedPlansContract.Model _model,
                                  ReceiveRelay _receiveRelay,
                                  ReceivedRefreshRelay _receiveRefreshRelay,
                                  WarningRelay _warningRelay) {
        this.mView = _view;
        this.mModel = _model;
        this.mReceiveRelay = _receiveRelay;
        this.mReceiveRefreshRelay = _receiveRefreshRelay;
        this.mWarningRelay = _warningRelay;
        this.mCompositeDisposable = new CompositeDisposable();
        this.page = 1;
        needRefresh = true;

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        if (needRefresh) {
            mView.showProgressMain();
            loadData(1);
        }
        mCompositeDisposable.add(mReceiveRelay.receiveRelay.subscribe(aBoolean -> {
            mView.openReBroadcastFlow();
        }));
        mCompositeDisposable.add(mReceiveRefreshRelay.receivedRefreshRelay.subscribe(aBoolean -> onRefresh()));
    }

    private void loadData(int _page) {
        mCompositeDisposable.add(mModel.getReceivedGoodDeal(_page)
                .subscribe(goodDealResponseListResponse -> {
                    mView.hideProgress();
                    this.page = _page;
                    totalPages = goodDealResponseListResponse.meta.pages;
                    mWarningRelay.proposeRelay.accept(goodDealResponseListResponse.meta.attention);
                    if (page == 1) {
                        mView.setReceivedGoodPlanList(goodDealResponseListResponse.data);
                        needRefresh = goodDealResponseListResponse.data.isEmpty();
                        if (goodDealResponseListResponse.data.isEmpty())
                            mView.showPlaceholder(Constants.PlaceholderType.EMPTY);
                    } else {
                        mView.addReceivedGoodPlanList(goodDealResponseListResponse.data);
                    }
                }, throwableConsumer)
        );
    }

    @Override
    public void loadNextPage() {
        if (page < totalPages) {
            mView.showProgressPagination();
            loadData(page + 1);
        }
    }

    @Override
    public void onRefresh() {
        loadData(1);
    }

    @Override
    public void sharePlayStoreLincInSMS() {
        mView.sharePlayStoreLincInSMS();
    }

    private Consumer<Throwable> throwableConsumer = throwable -> {
        throwable.printStackTrace();
        mView.hideProgress();
        if (throwable instanceof ConnectionLostException) {
            mView.showErrorMessage(Constants.MessageType.CONNECTION_PROBLEMS);
        } else {
            mView.showErrorMessage(Constants.MessageType.UNKNOWN);
        }
    };

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
