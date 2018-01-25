package com.uplink.carins.model.api;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chingment on 2018/1/24.
 */

public class OrderInfoBean implements Serializable {
    private int orderId;
    private String orderSn;
    private String productName;
    private List<ConfirmFieldBean> confirmField;
    //private List<PayMethodBean> payMethod;


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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<ConfirmFieldBean> getConfirmField() {
        return confirmField;
    }

    public void setConfirmField(List<ConfirmFieldBean> confirmField) {
        this.confirmField = confirmField;
    }

//    public List<PayMethodBean> getPayMethod() {
//        return payMethod;
//    }
//
//    public void setPayMethod(List<PayMethodBean> payMethod) {
//        this.payMethod = payMethod;
//    }
}
