package com.ferenc.pamp.presentation.screens.auth.sign_up.country_picker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.ferenc.pamp.R;
import com.ferenc.pamp.domain.AuthRepository;
import com.ferenc.pamp.presentation.screens.auth.sign_up.country_picker.adapter.CountryAdapter;
import com.ferenc.pamp.presentation.screens.auth.sign_up.country_picker.adapter.CountryDH;
import com.ferenc.pamp.presentation.utils.Constants;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by
 * Ferenc on 2017.11.20..
 */
@EActivity(R.layout.activity_country_picker)
public class CountryPickerActivity extends AppCompatActivity implements CountryContract.View {

    private CountryContract.Presenter mPresenter;

    @ViewById(R.id.rvCountryList_FCL)
    protected RecyclerView rvCountryList;

    @ViewById(R.id.btnOk_FCL)
    protected Button btnOk;
    @ViewById(R.id.btnCancel_FCL)
    protected Button btnCancel;
    @ViewById(R.id.pbCountry_FCL)
    protected ProgressBar pbCountry;

    @Bean
    protected AuthRepository mAuthRepo;
    @Bean
    protected CountryAdapter mCountryAdapter;

    @AfterInject
    @Override
    public void initPresenter() {
        new CountryPresenter(this, mAuthRepo, getIntent().getStringExtra(Constants.KEY_COUNTRY));
        mCountryAdapter.setOnCardClickListener((view, position, viewType) ->
                mPresenter.selectItem(mCountryAdapter.getItem(position), position));
    }

    @AfterViews
    protected void initUI() {
        rvCountryList.setLayoutManager(new LinearLayoutManager(this));
        rvCountryList.setAdapter(mCountryAdapter);

        RxView.clicks(btnOk)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.pickCountry());

        RxView.clicks(btnCancel)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> finish());

        mPresenter.subscribe();
    }

    @Override
    public void setCountryList(List<CountryDH> list) {
        mCountryAdapter.setListDH(list);
    }

    @Override
    public void updateItem(CountryDH item, int position) {
        mCountryAdapter.changeItem(item, position);
    }

    @Override
    public void returnResult(String country) {
        Intent intent = new Intent();
        intent.putExtra(Constants.KEY_COUNTRY, country);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void setPresenter(CountryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgressMain() {
        pbCountry.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgressPagination() {

    }

    @Override
    public void hideProgress() {
        pbCountry.setVisibility(View.GONE);
    }

    @Override
    public void showErrorMessage(Constants.MessageType messageType) {

    }

    @Override
    public void showCustomMessage(String msg, boolean isDangerous) {

    }

    @Override
    public void showPlaceholder(Constants.PlaceholderType placeholderType) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
