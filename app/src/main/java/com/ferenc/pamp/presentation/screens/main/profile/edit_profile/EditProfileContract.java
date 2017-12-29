package com.ferenc.pamp.presentation.screens.main.profile.edit_profile;

import com.ferenc.pamp.data.model.common.User;
import com.ferenc.pamp.presentation.base.BaseModel;
import com.ferenc.pamp.presentation.base.BasePresenter;
import com.ferenc.pamp.presentation.base.BaseView;


import java.io.File;
import java.util.Calendar;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * Created by
 * Ferenc on 2017.12.05..
 */

public interface EditProfileContract {
    interface View extends BaseView<Presenter> {
        void openImagePicker();

        void openDatePicker(Calendar _calendar);

        void openCountryPicker(String _country);

        void openLocationPicker();

        void setUserAvatar(String _patch);
        void setUserAvatar(File _avatarFile);

        void setUserName(String _name);

        void setUserSurName(String _surName);

        void setUserEmail(String _userEmail);

        void setUserBirthDay(String _birthDay);

        void setUserCountry(String _country);

        void setUserAddress(String _address);

        void setIban(String _iban);

        void setCardNumber(String _bankCardNumber);

        boolean isCameraPermissionNotGranted();

        void checkCameraPermission();

        void finishActivity();
    }

    interface Presenter extends BasePresenter {
        void selectAvatar();

        void saveAvatar(File _avatar);

        void clickedBirthDate();

        void clickedCountry();

        void clickedAddress();

        void clickedSave(String _firstName, String _lastName, String _country);

        void setBirthDay(Calendar _calendar);

        void setSelectedCountry(String _country);

        void setSelectedAddress(String _address);

    }

    interface Model extends BaseModel {
        Observable<User> updateUser(Map<String, RequestBody> userBody, MultipartBody.Part avatar);
    }
}
