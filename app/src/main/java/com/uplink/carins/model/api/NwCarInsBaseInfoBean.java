package com.uplink.carins.model.api;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chingment on 2018/6/15.
 */

public class NwCarInsBaseInfoBean implements Serializable {

    private CarInfoBean car ;

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

    private List<CustomerBean> customers ;
}
