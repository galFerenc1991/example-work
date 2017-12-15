package com.ferenc.pamp.presentation.screens.main.chat;

import android.app.Activity;

import com.ferenc.pamp.R;
import com.ferenc.pamp.data.model.home.good_deal.GoodDealResponse;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.tabs.ContentTabsFragment;
import com.ferenc.pamp.presentation.base.tabs.TabPagerAdapter;
import com.ferenc.pamp.presentation.screens.main.chat.messenger.MessengerFragment_;
import com.ferenc.pamp.presentation.screens.main.chat.orders.OrderFragment_;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.res.StringRes;

/**
 * Created by shonliu on 12/12/17.
 */
@EFragment
public class ChatFragment extends ContentTabsFragment {


    @StringRes(R.string.title_messenger)
    protected String titleMessenger;

    @StringRes(R.string.title_orders)
    protected String titleOrders;

    @FragmentArg
    protected GoodDealResponse goodDealResponse;


    @Override
    public void addFragmentsToAdapter(TabPagerAdapter adapter) {
        adapter.addFragment(MessengerFragment_.builder().goodDealResponse(goodDealResponse).build(), titleMessenger);
        adapter.addFragment(OrderFragment_.builder().build(), titleOrders);

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_content_tabs;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }
}
