package com.kubator.pamp.presentation.screens.main.chat.create_order.payment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.kubator.pamp.R;
import com.kubator.pamp.presentation.base.BaseActivity;
import com.kubator.pamp.presentation.screens.main.chat.create_order.payment.select_card.SelectCardFragment_;
import com.kubator.pamp.presentation.utils.Constants;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

/**
 * Created by
 * Ferenc on 2017.12.22..
 */

@EActivity(R.layout.activity_payment)
public class PaymentActivity extends BaseActivity {

    @Extra
    protected double mQuantity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
            initFragment();
    }

    protected void initFragment() {
        replaceFragment(SelectCardFragment_.builder().mQuantity(getIntent().getDoubleExtra(Constants.KEY_PRODUCT_QUANTITY, -1)).build());
    }

    @Override
    protected int getContainer() {
        return R.id.flContainer_AP;
    }

    @Override
    protected Toolbar getToolbar() {
        return null;
    }

    @Click(R.id.ivBack_AP)
    protected void backPressed() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            super.onBackPressed();
        } else {
            finish();
        }
    }
}
