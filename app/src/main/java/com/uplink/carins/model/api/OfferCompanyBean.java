package com.uplink.carins.model.api;

/**
 * Created by chingment on 2018/1/17.
 */

public class OfferCompanyBean {

    private int insuranceOfferId;
    private  int insuranceCompanyId;
    private  String insuranceCompanyName;
    private  String insureImgUrl;
    private  float commercialPrice;
    private  float travelTaxPrice;
    private  float compulsoryPrice;
    private  float insureTotalPrice;
    private  String description;
    private  Boolean isCheck;

    public int getInsuranceOfferId() {
        return insuranceOfferId;
    }

    public void setInsuranceOfferId(int insuranceOfferId) {
        this.insuranceOfferId = insuranceOfferId;
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

    public float getCommercialPrice() {
        return commercialPrice;
    }

    public void setCommercialPrice(float commercialPrice) {
        this.commercialPrice = commercialPrice;
    }

    public float getTravelTaxPrice() {
        return travelTaxPrice;
    }

    public void setTravelTaxPrice(float travelTaxPrice) {
        this.travelTaxPrice = travelTaxPrice;
    }

    public float getCompulsoryPrice() {
        return compulsoryPrice;
    }

    public void setCompulsoryPrice(float compulsoryPrice) {
        this.compulsoryPrice = compulsoryPrice;
    }

    public float getInsureTotalPrice() {
        return insureTotalPrice;
    }

    public void setInsureTotalPrice(float insureTotalPrice) {
        this.insureTotalPrice = insureTotalPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(Boolean isCheck) {
        this.isCheck = isCheck;
    }

}
