package com.ferenc.pamp.presentation.screens.main.chat.orders.producer;

import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.base.BaseActivity;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.GoodDealResponseManager;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.concurrent.TimeUnit;

/**
 * Created by shonliu on 12/28/17.
 */

@EActivity(R.layout.activity_send_order_list)
public class SendOrderListActivity extends BaseActivity {

    @ViewById(R.id.toolbar_ASOL)
    protected Toolbar toolbar;

    @ViewById(R.id.btnCommander_ASOL)
    protected Button btnCommander;

    @ViewById(R.id.tvProducer_ASOL)
    protected TextView tvProducer;

    @ViewById(R.id.tvBonPlanInfo_ASOL)
    protected TextView tvBonPlanInfo;

    @StringRes(R.string.title_send_order_list)
    protected String titleSendOrderList;

    @Bean
    protected GoodDealResponseManager mGoodDealResponseManager;

    @AfterViews
    protected void initUI() {
        initBar();
        initClickListeners();
    }
    @Override
    protected int getContainer() {
        return 0;
    }

    @Override
    protected Toolbar getToolbar() {
        return toolbar;
    }

    private void initBar() {
        toolbarManager.setTitle(titleSendOrderList);
        toolbarManager.showHomeAsUp(true);
        toolbarManager.closeActivityWhenBackArrowPressed(this);
    }

    private void initClickListeners() {

        RxView.clicks(btnCommander)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> {});

        RxView.clicks(tvProducer)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> {});

        RxView.clicks(tvBonPlanInfo)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> {});
    }
}
