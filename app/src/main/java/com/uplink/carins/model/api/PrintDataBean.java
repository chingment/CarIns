package com.uplink.carins.model.api;

import android.content.ClipData;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chingment on 2018/3/2.
 */

public class PrintDataBean implements Serializable {

    private  String merchantName;
    private  String merchantCode;
    private  String productName;
    private  String orderSn;
    private  String tradeType;

    public List<ItemFieldBean> getExtField() {
        return extField;
    }

    public void setExtField(List<ItemFieldBean> extField) {
        this.extField = extField;
    }

    private  String tradeNo;
    private  String tradeDateTime;
    private  String tradePayMethod;
    private  String tradeAmount;
    private  String serviceHotline;
    private  List<ItemFieldBean> extField;
    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getTradeDateTime() {
        return tradeDateTime;
    }

    public void setTradeDateTime(String tradeDateTime) {
        this.tradeDateTime = tradeDateTime;
    }

    public String getTradePayMethod() {
        return tradePayMethod;
    }

    public void setTradePayMethod(String tradePayMethod) {
        this.tradePayMethod = tradePayMethod;
    }

    public String getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(String tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public String getServiceHotline() {
        return serviceHotline;
    }

    public void setServiceHotline(String serviceHotline) {
        this.serviceHotline = serviceHotline;
    }


}
