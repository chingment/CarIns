package com.uplink.carins.model.common;

/**
 * Created by chingment on 2017/12/18.
 */



public class NineGridItemBean {

    private String title;
    private Object icon;
    private NineGridItemType type;
    private String action;
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

    public NineGridItemType getType() {
        return type;
    }

    public void setType(NineGridItemType type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public  NineGridItemBean()
    {

    }

    public  NineGridItemBean(String title,NineGridItemType type,String action, Object icon)
    {
        this.title=title;
        this.type=type;
        this.icon=icon;
        this.action=action;
    }
}
