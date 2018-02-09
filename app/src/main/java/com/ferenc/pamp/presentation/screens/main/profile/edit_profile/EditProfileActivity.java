package com.ferenc.pamp.presentation.screens.main.profile.edit_profile;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ferenc.pamp.PampApp_;
import com.ferenc.pamp.R;
import com.ferenc.pamp.data.api.RestConst;
import com.ferenc.pamp.domain.UserRepository;
import com.ferenc.pamp.presentation.base.BaseActivity;
import com.ferenc.pamp.presentation.custom.bank_account.BankAccountActivity_;
import com.ferenc.pamp.presentation.screens.auth.sign_up.country_picker.CountryPickerActivity_;
import com.ferenc.pamp.presentation.screens.main.chat.create_order.payment.add_card.AddCardFragment_;
import com.ferenc.pamp.presentation.screens.main.profile.UserRelay;
import com.ferenc.pamp.presentation.screens.main.profile.edit_profile.bank_card.AddBankCardActivity_;
import com.ferenc.pamp.presentation.screens.main.profile.edit_profile.change_password.ChangePasswordActivity_;
import com.ferenc.pamp.presentation.screens.main.propose.delivery.delivery_place.DeliveryPlaceActivity_;
import com.ferenc.pamp.presentation.utils.AvatarManager;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.CreditCardImageManager;
import com.ferenc.pamp.presentation.utils.RoundedTransformation;
import com.ferenc.pamp.presentation.utils.SignedUserManager;
import com.ferenc.pamp.presentation.utils.ToastManager;
import com.jakewharton.rxbinding2.view.RxView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by
 * Ferenc on 2017.12.05..
 */

