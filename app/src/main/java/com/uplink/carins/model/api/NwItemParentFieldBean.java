package com.uplink.carins.model.api;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chingment on 2018/5/14.
 */

public class NwItemParentFieldBean implements Serializable {
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

    public List<NwItemChildFieldBean> getChild() {
        return child;
    }

    public void setChild(List<NwItemChildFieldBean> child) {
        this.child = child;
    }

    private List<NwItemChildFieldBean> child;
}
