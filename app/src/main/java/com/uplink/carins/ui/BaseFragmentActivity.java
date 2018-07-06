package com.uplink.carins.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.nld.cloudpos.aidl.AidlDeviceService;
import com.nld.cloudpos.aidl.printer.AidlPrinter;
import com.nld.cloudpos.aidl.printer.AidlPrinterListener;
import com.nld.cloudpos.aidl.printer.PrintItemObj;
import com.nld.cloudpos.data.PrinterConstant;
import com.umeng.analytics.MobclickAgent;
import com.uplink.carins.Own.AppCacheManager;
import com.uplink.carins.Own.AppContext;
import com.uplink.carins.Own.AppManager;
import com.uplink.carins.Own.Config;
import com.uplink.carins.activity.MainActivity;
import com.uplink.carins.activity.PayConfirmActivity;
import com.uplink.carins.activity.PayQrcodeActivity;
import com.uplink.carins.activity.adapter.AcountBaseInfoHandler;
import com.uplink.carins.fragment.HomeFragment;
import com.uplink.carins.http.HttpClient;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.AcountBaseInfoResultBean;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.HomePageBean;
import com.uplink.carins.model.api.PrintDataBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.dialog.CustomDialogLoading;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.StringUtil;
import com.uplink.carins.utils.ToastUtil;

