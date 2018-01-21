package com.uplink.carins.Own;

import com.uplink.carins.*;



public class Config {
    public static final boolean showDebug = true;


    public class URL {
        public static final String getOrderList =BuildConfig.ENVIRONMENT+"/api/Order/GetList";
        public static final String submitInsure = BuildConfig.ENVIRONMENT + "/api/CarService/SubmitInsure";
        public static final String submitClaim = BuildConfig.ENVIRONMENT + "/api/CarService/SubmitClaim";
        public static final String home = BuildConfig.ENVIRONMENT + "/api/Account/Home";
        public static final String getDetails = BuildConfig.ENVIRONMENT + "/api/Order/GetDetails";
        public static final String submitFollowInsure = BuildConfig.ENVIRONMENT + "/api/CarService/SubmitFollowInsure";
        public static final String submitEstimateList = BuildConfig.ENVIRONMENT  + "/api/CarService/SubmitEstimateList";
        public static final String submitTalentDemand = BuildConfig.ENVIRONMENT  + "/api/Order/SubmitTalentDemand";
    }
}
