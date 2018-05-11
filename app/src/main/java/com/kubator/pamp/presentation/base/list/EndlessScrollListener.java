package com.kubator.pamp.presentation.base.list;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by
 * Ferenc on 2017.12.08..
 */

public class EndlessScrollListener extends RecyclerView.OnScrollListener {
    private final int visibleThreshold = 3;
    private int previousTotalItemCount = 0;
    private boolean loading = true;
    private int totalItemCount;
    private int lastVisibleItemPosition;
    private int firstVisibleItem;
    private LinearLayoutManager mLayoutManager;
    private OnNextPageListener listener;
    private boolean isReverseLayout;

    public EndlessScrollListener(LinearLayoutManager mLayoutManager, OnNextPageListener listener) {
        this.mLayoutManager = mLayoutManager;
        this.listener = listener;
        isReverseLayout = mLayoutManager.getReverseLayout();
    }
    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more url,
    // but first we check if we are waiting for the previous load to finish.
    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        firstVisibleItem = mLayoutManager.findLastCompletelyVisibleItemPosition();
        totalItemCount = mLayoutManager.getItemCount();
        lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }
        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more url.
        // If we do need to reload some more url, we execute onLoadMore to fetch the url.
        // threshold should reflect how many total columns there are too
        if (!loading && needLoading()) {
            loading = listener.onLoadMore();
        }
    }

    private boolean needLoading() {
        if (isReverseLayout)
            return totalItemCount - firstVisibleItem <= visibleThreshold;
        else
            return (visibleThreshold + firstVisibleItem) >= totalItemCount;
    }

    public void reset() {
        previousTotalItemCount = 0;
    }
}
