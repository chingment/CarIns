package com.uplink.carins.model.common;

/**
 * Created by chingment on 2017/12/18.
 */



public class NineGridItemBean {

    private int appId;
    private String title;
    private Object icon;
    private int type;
    private String action;
    private String referenceId;

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Object getIcon() {
        return icon;
    }

    public void setIcon(Object icon) {
        this.icon = icon;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public  NineGridItemBean()
    {

    }

    public  NineGridItemBean(int appId,String referenceId, String title,int type,String action, Object icon)
    {
        this.appId=appId;
        this.title=title;
        this.type=type;
        this.icon=icon;
        this.action=action;
        this.referenceId=referenceId;
    }
}
