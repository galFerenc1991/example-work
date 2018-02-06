package com.ferenc.pamp.presentation.screens.main.chat.create_order.create_order_pop_up;

import com.ferenc.pamp.data.api.exceptions.ConnectionLostException;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.data.model.home.orders.Order;
import com.ferenc.pamp.data.model.home.orders.OrderRequest;
import com.ferenc.pamp.presentation.utils.GoodDealResponseManager;
import com.ferenc.pamp.presentation.utils.ToastManager;

import java.text.DecimalFormat;

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
    private double mQuantity;
    private Order mOrder;
    private boolean mHasOrder;
    private boolean mIsSendOrderListFlow;
    private GoodDealResponse mGoodDealResponse;
    private int mSendOrderListQuantity;
    private String mCleanPrice;
    private String mHonorar;
    private String mTotalPrice;


    public CreateOrderPopUpPresenter(CreateOrderPopUpContract.View _view,
                                     GoodDealResponseManager _goodDealResponseManager,
                                     CreateOrderPopUpContract.OrderModel _orderModel,
                                     boolean _isSendOrderListFlow,
                                     int _sendOrderListQuantity) {
        this.mView = _view;
        this.mOrderModel = _orderModel;
        this.mGoodDealResponseManager = _goodDealResponseManager;
        this.mCompositeDisposable = new CompositeDisposable();
        this.mQuantity = 1.0d;
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
        calculateTotal();
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
        calculateTotal();
    }

    private void setOrder(Order _order) {
        mOrder = _order;
        mQuantity = _order.getQuantity();
        if (mQuantity == 0 || mQuantity >= 1) {
            DecimalFormat df = new DecimalFormat("#");
            mView.showQuantity(df.format(mQuantity));
        } else {
            mView.showQuantity(String.valueOf(_order.getQuantity()));
        }
        calculateTotal();
    }

    @Override
    public void clickedAddQuantity() {
        if (mQuantity < 1) {
            mQuantity = (mQuantity * 10 + 1) / 10;
        } else {
            mQuantity = mQuantity + 1;
        }

        if (mQuantity >= 1) {
            DecimalFormat df = new DecimalFormat("#");
            mView.showQuantity(df.format(mQuantity));
        } else {
            mView.showQuantity(String.valueOf(mQuantity));
        }
        mView.setQuantityColorToRed(false);
        calculateTotal();
    }

    @Override
    public void clickedRemoveQuantity() {
        if (mQuantity > 0 && mQuantity <= 1) {
            mQuantity = (mQuantity * 10 - 1) / 10;
        } else if (mQuantity > 1) {
            mQuantity = mQuantity - 1;
        }
        if (mQuantity == 0 || mQuantity >= 1) {
            DecimalFormat df = new DecimalFormat("#");
            mView.showQuantity(df.format(mQuantity));
        } else {
            mView.showQuantity(String.valueOf(mQuantity));
        }
        if (mHasOrder && mQuantity == 0) mView.setQuantityColorToRed(true);
        calculateTotal();
    }

    private void calculateTotal() {
        DecimalFormat df = new DecimalFormat("#.##");
        double cleanPrice = mGoodDealResponse.price * mQuantity;
        mCleanPrice = df.format(cleanPrice) + " €";
        double honorar = ((mGoodDealResponse.price * mQuantity / 100) * 3.5) + 0.75;
        mHonorar = df.format(honorar) + " €";
        double totalInDouble = cleanPrice + honorar;
        mTotalPrice = df.format(totalInDouble) + " €";
        mView.showPrices(mTotalPrice, mCleanPrice, mHonorar);
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
