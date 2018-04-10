package com.uplink.carins.model.api;

import java.io.Serializable;

/**
 * Created by chingment on 2018/4/10.
 */

public class ImgSetBean implements Serializable {

    private String imgUrl;
    private Boolean isMain;
    private String name;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Boolean getMain() {
        return isMain;
    }

    public void setMain(Boolean main) {
        isMain = main;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    private int priority;
}
