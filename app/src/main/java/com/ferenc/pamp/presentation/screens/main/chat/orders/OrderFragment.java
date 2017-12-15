package com.ferenc.pamp.presentation.screens.main.chat.orders;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.content.ContentFragment;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

/**
 * Created by shonliu on 12/13/17.
 */
@EFragment
public class OrderFragment extends ContentFragment implements OrderContract.View {
    @Override
    public void setPresenter(OrderContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_chat_orders;
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    private OrderContract.Presenter mPresenter;

    @AfterInject
    @Override
    public void initPresenter() {
        new OrderPresenter(this);
    }

    @AfterViews
    protected void initUI() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
