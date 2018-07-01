package com.uplink.carins.model.api;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chingment on 2018/6/7.
 */

public class ProductKindBean implements Serializable {
    private String id;
    private String name;
    private String imgUrl;
    private boolean selected;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private List<ImgSetBean> banners;
    private List<ProductChildKindBean> childs;

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

    public List<ImgSetBean> getBanners() {
        return banners;
    }

    public void setBanners(List<ImgSetBean> banners) {
        this.banners = banners;
    }

    public List<ProductChildKindBean> getChilds() {
        return childs;
    }

    public void setChilds(List<ProductChildKindBean> childs) {
        this.childs = childs;
    }
}
