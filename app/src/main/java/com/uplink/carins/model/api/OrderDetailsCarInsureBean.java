package com.uplink.carins.model.api;

import java.util.List;

/**
 * Created by chingment on 2018/1/17.
 */

public class OrderDetailsCarInsureBean {
    private int id;
    private String sn;
    private int status;
    private String statusName;
    private String submitTime;
    private String completeTime;
    private String cancleTime;
    private String payTime;
    private String carOwner;
    private String carPlateNo;
    private String carOwnerIdNumber;
    private int insuranceCompanyId;
    private String insuranceCompanyName;
    private String insureImgUrl;
    private String commercialPrice;
    private String travelTaxPrice;
    private String compulsoryPrice;
    private String price;
    private String recipient;
    private String recipientPhoneNumber;
    private String recipientAddress;
    private String remarks;
    private List<OfferCompanyBean> offerCompany;
   // private List<OfferKindBean> offerKind;
    private List<ZjBean> zj;
    private List<String> recipientAddressList;
    private int followStatus;

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

    public String getCarOwner() {
        return carOwner;
    }

    public void setCarOwner(String carOwner) {
        this.carOwner = carOwner;
    }

    public String getCarPlateNo() {
        return carPlateNo;
    }

    public void setCarPlateNo(String carPlateNo) {
        this.carPlateNo = carPlateNo;
    }

    public String getCarOwnerIdNumber() {
        return carOwnerIdNumber;
    }

    public void setCarOwnerIdNumber(String carOwnerIdNumber) {
        this.carOwnerIdNumber = carOwnerIdNumber;
    }

    public int getInsuranceCompanyId() {
        return insuranceCompanyId;
    }

    public void setInsuranceCompanyId(int insuranceCompanyId) {
        this.insuranceCompanyId = insuranceCompanyId;
    }

    public String getInsuranceCompanyName() {
        return insuranceCompanyName;
    }

    public void setInsuranceCompanyName(String insuranceCompanyName) {
        this.insuranceCompanyName = insuranceCompanyName;
    }

    public String getInsureImgUrl() {
        return insureImgUrl;
    }

    public void setInsureImgUrl(String insureImgUrl) {
        this.insureImgUrl = insureImgUrl;
    }

    public String getCommercialPrice() {
        return commercialPrice;
    }

    public void setCommercialPrice(String commercialPrice) {
        this.commercialPrice = commercialPrice;
    }

    public String getTravelTaxPrice() {
        return travelTaxPrice;
    }

    public void setTravelTaxPrice(String travelTaxPrice) {
        this.travelTaxPrice = travelTaxPrice;
    }



    public String getCompulsoryPrice() {
        return compulsoryPrice;
    }

    public void setCompulsoryPrice(String compulsoryPrice) {
        this.compulsoryPrice = compulsoryPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getRecipientPhoneNumber() {
        return recipientPhoneNumber;
    }

    public void setRecipientPhoneNumber(String recipientPhoneNumber) {
        this.recipientPhoneNumber = recipientPhoneNumber;
    }


    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<OfferCompanyBean> getOfferCompany() {
        return offerCompany;
    }

    public void setOfferCompany(List<OfferCompanyBean> offerCompany) {
        this.offerCompany = offerCompany;
    }

//    public List<OfferKindBean> getOfferKind() {
//        return offerKind;
//    }
//
//    public void setOfferKind(List<OfferKindBean> offerKind) {
//        this.offerKind = offerKind;
//    }

    public List<ZjBean> getZj() {
        return zj;
    }

    public void setZj(List<ZjBean> zj) {
        this.zj = zj;
    }

    public List<String> getRecipientAddressList() {
        return recipientAddressList;
    }

    public void setRecipientAddressList(List<String> recipientAddressList) {
        this.recipientAddressList = recipientAddressList;
    }

    public int getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(int followStatus) {
        this.followStatus = followStatus;
    }
}
