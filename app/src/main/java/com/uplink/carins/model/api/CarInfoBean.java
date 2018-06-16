package com.uplink.carins.model.api;

import java.io.Serializable;

/**
 * Created by chingment on 2018/5/7.
 */

public class CarInfoBean implements Serializable {

    private String belong;
    private String carType;
    private String licensePlateNo;
    private String vin;
    private String engineNo;
    private String firstRegisterDate;
    private String modelCode;
    private String modelName;
    private String displacement;
    private String marketYear;
    private String ratedPassengerCapacity;
    private String replacementValue;
    private String chgownerType;
    private String chgownerDate;
    private String tonnage;
    private String wholeWeight;
    private String licensePicKey;
    private String licensePicUrl;
    private String licenseOtherPicKey;
    private String licenseOtherPicUrl;

    public String getLicensePicUrl() {
        return licensePicUrl;
    }

    public void setLicensePicUrl(String licensePicUrl) {
        this.licensePicUrl = licensePicUrl;
    }

    public String getLicenseOtherPicUrl() {
        return licenseOtherPicUrl;
    }

    public void setLicenseOtherPicUrl(String licenseOtherPicUrl) {
        this.licenseOtherPicUrl = licenseOtherPicUrl;
    }

    public String getCarCertPicUrl() {
        return carCertPicUrl;
    }

    public void setCarCertPicUrl(String carCertPicUrl) {
        this.carCertPicUrl = carCertPicUrl;
    }

    public String getCarInvoicePicUrl() {
        return carInvoicePicUrl;
    }

    public void setCarInvoicePicUrl(String carInvoicePicUrl) {
        this.carInvoicePicUrl = carInvoicePicUrl;
    }

    private String carCertPicKey;
    private String carCertPicUrl;
    private String carInvoicePicKey;
    private String carInvoicePicUrl;

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getLicensePlateNo() {
        return licensePlateNo;
    }

    public void setLicensePlateNo(String licensePlateNo) {
        this.licensePlateNo = licensePlateNo;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public String getFirstRegisterDate() {
        return firstRegisterDate;
    }

    public void setFirstRegisterDate(String firstRegisterDate) {
        this.firstRegisterDate = firstRegisterDate;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getDisplacement() {
        return displacement;
    }

    public void setDisplacement(String displacement) {
        this.displacement = displacement;
    }

    public String getMarketYear() {
        return marketYear;
    }

    public void setMarketYear(String marketYear) {
        this.marketYear = marketYear;
    }

    public String getRatedPassengerCapacity() {
        return ratedPassengerCapacity;
    }

    public void setRatedPassengerCapacity(String ratedPassengerCapacity) {
        this.ratedPassengerCapacity = ratedPassengerCapacity;
    }

    public String getReplacementValue() {
        return replacementValue;
    }

    public void setReplacementValue(String replacementValue) {
        this.replacementValue = replacementValue;
    }

    public String getChgownerType() {
        return chgownerType;
    }

    public void setChgownerType(String chgownerType) {
        this.chgownerType = chgownerType;
    }

    public String getChgownerDate() {
        return chgownerDate;
    }

    public void setChgownerDate(String chgownerDate) {
        this.chgownerDate = chgownerDate;
    }

    public String getTonnage() {
        return tonnage;
    }

    public void setTonnage(String tonnage) {
        this.tonnage = tonnage;
    }

    public String getWholeWeight() {
        return wholeWeight;
    }

    public void setWholeWeight(String wholeWeight) {
        this.wholeWeight = wholeWeight;
    }

    public String getLicensePicKey() {
        return licensePicKey;
    }

    public void setLicensePicKey(String licensePicKey) {
        this.licensePicKey = licensePicKey;
    }

    public String getLicenseOtherPicKey() {
        return licenseOtherPicKey;
    }

    public void setLicenseOtherPicKey(String licenseOtherPicKey) {
        this.licenseOtherPicKey = licenseOtherPicKey;
    }

    public String getCarCertPicKey() {
        return carCertPicKey;
    }

    public void setCarCertPicKey(String carCertPicKey) {
        this.carCertPicKey = carCertPicKey;
    }

    public String getCarInvoicePicKey() {
        return carInvoicePicKey;
    }

    public void setCarInvoicePicKey(String carInvoicePicKey) {
        this.carInvoicePicKey = carInvoicePicKey;
    }
}
