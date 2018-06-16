package com.uplink.carins.model.api;

import java.io.Serializable;

/**
 * Created by chingment on 2018/5/7.
 */

public class CustomerBean implements Serializable {

    private String name ;
    private String certNo ;
    private String mobile ;
    private String address ;
    private String identityFacePicKey ;
    private String identityFacePicUrl ;
    private String identityBackPicKey ;
    private String identityBackPicUrl ;
    private String orgPicKey ;
    private String orgPicUrl ;
    private String insuredFlag;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdentityFacePicKey() {
        return identityFacePicKey;
    }

    public void setIdentityFacePicKey(String identityFacePicKey) {
        this.identityFacePicKey = identityFacePicKey;
    }

    public String getIdentityBackPicKey() {
        return identityBackPicKey;
    }

    public void setIdentityBackPicKey(String identityBackPicKey) {
        this.identityBackPicKey = identityBackPicKey;
    }

    public String getOrgPicKey() {
        return orgPicKey;
    }

    public void setOrgPicKey(String orgPicKey) {
        this.orgPicKey = orgPicKey;
    }

    public String getInsuredFlag() {
        return insuredFlag;
    }

    public void setInsuredFlag(String insuredFlag) {
        this.insuredFlag = insuredFlag;
    }

    public String getIdentityFacePicUrl() {
        return identityFacePicUrl;
    }

    public void setIdentityFacePicUrl(String identityFacePicUrl) {
        this.identityFacePicUrl = identityFacePicUrl;
    }

    public String getIdentityBackPicUrl() {
        return identityBackPicUrl;
    }

    public void setIdentityBackPicUrl(String identityBackPicUrl) {
        this.identityBackPicUrl = identityBackPicUrl;
    }

    public String getOrgPicUrl() {
        return orgPicUrl;
    }

    public void setOrgPicUrl(String orgPicUrl) {
        this.orgPicUrl = orgPicUrl;
    }
}
