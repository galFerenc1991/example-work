package com.ferenc.pamp.presentation.screens.main.chat.create_order.payment.save_card;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.content.ContentFragment;
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
 * Ferenc on 2017.12.25..
 */

@EFragment
public class SaveCardFragment extends ContentFragment implements SaveCardContract.View {

    private SaveCardContract.Presenter mPresenter;

    @ViewById(R.id.tvCardNumber_FSC)
    protected TextView tvCardNumber;
    @ViewById(R.id.switchSaveCard_FSC)
    protected Switch switchSaveCard;
    @ViewById(R.id.btnValidate_FSC)
    protected Button btnValidate;

    @FragmentArg
    protected String mLast4;
    @FragmentArg
    protected String mCardType;

    @AfterInject
    @Override
    public void initPresenter() {
        new SaveCardPresenter(this);
    }

    @AfterViews
    protected void InitUI() {
        tvCardNumber.setText(mLast4);
        setClickListeners();
    }

    private void setClickListeners() {
        RxView.clicks(btnValidate)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedValidate());

        switchSaveCard.setOnCheckedChangeListener((compoundButton, b) -> {
            mPresenter.setSaveCardInProfile(b);
        });
    }

    @Override
    public void openSuccessCreatedCardScreen() {
        mActivity.setResult(Activity.RESULT_OK);
        mActivity.finish();
//        mActivity.replaceFragment(EndFlowFragment_.builder().mIsCreatedFlow(true).build());
    }

    @Override
    public void setPresenter(SaveCardContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_save_card;
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }
}
