package com.ferenc.pamp.presentation.screens.main.chat.orders.producer.choose_producer.create_update_producer;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ferenc.pamp.R;
import com.ferenc.pamp.data.model.home.orders.Producer;
import com.ferenc.pamp.domain.OrderRepository;
import com.ferenc.pamp.presentation.base.BaseActivity;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.PlayServiceUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;


import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.concurrent.TimeUnit;


/**
 * Created by shonliu on 1/2/18.
 */
@EActivity(R.layout.activity_create_new_producer)
public class CreateUpdateProducerActivity extends BaseActivity implements CreateUpdateProducerContract.View {

    private CreateUpdateProducerContract.Presenter mPresenter;

    @Bean
    protected OrderRepository mOrderRepository;

    @Bean
    protected PlayServiceUtils playServiceUtils;

    @ViewById(R.id.toolbar_ACNP)
    protected Toolbar toolbar;

    @ViewById(R.id.etProducerName_ACNP)
    protected EditText etProducerName;

    @ViewById(R.id.etProducerEmail_ACNP)
    protected EditText etProducerEmail;

    @ViewById(R.id.etProducerPhone_ACNP)
    protected EditText etProducerPhone;

    @ViewById(R.id.tvProducerAddress_ACNP)
    protected TextView tvProducerAddress;

    @ViewById(R.id.etProducerDescription_ACNP)
    protected EditText etProducerDescription;

    @ViewById(R.id.btnValider_ACNP)
    protected Button btnValider;

    @ViewById(R.id.progressBar_ACNP)
    protected ProgressBar progressBar;

    @ViewById(R.id.scrollView_ACNP)
    protected ScrollView scrollView;

    @StringRes(R.string.send_order_list_producer)
    protected String titleProducer;

    @Extra
    protected boolean isCreate;

    @Extra
    protected Producer mProducer;

    @AfterInject
    @Override
    public void initPresenter() {
        new CreateUpdateProducerPresenter(this, mOrderRepository, isCreate, mProducer);
    }

    @AfterViews
    protected void initUI() {
        initBar();
        initClickListeners();
        mPresenter.subscribe();
    }

    @Override
    public void setPresenter(CreateUpdateProducerContract.Presenter _presenter) {
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
                .subscribe(o ->
                        mPresenter.createUpdateProducer(
                                getText(etProducerName),
                                getText(etProducerEmail),
                                getText(etProducerPhone),
                                tvProducerAddress.getText().toString(),
                                getText(etProducerDescription))
                );
        RxView.clicks(tvProducerAddress)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickOnAddress());

        RxTextView.afterTextChangeEvents(etProducerName).subscribe(textViewAfterTextChangeEvent -> mPresenter.validateFields().accept(validerData()));
        RxTextView.afterTextChangeEvents(etProducerEmail).subscribe(textViewAfterTextChangeEvent -> mPresenter.validateFields().accept(validerData()));
        RxTextView.afterTextChangeEvents(etProducerPhone).subscribe(textViewAfterTextChangeEvent -> mPresenter.validateFields().accept(validerData()));
        RxTextView.afterTextChangeEvents(tvProducerAddress).subscribe(textViewAfterTextChangeEvent -> mPresenter.validateFields().accept(validerData()));
        RxTextView.afterTextChangeEvents(etProducerDescription).subscribe(textViewAfterTextChangeEvent -> mPresenter.validateFields().accept(validerData()));
    }

    private String getText(EditText et) {
        return et.getText().toString().trim();
    }

    @Override
    public void openAutocompletePlaceScreen() {
        if (playServiceUtils.checkPlayServices(this)) {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                    .build();
            try {
                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                        .setFilter(typeFilter)
                        .build(this);
                startActivityForResult(intent, Constants.REQUEST_CODE_ACTIVITY_AUTOCOMPLETE_PLACE);
            } catch (GooglePlayServicesRepairableException e) {
                playServiceUtils.checkPlayServices(this);
            } catch (GooglePlayServicesNotAvailableException e) {
                Toast.makeText(this, "Google Play Services is not available.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnActivityResult(Constants.REQUEST_CODE_ACTIVITY_AUTOCOMPLETE_PLACE)
    protected void onDeliveryPlaceSelected(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(this, data);
            tvProducerAddress.setText(place.getAddress().toString());
        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Toast.makeText(this, "Error while select address. Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean validerData() {
        return mPresenter.validateData(getText(etProducerName), getText(etProducerEmail), getText(etProducerPhone), tvProducerAddress.getText().toString(), getText(etProducerDescription));
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
        intent.putExtra(Constants.KEY_PRODUCER_NAME, producer.name);
        intent.putExtra(Constants.KEY_PRODUCER_ID, producer.producerId);
        intent.putExtra(Constants.KEY_PRODUCER_EMAIL, producer.email);
        intent.putExtra(Constants.KEY_PRODUCER_PHONE, producer.phone);
        intent.putExtra(Constants.KEY_PRODUCER_ADDRESS, producer.address);
        intent.putExtra(Constants.KEY_PRODUCER_DESCRIPTION, producer.description);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void setProducerData(Producer _producer) {
        etProducerName.setText(_producer.name);
        etProducerEmail.setText(_producer.email);
        etProducerPhone.setText(_producer.phone);
        tvProducerAddress.setText(_producer.address);
        etProducerDescription.setText(_producer.description);
    }

    @Override
    public void showProgress() {
        scrollView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
    }
}
