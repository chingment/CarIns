package com.uplink.carins.model.api;

import java.io.Serializable;

/**
 * Created by chingment on 2018/5/8.
 */

public class CarInsCarModelInfoBean implements Serializable {

    private String modelCode;
    private String modelName;
    private String displacement;
    private String marketYear;
    private String ratedPassengerCapacity;
    private String replacementValue;

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

    public String getDisplacement() {
        return displacement;
    }

    public void setDisplacement(String displacement) {
        this.displacement = displacement;
    }

    public String getMarketYear() {
        return marketYear;
    }

    public void setMarketYear(String marketYear) {
        this.marketYear = marketYear;
    }

    public String getRatedPassengerCapacity() {
        return ratedPassengerCapacity;
    }

    public void setRatedPassengerCapacity(String ratedPassengerCapacity) {
        this.ratedPassengerCapacity = ratedPassengerCapacity;
    }

    public String getReplacementValue() {
        return replacementValue;
    }

    public void setReplacementValue(String replacementValue) {
        this.replacementValue = replacementValue;
    }
}
