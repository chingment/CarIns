package com.uplink.carins.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.uplink.carins.R;

/**
 * <p>
 * Title: CustomDialog
 * </p>
 * <p>
 * Description:自定义Dialog（参数传入Dialog样式文件，Dialog布局文件）
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 *
 * @author archie
 * @version 1.0
 */
public class CustomConfirmDialog extends Dialog {
    private View layoutRes;// 布局文件
    private Context context;
    private Button btnSure;
    private Button btnCancle;
    private TextView txtTips;

    public Button getBtnSure(){
        return this.btnSure;
    }

    public Button getBtnCancle(){
        return this.btnCancle;
    }

    public TextView getTxtTips(){
        return this.txtTips;
    }


    public CustomConfirmDialog(Context context,String tips) {
        super(context,R.style.dialog_style);
        this.context = context;
        this.layoutRes = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);

        txtTips=(TextView)this.layoutRes.findViewById(R.id.dialog_confirm_tips);
        btnSure=(Button)this.layoutRes.findViewById(R.id.dialog_confirm_btn_sure);
        btnCancle=(Button)this.layoutRes.findViewById(R.id.dialog_confirm_btn_cancle);

        txtTips.setText(tips);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (backListener != null) {
            backListener.onBackClick();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.setContentView(layoutRes);
    }

    public OnDialogBackKeyDown backListener;

    public void setOnBackListener(OnDialogBackKeyDown backListener) {
        this.backListener = backListener;
    }

    public interface OnDialogBackKeyDown{
        void onBackClick();
    }

}