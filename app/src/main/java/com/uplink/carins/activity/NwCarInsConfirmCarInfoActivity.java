package com.uplink.carins.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uplink.carins.R;
import com.uplink.carins.model.api.CarInfoResultBean;
import com.uplink.carins.model.api.CarInsCarModelInfoBean;
import com.uplink.carins.model.api.CarInsGetCarModelInfoPmsBean;
import com.uplink.carins.ui.dataPicker.CustomDatePicker;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.CarKeyboardUtil;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    private LinearLayout layout_customers_carowner_certno;
    private LinearLayout layout_chgownerDate;

    private Button btn_GoGetCarModel;
    private Button btn_submit;

    private CarInfoResultBean carInfo;

    private CustomDatePicker chgownerDatePicker;
    private CustomDatePicker firstRegisterDatePicker;

    private CarKeyboardUtil keyboardUtil;

    private TextView txt_carmodelinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nwcarins_confirmcarinfo);

        carInfo = (CarInfoResultBean) getIntent().getSerializableExtra("dataBean");

        initView();
        initEvent();
        initData();


//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
//        String now = sdf.format(new Date());
        firstRegisterDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                txt_firstRegisterDate.setText(time.split(" ")[0]);
            }
        }, "1960-01-01 00:00", "2099-12-31 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行

        firstRegisterDatePicker.showSpecificTime(false); // 不显示时和分
        firstRegisterDatePicker.setIsLoop(false); // 不允许循环滚动

        chgownerDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                txt_chgownerDate.setText(time.split(" ")[0]);
            }
        }, "1960-01-01 00:00", "2099-12-31 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行

        chgownerDatePicker.showSpecificTime(false); // 不显示时和分
        chgownerDatePicker.setIsLoop(false); // 不允许循环滚动
    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("确认信息");

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_GoGetCarModel = (Button) findViewById(R.id.btn_GoGetCarModel);

        txt_licensePlateNo = (EditText) findViewById(R.id.txt_licensePlateNo);
        keyboardUtil = new CarKeyboardUtil(this, txt_licensePlateNo);
        txt_vin = (EditText) findViewById(R.id.txt_vin);
        txt_modelName = (TextView) findViewById(R.id.txt_modelName);
        txt_engineNo = (EditText) findViewById(R.id.txt_engineNo);
        txt_ratedPassengerCapacity = (EditText) findViewById(R.id.txt_ratedPassengerCapacity);
        txt_firstRegisterDate = (EditText) findViewById(R.id.txt_firstRegisterDate);
        txt_firstRegisterDate.setCursorVisible(false);
        txt_firstRegisterDate.setFocusable(false);
        txt_firstRegisterDate.setFocusableInTouchMode(false);
        txt_chgownerDate = (EditText) findViewById(R.id.txt_chgownerDate);
        txt_chgownerDate.setCursorVisible(false);
        txt_chgownerDate.setFocusable(false);
        txt_chgownerDate.setFocusableInTouchMode(false);
        txt_customers_carowner_name = (EditText) findViewById(R.id.txt_customers_carowner_name);
        txt_customers_carowner_certno = (EditText) findViewById(R.id.txt_customers_carowner_certno);


        cb_chgownerType_0 = (CheckBox) findViewById(R.id.cb_chgownerType_0);
        cb_chgownerType_1 = (CheckBox) findViewById(R.id.cb_chgownerType_1);

        cb_belong_0 = (CheckBox) findViewById(R.id.cb_belong_0);
        cb_belong_1 = (CheckBox) findViewById(R.id.cb_belong_1);

        layout_customers_carowner_certno = (LinearLayout) findViewById(R.id.layout_customers_carowner_certno);
        layout_chgownerDate = (LinearLayout) findViewById(R.id.layout_chgownerDate);

        txt_carmodelinfo= (TextView) findViewById(R.id.txt_carmodelinfo);
    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        btn_GoGetCarModel.setOnClickListener(this);
        txt_chgownerDate.setOnClickListener(this);
        txt_firstRegisterDate.setOnClickListener(this);
        cb_chgownerType_0.setOnCheckedChangeListener(cb_chgownerType_CheckListener);
        cb_chgownerType_1.setOnCheckedChangeListener(cb_chgownerType_CheckListener);

        cb_belong_0.setOnCheckedChangeListener(cb_belong_CheckListener);
        cb_belong_1.setOnCheckedChangeListener(cb_belong_CheckListener);

        txt_licensePlateNo.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {

                switch (view.getId()) {
                    case R.id.txt_licensePlateNo:
                        keyboardUtil.hideSystemKeyBroad();
                        keyboardUtil.hideSoftInputMethod();
                        if (!keyboardUtil.isShow())
                            keyboardUtil.showKeyboard();
                        break;
                    default:
                        if (keyboardUtil.isShow())
                            keyboardUtil.hideKeyboard();
                        break;
                }

                return false;
            }
        });
    }


    private void initData() {
        txt_licensePlateNo.setText(carInfo.getCar().getLicensePlateNo() + "");
        txt_vin.setText(carInfo.getCar().getVin() + "");
        txt_modelName.setText(carInfo.getCar().getModelName() + "");
        txt_engineNo.setText(carInfo.getCar().getEngineNo() + "");
        txt_ratedPassengerCapacity.setText(carInfo.getCar().getRatedPassengerCapacity() + "");
        txt_firstRegisterDate.setText(carInfo.getCar().getFirstRegisterDate() + "");
        //txt_chgownerDate.setText(carInfo.getCar().getChgownerDate() + "");

        String belong = carInfo.getCar().getBelong();

        if (belong.equals("1")) {
            cb_belong_0.setChecked(false);
            cb_belong_1.setChecked(true);
        } else if (belong.equals("2")) {
            cb_belong_0.setChecked(true);
            cb_belong_1.setChecked(false);
        }

        String chgownerType = carInfo.getCar().getChgownerType();

        if (chgownerType.equals("0")) {
            cb_chgownerType_0.setChecked(false);
            cb_chgownerType_1.setChecked(true);
            cb_chgownerType_1.performClick();
        } else if (chgownerType.equals("1")) {
            cb_chgownerType_0.setChecked(true);
            cb_chgownerType_0.performClick();
            cb_chgownerType_1.setChecked(false);
        }

    }

    CheckBox.OnCheckedChangeListener cb_chgownerType_CheckListener = new CheckBox.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            switch (buttonView.getId()) {
                case R.id.cb_chgownerType_0:
                    if (isChecked) {
                        cb_chgownerType_1.setChecked(false);
                        layout_chgownerDate.setVisibility(View.VISIBLE);
                        carInfo.getCar().setChgownerType("1");
                    } else {
                        boolean chgownerType_1 = cb_chgownerType_1.isChecked();
                        if (chgownerType_1 == false) {
                            cb_chgownerType_0.setChecked(true);
                        }
                    }
                    break;
                case R.id.cb_chgownerType_1:
                    if (isChecked) {
                        cb_chgownerType_0.setChecked(false);
                        layout_chgownerDate.setVisibility(View.GONE);
                        carInfo.getCar().setChgownerType("0");
                    } else {
                        boolean chgownerType_0 = cb_chgownerType_0.isChecked();
                        if (chgownerType_0 == false) {
                            cb_chgownerType_1.setChecked(true);
                        }
                    }
                    break;
            }

        }
    };

    CheckBox.OnCheckedChangeListener cb_belong_CheckListener = new CheckBox.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.cb_belong_0:
                    if (isChecked) {
                        cb_belong_1.setChecked(false);
                        layout_customers_carowner_certno.setVisibility(View.GONE);
                    } else {
                        boolean belong_1 = cb_belong_1.isChecked();
                        if (belong_1 == false) {
                            cb_belong_0.setChecked(true);
                        }
                    }
                    break;
                case R.id.cb_belong_1:
                    if (isChecked) {
                        cb_belong_0.setChecked(false);
                        layout_customers_carowner_certno.setVisibility(View.VISIBLE);
                    } else {
                        boolean belong_0 = cb_belong_0.isChecked();
                        if (belong_0 == false) {
                            cb_belong_1.setChecked(true);
                        }
                    }
                    break;
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (keyboardUtil.isShow()) {
            keyboardUtil.hideKeyboard();
        }
        return super.onTouchEvent(event);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyboardUtil.isShow()) {
                keyboardUtil.hideKeyboard();
            } else {
                finish();
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {

        String firstRegisterDate = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        String chgownerDate = "";
        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.txt_firstRegisterDate:
                firstRegisterDate = txt_firstRegisterDate.getText().toString();
                if (StringUtil.isEmptyNotNull(firstRegisterDate)) {
                    firstRegisterDate = now;
                }
                firstRegisterDatePicker.show(firstRegisterDate);
                break;
            case R.id.txt_chgownerDate:
                chgownerDate = txt_chgownerDate.getText().toString();
                if (StringUtil.isEmptyNotNull(chgownerDate)) {
                    chgownerDate = now;
                }
                chgownerDatePicker.show(chgownerDate);
                break;
            case R.id.btn_GoGetCarModel:

                String vin = txt_vin.getText() + "";
                firstRegisterDate = txt_firstRegisterDate.getText() + "";
                String modelName = txt_modelName.getText() + "";
                if (StringUtil.isEmpty(firstRegisterDate)) {
                    showToast("请输入注册日期");
                    return;
                }

                Intent intent = new Intent(NwCarInsConfirmCarInfoActivity.this, NwCarInsGetCarModelInfoActivity.class);


                CarInsGetCarModelInfoPmsBean pmsData = new CarInsGetCarModelInfoPmsBean();

                pmsData.setVin(vin);
                pmsData.setFirstRegisterDate(firstRegisterDate);
                pmsData.setModelName(modelName);
                Bundle b = new Bundle();
                b.putSerializable("dataBean", pmsData);
                intent.putExtras(b);

                startActivityForResult(intent, 0);

                break;
            case R.id.btn_submit:

                if (carInfo.getCar().getChgownerType().equals("1")) {
                    chgownerDate = txt_chgownerDate.getText().toString();

                    if (StringUtil.isEmptyNotNull(chgownerDate)) {
                        showToast("过户日期不能为空");
                        return;
                    }
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                CarInsCarModelInfoBean bean = (CarInsCarModelInfoBean) data.getSerializableExtra("dataBean");


                txt_ratedPassengerCapacity.setText(bean.getRatedPassengerCapacity());


                carInfo.getCar().setModelName(bean.getModelName());
                carInfo.getCar().setModelCode(bean.getModelCode());
                carInfo.getCar().setRatedPassengerCapacity(bean.getRatedPassengerCapacity());
                carInfo.getCar().setDisplacement(bean.getDisplacement());
                carInfo.getCar().setReplacementValue(bean.getReplacementValue());

                String carmodelinfo = bean.getModelName() + ":排量" + bean.getDisplacement() + " " + bean.getMarketYear() + "款" + " " + bean.getRatedPassengerCapacity() + "座" + "（参考价：" + bean.getReplacementValue() + "）";
                txt_carmodelinfo.setText(carmodelinfo);

                break;
            default:
                break;
        }

    }
}
