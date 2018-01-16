package com.uplink.carins.model.api;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chingment on 2017/12/19.
 */

public class CarInsPlanBean implements Serializable {
    private int id;
    private String name;
    private String imgUrl;
    private List<CarInsPlanKindParentBean> kindParent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setKindParent(List<CarInsPlanKindParentBean> kindParent) {
        this.kindParent = kindParent;
    }

    public List<CarInsPlanKindParentBean> getKindParent() {
        return kindParent;
    }

    public CarInsPlanBean() {

    }

    public CarInsPlanBean(int id, String name, String imgUrl, List<CarInsPlanKindParentBean> kindParent) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
        this.kindParent = kindParent;
    }

    public boolean ContainPId(int pid) {
        boolean isflag = false;
        for (CarInsPlanKindParentBean bean : kindParent) {

            if (bean.getId() == pid) {
                isflag = true;
                break;
            }
        }
        return isflag;

    }

}
