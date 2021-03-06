package com.uplink.carins.model.api;


import java.io.Serializable;
import java.util.List;

/**
 * Created by chingment on 2018/5/9.
 */

public class NwCarInsCompanyBean implements Serializable {

    private String auto ;
    private  int id;
    private String imgUrl;
    private String name;
    private String descp;
    private String partnerCode;
    private int partnerChannelId;
    private int offerId;
    private String offerSumPremium;

    public String getAuto() {
        return auto;
    }

    public void setAuto(String auto) {
        this.auto = auto;
    }

    //private int offerResult;
    private int offerStatus;

    public int getOfferStatus() {
        return offerStatus;
    }

    public void setOfferStatus(int offerStatus) {
        this.offerStatus = offerStatus;
    }

    private List<NwItemParentFieldBean>  offerInquirys;
    private String offerMessage;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public int getPartnerChannelId() {
        return partnerChannelId;
    }

    public void setPartnerChannelId(int partnerChannelId) {
        this.partnerChannelId = partnerChannelId;
    }

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public String getOfferSumPremium() {
        return offerSumPremium;
    }

    public void setOfferSumPremium(String offerSumPremium) {
        this.offerSumPremium = offerSumPremium;
    }

//    public int getOfferResult() {
//        return offerResult;
//    }
//
//    public void setOfferResult(int offerResult) {
//        this.offerResult = offerResult;
//    }

    public List<NwItemParentFieldBean> getOfferInquirys() {
        return offerInquirys;
    }

    public void setOfferInquirys(List<NwItemParentFieldBean> offerInquirys) {
        this.offerInquirys = offerInquirys;
    }

    public String getOfferMessage() {
        return offerMessage;
    }

    public void setOfferMessage(String offerMessage) {
        this.offerMessage = offerMessage;
    }

}
