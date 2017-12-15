package com.ferenc.pamp.presentation.screens.main.profile.edit_profile;

import com.ferenc.pamp.data.model.common.User;
import com.ferenc.pamp.data.model.common.UserUpdateRequest;
import com.ferenc.pamp.presentation.utils.SignedUserManager;
import com.ferenc.pamp.presentation.utils.ToastManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by
 * Ferenc on 2017.12.05..
 */

public class EditProfilePresenter implements EditProfileContract.Presenter {

    private EditProfileContract.View mView;
    private EditProfileContract.Model mModel;
    private SignedUserManager mUserManager;
    private CompositeDisposable mCompositeDisposable;
    private Calendar mBirthDate;
    private String mCountry;
    private File mAvatarFile;

    public EditProfilePresenter(EditProfileContract.View _view
            , EditProfileContract.Model _userRepository
            , SignedUserManager _userManager) {
        this.mView = _view;
        this.mModel = _userRepository;
        this.mUserManager = _userManager;
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
            mView.setUserCountry(signedUser.getCountry());
        }
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
        mCompositeDisposable.add(mModel.updateUser(new UserUpdateRequest(_firstName, _lastName, _country, mAvatarFile))
                .subscribe(user -> {
                    ToastManager.showToast("User updated");
                }, throwable -> {
                    ToastManager.showToast(throwable.getMessage());
                }));
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
