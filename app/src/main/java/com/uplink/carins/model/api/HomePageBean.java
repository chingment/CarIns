package com.uplink.carins.model.api;

import com.uplink.carins.Own.AppCacheManager;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chingment on 2017/12/18.
 */

public class HomePageBean implements Serializable {

    private String lastUpdateTime;

    private List<BannerBean> banner;

    private List<CarInsCompanyBean> carInsCompany;

    private List<CarInsKindBean> carInsKind;

    private List<CarInsPlanBean> carInsPlan;

    public void setBanner(List<BannerBean> banner) {
        this.banner = banner;
    }

    public List<BannerBean> getBanner() {
        return banner;
    }

    public void setCarInsCompany(List<CarInsCompanyBean> carInsCompany) {
        this.carInsCompany = carInsCompany;
    }

    public List<CarInsCompanyBean> getCarInsCompany() {
        return carInsCompany;
    }

    public void setCarInsKind(List<CarInsKindBean> carInsKind) {
        this.carInsKind = carInsKind;
    }

    public List<CarInsKindBean> getCarInsKind() {
        return carInsKind;
    }

    public void setCarInsPlan(List<CarInsPlanBean> carInsPlan) {
        this.carInsPlan = carInsPlan;
    }

    public List<CarInsPlanBean> getCarInsPlan() {
        return carInsPlan;
    }

    public void setLastUpdateTime(String lastUpdateTime ) {

       this.lastUpdateTime=lastUpdateTime;
    }

    public String getLastUpdateTime() {

        return this.lastUpdateTime;

    }

}
