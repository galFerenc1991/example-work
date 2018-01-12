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
    public static final int CLICK_DELAY_SMALL = 50;


    /*MULTIPART REQUEST CODES*/
    public static final String MEDIA_TYPE_IMG = "image/jpeg";
    public static final String MEDIA_TYPE_TEXT = "text/plain";
    public static final String UPDATE_AVATAR_KEY = "avatar";
    public static final String UPDATE_FIRST_NAME_KEY = "firstName";
    public static final String UPDATE_LAST_NAME_KEY = "lastName";
    public static final String UPDATE_COUNTRY_KEY = "counry";
    public static final String MIME_TYPE = "text/html";
    public static final String DEFAULT_UNCODING = "UTF-8";


    /*REQUEST CODES*/
    public static final int REQUEST_CODE_GOOGLE = 1;
    public static final int REQUEST_CODE_COUNTRY_PICKER = 2;
    public static final int REQUEST_CODE_INPUT_ACTIVITY_NAME = 3;
    public static final int REQUEST_CODE_INPUT_ACTIVITY_DESCRIPTION = 4;
    public static final int REQUEST_CODE_INPUT_ACTIVITY_PRICE = 5;
    public static final int REQUEST_CODE_INPUT_ACTIVITY_PRICE_DESCRIPTION = 6;
    public static final int REQUEST_CODE_INPUT_ACTIVITY_QUANTITY = 7;
    public static final int REQUEST_CODE_ACTIVITY_DELIVERY_DATE = 8;
    public static final int REQUEST_CODE_ACTIVITY_DELIVERY_PLACE = 9;
    public static final int REQUEST_CODE_ACTIVITY_AUTOCOMPLETE_PLACE = 10;
    public static final int REQUEST_CODE_INPUT_ACTIVITY_DESCRIPTION_FROM_RE_BROADCAST_FLOW = 11;
    public static final int REQUEST_CODE_GET_IMAGE = 301;
    public static final int REQUEST_CODE_CROP_IMAGE = 302;
    public static final int REQUEST_CODE_SETTINGS_ACTIVITY = 16;
    public static final int REQUEST_CODE_CREATE_ORDER_POP_UP_ACTIVITY = 17;
    public static final int REQUEST_CODE_ACTIVITY_CARD_NUMBER = 18;
    public static final int REQUEST_CODE_ACTIVITY_EXPIRATION = 19;
    public static final int REQUEST_CODE_ACTIVITY_CARD_CVV = 20;
    public static final int REQUEST_CODE_ACTIVITY_END_FLOW_ACTIVITY = 21;
    public static final int REQUEST_CODE_ACTIVITY_CHOOSE_PRODUCER = 22;
    public static final int REQUEST_CODE_ACTIVITY_NEW_PRODUCER_CREATED = 23;
    public static final int REQUEST_CODE_ACTIVITY_BANK_ACCOUNT = 24;
    public static final int REQUEST_CODE_ACTIVITY_BANK_CARD = 24;



    /*PERMISSIONS REQUEST CODES*/
    public static final int REQUEST_CODE_REED_CONTACTS = 12;
    public static final int REQUEST_CODE_SEND_SMS = 13;
    public static final int REQUEST_CODE_CAMERA = 14;
    public static final int REQUEST_CODE_SEND_SMS_DONE = 15;
    public static final int REQUEST_CODE_STORAGE = 24;

    /*KEYS*/
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_INPUT_INDICATOR = "indicator";
    public static final String KEY_INPUT_RESULT = "indicator";
    public static final String KEY_START_DATE_RESULT = "startDate";
    public static final String KEY_END_DATE_RESULT = "endDate";
    public static final String KEY_PLACE_RESULT = "place";
    public static final String KEY_IS_REBROADCAST = "rebroadcast";
    public static final String KEY_SENDTO_SMS = "smsto: ";
    public static final String KEY_SMS_BODY = "sms_body";
    public static final String KEY_PRODUCT_QUANTITY = "quantity";
    public static final String KEY_PRODUCER_NAME = "producerName";
    public static final String KEY_PRODUCER_ID = "producerId";
    public static final String KEY_PRODUCER_EMAIL = "producerEmail";
    public static final String KEY_BANK_CARD = "creditCard";


    /*GOOD DEAL STATES*/
    public static final String STATE_CANCELED = "canceled";
    public static final String STATE_CLOSED = "closed";
    public static final String STATE_PROGRESS = "progress";
    public static final String STATE_CONFIRM = "confirmed";

    /*ORDER STATUS*/
    public static final String STATUS_CONFIRMED_TEXT = "Confirmé";
    public static final String STATUS_CANCELED_TEXT = "Annulé";
    public static final String STATUS_TO_DELIVER_TEXT = "À LIVRER";
    public static final String STATUS_DELIVER_TEXT = "LIVRER";
    public static final String STATUS_CANCEL_TEXT = "ANNULER";
    public static final String STATUS_IN_PROGRESS_TEXT = "EN COURS";



    /*CREDIT CARDS BRAND*/
    public static final String AMERICAN_EXPRESS = "American Express";
    public static final String DISCOVER = "Discover";
    public static final String JCB = "JCB";
    public static final String DINERS_CLUB = "Diners Club";
    public static final String VISA = "Visa";
    public static final String MASTERCARD = "MasterCard";
    public static final String UNKNOWN = "Unknown";


    /*ACTION KEYS*/
    public static final String KEY_SETTINGS = "settings";
    public static final String KEY_SEND_ORDERS = "sendOrders";
    public static final String KEY_CHANGE_CLOSE_DATE = "closeDate";
    public static final String KEY_CHANGE_DELIVERY_DATE = "deliveryDate";
    public static final String KEY_CANCEL_GOOD_DEAL = "cancelGoodDeal";
