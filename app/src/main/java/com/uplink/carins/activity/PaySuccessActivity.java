package com.uplink.carins.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.uplink.carins.R;
import com.uplink.carins.model.api.PayResultQueryResultBean;
import com.uplink.carins.ui.BaseFragmentActivity;

public class PaySuccessActivity extends BaseFragmentActivity implements View.OnClickListener {

    private String TAG = "PaySuccessActivity";

    private Button btn_gopage;
    private Button btn_details;
    private TextView txt_order_sn;
    private TextView txt_order_price;
    private TextView txt_order_paytime;

    private PayResultQueryResultBean payResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paysuccess);
        payResult = (PayResultQueryResultBean) getIntent().getSerializableExtra("dataBean");
        initView();
        initEvent();
        initData();
    }

    public void initView() {
        btn_gopage = (Button) findViewById(R.id.btn_gopage);
        btn_details = (Button) findViewById(R.id.btn_details);

        txt_order_sn = (TextView) findViewById(R.id.btn_details);
        txt_order_price = (TextView) findViewById(R.id.btn_details);
        txt_order_paytime = (TextView) findViewById(R.id.btn_details);
    }

    public void initEvent() {
        btn_gopage.setOnClickListener(this);
        btn_details.setOnClickListener(this);
    }

    public void initData() {
        if (payResult != null) {
            txt_order_sn.setText(payResult.getOrderSn());
            txt_order_price.setText(payResult.getPrice());
            txt_order_paytime.setText(payResult.getPayTime());
        }
    }

    @Override
    public void onClick(View v) {

        Intent l_Intent;

        switch (v.getId()) {
            case R.id.btn_gopage:
                l_Intent = new Intent(PaySuccessActivity.this, MainActivity.class);
                startActivity(l_Intent);
                startMyTask();
                finish();
                break;
            case R.id.btn_details:

                l_Intent = new Intent(PaySuccessActivity.this, OrderListActivity.class);
                l_Intent.putExtra("status", 4);
                startActivity(l_Intent);
                finish();

                break;
        }
    }
}
