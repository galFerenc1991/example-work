package com.kubator.pamp.presentation.custom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.kubator.pamp.R;
import com.kubator.pamp.presentation.utils.Constants;
import com.kubator.pamp.presentation.utils.GoodDealManager;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.concurrent.TimeUnit;

/**
 * Created by
 * Ferenc on 2017.11.23..
 */

@EActivity(R.layout.activity_input)
public class InputActivity extends AppCompatActivity {

    @ViewById(R.id.ivContentIcon_AI)
    protected ImageView ivContentIndicator;
    @ViewById(R.id.etContent_AI)
    protected EditText etContent;
    @ViewById(R.id.btnOK_AI)
    protected Button btnOk;

    @Bean
    protected GoodDealManager mGoodDealManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    protected void initUI() {

        RxView.clicks(btnOk)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> setResult());
        setInputIndicatorIcon(getIntent().getIntExtra(Constants.KEY_INPUT_INDICATOR, 0));
    }

    private void setInputIndicatorIcon(int _requestCode) {
        switch (_requestCode) {
            case Constants.REQUEST_CODE_INPUT_ACTIVITY_NAME:
                etContent.setHint(getString(R.string.hint_good_plan_name));
                etContent.setText(mGoodDealManager.getGoodDeal().getProduct());
                ivContentIndicator.setImageResource(R.drawable.ic_good_plan_name);
                break;
            case Constants.REQUEST_CODE_INPUT_ACTIVITY_DESCRIPTION:
                etContent.setHint(getString(R.string.hint_good_plan_description));
                etContent.setText(mGoodDealManager.getGoodDeal().getDescription());
                ivContentIndicator.setImageResource(R.drawable.ic_description);
                break;
            case Constants.REQUEST_CODE_INPUT_ACTIVITY_DESCRIPTION_FROM_RE_BROADCAST_FLOW:
                etContent.setHint(getString(R.string.hint_good_plan_description));
                btnOk.setBackground(getResources().getDrawable(R.drawable.bg_confirm_button_yellow));
                etContent.setText(mGoodDealManager.getGoodDeal().getDescription());
                ivContentIndicator.setImageResource(R.drawable.ic_description_yelow);
                break;
            case Constants.REQUEST_CODE_INPUT_ACTIVITY_PRICE:
                etContent.setHint(getString(R.string.hint_good_plan_price));
                double priceFromCash = mGoodDealManager.getGoodDeal().getPrice();
                etContent.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                etContent.setKeyListener(DigitsKeyListener.getInstance(false,true));
                if (priceFromCash == 0) {
                    etContent.setText("");
                } else {
                    etContent.setText(String.valueOf(priceFromCash));
                }
                ivContentIndicator.setImageResource(R.drawable.ic_price);
                break;
            case Constants.REQUEST_CODE_INPUT_ACTIVITY_PRICE_DESCRIPTION:
                etContent.setHint(getString(R.string.hint_good_plan_price_description));
                etContent.setText(mGoodDealManager.getGoodDeal().getUnit());
                ivContentIndicator.setImageResource(R.drawable.ic_help);
                break;
            case Constants.REQUEST_CODE_INPUT_ACTIVITY_QUANTITY:
                etContent.setHint(getString(R.string.hint_good_plan_quantity));
                etContent.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                etContent.setKeyListener(DigitsKeyListener.getInstance(false,true));
                double quantityFromCash = mGoodDealManager.getGoodDeal().getQuantity();

                if (quantityFromCash == 0.0) {
                    etContent.setText("");
                } else {
                    etContent.setText(String.valueOf(quantityFromCash));
                }
                ivContentIndicator.setImageResource(R.drawable.ic_payment);
                break;
        }
    }

    void setResult() {
        Intent intent = new Intent();
        if (!etContent.getText().toString().isEmpty()) {
            intent.putExtra(Constants.KEY_INPUT_RESULT, etContent.getText().toString());
        } else {
            intent.putExtra(Constants.KEY_INPUT_RESULT, "");
        }
        setResult(RESULT_OK, intent);
        finish();
    }
}