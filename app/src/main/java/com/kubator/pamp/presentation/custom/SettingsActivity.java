package com.kubator.pamp.presentation.custom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kubator.pamp.R;
import com.kubator.pamp.data.model.home.good_deal.GoodDealResponse;
import com.kubator.pamp.presentation.utils.Constants;
import com.kubator.pamp.presentation.utils.GoodDealResponseManager;
import com.kubator.pamp.presentation.utils.SignedUserManager;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.concurrent.TimeUnit;

/**
 * Created by
 * Ferenc on 2017.12.17..
 */
@EActivity(R.layout.activity_settings)
public class SettingsActivity extends AppCompatActivity {

    @Bean
    protected GoodDealResponseManager mDealResponseManager;
    @Bean
    protected SignedUserManager mSignedUserManager;

    @ViewById(R.id.rlSendOrders_AS)
    protected RelativeLayout rlSendOrders;
    @ViewById(R.id.rlCloseDate_AS)
    protected RelativeLayout rlCloseDate;
    @ViewById(R.id.rlDeliveryDate_AS)
    protected RelativeLayout rlDeliveryDate;
    @ViewById(R.id.rlCancelGoodDeal_AS)
    protected RelativeLayout rlCancelGoodDeal;
    @ViewById(R.id.tvCancel_AS)
    protected TextView tvCancel;

    @AfterViews
    protected void initUI() {
        initClickListeners();

        GoodDealResponse goodDealResponse = mDealResponseManager.getGoodDealResponse();

        if (!goodDealResponse.isResend) {
            if (goodDealResponse.state.equals(Constants.STATE_PROGRESS)) {
////do nothing
            } else {
                rlSendOrders.setVisibility(View.VISIBLE);
                rlCloseDate.setVisibility(View.GONE);
                rlCancelGoodDeal.setVisibility(View.GONE);
            }
        } else {
            if (goodDealResponse.state.equals(Constants.STATE_PROGRESS)) {
                rlCloseDate.setVisibility(View.GONE);
            } else {
                rlCloseDate.setVisibility(View.GONE);
                rlCancelGoodDeal.setVisibility(View.GONE);
            }
        }


//        if (!goodDealResponse.state.equals(Constants.STATE_PROGRESS) &&
//                goodDealResponse.isResend) {
//            rlSendOrders.setVisibility(View.VISIBLE);
//            rlCloseDate.setVisibility(View.GONE);
//            rlCancelGoodDeal.setVisibility(View.GONE);
//        }
//        if (goodDealResponse.state.equals(Constants.STATE_PROGRESS) &&
//                !goodDealResponse.isResend) {
//            rlCloseDate.setVisibility(View.GONE);
//            rlDeliveryDate.setVisibility(View.GONE);
//        }
    }

    private void initClickListeners() {
        RxView.clicks(tvCancel)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> onBackPressed());

        RxView.clicks(rlSendOrders)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> setResult(Constants.KEY_SEND_ORDERS));

        RxView.clicks(rlCloseDate)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> setResult(Constants.KEY_CHANGE_CLOSE_DATE));

        RxView.clicks(rlDeliveryDate)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> setResult(Constants.KEY_CHANGE_DELIVERY_DATE));

        RxView.clicks(rlCancelGoodDeal)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> setResult(Constants.KEY_CANCEL_GOOD_DEAL));
    }

    void setResult(String _actionKey) {
        Intent intent = new Intent();
        switch (_actionKey) {
            case Constants.KEY_CHANGE_CLOSE_DATE:
                intent.putExtra(Constants.KEY_SETTINGS, Constants.KEY_CHANGE_CLOSE_DATE);
                setResult(RESULT_OK, intent);
                break;
            case Constants.KEY_CHANGE_DELIVERY_DATE:
                intent.putExtra(Constants.KEY_SETTINGS, Constants.KEY_CHANGE_DELIVERY_DATE);
                setResult(RESULT_OK, intent);
                break;
            case Constants.KEY_CANCEL_GOOD_DEAL:
                intent.putExtra(Constants.KEY_SETTINGS, Constants.KEY_CANCEL_GOOD_DEAL);
                setResult(RESULT_OK, intent);
                break;
            case Constants.KEY_SEND_ORDERS:
                intent.putExtra(Constants.KEY_SETTINGS, Constants.KEY_SEND_ORDERS);
                setResult(RESULT_OK, intent);
                break;
        }
        finish();
    }
}
