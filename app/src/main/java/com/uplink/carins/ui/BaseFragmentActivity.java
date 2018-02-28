package com.uplink.carins.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.umeng.analytics.MobclickAgent;
import com.uplink.carins.Own.AppCacheManager;
import com.uplink.carins.Own.AppContext;
import com.uplink.carins.Own.AppManager;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.activity.ForgetPwdCheckUsernameActivity;
import com.uplink.carins.activity.ForgetPwdModifyActivity;
import com.uplink.carins.activity.LoginActivity;
import com.uplink.carins.activity.MainActivity;
import com.uplink.carins.activity.PayConfirmActivity;
import com.uplink.carins.activity.PayQrcodeActivity;
import com.uplink.carins.activity.RegisterActivity;
import com.uplink.carins.device.N900Device;
import com.uplink.carins.fragment.HomeFragment;
import com.uplink.carins.http.HttpClient;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.HomePageBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by chingment on 2017/8/23.
 */

public class BaseFragmentActivity extends FragmentActivity {
    private String TAG = "BaseFragmentActivity";
    public AppContext appContext;

    private static Handler myTaskHandler;
    private static Runnable myTaskRunnable;

    private static boolean isActive = false;

    private static N900Device n900Device;

    public static N900Device getn900Device() {
        return n900Device;
    }



    public void stopMyTask() {
        if (myTaskHandler != null) {
            if (myTaskRunnable != null) {
                myTaskHandler.removeCallbacks(myTaskRunnable); //关闭定时执行操作
            }
        }

    }


    public void startMyTask() {
        if (myTaskHandler != null) {
            if (myTaskRunnable != null) {
                myTaskHandler.postDelayed(myTaskRunnable, 2000);
            }
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getAppManager().addActivity(this);

        appContext = (AppContext) getApplication();

        mProgressDialog = new Dialog(this, R.style.dialog_loading_style);

        if (myTaskHandler == null) {
            myTaskHandler = new Handler();

            myTaskRunnable = new Runnable() {
                @Override
                public void run() {
                    LogUtil.i("定时任务:" + CommonUtil.getCurrentTime());
                    loadTaskData();

                    Activity act = AppManager.getAppManager().currentActivity();
                    if (act != null) {

                        if (act instanceof MainActivity) {
                            MainActivity act_main = (MainActivity) act;
                            if (act_main.getHomeFragment() != null) {
                                if (HomeFragment.isNeedUpdateActivity) {
                                    LogUtil.i("更新HomeFragment界面");
                                    HomeFragment homeFragment = act_main.getHomeFragment();
                                    homeFragment.setBanner(AppCacheManager.getBanner());
                                    homeFragment.setThirdPartyApp(AppCacheManager.getExtendedAppByThirdPartyApp());
                                    homeFragment.setHaoYiLianApp(AppCacheManager.getExtendedAppByHaoYiLianApp());
                                    HomeFragment.isNeedUpdateActivity = false;
                                }
                            }
                        }
                    }

                    myTaskHandler.postDelayed(this, 2000);
                }
            };
        }

        startMyTask();

        if (n900Device == null) {

            n900Device = N900Device.getInstance(this);

            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        n900Device.connectDevice();
                    }
                }).start();
            } catch (Exception e) {
                showToast("设备连接异常：" + e);
            }
        }

    }


    public void loadTaskData() {

        if (getAppContext().getUser() != null) {
            Map<String, String> params = new HashMap<>();
            params.put("userId", getAppContext().getUser().getId() + "");
            params.put("merchantId", getAppContext().getUser().getMerchantId() + "");
            params.put("posMachineId", getAppContext().getUser().getPosMachineId() + "");
            params.put("datetime", AppCacheManager.getLastUpdateTime());


            LogUtil.i("datetime:" + AppCacheManager.getLastUpdateTime());

            HttpClient.getWithMy(Config.URL.home, params, new CallBack());
        }
    }

    public AppContext getAppContext() {
        return appContext;
    }

    public void showToast(String txt) {
        if (!StringUtil.isEmpty(txt))
            Toast.makeText(BaseFragmentActivity.this, txt, Toast.LENGTH_SHORT).show();
    }

    public Dialog mProgressDialog;
    public TextView mProgressTextView;

    public Dialog getmProgressDialog() {
        return mProgressDialog;
    }

    public void showProgressDialog(boolean canCancel) {
        if (mProgressDialog != null && !mProgressDialog.isShowing() && !this.isFinishing()) {
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.dialog_base_progress);
            WindowManager.LayoutParams lp = mProgressDialog.getWindow().getAttributes();
            lp.dimAmount = 0.0f;
            mProgressDialog.getWindow().setAttributes(lp);
            mProgressDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressTextView = (TextView) mProgressDialog.findViewById(R.id.textView1);
        }
        mProgressDialog.setCancelable(canCancel);
        mProgressTextView.setText("请稍候...");
    }

    public void showProgressDialog(String text, boolean canCancel) {
        if (mProgressDialog != null && !mProgressDialog.isShowing() && !this.isFinishing()) {
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.dialog_base_progress);
            WindowManager.LayoutParams lp = mProgressDialog.getWindow().getAttributes();
            lp.dimAmount = 0.0f;
            mProgressDialog.getWindow().setAttributes(lp);
            mProgressDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressTextView = (TextView) mProgressDialog.findViewById(R.id.textView1);
        }
        mProgressDialog.setCancelable(canCancel);
        if (!StringUtil.isEmpty(text)) {
            mProgressTextView.setText(text);
        } else {
            mProgressTextView.setText("请稍候...");
        }
    }

    /**
     * 描述：移除进度框.
     */
    public void removeProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        MobclickAgent.onResume(this);
