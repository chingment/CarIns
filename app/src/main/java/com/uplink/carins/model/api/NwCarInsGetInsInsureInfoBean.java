package com.uplink.carins.model.api;

import java.io.Serializable;

/**
 * Created by chingment on 2018/6/24.
 */

public class NwCarInsGetInsInsureInfoBean implements Serializable {

    public NwCarInsCompanyBean getOfferInfo() {
        return offerInfo;
    }

    public void setOfferInfo(NwCarInsCompanyBean offerInfo) {
        this.offerInfo = offerInfo;
    }

    public NwCarInsInsureResult getInsureInfo() {
        return insureInfo;
    }

    public void setInsureInfo(NwCarInsInsureResult insureInfo) {
        this.insureInfo = insureInfo;
    }

    private NwCarInsCompanyBean offerInfo;
    private NwCarInsInsureResult insureInfo;
}
