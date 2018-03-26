package com.uplink.carins.model.api;

import com.uplink.carins.utils.StringUtil;

/**
 * Created by chingment on 2018/3/26.
 */

public class CarTypeBean {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CarTypeBean() {

    }


    public CarTypeBean(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private String code;
    private String name;
}
