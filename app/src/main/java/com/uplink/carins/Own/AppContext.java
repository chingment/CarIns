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

        // 注册App异常崩溃处理器
        registerUncaughtExceptionHandler();
    }

    private void registerUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
    }



    public UserBean getUser() {
        return AppCacheManager.getUser();
    }

    public void setUser(UserBean user) {
        AppCacheManager.setUser(user);
    }
}
