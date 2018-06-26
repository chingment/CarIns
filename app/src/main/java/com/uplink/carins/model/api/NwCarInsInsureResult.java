package com.uplink.carins.model.api;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chingment on 2018/6/21.
 */

public class NwCarInsInsureResult implements Serializable {

    private int insureResult;
    private NwReceiptAddressBean receiptAddress;
    private List<NwItemParentFieldBean> infoItems;

    public int getInsureResult() {
        return insureResult;
    }

    public void setInsureResult(int insureResult) {
        this.insureResult = insureResult;
    }



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


}
