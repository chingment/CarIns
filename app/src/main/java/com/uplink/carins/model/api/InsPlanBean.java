package com.uplink.carins.model.api;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chingment on 2018/6/2.
 */

public class InsPlanBean implements Serializable {
    private String bannerImgUrl ;
    private int companyId;
    private String companyName;
    private int productId;
    private String productName;
    private List<InsPlanProductSkuBean> productSkus;

    public String getBannerImgUrl() {
        return bannerImgUrl;
    }

    public void setBannerImgUrl(String bannerImgUrl) {
        this.bannerImgUrl = bannerImgUrl;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<InsPlanProductSkuBean> getProductSkus() {
        return productSkus;
    }

    public void setProductSkus(List<InsPlanProductSkuBean> productSkus) {
        this.productSkus = productSkus;
    }
}
