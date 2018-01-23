package com.uplink.carins.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
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

import com.uplink.carins.R;
import com.uplink.carins.model.api.UserBean;
import com.uplink.carins.ui.BaseFragmentActivity;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.StringUtil;

public class LoginActivity extends BaseFragmentActivity implements View.OnClickListener {


    ImageView logo_login;//logo
    Button btn_login;//登录按钮
    EditText txt_username;//账户
    EditText txt_password;//密码

    ImageView btn_show_password;//显示密码按钮
    ImageView btn_cancle_username;//显示密码按钮

    TextView btn_register;
    TextView btn_forgetpwd;

    LinearLayout mRoot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setContentView(R.layout.activity_login);
        initView();//加载视图控件
        initViewEvent();//加载控件事件

        //mRoot = (LinearLayout) findViewById(R.id.main);
        //controlKeyboardLayout(mRoot, btn_login);

        if(this.getAppContext().getUser()!=null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            LoginActivity.this.startActivity(intent);
        }
    }

    public void initView() {
        btn_login = (Button) this.findViewById(R.id.btn_login);
        txt_username = (EditText) this.findViewById(R.id.txt_username);
        txt_password = (EditText) this.findViewById(R.id.txt_password);
        btn_cancle_username = (ImageView) this.findViewById(R.id.btn_cancle_username);
        btn_show_password = (ImageView) this.findViewById(R.id.btn_show_password);
        logo_login = (ImageView) this.findViewById(R.id.logo_login);
        btn_register= (TextView) this.findViewById(R.id.btn_register);
        btn_forgetpwd= (TextView) this.findViewById(R.id.btn_forgetpwd);
    }

    public void initViewEvent() {
        btn_login.setOnClickListener(LoginActivity.this);
        btn_register.setOnClickListener(LoginActivity.this);
        btn_forgetpwd.setOnClickListener(LoginActivity.this);

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

        btn_cancle_username.setOnClickListener(LoginActivity.this);
        btn_show_password.setOnClickListener(LoginActivity.this);
    }

    @Override
    public void onClick(View view) {


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

                UserBean user = new UserBean();


                if (txt_username.getText().toString().equals("a")) {
                    user.setId(1081);
                    user.setMerchantId(115);
                } else {
                    user.setId(1027);
                    user.setMerchantId(20);
                }


                this.getAppContext().setUser(user);

                intent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(intent);

                //Intent intent = new Intent(LoginActivity.this, SelectImageActivity.class);
                //intent.putExtra(SelectImageActivity.EXTRA, SelectImageActivity.OPENALBUM);
                //startActivityForResult(intent, SelectImageActivity.OPENALBUM);

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == SelectImageActivity.OPENCAMERA
                    || requestCode == SelectImageActivity.OPENALBUM) {
                String url = data.getStringExtra(SelectImageActivity.IMAGEURL);
                logo_login.setImageBitmap(BitmapFactory.decodeFile(url));
            }
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





}
