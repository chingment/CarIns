package com.uplink.carins.Own;

import android.app.Application;

import com.uplink.carins.model.api.UserBean;

/**
 * Created by chingment on 2017/8/23.
 */

public class AppContext extends Application {

    private static AppContext app;

    public AppContext() {
        app = this;
    }

    public static synchronized AppContext getInstance() {
        if (app == null) {
            app = new AppContext();
        }
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public UserBean user;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
}
