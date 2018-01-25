package com.ferenc.pamp.presentation.screens.main.profile.about;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ferenc.pamp.R;
import com.ferenc.pamp.domain.UserRepository;
import com.ferenc.pamp.presentation.base.BaseActivity;
import com.ferenc.pamp.presentation.utils.Constants;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.concurrent.TimeUnit;

/**
 * Created by shonliu on 1/25/18.
 */
@EActivity(R.layout.activity_about)
public class AboutActivity  extends BaseActivity implements AboutContract.View {

    private AboutContract.Presenter mPresenter;

    private boolean isWebPageShown = false;

    @ViewById(R.id.toolbar_AA)
    protected Toolbar toolbar;

    @ViewById(R.id.progressBar_AA)
    protected ProgressBar progressBar;

    @ViewById(R.id.llCGU_AA)
    protected LinearLayout llCGU;

    @ViewById(R.id.llRules_AA)
    protected LinearLayout llRules;

    @ViewById(R.id.llAboutUs_AA)
    protected LinearLayout llAboutUs;

    @ViewById(R.id.wvHtml_AA)
    protected WebView wvHtmlPage;

    @ViewById(R.id.llAboutBtns_AA)
    protected LinearLayout llAboutBtns;

    @StringRes(R.string.text_about)
    protected String titleAbout;

    @Bean
    protected UserRepository mUserRepository;

    @Override
    protected int getContainer() {
        return -1;
    }

    @Override
    protected Toolbar getToolbar() {
        return toolbar;
    }

    @AfterInject
    @Override
    public void initPresenter() {
        new AboutPresenter(this, mUserRepository);
    }

    @Override
    public void setPresenter(AboutContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @AfterViews
    protected void initUI() {
        mPresenter.subscribe();
    }

    @Override
    public void initClickListeners() {
        RxView.clicks(llCGU)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickCGU());

        RxView.clicks(llRules)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickRules());

        RxView.clicks(llAboutUs)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickAboutUs());
    }

    @Override
    public void initBar() {
        toolbarManager.setTitle(titleAbout);
        toolbarManager.showHomeAsUp(true);
        toolbarManager.superBackWhenBackArrowPressed(this,true);
    }

    @Override
    public void showHTML(String html) {
        wvHtmlPage.loadData(html, Constants.MIME_TYPE_HTML, Constants.DEFAULT_UNCODING);
        showWebPage(true);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (isWebPageShown) {
            showWebPage(false);
        } else
            finish();
    }

    private void showWebPage(boolean isWebPageShown) {
        wvHtmlPage.setVisibility(isWebPageShown ? View.VISIBLE : View.GONE);
        llAboutBtns.setVisibility(isWebPageShown ? View.GONE : View.VISIBLE);
        this.isWebPageShown = isWebPageShown;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
