package com.uplink.carins.model.api;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chingment on 2018/6/13.
 */

public class ProductSkuBean implements Serializable {
    private String skuId;
    private String name;
    private String mainImg;
    private float unitPrice;
    private float showPirce;
    private String briefIntro;

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMainImg() {
        return mainImg;
    }

    public void setMainImg(String mainImg) {
        this.mainImg = mainImg;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public float getShowPirce() {
        return showPirce;
    }

    public void setShowPirce(float showPirce) {
        this.showPirce = showPirce;
    }

    public String getBriefIntro() {
        return briefIntro;
    }

    public void setBriefIntro(String briefIntro) {
        this.briefIntro = briefIntro;
    }

    public boolean isHot() {
        return isHot;
    }

    public void setHot(boolean hot) {
        isHot = hot;
    }

    public String getDetailsDesc() {
        return detailsDesc;
    }

    public void setDetailsDesc(String detailsDesc) {
        this.detailsDesc = detailsDesc;
    }

    public List<ImgSetBean> getDisplayImgs() {
        return displayImgs;
    }

    public void setDisplayImgs(List<ImgSetBean> displayImgs) {
        this.displayImgs = displayImgs;
    }

    public String getServiceDesc() {
        return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    private boolean isHot;
    private String detailsDesc;
    private List<ImgSetBean> displayImgs;
    private String serviceDesc;
    private String specs;

}
