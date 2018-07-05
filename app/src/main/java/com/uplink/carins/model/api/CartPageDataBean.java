package com.uplink.carins.model.api;

import java.io.Serializable;

/**
 * Created by chingment on 2018/7/5.
 */


public class CartPageDataBean implements Serializable {

    private CartShoppingDataBean shoppingData;

    public CartShoppingDataBean getShoppingData() {
        return shoppingData;
    }

    public void setShoppingData(CartShoppingDataBean shoppingData) {
        this.shoppingData = shoppingData;
    }
}
