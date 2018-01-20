package com.ferenc.pamp.presentation.screens.main.chat.create_order.payment.select_card;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.domain.OrderRepository;
import com.ferenc.pamp.domain.UserRepository;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.content.ContentFragment;
import com.ferenc.pamp.presentation.base.content.ContentView;
import com.ferenc.pamp.presentation.screens.main.chat.create_order.payment.add_card.AddCardFragment;
import com.ferenc.pamp.presentation.screens.main.chat.create_order.payment.add_card.AddCardFragment_;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.CreditCardImageManager;
import com.ferenc.pamp.presentation.utils.GoodDealResponseManager;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.concurrent.TimeUnit;

/**
 * Created by
 * Ferenc on 2017.12.22..
 */

@EFragment
public class SelectCardFragment extends ContentFragment implements SelectCardContract.View {
    @Override
    public void setPresenter(SelectCardContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_select_card;
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    private SelectCardContract.Presenter mPresenter;

    @ViewById(R.id.cvAddBankCard_FSC)
    protected CardView cvAddBankCard;
    @ViewById(R.id.cvSelectBankCard_FSC)
    protected CardView cvSelectBankCard;
    @ViewById(R.id.btnValidate_FSC)
    protected Button btnValidate;
    @ViewById(R.id.tvCardNumber_FSC)
    protected TextView tvCardNumber;

    @FragmentArg
    protected double mQuantity;

    @Bean
    protected UserRepository mUserRepository;

    @Bean
    protected OrderRepository mOrderRepository;

    @Bean
    protected GoodDealResponseManager mGoodDealResponseManager;

    @AfterInject
    @Override
    public void initPresenter() {
        new SelectCardPresenter(this, mGoodDealResponseManager, mUserRepository, mOrderRepository, mQuantity);
    }

    @AfterViews
    protected void initUI() {
        initClickListeners();

        mPresenter.subscribe();
    }

    @Override
    public void showEndFlowCreateOrder() {
        mActivity.setResult(Activity.RESULT_OK);
        mActivity.finish();
    }

    @Override
    public void setCardNumber(String _cardNumber, String _brand) {
        cvSelectBankCard.setVisibility(View.VISIBLE);
        tvCardNumber.setText(_cardNumber);
        tvCardNumber.setCompoundDrawablesWithIntrinsicBounds(CreditCardImageManager.getBrandImage(_brand), 0, 0, 0);
    }

    private void initClickListeners() {
        RxView.clicks(cvSelectBankCard)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    btnValidate.setEnabled(true);
                    cvSelectBankCard.setCardBackgroundColor(getResources().getColor(R.color.textColorGreen));
                });
        RxView.clicks(cvAddBankCard)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mActivity.replaceFragment(AddCardFragment_.builder().mQuantity(mQuantity).build()));
        RxView.clicks(btnValidate)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.createOrder());
    }

}
