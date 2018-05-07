package com.uplink.carins.model.api;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chingment on 2018/5/7.
 */

public class CarInfoResultBean implements Serializable {
    private String auto ;

    public String getAuto() {
        return auto;
    }

    public void setAuto(String auto) {
        this.auto = auto;
    }

    public CarInfoBean getCar() {
        return car;
    }

    public void setCar(CarInfoBean car) {
        this.car = car;
    }

    public List<CustomerBean> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomerBean> customers) {
        this.customers = customers;
    }

    private CarInfoBean car ;
    private List<CustomerBean> customers ;
}
