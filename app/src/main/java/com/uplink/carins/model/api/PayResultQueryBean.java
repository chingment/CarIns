package com.uplink.carins.model.api;

import java.io.Serializable;

/**
 * Created by chingment on 2018/1/29.
 */

public class PayResultQueryBean implements Serializable {
    private String orderSn;
    private int status;
    private String remarks;

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
