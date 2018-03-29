package com.uplink.carins.model.api;

import java.io.Serializable;

/**
 * Created by chingment on 2018/3/29.
 */

public  class ApiResultByLoginResultBean implements Serializable
{
    private int result;
    private int code;
    private String message;
    private LoginResultBean data;

    public int getResult() {
        return result;
    }
    public void setResult(int result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public LoginResultBean getData() {
        return data;
    }

    public void setData(LoginResultBean data) {
        this.data = data;
    }
}