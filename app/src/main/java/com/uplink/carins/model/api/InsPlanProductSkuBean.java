package com.uplink.carins.model.api;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chingment on 2018/6/2.
 */

public class InsPlanProductSkuBean implements Serializable {
    private int productSkuId ;
    private String productSkuName;
    private String price;

    public int getProductSkuId() {
        return productSkuId;
    }

    public void setProductSkuId(int productSkuId) {
        this.productSkuId = productSkuId;
    }

    public String getProductSkuName() {
        return productSkuName;
    }

    public void setProductSkuName(String productSkuName) {
        this.productSkuName = productSkuName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<ItemFieldBean> getAttrItems() {
        return attrItems;
    }

    public void setAttrItems(List<ItemFieldBean> attrItems) {
        this.attrItems = attrItems;
    }

    private List<ItemFieldBean> attrItems;
}
