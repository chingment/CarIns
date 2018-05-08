package com.uplink.carins.model.api;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chingment on 2018/5/8.
 */

public class CarInsCarModelInfoResultBean implements Serializable {

    public List<CarInsCarModelInfoBean> getModels() {
        return models;
    }

    public void setModels(List<CarInsCarModelInfoBean> models) {
        this.models = models;
    }

    private List<CarInsCarModelInfoBean> models;
}
