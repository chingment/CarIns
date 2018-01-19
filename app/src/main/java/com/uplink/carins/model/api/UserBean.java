package com.uplink.carins.model.api;

import com.uplink.carins.utils.LogUtil;

import java.io.Serializable;

/**
 * Created by chingment on 2018/1/19.
 */

public class UserBean  implements Serializable {
    private int id;
    private int merchantId;
    public int getId() {

        LogUtil.i("获取当前用户id:"+id);

        return id;
    }

    public void setId(int id) {
        LogUtil.i("设置当前用户id:"+id);
        this.id = id;
    }

    public int getMerchantId() {

        return merchantId;
    }

    public void setMerchantId(int id) {
        this.merchantId = id;
    }
}
