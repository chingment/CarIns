package com.uplink.carins.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.uplink.carins.R;
import com.uplink.carins.model.api.CarInfoResultBean;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;

public class NwCarInsConfirmCarInfoActivity extends SwipeBackActivity implements View.OnClickListener {

    private String TAG = "NwCarInsConfirmCarInfoActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;

    private EditText txt_licensePlateNo;
    private EditText txt_vin;
    private TextView txt_modelName;
    private EditText txt_engineNo;
    private EditText txt_ratedPassengerCapacity;
    private EditText txt_firstRegisterDate;
    private CheckBox cb_chgownerType_0;
    private CheckBox cb_chgownerType_1;
    private EditText txt_chgownerDate;
    private CheckBox cb_belong_0;
    private CheckBox cb_belong_1;
    private EditText txt_customers_carowner_name;
    private EditText txt_customers_carowner_certno;

    private CarInfoResultBean carInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nwcarins_confirmcarinfo);

        carInfo = (CarInfoResultBean) getIntent().getSerializableExtra("dataBean");
    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("上传证件");


        cb_chgownerType_0 = (CheckBox) findViewById(R.id.cb_chgownerType_0);
        cb_chgownerType_1 = (CheckBox) findViewById(R.id.cb_chgownerType_1);

        cb_belong_0 = (CheckBox) findViewById(R.id.cb_belong_0);
        cb_belong_1 = (CheckBox) findViewById(R.id.cb_belong_1);
    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);

        cb_chgownerType_0.setOnCheckedChangeListener(cb_chgownerType_CheckListener);
        cb_chgownerType_1.setOnCheckedChangeListener(cb_chgownerType_CheckListener);

        cb_belong_0.setOnCheckedChangeListener(cb_belong_CheckListener);
        cb_belong_1.setOnCheckedChangeListener(cb_belong_CheckListener);
    }

    CheckBox.OnCheckedChangeListener cb_chgownerType_CheckListener = new CheckBox.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked)
                switch (buttonView.getId()) {
                    case R.id.cb_chgownerType_0:
                        cb_chgownerType_1.setChecked(false);
                        break;
                    case R.id.cb_chgownerType_1:
                        cb_chgownerType_0.setChecked(false);
                        break;
                }
        }
    };

    CheckBox.OnCheckedChangeListener cb_belong_CheckListener = new CheckBox.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked)
                switch (buttonView.getId()) {
                    case R.id.cb_belong_0:
                        cb_belong_1.setChecked(false);
                        break;
                    case R.id.cb_belong_1:
                        cb_belong_0.setChecked(false);
                        break;
                }
        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
        }
    }
}
