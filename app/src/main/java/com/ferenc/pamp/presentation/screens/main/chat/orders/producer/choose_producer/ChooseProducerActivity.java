package com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.data.model.home.orders.Producer;
import com.ferenc.pamp.domain.OrderRepository;
import com.ferenc.pamp.presentation.base.BaseActivity;
import com.ferenc.pamp.presentation.base.list.EndlessScrollListener;
import com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer.adapter.ProducerAdapter;
import com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer.adapter.ProducerDH;
import com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer.create_update_producer.CreateUpdateProducerActivity_;
import com.ferenc.pamp.presentation.utils.Constants;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by shonliu on 12/29/17.
 */

@EActivity(R.layout.activity_choose_producer)
public class ChooseProducerActivity extends BaseActivity implements ChooseProducerContract.View {


    private ChooseProducerContract.Presenter mPresenter;

    private List<ProducerDH> mProducerDHList;

    protected EndlessScrollListener mScrollListener;

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

    @ViewById(R.id.progressBar_ACP)
    protected ProgressBar progressBar;


    @StringRes(R.string.send_order_list_producer)
    protected String titleProducer;


    @AfterViews
    protected void initUI() {
        initBar();
        initClickListeners();

        LinearLayoutManager llm = new LinearLayoutManager(this);

        rvProducer.setLayoutManager(llm);
        rvProducer.setAdapter(mProducerAdapter);

        mScrollListener = new EndlessScrollListener(llm, () -> {
//            if (isRefreshing()) return false;
            mPresenter.loadNextPage();
            return true;
        });

        rvProducer.addOnScrollListener(mScrollListener);

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
                .subscribe(o -> mPresenter.clickToCreateNewProducer());
        RxView.clicks(btnValider)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedValide());
    }

    @Override
    public void createNewProducer() {
        CreateUpdateProducerActivity_.intent(this).isCreate(true).startForResult(Constants.REQUEST_CODE_ACTIVITY_NEW_PRODUCER_CREATED);
    }

    @Override
    public void showProgressBar() {
        rvProducer.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        rvProducer.setVisibility(View.VISIBLE);
    }

    @Override
    public void addProducerList(List<ProducerDH> producerDHS) {
        mProducerAdapter.addListDH(producerDHS);
    }

    @Override
    public List<ProducerDH> getCurrentList() {
        return mProducerAdapter.getListDH();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String mProducerId = data.getStringExtra(Constants.KEY_PRODUCER_ID);
            String mProducerName = data.getStringExtra(Constants.KEY_PRODUCER_NAME);
            String mProducerEmail = data.getStringExtra(Constants.KEY_PRODUCER_EMAIL);
            String mProducerPhone = data.getStringExtra(Constants.KEY_PRODUCER_PHONE);
            String mProducerAddress = data.getStringExtra(Constants.KEY_PRODUCER_ADDRESS);
            String mProducerDescription = data.getStringExtra(Constants.KEY_PRODUCER_DESCRIPTION);

            if (requestCode == Constants.REQUEST_CODE_ACTIVITY_NEW_PRODUCER_CREATED)
                mPresenter.addNewProducer(new Producer(mProducerId, mProducerName, mProducerEmail, mProducerPhone, mProducerAddress, mProducerDescription));
            else if (requestCode == Constants.REQUEST_CODE_ACTIVITY_UPDATE_PRODUCER)
                mPresenter.updateProducer(new Producer(mProducerId, mProducerName, mProducerEmail, mProducerPhone, mProducerAddress, mProducerDescription));

        }
    }

    @Override
    public void setProducerList(List<ProducerDH> _list) {
        mProducerDHList = _list != null ? _list : new ArrayList<>();
        mProducerAdapter.setListDH(mProducerDHList);
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
        intent.putExtra(Constants.KEY_PRODUCER_ID, producer.producerId);
        intent.putExtra(Constants.KEY_PRODUCER_NAME, producer.name);
        intent.putExtra(Constants.KEY_PRODUCER_EMAIL, producer.email);
        intent.putExtra(Constants.KEY_PRODUCER_PHONE, producer.phone);
        intent.putExtra(Constants.KEY_PRODUCER_ADDRESS, producer.address);
        intent.putExtra(Constants.KEY_PRODUCER_DESCRIPTION, producer.description);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void enabledValider() {
        btnValider.setBackground(getResources().getDrawable(R.drawable.bg_confirm_button));
        btnValider.setEnabled(true);
    }

    @Override
    public void addItemToList(Producer _producer) {
        mProducerDHList.add(0, new ProducerDH(_producer,false));
        mProducerAdapter.setListDH(mProducerDHList);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
