package com.ferenc.pamp.presentation.screens.main.chat.create_order.create_order_pop_up;

import com.ferenc.pamp.data.api.exceptions.ConnectionLostException;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.data.model.home.orders.Order;
import com.ferenc.pamp.data.model.home.orders.OrderRequest;
import com.ferenc.pamp.presentation.utils.GoodDealResponseManager;
import com.ferenc.pamp.presentation.utils.ToastManager;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by
 * Ferenc on 2017.12.21..
 */

public class CreateOrderPopUpPresenter implements CreateOrderPopUpContract.Presenter {

    private CreateOrderPopUpContract.View mView;
    private GoodDealResponseManager mGoodDealResponseManager;
    private CreateOrderPopUpContract.OrderModel mOrderModel;
    private CompositeDisposable mCompositeDisposable;
    private int mQuantity;
    private Order mOrder;
    private boolean mHasOrder;
    private boolean mIsSendOrderListFlow;
    private GoodDealResponse mGoodDealResponse;
    private int mSendOrderListQuantity;


    public CreateOrderPopUpPresenter(CreateOrderPopUpContract.View _view,
                                     GoodDealResponseManager _goodDealResponseManager,
                                     CreateOrderPopUpContract.OrderModel _orderModel,
                                     boolean _isSendOrderListFlow,
                                     int _sendOrderListQuantity) {
        this.mView = _view;
        this.mOrderModel = _orderModel;
        this.mGoodDealResponseManager = _goodDealResponseManager;
        this.mCompositeDisposable = new CompositeDisposable();
        this.mQuantity = 0;
        this.mGoodDealResponse = mGoodDealResponseManager.getGoodDealResponse();
        this.mHasOrder = mGoodDealResponse.hasOrders;
        this.mIsSendOrderListFlow = _isSendOrderListFlow;
        this.mSendOrderListQuantity = _sendOrderListQuantity;
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mView.showProductName(mGoodDealResponse.product);
        mView.showPriceDescription(mGoodDealResponse.unit);
        mView.showPrice(String.valueOf(mGoodDealResponse.price) + " €");
        if (!mIsSendOrderListFlow) {
            if (mHasOrder) {
                mView.showProgress();
                mCompositeDisposable.add(mOrderModel.getMyOrder(mGoodDealResponse.id)
                        .subscribe(order -> {
                            mView.hideProgress(false);
                            setOrder(order);
                        }, throwableConsumer));
            }
        } else {
            setLocalOrder(mSendOrderListQuantity);
        }
    }

    private void setLocalOrder(int _quantity) {
        mQuantity = _quantity;
        mView.showQuantity(String.valueOf(_quantity));
        mView.showTotal(String.valueOf(mGoodDealResponse.price * _quantity) + " €");
    }

    private void setOrder(Order _order) {
        mOrder = _order;
        mQuantity = _order.getQuantity();
        mView.showQuantity(String.valueOf(_order.getQuantity()));
        mView.showTotal(String.valueOf(_order.getPrice() * _order.getQuantity()) + " €");
    }

    @Override
    public void clickedAddQuantity() {
        mQuantity = mQuantity + 1;
        mView.setQuantityColorToRed(false);
        mView.showQuantity(String.valueOf(mQuantity));
        mView.showTotal(String.valueOf(mGoodDealResponse.price * mQuantity) + " €");
    }

    @Override
    public void clickedRemoveQuantity() {
        if (mQuantity > 0) mQuantity = mQuantity - 1;
        if (mHasOrder && mQuantity == 0) mView.setQuantityColorToRed(true);
        mView.showQuantity(String.valueOf(mQuantity));
        mView.showTotal(String.valueOf(mGoodDealResponse.price * mQuantity) + " €");
    }

    @Override
    public void clickedOk() {
        if (!mIsSendOrderListFlow) {
            if (mHasOrder) {
                if (mQuantity != 0) updateOrder();
                else deleteOrder();
            } else {
                if (mQuantity != 0) createOrder();
                else ToastManager.showToast("Quantity not have be null");
            }
        } else {
            mView.closeActivityForResult(mQuantity);
        }
    }

    private void createOrder() {
        mView.closeActivityForResult(mQuantity);
    }

    private void deleteOrder() {
        mView.showProgress();
        mCompositeDisposable.add(mOrderModel.deleteOrder(mOrder.getId())
                .subscribe(messageOrderResponse -> {
                    mView.hideProgress(false);
                    mGoodDealResponse.hasOrders = false;
                    mGoodDealResponseManager.saveGoodDealResponse(mGoodDealResponse);
                    ToastManager.showToast(messageOrderResponse.getMessage());
                    mView.closeActivityForResult(mQuantity);
                }, throwableConsumer));
    }

    private void updateOrder() {
        mView.showProgress();
        mCompositeDisposable.add(mOrderModel.updateOrder(mOrder.getId(), new OrderRequest.Builder()
                .setDealId(mGoodDealResponse.id)
                .setQuantity(mQuantity)
                .build())
                .subscribe(messageOrderResponse -> {
                    mView.hideProgress(false);
                    ToastManager.showToast(messageOrderResponse.getMessage());
                    mView.closeActivityForResult(mQuantity);
                }, throwableConsumer));
    }

    private Consumer<Throwable> throwableConsumer = throwable -> {
        throwable.printStackTrace();
        if (throwable instanceof ConnectionLostException) {
            mView.hideProgress(true);
            ToastManager.showToast("Connection Lost");
        } else {
            mView.hideProgress(true);
            ToastManager.showToast("Something went wrong");
        }
    };

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
