package com.uplink.carins.Own;

import android.util.Base64;

import com.uplink.carins.*;
import com.uplink.carins.utils.SHA256Encrypt;
import com.uplink.carins.utils.StringUtil;


public class Config {
    public static final boolean showDebug = BuildConfig.SHOWDEBUG;

    public static String getSign(String data, String currenttime) {
        // 待加密
        String queryStr = BuildConfig.APPKEY + BuildConfig.APPSECRET + currenttime + data;
//        LogUtil.e(TAG, "queryStr>>==>>" + queryStr);
        String sortedStr = StringUtil.sortString(queryStr);
//        LogUtil.e(TAG, "sortedStr>>==>>" + sortedStr);
        String sha256edStr = SHA256Encrypt.bin2hex(sortedStr).toLowerCase();
//        LogUtil.e(TAG, "sha256edStr>>==>>" + sha256edStr);
        String base64Str = Base64.encodeToString(sha256edStr.getBytes(), Base64.NO_WRAP);
//        String base64Str = StringUtils.replaceEnter(Base64.encodeToString(sha256edStr.getBytes(), Base64.NO_WRAP), "");
//        LogUtil.e(TAG, "加密后>>==>>" + base64Str);
        return base64Str;
    }

    public class URL {
        public static final String login =BuildConfig.ENVIRONMENT+"/api/Account/Login";
        public static final String getOrderList =BuildConfig.ENVIRONMENT+"/api/Order/GetList";
        public static final String getProductList =BuildConfig.ENVIRONMENT+"/api/Product/GetList";
        public static final String submitInsure = BuildConfig.ENVIRONMENT + "/api/CarService/SubmitInsure";
        public static final String submitClaim = BuildConfig.ENVIRONMENT + "/api/CarService/SubmitClaim";
        public static final String home = BuildConfig.ENVIRONMENT + "/api/Account/Home";
        public static final String getDetails = BuildConfig.ENVIRONMENT + "/api/Order/GetDetails";
        public static final String submitFollowInsure = BuildConfig.ENVIRONMENT + "/api/CarService/SubmitFollowInsure";
        public static final String submitEstimateList = BuildConfig.ENVIRONMENT  + "/api/CarService/SubmitEstimateList";
        public static final String submitTalentDemand = BuildConfig.ENVIRONMENT  + "/api/Order/SubmitTalentDemand";
        public static final String submitApplyLossAssess= BuildConfig.ENVIRONMENT  + "/api/Order/SubmitApplyLossAssess";
        public static final String getCreateAccountCode = BuildConfig.ENVIRONMENT  + "/api/Sms/GetCreateAccountCode";
        public static final String getGetForgetPwdCode = BuildConfig.ENVIRONMENT  + "/api/Sms/GetForgetPwdCode";
        public static final String accountCreate = BuildConfig.ENVIRONMENT  + "/api/Account/Create";
        public static final String accountResetPassword = BuildConfig.ENVIRONMENT  + "/api/Account/ResetPassword";
        public static final String orderQrCodeDownload = BuildConfig.ENVIRONMENT  + "/api/Order/QrCodeDownload";
        public static final String orderPayResultQuery = BuildConfig.ENVIRONMENT  + "/api/Order/PayResultQuery";
        public static final String extendedAppGoTo = BuildConfig.ENVIRONMENT  + "/ExtendedApp/GoTo";
        public static final String orderPayResultNotify = BuildConfig.ENVIRONMENT  + "/api/Order/PayResultNotify";
        public static final String dingshundianshenqingxieyi = BuildConfig.ENVIRONMENT  + "/Resource/lipeixieyi";
        public static final String yonggongxieyi = BuildConfig.ENVIRONMENT  + "/Resource/yonggongxieyi";
        public static final String lllegalQuery  = BuildConfig.ENVIRONMENT + "/api/lllegal/query";
        public static final String lllegalQueryLog  = BuildConfig.ENVIRONMENT + "/api/lllegal/querylog";
        public static final String submitLllegalQueryScoreRecharge= BuildConfig.ENVIRONMENT + "/api/Order/SubmitLllegalQueryScoreRecharge";
        public static final String submitLllegalDealt= BuildConfig.ENVIRONMENT + "/api/Lllegal/Dealt";
        public static final String submitInsurance= BuildConfig.ENVIRONMENT + "/api/Order/SubmitInsurance";
        public static final String orderGetPayTranSn = BuildConfig.ENVIRONMENT + "/api/Order/GetPayTranSn";
        public static final String carInsGetCarInfo = BuildConfig.ENVIRONMENT + "/api/CarIns/GetCarInfo";
        public static final String carInsGetCarModelInfo = BuildConfig.ENVIRONMENT + "/api/CarIns/GetCarModelInfo";
        public static final String carInsEditBaseInfo = BuildConfig.ENVIRONMENT + "/api/CarIns/EditBaseInfo";
        public static final String carInsComanyInfo = BuildConfig.ENVIRONMENT + "/api/CarIns/InsComanyInfo";
        public static final String carInsInsInquiry = BuildConfig.ENVIRONMENT + "/api/CarIns/InsInquiry";

    }
}
