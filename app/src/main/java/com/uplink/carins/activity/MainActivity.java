package com.uplink.carins.activity;


import android.content.OperationApplicationException;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.*;

import com.squareup.picasso.Picasso;
import com.uplink.carins.R;
import com.uplink.carins.fragment.HomeFragment;
import com.uplink.carins.fragment.MyFragment;
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


public class MainActivity extends BaseFragmentActivity {
    private final static String TAG = "MainActivity";
    private static int currIndex = 0;

    private FragmentManager fragmentManager;

    //Footer Fragment 集合
    private ArrayList<String> fragmentTags = new ArrayList<>(Arrays.asList("HomeFragment", "MyFragment"));

    private RadioGroup footerRadioGroup;


    private final static String HUB_URL="http://112.74.179.185:8084/signalr/hubs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        initView();//加载视图控件
        initVent();//加载控件事件
        showFragment();//展示默认Fragment

        beginConnect();
    }

    /**
     * hub链接
     */
    private HubConnection conn=new HubConnection(HUB_URL, this, new LongPollingTransport()) {
        @Override
        public void OnError(Exception exception) {
            Log.i(TAG, "OnError=" + exception.getMessage());
        }
        @Override
        public void OnMessage(String message) {
            Log.i(TAG, "message=" + message);
        }
        @Override
        public void OnStateChanged(StateBase oldState, StateBase newState) {
            Log.i(TAG, "OnStateChanged=" + oldState.getState() + " -> " + newState.getState());
        }
    };

    private IHubProxy hub = null;
    /**
     * 开启推送服务 panderman 2013-10-25
     */
    private void beginConnect(){
        try {
            hub=conn.CreateHubProxy("ChatHub");
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
        hub.On("broadcastMessage", new HubOnDataCallback()
        {
            @Override
            public void OnReceived(JSONArray args) {
                Log.i(TAG, "args========================" + args.toString());

//                for(int i=0; i<args.length(); i++)
//                {
//                    LogUtil.d(TAG, "args========================" + args.opt(i).toString());
//
//                }
            }
        });
        conn.Start();
    }

    public void initView() {

        footerRadioGroup=(RadioGroup) findViewById(R.id.main_footer_radiogroup);

    }

    public void initVent() {

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

    //构造Fragment
    private Fragment instantFragment(int currIndex) {
        switch (currIndex) {
            case 0:
                return new HomeFragment();
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
