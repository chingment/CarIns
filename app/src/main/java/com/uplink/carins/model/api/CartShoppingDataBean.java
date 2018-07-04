package com.uplink.carins.model.api;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chingment on 2018/7/4.
 */

public class CartShoppingDataBean implements Serializable {

    private int count;
    private float sumPrice;
    private int countBySelected;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(float sumPrice) {
        this.sumPrice = sumPrice;
    }

    public int getCountBySelected() {
        return countBySelected;
    }

    public void setCountBySelected(int countBySelected) {
        this.countBySelected = countBySelected;
    }

    public float getSumPriceBySelected() {
        return sumPriceBySelected;
    }

    public void setSumPriceBySelected(float sumPriceBySelected) {
        this.sumPriceBySelected = sumPriceBySelected;
    }

    public List<CartProductSkuBean> getSkus() {
        return skus;
    }

    public void setSkus(List<CartProductSkuBean> skus) {
        this.skus = skus;
    }

    private float sumPriceBySelected;
    private List<CartProductSkuBean> skus;
}