//    }
//
//    public void onPause() {
//        super.onPause();
//        MobclickAgent.onPause(this);
//    }


    /**
     * Activity从后台重新回到前台时被调用
     */
    @Override
    protected void onRestart() {
        super.onRestart();

        LogUtil.e("onRestart is invoke!!!");
    }

    /**
     * Activity创建或者从后台重新回到前台时被调用
     */
    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.e("onStart is invoke!!!");
    }


    /**
     * Activity创建或者从被覆盖、后台重新回到前台时被调用
     */
    @Override
    protected void onResume() {

        super.onResume();

        if (!isActive) {
            startMyTask();
            isActive = true;
        }

        LogUtil.e("onResume is invoke!!!");
    }

    /**
     * Activity被覆盖到下面或者锁屏时被调用
     */
    @Override
    protected void onPause() {
        super.onPause();

        LogUtil.e("onPause is invoke!!!");
    }


    /**
     * 退出当前Activity或者跳转到新Activity时被调用
     */
    @Override
    protected void onStop() {
        super.onStop();

        if (!isAppOnForeground()) {
            LogUtil.e("后台 onStop is invoke!!!");
            stopMyTask();
            isActive = false;
        } else {
            LogUtil.e("前台 onStop is invoke!!!");
        }
    }

    /**
     * 退出当前Activity时被调用,调用之后Activity就结束了
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e("onDestroy is invoke!!!");
        // 结束Activity从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    public void finish() {
        super.finish();

    }


    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    public void postWithMy(String url, Map<String, Object> params, Map<String, String> filePaths, final HttpResponseHandler handler) {

        HttpClient.postWithMy(url, params, filePaths, new HttpResponseHandler() {

            @Override
            public void onBeforeSend() {
                showProgressDialog(false);
            }

            @Override
            public void onSuccess(String response) {
                handler.onSuccess(response);
            }

            @Override
            public void onFailure(Request request, Exception e) {
                handler.onFailure(request, e);
            }

            @Override
            public void onComplete() {
                removeProgressDialog();
            }
        });
    }

    public void getWithMy(String url, Map<String, String> param, final HttpResponseHandler handler) {
        HttpClient.getWithMy(url, param, new HttpResponseHandler() {

            @Override
            public void onBeforeSend() {
                showProgressDialog(false);
            }

            @Override
            public void onSuccess(String response) {
                handler.onSuccess(response);
            }

            @Override
            public void onFailure(Request request, Exception e) {
                handler.onFailure(request, e);
            }

            @Override
            public void onComplete() {
                removeProgressDialog();
            }
        });
    }


    private class CallBack extends HttpResponseHandler {
        @Override
        public void onSuccess(String response) {
            super.onSuccess(response);


            LogUtil.i(TAG, "onSuccess====>>>" + response);

            ApiResultBean<HomePageBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<HomePageBean>>() {
            });

            if (rt.getResult() == Result.SUCCESS) {


                HomePageBean bean = rt.getData();

                LogUtil.i("更新缓存数据" + bean.getLastUpdateTime());

                AppCacheManager.setLastUpdateTime(bean.getLastUpdateTime());
                AppCacheManager.setBanner(bean.getBanner());
                AppCacheManager.setCarInsCompany(bean.getCarInsCompany());
                AppCacheManager.setCarInsPlan(bean.getCarInsPlan());
                AppCacheManager.setCarInsKind(bean.getCarInsKind());
                AppCacheManager.setTalentDemandWorkJob(bean.getTalentDemandWorkJob());
                AppCacheManager.setExtendedApp(bean.getExtendedApp());


                HomeFragment.isNeedUpdateActivity = true;


                if (bean.getOrderInfo() != null) {

                    Bundle b = new Bundle();
                    b.putSerializable("dataBean", bean.getOrderInfo());

                    //LogUtil.i("d=>>>>>>>>getOrderInfo().getProductName" + bean.getOrderInfo().getProductName());
                    //LogUtil.i("d=>>>>>>>>getOrderInfo().getProductType" + bean.getOrderInfo().getProductType());

                    Activity act = AppManager.getAppManager().currentActivity();
                    if (act != null) {

                        //LogUtil.i(TAG, "is====>>>BaseFragmentActivity:" + act.canAutoGoToPayConfirmActivity);


                        Boolean isfalg1 = act instanceof LoginActivity;
                        Boolean isfalg2 = act instanceof PayQrcodeActivity;
                        Boolean isfalg3 = act instanceof PayConfirmActivity;
                        Boolean isfalg4 = act instanceof RegisterActivity;
                        Boolean isfalg5 = act instanceof ForgetPwdCheckUsernameActivity;
                        Boolean isfalg6 = act instanceof ForgetPwdModifyActivity;
                        //LogUtil.i("d=>>>>>>>>>>>>>>>>isfalg:" + isfalg);

                        if (isfalg1.equals(false) && isfalg2.equals(false) && isfalg3.equals(false)
                                && isfalg4.equals(false) && isfalg5.equals(false) && isfalg6.equals(false)
                                ) {

                            Intent intent = new Intent(act, PayConfirmActivity.class);
                            intent.putExtras(b);

                            stopMyTask();

                            startActivity(intent);

                            AppManager.getAppManager().finishAllActivity();
                        }
                    }
                }

            }
        }

        @Override
        public void onFailure(Request request, Exception e) {
            super.onFailure(request, e);
            LogUtil.e(TAG, "onFailure====>>>" + e.getMessage());
            showToast("数据加载失败");
        }
    }


}
