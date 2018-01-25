package com.uplink.carins.model.api;

import java.io.Serializable;

/**
 * Created by chingment on 2018/1/22.
 */

public class ConfirmFieldBean implements Serializable {
    private String field;
    private String value;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public  ConfirmFieldBean() {

    }

    public  ConfirmFieldBean(String field,String value) {
        this.field=field;
        this.value=value;
    }
}
