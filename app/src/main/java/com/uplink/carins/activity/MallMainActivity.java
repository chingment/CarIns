package com.uplink.carins.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.uplink.carins.R;
import com.uplink.carins.fragment.MallFragment_Cart;
import com.uplink.carins.fragment.MallFragment_Productkind;
import com.uplink.carins.fragment.MyFragment;
import com.uplink.carins.ui.BaseFragmentActivity;
import com.uplink.carins.utils.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class MallMainActivity extends BaseFragmentActivity {
    private final static String TAG = "MallMainActivity";
    private static int fragmentIndex = 0;
    private FragmentManager fragmentManager;
    //Footer Fragment 集合
    private ArrayList<String> fragmentTags = new ArrayList<>(Arrays.asList("MallFragment_Productkind", "MallFragment_Cart"));
    private RadioGroup footerRadioGroup;
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mallmain);

        fragmentManager = getSupportFragmentManager();

        initView();//加载视图控件
        initVent();//加载控件事件

        fragmentIndex = getIntent().getIntExtra("fragmentIndex", 0);


        showFragment();//展示默认Fragment
    }

    public void initView() {
        footerRadioGroup = (RadioGroup) findViewById(R.id.main_footer_radiogroup);
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("商城");
    }

    public void initVent() {
        btnHeaderGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        footerRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.mall_main_footer_bar_productkind:
                        fragmentIndex = 0;
                        break;
                    case R.id.mall_main_footer_bar_cart:
                        fragmentIndex = 1;
                        break;
                    default:
                        fragmentIndex = 0;
                        break;
                }
                showFragment();
            }

        });
    }


    private void showFragment() {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTags.get(fragmentIndex));
        if (fragment == null) {
            fragment = instantFragment(fragmentIndex);
        }
        for (int i = 0; i < fragmentTags.size(); i++) {
            Fragment f = fragmentManager.findFragmentByTag(fragmentTags.get(i));
            if (f != null && f.isAdded()) {
                fragmentTransaction.hide(f);
            }
        }
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.fragment_container, fragment, fragmentTags.get(fragmentIndex));
        }
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();


        for (int i = 0; i < footerRadioGroup.getChildCount(); i++) {
            RadioButton rb = (RadioButton) footerRadioGroup.getChildAt(i);
            if (i == fragmentIndex) {
                rb.setChecked(true);
            }
        }
    }


    private MallFragment_Productkind mallFragment_Productkind;
    private MallFragment_Cart mallFragment_Cart;

    public MallFragment_Productkind getMallFragment_Productkind() {
        return mallFragment_Productkind;
    }

    public MallFragment_Cart getMallFragment_Cart() {
        return mallFragment_Cart;
    }

    //构造Fragment
    private Fragment instantFragment(int currIndex) {
        switch (currIndex) {
            case 0:
                mallFragment_Productkind = new MallFragment_Productkind();
                return mallFragment_Productkind;
            case 1:
                mallFragment_Cart = new MallFragment_Cart();
                return mallFragment_Cart;
            default:
                return null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            LogUtil.i("moveTaskToBack");
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
