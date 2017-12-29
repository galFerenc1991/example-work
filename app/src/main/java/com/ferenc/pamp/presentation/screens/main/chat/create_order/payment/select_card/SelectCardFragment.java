package com.ferenc.pamp.presentation.screens.main.chat.create_order.payment.select_card;

import android.support.v7.widget.CardView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.content.ContentFragment;
import com.ferenc.pamp.presentation.base.content.ContentView;
import com.ferenc.pamp.presentation.screens.main.chat.create_order.payment.add_card.AddCardFragment;
import com.ferenc.pamp.presentation.screens.main.chat.create_order.payment.add_card.AddCardFragment_;
import com.ferenc.pamp.presentation.utils.Constants;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
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

    @FragmentArg
    protected int mQuantity;


    @AfterInject
    @Override
    public void initPresenter() {
        new SelectCardPresenter(this);
    }

    @AfterViews
    protected void initUI() {
        RxView.clicks(cvAddBankCard)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mActivity.replaceFragment(AddCardFragment_.builder().mQuantity(mQuantity).build()));
    }

}