import java.util.ArrayList;
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


    public void stopMyTask() {
        if (myTaskHandler != null) {
            if (myTaskRunnable != null) {
                LogUtil.i("停止->定时任务:" + CommonUtil.getCurrentTime());
                myTaskHandler.removeCallbacks(myTaskRunnable); //关闭定时执行操作
                myTaskHandler = null;
                myTaskRunnable = null;
            }
        }
    }

    public void startMyTask() {
        if (myTaskHandler == null) {
            if (myTaskRunnable == null) {
                if (getAppContext().getUser() != null) {
                    LogUtil.i("开始->定时任务:" + CommonUtil.getCurrentTime());
                    myTaskHandler = new Handler();
                    myTaskRunnable = new Runnable() {
                        @Override
                        public void run() {
                            LogUtil.i("执行->定时任务:" + CommonUtil.getCurrentTime());

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
                                            homeFragment.setServiceTelphone(AppCacheManager.getServiceTelPhone());
                                            HomeFragment.isNeedUpdateActivity = false;
                                        }
                                    }
                                }
                            }
                            myTaskHandler.postDelayed(this, 50000);
                        }
                    };

                    myTaskHandler.postDelayed(myTaskRunnable, 50000);
                }
            }
        }
    }

    private CustomDialogLoading customDialogLoading;

    public CustomDialogLoading getCustomDialogLoading() {
        return customDialogLoading;
    }


    AidlDeviceService aidlDeviceService = null;
    AidlPrinter aidlPrinter = null;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //Log.d(TAG, "bind device service");
            aidlDeviceService = AidlDeviceService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //Log.d(TAG, "unbind device service");
            aidlDeviceService = null;
        }
    };

    public void bindServiceConnection() {
        bindService(new Intent("nld_cloudpos_device_service"), serviceConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getAppManager().addActivity(this);

        appContext = (AppContext) getApplication();

        customDialogLoading = new CustomDialogLoading(this);


        try {
            bindServiceConnection();
        } catch (Exception ex) {
            //showToast("链接打印机失败");
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

            getWithMy(Config.URL.home, params, false, "", new CallBack());
        }
    }

    public void getAcountBaseInfo(final AcountBaseInfoHandler handler) {

        if (getAppContext().getUser() != null) {

            Map<String, String> params = new HashMap<>();
            params.put("userId", getAppContext().getUser().getId() + "");
            params.put("merchantId", getAppContext().getUser().getMerchantId() + "");
            params.put("posMachineId", getAppContext().getUser().getPosMachineId() + "");

            getWithMy(Config.URL.accountBaseInfo, params, false, "", new HttpResponseHandler() {

                @Override
                public void onSuccess(String response) {
                    super.onSuccess(response);

                    ApiResultBean<AcountBaseInfoResultBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<AcountBaseInfoResultBean>>() {
                    });

                    if (rt.getResult() == Result.SUCCESS) {
                        handler.handler(rt.getData());
                    }
                }

            });
        }
    }

    public AppContext getAppContext() {
        return appContext;
    }

    public void showToast(String txt) {
        if (!StringUtil.isEmpty(txt)) {
            ToastUtil.showMessage(BaseFragmentActivity.this, txt, Toast.LENGTH_LONG);
        }
    }

    public void showMsg(String string) {
        final String log = string;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(log);
            }
        });
    }

    /**
     * Activity从后台重新回到前台时被调用
     */
    @Override
    protected void onRestart() {
        super.onRestart();

        //LogUtil.e("onRestart is invoke!!!");
    }

    /**
     * Activity创建或者从后台重新回到前台时被调用
     */
    @Override
    protected void onStart() {
        super.onStart();
        //LogUtil.e("onStart is invoke!!!");
    }


    /**
     * Activity创建或者从被覆盖、后台重新回到前台时被调用
     */
    @Override
    protected void onResume() {

        super.onResume();
        MobclickAgent.onResume(this);
        if (!isActive) {

            startMyTask();

            isActive = true;
        }

        //LogUtil.e("onResume is invoke!!!");
    }

    /**
     * Activity被覆盖到下面或者锁屏时被调用
     */
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        //LogUtil.e("onPause is invoke!!!");
    }


    /**
     * 退出当前Activity或者跳转到新Activity时被调用
     */
    @Override
    protected void onStop() {
        super.onStop();

        if (!isAppOnForeground()) {
            //LogUtil.e("后台 onStop is invoke!!!");
            stopMyTask();
            isActive = false;
        } else {
            //LogUtil.e("前台 onStop is invoke!!!");
        }
    }

    /**
     * 退出当前Activity时被调用,调用之后Activity就结束了
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        //aidlPrinter = null;
        //LogUtil.e("onDestroy is invoke!!!");
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

    public void postWithMy(String url, Map<String, Object> params, Map<String, String> filePaths, final Boolean isShowLoading, final String loadingMsg, final HttpResponseHandler handler) {

        HttpClient.postWithMy(url, params, filePaths, new HttpResponseHandler() {

            @Override
            public void onBeforeSend() {
                if (isShowLoading) {
                    if (!StringUtil.isEmptyNotNull(loadingMsg)) {
                        customDialogLoading.setProgressText(loadingMsg);
                        customDialogLoading.showDialog();
                    }
                }
            }

            @Override
            public void onSuccess(String response) {

                final String s = response;
                if (s.indexOf("\"result\":") > -1) {
                    //运行在子线程,,
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            handler.onSuccess(s);
                        }
                    });
                } else {
                    showToast("服务器数据异常");
                    LogUtil.e("解释错误：原始数据》》" + s);
                }
            }

            @Override
            public void onFailure(Request request, Exception e) {
                handler.onFailure(request, e);
            }

            @Override
            public void onComplete() {
                if (isShowLoading) {
                    customDialogLoading.cancelDialog();
                }
            }
        });
    }

    public void getWithMy(String url, Map<String, String> param, final Boolean isShowLoading, final String loadingMsg, final HttpResponseHandler handler) {
        HttpClient.getWithMy(url, param, new HttpResponseHandler() {

            @Override
            public void onBeforeSend() {

                if (isShowLoading) {
                    if (!StringUtil.isEmptyNotNull(loadingMsg)) {
                        customDialogLoading.setProgressText(loadingMsg);
                        customDialogLoading.showDialog();
                    }
                }
            }

            @Override
            public void onSuccess(String response) {

                final String s = response;


                if (s.indexOf("\"result\":") > -1) {
                    //运行在子线程,,
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            handler.onSuccess(s);
                        }
                    });
                } else {
                    showToast("服务器数据异常");
                    LogUtil.e("解释错误：原始数据》》" + s);
                }
            }

            @Override
            public void onFailure(Request request, Exception e) {
                handler.onFailure(request, e);
            }

            @Override
            public void onComplete() {
                if (isShowLoading) {
                    customDialogLoading.cancelDialog();
                }
            }
        });
    }


    private class CallBack extends HttpResponseHandler {
        @Override
        public void onSuccess(String response) {
            super.onSuccess(response);


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
                AppCacheManager.setServiceTelPhone(bean.getServiceTelphone());

                HomeFragment.isNeedUpdateActivity = true;


                if (bean.getOrderInfo() != null) {

                    Bundle b = new Bundle();
                    b.putSerializable("dataBean", bean.getOrderInfo());
                    Activity act = AppManager.getAppManager().currentActivity();

                    if (act != null) {

                        if (getAppContext().getUser() != null) {

                            Boolean isPayConfirmActivity = act instanceof PayConfirmActivity;
                            Boolean isPayQrcodeActivity = act instanceof PayQrcodeActivity;
                            if (isPayConfirmActivity.equals(false) && isPayQrcodeActivity.equals(false)) {
                                Intent intent = new Intent(act, PayConfirmActivity.class);
                                intent.putExtras(b);
                                stopMyTask();
                                startActivity(intent);
                            }
                        }

                    }
                }
            }
        }
    }

    /**
     * 打印文本
     *
     * @throws RemoteException
     */

    public void printTicket(PrintDataBean data) {


        try {

            if (data == null) {
                showToast("打印的数据为空");
                return;
            }


            if (aidlDeviceService == null) {
                showToast("aidlDeviceService为空");
                return;
            }

            aidlPrinter = AidlPrinter.Stub.asInterface(aidlDeviceService.getPrinter());

            if (aidlPrinter == null) {
                showToast("打印设备对象为空");
                return;
            }

            //文本内容
            final List<PrintItemObj> printItems = new ArrayList<PrintItemObj>();

            printItems.add(new PrintItemObj("签购单", PrinterConstant.FontScale.FONTSCALE_DW_H, PrinterConstant.FontType.FONTTYPE_N, PrintItemObj.ALIGN.CENTER, false, 6));
            printItems.add(new PrintItemObj("商户存根/MERCHANT COPY", PrinterConstant.FontScale.FONTSCALE_DW_H, PrinterConstant.FontType.FONTTYPE_N, PrintItemObj.ALIGN.CENTER, false, 6));
            printItems.add(new PrintItemObj("商户名称:" + data.getMerchantName(),  PrinterConstant.FontScale.FONTSCALE_W_H, PrinterConstant.FontType.FONTTYPE_N, PrintItemObj.ALIGN.LEFT, false, 6));
            printItems.add(new PrintItemObj("交易类型:" + data.getTradeType(), PrinterConstant.FontScale.FONTSCALE_W_H, PrinterConstant.FontType.FONTTYPE_N, PrintItemObj.ALIGN.LEFT, false, 6));
            printItems.add(new PrintItemObj("商品名称:" + data.getProductName(), PrinterConstant.FontScale.FONTSCALE_W_H, PrinterConstant.FontType.FONTTYPE_N, PrintItemObj.ALIGN.LEFT, false, 6));
            printItems.add(new PrintItemObj("交易单号:" + data.getTradeNo(), PrinterConstant.FontScale.FONTSCALE_W_H, PrinterConstant.FontType.FONTTYPE_N, PrintItemObj.ALIGN.LEFT, false, 6));
            printItems.add(new PrintItemObj("订单号:" + data.getOrderSn(), PrinterConstant.FontScale.FONTSCALE_W_H, PrinterConstant.FontType.FONTTYPE_N, PrintItemObj.ALIGN.LEFT, false, 6));
            printItems.add(new PrintItemObj("支付方式:" + data.getTradePayMethod(), PrinterConstant.FontScale.FONTSCALE_W_H, PrinterConstant.FontType.FONTTYPE_N, PrintItemObj.ALIGN.LEFT, false, 6));
            printItems.add(new PrintItemObj("日期时间:" + data.getTradeDateTime(), PrinterConstant.FontScale.FONTSCALE_W_H, PrinterConstant.FontType.FONTTYPE_N, PrintItemObj.ALIGN.LEFT, false, 6));
            printItems.add(new PrintItemObj("金 额:RMB " + data.getTradeAmount(), PrinterConstant.FontScale.FONTSCALE_W_H, PrinterConstant.FontType.FONTTYPE_N, PrintItemObj.ALIGN.LEFT, false, 6));
            printItems.add(new PrintItemObj("服务热线:" + data.getServiceHotline(), PrinterConstant.FontScale.FONTSCALE_W_H, PrinterConstant.FontType.FONTTYPE_N, PrintItemObj.ALIGN.LEFT, false, 6));
            printItems.add(new PrintItemObj("\r"));
            printItems.add(new PrintItemObj("\r"));
            printItems.add(new PrintItemObj("-------------------------------"));


            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (aidlPrinter != null) {
                        try {
                            aidlPrinter.open();
                            //打印文本
                            aidlPrinter.printText(printItems);
                            aidlPrinter.start(new AidlPrinterListener.Stub() {

                                @Override
                                public void onPrintFinish() throws RemoteException {
                                    Log.e(TAG, "打印结束");
                                    /**如果出现纸撕下部分有未输出的内容释放下面代码**/
                                    aidlPrinter.paperSkip(2);
                                    showMsg("打印结束");
                                }

                                @Override
                                public void onError(int errorCode) throws RemoteException {
                                    Log.e(TAG, "打印异常");
                                    showMsg("打印异常码:" + errorCode);
                                }
                            });

                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

        } catch (RemoteException ex) {
            ex.printStackTrace();
            showToast("打印失败,设备异常" + ex);
        }
    }

    private String getVersionName() {
        String versionName = "1.0";
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionName;
    }

}
