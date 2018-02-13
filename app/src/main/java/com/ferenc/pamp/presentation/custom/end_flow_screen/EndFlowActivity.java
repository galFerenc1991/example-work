package com.ferenc.pamp.presentation.custom.end_flow_screen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.presentation.screens.main.chat.ChatActivity_;
import com.ferenc.pamp.presentation.screens.main.chat.participants.ParticipantsActivity_;
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
 * Ferenc on 2017.12.15..
 */
@EActivity(R.layout.activity_end_flow)
@Fullscreen
public class EndFlowActivity extends AppCompatActivity {

    @ViewById(R.id.ivClose_AEF)
    protected ImageView ivClose;
    @ViewById(R.id.ivSuccess_AEF)
    protected ImageView ivSuccess;
    @ViewById(R.id.tvEndFlowMassage_AEF)
    protected TextView tvEndFlowMassage;

    //    @Extra
//    protected boolean mIsCreatedFlow;
    @Extra
    int mFlow;

    @Extra
    protected int fromWhere;
    @Extra
    protected GoodDealResponse mGoodDealResponse;

    @AfterViews
    protected void initUI() {
        initCloseButton();

        switch (mFlow) {
            case Constants.CREATE_FLOW:
                break;
            case Constants.NOT_CREATE_FLOW:
                ivSuccess.setImageResource(R.drawable.ic_close_big_red);
                tvEndFlowMassage.setText(R.string.title_good_deal_canceled);
                tvEndFlowMassage.setTextColor(getResources().getColor(R.color.textColorRed));
                break;
            case Constants.ATTACH_BANK_ACCOUNT_FLOW:
                tvEndFlowMassage.setText(R.string.text_bank_account_attached);
                break;
            case Constants.ADD_PARTICIPANT_FLOW:
                tvEndFlowMassage.setText(R.string.text_participant_added);
                break;
        }
//        if (!mIsCreatedFlow) {
//            ivSuccess.setImageResource(R.drawable.ic_close_big_red);
//            tvEndFlowMassage.setText(R.string.title_good_deal_canceled);
//            tvEndFlowMassage.setTextColor(getResources().getColor(R.color.textColorRed));
//        }
    }

    private void initCloseButton() {
        RxView.clicks(ivClose)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    switch (mFlow) {
                        case Constants.CREATE_FLOW:
                            startChatScreen();
                            break;
                        case Constants.NOT_CREATE_FLOW:
                            finish();
                            break;
                        case Constants.ATTACH_BANK_ACCOUNT_FLOW:
                            finish();
                            break;
                        case Constants.ADD_PARTICIPANT_FLOW:
                            finish();
                            break;
                    }
//                    if (!mIsCreatedFlow) finish();
//                    else startChatScreen();
                });
    }

    private void startChatScreen() {
        ChatActivity_
                .intent(this)
                .mDealId(mGoodDealResponse.id)
//                .fromWhere(fromWhere)
                .flags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .start();
        finish();
    }
}
