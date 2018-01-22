package com.uplink.carins.model.api;

import java.io.Serializable;

/**
 * Created by chingment on 2018/1/22.
 */

public class TalentDemandWorkJobBean implements Serializable {

    private int id;

    private String name;

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
}
