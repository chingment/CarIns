package com.uplink.carins.model.api;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chingment on 2017/12/18.
 */

public class HomePageBean implements Serializable {

    private List<BannerBean> banner;

    private List<CarInsCompanyBean> carInsCompany;

    private List<CarInsKindBean> carInsKind;

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

}
