package com.uplink.carins.model.api;

import java.io.Serializable;

/**
 * Created by chingment on 2018/7/5.
 */

public class CartProductSkuByOpreateBean implements Serializable {

    private int cartId;
    private int skuId;

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private int quantity;

    public CartProductSkuByOpreateBean() {

    }

    public CartProductSkuByOpreateBean(int cartId, int skuId, int quantity) {

        this.cartId = cartId;
        this.skuId = skuId;
        this.quantity = quantity;
    }
}
