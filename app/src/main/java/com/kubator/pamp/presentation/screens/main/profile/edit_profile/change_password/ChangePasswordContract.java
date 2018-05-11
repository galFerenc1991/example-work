package com.kubator.pamp.presentation.screens.main.profile.edit_profile.change_password;

import com.kubator.pamp.data.model.base.GeneralMessageResponse;
import com.kubator.pamp.data.model.common.ChangePasswordRequest;
import com.kubator.pamp.presentation.base.BaseModel;
import com.kubator.pamp.presentation.base.BasePresenter;
import com.kubator.pamp.presentation.base.BaseView;
import com.kubator.pamp.presentation.base.content.ContentView;

import io.reactivex.Observable;

/**
 * Created by
 * Ferenc on 2018.01.10..
 */

public interface ChangePasswordContract {

    interface View extends ContentView, BaseView<Presenter> {

        void closeChangePasswordScreen();

        void showErrorNewPasswordRequired();

        void showErrorOldPasswordRequired();

        void showErrorEnteredOldPasswordIncorrect();

        void showErrorNewPasswordInvalid();

        void showErrorOldPasswordInvalid();

        void hideNewPasswordError();

        void hideConfPasswordError();

        void showErrorPassNotMatch();

    }

    interface Presenter extends BasePresenter {
        void setNewPassword(String oldPass, String newPass, String confNewPass);

    }

    interface Model extends BaseModel {
        Observable<GeneralMessageResponse> changePassword(ChangePasswordRequest request);

    }
}
