package com.uplink.carins.model.api;

import java.io.Serializable;

/**
 * Created by chingment on 2018/6/15.
 */

public class NwUploadResultByLicenseBean implements Serializable {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public NwImageLicenseBean getInfo() {
        return info;
    }

    public void setInfo(NwImageLicenseBean info) {
        this.info = info;
    }

    private String key;
    private NwImageLicenseBean info;
}
