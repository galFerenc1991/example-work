package com.ferenc.pamp.presentation.screens.main.good_plan.proposed;

import com.ferenc.pamp.data.api.exceptions.ConnectionLostException;
import com.ferenc.pamp.presentation.screens.main.chat.chat_relay.ProposeRefreshRelay;
import com.ferenc.pamp.presentation.screens.main.chat.chat_relay.ReceivedRefreshRelay;
import com.ferenc.pamp.presentation.screens.main.good_plan.proposed.propose_relay.ProposeRelay;
import com.ferenc.pamp.presentation.utils.Constants;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by
 * Ferenc on 2017.11.21..
 */

public class ProposedPlansPresenter implements ProposedPlansContract.Presenter {

    private ProposedPlansContract.View mView;
    private ProposedPlansContract.Model mModel;
    private CompositeDisposable mCompositeDisposable;
    private ProposeRelay mProposeRelay;
    private ProposeRefreshRelay mProposeRefreshRelay;

    private int page;
    private int totalPages = Integer.MAX_VALUE;
    private boolean needRefresh;

    public ProposedPlansPresenter(ProposedPlansContract.View mView,
                                  ProposedPlansContract.Model _goodDealRepository,
                                  ProposeRelay _proposeRelay,
                                  ProposeRefreshRelay _proposeRefreshRelay) {
        this.mView = mView;
        this.mModel = _goodDealRepository;
        this.mCompositeDisposable = new CompositeDisposable();
        this.mProposeRelay = _proposeRelay;
        this.mProposeRefreshRelay = _proposeRefreshRelay;
        this.page = 1;
        needRefresh = true;

        mView.setPresenter(this);
    }

    @Override
    public void openProposerFragment() {
        mProposeRelay.proposeRelay.accept(true);
    }

    @Override
    public void subscribe() {
        if (needRefresh) {
            mView.showProgressMain();
            loadData(1);
        }
        mCompositeDisposable.add(mProposeRefreshRelay.proposeRefreshRelay.subscribe(aBoolean -> onRefresh()));
    }

    private void loadData(int _page) {
        mCompositeDisposable.add(mModel.getProposedGoodDeal(_page)
                .subscribe(goodDealResponseListResponse -> {
                    mView.hideProgress();
                    this.page = _page;
                    totalPages = goodDealResponseListResponse.meta.pages;
                    if (page == 1) {
                        mView.setProposedGoodPlanList(goodDealResponseListResponse.data);
                        needRefresh = goodDealResponseListResponse.data.isEmpty();
                        if (goodDealResponseListResponse.data.isEmpty())
                            mView.showPlaceholder(Constants.PlaceholderType.EMPTY);
                    } else {
                        mView.addProposedGoodPlanList(goodDealResponseListResponse.data);
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
