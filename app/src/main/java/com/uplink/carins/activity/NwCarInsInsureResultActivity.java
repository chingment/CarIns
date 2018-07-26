package com.uplink.carins.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
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
import com.uplink.carins.activity.adapter.NwItemParentFieldAdapter;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.NwCarInsCompanyBean;
import com.uplink.carins.model.api.NwCarInsInsureResult;
import com.uplink.carins.model.api.NwCarInsPayResultBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.city.CitycodeUtil;
import com.uplink.carins.ui.city.ScrollerNumberPicker;
import com.uplink.carins.ui.dialog.CustomConfirmDialog;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.NoDoubleClickUtils;
import com.uplink.carins.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NwCarInsInsureResultActivity extends SwipeBackActivity implements View.OnClickListener {


    String TAG = "NwCarInsInsureResultActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;
    private Button btn_submit;

    private EditText txt_receiptaddress_consignee;
    private EditText txt_receiptaddress_mobile;
    private EditText txt_receiptaddress_address;
    private TextView sel_area;
    private ListView list_item_parent;


    private NwCarInsCompanyBean offerInfo;
    private NwCarInsInsureResult insureInfo;

    private AlertDialog.Builder area_control_builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nwcarins_insureresult);
        offerInfo = (NwCarInsCompanyBean) getIntent().getSerializableExtra("offerInfo");
        insureInfo = (NwCarInsInsureResult) getIntent().getSerializableExtra("insureInfo");
        initView();
        initEvent();
        initData();

        area_control_builder = new AlertDialog.Builder(NwCarInsInsureResultActivity.this);


    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("投保订单");

        btn_submit = (Button) findViewById(R.id.btn_submit);
        txt_receiptaddress_consignee = (EditText) findViewById(R.id.txt_receiptaddress_consignee);
        txt_receiptaddress_mobile = (EditText) findViewById(R.id.txt_receiptaddress_mobile);
        txt_receiptaddress_address = (EditText) findViewById(R.id.txt_receiptaddress_address);
        list_item_parent = (ListView) findViewById(R.id.list_item_parent);
        sel_area = (TextView) findViewById(R.id.sel_area);
    }

    private void initData() {

        if (!StringUtil.isEmptyNotNull(insureInfo.getReceiptAddress().getAreaName())) {
            sel_area.setText(insureInfo.getReceiptAddress().getAreaName());

        } else {
            sel_area.setText("选择地区");
        }

        if (!StringUtil.isEmptyNotNull(insureInfo.getReceiptAddress().getAreaId())) {
            sel_area.setTag(insureInfo.getReceiptAddress().getAreaId());
        } else {
            sel_area.setTag("");
        }

        txt_receiptaddress_consignee.setText(insureInfo.getReceiptAddress().getConsignee());
        txt_receiptaddress_mobile.setText(insureInfo.getReceiptAddress().getMobile());
        txt_receiptaddress_address.setText(insureInfo.getReceiptAddress().getAddress());

        NwItemParentFieldAdapter adapter = new NwItemParentFieldAdapter(NwCarInsInsureResultActivity.this, insureInfo.getInfoItems());

        list_item_parent.setAdapter(adapter);

    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        sel_area.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.btn_submit:
                if (!NoDoubleClickUtils.isDoubleClick()) {
                    submit();
                }
                break;
            case R.id.sel_area:

                String txt_areaName = sel_area.getText().toString();

                View area_control_builder_view = LayoutInflater.from(NwCarInsInsureResultActivity.this).inflate(R.layout.dialog_address, null);
                area_control_builder.setView(area_control_builder_view);
                LinearLayout area_control_linearlayout = (LinearLayout) area_control_builder_view.findViewById(R.id.addressdialog_linearlayout);
                final ScrollerNumberPicker area_control_provincePicker = (ScrollerNumberPicker) area_control_builder_view.findViewById(R.id.province);
                final ScrollerNumberPicker area_control_cityPicker = (ScrollerNumberPicker) area_control_builder_view.findViewById(R.id.city);
                final ScrollerNumberPicker area_control_counyPicker = (ScrollerNumberPicker) area_control_builder_view.findViewById(R.id.couny);
                String[] arr_AreaName = txt_areaName.split("-");

                if (arr_AreaName.length == 1) {
                    area_control_provincePicker.setDefaultByName(arr_AreaName[0]);
                }

                if (arr_AreaName.length == 2) {
                    area_control_provincePicker.setDefaultByName(arr_AreaName[0]);
                    area_control_cityPicker.setDefaultByName(arr_AreaName[1]);
                }

                if (arr_AreaName.length == 3) {
                    area_control_provincePicker.setDefaultByName(arr_AreaName[0]);
                    area_control_cityPicker.setDefaultByName(arr_AreaName[1]);
                    area_control_counyPicker.setDefaultByName(arr_AreaName[2]);
                }


                final AlertDialog dialog = area_control_builder.show();
                area_control_linearlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String areaName = "";

                        if (!StringUtil.isEmptyNotNull(area_control_provincePicker.getSelectedText())) {
                            areaName = area_control_provincePicker.getSelectedText();
                        }

                        if (!StringUtil.isEmptyNotNull(area_control_cityPicker.getSelectedText())) {
                            areaName += "-" + area_control_cityPicker.getSelectedText();
                        }

                        if (!StringUtil.isEmptyNotNull(area_control_counyPicker.getSelectedText())) {
                            areaName += "-" + area_control_counyPicker.getSelectedText();
                        }

                        sel_area.setText(areaName);


                        String code = CitycodeUtil.getSingleton().getCouny_list_code().get(area_control_counyPicker.getSelected());
                        sel_area.setTag(code);
                        dialog.dismiss();

                    }
                });

                break;
        }
    }

    private void submit() {

        pay("正在支付申请中");
    }

    private void pay(String loadingmsg) {


        String receiptaddress_consignee = txt_receiptaddress_consignee.getText().toString();
        String receiptaddress_mobile = txt_receiptaddress_mobile.getText().toString();
        String receiptaddress_areacode = sel_area.getTag().toString();
        String receiptaddress_areaname = sel_area.getText().toString();
        String receiptaddress_address = txt_receiptaddress_address.getText().toString();

        if (StringUtil.isEmpty(receiptaddress_consignee)) {
            showToast("请输入收件人姓名");
            return;
        }

        if (StringUtil.isEmpty(receiptaddress_mobile)) {
            showToast("请输入收件人联系电话");
            return;
        }

        if (StringUtil.isEmpty(receiptaddress_address)) {
            showToast("请输入收件地址");
            return;
        }

        if (StringUtil.isEmpty(receiptaddress_areacode)) {
            showToast("请选择地区");
            return;
        }

        if (StringUtil.isEmpty(receiptaddress_address)) {
            showToast("请输入收件地址");
            return;
        }


        Map<String, Object> params = new HashMap<>();
        params.put("userId", this.getAppContext().getUser().getId());
        params.put("merchantId", this.getAppContext().getUser().getMerchantId());
        params.put("posMachineId", this.getAppContext().getUser().getPosMachineId());
        params.put("offerId", offerInfo.getOfferId());


        JSONObject json_ReceiptAddress = new JSONObject();

        try {
            json_ReceiptAddress.put("consignee", receiptaddress_consignee);
            json_ReceiptAddress.put("mobile", receiptaddress_mobile);
            json_ReceiptAddress.put("address", receiptaddress_address);
            json_ReceiptAddress.put("email", "");
            json_ReceiptAddress.put("areaId", receiptaddress_areacode);
            json_ReceiptAddress.put("areaName", receiptaddress_areaname);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        params.put("receiptAddress", json_ReceiptAddress);

        postWithMy(Config.URL.carInsPay, params, null, true, "正在提交中", new HttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                ApiResultBean<NwCarInsPayResultBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<NwCarInsPayResultBean>>() {

                });

                if (rt.getResult() == Result.SUCCESS) {
                    Intent intent = new Intent(NwCarInsInsureResultActivity.this, NwCarInsPayActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("payResult", rt.getData());
                    intent.putExtras(b);
                    startActivity(intent);
                } else {
                    showToast(rt.getMessage());
                }
            }
        });

    }

}
