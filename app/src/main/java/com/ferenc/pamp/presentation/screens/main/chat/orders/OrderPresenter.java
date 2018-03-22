package com.ferenc.pamp.presentation.screens.main.chat.orders;

import com.ferenc.pamp.R;
import com.ferenc.pamp.data.api.exceptions.ConnectionLostException;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.data.model.home.orders.ChangeOrderDeliveryStateRequest;
import com.ferenc.pamp.data.model.home.orders.Order;
import com.ferenc.pamp.data.model.home.orders.OrdersList;
import com.ferenc.pamp.presentation.screens.main.chat.orders.order_adapter.OrderAdapter;
import com.ferenc.pamp.presentation.screens.main.chat.orders.order_adapter.OrderDH;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.GoodDealResponseManager;
import com.ferenc.pamp.presentation.utils.ToastManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import retrofit2.HttpException;

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

        if (mIsOriginal && mDealStatus.equals(Constants.STATE_CLOSED) && mGoodDealResponseManager.getGoodDealResponse().hasOrders) {
            mView.initSendPdfInfo(mGoodDealResponseManager.getGoodDealResponse().sent != null);
            mView.showConfButton();
        } else mView.hideConfButton();

        if (needRefresh) {
            mView.showProgressMain();
            loadData(1);
        }

    }

    private void setDealInfo() {
        GoodDealResponse currentDeal = mGoodDealResponseManager.getGoodDealResponse();
        mView.setDealInfo(currentDeal.product, currentDeal.unit, convertServerDateToString(currentDeal.closingDate));
        if (currentDeal.isResend) {
            mView.setResendTitle("Re-diffusion rattachée à : " + currentDeal.originalTitle + "/dim. " + convertServerDateToString(currentDeal.closingDate));
        }
    }

    private String getCloseDateInString(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat(" EEE dd MMM HH:mm", Locale.FRANCE);
        return sdf.format(calendar.getTime());
    }

    private String convertServerDateToString(long _dateInMillis) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(_dateInMillis);
        return getCloseDateInString(date);
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
                        if (orderListResponse.data.isEmpty()) {
                            mView.showPlaceHolderText();
                            mView.hideConfButton();
                        }
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
        double totalQuantity = 0;
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
                        mView.hideConfButton();
                        mDealStatus = Constants.STATE_CONFIRM;
//                        ToastManager.showToast("Confirm deal success");
                        onRefresh();
                    }, throwableConsumer));
        } else {
            ToastManager.showToast("Vous devez confirmer au moins 1 commande");
        }
    }

    private Consumer<Throwable> throwableConsumer = throwable -> {
        throwable.printStackTrace();
        mView.hideProgress();
        if (throwable instanceof ConnectionLostException) {
            ToastManager.showToast(R.string.err_msg_connection_problem);
        } else if (throwable instanceof HttpException) {
            HttpException e = (HttpException) throwable;
            switch (e.code()) {
                case 204:
//                    mView.hideCreateOrderProgress(false);
            }
        } else {
            ToastManager.showToast(R.string.err_msg_something_goes_wrong);
        }
    };

    @Override
    public void changeDeliveryState(OrderDH _orderDH, int position) {
        boolean delivered = !_orderDH.getmOrder().isDelivered();
        mView.showChangeDeliveryProgress();
        ChangeOrderDeliveryStateRequest request = new ChangeOrderDeliveryStateRequest();
        request.setDealId(mGoodDealResponseManager.getGoodDealResponse().id);
        request.setDelivered(delivered);
        mCompositeDisposable.add(mModel.changeDeliveryState(_orderDH.getmOrder().getId(), request)
                .subscribe(messageOrderResponse -> {
                    mView.hideChangeDeliveryProgress();
                    OrderDH changedDeliveredStatusOrder = _orderDH;
                    changedDeliveredStatusOrder.getmOrder().setDelivered(delivered);
                    mView.updateOrder(changedDeliveredStatusOrder, position);
                }, throwable -> {
                    mView.hideChangeDeliveryProgress();
                    mView.updateOrder(_orderDH, position);
                }));
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
