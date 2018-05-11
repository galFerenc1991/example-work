package com.kubator.pamp.presentation.base.refreshable;

import com.kubator.pamp.presentation.base.content.ContentFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

/**
 * Created by
 * Ferenc on 2017.11.09..
 */

@EFragment
public abstract class RefreshableFragment extends ContentFragment {

    protected abstract RefreshablePresenter getPresenter();

    @AfterViews
    protected void initRefreshing() {
        swipeContainer_VC.setEnabled(true);
        swipeContainer_VC.setOnRefreshListener(() -> getPresenter().onRefresh());
    }

    public void enableRefreshing(boolean isEnabled) {
        swipeContainer_VC.setEnabled(isEnabled);
    }

    public boolean isRefreshing() {
        return swipeContainer_VC.isRefreshing();
    }

    @Override
    public void showProgressPagination() {
        super.showProgressPagination();
        enableRefreshing(false);
    }

    public boolean isRefreshingEnabled() {
        return swipeContainer_VC.isEnabled();
    }

    @Override
    public void showProgressMain() {
        super.showProgressMain();
        enableRefreshing(false);
    }

    @Override
    public void hideProgress() {
        super.hideProgress();
        enableRefreshing(true);
        if(swipeContainer_VC.isRefreshing()) swipeContainer_VC.setRefreshing(false);
    }
}
