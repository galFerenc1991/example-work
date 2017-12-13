package com.ferenc.pamp.presentation.screens.main.chat.messenger;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.base.refreshable.RefreshableFragment;
import com.ferenc.pamp.presentation.base.refreshable.RefreshablePresenter;

import org.androidannotations.annotations.EFragment;

/**
 * Created by shonliu on 12/12/17.
 */
@EFragment
public class MessengerFragment extends RefreshableFragment {
    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_chat_messenger;
    }

    @Override
    protected RefreshablePresenter getPresenter() {
        return null;
    }
}
