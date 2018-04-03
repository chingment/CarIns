package com.uplink.carins.model.api;

import java.io.Serializable;

/**
 * Created by chingment on 2018/4/2.
 */

public class GetPayTranSnResultBean implements Serializable {

    private int orderId;
    private String orderSn;
    private String payTransSn;
    private int transType;

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    private long amount;

    public int getTransType() {
        return transType;
    }

    public void setTransType(int transType) {
        this.transType = transType;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getPayTransSn() {
        return payTransSn;
    }

    public void setPayTransSn(String payTransSn) {
        this.payTransSn = payTransSn;
    }
}
