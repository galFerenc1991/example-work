package com.ferenc.pamp.presentation.screens.main.propose.delivery.delivery_date;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.ferenc.pamp.R;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.DateManager;
import com.ferenc.pamp.presentation.utils.GoodDealManager;
import com.jakewharton.rxbinding2.view.RxView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by
 * Ferenc on 2017.11.29..
 */
@EActivity(R.layout.activity_delivery_date)
public class DeliveryDateActivity extends AppCompatActivity implements DeliveryDateContract.View {

    private DeliveryDateContract.Presenter mPresenter;

    @ViewById(R.id.ivBack_ADD)
    protected ImageView btnBack;
    @ViewById(R.id.rlStartDate_ADD)
    protected RelativeLayout rlStartDate;
    @ViewById(R.id.tvStartDate_ADD)
    protected TextView tvStartDate;
    @ViewById(R.id.rlEndDate_ADD)
    protected RelativeLayout rlEndDate;
    @ViewById(R.id.tvEndDate_ADD)
    protected TextView tvEndDate;
    @ViewById(R.id.btnValidate_ADD)
    protected Button btnValidate;

    @Bean
    protected GoodDealManager mGoodDealManager;

    @AfterInject
    @Override
    public void initPresenter() {
        new DeliveryDatePresenter(this, mGoodDealManager);
    }

    @AfterViews
    protected void initUI() {
        RxView.clicks(btnBack)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedBack());

        RxView.clicks(rlStartDate)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedStartDate());

        RxView.clicks(rlEndDate)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedEndDate());

        RxView.clicks(btnValidate)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedValidate());
    }

    @Override
    public void setPresenter(DeliveryDateContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void selectStartDate(Calendar _startDate) {
        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(Locale.FRANCE);

        Calendar result = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.DialogTheme, (view, hourOfDay, minute) -> {
            result.set(Calendar.HOUR_OF_DAY, hourOfDay);
            result.set(Calendar.MINUTE, minute);
            if (DateManager.isBeforeNow(result)) {
                Toast.makeText(this, R.string.err_msg_select_date_in_future, Toast.LENGTH_SHORT).show();
//                showErrorMessage(Constants.MessageType.SELECT_FUTURE_DATE);
            } else {
                mPresenter.setStartDate(result);
            }
        }, _startDate.get(Calendar.HOUR_OF_DAY), _startDate.get(Calendar.MINUTE), true);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, (view, year, month, dayOfMonth) -> {
            result.set(year, month, dayOfMonth);
            timePickerDialog.show();
        }, _startDate.get(Calendar.YEAR), _startDate.get(Calendar.MONTH), _startDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 315569260000L);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.setTitle(R.string.start_delivery_date_dialog_title);
        datePickerDialog.show();
    }

    @Override
    public void selectEndDate(Calendar _endDate) {
        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(Locale.FRANCE);

        Calendar result = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.DialogTheme, (view, hourOfDay, minute) -> {
            result.set(Calendar.HOUR_OF_DAY, hourOfDay);
            result.set(Calendar.MINUTE, minute);
            if (DateManager.isBeforeNow(result)) {
                Toast.makeText(this, R.string.err_msg_select_date_in_future, Toast.LENGTH_SHORT).show();
//                showErrorMessage(Constants.MessageType.SELECT_FUTURE_DATE);
            } else {
                mPresenter.setEndDate(result);
            }
        }, _endDate.get(Calendar.HOUR_OF_DAY), _endDate.get(Calendar.MINUTE), true);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, (view, year, month, dayOfMonth) -> {
            result.set(year, month, dayOfMonth);
            timePickerDialog.show();
        }, _endDate.get(Calendar.YEAR), _endDate.get(Calendar.MONTH), _endDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 315569260000L);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.setTitle(R.string.end_delivery_date_dialog_title);
        datePickerDialog.show();
    }

    @Override
    public void setStartDate(String _startDate) {
        tvStartDate.setText(_startDate);
    }

    @Override
    public void setEndDate(String _endDate) {
        tvEndDate.setText(_endDate);
    }

    @Override
    public void setDeliveryDate(String _startDate, String _endDate) {
        Intent intent = new Intent();
        intent.putExtra(Constants.KEY_START_DATE_RESULT, _startDate);
        intent.putExtra(Constants.KEY_END_DATE_RESULT, _endDate);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void closeScreen() {
        finish();
    }
}
