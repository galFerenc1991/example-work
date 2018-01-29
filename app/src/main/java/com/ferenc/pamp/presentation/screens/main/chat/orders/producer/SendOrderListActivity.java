package com.ferenc.pamp.presentation.screens.main.chat.orders.producer;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ferenc.pamp.PampApp_;
import com.ferenc.pamp.R;
import com.ferenc.pamp.data.model.home.orders.PDFPreviewRequest;
import com.ferenc.pamp.data.model.home.orders.Producer;
import com.ferenc.pamp.data.model.home.orders.SendPDFRequest;
import com.ferenc.pamp.presentation.base.BaseActivity;
import com.ferenc.pamp.presentation.screens.main.chat.create_order.create_order_pop_up.CreateOrderPopUpActivity_;
import com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer.ChooseProducerActivity_;
import com.ferenc.pamp.presentation.screens.main.chat.orders.producer.preview_pdf.PreviewPDFActivity_;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.GoodDealResponseManager;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

/**
 * Created by shonliu on 12/28/17.
 */

@EActivity(R.layout.activity_send_order_list)
public class SendOrderListActivity extends BaseActivity implements SendOrderListContract.View{

    @ViewById(R.id.toolbar_ASOL)
    protected Toolbar toolbar;

    @ViewById(R.id.btnCommander_ASOL)
    protected Button btnCommander;

    @ViewById(R.id.tvProducer_ASOL)
    protected TextView tvProducer;

    @ViewById(R.id.tvBonPlanInfo_ASOL)
    protected TextView tvBonPlanInfo;

    @ViewById(R.id.btnValider_ASOL)
    protected Button btnValider;

    @StringRes(R.string.title_send_order_list)
    protected String titleSendOrderList;

    private String mProducerName;

    @Nullable
    private String mProducerId;
    private int mQuantity;
    private String mProducerEmail;

    private SendOrderListContract.Presenter mPresenter;

    @Bean
    protected GoodDealResponseManager mGoodDealResponseManager;

    @Override
    public void setPresenter(SendOrderListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @AfterInject
    @Override
    public void initPresenter() {
        new SendOrderListPresenter(this, mGoodDealResponseManager);
    }

    @AfterViews
    protected void initUI() {
        initBar();
        initClickListeners();

        mPresenter.subscribe();
    }

    @Override
    protected int getContainer() {
        return 0;
    }

    @Override
    protected Toolbar getToolbar() {
        return toolbar;
    }

    @OnActivityResult(Constants.REQUEST_CODE_CREATE_ORDER_POP_UP_ACTIVITY)
    protected void createOrderResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            mQuantity = data.getIntExtra(Constants.KEY_PRODUCT_QUANTITY, -1);
            mPresenter.setQuantity(data.getIntExtra(Constants.KEY_PRODUCT_QUANTITY, -1));
            mPresenter.validateData(mQuantity, mProducerId);
        }
    }

    private void initBar() {
        toolbarManager.setTitle(titleSendOrderList);
        toolbarManager.showHomeAsUp(true);
        toolbarManager.closeActivityWhenBackArrowPressed(this);
    }

    private void initClickListeners() {

        RxView.clicks(btnCommander)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickToOpenCreateOrderPopUp());

        RxView.clicks(tvBonPlanInfo)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickToOpenCreateOrderPopUp());

        RxView.clicks(tvProducer)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickToChooseProducer());

        RxView.clicks(btnValider)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickValider(mProducerId, mGoodDealResponseManager.getGoodDealResponse().id, mQuantity, mProducerEmail));

    }

    @Override
    public void setBonPlanInfoVisibility(boolean _quantity) {
        tvBonPlanInfo.setVisibility(_quantity ? View.VISIBLE : View.GONE);
        btnCommander.setVisibility(_quantity  ? View.GONE : View.VISIBLE);
    }

    @Override
    public void openCreateOrderPopUp() {
        CreateOrderPopUpActivity_.intent(this).isSendOrderListFlow(true).mSendOrderListQuantity(mQuantity)
                .startForResult(Constants.REQUEST_CODE_CREATE_ORDER_POP_UP_ACTIVITY);
    }

    @Override
    public void chooseProducer() {
        ChooseProducerActivity_.intent(this).startForResult(Constants.REQUEST_CODE_ACTIVITY_CHOOSE_PRODUCER);
    }

    @Override
    public void setBonPlanInfo(String _bonPlanInfo) {
        tvBonPlanInfo.setText(_bonPlanInfo);
    }

    @Override
    public void setProducer(String name) {
        tvProducer.setText(name);
    }

    @Override
    public void openSendOrderListFlow(PDFPreviewRequest _pdfPreviewRequest, String _producerEmail, SendPDFRequest _sendPDFRequest) {
        PreviewPDFActivity_.intent(this).mPDFPreviewRequest(_pdfPreviewRequest).mProducerEmail(_producerEmail).mSendPDFRequest(_sendPDFRequest).start();
    }

    @Override
    public void setValidateButtonEnabled(boolean _isEnabled) {
        btnValider.setBackground(getResources().getDrawable(!_isEnabled ? R.drawable.bg_confirm_button_disable : R.drawable.bg_confirm_button));
        btnValider.setEnabled(_isEnabled);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
           if (requestCode == Constants.REQUEST_CODE_ACTIVITY_CHOOSE_PRODUCER) {
               if (data.getExtras() != null) {
                   Producer producer = data.getExtras().getParcelable(Constants.KEY_PRODUCER);
                   if (producer != null) {
                       mProducerId = producer.producerId;
                       mProducerName = producer.name;
                       mProducerEmail = producer.email;
                   }
               }
               tvProducer.setText(mProducerName);
               mPresenter.validateData(mQuantity, mProducerId);
           }
        }
    }
}
