package com.ferenc.pamp.presentation.screens.main.profile.edit_profile.change_password;

import android.icu.text.UnicodeSetSpanner;

import com.ferenc.pamp.data.api.exceptions.ConnectionLostException;
import com.ferenc.pamp.data.model.common.ChangePasswordRequest;
import com.ferenc.pamp.presentation.utils.Constants;
import com.ferenc.pamp.presentation.utils.ToastManager;
import com.ferenc.pamp.presentation.utils.ValidationManager;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.HttpException;

/**
 * Created by
 * Ferenc on 2018.01.10..
 */

public class ChangePasswordPresenter implements ChangePasswordContract.Presenter {

    private ChangePasswordContract.View mView;
    private ChangePasswordContract.Model mModel;
    private CompositeDisposable mCompositeDisposable;

    public ChangePasswordPresenter(ChangePasswordContract.View _view, ChangePasswordContract.Model mModel) {
        this.mView = _view;
        this.mModel = mModel;
        this.mCompositeDisposable = new CompositeDisposable();

        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void setNewPassword(String oldPass, String newPass, String confNewPass) {
        int errCodeOldPass = ValidationManager.validatePassword(oldPass);
        int errCodeNewPass = ValidationManager.validatePassword(newPass);
        int errCodePasswordMatches = ValidationManager.isPasswordsMatches(newPass, confNewPass);

        switch (errCodeOldPass) {
            case ValidationManager.EMPTY:
                mView.showErrorOldPasswordRequired();
                break;
            case ValidationManager.INVALID:
                mView.showErrorOldPasswordInvalid();
                break;
            case ValidationManager.OK:
                mView.hideNewPasswordError();
                break;
        }

        switch (errCodeNewPass) {
            case ValidationManager.EMPTY:
                mView.showErrorNewPasswordRequired();
                break;
            case ValidationManager.INVALID:
                mView.showErrorNewPasswordInvalid();
                break;
            case ValidationManager.OK:
                mView.hideNewPasswordError();
                break;
        }
        switch (errCodePasswordMatches) {
            case ValidationManager.NOT_MATCHES:
                mView.showErrorPassNotMatch();
                break;
            case ValidationManager.OK:
                mView.hideConfPasswordError();
                break;
        }

        if (errCodeOldPass == ValidationManager.OK &&
                errCodeNewPass == ValidationManager.OK
                && errCodePasswordMatches == ValidationManager.OK) {
            mView.showProgressMain();
            mCompositeDisposable.add(mModel.changePassword(new ChangePasswordRequest(oldPass, newPass, confNewPass))
                    .subscribe(generalMessageResponse -> {
                        mView.hideProgress();
                        ToastManager.showToast(generalMessageResponse.getMessage());
                        mView.closeChangePasswordScreen();
                    }, throwable -> {
                        mView.hideProgress();
                        ToastManager.showToast("ChangePassword Error");
//                        if (throwable instanceof ConnectionLostException) {
//                            mView.showErrorMessage(Constants.MessageType.CONNECTION_PROBLEMS);
//                        } else if (throwable instanceof HttpException) {
//                            int errorCode = ((HttpException) throwable).response().code();
//                            switch (errorCode) {
//                                case 422:
////                                    mView.showErrorEnteredOldPasswordIncorrect();
//                            }
//                        } else {
//                            mView.showErrorMessage(Constants.MessageType.UNKNOWN);
//
//                        }
                    }));
        }
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
