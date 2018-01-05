package com.ferenc.pamp.presentation.custom.bank_account;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.domain.BankAccountRepository;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.content.ContentFragment;
import com.ferenc.pamp.presentation.custom.end_flow_screen.EndFlowActivity_;
import com.ferenc.pamp.presentation.screens.auth.sign_up.country_picker.CountryPickerActivity_;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.ToastManager;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.CompletableOnSubscribe;

/**
 * Created by
 * Ferenc on 2018.01.04..
 */

@EFragment
public class BankAccountFragment extends ContentFragment implements BankAccountContract.View {
    @Override
    public void setPresenter(BankAccountContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_bank_account;
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    private BankAccountContract.Presenter mPresenter;

    @ViewById(R.id.llCountrySpinner_FBA)
    protected LinearLayout llCountrySpinner;
    @ViewById(R.id.tvCountry_FBA)
    protected TextView tvCountry;
    @ViewById(R.id.etIban_FBA)
    protected EditText etIban;
    @ViewById(R.id.btnConfirm_FBA)
    protected Button btnConfirm;

    @Bean
    protected BankAccountRepository mBankAccountRepository;

    @AfterInject
    @Override
    public void initPresenter() {
        new BankAccountPresenter(this, mBankAccountRepository);
    }

    @AfterViews
    protected void initUI() {
        RxView.clicks(btnConfirm)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedValidate(etIban.getText().toString()));
        RxView.clicks(llCountrySpinner)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedCountry());
    }

    @Override
    public void openCountryPicker(String _selectedCountry) {
        CountryPickerActivity_.intent(this)
                .extra(Constants.KEY_COUNTRY, _selectedCountry)
                .startForResult(Constants.REQUEST_CODE_COUNTRY_PICKER);
    }

    @OnActivityResult(Constants.REQUEST_CODE_COUNTRY_PICKER)
    protected void resultCountryCode(@OnActivityResult.Extra(Constants.KEY_COUNTRY) String country) {
        mPresenter.setSelectedCountry(country);
    }

    @Override
    public void setCountry(String _country) {
        tvCountry.setText(_country);
    }

    @Override
    public void showSuccessEndFlowScreen() {
        mActivity.finish();
        EndFlowActivity_.intent(this)
                .mFlow(Constants.ATTACH_BANK_ACCOUNT_FLOW)
                .start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
