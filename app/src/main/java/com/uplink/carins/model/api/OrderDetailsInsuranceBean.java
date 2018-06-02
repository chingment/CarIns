package com.uplink.carins.model.api;

import java.util.List;

/**
 * Created by chingment on 2018/4/10.
 */

public class OrderDetailsInsuranceBean {

    private int id;
    private String sn;
    private int status;
    private String statusName;
    private String submitTime;
    private String payTime;
    private String completeTime;
    private String cancleTime;

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

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getInsCompanyName() {
        return insCompanyName;
    }

    public void setInsCompanyName(String insCompanyName) {
        this.insCompanyName = insCompanyName;
    }

    public String getProductSkuName() {
        return productSkuName;
    }

    public void setProductSkuName(String productSkuName) {
        this.productSkuName = productSkuName;
    }

    private String remarks;

    public List<ZjBean> getCredentialsImgs() {
        return credentialsImgs;
    }

    public void setCredentialsImgs(List<ZjBean> credentialsImgs) {
        this.credentialsImgs = credentialsImgs;
    }

    public List<ItemFieldBean> getProductSkuAttrItems() {
        return productSkuAttrItems;
    }

    public void setProductSkuAttrItems(List<ItemFieldBean> productSkuAttrItems) {
        this.productSkuAttrItems = productSkuAttrItems;
    }

    private String insCompanyName;
    private String productSkuName;
    private List<ZjBean> credentialsImgs;
    private List<ItemFieldBean> productSkuAttrItems;
}
