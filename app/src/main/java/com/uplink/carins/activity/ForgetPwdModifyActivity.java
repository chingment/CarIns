package com.uplink.carins.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.http.HttpClient;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

public class ForgetPwdModifyActivity extends SwipeBackActivity implements View.OnClickListener {

    String TAG = "ForgetPwdModifyActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;

    private TextView form_forgetpwd_modify_txt_username;
    private EditText form_forgetpwd_modify_txt_password;
    private Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd_modify);
        initView();
        initEvent();
    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("修改密码");

        form_forgetpwd_modify_txt_username = (TextView) findViewById(R.id.form_forgetpwd_modify_txt_username);
        form_forgetpwd_modify_txt_password = (EditText) findViewById(R.id.form_forgetpwd_modify_txt_password);
        btn_submit = (Button) findViewById(R.id.btn_submit);
    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.btn_submit:
                forgetPwdModidy();
                break;
        }
    }

    public  void  forgetPwdModidy() {


        String password = form_forgetpwd_modify_txt_password.getText() + "";
        if (StringUtil.isEmpty(password)) {
            showToast("请输入密码");
            return;
        }


        Map<String, Object> params = new HashMap<>();

        params.put("password", password);

        HttpClient.postWithMy(Config.URL.accountCreate, params, null, new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                LogUtil.i(TAG, "onSuccess====>>>" + response);

                ApiResultBean<Object> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<Object>>() {
                });

                showToast(rt.getMessage());

                if (rt.getResult() == Result.SUCCESS) {
                    Intent intent = new Intent(ForgetPwdModifyActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
                LogUtil.e(TAG, "onFailure====>>>" + e.getMessage());
                showToast("验证失败");
            }
        });
    }
}
