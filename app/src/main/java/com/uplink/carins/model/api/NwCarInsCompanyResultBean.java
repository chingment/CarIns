package com.uplink.carins.model.api;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chingment on 2018/5/9.
 */

public class NwCarInsCompanyResultBean implements Serializable {
    private  int areaId;

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getLicensePicKey() {
        return licensePicKey;
    }

    public void setLicensePicKey(String licensePicKey) {
        this.licensePicKey = licensePicKey;
    }

    public List<NwCarInsChannelBean> getChannels() {
        return channels;
    }

    public void setChannels(List<NwCarInsChannelBean> channels) {
        this.channels = channels;
    }

    public List<NwCarInsAreaBean> getAreas() {
        return areas;
    }

    public void setAreas(List<NwCarInsAreaBean> areas) {
        this.areas = areas;
    }

    private String licensePicKey;
    private List<NwCarInsChannelBean> channels;
    private List<NwCarInsAreaBean> areas;
}
