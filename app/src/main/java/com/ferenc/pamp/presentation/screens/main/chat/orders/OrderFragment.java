package com.ferenc.pamp.presentation.screens.main.chat.orders;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.base.refreshable.RefreshableFragment;
import com.ferenc.pamp.presentation.base.refreshable.RefreshablePresenter;

import org.androidannotations.annotations.EFragment;

/**
 * Created by shonliu on 12/13/17.
 */
@EFragment
public class OrderFragment extends RefreshableFragment {
    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_chat_orders;
    }

    @Override
    protected RefreshablePresenter getPresenter() {
        return null;
    }
}
