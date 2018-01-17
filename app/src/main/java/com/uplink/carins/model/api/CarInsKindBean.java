package com.uplink.carins.model.api;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chingment on 2018/1/15.
 */

public class CarInsKindBean implements Serializable{

    private int id;
    private int pId=0;
    private String name;
    private String aliasName;
    private boolean isCheck = true;// 默认投保
    private boolean canWaiverDeductible;//是否能够选择不计免赔
    private boolean isWaiverDeductible;//是否不计免赔
    private int type;
    private int inputType;
    private String inputUnit;
    private String inputValue;
    private List<String> inputOption;
    private boolean isHasDetails;


    public boolean getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }


    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setPId(int pId) {
        this.pId = pId;
    }

    public int getPId() {
        return pId;
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

    public boolean getIsWaiverDeductible() {
        return isWaiverDeductible;
    }

    public void setIsWaiverDeductible(boolean isWaiverDeductible) {
        this.isWaiverDeductible = isWaiverDeductible;
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

    public List<String> getInputOption() {
        return inputOption;
    }

    public void setInputOption(List<String>  inputOption) {
        this.inputOption = inputOption;
    }

    public boolean getIsHasDetails() {
        return isHasDetails;
    }

    public void setIsHasDetails(boolean isHasDetails) {
        this.isHasDetails = isHasDetails;
    }


    public CarInsKindBean() {

    }

}
