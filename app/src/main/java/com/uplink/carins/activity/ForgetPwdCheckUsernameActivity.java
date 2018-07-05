package com.uplink.carins.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.GetForgetPwdCheckUsernameCodeResultBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

public class ForgetPwdCheckUsernameActivity extends SwipeBackActivity implements View.OnClickListener {

    String TAG = "ForgetPwdCheckUsernameActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;


    private EditText form_forgetpwd_checkusername_txt_username;
    private EditText form_forgetpwd_checkusername_txt_validcode;
    private Button btn_getvalidcode;
    private Button btn_submit_forgetpwd_checkusername;
    private GetForgetPwdCheckUsernameCodeResultBean forgetPwdCheckUsernameResult;
    private int daoji = 120;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd_checkusername);
        initView();
        initEvent();
    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("验证账号");

        form_forgetpwd_checkusername_txt_username = (EditText) findViewById(R.id.form_forgetpwd_checkusername_txt_username);
        form_forgetpwd_checkusername_txt_validcode = (EditText) findViewById(R.id.form_forgetpwd_checkusername_txt_validcode);
        btn_submit_forgetpwd_checkusername = (Button) findViewById(R.id.btn_submit_forgetpwd_checkusername);
        btn_getvalidcode = (Button) findViewById(R.id.btn_getvalidcode);

    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);

        btn_submit_forgetpwd_checkusername.setOnClickListener(this);
        btn_getvalidcode.setOnClickListener(this);
    }


    // 倒计时用
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {


            super.handleMessage(msg);
            switch (msg.what) {
                case 1021:

                    daoji = msg.arg1;
                    btn_getvalidcode.setText(daoji + "s重新获取");
                    handler.sendEmptyMessageDelayed(1022, 1000);
                    btn_getvalidcode.setEnabled(false);
                    btn_getvalidcode.setBackgroundResource(R.drawable.corn_c1c1c1);
                    break;
                case 1022:
                    if (daoji > 0) {
                        daoji--;
                        btn_getvalidcode.setText(daoji + "s重新获取");
                        handler.sendEmptyMessageDelayed(1022, 1000);
                    } else {
                        btn_getvalidcode.setText("获取验证码");
                        btn_getvalidcode.setEnabled(true);
                        btn_getvalidcode.setBackgroundResource(R.drawable.widget_btn_green);
                    }
                    break;
            }
        }
    };

    public void getValidCode() {

        String phone = form_forgetpwd_checkusername_txt_username.getText() + "";
        if (StringUtil.isEmpty(phone)) {
            showToast("请输入手机号");
            return;
        } else if (!StringUtil.isMobileNo(phone)) {
            showToast("请输入正确的手机号");
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("phone", phone);

        postWithMy(Config.URL.getGetForgetPwdCode, params, null,true,"正在提交中", new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                ApiResultBean<GetForgetPwdCheckUsernameCodeResultBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<GetForgetPwdCheckUsernameCodeResultBean>>() {
                });


                showToast(rt.getMessage());

                if (rt.getResult() == Result.SUCCESS) {

                    forgetPwdCheckUsernameResult = rt.getData();
                    Message msg = new Message();
                    msg.what = 1021;
                    msg.arg1 = forgetPwdCheckUsernameResult.getSeconds();
                    handler.sendMessage(msg);
                }

            }

        });
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
            case R.id.btn_submit_forgetpwd_checkusername:
                forgetpwdCheckUsername();
                break;
        }
    }

    public void  forgetpwdCheckUsername() {

        String username = form_forgetpwd_checkusername_txt_username.getText() + "";
        if (StringUtil.isEmpty(username)) {
            showToast("请输入手机号码");
            return;
        } else if (!StringUtil.isMobileNo(username)) {
            showToast("请输入11位数字手机号码");
            return;
        }

        String validcode = form_forgetpwd_checkusername_txt_validcode.getText() + "";

        if (StringUtil.isEmpty(validcode)) {
            showToast("请输入验证码");
            return;
        } else if (forgetPwdCheckUsernameResult == null) {
            showToast("请获取验证码");
            return;
        } else if (!forgetPwdCheckUsernameResult.getValidCode().equals(validcode)) {
            showToast("输入的验证码不正确");
            return;
        }


        Intent intent = new Intent(ForgetPwdCheckUsernameActivity.this, ForgetPwdModifyActivity.class);

        Bundle b = new Bundle();

        //forgetPwdCheckUsernameResult=new GetForgetPwdCheckUsernameCodeResultBean();

        //forgetPwdCheckUsernameResult.setUserName("15989287032");
        //forgetPwdCheckUsernameResult.setPhone("15989287032");
        //forgetPwdCheckUsernameResult.setSeconds(120);
        //forgetPwdCheckUsernameResult.setToken("e0d909f6-b4f6-474d-8bae-a83d5fd765c7");
        //forgetPwdCheckUsernameResult.setValidCode("565165");

        b.putSerializable("dataBean", forgetPwdCheckUsernameResult);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }
}
