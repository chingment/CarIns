package com.uplink.carins.model.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chingment on 2017/12/20.
 */

public class CarInsPlanKindParentBean implements Serializable {

    private int id;

    private List<Integer> child;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setChild(List<Integer> child) {
        this.child = child;
    }

    public List<Integer> getChild() {
        return child;
    }


    public CarInsPlanKindParentBean() {

    }

    public boolean ContainId(int id) {
        boolean isflag = false;
        for (Integer bean : child) {

            if (bean == id) {
                isflag = true;
                break;
            }
        }
        return isflag;

    }
}
