package com.uplink.carins.model.api;

import com.uplink.carins.utils.StringUtil;

import java.util.List;

/**
 * Created by chingment on 2018/3/30.
 */

public class OrderDetailsLllegalDealtBean {
    private int id;
    private String sn;
    private int status;
    private String statusName;
    private String submitTime;
    private String completeTime;
    private String cancleTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    public String getCancleTime() {
        return cancleTime;
    }

    public void setCancleTime(String cancleTime) {
        this.cancleTime = cancleTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSumFine() {
        return sumFine;
    }

    public void setSumFine(String sumFine) {
        this.sumFine = sumFine;
    }

    public String getSumCount() {
        return sumCount;
    }

    public void setSumCount(String sumCount) {
        this.sumCount = sumCount;
    }

    public String getSumServiceFees() {
        return sumServiceFees;
    }

    public void setSumServiceFees(String sumServiceFees) {
        this.sumServiceFees = sumServiceFees;
    }

    public String getSumLateFees() {
        return sumLateFees;
    }

    public void setSumLateFees(String sumLateFees) {
        this.sumLateFees = sumLateFees;
    }

    public String getSumPoint() {
        return sumPoint;
    }

    public void setSumPoint(String sumPoint) {
        this.sumPoint = sumPoint;
    }


    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<LllegalPriceRecordBean> getLllegalRecord() {
        return lllegalRecord;
    }

    public void setLllegalRecord(List<LllegalPriceRecordBean> lllegalRecord) {
        this.lllegalRecord = lllegalRecord;
    }

    private String payTime;
    private String price;
    private String sumFine;
    private String sumCount;
    private String sumServiceFees;
    private String sumLateFees;
    private String sumPoint;
    private String remarks;
    private List<LllegalPriceRecordBean> lllegalRecord;;
}
