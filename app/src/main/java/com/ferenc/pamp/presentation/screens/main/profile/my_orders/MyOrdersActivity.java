package com.ferenc.pamp.presentation.screens.main.profile.my_orders;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.ferenc.pamp.R;
import com.ferenc.pamp.domain.OrderRepository;
import com.ferenc.pamp.presentation.base.BaseActivity;
import com.ferenc.pamp.presentation.base.list.EndlessScrollListener;
import com.ferenc.pamp.presentation.screens.main.chat.orders.producer.preview_pdf.PreviewPDFActivity_;
import com.ferenc.pamp.presentation.screens.main.profile.my_orders.adapter.MyOrderDH;
import com.ferenc.pamp.presentation.screens.main.profile.my_orders.adapter.MyOrdersAdapter;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.List;

/**
 * Created by shonliu on 1/10/18.
 */
@EActivity(R.layout.activity_my_orders)
public class MyOrdersActivity extends BaseActivity implements MyOrdersContract.View {

    private MyOrdersContract.Presenter mPresenter;

    protected EndlessScrollListener mScrollListener;

    @Bean
    protected OrderRepository mOrderRepository;

    @Bean
    protected MyOrdersAdapter mMyOrdersAdapter;

    @StringRes(R.string.text_good_plans)
    protected String titleMyOrders;

    @ViewById(R.id.toolbar_AMO)
    protected Toolbar toolbar;

    @ViewById(R.id.progressBar_AMO)
    protected ProgressBar progressBar;

    @ViewById(R.id.pbPagination_AMO)
    protected ProgressBar pbPagination;

    @ViewById(R.id.rvMyOrders_AMO)
    protected RecyclerView rvMyOrders;


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
        new MyOrdersPresenter(this, mOrderRepository);
        mMyOrdersAdapter.setOnCardClickListener((view, position, viewType) ->
                mPresenter.selectItem(mMyOrdersAdapter.getItem(position), position));
    }

    @Override
    public void setPresenter(MyOrdersContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @AfterViews
    protected void initUI() {
        initBar();
        initAdapter();

        mPresenter.subscribe();
    }


    private void initBar() {
        toolbarManager.setTitle(titleMyOrders);
        toolbarManager.showHomeAsUp(true);
        toolbarManager.closeActivityWhenBackArrowPressed(this);
        toolbarManager.setIconHome(R.drawable.ic_arrow_back_green);
    }

    private void initAdapter() {
        LinearLayoutManager llm = new LinearLayoutManager(this);

        rvMyOrders.setLayoutManager(llm);
        rvMyOrders.setAdapter(mMyOrdersAdapter);

        mScrollListener = new EndlessScrollListener(llm, () -> {
            mPresenter.loadNextPage();
            return true;
        });

        rvMyOrders.addOnScrollListener(mScrollListener);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showPaginationProgressBar() {
        pbPagination.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePaginationProgressBar() {
        pbPagination.setVisibility(View.GONE);
    }

    @Override
    public void setOrdersList(List<MyOrderDH> _orders) {
        mMyOrdersAdapter.setListDH(_orders);
    }

    @Override
    public void addOrdersList(List<MyOrderDH> _orders) {
        mMyOrdersAdapter.addListDH(_orders);
    }

    @Override
    public void openOrderPreview(String _orderId) {
        PreviewPDFActivity_.intent(this).mOrderId(_orderId).start();
    }


}
