package com.uplink.carins.model.api;

import com.uplink.carins.ui.crop.util.PaintUtil;

import java.io.Serializable;

/**
 * Created by chingment on 2018/3/25.
 */

public class LllegalQueryLogBean implements Serializable {

    private  String carNo;
    private  String carType;
    private  String rackNo;
    private  String enginNo;
    private  String isCompany;


    public  LllegalQueryLogBean()
    {

    }

    public  LllegalQueryLogBean(String carNo)
    {
        this.carNo=carNo;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getRackNo() {
        return rackNo;
    }

    public void setRackNo(String rackNo) {
        this.rackNo = rackNo;
    }

    public String getEnginNo() {
        return enginNo;
    }

    public void setEnginNo(String enginNo) {
        this.enginNo = enginNo;
    }

    public String getIsCompany() {
        return isCompany;
    }

    public void setIsCompany(String isCompany) {
        this.isCompany = isCompany;
    }



}
