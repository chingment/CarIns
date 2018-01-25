package com.uplink.carins.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uplink.carins.R;
import com.uplink.carins.model.api.ConfirmFieldBean;
import com.uplink.carins.model.api.OrderInfoBean;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

public class PayConfirmActivity extends SwipeBackActivity implements View.OnClickListener {

    private String TAG = "PayConfirmActivity";

    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;

    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payconfirm);


        initView();
        initEvent();

        LinearLayout list_confirmfields = (LinearLayout) this.findViewById(R.id.list_confirmfields);
        inflater = LayoutInflater.from(PayConfirmActivity.this);

        OrderInfoBean orderInfo = (OrderInfoBean) getIntent().getSerializableExtra("dataBean");

        List<ConfirmFieldBean> confirmFields = orderInfo.getConfirmField();


        if (confirmFields != null && confirmFields.size() > 0)
            for (int i = 0; i < confirmFields.size(); i++) {
                ConfirmFieldBean confirmField = confirmFields.get(i);
                View view = inflater.inflate(R.layout.item_confirmfield, null);
                TextView item_name = (TextView) view.findViewById(R.id.item_field);
                TextView item_value = (TextView) view.findViewById(R.id.item_value);
                item_name.setText(confirmField.getField() + "");
                item_value.setText(confirmField.getValue() + "");
                list_confirmfields.addView(view);
            }


    }

    public void initView() {

        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("支付信息");

    }

    public void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);


    }

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
}
