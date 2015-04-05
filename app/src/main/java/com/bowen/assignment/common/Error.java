package com.bowen.assignment.common;


/**
 * Created by patrick on 2015-03-26.
 */
public class Error {

    public static final int UN_KNOW_HOST_CODE=1;

    public static final String UN_KNOW_HOST_MESSAGE="un know host";

    public static final String ERROR_MODEL_DOMAIN="model";

    public static final int IO_ERROR_CODE=2;

    public static final String IO_ERROR_MESSAGE="io error";

    public static final int IO_ERROR_CLOSE_CODE=3;

    public static final String IO_ERROR_CLOSE_MESSAGE="io close error";


    private int code;

    private String domain;

    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
