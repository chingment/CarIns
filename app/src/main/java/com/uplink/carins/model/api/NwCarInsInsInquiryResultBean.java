package com.uplink.carins.model.api;


import java.io.Serializable;
import java.util.List;

/**
 * Created by chingment on 2018/5/14.
 */

public class NwCarInsInsInquiryResultBean implements Serializable {
    private NwCarInsChannelBean channel;
    private int offerId;

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    private float sumPremium;

    public NwCarInsChannelBean getChannel() {
        return channel;
    }

    public void setChannel(NwCarInsChannelBean channel) {
        this.channel = channel;
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
