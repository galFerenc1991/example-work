package com.ferenc.pamp.data.api;

/**
 * Created by
 * Ferenc on 2017.11.16..
 */

public abstract class RestConst {

    public static final String BASE_URL = "http://45.77.55.49:3031";
    // : "http://45.77.55.49:3031/"

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