@EActivity(R.layout.activity_edit_profile)
public class EditProfileActivity extends BaseActivity implements EditProfileContract.View {
    @Override
    public void setPresenter(EditProfileContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getContainer() {
        return R.id.flContainer_AEP;
    }

    @Override
    protected Toolbar getToolbar() {
        return mToolBar;
    }

    private EditProfileContract.Presenter mPresenter;

    @ViewById(R.id.toolbar_AEP)
    protected Toolbar mToolBar;
    @ViewById(R.id.btnSave_AEP)
    protected Button btnSave;

    @ViewById(R.id.ivProfilePicture_AEP)
    protected ImageView ivProfilePicture;

    @ViewById(R.id.tilName_AEP)
    protected TextInputLayout tilName;
    @ViewById(R.id.etName_AEP)
    protected EditText etName;
    @ViewById(R.id.tilSurname_AEP)
    protected TextInputLayout tilSurname;
    @ViewById(R.id.etSurname_AEP)
    protected EditText etSurname;
    @ViewById(R.id.llBirthDate_AEP)
    protected LinearLayout llBirthDate;
    @ViewById(R.id.tvBirthDate_AEP)
    protected TextView tvBirthDate;
    @ViewById(R.id.tvEmail_AEP)
    protected TextView tvEmail;
    @ViewById(R.id.llCountrySpinner_AEP)
    protected LinearLayout llCountrySpinner;
    @ViewById(R.id.tvCountry_AEP)
    protected TextView tvCountry;
    @ViewById(R.id.llAddress_AEP)
    protected LinearLayout llAddress;
    @ViewById(R.id.tvAddress_AEP)
    protected TextView tvAddress;
    @ViewById(R.id.llIBAN_AEP)
    protected LinearLayout llIBAN;
    @ViewById(R.id.tvIBAN_AEP)
    protected TextView tvIBAN;
    @ViewById(R.id.llBankCard_AEP)
    protected LinearLayout llBankCard;
    @ViewById(R.id.tvBankCard_AEP)
    protected TextView tvBankCard;
    @ViewById(R.id.llChangePassword_AEP)
    protected LinearLayout llChangePassword;
    @ViewById(R.id.tvChangePassword_AEP)
    protected TextView tvChangePassword;


    @StringRes(R.string.title_edit_profile)
    protected String mTitle;

    @Bean
    protected SignedUserManager mUserManager;
    @Bean
    protected UserRepository mUserRepository;
    @Bean
    protected AvatarManager avatarManager;
    @Bean
    protected UserRelay mUserRelay;

    @AfterInject
    @Override
    public void initPresenter() {
        new EditProfilePresenter(this, mUserRepository, mUserManager, mUserRelay);
        avatarManager.attach(this);
    }

    @AfterViews
    protected void initUI() {
        initToolBar();
        setRxListeners();
        mPresenter.subscribe();
    }

    private void initToolBar() {
        getToolbarManager().setTitle(mTitle);
        getToolbarManager().showHomeAsUp(true);
        getToolbarManager().closeActivityWhenBackArrowPressed(this);
    }

    private void setRxListeners() {
        RxView.clicks(ivProfilePicture)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.selectAvatar());

        RxView.clicks(llBirthDate)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedBirthDate());
        RxView.clicks(llCountrySpinner)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedCountry());
        RxView.clicks(llAddress)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedAddress());
        RxView.clicks(btnSave)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedSave(etName.getText().toString(), etSurname.getText().toString(), tvCountry.getText().toString()));

        RxView.clicks(llBankCard)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedBankCard());

        RxView.clicks(llIBAN)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedIban());

        RxView.clicks(llChangePassword)
                .throttleFirst(Constants.CLICK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(o -> mPresenter.clickedChangePassword());
    }

    @Override
    public void openAddBankCardScreen() {
        AddBankCardActivity_.intent(this).startForResult(Constants.REQUEST_CODE_ACTIVITY_BANK_CARD);
    }

    @OnActivityResult(Constants.REQUEST_CODE_ACTIVITY_BANK_CARD)
    protected void bankCardCreated(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            mPresenter.subscribe();
        }
    }

    @Override
    public void openCreateBankAccountScreen() {
        BankAccountActivity_.intent(this).startForResult(Constants.REQUEST_CODE_ACTIVITY_BANK_ACCOUNT);
    }

    @OnActivityResult(Constants.REQUEST_CODE_ACTIVITY_BANK_ACCOUNT)
    protected void bankAccountCreated(int resultCode) {
        if (resultCode == RESULT_OK) {
            mPresenter.subscribe();
        }
    }

    @Override
    public void openChangePasswordScreen() {
        ChangePasswordActivity_.intent(this).start();
    }

    @Override
    public void openImagePicker() {
        checkCameraPermission();
    }

    @OnActivityResult(Constants.REQUEST_CODE_GET_IMAGE)
    protected void handleImage(int resultCode, Intent data) {
        if (resultCode == RESULT_OK)
            avatarManager.handleFullsizeImage(Constants.REQUEST_CODE_CROP_IMAGE, resultCode, data, false);
    }


    @OnActivityResult(Constants.REQUEST_CODE_CROP_IMAGE)
    protected void cropImage(int resultCode) {
        if (resultCode == RESULT_OK) {
            mPresenter.saveAvatar(avatarManager.getCroppedFile());
        }
    }

    @Override
    public void openDatePicker(Calendar _calendar) {
        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(Locale.FRANCE);

        Calendar result = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, (view, year, month, dayOfMonth) -> {
            result.set(year, month, dayOfMonth);
            mPresenter.setBirthDay(result);
        }, _calendar.get(Calendar.YEAR), _calendar.get(Calendar.MONTH), _calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 315569260000L * 7);
        datePickerDialog.setTitle(R.string.birth_date_dialog_title);
        datePickerDialog.show();
    }

    @Override
    public void openCountryPicker(String _country) {
        CountryPickerActivity_.intent(this)
                .extra(Constants.KEY_COUNTRY, _country)
                .startForResult(Constants.REQUEST_CODE_COUNTRY_PICKER);
    }

    @OnActivityResult(Constants.REQUEST_CODE_COUNTRY_PICKER)
    protected void resultCountryCode(@OnActivityResult.Extra(Constants.KEY_COUNTRY) String country) {
        mPresenter.setSelectedCountry(country);
    }

    @Override
    public void openLocationPicker() {
        DeliveryPlaceActivity_.intent(this)
                .startForResult(Constants.REQUEST_CODE_ACTIVITY_DELIVERY_PLACE);
    }

    @OnActivityResult(Constants.REQUEST_CODE_ACTIVITY_DELIVERY_PLACE)
    void resultPlace(int resultCode, @OnActivityResult.Extra(value = Constants.KEY_PLACE_RESULT) String _place) {
        if (resultCode == Activity.RESULT_OK) {
            mPresenter.setSelectedAddress(_place);
        }
    }

    @Override
    public void setUserAvatar(String _patch) {
        Picasso.with(PampApp_.getInstance())
                .load(RestConst.BASE_URL + "/" + _patch)
                .placeholder(R.drawable.ic_add_photo)
                .error(R.drawable.ic_add_photo)
                .transform(new RoundedTransformation(200, 0))
                .fit()
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .centerCrop()
                .into(ivProfilePicture);
    }

    @Override
    public void setUserAvatar(File _avatarFile) {
        Picasso.with(PampApp_.getInstance())
                .load(_avatarFile)
                .placeholder(R.drawable.ic_add_photo)
                .error(R.drawable.ic_add_photo)
                .transform(new RoundedTransformation(200, 0))
                .fit()
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .centerCrop()
                .into(ivProfilePicture);
    }

    @Override
    public void setUserName(String _name) {
        etName.setText(_name);
    }

    @Override
    public void setUserSurName(String _surName) {
        etSurname.setText(_surName);
    }

    @Override
    public void setUserEmail(String _userEmail) {
        tvEmail.setText(_userEmail);
    }

    @Override
    public void setUserBirthDay(String _birthDay) {
        tvBirthDate.setText(_birthDay);
    }

    @Override
    public void setUserCountry(String _country) {
        tvCountry.setText(_country);
    }

    @Override
    public void setUserAddress(String _address) {
        tvAddress.setText(_address);
    }

    @Override
    public void setIban(String _iban) {
        tvIBAN.setText(_iban);
    }

    @Override
    public void setCardNumber(String _bankCardNumber, String _brand) {
        tvBankCard.setText(_bankCardNumber);
        tvBankCard.setCompoundDrawablesWithIntrinsicBounds(CreditCardImageManager.getBrandImage(_brand), 0, 0, 0);
    }

    @Override
    public boolean isCameraPermissionNotGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void checkCameraPermission() {
        if (isCameraPermissionNotGranted()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, Constants.REQUEST_CODE_CAMERA);
        } else {
            avatarManager.getImage(Constants.REQUEST_CODE_GET_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.REQUEST_CODE_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                avatarManager.getImage(Constants.REQUEST_CODE_GET_IMAGE);
            } else {
                ToastManager.showToast("Please, allow for PAMP access to Camera.");
            }
        }
    }

    @Override
    public void hideChangePasswordField() {
        llChangePassword.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finishActivity() {
        finish();
    }
}
