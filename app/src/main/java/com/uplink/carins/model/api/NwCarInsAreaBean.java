package com.uplink.carins.model.api;

import java.io.Serializable;

/**
 * Created by chingment on 2018/5/9.
 */

public class NwCarInsAreaBean implements Serializable {
    private int areaId;
    private String areaName;

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
