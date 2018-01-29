package com.ferenc.pamp.presentation.screens.main.chat.orders;

import android.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.domain.GoodDealRepository;
import com.ferenc.pamp.domain.OrderRepository;
import com.ferenc.pamp.domain.UserRepository;
import com.ferenc.pamp.presentation.base.list.EndlessScrollListener;
import com.ferenc.pamp.presentation.base.refreshable.RefreshableFragment;
import com.ferenc.pamp.presentation.base.refreshable.RefreshablePresenter;
import com.ferenc.pamp.presentation.custom.bank_account.BankAccountActivity;
import com.ferenc.pamp.presentation.custom.bank_account.BankAccountActivity_;
import com.ferenc.pamp.presentation.screens.main.chat.orders.order_adapter.OrderAdapter;
import com.ferenc.pamp.presentation.screens.main.chat.orders.order_adapter.OrderDH;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.GoodDealResponseManager;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by shonliu on 12/13/17.
 */
@EFragment
public class OrderFragment extends RefreshableFragment implements OrderContract.View {
    @Override
    public void setPresenter(OrderContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_chat_orders;
    }

    @Override
    protected RefreshablePresenter getPresenter() {
        return mPresenter;
    }

    private OrderContract.Presenter mPresenter;
    protected EndlessScrollListener mScrollListener;

    @ViewById(R.id.tvPlaceHolder_FCO)
    protected TextView tvPlaceHolder;
    @ViewById(R.id.rvOrders_FCO)
    protected RecyclerView rvOrders;
    @ViewById(R.id.tvSendPdfToProducer_FCO)
    protected TextView tvSendPdfToProducer;
    @ViewById(R.id.tvProductName_FCO)
    protected TextView tvProductName;
    @ViewById(R.id.tvUnit_FCO)
    protected TextView tvUnit;
    @ViewById(R.id.tvOrderCreatedAtFCO)
    protected TextView tvOrderCreatedAt;
    @ViewById(R.id.btnConfirmOrders_FCO)
    protected Button btnConfirmOrders;

    @Bean
    protected OrderAdapter mOrderAdapter;
    @Bean
    protected OrderRepository mOrderRepository;
    @Bean
    protected GoodDealRepository mGoodDealRepository;
    @Bean
    protected UserRepository mUserRepository;
    @Bean
    protected GoodDealResponseManager mGoodDealResponseManager;

    @StringRes(R.string.button_ok)
    protected String mOk;
    @StringRes(R.string.button_cancel)
    protected String mCancel;

    @AfterInject
    @Override
    public void initPresenter() {
        new OrderPresenter(this, mOrderRepository, mGoodDealResponseManager, mGoodDealRepository, mUserRepository);
        mOrderAdapter.setOnCardClickListener((view, position, viewType) -> {

        });
    }

    @AfterViews
    protected void initUI() {
        RxView.clicks(btnConfirmOrders)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedConfButton(mOrderAdapter.getListDH()));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mScrollListener = new EndlessScrollListener(layoutManager, () -> {
            if (isRefreshing()) return false;
            mPresenter.loadNextPage();
            return true;
        });
        rvOrders.setLayoutManager(layoutManager);
        rvOrders.setAdapter(mOrderAdapter);
        rvOrders.addOnScrollListener(mScrollListener);

        mPresenter.subscribe();
    }

    @Override
    public void showBankAccountErrorPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.DialogTheme);
        builder.setView(R.layout.view_bank_account_error_popup)
                .setPositiveButton(mOk, (dialog, which) -> {
                    mPresenter.openCreateBankAccountFlow();
                })
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    public void showConfirmPopUp() {
        View dialogViewTitle = LayoutInflater.from(getContext())
                .inflate(R.layout.view_orders_confirm_popup_title, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.DialogTheme);
        builder.setCustomTitle(dialogViewTitle)
                .setView(R.layout.view_orders_confirm_popup_message)
                .setPositiveButton(mOk, (dialog, which) -> {
                    mPresenter.doConfirm();
                })
                .setNegativeButton(mCancel, (dialogInterface, i) -> {
                })
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    public void openCreateBankAccountFlow() {
        BankAccountActivity_.intent(this).start();
    }

    @Override
    public void setDealInfo(String _productName, String _util) {
        tvProductName.setText(_productName);
        tvUnit.setText(_util);
    }

    @Override
    public void showConfButton() {
        btnConfirmOrders.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideConfButton() {
        btnConfirmOrders.setVisibility(View.GONE);
    }

    @Override
    public void setOrdersList(List<OrderDH> _orders) {
        mScrollListener.reset();
        mOrderAdapter.setListDH(_orders);
    }

    @Override
    public void addOrder(List<OrderDH> _order) {
        mOrderAdapter.addListDH(_order);
    }

    @Override
    public void showPlaceHolderText() {
        tvPlaceHolder.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePlaceholderText() {
        tvPlaceHolder.setVisibility(View.GONE);
    }

    @Override
    public void initSendPdfInfo(boolean isSentEmpty) {
        String sentTime = new SimpleDateFormat("dd/MM HH:mm", Locale.FRANCE).format(new Date(isSentEmpty ? mGoodDealResponseManager.getGoodDealResponse().sent.getSentAt() : 0));

        String sendPdfToProducerText = isSentEmpty ? getString(R.string.text_send_pdf_to_producer) + "\n" + mGoodDealResponseManager.getGoodDealResponse().sent.getName() + " " + sentTime : "";

        tvSendPdfToProducer.setVisibility(isSentEmpty ? View.VISIBLE : View.GONE);


        tvSendPdfToProducer.setText(sendPdfToProducerText);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
