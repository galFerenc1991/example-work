package com.kubator.pamp.presentation.screens.main.chat.orders.producer;


import com.kubator.pamp.data.model.home.orders.PDFPreviewRequest;
import com.kubator.pamp.data.model.home.orders.SendPDFRequest;
import com.kubator.pamp.presentation.utils.GoodDealResponseManager;
import com.jakewharton.rxrelay2.PublishRelay;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by shonliu on 12/29/17.
 */

public class SendOrderListPresenter implements SendOrderListContract.Presenter {


    private SendOrderListContract.View mView;
    private GoodDealResponseManager mGoodDealResponseManager;
    private CompositeDisposable compositeDisposable;
    private PublishRelay<Boolean> validateData;

    public SendOrderListPresenter(SendOrderListContract.View _view, GoodDealResponseManager _goodDealResponseManager) {
        mView = _view;
        mGoodDealResponseManager = _goodDealResponseManager;
        compositeDisposable = new CompositeDisposable();
        validateData = PublishRelay.create();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        compositeDisposable.add(validateData.subscribe(aBoolean ->
            mView.setValidateButtonEnabled(aBoolean)
        ));
    }

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
    }

    @Override
    public void setQuantity(double _quantity) {

        String bonPlanInfo = mGoodDealResponseManager.getGoodDealResponse().title
                + " : "
                + String.valueOf(_quantity)
                + " / "
                + mGoodDealResponseManager.getGoodDealResponse().unit;

        mView.setBonPlanInfo(bonPlanInfo);

        mView.setBonPlanInfoVisibility(_quantity > 0.0);
    }

    @Override
    public void clickValider(String _id, String _dealId, double _quantity, String _producerEmail) {
        PDFPreviewRequest pdfPreviewRequest = new PDFPreviewRequest(_id, _dealId, _quantity);
        mView.openSendOrderListFlow(pdfPreviewRequest, _producerEmail, new SendPDFRequest(_dealId, _id));
    }

    @Override
    public void validateData(double _quantity, String _producerId) {
         validateData.accept(_producerId != null);
    }

    @Override
    public void clickToOpenCreateOrderPopUp() {
        mView.openCreateOrderPopUp();
    }

    @Override
    public void clickToChooseProducer() {
        mView.chooseProducer();
    }

}
