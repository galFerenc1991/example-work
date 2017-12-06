package com.ferenc.pamp.presentation.screens.main.propose.delivery;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.content.ContentFragment;
import com.ferenc.pamp.presentation.screens.main.propose.delivery.delivery_date.DeliveryDateActivity_;
import com.ferenc.pamp.presentation.screens.main.propose.delivery.delivery_place.DeliveryPlaceActivity_;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.DateManager;
import com.ferenc.pamp.presentation.utils.GoodDealManager;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by
 * Ferenc on 2017.11.22..
 */
@EFragment
public class DeliveryFragment extends ContentFragment implements DeliveryContract.View {
    @Override
    public void setPresenter(DeliveryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_delivery;
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    private DeliveryContract.Presenter mPresenter;

    @ViewById(R.id.tvCloseDate_FDel)
    protected TextView tvCloseDate;
    @ViewById(R.id.tvDeliveryPlace_FDel)
    protected TextView tvDeliveryPlace;
    @ViewById(R.id.tvDeliveryDate_FDel)
    protected TextView tvDeliveryDate;

    @Bean
    protected GoodDealManager mGoodDealManager;

    @AfterInject
    @Override
    public void initPresenter() {
        new DeliveryPresenter(this, mGoodDealManager);
    }

    @AfterViews
    protected void initUI() {
        RxView.clicks(tvCloseDate)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedCloseDate());

        RxView.clicks(tvDeliveryPlace)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.
                        clickedDeliveryPlace());

        RxView.clicks(tvDeliveryDate)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedDeliveryDate());
    }

    @Override
    public void openDatePicker(Calendar _calendar) {

        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(Locale.FRANCE);

        Calendar result = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(mActivity, R.style.DialogTheme, (view, hourOfDay, minute) -> {
            result.set(Calendar.HOUR_OF_DAY, hourOfDay);
            result.set(Calendar.MINUTE, minute);
            if (DateManager.isBeforeNow(result)) {
                showErrorMessage(Constants.MessageType.SELECT_FUTURE_DATE);
            } else {
                mPresenter.setCloseDate(result);
            }
        }, _calendar.get(Calendar.HOUR_OF_DAY), _calendar.get(Calendar.MINUTE), true);

        DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity, R.style.DialogTheme, (view, year, month, dayOfMonth) -> {
            result.set(year, month, dayOfMonth);
            timePickerDialog.show();
        }, _calendar.get(Calendar.YEAR), _calendar.get(Calendar.MONTH), _calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 315569260000L);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.setTitle(R.string.close_date_dialog_title);
        datePickerDialog.show();
    }

    @Override
    public void openLocationScreen() {
        DeliveryPlaceActivity_.intent(this)
                .startForResult(Constants.REQUEST_CODE_ACTIVITY_DELIVERY_PLACE);
    }

    @OnActivityResult(Constants.REQUEST_CODE_ACTIVITY_DELIVERY_PLACE)
    void resultPlace(int resultCode, @OnActivityResult.Extra(value = Constants.KEY_PLACE_RESULT) String _place) {
        if (resultCode == Activity.RESULT_OK) {
            mPresenter.setDeliveryPlace(_place);
        }
    }

    @Override
    public void openDateScreen() {
        DeliveryDateActivity_.intent(this)
                .startForResult(Constants.REQUEST_CODE_ACTIVITY_DELIVERY_DATE);
    }

    @OnActivityResult(Constants.REQUEST_CODE_ACTIVITY_DELIVERY_DATE)
    void resultDate(int resultCode, @OnActivityResult.Extra(value = Constants.KEY_START_DATE_RESULT) String _startDate
            , @OnActivityResult.Extra(value = Constants.KEY_END_DATE_RESULT) String _endDate) {
        if (resultCode == Activity.RESULT_OK) {
            mPresenter.setDeliveryDate(_startDate, _endDate);
        }
    }

    @Override
    public void setCloseDate(String _closeDate) {
        tvCloseDate.setText(_closeDate);
    }

    @Override
    public void setDeliveryDate(String _deliveryDate) {
        tvDeliveryDate.setText(_deliveryDate);
    }

    @Override
    public void setDeliveryPlace(String _deliveryPlace) {
        tvDeliveryPlace.setText(_deliveryPlace);
    }

}
