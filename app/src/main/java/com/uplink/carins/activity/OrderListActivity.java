package com.uplink.carins.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.uplink.carins.R;
import com.uplink.carins.activity.adapter.MyViewPagerAdapter;
import com.uplink.carins.fragment.OrderListFragment;
import com.uplink.carins.model.api.CarInsPlanBean;
import com.uplink.carins.ui.my.MyViewPager;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.AnimationUtil;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class OrderListActivity extends SwipeBackActivity implements View.OnClickListener{

    private String TAG = "OrderListActivity";

    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;

    private RadioGroup form_orderlist_rb_status;//
    private List<Fragment> form_orderlist_fragments = new ArrayList<Fragment>();
    private MyViewPager form_orderlist_viewpager;
    private MyViewPagerAdapter form_orderlist_viewpager_Adapter;

    public int currentStatus=0;//当前tab 状态
    public int statusItemCount=0;//状态的类目数量
    public boolean isFirstLoad = true;//是否第一次加载
    public HashMap<Integer, Boolean> refreshFlag = null;//刷新标识

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist);

        currentStatus=getIntent().getIntExtra("status",0);

        LogUtil.i("当前状态:" + currentStatus);

        initView();
        initViewPager();
        initEvent();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){

            String checkedIndex = form_orderlist_rb_status.getTag().toString();
            if (checkedIndex.equals("0")) {
                CommonUtil.setRadioGroupCheckedByStringTag(form_orderlist_rb_status, String.valueOf(currentStatus));
            }

        }
    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("我的订单");

        form_orderlist_rb_status = (RadioGroup) findViewById(R.id.form_orderlist_rb_status);
        form_orderlist_viewpager= (MyViewPager) findViewById(R.id.form_orderlist_viewpager);
    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);

        form_orderlist_rb_status.setOnCheckedChangeListener(rb_status_ChangeListener);
    }

    private void initViewPager() {

        statusItemCount=form_orderlist_rb_status.getChildCount();

        LogUtil.i("tab item count:"+statusItemCount);

        form_orderlist_fragments = new ArrayList<>();

        refreshFlag = new HashMap<>();

        for (int i=0;i<statusItemCount;i++)
        {
            refreshFlag.put(i, true);
            form_orderlist_fragments.add(OrderListFragment.newInstance(i));
        }


        form_orderlist_viewpager_Adapter = new MyViewPagerAdapter(getSupportFragmentManager(), form_orderlist_fragments);
        form_orderlist_viewpager.setPagingEnabled(false);
        form_orderlist_viewpager.setAdapter(form_orderlist_viewpager_Adapter);
        form_orderlist_viewpager.setCurrentItem(0, false);
        form_orderlist_viewpager.setOffscreenPageLimit(5);
    }

    RadioGroup.OnCheckedChangeListener rb_status_ChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {


            RadioButton currentCheckedRadio = (RadioButton) findViewById(group.getCheckedRadioButtonId());

            int tabCurrentSelectPisition = Integer.parseInt(currentCheckedRadio.getTag().toString());

            form_orderlist_viewpager.setCurrentItem(tabCurrentSelectPisition,false);

            LogUtil.i("tab当前选择:" + tabCurrentSelectPisition);
            LogUtil.i("tab当前选择的宽度:" + currentCheckedRadio.getWidth());

            AnimationUtil.SetTab1ImageSlide(group,OrderListActivity.this);

            group.setTag(tabCurrentSelectPisition);


        }
    };


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.form_carclaim_select_company:

                break;
        }
    }


    public void setRefreshALL() {
        if (refreshFlag != null) {
            for (int i=0;i<statusItemCount;i++){
                refreshFlag.put(i, true);
            }
        }
    }

    public void setRefreshOne(int index, boolean flag) {
        if (refreshFlag != null)
            refreshFlag.put(index, flag);
    }

    public boolean getRefreshFlag(int index) {
        if (refreshFlag == null) {
            return false;
        } else if (refreshFlag.size() > index) {
            return refreshFlag.get(index);
        } else {
            return false;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            isFirstLoad = true;
            setRefreshALL();
        }
    }
}
