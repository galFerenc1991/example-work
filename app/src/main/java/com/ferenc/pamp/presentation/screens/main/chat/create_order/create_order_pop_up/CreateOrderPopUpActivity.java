package com.ferenc.pamp.presentation.screens.main.chat.create_order.create_order_pop_up;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.domain.OrderRepository;
import com.ferenc.pamp.presentation.base.BaseActivity;
import com.ferenc.pamp.presentation.screens.auth.sign_up.create_password.CreatePasswordContract;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.GoodDealResponseManager;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.concurrent.TimeUnit;

/**
 * Created by
 * Ferenc on 2017.12.21..
 */

@EActivity(R.layout.activity_create_order_pop_up)
public class CreateOrderPopUpActivity extends AppCompatActivity implements CreateOrderPopUpContract.View {

    private CreateOrderPopUpContract.Presenter mPresenter;

    @ViewById(R.id.rlProgress_ACOPU)
    protected RelativeLayout rlProgress;
    @ViewById(R.id.btnOk_ACOPU)
    protected Button btnOk;
    @ViewById(R.id.btnCancel_ACOPU)
    protected Button btnCancel;
    @ViewById(R.id.tvProductName_ACOPU)
    protected TextView tvProductName;
    @ViewById(R.id.tvPriceDescription_ACOPU)
    protected TextView tvPriceDescription;
    @ViewById(R.id.ivRemoveQuantity_ACOPU)
    protected ImageView ivRemoveQuantity;
    @ViewById(R.id.ivAddQuantity_ACOPU)
    protected ImageView ivAddQuantity;
    @ViewById(R.id.tvQuantity_ACOPU)
    protected TextView tvQuantity;
    @ViewById(R.id.tvPrice_ACOPU)
    protected TextView tvPrice;
    @ViewById(R.id.tvPampHonorar_ACOPU)
    protected TextView tvPampHonorar;
    @ViewById(R.id.tvTotalPrice_ACOPU)
    protected TextView tvTotalPrice;
    @ViewById(R.id.tvDeleteOrder_ACOPU)
    protected TextView tvDeleteOrder;

    @Extra
    protected boolean isSendOrderListFlow;
    @Extra
    protected int mSendOrderListQuantity;

    @Bean
    protected GoodDealResponseManager mGoodDealResponseManager;
    @Bean
    protected OrderRepository mOrderRepository;

    @AfterViews
    protected void initUI() {
        initClickListeners();
        mPresenter.subscribe();
    }

    private void initClickListeners() {
        RxView.clicks(btnOk)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedOk());
        RxView.clicks(btnCancel)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> finish());
        RxView.clicks(ivAddQuantity)
                .throttleFirst(Constants.CLICK_DELAY_SMALL, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedAddQuantity());
        RxView.clicks(ivRemoveQuantity)
                .throttleFirst(Constants.CLICK_DELAY_SMALL, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedRemoveQuantity());
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
    public void showProductName(String _productName) {
        tvProductName.setText(_productName);
    }

    @Override
    public void showPriceDescription(String _priceDescription) {
        tvPriceDescription.setText(_priceDescription);
    }

    @Override
    public void showQuantity(String _quantity) {
        tvQuantity.setText(_quantity);
    }

    @Override
    public void setQuantityColorToRed(boolean _isRed) {
        if (_isRed) {
            tvQuantity.setTextColor(getResources().getColor(R.color.textColorRed));
            tvDeleteOrder.setVisibility(View.VISIBLE);
        } else {
            tvQuantity.setTextColor(getResources().getColor(R.color.textColorBlack));
            tvDeleteOrder.setVisibility(View.GONE);
        }
    }

    @Override
    public void showPrices(String _total, String _price, String _honorar) {
        tvPrice.setText(_price);
        tvTotalPrice.setText(_total);
        tvPampHonorar.setText(_honorar);
    }

    @AfterInject
    @Override
    public void initPresenter() {
        new CreateOrderPopUpPresenter(this, mGoodDealResponseManager, mOrderRepository, isSendOrderListFlow, mSendOrderListQuantity);
    }

    @Override
    public void closeActivityForResult(double _quantity) {
        Intent intent = new Intent();
        intent.putExtra(Constants.KEY_PRODUCT_QUANTITY, _quantity);
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public void setPresenter(CreateOrderPopUpContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
