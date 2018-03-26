package com.uplink.carins.model.api;

import java.io.Serializable;

/**
 * Created by chingment on 2018/3/25.
 */

public class LllegalPriceRecordBean implements Serializable {

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getBookNo() {
        return bookNo;
    }

    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    public String getBookTypeName() {
        return bookTypeName;
    }

    public void setBookTypeName(String bookTypeName) {
        this.bookTypeName = bookTypeName;
    }

    public String getLllegalCode() {
        return lllegalCode;
    }

    public void setLllegalCode(String lllegalCode) {
        this.lllegalCode = lllegalCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getLllegalTime() {
        return lllegalTime;
    }

    public void setLllegalTime(String lllegalTime) {
        this.lllegalTime = lllegalTime;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public String getOfserTypeName() {
        return ofserTypeName;
    }

    public void setOfserTypeName(String ofserTypeName) {
        this.ofserTypeName = ofserTypeName;
    }

    public String getFine() {
        return fine;
    }

    public void setFine(String fine) {
        this.fine = fine;
    }

    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getLate_fees() {
        return late_fees;
    }

    public void setLate_fees(String late_fees) {
        this.late_fees = late_fees;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLllegalDesc() {
        return lllegalDesc;
    }

    public void setLllegalDesc(String lllegalDesc) {
        this.lllegalDesc = lllegalDesc;
    }

    public String getLllegalCity() {
        return lllegalCity;
    }

    public void setLllegalCity(String lllegalCity) {
        this.lllegalCity = lllegalCity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getNeedDealt() {
        return needDealt;
    }

    public void setNeedDealt(Boolean needDealt) {
        this.needDealt = needDealt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getCanDealt() {
        return canDealt;
    }

    public void setCanDealt(Boolean canDealt) {
        this.canDealt = canDealt;
    }

    private  String carNo;
    private  String bookNo;
    private  String bookType;
    private  String bookTypeName;
    private  String lllegalCode;
    private  String cityCode;
    private  String lllegalTime;
    private  String point;
    private  String offerType;
    private  String ofserTypeName;
    private  String fine;
    private  String serviceFee;
    private  String late_fees;
    private  String content;
    private  String lllegalDesc;
    private  String lllegalCity;
    private  String address;
    private  Boolean needDealt;
    private  String status;
    private  Boolean canDealt;

}
