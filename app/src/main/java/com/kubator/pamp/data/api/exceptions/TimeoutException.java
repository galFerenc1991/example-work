package com.kubator.pamp.data.api.exceptions;

/**
 * Created by
 * Ferenc on 2017.11.16..
 */

public class TimeoutException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Server doesn't respond";
    }
}
