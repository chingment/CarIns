package com.uplink.carins.ui;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.uplink.carins.Own.AppContext;
import com.uplink.carins.Own.AppManager;
import com.uplink.carins.R;
import com.uplink.carins.utils.StringUtil;

/**
 * Created by chingment on 2017/8/23.
 */

public class BaseFragmentActivity extends FragmentActivity {
    private String tag = "BaseFragmentActivity";
    public AppContext app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
        //app = (AppContext) getApplication();

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
}
