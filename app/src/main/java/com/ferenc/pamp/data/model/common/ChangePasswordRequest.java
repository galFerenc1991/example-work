package com.ferenc.pamp.data.model.common;

import com.google.gson.annotations.SerializedName;

/**
 * Created by
 * Ferenc on 2018.01.10..
 */

public class ChangePasswordRequest {
    public String oldPassword;
    public String newPassword;
    public String confirmNewPassword;

    public ChangePasswordRequest(String oldPassword, String newPassword, String newPasswordConfirmation) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmNewPassword = newPasswordConfirmation;
    }
}