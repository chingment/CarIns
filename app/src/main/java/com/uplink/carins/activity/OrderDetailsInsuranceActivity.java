package com.uplink.carins.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.OrderDetailsInsuranceBean;
import com.uplink.carins.model.api.OrderListBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

public class OrderDetailsInsuranceActivity extends SwipeBackActivity implements View.OnClickListener {

    private String TAG = "OrderDetailsInsuranceActivity";

    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;
    private OrderListBean order;
    private LayoutInflater inflater;

    private TextView txt_order_sn;
    private TextView txt_order_statusname;
    private TextView txt_order_remarks;
    private TextView txt_order_submittime;
    //private TextView txt_order_paytime;
    private TextView txt_order_completetime;
    private TextView txt_order_cancletime;
    private LinearLayout layout_submittime;
    private LinearLayout layout_paytime;
    private LinearLayout layout_completetime;
    private LinearLayout layout_cancletime;

    private TextView txt_order_insurancecompanyname;
    private TextView txt_order_producttype;
    private TextView txt_order_productskuname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails_insurance);

        order = (OrderListBean) getIntent().getSerializableExtra("dataBean");

        initView();
        initEvent();
        loadData();
    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("订单详情");

        inflater = LayoutInflater.from(this);
        txt_order_sn = (TextView) findViewById(R.id.txt_order_sn);
        txt_order_statusname = (TextView) findViewById(R.id.txt_order_statusname);
        txt_order_remarks = (TextView) findViewById(R.id.txt_order_remarks);
        txt_order_submittime = (TextView) findViewById(R.id.txt_order_submittime);
        txt_order_completetime = (TextView) findViewById(R.id.txt_order_completetime);
        txt_order_cancletime = (TextView) findViewById(R.id.txt_order_cancletime);
        //txt_order_paytime = (TextView) findViewById(R.id.txt_order_paytime);
        layout_submittime = (LinearLayout) findViewById(R.id.layout_submittime);
        layout_completetime = (LinearLayout) findViewById(R.id.layout_completetime);
        layout_cancletime = (LinearLayout) findViewById(R.id.layout_cancletime);
        layout_paytime = (LinearLayout) findViewById(R.id.layout_paytime);

        txt_order_insurancecompanyname = (TextView) findViewById(R.id.txt_order_insurancecompanyname);
        txt_order_producttype = (TextView) findViewById(R.id.txt_order_producttype);
        txt_order_productskuname = (TextView) findViewById(R.id.txt_order_productskuname);

    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
        }
    }

    private void loadData() {

        Map<String, String> params = new HashMap<>();
        params.put("userId", this.getAppContext().getUser().getId() + "");
        params.put("merchantId", this.getAppContext().getUser().getMerchantId() + "");
        params.put("posMachineId", this.getAppContext().getUser().getPosMachineId() + "");
        params.put("orderId", order.getId() + "");
        params.put("type", order.getType() + "");
        getWithMy(Config.URL.getDetails, params, new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                LogUtil.i(TAG, "onSuccess====>>>" + response);

                ApiResultBean<OrderDetailsInsuranceBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<OrderDetailsInsuranceBean>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {
                    setView(rt.getData());
                }
            }

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
                LogUtil.e(TAG, "onFailure====>>>" + e.getMessage());
            }

        });
    }

    public void setView(OrderDetailsInsuranceBean bean) {

        txt_order_sn.setText(bean.getSn());
        txt_order_statusname.setText(bean.getStatusName());
        txt_order_remarks.setText(bean.getRemarks());
        txt_order_submittime.setText(bean.getSubmitTime());
        //txt_order_paytime.setText(bean.getPayTime());
        txt_order_completetime.setText(bean.getCompleteTime());
        txt_order_cancletime.setText(bean.getCancleTime());

        txt_order_insurancecompanyname.setText(bean.getInsuranceCompanyName());
        txt_order_producttype.setText(bean.getProductTypeName());
        txt_order_productskuname.setText(bean.getProductSkuName());

        switch (bean.getStatus()) {
            case 1:
                layout_submittime.setVisibility(View.VISIBLE);
                break;
            case 2:
                layout_submittime.setVisibility(View.VISIBLE);
                break;
            case 3:
                layout_submittime.setVisibility(View.VISIBLE);
                break;
            case 4:
                layout_submittime.setVisibility(View.VISIBLE);
                //layout_paytime.setVisibility(View.VISIBLE);
                layout_completetime.setVisibility(View.VISIBLE);
                break;
            case 5:
                layout_cancletime.setVisibility(View.VISIBLE);
                break;
        }

    }
}
