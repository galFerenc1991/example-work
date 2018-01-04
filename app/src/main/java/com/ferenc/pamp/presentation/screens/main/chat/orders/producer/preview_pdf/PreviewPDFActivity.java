package com.ferenc.pamp.presentation.screens.main.chat.orders.producer.preview_pdf;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.ferenc.pamp.R;
import com.ferenc.pamp.data.model.home.orders.PDFPreviewRequest;
import com.ferenc.pamp.data.model.home.orders.PDFPreviewResponse;
import com.ferenc.pamp.domain.OrderRepository;
import com.ferenc.pamp.presentation.base.BaseActivity;
import com.ferenc.pamp.presentation.utils.Constants;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.concurrent.TimeUnit;

/**
 * Created by shonliu on 12/29/17.
 */
@EActivity(R.layout.activity_preview_pdf)
public class PreviewPDFActivity extends BaseActivity implements PreviewPDFContract.View{

    private PreviewPDFContract.Presenter mPresenter;

    @ViewById(R.id.btnConfirm_APPDF)
    protected Button btnConfirm;
    @ViewById(R.id.rlProgress_APPDF)
    protected RelativeLayout rlProgress;
    @ViewById(R.id.wvHtml_APPDF)
    protected WebView wvHtmlPage;
    @ViewById(R.id.toolbar_APPDF)
    protected Toolbar toolbar;
    @StringRes(R.string.send_order_list_producer)
    protected String titlePDFPreview;

    @Extra
    protected PDFPreviewRequest mPDFPreviewRequest;

    @Bean
    protected OrderRepository mOrderRepository;

    @AfterInject
    @Override
    public void initPresenter() {
        new PreviewPDFPresenter(this, mOrderRepository, mPDFPreviewRequest, this);
    }

    @AfterViews
    protected void initUI() {
        initClickListeners();
        initBar();
        mPresenter.getPDFPreview();
        mPresenter.subscribe();
    }

    private void initBar() {
        toolbarManager.setTitle(titlePDFPreview);
        toolbarManager.showHomeAsUp(true);
        toolbarManager.closeActivityWhenBackArrowPressed(this);
    }

    private void initClickListeners() {
        RxView.clicks(btnConfirm)
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
        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        wvHtmlPage.loadData(pdfPreviewResponse.template, mimeType, encoding);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
