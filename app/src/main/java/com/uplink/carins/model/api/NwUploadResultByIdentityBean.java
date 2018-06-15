package com.uplink.carins.model.api;

import java.io.Serializable;

/**
 * Created by chingment on 2018/6/15.
 */

public class NwUploadResultByIdentityBean implements Serializable {
    private String url;
    private String key;
    private NwImageIdentityBean info;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public NwImageIdentityBean getInfo() {
        return info;
    }

    public void setInfo(NwImageIdentityBean info) {
        this.info = info;
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
