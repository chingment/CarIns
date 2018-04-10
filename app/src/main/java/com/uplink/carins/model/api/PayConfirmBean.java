package com.uplink.carins.model.api;

import java.util.List;

/**
 * Created by chingment on 2018/1/22.
 */

public class PayConfirmBean {
    private String typeName;
    private List<ConfirmFieldBean> confirmField;
    private List<PayMethodBean> payMethod;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public  List<ConfirmFieldBean> getConfirmField() {
        return confirmField;
    }

    public void setConfirmField( List<ConfirmFieldBean> confirmField) {
        this.confirmField = confirmField;
    }

    public  List<PayMethodBean> getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(List<PayMethodBean> payMethod) {
        this.payMethod = payMethod;
    }
}
