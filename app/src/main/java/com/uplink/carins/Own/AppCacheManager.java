package com.uplink.carins.Own;

import com.uplink.carins.model.api.*;
import com.uplink.carins.utils.ACache;
import com.uplink.carins.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chingment on 2018/1/16.
 */

public class AppCacheManager {

    private static String Cache_Key_CarInsCompany = "Cache_CarInsCompany";
    private static String Cache_Key_CarInsKind = "Cache_CarInsKind";
    private static String Cache_Key_CarInsPlan = "Cache_CarInsPlan";
    private static String Cache_Key_LastUpdateTime = "Cache_LastUpdateTime";
    private static String Cache_Key_TalentDemandWorkJob = "Cache_TalentDemandWorkJob";
    private static String Cache_Key_User = "Cache_User";
    private static String Cache_Key_LastUserName = "Cache_LastUserName";
    private static String Cache_Key_Banner = "Cache_Banner";
    private static String Cache_Key_ExtendedApp = "Cache_ExtendedApp";

    private static ACache getCache() {

        return ACache.get(AppContext.getInstance());
    }

    public static void setLastUpdateTime(String lastUpdateTime) {

        if (lastUpdateTime == null) {
            AppCacheManager.getCache().remove(Cache_Key_LastUpdateTime);
        } else {
            AppCacheManager.getCache().put(Cache_Key_LastUpdateTime, lastUpdateTime);
        }

    }

    public static String getLastUpdateTime() {

        String lastUpdateTime = AppCacheManager.getCache().getAsString(Cache_Key_LastUpdateTime);

        if (lastUpdateTime == null)
            return "";
        return lastUpdateTime;

    }


    public static void setCarInsCompany(List<CarInsCompanyBean> carInsCompany) {
        ArrayList<CarInsCompanyBean> been = (ArrayList<CarInsCompanyBean>) carInsCompany;
        AppCacheManager.getCache().put(Cache_Key_CarInsCompany, been);
    }

    private static ArrayList<CarInsCompanyBean> getCarInsCompany() {

        ArrayList<CarInsCompanyBean> carInsKinds = (ArrayList<CarInsCompanyBean>) AppCacheManager.getCache().getAsObject(Cache_Key_CarInsCompany);

        return carInsKinds;

    }

    public static List<CarInsCompanyBean> getCarInsCompanyCanClaims() {

        ArrayList<CarInsCompanyBean> carInsCompanys = AppCacheManager.getCarInsCompany();
        List<CarInsCompanyBean> carInsCompanyCanClaims = new ArrayList<>();

        for (CarInsCompanyBean bean : carInsCompanys) {

            if (bean.getCanClaims()) {
                carInsCompanyCanClaims.add(bean);
            }
        }

        return carInsCompanyCanClaims;

    }

    public static List<CarInsCompanyBean> getCarInsCompanyCanInsure() {

        ArrayList<CarInsCompanyBean> carInsCompanys = AppCacheManager.getCarInsCompany();

        List<CarInsCompanyBean> carInsCompanyCanInsures = new ArrayList<>();

        for (CarInsCompanyBean bean : carInsCompanys) {

            if (bean.getCanInsure()) {
                carInsCompanyCanInsures.add(bean);
            }
        }

        return carInsCompanyCanInsures;

    }


    public static List<CarInsCompanyBean> getCarInsCompanyCanApplyLossAssess() {

        ArrayList<CarInsCompanyBean> carInsCompanys = AppCacheManager.getCarInsCompany();

        List<CarInsCompanyBean> carInsCompanyCanInsures = new ArrayList<>();

        for (CarInsCompanyBean bean : carInsCompanys) {

            if (bean.getCanApplyLossAssess()) {
                carInsCompanyCanInsures.add(bean);
            }
        }

        return carInsCompanyCanInsures;

    }

    public static void setCarInsKind(List<CarInsKindBean> carInsKindBean) {
        ArrayList<CarInsKindBean> been = (ArrayList<CarInsKindBean>) carInsKindBean;

        for (CarInsKindBean b : been) {
            b.setIsCheck(true);
        }

        AppCacheManager.getCache().put(Cache_Key_CarInsKind, been);
    }

    private static ArrayList<CarInsKindBean> getCarInsKind() {

        ArrayList<CarInsKindBean> carInsKinds = (ArrayList<CarInsKindBean>) AppCacheManager.getCache().getAsObject(Cache_Key_CarInsKind);

        return carInsKinds;

    }

    public static CarInsKindBean getCarInsKind(int id) {

        List<CarInsKindBean> carInsKinds = AppCacheManager.getCarInsKind();

        CarInsKindBean carInsKind = null;

        for (CarInsKindBean bean : carInsKinds) {

            if (bean.getId() == id) {
                carInsKind = bean;
                break;
            }

        }
        return carInsKind;
    }

    public static List<CarInsKindBean> getCarInsKind(List<Integer> ids) {

        List<CarInsKindBean> carInsKinds = AppCacheManager.getCarInsKind();


        List<CarInsKindBean> carInsKindChilds = new ArrayList<>();

        for (CarInsKindBean carInsKind : carInsKinds) {

            for (int id : ids) {
                if (carInsKind.getId() == id) {

                    carInsKindChilds.add(carInsKind);
                }
            }

        }


        return carInsKindChilds;
    }

