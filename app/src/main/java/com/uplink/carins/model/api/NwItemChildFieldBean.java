package com.uplink.carins.model.api;


import java.io.Serializable;

/**
 * Created by chingment on 2018/5/14.
 */

public class NwItemChildFieldBean implements Serializable {
    private String field;

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

    private String value;
}
