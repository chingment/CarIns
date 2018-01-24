package com.uplink.carins.model.api;

/**
 * Created by chingment on 2018/1/24.
 */

public class GetCreateAccountCodeResultBean {

    private String phone;
    private String validCode;
    private String token;
    private int seconds;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getValidCode() {
        return validCode;
    }

    public void setValidCode(String validCode) {
        this.validCode = validCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
