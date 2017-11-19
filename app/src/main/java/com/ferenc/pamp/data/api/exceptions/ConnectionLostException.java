package com.ferenc.pamp.data.api.exceptions;

/**
 * Created by
 * Ferenc on 2017.11.16..
 */

public class ConnectionLostException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Internet connection lost";
    }
}
