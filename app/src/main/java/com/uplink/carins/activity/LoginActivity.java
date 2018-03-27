package com.uplink.carins.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.appcompat.BuildConfig;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.newland.me.ConnUtils;
import com.newland.me.DeviceManager;
import com.newland.me.K21Driver;
import com.newland.mtype.ConnectionCloseEvent;
import com.newland.mtype.Device;
import com.newland.mtype.DeviceDriver;
import com.newland.mtype.ModuleType;
import com.newland.mtype.event.DeviceEventListener;
import com.newland.mtype.module.common.printer.Printer;
import com.newland.mtype.module.common.printer.PrinterResult;
import com.newland.mtype.module.common.printer.PrinterStatus;
import com.newland.mtypex.nseries.NSConnV100ConnParams;
import com.newland.mtypex.nseries3.NS3ConnParams;
import com.uplink.carins.Own.AppCacheManager;
import com.uplink.carins.Own.AppContext;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.device.DeviceCloseListener;
import com.uplink.carins.device.N900Device;
import com.uplink.carins.http.HttpClient;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.LoginResultBean;
import com.uplink.carins.model.api.PayConfirmBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.model.api.UserBean;
import com.uplink.carins.ui.BaseFragmentActivity;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.IpAdressUtil;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.StringUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Request;


public class LoginActivity extends BaseFragmentActivity implements View.OnClickListener {
    String TAG = "LoginActivity";

    ImageView logo_login;//logo
    Button btn_login;//登录按钮
    EditText txt_username;//账户
    EditText txt_password;//密码

    ImageView btn_show_password;//显示密码按钮
    ImageView btn_cancle_username;//显示密码按钮

    TextView btn_register;
    TextView btn_forgetpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        initView();//加载视图控件
        initEvent();//加载控件事件

        if (this.getAppContext().getUser() != null) {
            if (this.getAppContext().getUser().getStatus() == 1) {
                AppCacheManager.setLastUpdateTime("");
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }

        String lastUsername = AppCacheManager.getLastUserName();


        if (lastUsername != null) {
            txt_username.setText(lastUsername);
        }

    }

    public void initView() {
        btn_login = (Button) this.findViewById(R.id.btn_login);
        txt_username = (EditText) this.findViewById(R.id.txt_username);
        txt_password = (EditText) this.findViewById(R.id.txt_password);
        btn_cancle_username = (ImageView) this.findViewById(R.id.btn_cancle_username);
        btn_show_password = (ImageView) this.findViewById(R.id.btn_show_password);
        logo_login = (ImageView) this.findViewById(R.id.logo_login);
        btn_register = (TextView) this.findViewById(R.id.btn_register);
        btn_forgetpwd = (TextView) this.findViewById(R.id.btn_forgetpwd);

    }

    public void initEvent() {
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_forgetpwd.setOnClickListener(this);

        txt_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (StringUtil.isEmpty(editable + "")) {
                    btn_cancle_username.setVisibility(View.GONE);

                } else {
                    btn_cancle_username.setVisibility(View.VISIBLE);

                }
            }
        });

        btn_cancle_username.setOnClickListener(this);
        btn_show_password.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

//        splits {
//            abi {
//                enable true
//                reset()
//                include 'x86', 'armeabi-v7a'
//                universalApk true
//            }
//        }

        Intent intent;

        switch (view.getId()) {
            case R.id.btn_cancle_username:
                txt_username.setText("");
                break;
            case R.id.btn_show_password:
                String isShow = (String) btn_show_password.getTag();

                if (isShow.equals("hide")) {
                    btn_show_password.setTag("show");
                    btn_show_password.setImageResource(R.drawable.login_showpsw_yes);
                    txt_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    btn_show_password.setTag("hide");
                    btn_show_password.setImageResource(R.drawable.login_showpsw_no);
                    txt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }

                break;
            case R.id.btn_login:

                submitLogin();

                break;
            case R.id.btn_register:
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
                break;
            case R.id.btn_forgetpwd:
                intent = new Intent(LoginActivity.this, ForgetPwdCheckUsernameActivity.class);
                LoginActivity.this.startActivity(intent);
                break;
        }
    }



    private void controlKeyboardLayout(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                //若不可视区域高度大于100，则键盘显示
                if (rootInvisibleHeight > 100) {
                    int[] location = new int[2];
                    //获取scrollToView在窗体的坐标
                    scrollToView.getLocationInWindow(location);
                    //计算root滚动高度，使scrollToView在可见区域
                    int srollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
                    root.scrollTo(0, srollHeight);
                } else {
                    //键盘隐藏
                    root.scrollTo(0, 0);
                }
            }
        });
    }


    private void submitLogin() {

        String username = txt_username.getText() + "";
        if (StringUtil.isEmpty(username)) {
            showToast("用户名为空");
            return;
        }
        String password = txt_password.getText() + "";
        if (StringUtil.isEmpty(password)) {
            showToast("密码为空");
            return;
        }


        Map<String, Object> param = new HashMap<>();
        param.put("userName", username);
        param.put("password", password);
        param.put("deviceId", appContext.getDeviceId());//861097039013879


        postWithMy(Config.URL.login, param, null, new HttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                LogUtil.i(TAG, "onSuccess===>>" + response);

                ApiResultBean<LoginResultBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<LoginResultBean>>() {
                });

                showToast(rt.getMessage());

                if (rt.getResult() == Result.SUCCESS) {

                    LoginResultBean d = rt.getData();

                    AppCacheManager.setLastUserName(d.getUserName());


                    UserBean user = new UserBean();

                    user.setId(rt.getData().getUserId());
                    user.setMerchantId(rt.getData().getMerchantId());
                    user.setPosMachineId(rt.getData().getPosMachineId());
                    user.setStatus(rt.getData().getStatus());

                    getAppContext().setUser(user);

                    Intent intent = null;
                    switch (d.getStatus()) {
                        case 1:

                            intent = new Intent(LoginActivity.this, MainActivity.class);

                            break;
                        case 2:
                        case 3:

                            if (d.getOrderInfo() == null) {
                                showToast("登陆异常，错误代码:EX100001");
                                return;
                            }

                            intent = new Intent(LoginActivity.this, PayConfirmActivity.class);
                            Bundle b = new Bundle();
                            b.putSerializable("dataBean", d.getOrderInfo());
                            intent.putExtras(b);

                            break;
                    }

                    if (intent != null) {
                        startActivity(intent);
                    }

                }

            }

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
                LogUtil.e(TAG, e);
                showToast("登陆失败");

            }

        });
    }

}
