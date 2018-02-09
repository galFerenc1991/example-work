package com.ferenc.pamp.presentation.screens.main.profile.edit_profile;

import com.ferenc.pamp.data.model.common.User;
import com.ferenc.pamp.presentation.screens.main.profile.UserRelay;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.SignedUserManager;
import com.ferenc.pamp.presentation.utils.ToastManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by
 * Ferenc on 2017.12.05..
 */

public class EditProfilePresenter implements EditProfileContract.Presenter {

    private EditProfileContract.View mView;
    private EditProfileContract.Model mModel;
    private SignedUserManager mUserManager;
    private UserRelay mUserRelay;
    private CompositeDisposable mCompositeDisposable;
    private Calendar mBirthDate;
    private String mCountry;
    private File mAvatarFile;


    public EditProfilePresenter(EditProfileContract.View _view
            , EditProfileContract.Model _userRepository
            , SignedUserManager _userManager
            , UserRelay _userRelay) {
        this.mView = _view;
        this.mModel = _userRepository;
        this.mUserManager = _userManager;
        this.mUserRelay = _userRelay;
        this.mCompositeDisposable = new CompositeDisposable();

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        User signedUser = mUserManager.getCurrentUser();
        if (signedUser != null) {
            mView.setUserAvatar(signedUser.getAvatar());
            mView.setUserName(signedUser.getFirstName());
            mView.setUserSurName(signedUser.getLastName());
            mView.setUserEmail(signedUser.getEmail());
//            mView.setUserCountry(signedUser.getCountry());
            if (signedUser.getBankAccount() != null) {
                mView.setIban(signedUser.getBankAccount().getCountry() + " ***** " + signedUser.getBankAccount().getLast4());
            }
            if (signedUser.getCard() != null) {
                mView.setCardNumber(" **** " + signedUser.getCard().getLast4(), signedUser.getCard().getBrand());
            }
            if (signedUser.isSocial())
                mView.hideChangePasswordField();
        }
    }

    @Override
    public void clickedChangePassword() {
        mView.openChangePasswordScreen();
    }

    @Override
    public void clickedBankCard() {
        mView.openAddBankCardScreen();
    }

    @Override
    public void clickedIban() {
        mView.openCreateBankAccountScreen();
    }

    @Override
    public void selectAvatar() {
        mView.openImagePicker();
    }

    @Override
    public void saveAvatar(File _avatar) {
        mAvatarFile = _avatar;
        mView.setUserAvatar(_avatar);

    }

    @Override
    public void clickedBirthDate() {
        mView.openDatePicker(mBirthDate == null ? Calendar.getInstance() : mBirthDate);
    }

    @Override
    public void setBirthDay(Calendar _calendar) {
        mBirthDate = _calendar;
        mView.setUserBirthDay(getCloseDateInString(_calendar));
    }

    @Override
    public void clickedCountry() {
        mView.openCountryPicker(mCountry);
    }

    @Override
    public void setSelectedCountry(String _country) {
        mCountry = _country;
        mView.setUserCountry(_country);
    }

    @Override
    public void clickedAddress() {
        mView.openLocationPicker();
    }

    @Override
    public void setSelectedAddress(String _address) {
        mView.setUserAddress(_address);
    }


    private String getCloseDateInString(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM 'at' yyyy", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    @Override
    public void clickedSave(String _firstName, String _lastName, String _country) {

        mCompositeDisposable.add(mModel.updateUser(
                getUpdatedBody(_firstName, _lastName, _country),
                getUpdatedAvatar(mAvatarFile))
                .subscribe(user -> {
                    mUserRelay.userRelay.accept(user);
                    mView.finishActivity();
                }, throwable -> ToastManager.showToast(throwable.getMessage()))
        );

    }


    private MultipartBody.Part getUpdatedAvatar(File _avatarFile) {
        return _avatarFile != null
                ? MultipartBody.Part.createFormData(
                Constants.UPDATE_AVATAR_KEY,
                _avatarFile.getName(),
                RequestBody.create(MediaType.parse(Constants.MEDIA_TYPE_IMG), _avatarFile))
                : null;
    }

    private Map<String, RequestBody> getUpdatedBody(String _firstName, String _lastName, String _country) {

        HashMap<String, RequestBody> updateRequest = new HashMap<>();

        updateRequest.put(Constants.UPDATE_FIRST_NAME_KEY, createTextRequestBody(_firstName));
        updateRequest.put(Constants.UPDATE_LAST_NAME_KEY, createTextRequestBody(_lastName));
        updateRequest.put(Constants.UPDATE_COUNTRY_KEY, createTextRequestBody(_country));

        return updateRequest;
    }

    private RequestBody createTextRequestBody(String _data) {
        return RequestBody.create(MediaType.parse(Constants.MEDIA_TYPE_TEXT), _data);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
