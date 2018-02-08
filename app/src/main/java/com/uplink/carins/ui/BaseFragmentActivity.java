package com.uplink.carins.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.uplink.carins.Own.AppCacheManager;
import com.uplink.carins.Own.AppContext;
import com.uplink.carins.Own.AppManager;
import com.uplink.carins.R;
import com.uplink.carins.activity.MainActivity;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.StringUtil;

/**
 * Created by chingment on 2017/8/23.
 */

public class BaseFragmentActivity extends FragmentActivity {
    private String tag = "BaseFragmentActivity";
    public AppContext appContext;

    private Handler myTaskHandler;
    private  Runnable myTaskRunnable;


    public Handler getMyTaskHandler(){

        return  myTaskHandler;
    }

    public void setMyTask(Handler handler,Runnable runnable){

        this.myTaskHandler=handler;
        this.myTaskRunnable=runnable;
    }

    public Runnable getMyTaskRunnable(){

        return  myTaskRunnable;
    }

    public  void stopMyTask()
    {
        if(myTaskHandler!=null)
        {
            if(myTaskRunnable!=null)
            {
                myTaskHandler.removeCallbacks(myTaskRunnable); //关闭定时执行操作
            }
        }

    }

    public  void startMyTask()
    {
        if(myTaskHandler!=null)
        {
            if(myTaskRunnable!=null)
            {
                myTaskHandler.postDelayed(myTaskRunnable, 2000);
            }
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
        appContext = (AppContext) getApplication();

        mProgressDialog = new Dialog(this, R.style.dialog_loading_style);

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

        startMyTask();
        LogUtil.e("onRestart is invoke!!!");
    }

    /**
     *Activity创建或者从后台重新回到前台时被调用
     */
    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.e("onStart is invoke!!!");
    }


    /**
     *Activity创建或者从被覆盖、后台重新回到前台时被调用
     */
    @Override
    protected void onResume() {
        super.onResume();


        LogUtil.e("onResume is invoke!!!");
    }

    /**
     *  Activity被覆盖到下面或者锁屏时被调用
     */
    @Override
    protected void onPause() {
        super.onPause();

        stopMyTask();

        LogUtil.e("onPause is invoke!!!");
    }

    /**
     *退出当前Activity或者跳转到新Activity时被调用
     */
    @Override
    protected void onStop() {
        super.onStop();

        LogUtil.e("onStop is invoke!!!");
    }

    /**
     *退出当前Activity时被调用,调用之后Activity就结束了
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
}
