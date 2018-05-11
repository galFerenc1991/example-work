package com.kubator.pamp.presentation.screens.main.chat;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RelativeLayout;

import com.kubator.pamp.R;
import com.kubator.pamp.presentation.base.BasePresenter;
import com.kubator.pamp.presentation.base.tabs.ContentTabsFragment;
import com.kubator.pamp.presentation.base.tabs.TabPagerAdapter;
import com.kubator.pamp.presentation.screens.main.chat.messenger.MessengerFragment_;
import com.kubator.pamp.presentation.screens.main.chat.orders.OrderFragment_;
import com.kubator.pamp.presentation.utils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;
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
    protected int fromWhere;

    @ViewById(R.id.rlTitle_FCT)
    protected RelativeLayout rlTitle;


    @Override
    public void addFragmentsToAdapter(TabPagerAdapter adapter) {
        switch (fromWhere) {
            case Constants.ITEM_TYPE_RE_BROADCAST:
                adapter.addFragment(MessengerFragment_.builder().build(), titleMessenger);
                break;
            case Constants.ITEM_TYPE_REUSE:
                adapter.addFragment(MessengerFragment_.builder().build(), titleMessenger);
                adapter.addFragment(OrderFragment_.builder().build(), titleOrders);
                break;
            default:
                throw new RuntimeException("ChatFragment :: addFragmentsToAdapter [Can find needed fragment(s)]");
        }
    }

    @AfterViews
    protected void toggleTabsVisibility() {
        rlTitle.setVisibility(View.GONE);

        if (fromWhere == Constants.ITEM_TYPE_RE_BROADCAST)
            toggleTabsVisibility(true);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (getChildFragmentManager().getFragments() == null)
            return;

        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
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
