package com.uplink.carins.model.api;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chingment on 2018/6/15.
 */

public class NwCarInsConfirmPayInfoBean implements Serializable {

    private NwReceiptAddressBean receiptAddress;

    public NwReceiptAddressBean getReceiptAddress() {
        return receiptAddress;
    }

    public void setReceiptAddress(NwReceiptAddressBean receiptAddress) {
        this.receiptAddress = receiptAddress;
    }

    public List<NwItemParentFieldBean> getInfoItems() {
        return infoItems;
    }

    public void setInfoItems(List<NwItemParentFieldBean> infoItems) {
        this.infoItems = infoItems;
    }

    private List<NwItemParentFieldBean> infoItems;
}
