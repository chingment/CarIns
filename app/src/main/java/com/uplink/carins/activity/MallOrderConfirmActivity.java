package com.uplink.carins.activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.uplink.carins.activity.adapter.OrderDetailsProductSkuAdapter;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.CartComfirmOrderData;
import com.uplink.carins.model.api.CartProductSkuBean;
import com.uplink.carins.model.api.CartProductSkuByOpreateBean;
import com.uplink.carins.model.api.OrderInfoBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.city.CitycodeUtil;
import com.uplink.carins.ui.city.ScrollerNumberPicker;
import com.uplink.carins.ui.my.MyListView;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.NoDoubleClickUtils;
import com.uplink.carins.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MallOrderConfirmActivity extends SwipeBackActivity implements View.OnClickListener {

    String TAG = "MallOrderConfirmActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;
    private Button btn_submit;

    private EditText txt_recipientAddress_recipient;
    private TextView txt_recipientAddress_areaName;
    private EditText txt_recipientAddress_address;
    private TextView txt_recipientAddress_phoneNumber;
    private MyListView list_skus;

    private CartComfirmOrderData cartComfirmOrderData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall_order_confirm);
        cartComfirmOrderData = (CartComfirmOrderData) getIntent().getSerializableExtra("dataBean");
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("确认订单");

        btn_submit = (Button) findViewById(R.id.btn_submit);
        txt_recipientAddress_recipient = (EditText) findViewById(R.id.txt_recipientAddress_recipient);
        txt_recipientAddress_areaName = (TextView) findViewById(R.id.txt_recipientAddress_areaName);
        txt_recipientAddress_address = (EditText) findViewById(R.id.txt_recipientAddress_address);
        txt_recipientAddress_phoneNumber = (EditText) findViewById(R.id.txt_recipientAddress_phoneNumber);
        list_skus = (MyListView) findViewById(R.id.list_skus);
    }

    private void initData() {

        txt_recipientAddress_recipient.setText(cartComfirmOrderData.getRecipientAddress().getRecipient());
        txt_recipientAddress_areaName.setText(cartComfirmOrderData.getRecipientAddress().getAreaName());
        txt_recipientAddress_areaName.setTag(cartComfirmOrderData.getRecipientAddress().getAreaCode());
        txt_recipientAddress_address.setText(cartComfirmOrderData.getRecipientAddress().getAddress());
        txt_recipientAddress_phoneNumber.setText(cartComfirmOrderData.getRecipientAddress().getPhoneNumber());

        OrderDetailsProductSkuAdapter adapter = new OrderDetailsProductSkuAdapter(MallOrderConfirmActivity.this, cartComfirmOrderData.getSkus());
        list_skus.setAdapter(adapter);

    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        txt_recipientAddress_areaName.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        if (!NoDoubleClickUtils.isDoubleClick()) {
            switch (v.getId()) {
                case R.id.btn_main_header_goback:
                    finish();
                    break;
                case R.id.btn_submit:
                    submit();
                    break;
                case R.id.txt_recipientAddress_areaName:

                    String txt_areaName = txt_recipientAddress_areaName.getText().toString();

                    AlertDialog.Builder builder = new AlertDialog.Builder(MallOrderConfirmActivity.this);
                    View view = LayoutInflater.from(MallOrderConfirmActivity.this).inflate(R.layout.dialog_address, null);
                    builder.setView(view);
                    LinearLayout addressdialog_linearlayout = (LinearLayout) view.findViewById(R.id.addressdialog_linearlayout);
                    final ScrollerNumberPicker provincePicker = (ScrollerNumberPicker) view.findViewById(R.id.province);
                    final ScrollerNumberPicker cityPicker = (ScrollerNumberPicker) view.findViewById(R.id.city);
                    final ScrollerNumberPicker counyPicker = (ScrollerNumberPicker) view.findViewById(R.id.couny);

                    String[] arr_AreaName = txt_areaName.split("-");

                    if (arr_AreaName.length == 1) {
                        provincePicker.setDefaultByName(arr_AreaName[0]);
                    }

                    if (arr_AreaName.length == 2) {
                        provincePicker.setDefaultByName(arr_AreaName[0]);
                        cityPicker.setDefaultByName(arr_AreaName[1]);
                    }

                    if (arr_AreaName.length == 3) {
                        provincePicker.setDefaultByName(arr_AreaName[0]);
                        cityPicker.setDefaultByName(arr_AreaName[1]);
                        counyPicker.setDefaultByName(arr_AreaName[2]);
                    }


                    final AlertDialog dialog = builder.show();
                    addressdialog_linearlayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String areaName = "";

                            if (!StringUtil.isEmptyNotNull(provincePicker.getSelectedText())) {
                                areaName = provincePicker.getSelectedText();
                            }

                            if (!StringUtil.isEmptyNotNull(cityPicker.getSelectedText())) {
                                areaName += "-" + cityPicker.getSelectedText();
                            }

                            if (!StringUtil.isEmptyNotNull(counyPicker.getSelectedText())) {
                                areaName += "-" + counyPicker.getSelectedText();
                            }

                            txt_recipientAddress_areaName.setText(areaName);

                            String areaCode = CitycodeUtil.getSingleton().getCouny_list_code().get(counyPicker.getSelected());

                            txt_recipientAddress_areaName.setTag(areaCode);

                            dialog.dismiss();

                        }
                    });


                    break;
            }
        }
    }

    private void submit() {

        String recipient = txt_recipientAddress_recipient.getText().toString();
        String recipient_areaName = txt_recipientAddress_areaName.getText().toString();
        String recipient_areaCode = txt_recipientAddress_areaName.getTag().toString();
        String recipient_phoneNumber = txt_recipientAddress_phoneNumber.getText().toString();
        String recipient_address = txt_recipientAddress_address.getText().toString();


        if (StringUtil.isEmptyNotNull(recipient)) {
            showToast("请输入收件人姓名");
            return;
        }

        if (StringUtil.isEmptyNotNull(recipient_phoneNumber)) {
            showToast("请输入联系电话");
            return;
        }

        if (StringUtil.isEmptyNotNull(recipient_areaCode)) {
            showToast("请选择地区");
            return;
        }

        if (StringUtil.isEmptyNotNull(recipient_address)) {
            showToast("请输入详细地址");
            return;
        }


        Map<String, Object> params = new HashMap<>();
        params.put("userId", this.getAppContext().getUser().getId());
        params.put("merchantId", this.getAppContext().getUser().getMerchantId());
        params.put("posMachineId", this.getAppContext().getUser().getPosMachineId());


        JSONArray jsonObj_Skus = new JSONArray();

        try {
            for (CartProductSkuBean sku :
                    cartComfirmOrderData.getSkus()) {
                JSONObject jsonFa1 = new JSONObject();
                jsonFa1.put("cartId", sku.getCartId());
                jsonFa1.put("skuId", sku.getSkuId());
                jsonFa1.put("quantity", sku.getQuantity());
                jsonObj_Skus.put(jsonFa1);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        params.put("skus", jsonObj_Skus);

        JSONObject json_recipientAddress = new JSONObject();
        try {
            json_recipientAddress.put("recipient", recipient);
            json_recipientAddress.put("areaCode", recipient_areaCode);
            json_recipientAddress.put("areaName", recipient_areaName);
            json_recipientAddress.put("phoneNumber", recipient_phoneNumber);
            json_recipientAddress.put("address", recipient_address);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        params.put("recipientAddress", json_recipientAddress);

        postWithMy(Config.URL.mallOrderSubmitShopping, params, null, true, "正在提交中", new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                ApiResultBean<OrderInfoBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<OrderInfoBean>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {

                    Intent intent = new Intent(MallOrderConfirmActivity.this, PayConfirmActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("dataBean", rt.getData());
                    intent.putExtras(b);

                    startActivity(intent);
                } else {
                    showToast(rt.getMessage());
                }


            }
        });

    }
}
