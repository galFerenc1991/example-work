package com.ferenc.pamp.presentation.utils;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * Created by
 * Ferenc on 2017.11.17..
 */

public abstract class ValidationManager {

    public static final int OK = 10;
    public static final int EMPTY = 11;
    public static final int INVALID = 12;
    public static final int NOT_MATCHES = 13;

    private static final Pattern regExPhoneNumber = Pattern.compile("^[0-9]{12,15}$");   //12-15 digits
    private static final Pattern regExLicenceId = Pattern.compile("^([A-Z]{3})(\\040)(\\d{6})$");   //3 upper case letter and 6 digits
//    private static final Pattern regExPassword = Pattern.compile("^(?=\\S*[a-z])(?=\\S*[A-Z])(?=\\S*[^a-zA-Z\\d])\\S{6,24}$");   //6-24, lower, upper, special
    private static final Pattern regExPassword = Pattern.compile("^\\S{6,24}$");   //6-24, lower, upper, special

    public static int validatePhoneNumber(String phoneNumber) {
        if(TextUtils.isEmpty(phoneNumber)) return EMPTY;
        return regExPhoneNumber.matcher(phoneNumber).matches() ? OK : INVALID;
    }

    public static int validatePassword(String password) {
        if(TextUtils.isEmpty(password)) return EMPTY;
        return regExPassword.matcher(password).matches() ? OK : INVALID;
    }

    public static int isPasswordsMatches(String pass1, String pass2) {
        if(TextUtils.isEmpty(pass1) || TextUtils.isEmpty(pass2)) return NOT_MATCHES;
        else return TextUtils.equals(pass1, pass2) ? OK : NOT_MATCHES;
    }

    public static int validateName(String name) {
        String result = name.replaceAll("\\s+$", "");
        return TextUtils.isEmpty(result) ? EMPTY : OK;
    }

    public static  int validateSurname(String surname) {
        String result = surname.replaceAll("\\s+$", "");
        return TextUtils.isEmpty(result) ? EMPTY : OK;
    }

    public static int validateDateOfBirth(String dob) {
        return TextUtils.isEmpty(dob) ? EMPTY : OK;
    }

    public static int validateGender(String gender) {
        return TextUtils.isEmpty(gender) ? EMPTY : OK;
    }

    public static int validateText(String _text) {
        return TextUtils.isEmpty(_text) ? EMPTY : OK;
    }

    public static int validateEmail(String email) {
        String result = email.replaceAll("\\s+$", "");
        return android.util.Patterns.EMAIL_ADDRESS.matcher(result).matches() ? OK : INVALID;
    }

    public static int validateLicenceId(String licenceId) {
        if(TextUtils.isEmpty(licenceId)) return EMPTY;
        return regExLicenceId.matcher(licenceId).matches() ? OK : INVALID;
    }

    public static int validateNumberOfSeats(String number) {
        if(TextUtils.isEmpty(number)) return EMPTY;
        return Integer.valueOf(number) > 0 ? OK : INVALID;
    }

    public static int validateNumber(String _number) {
        if(TextUtils.isEmpty(_number)) return EMPTY;
        return Double.valueOf(_number) > 0 ? OK : INVALID;
    }

    public static int validateDooble(String _number) {
        if(TextUtils.isEmpty(_number)) return EMPTY;
        return Double.valueOf(_number) > 0 ? OK : INVALID;
    }

    public static int validateLongNumber(String _number) {
        if(TextUtils.isEmpty(_number)) return EMPTY;
        return Long.valueOf(_number) > 0 ? OK : INVALID;
    }

    public static int validateColor(String color) {
        return TextUtils.isEmpty(color) ? EMPTY : OK;
    }

    public static boolean isEquals(String _firstVal, String _secondVal) {
        return _firstVal.equals(_secondVal == null ? "" : _secondVal);
    }
}
