package com.uplink.carins.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.http.HttpClient;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.ConfirmFieldBean;
import com.uplink.carins.model.api.OrderInfoBean;
import com.uplink.carins.model.api.PayQrCodeDownloadBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class PayConfirmActivity extends SwipeBackActivity implements View.OnClickListener {

    private String TAG = "PayConfirmActivity";

    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;

    private LayoutInflater inflater;

    private LinearLayout layout_paymethod;

    Button btn_submit_gopay;

    OrderInfoBean orderInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payconfirm);


        initView();
        initEvent();

        LinearLayout list_confirmfields = (LinearLayout) this.findViewById(R.id.list_confirmfields);
        inflater = LayoutInflater.from(PayConfirmActivity.this);

        orderInfo = (OrderInfoBean) getIntent().getSerializableExtra("dataBean");

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

        layout_paymethod = (LinearLayout) findViewById(R.id.layout_paymethod);

        btn_submit_gopay= (Button) findViewById(R.id.btn_submit_gopay);
    }

    public void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);
        btn_submit_gopay.setOnClickListener(this);

        int layout_paymethod_count = layout_paymethod.getChildCount();

        for (int i = 0; i < layout_paymethod_count; i++) {
            View view = layout_paymethod.getChildAt(i);
            if (view instanceof RelativeLayout) {

                view.setOnClickListener(myPayMethodClick);
            }
        }
    }


    View.OnClickListener myPayMethodClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            int layout_paymethod_count = layout_paymethod.getChildCount();

            for (int i = 0; i < layout_paymethod_count; i++) {
                View view = layout_paymethod.getChildAt(i);
                if (view instanceof RelativeLayout) {
                    RelativeLayout layout_paymethod_item = ((RelativeLayout) view);
                    int layout_paymethod_child_count = layout_paymethod.getChildCount();
                    // view.get
                    for (int j = 0; j <layout_paymethod_child_count;j++) {
                        View view1 = layout_paymethod_item.getChildAt(j);
                        if (view1 instanceof CheckBox) {
                            CheckBox cb = (CheckBox) view1;
                            if(v.equals(layout_paymethod_item))
                            {
                                cb.setChecked(true);
                            }
                            else {
                                cb.setChecked(false);
                            }
                        }
                    }
                }
            }

            LogUtil.i("点击");
        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.btn_submit_gopay:
                getPayQrCode();
                break;
        }
    }


    public  void getPayQrCode()
    {


        Map<String, Object> params = new HashMap<>();
        params.put("userId", this.getAppContext().getUser().getId());
        params.put("orderSn", orderInfo.getOrderSn());
        params.put("payWay", "2");
        params.put("termId", "3436563");
        params.put("spbillIp", "127.0.0.1");

        HttpClient.postWithMy(Config.URL.orderQrCodeDownload, params,null, new  HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                LogUtil.i(TAG,"onSuccess====>>>" +response);

                ApiResultBean<PayQrCodeDownloadBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<PayQrCodeDownloadBean>>() {
                });

                showToast(rt.getMessage());

                if (rt.getResult() == Result.SUCCESS) {
                    Bundle b = new Bundle();
                    b.putSerializable("dataBean",rt.getData());
                    Intent intent = new Intent(PayConfirmActivity.this,PayQrcodeActivity.class);
                    intent.putExtras(b);
                    startActivity(intent);
                }

                removeProgressDialog();
            }

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
                LogUtil.e(TAG,"onFailure====>>>" + e.getMessage());
                removeProgressDialog();
                showToast("支付异常");
            }
        });


    }


}
