package com.uplink.carins.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.uplink.carins.R;
import com.uplink.carins.model.api.OrderListBean;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;

public class OrderDetailsTalentDemandActivity extends SwipeBackActivity implements View.OnClickListener{

    private String TAG = "OrderDetailsCarInsrueActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;
    private OrderListBean order;

    private TextView txt_order_sn;
    private TextView txt_order_statusname;
    private TextView txt_order_workjob;
    private TextView txt_order_quantity;
    private TextView txt_order_remarks;

    private TextView txt_order_submittime;
    private TextView txt_order_paytime;
    private TextView txt_order_completetime;
    private TextView txt_order_cancletime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails_talentdemand);
        initView();
        initEvent();
    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("订单详情");


        txt_order_sn = (TextView) findViewById(R.id.txt_order_sn);
        txt_order_statusname = (TextView) findViewById(R.id.txt_order_statusname);

        txt_order_workjob = (TextView) findViewById(R.id.txt_order_workjob);
        txt_order_quantity = (TextView) findViewById(R.id.txt_order_quantity);

        txt_order_remarks = (TextView) findViewById(R.id.txt_order_remarks);



        txt_order_submittime = (TextView) findViewById(R.id.txt_order_submittime);
        txt_order_paytime = (TextView) findViewById(R.id.txt_order_paytime);
        txt_order_completetime = (TextView) findViewById(R.id.txt_order_completetime);
        txt_order_cancletime = (TextView) findViewById(R.id.txt_order_cancletime);


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
}
