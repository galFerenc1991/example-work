package com.ferenc.pamp.presentation.screens.main.chat.orders.producer;

import android.content.Context;

import com.ferenc.pamp.presentation.utils.GoodDealResponseManager;

/**
 * Created by shonliu on 12/29/17.
 */

public class SendOrderListPresenter implements SendOrderListContract.Presenter {


    private SendOrderListContract.View mView;
    private GoodDealResponseManager mGoodDealResponseManager;
    private Context mContext;


    public SendOrderListPresenter(SendOrderListContract.View _view, GoodDealResponseManager _goodDealResponseManager, Context _context) {
        mView = _view;
        mGoodDealResponseManager = _goodDealResponseManager;
        mContext = _context;
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public boolean setQuantity(int _quantity) {

        String bonPlanInfo = mGoodDealResponseManager.getGoodDealResponse().title
                + " : "
                + String.valueOf(_quantity)
                + " / "
                + mGoodDealResponseManager.getGoodDealResponse().unit;

        mView.setBonPlanInfo(bonPlanInfo);

        return _quantity >= 1;
    }
}
