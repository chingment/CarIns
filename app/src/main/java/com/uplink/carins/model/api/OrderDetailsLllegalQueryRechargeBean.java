package com.uplink.carins.model.api;

/**
 * Created by chingment on 2018/3/30.
 */

public class OrderDetailsLllegalQueryRechargeBean {

    private int id;
    private String sn;
    private int status;
    private String statusName;
    private String submitTime;
    private PrintDataBean printData;

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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public PrintDataBean getPrintData() {
        return printData;
    }

    public void setPrintData(PrintDataBean printData) {
        this.printData = printData;
    }

    private String completeTime;
    private String cancleTime;
    private String payTime;
    private String price;
    private int score;
    private String remarks;
}
