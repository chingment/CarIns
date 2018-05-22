package com.uplink.carins.model.api;

import java.io.Serializable;

/**
 * Created by chingment on 2018/1/29.
 */

public class PayResultQueryResultBean implements Serializable {
    private String orderSn;
    private int orderType;
    private int status;
    private String remarks;
    private PrintDataBean printData;


    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public void setPrintData(PrintDataBean expiryTime) {
        this.printData = printData;
    }
}
