package com.ferenc.pamp.presentation.screens.main.chat.orders;

import com.ferenc.pamp.data.model.home.orders.Order;
import com.ferenc.pamp.data.model.home.orders.OrdersList;
import com.ferenc.pamp.presentation.screens.main.chat.orders.order_adapter.OrderAdapter;
import com.ferenc.pamp.presentation.screens.main.chat.orders.order_adapter.OrderDH;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.GoodDealResponseManager;
import com.ferenc.pamp.presentation.utils.ToastManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by
 * Ferenc on 2017.12.15..
 */

public class OrderPresenter implements OrderContract.Presenter {

    private OrderContract.View mView;
    private OrderContract.Model mModel;
    private OrderContract.GoodDealModel mGoodDealModel;
    private OrderContract.UserModel mUserModel;
    private CompositeDisposable mCompositeDisposable;
    private GoodDealResponseManager mGoodDealResponseManager;
    private boolean mIsOriginal;
    private String mDealStatus;
    private List<OrderDH> mOrders;
    private List<OrderDH> loadedOrders;


    private int page;
    private int totalPages = Integer.MAX_VALUE;
    private boolean needRefresh;

    public OrderPresenter(OrderContract.View _view,
                          OrderContract.Model _model,
                          GoodDealResponseManager _goodDealResponseManager,
                          OrderContract.GoodDealModel _goodDealModel,
                          OrderContract.UserModel _userModel) {
        this.mView = _view;
        this.mModel = _model;
        this.mGoodDealModel = _goodDealModel;
        this.mUserModel = _userModel;
        this.mGoodDealResponseManager = _goodDealResponseManager;
        this.mCompositeDisposable = new CompositeDisposable();
        this.mIsOriginal = !mGoodDealResponseManager.getGoodDealResponse().isResend;
        this.mDealStatus = mGoodDealResponseManager.getGoodDealResponse().state;
        this.page = 1;
        needRefresh = true;
        mOrders = new ArrayList<>();
        loadedOrders = new ArrayList<>();

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        setDealInfo();
        if (mIsOriginal && mDealStatus.equals(Constants.STATE_CLOSED)) mView.showConfButton();
        else mView.hideConfButton();

        if (needRefresh) {
            mView.showProgressMain();
            loadData(1);
        }

    }

    private void setDealInfo() {
        mView.setDealInfo(mGoodDealResponseManager.getGoodDealResponse().product, mGoodDealResponseManager.getGoodDealResponse().unit);
    }

    private void loadData(int _page) {
        mView.showProgressMain();
        mCompositeDisposable.add(mModel.getOrders(mGoodDealResponseManager.getGoodDealResponse().id, _page)
                .subscribe(orderListResponse -> {
                    mView.hideProgress();
                    mView.hidePlaceholderText();
                    this.page = _page;
                    totalPages = orderListResponse.meta.pages;
                    if (page == 1) {
                        mView.setOrdersList(createOrderList(orderListResponse.data));
                        needRefresh = orderListResponse.data.isEmpty();
                        if (orderListResponse.data.isEmpty())
                            mView.showPlaceHolderText();
                    } else {
                        mView.addOrder(createOrderList(orderListResponse.data));
                    }
                    if (page == totalPages) {
                        mView.addOrder(createTotalItem());
                    }
                }, throwable -> {
                    ToastManager.showToast("Something went wrong");
                }));
    }

    private List<OrderDH> createOrderList(List<Order> orders) {
        if (!orders.isEmpty()) {
            for (Order order : orders) {
                loadedOrders.add(new OrderDH(order, mIsOriginal, mDealStatus, OrderAdapter.TYPE_ORDER));
            }
        }
        return loadedOrders;
    }

    private List<OrderDH> createTotalItem() {
        List<OrderDH> totalItem = new ArrayList<>();
        int totalQuantity = 0;
        double totalPrice = 0.0;

        if (mDealStatus.equals(Constants.STATE_CLOSED)) {
            for (OrderDH orderDH : loadedOrders) {
                totalQuantity = totalQuantity + orderDH.getmOrder().getQuantity();
                totalPrice = totalPrice + orderDH.getmOrder().getPrice();
            }
            totalItem.add(new OrderDH(new Order(totalPrice, totalQuantity), mIsOriginal, mDealStatus, OrderAdapter.TYPE_TOTAL));
        }
        return totalItem;
    }

    @Override
    public void onRefresh() {
        loadedOrders.clear();
        loadData(1);
    }

    @Override
    public void loadNextPage() {
        if (page < totalPages) {
            mView.showProgressPagination();
            loadData(page + 1);
        }
    }

    @Override
    public void clickedConfButton(List<OrderDH> _orders) {
        mOrders = _orders;
        mView.showProgressMain();
        mCompositeDisposable.add(mUserModel.getUserProfile()
                .subscribe(user -> {
                    mView.hideProgress();
                    if (user.getBankAccount() != null) {
                        mView.showConfirmPopUp();
                    } else {
                        mView.showBankAccountErrorPopUp();
                    }
                }, throwable -> {
                    mView.hideProgress();
                    ToastManager.showToast("GEt User Error: " + throwable.getMessage());
                }));
    }

    @Override
    public void doConfirm() {
        List<String> confirmedOrderIdsList = new ArrayList<>();
        for (OrderDH orderDH : mOrders) {
            if (orderDH.isSelected())
                confirmedOrderIdsList.add(orderDH.getmOrder().getId());
        }
        if (!confirmedOrderIdsList.isEmpty()) {
            mView.showProgressMain();
            mCompositeDisposable.add(mGoodDealModel.confirmDeal(mGoodDealResponseManager.getGoodDealResponse().id, new OrdersList(confirmedOrderIdsList))
                    .subscribe(goodDealCancelResponse -> {
                        mView.hideProgress();
                        ToastManager.showToast("Confirm deal success");
                        onRefresh();
                    }, throwable -> {
                        mView.hideProgress();
                        ToastManager.showToast("Confirm deal error");
                    }));
        } else {
            ToastManager.showToast("Don't selected any order");
        }
    }


    @Override
    public void openCreateBankAccountFlow() {
        mView.openCreateBankAccountFlow();
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
