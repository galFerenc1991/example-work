package com.ferenc.pamp.presentation.custom.end_flow_screen;

import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.utils.Constants;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;

import java.util.concurrent.TimeUnit;

/**
 * Created by
 * Ferenc on 2017.12.28..
 */
@EActivity(R.layout.activity_end_flow_order)
@Fullscreen
public class EndFlowOrderActivity extends AppCompatActivity {
    @ViewById(R.id.ivClose_AEFO)
    protected ImageView ivClose;
    @ViewById(R.id.ivSuccess_AEFO)
    protected ImageView ivSuccess;
    @ViewById(R.id.tvEndFlowMassage_AEFO)
    protected TextView tvEndFlowMassage;
    @ViewById(R.id.tvEndFlowSubtitle_AEFO)
    protected TextView tvEndFlowSubtitle;

    @Extra
    protected boolean mIsCreatedFlow;


    @AfterViews
    protected void initUI() {
        initCloseButton();
        if (!mIsCreatedFlow) {
            ivSuccess.setImageResource(R.drawable.ic_check_red);
            tvEndFlowMassage.setText(R.string.title_order_canceled);
            tvEndFlowMassage.setTextColor(getResources().getColor(R.color.textColorRed));
            tvEndFlowSubtitle.setText(R.string.title_order_canceled_subtitle);
            tvEndFlowSubtitle.setTextColor(getResources().getColor(R.color.textColorRed));
        }
    }

    private void initCloseButton() {
        RxView.clicks(ivClose)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> finish());
    }
}
