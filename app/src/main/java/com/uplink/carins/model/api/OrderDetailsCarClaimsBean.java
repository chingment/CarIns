package com.uplink.carins.model.api;

import com.uplink.carins.utils.StringUtil;

/**
 * Created by chingment on 2018/1/19.
 */

public class OrderDetailsCarClaimsBean {

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
    private String repairsType;
    private String handPerson;
    private String handPersonPhone;
    private String remarks;
    private int followStatus;
    private HandMerchantBean handMerchant;

    private String accessoriesPrice;
    private String workingHoursPrice;
    private String estimatePrice;
    private String price;
    private String estimateListImgUrl;


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

    public String getRepairsType() {
        return repairsType;
    }

    public void setRepairsType(String repairsType) {
        this.repairsType = repairsType;
    }

    public String getHandPerson() {
        return handPerson;
    }

    public void setHandPerson(String handPerson) {
        this.handPerson = handPerson;
    }

    public String getHandPersonPhone() {
        return handPersonPhone;
    }

    public void setHandPersonPhone(String handPersonPhone) {
        this.handPersonPhone = handPersonPhone;
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

    public HandMerchantBean getHandMerchant() {
        return handMerchant;
    }

    public void setHandMerchant(HandMerchantBean handMerchant) {
        this.handMerchant = handMerchant;
    }

    public String getAccessoriesPrice() {
        return accessoriesPrice;
    }

    public void setAccessoriesPrice(String accessoriesPrice) {
        this.accessoriesPrice = accessoriesPrice;
    }

    public String getWorkingHoursPrice() {
        return workingHoursPrice;
    }

    public void setWorkingHoursPrice(String workingHoursPrice) {
        this.workingHoursPrice = workingHoursPrice;
    }

    public String getEstimatePrice() {
        return estimatePrice;
    }

    public void setEstimatePrice(String estimatePrice) {
        this.estimatePrice = estimatePrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getEstimateListImgUrl() {
        return estimateListImgUrl;
    }

    public void setEstimateListImgUrl(String estimateListImgUrl) {
        this.estimateListImgUrl = estimateListImgUrl;
    }
}
