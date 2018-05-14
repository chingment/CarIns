package com.uplink.carins.model.api;


import java.io.Serializable;
import java.util.List;

/**
 * Created by chingment on 2018/5/14.
 */

public class NwCarInsInsInquiryResultBean implements Serializable {
    private NwCarInsChannelBean channel;
    private String inquirySeq;
    private String orderSeq;
    private float sumPremium;

    public NwCarInsChannelBean getChannel() {
        return channel;
    }

    public void setChannel(NwCarInsChannelBean channel) {
        this.channel = channel;
    }

    public String getInquirySeq() {
        return inquirySeq;
    }

    public void setInquirySeq(String inquirySeq) {
        this.inquirySeq = inquirySeq;
    }

    public String getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(String orderSeq) {
        this.orderSeq = orderSeq;
    }

    public float getSumPremium() {
        return sumPremium;
    }

    public void setSumPremium(float sumPremium) {
        this.sumPremium = sumPremium;
    }

    public List<NwItemParentFieldBean> getInsureItem() {
        return insureItem;
    }

    public void setInsureItem(List<NwItemParentFieldBean> insureItem) {
        this.insureItem = insureItem;
    }

    private List<NwItemParentFieldBean>  insureItem;
}
