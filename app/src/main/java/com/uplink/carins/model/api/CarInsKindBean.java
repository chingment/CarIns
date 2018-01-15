package com.uplink.carins.model.api;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chingment on 2018/1/15.
 */

public class CarInsKindBean {

    private int id;
    private String name;
    private String aliasName;
    private String value;
    private boolean isCheck = true;// 默认投保
    private boolean canWaiverDeductible;
    private int type;
    private int inputType;
    private String inputUnit;
    private CarInsPlanKindChildBean.InputValueBean inputValue;
    private boolean isHasDetails;


    public boolean getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        isCheck = isCheck;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public boolean getCanWaiverDeductible() {
        return canWaiverDeductible;
    }

    public void setCanWaiverDeductible(boolean canWaiverDeductible) {
        this.canWaiverDeductible = canWaiverDeductible;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getInputType() {
        return inputType;
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
    }

    public String getInputUnit() {
        return inputUnit;
    }

    public void setInputUnit(String inputUnit) {
        this.inputUnit = inputUnit;
    }

    public CarInsPlanKindChildBean.InputValueBean getInputValue() {
        return inputValue;
    }

    public void setInputValue(CarInsPlanKindChildBean.InputValueBean inputValue) {
        this.inputValue = inputValue;
    }

    public boolean getIsHasDetails() {
        return isHasDetails;
    }

    public void setIsHasDetails(boolean isHasDetails) {
        this.isHasDetails = isHasDetails;
    }

    public static class InputValueBean implements Serializable {
        /**
         * default : 10w
         * value : ["5w","10w","15w","20w","30w","50w","100w","150w","200w以上"]
         */


        private String defaultVal;
        private List<String> value;

        public String getDefaultVal() {
            return defaultVal;
        }

        public void setDefaultVal(String defaultVal) {
            this.defaultVal = defaultVal;
        }

        public List<String> getValue() {
            return value;
        }

        public void setValue(List<String> value) {
            this.value = value;
        }
    }


    public CarInsKindBean() {

    }




    public CarInsKindBean(int id, String name, String aliasName, String value, boolean isCheck, boolean canWaiverDeductible, int type, int inputType, String inputUnit, CarInsPlanKindChildBean.InputValueBean inputValue, boolean isHasDetails) {
        this.id = id;
        this.name=name;
        this.aliasName=aliasName;
        this.value=value;
        this.isCheck=isCheck;
        this.canWaiverDeductible=canWaiverDeductible;
        this.type=type;
        this.inputType=inputType;
        this.inputUnit=inputUnit;
        this.inputValue=inputValue;
        this.isHasDetails=isHasDetails;

    }
}
