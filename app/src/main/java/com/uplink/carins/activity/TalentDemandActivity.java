package com.uplink.carins.activity;


import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.uplink.carins.R;
 import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;



public class TalentDemandActivity extends SwipeBackActivity implements View.OnClickListener{

    String TAG = "TalentDemandActivity";

    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist);
        initView();
        initEvent();
    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("人才需求");


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
            case R.id.form_carclaim_select_company:

                break;
        }
    }
}