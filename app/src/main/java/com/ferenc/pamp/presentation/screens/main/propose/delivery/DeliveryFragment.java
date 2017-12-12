package com.ferenc.pamp.presentation.screens.main.propose.delivery;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.content.ContentFragment;
import com.ferenc.pamp.presentation.custom.InputActivity_;
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
import org.androidannotations.annotations.FragmentArg;
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
    @ViewById(R.id.tvProductDescription_FDel)
    protected TextView tvProductDescription;
    @ViewById(R.id.rlCloseDate_FDel)
    protected RelativeLayout rlCloseDate;
    @ViewById(R.id.rlProductDescription_FDel)
    protected RelativeLayout rlProductDescription;
    @ViewById(R.id.rlBackground_FDel)
    protected RelativeLayout rlBackground;
    @Bean
    protected GoodDealManager mGoodDealManager;

    @FragmentArg
    protected boolean isReBroadcastFlow;

    @AfterInject
    @Override
    public void initPresenter() {
        new DeliveryPresenter(this, mGoodDealManager, isReBroadcastFlow);
    }

    @AfterViews
    protected void initUI() {
        if (!isReBroadcastFlow) {
            rlProductDescription.setVisibility(View.GONE);
        } else {
            rlCloseDate.setVisibility(View.GONE);
            rlBackground.setBackgroundColor(getResources().getColor(R.color.color_re_broadcast_tab_indicator));
            tvProductDescription.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_description_yelow, 0, 0, 0);
            tvDeliveryPlace.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location_yelow, 0, 0, 0);
            tvDeliveryDate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_calendar_yelow, 0, 0, 0);
        }

        RxView.clicks(tvCloseDate)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedCloseDate());

        RxView.clicks(tvProductDescription)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedInputField(Constants.REQUEST_CODE_INPUT_ACTIVITY_DESCRIPTION_FROM_RE_BROADCAST_FLOW));

        RxView.clicks(tvDeliveryPlace)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.
                        clickedDeliveryPlace());

        RxView.clicks(tvDeliveryDate)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedDeliveryDate());

        mPresenter.subscribe();
    }

    @Override
    public void openInputActivity(int _requestCode) {
        InputActivity_.intent(this)
                .extra(Constants.KEY_INPUT_INDICATOR, _requestCode)
                .startForResult(_requestCode);
    }

    @OnActivityResult(Constants.REQUEST_CODE_INPUT_ACTIVITY_DESCRIPTION_FROM_RE_BROADCAST_FLOW)
    void resultDescription(int resultCode, @OnActivityResult.Extra(value = Constants.KEY_INPUT_RESULT) String _description) {
        if (resultCode == Activity.RESULT_OK)
            mPresenter.saveProductDescription(_description);
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
    public void setProductDescription(String _nameDescription) {
        tvProductDescription.setText(_nameDescription);
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
