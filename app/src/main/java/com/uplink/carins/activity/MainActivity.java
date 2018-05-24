package com.uplink.carins.activity;


import android.content.OperationApplicationException;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;

import com.squareup.picasso.Picasso;
import com.uplink.carins.R;
import com.uplink.carins.device.N900Device;
import com.uplink.carins.fragment.HomeFragment;
import com.uplink.carins.fragment.MyFragment;
import com.uplink.carins.model.api.PrintDataBean;
import com.uplink.carins.ui.*;
import com.uplink.carins.utils.LogUtil;

import com.zsoft.signala.hubs.HubConnection;
import com.zsoft.signala.hubs.HubOnDataCallback;
import com.zsoft.signala.hubs.IHubProxy;
import com.zsoft.signala.transport.StateBase;
import com.zsoft.signala.transport.longpolling.LongPollingTransport;

import org.json.JSONArray;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.GZIPOutputStream;


public class MainActivity extends BaseFragmentActivity {
    private final static String TAG = "MainActivity";
    private static int currIndex = 0;

    private FragmentManager fragmentManager;

    //Footer Fragment 集合
    private ArrayList<String> fragmentTags = new ArrayList<>(Arrays.asList("HomeFragment", "MyFragment"));

    private RadioGroup footerRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        initView();//加载视图控件
        initVent();//加载控件事件
        showFragment();//展示默认Fragment


        loadTaskData();

        startMyTask();

    }

    public void initView() {

        footerRadioGroup = (RadioGroup) findViewById(R.id.main_footer_radiogroup);

    }

    public void initVent() {

//        btn_1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Go();
//            }
//        });

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


    private HomeFragment homeFragment;

    public HomeFragment getHomeFragment() {
        return homeFragment;
    }

    //构造Fragment
    private Fragment instantFragment(int currIndex) {
        switch (currIndex) {
            case 0:
                homeFragment = new HomeFragment();
                return homeFragment;
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
