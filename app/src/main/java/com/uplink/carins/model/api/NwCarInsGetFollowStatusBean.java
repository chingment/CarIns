package com.uplink.carins.model.api;

import java.io.Serializable;

/**
 * Created by chingment on 2018/6/27.
 */

public class NwCarInsGetFollowStatusBean implements Serializable {
    public NwCarInsCompanyBean getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(NwCarInsCompanyBean orderInfo) {
        this.orderInfo = orderInfo;
    }

    private int followStatus;
    private NwCarInsCompanyBean orderInfo;

    public String getPartnerOrderId() {
        return partnerOrderId;
    }

    public void setPartnerOrderId(String partnerOrderId) {
        this.partnerOrderId = partnerOrderId;
    }

    private String partnerOrderId;

    public int getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(int followStatus) {
        this.followStatus = followStatus;
    }
}
