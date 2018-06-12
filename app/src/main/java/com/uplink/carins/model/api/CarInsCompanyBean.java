package com.uplink.carins.model.api;

import java.io.Serializable;

/**
 * Created by chingment on 2017/12/19.
 */

public class CarInsCompanyBean implements Serializable {
    private int id;
    private String name;
    private String imgUrl;
    private boolean canInsure;
    private boolean canClaims;
    private boolean canApplyLossAssess;

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

    public boolean getCanInsure() {
        return canInsure;
    }

    public void setCanInsure(boolean canInsure) {
        this.canInsure = canInsure;
    }

    public boolean getCanClaims() {
        return canClaims;
    }

    public void setCanClaims(boolean canClaims) {
        this.canClaims = canClaims;
    }

    public boolean getCanApplyLossAssess() {
        return canApplyLossAssess;
    }

    public void setCanApplyLossAssess(boolean canApplyLossAssess) {
        this.canApplyLossAssess = canApplyLossAssess;
    }



    public  CarInsCompanyBean()
    {

    }
    public  CarInsCompanyBean(int id,String name,String imgUrl,boolean canInsure,boolean canClaims)
    {
        this.id=id;
        this.name=name;
        this.imgUrl=imgUrl;
        this.canInsure=canInsure;
        this.canClaims=canClaims;
    }


}
