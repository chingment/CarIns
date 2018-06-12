package com.uplink.carins.model.api;


import java.io.Serializable;

/**
 * Created by chingment on 2018/5/9.
 */

public class NwCarInsChannelBean implements Serializable {
    private  int companyId;
    private String companyImg;
    private String code;
    private String descp;
    private String inquiry;
    private String message;
    private String name;
    private String opType;
    private String remote;
    private String sort;
    private int channelId;

    private int offerResult;
    private float offerPremium;
    private String offerMsg;
    private NwCarInsInsInquiryResultBean offerData;

    public NwCarInsInsInquiryResultBean getOfferData() {
        return offerData;
    }

    public void setOfferData(NwCarInsInsInquiryResultBean offerData) {
        this.offerData = offerData;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getCompanyImg() {
        return companyImg;
    }

    public void setCompanyImg(String companyImg) {
        this.companyImg = companyImg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }

    public String getInquiry() {
        return inquiry;
    }

    public void setInquiry(String inquiry) {
        this.inquiry = inquiry;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public String getRemote() {
        return remote;
    }

    public void setRemote(String remote) {
        this.remote = remote;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getOfferResult() {
        return offerResult;
    }

    public void setOfferResult(int offerResult) {
        this.offerResult = offerResult;
    }

    public float getOfferPremium() {
        return offerPremium;
    }

    public void setOfferPremium(float offerPremium) {
        this.offerPremium = offerPremium;
    }

    public String getOfferMsg() {
        return offerMsg;
    }

    public void setOfferMsg(String offerMsg) {
        this.offerMsg = offerMsg;
    }

}
