package com.uplink.carins.model.api;

import com.uplink.carins.ui.crop.util.PaintUtil;

/**
 * Created by chingment on 2018/3/25.
 */

public class LllegalQueryLogBean {

    private  String carNo;
    private  String carType;
    private  String rackNo;
    private  String enginNo;
    private  Boolean isCompany;


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

    public Boolean getIsCompany() {
        return isCompany;
    }

    public void setIsCompany(Boolean isCompany) {
        this.isCompany = isCompany;
    }



}
