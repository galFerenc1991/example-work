package com.ferenc.pamp.presentation.screens.main.chat.orders.producer;

import android.content.Context;

import com.ferenc.pamp.data.model.home.orders.PDFPreviewRequest;
import com.ferenc.pamp.presentation.utils.GoodDealResponseManager;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by shonliu on 12/29/17.
 */

public class SendOrderListPresenter implements SendOrderListContract.Presenter {


    private SendOrderListContract.View mView;
    private GoodDealResponseManager mGoodDealResponseManager;
    private Context mContext;
    private CompositeDisposable compositeDisposable;


    public SendOrderListPresenter(SendOrderListContract.View _view, GoodDealResponseManager _goodDealResponseManager, Context _context) {
        mView = _view;
        mGoodDealResponseManager = _goodDealResponseManager;
        mContext = _context;
        compositeDisposable = new CompositeDisposable();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
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

    @Override
    public void clickValider(String _id, String _dealId, int _quantity) {
        PDFPreviewRequest pdfPreviewRequest = new PDFPreviewRequest(_id, _dealId, _quantity);
        mView.openSendOrderListFlow(pdfPreviewRequest);
    }

}
