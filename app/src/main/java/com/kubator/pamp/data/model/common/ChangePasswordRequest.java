package com.kubator.pamp.data.model.common;

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