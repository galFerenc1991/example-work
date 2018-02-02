package com.ferenc.pamp.presentation.custom.bank_account;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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
import com.ferenc.pamp.presentation.utils.PlayServiceUtils;
import com.ferenc.pamp.presentation.utils.SignedUserManager;
import com.ferenc.pamp.presentation.utils.ToastManager;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;

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
    @ViewById(R.id.llAddress_FBA)
    protected LinearLayout llAddress;
    @ViewById(R.id.tvAddress_FBA)
    protected TextView tvAddress;
    @ViewById(R.id.llBirthDate_FBA)
    protected LinearLayout llBirthDate;
    @ViewById(R.id.tvBirthDate_FBA)
    protected TextView tvBirthDate;
    @ViewById(R.id.etIban_FBA)
    protected EditText etIban;
    @ViewById(R.id.btnConfirm_FBA)
    protected Button btnConfirm;

    @Bean
    protected BankAccountRepository mBankAccountRepository;
    @Bean
    protected PlayServiceUtils playServiceUtils;
    @Bean
    protected SignedUserManager mSignedUserManager;

    @AfterInject
    @Override
    public void initPresenter() {
        new BankAccountPresenter(this, mBankAccountRepository, mSignedUserManager);
    }

    @AfterViews
    protected void initUI() {
        RxView.clicks(btnConfirm)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedValidate(etIban.getText().toString()));
        RxView.clicks(llCountrySpinner)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedCountry());
        RxView.clicks(llAddress)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedAddress());
        RxView.clicks(llBirthDate)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedBirthDay());
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
    public void openAutocompletePlaceScreen() {
        if (playServiceUtils.checkPlayServices(getActivity())) {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                    .build();
            try {
                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                        .setFilter(typeFilter)
                        .build(getActivity());
                startActivityForResult(intent, Constants.REQUEST_CODE_ACTIVITY_AUTOCOMPLETE_PLACE);
            } catch (GooglePlayServicesRepairableException e) {
                playServiceUtils.checkPlayServices(getActivity());
            } catch (GooglePlayServicesNotAvailableException e) {
                ToastManager.showToast("Google Play Services is not available.");
//                Toast.makeText(this, "Google Play Services is not available.", Toast.LENGTH_SHORT).show();
//                showErrorMessage(Constants.MessageType.PLAY_SERVICES_UNAVAILABLE);
            }
        }
    }

    @OnActivityResult(Constants.REQUEST_CODE_ACTIVITY_AUTOCOMPLETE_PLACE)
    protected void onDeliveryPlaceSelected(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(getActivity(), data);
            double lat = place.getLatLng().latitude;
            double lon = place.getLatLng().longitude;

            final Geocoder gcd = new Geocoder(getActivity(), Locale.ENGLISH);
            try {
                List<Address> addresses = gcd.getFromLocation(lat, lon, 10);
                Address address = addresses.get(0);
                if (address.getLocality() != null && address.getPostalCode() != null) {
                    String country = address.getCountryCode();
                    String city = address.getLocality();
                    String street = address.getThoroughfare();
                    String postalCode = address.getPostalCode();
                    mPresenter.setSelectedAddress(address.getAddressLine(0), country, city, street, postalCode);
                } else {
                    ToastManager.showToast("Selected address not have Postal Code");
                }
            } catch (Exception e) {
                ToastManager.showToast(e.getMessage());
            }
        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            ToastManager.showToast("Error while select address. Try again.");
        }
    }

    @Override
    public void opedDatePicker(Calendar _calendar) {
        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(Locale.FRANCE);

        Calendar result = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DialogTheme, (view, year, month, dayOfMonth) -> {
            result.set(year, month, dayOfMonth);
            mPresenter.setBirthDay(result);
        }, _calendar.get(Calendar.YEAR), _calendar.get(Calendar.MONTH), _calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 31556926000L * 13);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 315569260000L * 7);
        datePickerDialog.setTitle(R.string.birth_date_dialog_title);
        datePickerDialog.show();
    }

    @Override
    public void setUserBirthDay(String _birthDay) {
        tvBirthDate.setText(_birthDay);
    }

    @Override
    public void setCountry(String _country) {
        tvCountry.setText(_country);
    }

    @Override
    public void setSelectedAddress(String _fullAddress) {
        tvAddress.setText(_fullAddress);
    }

    @Override
    public void setIBan(String _iBan) {
        etIban.setText(_iBan);
    }

    @Override
    public void showSuccessEndFlowScreen() {
        mActivity.setResult(RESULT_OK);
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