    public static void setCarInsPlan(List<CarInsPlanBean> carInsPlan) {
        ArrayList<CarInsPlanBean> been = (ArrayList<CarInsPlanBean>) carInsPlan;
        AppCacheManager.getCache().put(Cache_Key_CarInsPlan, been);
    }

    public static List<CarInsPlanBean> getCarInsPlan() {

        ArrayList<CarInsPlanBean> carInsPlan = (ArrayList<CarInsPlanBean>) AppCacheManager.getCache().getAsObject(Cache_Key_CarInsPlan);

        return carInsPlan;

    }


    public static CarInsPlanBean getCarInsPlan(int id) {

        List<CarInsPlanBean> carInsPlans = AppCacheManager.getCarInsPlan();

        CarInsPlanBean carInsPlan = null;

        for (CarInsPlanBean bean : carInsPlans) {

            if (bean.getId() == id) {
                carInsPlan = bean;
                break;
            }

        }


        return carInsPlan;

    }

    public static List<CarInsKindBean> getCarInsKindByPlanId(int id) {

        CarInsPlanBean carInsPlan = AppCacheManager.getCarInsPlan(id);

        List<CarInsKindBean> carInsKinds = new ArrayList<>();

        for (CarInsPlanKindParentBean p : carInsPlan.getKindParent()) {

            CarInsKindBean bean = AppCacheManager.getCarInsKind(p.getId());
            carInsKinds.add(bean);

            for (Integer child_id : p.getChild()) {

                bean = AppCacheManager.getCarInsKind(child_id);
                carInsKinds.add(bean);
            }


        }
        return carInsKinds;
    }


    public static void setTalentDemandWorkJob(List<TalentDemandWorkJobBean> talentDemandWorkJob) {
        ArrayList<TalentDemandWorkJobBean> been = (ArrayList<TalentDemandWorkJobBean>) talentDemandWorkJob;
        AppCacheManager.getCache().put(Cache_Key_TalentDemandWorkJob, been);
    }

    public static ArrayList<TalentDemandWorkJobBean> getTalentDemandWorkJob() {

        ArrayList<TalentDemandWorkJobBean> carInsKinds = (ArrayList<TalentDemandWorkJobBean>) AppCacheManager.getCache().getAsObject(Cache_Key_TalentDemandWorkJob);

        return carInsKinds;

    }

    public static void setUser(UserBean user) {
        AppCacheManager.getCache().put(Cache_Key_User, user);
    }

    public static UserBean getUser() {

        UserBean user = (UserBean) AppCacheManager.getCache().getAsObject(Cache_Key_User);

        return user;

    }

    public static void setLastUserName(String userName) {
        AppCacheManager.getCache().put(Cache_Key_LastUserName, userName);
    }

    public static String getLastUserName() {

        String userName = AppCacheManager.getCache().getAsString(Cache_Key_LastUserName);

        return userName;

    }


    public static void setBanner(List<BannerBean> bannerBean) {
        ArrayList<BannerBean> been = (ArrayList<BannerBean>) bannerBean;
        AppCacheManager.getCache().put(Cache_Key_Banner, been);
    }

    public static List<BannerBean> getBanner() {

        ArrayList<BannerBean> been = (ArrayList<BannerBean>) AppCacheManager.getCache().getAsObject(Cache_Key_Banner);

        return been;

    }


    public static void setExtendedApp(List<ExtendedAppBean> extendedApp) {
        ArrayList<ExtendedAppBean> been = (ArrayList<ExtendedAppBean>) extendedApp;
        AppCacheManager.getCache().put(Cache_Key_ExtendedApp, been);
    }

    public static List<ExtendedAppBean> getExtendedApp() {

        ArrayList<ExtendedAppBean> been = (ArrayList<ExtendedAppBean>) AppCacheManager.getCache().getAsObject(Cache_Key_ExtendedApp);

        return been;

    }


    public static List<ExtendedAppBean> getExtendedAppByCarInsService() {

        List<ExtendedAppBean> extendedApps = AppCacheManager.getExtendedApp();

        List<ExtendedAppBean> extendedAppsByCarInsService = new ArrayList<>();

        for (ExtendedAppBean bean : extendedApps) {

            if (bean.getType() == 3) {
                extendedAppsByCarInsService.add(bean);
            }
        }

        return extendedAppsByCarInsService;

    }

    public static List<ExtendedAppBean> getExtendedAppByThirdPartyApp() {

        List<ExtendedAppBean> extendedApps = AppCacheManager.getExtendedApp();

        List<ExtendedAppBean> extendedAppsByCarInsService = new ArrayList<>();

        for (ExtendedAppBean bean : extendedApps) {

            if (bean.getType() == 2) {
                extendedAppsByCarInsService.add(bean);
            }
        }

        return extendedAppsByCarInsService;

    }

    public static List<ExtendedAppBean> getExtendedAppByHaoYiLianApp() {

        List<ExtendedAppBean> extendedApps = AppCacheManager.getExtendedApp();

        List<ExtendedAppBean> extendedAppsByCarInsService = new ArrayList<>();

        for (ExtendedAppBean bean : extendedApps) {

            if (bean.getType() == 1) {
                extendedAppsByCarInsService.add(bean);
            }
        }

        return extendedAppsByCarInsService;

    }
}
