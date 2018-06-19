package com.uplink.carins.model.api;

import java.io.Serializable;

/**
 * Created by chingment on 2018/6/19.
 */

public class NwCarInsPayResultBean implements Serializable {
    private int offerId;
    private String payUrl;

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }
}
