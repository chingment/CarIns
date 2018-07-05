package com.uplink.carins.model.api;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chingment on 2018/7/5.
 */

public class CartComfirmOrderData implements Serializable {

    private List<CartProductSkuBean> skus;
    private RecipientAddressBean recipientAddress;
    private String actualAmount;

    public RecipientAddressBean getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(RecipientAddressBean recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public List<CartProductSkuBean> getSkus() {
        return skus;
    }

    public void setSkus(List<CartProductSkuBean> skus) {
        this.skus = skus;
    }

    public String getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(String actualAmount) {
        this.actualAmount = actualAmount;
    }
}
