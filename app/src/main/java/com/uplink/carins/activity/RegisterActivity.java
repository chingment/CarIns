package com.uplink.carins.activity;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.InputType;
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
import com.uplink.carins.model.api.GetCreateAccountCodeResultBean;
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
    ImageView btn_show_password;//显示密码按钮

    private GetCreateAccountCodeResultBean createAccountCodeResult;
    private int daoji = 120;

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
        btn_show_password = (ImageView) this.findViewById(R.id.btn_show_password);
    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);

        btn_submit_register.setOnClickListener(this);
        btn_getvalidcode.setOnClickListener(this);
        btn_show_password.setOnClickListener(this);
    }

    // 倒计时用
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {


            super.handleMessage(msg);
            switch (msg.what) {
                case 1021:
                    LogUtil.i("msg.arg1:"+msg.arg1);
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
            case R.id.btn_show_password:
                String isShow = (String) btn_show_password.getTag();

                if (isShow.equals("hide")) {
                    btn_show_password.setTag("show");
                    btn_show_password.setImageResource(R.drawable.login_showpsw_yes);
                    form_register_txt_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    btn_show_password.setTag("hide");
                    btn_show_password.setImageResource(R.drawable.login_showpsw_no);
                    form_register_txt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }

                break;
        }
    }

    public void getValidCode() {

        String phone = form_register_txt_username.getText() + "";
        if (StringUtil.isEmpty(phone)) {
            showToast("请输入手机号");
            return;
        } else if (!StringUtil.isMobileNo(phone)) {
            showToast("请输入正确的手机号");
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("phone", phone);

        postWithMy(Config.URL.getCreateAccountCode, params, null,true,"正在提交中", new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                ApiResultBean<GetCreateAccountCodeResultBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<GetCreateAccountCodeResultBean>>() {
                });


                showToast(rt.getMessage());

                if (rt.getResult() == Result.SUCCESS) {

                    createAccountCodeResult = rt.getData();
                    Message msg = new Message();
                    msg.what = 1021;
                    msg.arg1 = createAccountCodeResult.getSeconds();
                    handler.sendMessage(msg);
                }

            }
        });
    }

    public void register() {


        String username = form_register_txt_username.getText() + "";
        if (StringUtil.isEmpty(username)) {
            showToast("请输入手机号码");
            return;
        } else if (!StringUtil.isMobileNo(username)) {
            showToast("请输入11位数字手机号码");
            return;
        }

        String validcode = form_register_txt_validcode.getText() + "";


        if (StringUtil.isEmpty(validcode)) {
            showToast("请输入验证码");
            return;
        } else if (createAccountCodeResult == null) {
            showToast("请获取验证码");
            return;
        } else if (!createAccountCodeResult.getValidCode().equals(validcode)) {
            showToast("输入的验证码不正确");
            return;
        }

        String password = form_register_txt_password.getText() + "";

        if (StringUtil.isEmpty(password)) {
            showToast("请输入密码");
            return;
        }


        Map<String, Object> params = new HashMap<>();
        params.put("userName", username);
        params.put("token", createAccountCodeResult.getToken());
        params.put("validCode", createAccountCodeResult.getValidCode());
        params.put("password", password);
        params.put("deviceId", getAppContext().getDeviceId());

        postWithMy(Config.URL.accountCreate, params, null, true,"正在提交中",new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                LogUtil.i(TAG, "onSuccess====>>>" + response);

                ApiResultBean<Object> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<Object>>() {
                });

                showToast(rt.getMessage());

                if (rt.getResult() == Result.SUCCESS) {
                    setResult(RESULT_OK);
                    finish();
                    handler.sendEmptyMessage(1021);
                }
            }
        });

    }
}
