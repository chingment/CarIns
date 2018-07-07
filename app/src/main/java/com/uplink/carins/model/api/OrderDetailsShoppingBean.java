package com.uplink.carins.model.api;

import java.util.List;

/**
 * Created by chingment on 2018/7/6.
 */

public class OrderDetailsShoppingBean {

    private int id;
    private String sn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    public String getCancleTime() {
        return cancleTime;
    }

    public void setCancleTime(String cancleTime) {
        this.cancleTime = cancleTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(int followStatus) {
        this.followStatus = followStatus;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    private int status;
    private String statusName;
    private String submitTime;
    private String completeTime;
    private String cancleTime;
    private String payTime;

    public PrintDataBean getPrintData() {
        return printData;
    }

    public void setPrintData(PrintDataBean printData) {
        this.printData = printData;
    }

    private String remarks;
    private int followStatus;
    private String price;
    private PrintDataBean printData;

    public RecipientAddressBean getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(RecipientAddressBean recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    private RecipientAddressBean recipientAddress;
    private List<CartProductSkuBean> skus;

    public List<CartProductSkuBean> getSkus() {
        return skus;
    }

    public void setSkus(List<CartProductSkuBean> skus) {
        this.skus = skus;
    }
}