//    public static final String KEY_CANCEL = "startDate";

    /*GOOD_PLANS_ITEM_TYPE*/
    public static final int ITEM_TYPE_RE_BROADCAST = 0;
    public static final int ITEM_TYPE_REUSE = 1;

    /*GOOGLE CODES*/
    public static final String GOOGLE_CLIENT_ID = "392469561251-kl0irng19cc3ne0vurejkb6fanu1tv3j.apps.googleusercontent.com";

    /*MESSAGES CODES*/
    public static final String M1_GOOD_DEAL_DIFFUSION = "M1";

    public static final String M2_PRODUCT_ORDERING = "M2";
    public static final String M3_ORDER_CHANGING = "M3";
    public static final String M4_ORDER_CANCELLATION = "M4";

    public static final String M5_GOOD_DEAL_DELIVERY_DATE_CHANGED = "M5";
    public static final String M6_GOOD_DEAL_CLOSING_DATE_CHANGED = "M6";
    public static final String M9_CLOSING_DATE = "M9";
    public static final String M12_DELIVERY_DATE = "M12";

    public static final String M8_GOOD_DEAL_CANCELLATION = "M8";
    public static final String M10_GOOD_DEAL_CLOSING = "M10";

    public static final String M11_1_GOOD_DEAL_CONFIRMATION = "M11_1";
    public static final String M11_2_GOOD_DEAL_CONFIRMATION_APPLYED = "M11_2";
    public static final String M11_3_GOOD_DEAL_CONFIRMATION_REJECTED = "M11_3";

    public static final int DEFAULT_MSG_GROUP_TYPE = 0;
    public static final int M1_MSG_GROUP_TYPE = 1;
    public static final int M2_M3_M4_MSG_GROUP_TYPE = 2;
    public static final int M5_M6_M9_M12_MSG_GROUP_TYPE = 3;
    public static final int M8_M10_MSG_GROUP_TYPE = 4;
    public static final int M11_1_M11_2_M11_3_MSG_GROUP_TYPE = 5;

    /*FLOWS*/
    public static final int CREATE_FLOW = 501;
    public static final int NOT_CREATE_FLOW = 502;
    public static final int ATTACH_BANK_ACCOUNT_FLOW = 503;



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
