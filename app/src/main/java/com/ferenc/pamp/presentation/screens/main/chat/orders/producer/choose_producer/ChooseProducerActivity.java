package com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.data.model.home.orders.Producer;
import com.ferenc.pamp.domain.OrderRepository;
import com.ferenc.pamp.presentation.base.BaseActivity;
import com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer.adapter.ProducerAdapter;
import com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer.adapter.ProducerDH;
import com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer.create_new_producer.CreateNewProducerActivity_;
import com.ferenc.pamp.presentation.utils.Constants;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by shonliu on 12/29/17.
 */

@EActivity(R.layout.activity_choose_producer)
public class ChooseProducerActivity extends BaseActivity implements ChooseProducerContract.View {


    private ChooseProducerContract.Presenter mPresenter;

    private List<ProducerDH> mProducerDHList;

    @Bean
    protected OrderRepository mOrderRepository;

    @Bean
    protected ProducerAdapter mProducerAdapter;

    @ViewById(R.id.toolbar_ACP)
    protected Toolbar toolbar;

    @ViewById(R.id.tvAddNewProducer_ACP)
    protected TextView tvAddNewProducer;

    @ViewById(R.id.rvProducerList_ACP)
    protected RecyclerView rvProducer;

    @ViewById(R.id.btnValider_ACP)
    protected Button btnValider;

    @StringRes(R.string.send_order_list_producer)
    protected String titleProducer;


    @AfterViews
    protected void initUI() {
        initBar();
        initClickListeners();

        rvProducer.setLayoutManager(new LinearLayoutManager(this));
        rvProducer.setAdapter(mProducerAdapter);
        rvProducer.setNestedScrollingEnabled(false);

        mPresenter.subscribe();
    }

    @Override
    protected int getContainer() {
        return -1;
    }

    @Override
    protected Toolbar getToolbar() {
        return toolbar;
    }

    @AfterInject
    @Override
    public void initPresenter() {
        new ChooseProducerPresenter(this, mOrderRepository);
        mProducerAdapter.setOnCardClickListener((view, position, viewType) ->
                mPresenter.selectItem(mProducerAdapter.getItem(position), position));
    }

    @Override
    public void setPresenter(ChooseProducerContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void initBar() {
        toolbarManager.setTitle(titleProducer);
        toolbarManager.showHomeAsUp(true);
        toolbarManager.closeActivityWhenBackArrowPressed(this);
    }

    private void initClickListeners() {
        RxView.clicks(tvAddNewProducer)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> createNewProducer());
        RxView.clicks(btnValider)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedValide());
    }

    private void createNewProducer() {
        CreateNewProducerActivity_.intent(this).startForResult(Constants.REQUEST_CODE_ACTIVITY_NEW_PRODUCER_CREATED);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.REQUEST_CODE_ACTIVITY_NEW_PRODUCER_CREATED) {
                String mProducerName = data.getStringExtra("producerName");
                String mProducerId = data.getStringExtra("producerId");
                mPresenter.addNewProducer(mProducerName, mProducerId);
            }
        }
    }

    @Override
    public void setProducerList(List<ProducerDH> _list) {
        mProducerDHList = _list;
        mProducerAdapter.setListDH(_list);
    }

    @Override
    public void updateItem(ProducerDH item, int position) {
        mProducerAdapter.changeItem(item, position);
    }

    @Override
    public void selectItem(int _poss) {
        mPresenter.selectItem(mProducerAdapter.getItem(_poss), _poss);
    }

    @Override
    public void finishActivityWithResult() {
        Producer producer = mPresenter.getSelectedProducer();
        Intent intent = new Intent();
        intent.putExtra("producerName", producer.name);
        intent.putExtra("producerId", producer.producerId);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void enabledValider() {
        btnValider.setBackground(getResources().getDrawable(R.drawable.bg_confirm_button));
        btnValider.setEnabled(true);
    }

    @Override
    public void addItemToList(String _producerName, String _producerId) {
        mProducerDHList.add(0, new ProducerDH(_producerId,_producerName));
        mProducerAdapter.setListDH(mProducerDHList);
    }
}
