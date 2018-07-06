package com.uplink.carins.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.activity.adapter.OrderDetailsProductSkuAdapter;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.OrderDetailsServiceFeeBean;
import com.uplink.carins.model.api.OrderDetailsShoppingBean;
import com.uplink.carins.model.api.OrderListBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class OrderDetailsShoppingActivity extends SwipeBackActivity implements View.OnClickListener {

    private String TAG = "OrderDetailsShoppingActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;
    private OrderListBean order;

    private TextView txt_order_sn;
    private TextView txt_order_statusname;
    private TextView txt_order_remarks;
    private TextView txt_order_submittime;
    private TextView txt_order_paytime;
    private TextView txt_order_completetime;
    private TextView txt_order_cancletime;
    private TextView txt_order_price;


    private TextView txt_recipientAddress_recipient;
    private TextView txt_recipientAddress_address;
    private TextView txt_recipientAddress_areaName;
    private TextView txt_recipientAddress_phoneNumber;


    private EditText edit_recipientAddress_recipient;
    private EditText edit_recipientAddress_address;
    private TextView edit_recipientAddress_areaName;
    private EditText edit_recipientAddress_phoneNumber;


    private LinearLayout layout_submittime;
    private LinearLayout layout_paytime;
    private LinearLayout layout_completetime;
    private LinearLayout layout_cancletime;

    private ListView list_skus;

    private OrderDetailsShoppingBean orderDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails_shopping);
        order = (OrderListBean) getIntent().getSerializableExtra("dataBean");

        initView();
        initEvent();
        loadData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.btn_printer:

                break;
        }
    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("订单详情");


        txt_order_sn = (TextView) findViewById(R.id.txt_order_sn);
        txt_order_statusname = (TextView) findViewById(R.id.txt_order_statusname);
        txt_order_price = (TextView) findViewById(R.id.txt_order_price);

        txt_order_remarks = (TextView) findViewById(R.id.txt_order_remarks);
        txt_order_submittime = (TextView) findViewById(R.id.txt_order_submittime);
        txt_order_paytime = (TextView) findViewById(R.id.txt_order_paytime);
        txt_order_completetime = (TextView) findViewById(R.id.txt_order_completetime);
        txt_order_cancletime = (TextView) findViewById(R.id.txt_order_cancletime);


        txt_recipientAddress_recipient = (TextView) findViewById(R.id.txt_recipientAddress_recipient);
        txt_recipientAddress_address = (TextView) findViewById(R.id.txt_recipientAddress_address);
        txt_recipientAddress_areaName = (TextView) findViewById(R.id.txt_recipientAddress_areaName);
        txt_recipientAddress_phoneNumber = (TextView) findViewById(R.id.txt_recipientAddress_phoneNumber);


        edit_recipientAddress_recipient = (EditText) findViewById(R.id.edit_recipientAddress_recipient);
        edit_recipientAddress_address = (EditText) findViewById(R.id.edit_recipientAddress_address);
        edit_recipientAddress_areaName = (TextView) findViewById(R.id.edit_recipientAddress_areaName);
        edit_recipientAddress_phoneNumber = (EditText) findViewById(R.id.edit_recipientAddress_phoneNumber);


        layout_submittime = (LinearLayout) findViewById(R.id.layout_submittime);
        layout_paytime = (LinearLayout) findViewById(R.id.layout_paytime);
        layout_completetime = (LinearLayout) findViewById(R.id.layout_completetime);
        layout_cancletime = (LinearLayout) findViewById(R.id.layout_cancletime);

        list_skus = (ListView) findViewById(R.id.list_skus);
    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);

    }

    public void setView(OrderDetailsShoppingBean bean) {

        txt_order_sn.setText(bean.getSn());
        txt_order_statusname.setText(bean.getStatusName());
        txt_order_remarks.setText(bean.getRemarks());
        txt_order_submittime.setText(bean.getSubmitTime());
        txt_order_paytime.setText(bean.getPayTime());
        txt_order_completetime.setText(bean.getCompleteTime());
        txt_order_cancletime.setText(bean.getCancleTime());

        txt_order_price.setText(bean.getPrice());


        txt_recipientAddress_recipient.setText(bean.getRecipientAddress().getRecipient());
        txt_recipientAddress_address.setText(bean.getRecipientAddress().getAddress());
        txt_recipientAddress_areaName.setText(bean.getRecipientAddress().getAreaName());
        txt_recipientAddress_phoneNumber.setText(bean.getRecipientAddress().getPhoneNumber());

        edit_recipientAddress_recipient.setText(bean.getRecipientAddress().getRecipient());
        edit_recipientAddress_address.setText(bean.getRecipientAddress().getAddress());
        edit_recipientAddress_areaName.setText(bean.getRecipientAddress().getAreaName());
        edit_recipientAddress_phoneNumber.setText(bean.getRecipientAddress().getPhoneNumber());

        switch (bean.getStatus()) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                txt_recipientAddress_recipient.setVisibility(View.GONE);
                txt_recipientAddress_address.setVisibility(View.GONE);
                txt_recipientAddress_areaName.setVisibility(View.GONE);
                txt_recipientAddress_phoneNumber.setVisibility(View.GONE);

                edit_recipientAddress_recipient.setVisibility(View.VISIBLE);
                edit_recipientAddress_address.setVisibility(View.VISIBLE);
                edit_recipientAddress_areaName.setVisibility(View.VISIBLE);
                edit_recipientAddress_phoneNumber.setVisibility(View.VISIBLE);
                break;
            case 4:
                layout_paytime.setVisibility(View.VISIBLE);
                layout_completetime.setVisibility(View.VISIBLE);
                break;
            case 5:
                layout_cancletime.setVisibility(View.VISIBLE);
                break;
        }

        OrderDetailsProductSkuAdapter adapter = new OrderDetailsProductSkuAdapter(OrderDetailsShoppingActivity.this, bean.getSkus());
        list_skus.setAdapter(adapter);
    }

    private void loadData() {

        Map<String, String> params = new HashMap<>();
        params.put("userId", this.getAppContext().getUser().getId() + "");
        params.put("merchantId", this.getAppContext().getUser().getMerchantId() + "");
        params.put("posMachineId", this.getAppContext().getUser().getPosMachineId() + "");
        params.put("orderId", order.getId() + "");
        params.put("type", order.getType() + "");
        getWithMy(Config.URL.getDetails, params, false, "", new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                ApiResultBean<OrderDetailsShoppingBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<OrderDetailsShoppingBean>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {
                    orderDetails = rt.getData();
                    setView(orderDetails);
                }
            }
        });
    }
}
