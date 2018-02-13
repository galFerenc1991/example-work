package com.ferenc.pamp.presentation.screens.main.chat.orders.producer.preview_pdf;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ferenc.pamp.R;
import com.ferenc.pamp.data.model.home.orders.PDFPreviewRequest;
import com.ferenc.pamp.data.model.home.orders.PDFPreviewResponse;
import com.ferenc.pamp.data.model.home.orders.SendPDFRequest;
import com.ferenc.pamp.domain.OrderRepository;
import com.ferenc.pamp.presentation.base.BaseActivity;
import com.ferenc.pamp.presentation.utils.Constants;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.concurrent.TimeUnit;

/**
 * Created by shonliu on 12/29/17.
 */
@EActivity(R.layout.activity_preview_pdf)
public class PreviewPDFActivity extends BaseActivity implements PreviewPDFContract.View{

    private PreviewPDFContract.Presenter mPresenter;
    private boolean mIsFromSendOrderListAct;

    @ViewById(R.id.btnConfirm_APPDF)
    protected Button btnConfirm;
    @ViewById(R.id.ivSendMyOrder_APPDF)
    protected ImageView ivSendMyOrder;
    @ViewById(R.id.rlProgress_APPDF)
    protected RelativeLayout rlProgress;
    @ViewById(R.id.wvHtml_APPDF)
    protected WebView wvHtmlPage;
    @ViewById(R.id.toolbar_APPDF)
    protected Toolbar toolbar;
    @StringRes(R.string.send_order_list_producer)
    protected String titlePDFPreview;
    @StringRes(R.string.send_my_order)
    protected String titleSendMyOrder;

    @Extra
    protected PDFPreviewRequest mPDFPreviewRequest;

    @Extra
    protected SendPDFRequest mSendPDFRequest;

    @Extra
    protected String mProducerEmail;

    @Extra
    protected String mOrderId;

    @Bean
    protected OrderRepository mOrderRepository;

    @AfterInject
    @Override
    public void initPresenter() {
        mIsFromSendOrderListAct = TextUtils.isEmpty(mOrderId);
        new PreviewPDFPresenter(this, mOrderRepository, mPDFPreviewRequest, this, mProducerEmail, mOrderId, mIsFromSendOrderListAct, mSendPDFRequest);
    }

    @AfterViews
    protected void initUI() {
        initClickListeners();
        initBar();
        checkStoragePermission();
    }

    private void initBar() {
        toolbarManager.setTitle(mIsFromSendOrderListAct ? titlePDFPreview : titleSendMyOrder);
        toolbarManager.showHomeAsUp(true);
        toolbarManager.closeActivityWhenBackArrowPressed(this);
        toolbarManager.setIconHome(R.drawable.ic_arrow_back_green);

    }

    private void initClickListeners() {
        RxView.clicks(btnConfirm)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedConfirm());
        RxView.clicks(ivSendMyOrder)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedConfirm());
    }

    @Override
    public void setPresenter(PreviewPDFContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getContainer() {
        return -1;
    }

    @Override
    protected Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void showProgress() {
        rlProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress(boolean _closeActivity) {
        if (_closeActivity) {
            finish();
        } else {
            rlProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void showPDFInWebView(PDFPreviewResponse pdfPreviewResponse) {
        final String mimeType = Constants.MIME_TYPE_HTML;
        final String encoding = Constants.DEFAULT_UNCODING;
        wvHtmlPage.loadData(pdfPreviewResponse.template, mimeType, encoding);
    }

    @Override
    public boolean isReedStoragePermissionNotGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED;
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void checkStoragePermission() {
        if (isReedStoragePermissionNotGranted()) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_CODE_STORAGE);
        } else {
            mPresenter.subscribe();
        }
    }

    @Override
    public void showValiderButton() {
        btnConfirm.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideValiderButton() {
        btnConfirm.setVisibility(View.GONE);
    }

    @Override
    public void showSendMyOrderButton() {
        ivSendMyOrder.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSendMyOrderButton() {
        ivSendMyOrder.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.REQUEST_CODE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPresenter.subscribe();
            } else {
                Snackbar.make(
                        getWindow().getDecorView(),
                        "Please, allow for PAMP access to read storage.",
                        BaseTransientBottomBar.LENGTH_INDEFINITE)
                        .setAction("OK", click -> checkStoragePermission())
                        .show();

            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnActivityResult(Constants.REQUEST_CODE_ACTIVITY_SEND_PDF)
    void pdfSent(int resultCode) {
        if (resultCode == Activity.RESULT_OK)
            finish();
    }
}
