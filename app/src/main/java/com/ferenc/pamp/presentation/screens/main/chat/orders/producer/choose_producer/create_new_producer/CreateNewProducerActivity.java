package com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer.create_new_producer;

import android.support.v7.widget.Toolbar;

import com.ferenc.pamp.R;
import com.ferenc.pamp.domain.OrderRepository;
import com.ferenc.pamp.presentation.base.BaseActivity;


import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;



/**
 * Created by shonliu on 1/2/18.
 */
@EActivity(R.layout.activity_create_new_producer)
public class CreateNewProducerActivity extends BaseActivity implements CreateNewProducerContract.View {

    private CreateNewProducerContract.Presenter mPresenter;

    @Bean
    protected OrderRepository mOrderRepository;

    @ViewById(R.id.toolbar_ACNP)
    protected Toolbar toolbar;

    @StringRes(R.string.send_order_list_producer)
    protected String titleProducer;

    @AfterInject
    @Override
    public void initPresenter() {
        new CreateNewProducerPresenter(this, mOrderRepository);
    }

    @AfterViews
    protected void initUI() {
        initBar();
        initClickListeners();

        mPresenter.subscribe();
    }

    @Override
    public void setPresenter(CreateNewProducerContract.Presenter _presenter) {
        mPresenter = _presenter;
    }

    @Override
    protected int getContainer() {
        return 0;
    }

    @Override
    protected Toolbar getToolbar() {
        return toolbar;
    }

    private void initBar() {
        toolbarManager.setTitle(titleProducer);
        toolbarManager.showHomeAsUp(true);
        toolbarManager.closeActivityWhenBackArrowPressed(this);
    }

    private void initClickListeners() {

    }
}
