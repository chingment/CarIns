package com.uplink.carins.model.api;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chingment on 2018/3/25.
 */

public class LllegalQueryResultBean implements Serializable {


    private List<LllegalPriceRecordBean> lllegalPriceRecord;

    public void setLllegalPriceRecord(List<LllegalPriceRecordBean> lllegalPriceRecord) {
        this.lllegalPriceRecord = lllegalPriceRecord;
    }

    public List<LllegalPriceRecordBean> getLllegalPriceRecord() {
        return lllegalPriceRecord;
    }
}
