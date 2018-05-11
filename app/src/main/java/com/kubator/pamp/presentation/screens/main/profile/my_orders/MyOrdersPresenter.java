package com.kubator.pamp.presentation.screens.main.profile.my_orders;

import android.util.Log;

import com.kubator.pamp.data.model.home.orders.Order;
import com.kubator.pamp.presentation.screens.main.profile.my_orders.adapter.MyOrderDH;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by shonliu on 1/10/18.
 */

public class MyOrdersPresenter implements MyOrdersContract.Presenter {

    private MyOrdersContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private MyOrdersContract.Model mModel;
    private int mPage = 1;
    private int mTotalPages = Integer.MAX_VALUE;


    public MyOrdersPresenter(MyOrdersContract.View _view, MyOrdersContract.Model _model) {
        mView = _view;
        mModel = _model;
        mCompositeDisposable = new CompositeDisposable();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        getMyOrders(mPage, false);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    private void getMyOrders(int _page, boolean isLoadMore) {

        if (!isLoadMore)
            mView.showProgressBar();
        else
            mView.showPaginationProgressBar();

        mCompositeDisposable.add(mModel.getMyOrders(_page)
                .subscribe(orderListResponse -> {

                    mView.setPlaceholderVisibility(orderListResponse.data.size() == 0);
                    Log.d("getMyOrders", "successfully");

                    ArrayList<MyOrderDH> orders = new ArrayList<>();

                    for (Order order : orderListResponse.data)
                        orders.add(new MyOrderDH(order.getId(), order.getDeal().product, order.getDeal().title, order.getCreatedAt()));

                    if (!isLoadMore) {
                        mView.hideProgressBar();
                        mView.setOrdersList(orders);
                    } else {
                        mView.hidePaginationProgressBar();
                        mView.addOrdersList(orders);
                    }

                    mTotalPages = orderListResponse.meta.pages;
                    mPage++;
                }, e -> {
                    if (!isLoadMore)
                        mView.hideProgressBar();
                    else
                        mView.hidePaginationProgressBar();

                    Log.d("getMyOrders", "Error while getting my orders: " + e.getMessage());
                }));
    }

    @Override
    public void loadNextPage() {
        if (mPage - 1 != mTotalPages) {
            getMyOrders(mPage, true);
        }
    }

    @Override
    public void selectItem(MyOrderDH item, int position) {
        mView.openOrderPreview(item.getOrderId());
    }
}
