package com.uplink.carins.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.activity.adapter.NwItemParentFieldAdapter;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.NwCarInsBaseInfoBean;
import com.uplink.carins.model.api.NwCarInsCompanyBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.my.MyListView;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.NoDoubleClickUtils;

import java.util.HashMap;
import java.util.Map;

public class OrderDetailsCarInsrueInfoActivity extends SwipeBackActivity implements View.OnClickListener {

    String TAG = "OrderDetailsCarInsrueInfoActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;
    private NwCarInsCompanyBean offerResult;

    private ImageView company_img;
    private TextView company_name;
    private TextView company_offerpremium;
    private MyListView list_offer_parent;
    private LinearLayout layout_company_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails_carinsrueinfo);

        initView();
        initEvent();

        offerResult = (NwCarInsCompanyBean) getIntent().getSerializableExtra("dataBean");

        initData(offerResult);
    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("订单详情");
        company_img = (ImageView) findViewById(R.id.company_img);
        company_name = (TextView) findViewById(R.id.company_name);
        company_offerpremium = (TextView) findViewById(R.id.company_offerpremium);
        list_offer_parent = (MyListView) findViewById(R.id.list_offer_parent);
        layout_company_info = (LinearLayout) findViewById(R.id.layout_company_info);

        list_offer_parent.setFocusable(false);
        list_offer_parent.setClickable(false);
        list_offer_parent.setPressed(false);
        list_offer_parent.setEnabled(false);
    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);
    }

    private void initData(NwCarInsCompanyBean bean) {

        if (bean == null) {

            return;
        }


        CommonUtil.loadImageFromUrl(OrderDetailsCarInsrueInfoActivity.this, company_img, bean.getImgUrl() + "");
        company_name.setText(bean.getName());
        company_offerpremium.setText(bean.getOfferSumPremium() + "");

        NwItemParentFieldAdapter adapter = new NwItemParentFieldAdapter(OrderDetailsCarInsrueInfoActivity.this, bean.getOfferInquirys());

        list_offer_parent.setAdapter(adapter);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
        }
    }


}
