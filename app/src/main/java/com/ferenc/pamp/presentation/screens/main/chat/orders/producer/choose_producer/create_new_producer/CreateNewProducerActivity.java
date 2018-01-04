package com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer.create_new_producer;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.ferenc.pamp.R;
import com.ferenc.pamp.data.model.home.orders.Producer;
import com.ferenc.pamp.domain.OrderRepository;
import com.ferenc.pamp.presentation.base.BaseActivity;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.ValidationManager;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;


import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.concurrent.TimeUnit;


/**
 * Created by shonliu on 1/2/18.
 */
@EActivity(R.layout.activity_create_new_producer)
public class CreateNewProducerActivity extends BaseActivity implements CreateNewProducerContract.View {

    private CreateNewProducerContract.Presenter mPresenter;

    @Bean
    protected OrderRepository mOrderRepository;

    @ViewById(R.id.toolbar_ACNP)
    protected Toolbar toolbar;

    @ViewById(R.id.etProducerName_ACNP)
    protected EditText etProducerName;

    @ViewById(R.id.etProducerEmail_ACNP)
    protected EditText etProducerEmail;

    @ViewById(R.id.etProducerPhone_ACNP)
    protected EditText etProducerPhone;

    @ViewById(R.id.etProducerAddress_ACNP)
    protected EditText etProducerAddress;

    @ViewById(R.id.etProducerDescription_ACNP)
    protected EditText etProducerDescription;

    @ViewById(R.id.btnValider_ACNP)
    protected Button btnValider;

    @StringRes(R.string.send_order_list_producer)
    protected String titleProducer;

    @AfterInject
    @Override
    public void initPresenter() {
        new CreateNewProducerPresenter(this, mOrderRepository);
    }

    @AfterViews
    protected void initUI() {
        initBar();
        initClickListeners();

        mPresenter.subscribe();
    }

    @Override
    public void setPresenter(CreateNewProducerContract.Presenter _presenter) {
        mPresenter = _presenter;
    }

    @Override
    protected int getContainer() {
        return 0;
    }

    @Override
    protected Toolbar getToolbar() {
        return toolbar;
    }

    private void initBar() {
        toolbarManager.setTitle(titleProducer);
        toolbarManager.showHomeAsUp(true);
        toolbarManager.closeActivityWhenBackArrowPressed(this);
    }

    private void initClickListeners() {
        RxView.clicks(btnValider)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.createProducer(
                        getText(etProducerName),
                        getText(etProducerEmail),
                        getText(etProducerPhone),
                        getText(etProducerAddress),
                        getText(etProducerDescription))
                );

        RxTextView.afterTextChangeEvents(etProducerName).subscribe(textViewAfterTextChangeEvent -> mPresenter.validateFields().accept(validerData()));
        RxTextView.afterTextChangeEvents(etProducerEmail).subscribe(textViewAfterTextChangeEvent -> mPresenter.validateFields().accept(validerData()));

    }

    private String getText(EditText et) {
        return et.getText().toString().trim();
    }

    @Override
    public boolean validerData() {
        return !TextUtils.isEmpty(getText(etProducerName))
                && !TextUtils.isEmpty(getText(etProducerEmail))
                && ValidationManager.validateEmail(getText(etProducerEmail)) == ValidationManager.OK;
    }

    @Override
    public void enableValidateBtn(boolean _isEnabled) {
        btnValider.setBackground(getResources().getDrawable(!_isEnabled ? R.drawable.bg_confirm_button_disable : R.drawable.bg_confirm_button));
        btnValider.setEnabled(_isEnabled);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finishActivityWithResult(Producer producer) {

        Intent intent = new Intent();
        intent.putExtra("producerName", producer.name);
        intent.putExtra("producerId", producer.producerId);
        setResult(RESULT_OK, intent);
        finish();
    }
}
