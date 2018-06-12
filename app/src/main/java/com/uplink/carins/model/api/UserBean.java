package com.uplink.carins.model.api;

import java.io.Serializable;

/**
 * Created by chingment on 2018/1/19.
 */

public class UserBean  implements Serializable {
    private int id;
    private int merchantId;
    private int posMachineId;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMerchantId() {

        return merchantId;
    }

    public void setMerchantId(int id) {
        this.merchantId = id;
    }

    public int getPosMachineId() {

        return posMachineId;
    }

    public void setPosMachineId(int posMachineId) {
        this.posMachineId = posMachineId;
    }

    public int getStatus() {

        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
