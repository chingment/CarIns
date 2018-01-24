package com.uplink.carins.activity;

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

public class RegisterActivity extends SwipeBackActivity implements View.OnClickListener {


    String TAG = "RegisterActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;


    private EditText form_register_txt_username;
    private EditText form_register_txt_validcode;
    private EditText form_register_txt_password;

    private Button btn_getvalidcode;
    private Button btn_submit_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initEvent();
    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("注册新用户");

        form_register_txt_username = (EditText) findViewById(R.id.form_register_txt_username);
        form_register_txt_validcode = (EditText) findViewById(R.id.form_register_txt_validcode);
        form_register_txt_password = (EditText) findViewById(R.id.form_register_txt_password);
        btn_submit_register = (Button) findViewById(R.id.btn_submit_register);
        btn_getvalidcode = (Button) findViewById(R.id.btn_getvalidcode);
    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);

        btn_submit_register.setOnClickListener(this);
        btn_getvalidcode.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.btn_getvalidcode:
                getValidCode();
                break;
            case R.id.btn_submit_register:
                register();
                break;
        }
    }

    public void getValidCode() {

        String phone = form_register_txt_username.getText() + "";
        if (StringUtil.isEmpty(phone)) {
            showToast("请输入手机号！");
            return;
        } else if (!StringUtil.isMobileNo(phone)) {
            showToast("请输入正确的手机号！");
            return;
        }
        showProgressDialog(false);
        Map<String, Object> params = new HashMap<>();
        params.put("phone", phone);

        HttpClient.postWithMy(Config.URL.getCreateAccountCode, params, null, new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                removeProgressDialog();
                LogUtil.i("onSuccess===>>" + response);

//                showToast("" + message);

//                GetAddChildAccountCodeResultBean data = GsonUtils.getData(response, GetAddChildAccountCodeResultBean.class);
//                if (data != null) {
//                    if (data.getResult() == 1) {
//                        accountCodeResult = data.getData();
//                        handler.sendEmptyMessage(1021);
//                    } else {
//                        String message = data.getMessage() + "";
//                        showToast("" + message);
//                    }
//                } else {
//                    showToast("验证码获取失败！");
//                }

            }

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
                removeProgressDialog();
                showToast("验证码获取失败！");
            }
        });
    }

    public void register() {


        String username = form_register_txt_username.getText() + "";
        if (StringUtil.isEmpty(username)) {
            showToast("请输入手机号码");
            return;
        }
        String password = form_register_txt_password.getText() + "";
        if (StringUtil.isEmpty(password)) {
            showToast("请输入密码");
            return;
        }

        showProgressDialog(false);
        Map<String, Object> params = new HashMap<>();
        params.put("userName ", username);
        params.put("token", "");
        params.put("validCode ", "");
        params.put("password ", password);
        params.put("deviceId ", "123445");

        HttpClient.postWithMy(Config.URL.accountCreate, params, null, new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                removeProgressDialog();

                LogUtil.i(TAG, "onSuccess====>>>" + response);

                ApiResultBean<Object> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<Object>>() {
                });

                showToast(rt.getMessage());

                if (rt.getResult() == Result.SUCCESS) {
                    setResult(RESULT_OK);
                    finish();
                    //handler.sendEmptyMessage(1021);
                }
            }

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
                LogUtil.e(TAG,"onFailure====>>>" + e.getMessage());
                removeProgressDialog();
                showToast("注册失败");
            }
        });

    }
}
