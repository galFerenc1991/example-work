package com.ferenc.pamp.presentation.screens.main.propose.delivery.delivery_place;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ferenc.pamp.R;
import com.ferenc.pamp.domain.GoodDealRepository;
import com.ferenc.pamp.presentation.screens.main.propose.delivery.delivery_place.delivery_place_adapter.DeliveryPlaceAdapter;
import com.ferenc.pamp.presentation.screens.main.propose.delivery.delivery_place.delivery_place_adapter.DeliveryPlaceDH;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.PlayServiceUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by
 * Ferenc on 2017.11.29..
 */
@EActivity(R.layout.activity_delivery_place)
public class DeliveryPlaceActivity extends AppCompatActivity implements DeliveryPlaceContract.View {

    private DeliveryPlaceContract.Presenter mPresenter;

    @ViewById(R.id.ivBack_ADP)
    protected ImageView ivBack;
    @ViewById(R.id.cvSelectPlace_ADD)
    protected CardView cvSelectPlace;
    @ViewById(R.id.tvAddress_ADP)
    protected TextView tvAddress;
    @ViewById(R.id.rvCountries_ADP)
    protected RecyclerView rvCountries;
    @ViewById(R.id.pbAddresses_ADP)
    protected ProgressBar progressBar;

    @Bean
    protected PlayServiceUtils playServiceUtils;

    @Bean
    protected GoodDealRepository mGoodDealRepository;

    @Bean
    protected DeliveryPlaceAdapter mDeliveryPlaceAdapter;

    @Extra
    protected boolean isRebroadcast;

    @AfterInject
    @Override
    public void initPresenter() {
        new DeliveryPlacePresenter(this, mGoodDealRepository, isRebroadcast);
        mDeliveryPlaceAdapter.setOnCardClickListener((view, position, viewType) ->
                mPresenter.selectItem(mDeliveryPlaceAdapter.getItem(position), position)
        );
    }

    @Override
    public void setPresenter(DeliveryPlaceContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @AfterViews
    protected void initUI() {

        rvCountries.setLayoutManager(new LinearLayoutManager(this));
        rvCountries.setAdapter(mDeliveryPlaceAdapter);

        if (isRebroadcast){
            setTheme(R.style.ReBroadcastTheme);
            ivBack.setImageResource(R.drawable.ic_arrow_back_yellow);
            tvAddress.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_add_circle_yellow, 0, 0, 0);
        }

        RxView.clicks(ivBack)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedBack());

        RxView.clicks(cvSelectPlace)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedSelectPlace());

        mPresenter.subscribe();
    }

    @Override
    public void openAutocompletePlaceScreen() {
        if (playServiceUtils.checkPlayServices(this)) {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE)
                    .build();
            try {
                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                        .setFilter(typeFilter)
                        .build(this);
                startActivityForResult(intent, Constants.REQUEST_CODE_ACTIVITY_AUTOCOMPLETE_PLACE);
            } catch (GooglePlayServicesRepairableException e) {
                playServiceUtils.checkPlayServices(this);
            } catch (GooglePlayServicesNotAvailableException e) {
                Toast.makeText(this, "Google Play Services is not available.", Toast.LENGTH_SHORT).show();
//                showErrorMessage(Constants.MessageType.PLAY_SERVICES_UNAVAILABLE);
            }
        }
    }

    @OnActivityResult(Constants.REQUEST_CODE_ACTIVITY_AUTOCOMPLETE_PLACE)
    protected void onDeliveryPlaceSelected(int resultCode, Intent data) {
//        hideKeyboard();
        if (resultCode == RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(this, data);
            mPresenter.placeSelected(place.getAddress().toString());
//            presenter.setStartPoint(new LocationPoint(place));
        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Toast.makeText(this, "Error while select address. Try again", Toast.LENGTH_SHORT).show();
//            showErrorMessage(Constants.MessageType.ERROR_WHILE_SELECT_ADDRESS);
        }
    }

    @Override
    public void returnPlace(String _selectedPlace) {
        Intent intent = new Intent();
        intent.putExtra(Constants.KEY_PLACE_RESULT, _selectedPlace);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void setContactAdapterList(List<DeliveryPlaceDH> _list) {
        mDeliveryPlaceAdapter.setListDH(_list);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void closeScreen() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
