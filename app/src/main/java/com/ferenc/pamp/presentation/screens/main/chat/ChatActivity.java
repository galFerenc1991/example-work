package com.ferenc.pamp.presentation.screens.main.chat;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ferenc.pamp.R;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.presentation.base.BaseActivity;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

/**
 * Created by shonliu on 12/13/17.
 */

@EActivity(R.layout.activity_chat)
public class ChatActivity extends BaseActivity {

    @Extra
    protected int fromWhere;

    @Extra
    protected GoodDealResponse goodDealResponse;

    Toolbar toolbar;

    @AfterViews
    protected void initFragment() {
        replaceFragment(ChatFragment_.builder().goodDealResponse(goodDealResponse).build());

    }

    @Override
    protected int getContainer() {
        return R.id.fragmentContainer;
    }

    @Override
    protected Toolbar getToolbar() {
        return new Toolbar(this);
    }
}
