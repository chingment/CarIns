package com.uplink.carins.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.appcompat.BuildConfig;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.ImageView;

import com.uplink.carins.R;
import com.uplink.carins.model.api.UserBean;
import com.uplink.carins.ui.BaseFragmentActivity;
import com.uplink.carins.utils.StringUtil;

public class LoginActivity extends BaseFragmentActivity implements View.OnClickListener {


    Button btn_login;//登录按钮
    EditText txt_username;//账户
    EditText txt_password;//密码

    ImageView btn_show_password;//显示密码按钮
    ImageView btn_cancle_username;//显示密码按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();//加载视图控件
        initViewEvent();//加载控件事件
    }

    public void initView() {
        btn_login=(Button)this.findViewById(R.id.btn_login);
        txt_username=(EditText)this.findViewById(R.id.txt_username);
        txt_password=(EditText)this.findViewById(R.id.txt_password);
        btn_cancle_username=(ImageView)this.findViewById(R.id.btn_cancle_username);
        btn_show_password=(ImageView)this.findViewById(R.id.btn_show_password);
    }

    public void initViewEvent() {
        btn_login.setOnClickListener(LoginActivity.this);


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
        switch (view.getId()) {
            case R.id.btn_cancle_username:
                txt_username.setText("");
                break;
            case R.id.btn_show_password:
                String isShow=(String)btn_show_password.getTag();

                if(isShow.equals("hide")) {
                    btn_show_password.setTag("show");
                    btn_show_password.setImageResource(R.drawable.login_showpsw_yes);
                    txt_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else
                {
                    btn_show_password.setTag("hide");
                    btn_show_password.setImageResource(R.drawable.login_showpsw_no);
                    txt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }

                break;
            case R.id.btn_login:

                UserBean user=new UserBean();


                if(txt_username.getText().equals("a")) {
                    user.setId(1081);
                    user.setMerchantId(115);
                }
                else
                {
                    user.setId(1027);
                    user.setMerchantId(20);
                }



                this.getAppContext().setUser(user);

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(intent);
                break;
        }
    }

}
