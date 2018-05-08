package com.uplink.carins.model.api;

import java.io.Serializable;

/**
 * Created by chingment on 2018/5/8.
 */

public class CarInsGetCarModelInfoPmsBean implements Serializable {

    private String modelCode;
    private String modelName;
    private String vin;

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getFirstRegisterDate() {
        return firstRegisterDate;
    }

    public void setFirstRegisterDate(String firstRegisterDate) {
        this.firstRegisterDate = firstRegisterDate;
    }

    private String firstRegisterDate;
}
