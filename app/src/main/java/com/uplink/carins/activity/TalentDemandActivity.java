package com.uplink.carins.activity;


import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.uplink.carins.Own.AppManager;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.http.HttpClient;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.dialog.CustomConfirmDialog;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;


public class TalentDemandActivity extends SwipeBackActivity implements View.OnClickListener{

    String TAG = "TalentDemandActivity";

    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;

    private TextView form_talentdemand_txt_workjob;
    private EditText form_talentdemand_txt_quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talentdemand);
        initView();
        initEvent();
    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("填写人才需求");


        form_talentdemand_txt_workjob = (TextView) findViewById(R.id.form_carclaim_txt_company);
        form_talentdemand_txt_quantity = (EditText) findViewById(R.id.form_carclaim_txt_carplateno);

    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);
    }




    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.btn_submit_talentdemand:
                submitTalentDemand();
                break;
        }
    }

    private void submitTalentDemand() {

        String workJob = form_talentdemand_txt_workjob.getTag() + "";
        String quantity = form_talentdemand_txt_quantity.getText() + "";


        if (StringUtil.isEmptyNotNull(workJob)) {
            showToast("请选择工种");
            return;
        }

        if (StringUtil.isEmptyNotNull(quantity)) {
            showToast("请输入人数");
            return;
        }



        Map<String, Object> params = new HashMap<>();
        params.put("userId", this.getAppContext().getUser().getId());
        params.put("merchantId", this.getAppContext().getUser().getMerchantId()+"");
        params.put("posMachineId","1");
        params.put("workJob", workJob);
        params.put("quantity", quantity);


        HttpClient.postWithMy(Config.URL.submitTalentDemand, params,null, new  HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                LogUtil.d(TAG,"onSuccess====>>>" +response);

                ApiResultBean<Object> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<Object>>() {
                });

                showToast(rt.getMessage());

                if (rt.getResult() == Result.SUCCESS) {
                    showSuccessDialog();
                }

                removeProgressDialog();
            }

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
                LogUtil.e(TAG,"onFailure====>>>" + e.getMessage());
                removeProgressDialog();
                showToast("提交失败");
            }
        });
    }

    private CustomConfirmDialog dialog_Success;
    private void showSuccessDialog() {
        if (dialog_Success == null) {

            dialog_Success = new CustomConfirmDialog(TalentDemandActivity.this, "提交成功",false);

            dialog_Success.getBtnSure().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog_Success.dismiss();

                    Intent l_Intent = new Intent(TalentDemandActivity.this, OrderListActivity.class);
                    l_Intent.putExtra("status", 1);
                    startActivity(l_Intent);
                    AppManager.getAppManager().finishAllActivity();


                }
            });

            dialog_Success.getBtnCancle().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog_Success.dismiss();
                }
            });


        }

        dialog_Success.show();
    }
}