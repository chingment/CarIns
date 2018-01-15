package com.uplink.carins.model.api;

import java.util.List;

/**
 * Created by chingment on 2017/12/20.
 */

public class CarInsPlanKindParentBean {

    private int id;
    private String name;
    private List<CarInsPlanKindChildBean> child;

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

    public void setChild(List<CarInsPlanKindChildBean> child) {
        this.child = child;
    }

    public List<CarInsPlanKindChildBean> getChild() {
        return child;
    }


    public CarInsPlanKindParentBean() {

    }

    public CarInsPlanKindParentBean(int id, String name, List<CarInsPlanKindChildBean> child) {
        this.id = id;
        this.name = name;
        this.child = child;
    }
}
