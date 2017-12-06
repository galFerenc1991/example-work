package com.ferenc.pamp.presentation.screens.main.propose.description;

import android.app.Activity;
import android.widget.TextView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.content.ContentFragment;
import com.ferenc.pamp.presentation.custom.InputActivity_;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.GoodDealManager;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.concurrent.TimeUnit;

/**
 * Created by
 * Ferenc on 2017.11.22..
 */
@EFragment
public class DescriptionFragment extends ContentFragment implements DescriptionContract.View {
    @Override
    public void setPresenter(DescriptionContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_description;
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    private DescriptionContract.Presenter mPresenter;

    @ViewById(R.id.tvName_FD)
    protected TextView tvName;
    @ViewById(R.id.tvDescription_FD)
    protected TextView tvDescription;
    @ViewById(R.id.tvPrice_FD)
    protected TextView tvPrice;
    @ViewById(R.id.tvPriceDescription_FD)
    protected TextView tvPriceDescription;
    @ViewById(R.id.tvQuantity_FD)
    protected TextView tvQuantity;

    @Bean
    protected GoodDealManager mGoodDealManager;

    @AfterInject
    @Override
    public void initPresenter() {
        new DescriptionPresenter(this, mGoodDealManager);
    }

    @AfterViews
    protected void initUI() {
        RxView.clicks(tvName)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedInputField(Constants.REQUEST_CODE_INPUT_ACTIVITY_NAME));

        RxView.clicks(tvDescription)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedInputField(Constants.REQUEST_CODE_INPUT_ACTIVITY_DESCRIPTION));

        RxView.clicks(tvPrice)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedInputField(Constants.REQUEST_CODE_INPUT_ACTIVITY_PRICE));

        RxView.clicks(tvPriceDescription)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedInputField(Constants.REQUEST_CODE_INPUT_ACTIVITY_PRICE_DESCRIPTION));

        RxView.clicks(tvQuantity)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedInputField(Constants.REQUEST_CODE_INPUT_ACTIVITY_QUANTITY));
    }

    @Override
    public void openInputActivity(int _requestCode) {
        InputActivity_.intent(this)
                .extra(Constants.KEY_INPUT_INDICATOR, _requestCode)
                .startForResult(_requestCode);
    }

    @OnActivityResult(Constants.REQUEST_CODE_INPUT_ACTIVITY_NAME)
    void resultName(int resultCode, @OnActivityResult.Extra(value = Constants.KEY_INPUT_RESULT) String _name) {
        if (resultCode == Activity.RESULT_OK)
            mPresenter.saveName(_name);
    }

    @OnActivityResult(Constants.REQUEST_CODE_INPUT_ACTIVITY_DESCRIPTION)
    void resultDescription(int resultCode, @OnActivityResult.Extra(value = Constants.KEY_INPUT_RESULT) String _description) {
        if (resultCode == Activity.RESULT_OK)
            mPresenter.saveDescription(_description);
    }

    @OnActivityResult(Constants.REQUEST_CODE_INPUT_ACTIVITY_PRICE)
    void resultPrice(int resultCode, @OnActivityResult.Extra(value = Constants.KEY_INPUT_RESULT) String _price) {
        if (resultCode == Activity.RESULT_OK)
            mPresenter.savePrice(_price);
    }

    @OnActivityResult(Constants.REQUEST_CODE_INPUT_ACTIVITY_PRICE_DESCRIPTION)
    void resultPriceDescription(int resultCode, @OnActivityResult.Extra(value = Constants.KEY_INPUT_RESULT) String _priceDescription) {
        if (resultCode == Activity.RESULT_OK)
            mPresenter.savePriceDescription(_priceDescription);
    }

    @OnActivityResult(Constants.REQUEST_CODE_INPUT_ACTIVITY_QUANTITY)
    void resultQuantity(int resultCode, @OnActivityResult.Extra(value = Constants.KEY_INPUT_RESULT) String _quantity) {
        if (resultCode == Activity.RESULT_OK)
            mPresenter.saveQuantity(_quantity);
    }

    @Override
    public void setName(String _name) {
        tvName.setText(_name);
    }

    @Override
    public void setDescription(String _description) {
        tvDescription.setText(_description);
    }

    @Override
    public void setPrice(String _price) {
        tvPrice.setText(_price);
    }

    @Override
    public void setPriceDescription(String _priceDescription) {
        tvPriceDescription.setText(_priceDescription);
    }

    @Override
    public void setQuantity(String _quantity) {
        tvQuantity.setText(_quantity);
    }

}
