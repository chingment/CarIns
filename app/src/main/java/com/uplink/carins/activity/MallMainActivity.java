package com.uplink.carins.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.uplink.carins.R;
import com.uplink.carins.fragment.MallPrdCategoryFragment;
import com.uplink.carins.fragment.MyFragment;
import com.uplink.carins.ui.BaseFragmentActivity;
import com.uplink.carins.utils.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class MallMainActivity extends BaseFragmentActivity {
    private final static String TAG = "MallMainActivity";
    private static int currIndex = 0;
    private FragmentManager fragmentManager;
    //Footer Fragment 集合
    private ArrayList<String> fragmentTags = new ArrayList<>(Arrays.asList("MallPrdCategoryFragment", "MyFragment"));
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
                    case R.id.main_footbar_home:
                        currIndex = 0;
                        break;
                    case R.id.main_footbar_my:
                        currIndex = 1;
                        break;
                    default:
                        currIndex = 0;
                        break;
                }
                showFragment();
            }

        });
    }


    private void showFragment() {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTags.get(currIndex));
        if (fragment == null) {
            fragment = instantFragment(currIndex);
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
            fragmentTransaction.add(R.id.fragment_container, fragment, fragmentTags.get(currIndex));
        }
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }


    private MallPrdCategoryFragment mallPrdCategoryFragment;

    public MallPrdCategoryFragment getMallPrdCategoryFragment() {
        return mallPrdCategoryFragment;
    }

    //构造Fragment
    private Fragment instantFragment(int currIndex) {
        switch (currIndex) {
            case 0:
                mallPrdCategoryFragment = new MallPrdCategoryFragment();
                return mallPrdCategoryFragment;
            case 1:
                return new MyFragment();
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
