package com.ferenc.pamp.presentation.screens.main.chat;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.presentation.base.BaseActivity;
import com.ferenc.pamp.presentation.utils.Constants;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * Created by shonliu on 12/13/17.
 */

@EActivity(R.layout.activity_chat)
public class ChatActivity extends BaseActivity {

    @Extra
    protected int fromWhere;

    @Extra
    protected GoodDealResponse goodDealResponse;

    @ViewById(R.id.ivShare_AC)
    protected ImageView shareBonPlan;

    @ViewById(R.id.ivSettings_AC)
    protected ImageView ivSettings;

    @ViewById(R.id.tvParticipants_AC)
    protected TextView tvParticipants;


    @AfterViews
    protected void initFragment() {
        switch (fromWhere) {
            case Constants.ITEM_TYPE_RE_BROADCAST:
                ivSettings.setVisibility(View.VISIBLE);
                break;
            case Constants.ITEM_TYPE_REUSE:
                shareBonPlan.setVisibility(View.VISIBLE);
                break;
            default:
                throw new RuntimeException("ChatActivity :: initFragment [Can find fromWhere]");
        }
        replaceFragment(ChatFragment_.builder().fromWhere(fromWhere).goodDealResponse(goodDealResponse).build());
    }

    @Override
    protected int getContainer() {
        return R.id.fragmentContainer;
    }

    @Override
    protected Toolbar getToolbar() {
        return new Toolbar(this);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Click(R.id.ivBack_AC)
    protected void backPressed() {
        onBackPressed();
    }

    @Click(R.id.ivShare_AC)
    protected void shareBonPlan() {

    }

    @Click(R.id.llBonPlanParticipants_AC)
    protected void showParticipants() {

    }

    @Click(R.id.ivSettings_AC)
    protected void settings() {

    }



}
