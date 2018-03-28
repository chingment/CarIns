package com.uplink.carins.model.api;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chingment on 2018/3/25.
 */

public class LllegalQueryResultBean implements Serializable {


    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getSumCount() {
        return sumCount;
    }

    public void setSumCount(String sumCount) {
        this.sumCount = sumCount;
    }

    public String getSumPoint() {
        return sumPoint;
    }

    public void setSumPoint(String sumPoint) {
        this.sumPoint = sumPoint;
    }

    public String getSumFine() {
        return sumFine;
    }

    public void setSumFine(String sumFine) {
        this.sumFine = sumFine;
    }

    public List<LllegalPriceRecordBean> getRecord() {
        return record;
    }

    public void setRecord(List<LllegalPriceRecordBean> record) {
        this.record = record;
    }


    public int getQueryScore() {
        return queryScore;
    }

    public void setQueryScore(int queryScore) {
        this.queryScore = queryScore;
    }

    public Boolean getIsOfferPrice() {
        return isOfferPrice;
    }

    public void setIsOfferPrice(Boolean isOfferPrice) {
        this.isOfferPrice = isOfferPrice;
    }

    private String carNo;
    private String sumCount;
    private String sumPoint;
    private String sumFine;
    private List<LllegalPriceRecordBean> record;
    private int queryScore;
    private Boolean isOfferPrice = false;

}
