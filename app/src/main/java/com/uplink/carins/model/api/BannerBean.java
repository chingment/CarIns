package com.uplink.carins.model.api;

import java.io.Serializable;

public class BannerBean implements Serializable  {


    private int id;

    private String imgUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}