package com.uplink.carins.model.api;

import java.io.Serializable;

/**
 * Created by chingment on 2018/1/24.
 */

public class LoginResultBean implements Serializable {
    private int userId;
    private String userName;
    private int merchantId;
    private String merchantCode;
    private boolean isTestAccount;
    private int posMachineId;
    private int status;
    private OrderInfoBean  orderInfo;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public Boolean getIsTestAccount() {
        return isTestAccount;
    }

    public void setIsTestAccount(Boolean isTestAccount) {
        this.isTestAccount = isTestAccount;
    }

    public int getPosMachineId() {
        return posMachineId;
    }

    public void setPosMachineId(int posMachineId) {
        this.posMachineId = posMachineId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public OrderInfoBean getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfoBean orderInfo) {
        this.orderInfo = orderInfo;
    }

}
