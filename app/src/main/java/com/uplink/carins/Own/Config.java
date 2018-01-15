package com.uplink.carins.Own;

import com.uplink.carins.*;



public class Config {
    public static final boolean showDebug = true;


    public class URL {



        public static final String getOrderList =BuildConfig.ENVIRONMENT+"/api/Order/GetList";
        public static final String submitInsure = BuildConfig.ENVIRONMENT + "/api/CarService/SubmitInsure";

        public static final String home = BuildConfig.ENVIRONMENT + "/api/Account/Home";


    }
}
