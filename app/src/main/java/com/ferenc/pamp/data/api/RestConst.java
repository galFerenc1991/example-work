package com.ferenc.pamp.data.api;

/**
 * Created by
 * Ferenc on 2017.11.16..
 */

public abstract class RestConst {

    public static final String BASE_URL = "https://pamp-qa.herokuapp.com/";

    public static final String HEADER_AUTH = "authorization";
    public static final String HEADER_ACCEPT = "Accept";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_VALUE_HTML = "text/html";
    public static final String HEADER_VALUE_JSON = "application/json";

    public static final long TIMEOUT = 30; //seconds
    public static final long TIMEOUT_READ = 30; //seconds
    public static final long TIMEOUT_WRITE = 60; //seconds

    public static final int ITEMS_PER_PAGE = 25;

}
