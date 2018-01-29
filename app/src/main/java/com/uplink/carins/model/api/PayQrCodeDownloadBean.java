package com.uplink.carins.model.api;

import java.io.Serializable;

/**
 * Created by chingment on 2018/1/29.
 */

public class PayQrCodeDownloadBean implements Serializable {

    private String orderSn;
    private String mwebUrl;
    private int payWay;

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getMwebUrl() {
        return mwebUrl;
    }

    public void setMwebUrl(String mwebUrl) {
        this.mwebUrl = mwebUrl;
    }

    public int getPayWay() {
        return payWay;
    }

    public void setPayWay(int payWay) {
        this.payWay = payWay;
    }

}
