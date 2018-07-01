package com.uplink.carins.model.api;

import java.io.Serializable;

/**
 * Created by chingment on 2018/6/7.
 */

public class ProductChildKindBean implements Serializable {
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    private String id;
    private String name;
    private String imgUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
