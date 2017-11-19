package com.ferenc.pamp.presentation.utils;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.ferenc.pamp.R;

/**
 * Created by
 * Ferenc on 2017.11.09..
 */

public abstract class Constants {

    public static final int CLICK_DELAY = 600;

    /*REQUEST CODES*/
    public static final int REQUEST_CODE_GOOGLE = 0;

    /*GOOGLE CODES*/
    public static final String GOOGLE_CLIENT_ID = "773378553548-46hfgulvfai96cd7aiba7v7c2ii7g1rm.apps.googleusercontent.com";


    public enum MessageType {
        CONNECTION_PROBLEMS(R.string.err_msg_connection_problem, true),
        USER_NOT_REGISTERED(R.string.err_msg_user_not_registered, true),
        BAD_CREDENTIALS(R.string.err_msg_bad_credentials, true),
        INVALID_EMAIL_OR_SOME_FIELDS_EMPTY(R.string.err_msg_invalid_email_or_empty_fields, true),
        EMPTY_FIELDS_OR_NOT_MATCHES_PASSWORDS(R.string.err_msg_invalid_email_or_empty_fields, true),
        UNKNOWN(R.string.err_msg_something_goes_wrong, true),
        PLAY_SERVICES_UNAVAILABLE(R.string.err_msg_play_services_unavailable, true),
        ERROR_WHILE_SELECT_ADDRESS(R.string.err_msg_select_address_fail, true),
        REQUEST_SENT(R.string.err_msg_request_is_sent, false),
        SELECT_START_POINT(R.string.err_msg_select_start_point, true),
        SELECT_DESTINATION(R.string.err_msg_select_destination, true),
        SELECT_ROUTE(R.string.err_msg_select_route, true),
        SPECIFY_DEPARTURE_TIME(R.string.err_msg_specify_departure_time, true),
        SPECIFY_RIDE_FLEXIBILITY(R.string.err_msg_specify_ride_flexibility, true),
        SELECT_RATING(R.string.err_msg_set_rating, true),
        TOKEN_EXPIRED(R.string.err_msg_token_expired, true),
        SELECT_FUTURE_DATE(R.string.err_msg_select_date_in_future, true);

        @StringRes
        private int messageRes;
        private boolean isDangerous;

        MessageType(@StringRes int messageRes, boolean isDangerous) {
            this.messageRes = messageRes;
            this.isDangerous = isDangerous;
        }

        public int getMessageRes() {
            return messageRes;
        }

        public boolean isDangerous() {
            return isDangerous;
        }
    }

    public enum PlaceholderType {
        NETWORK(R.string.err_msg_connection_problem, R.drawable.ic_cloud_off),
        UNKNOWN(R.string.err_msg_something_goes_wrong, R.drawable.common_google_signin_btn_icon_dark),
        EMPTY(R.string.err_msg_no_data, R.drawable.ic_no_data),
        NO_REQUESTS(R.string.err_msg_no_requests, R.drawable.common_google_signin_btn_icon_dark);

        @StringRes
        private int messageRes;
        @DrawableRes
        private int iconRes;

        PlaceholderType(@StringRes int messageRes, @DrawableRes int iconRes) {
            this.messageRes = messageRes;
            this.iconRes = iconRes;
        }

        public int getMessageRes() {
            return messageRes;
        }

        public int getIconRes() {
            return iconRes;
        }
    }
}
