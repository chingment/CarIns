package com.uplink.carins.activity;

import android.app.Activity;
import android.content.ComponentName;
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
import com.uplink.carins.Own.AppCacheManager;
import com.uplink.carins.Own.AppContext;
import com.uplink.carins.Own.AppManager;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.http.HttpClient;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.ConfirmFieldBean;
import com.uplink.carins.model.api.GetPayTranSnResultBean;
import com.uplink.carins.model.api.OrderInfoBean;
import com.uplink.carins.model.api.OrderType;
import com.uplink.carins.model.api.PayQrCodeDownloadBean;
import com.uplink.carins.model.api.PayResultNotifyByLllegalQueryRechargeBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.IpAdressUtil;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.NoDoubleClickUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

        if (orderInfo != null) {

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
    }

    public void initView() {

        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("支付信息");

        layout_paymethod = (LinearLayout) findViewById(R.id.layout_paymethod);

        btn_submit_gopay = (Button) findViewById(R.id.btn_submit_gopay);
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
                    for (int j = 0; j < layout_paymethod_child_count; j++) {
                        View view1 = layout_paymethod_item.getChildAt(j);
                        if (view1 instanceof CheckBox) {
                            CheckBox cb = (CheckBox) view1;
                            if (v.equals(layout_paymethod_item)) {
                                String tag = cb.getTag().toString();

                                layout_paymethod.setTag(tag);

                                cb.setChecked(true);
                            } else {
                                cb.setChecked(false);
                            }
                        }
                    }
                }
            }

            LogUtil.i("点击");
        }
    };

    public static String getCurrentYMDHMS() {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        long lt = System.currentTimeMillis();
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_main_header_goback:

                LogUtil.i("d=>>>>>>>>getOrderInfo().getProductType2" + orderInfo.getOrderType());

                if (orderInfo.getOrderType() == OrderType.ServiceFee) {

                    AppContext.getInstance().setUser(null);
                    AppCacheManager.setLastUpdateTime("");

                    stopMyTask();
                    Intent intent = new Intent(PayConfirmActivity.this, LoginActivity.class);
                    startActivity(intent);
                    AppManager.getAppManager().finishAllActivity();
                } else {
                    finish();
                }

                break;
            case R.id.btn_submit_gopay:
                if (!NoDoubleClickUtils.isDoubleClick()) {

                    String str_transType = layout_paymethod.getTag().toString();
                    int transType = 0;
                    if (str_transType.equals("2")) {
                        transType = 67;
                    } else if (str_transType.equals("3")) {
                        transType = 73;
                    }



                    LogUtil.e("transType=>" + transType);

                    if (transType == 67 || transType == 73) {
                        Map<String, Object> params = new HashMap<>();
                        params.put("userId", this.getAppContext().getUser().getId());
                        params.put("posMachineId", this.getAppContext().getUser().getPosMachineId());
                        params.put("merchantId", this.getAppContext().getUser().getMerchantId());
                        params.put("orderId", orderInfo.getOrderId());
                        params.put("orderSn", orderInfo.getOrderSn());
                        params.put("transType", transType);


                        postWithMy(Config.URL.orderGetPayTranSn, params, null, new HttpResponseHandler() {
                            @Override
                            public void onSuccess(String response) {
                                super.onSuccess(response);
                                LogUtil.i(TAG, "onSuccess====>>>" + response);

                                ApiResultBean<GetPayTranSnResultBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<GetPayTranSnResultBean>>() {
                                });

                                if (rt.getResult() == Result.SUCCESS) {
                                    orderInfo.setPayTransSn(rt.getData().getPayTransSn());
                                    LogUtil.e("订单号：" + orderInfo.getOrderSn());
                                    LogUtil.e("流水号：" + orderInfo.getPayTransSn());
                                    LogUtil.e("金额：" + rt.getData().getAmount());
                                    Intent scan2 = new Intent();
                                    scan2.setClassName("com.newland.fczhu", "com.newland.fczhu.ui.activity.MainActivity");
                                    //第三方应用传入交易参数给厂商程序
                                    scan2.putExtra("transType", rt.getData().getTransType());//微信67，支付宝73
                                    scan2.putExtra("amount", rt.getData().getAmount());    //金额
                                    scan2.putExtra("order", rt.getData().getPayTransSn());//交易方式

                                    try {
                                        startActivityForResult(scan2, 1);
                                    } catch (Exception ex) {
                                        showToast("跳转支付发生异常");
                                    }

                                } else {
                                    showToast(rt.getMessage());
                                }

                            }

                            @Override
                            public void onFailure(Request request, Exception e) {
                                super.onFailure(request, e);
                                LogUtil.e(TAG, "onFailure====>>>" + e.getMessage());
                                showToast("提交失败");
                            }
                        });


                    }
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (data == null) {
                LogUtil.e("data 数据为空");
            } else {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    LogUtil.e("bundle 数据为空");
                } else {
                    Set<String> keySet = bundle.keySet();  //获取所有的Key,
                    if (keySet == null) {
                        LogUtil.e("bundle keySet 数据为空");
                    } else {
                        LogUtil.e("读取 keySet数据");


                        for (String key : keySet) {

                            LogUtil.e("key=>" + key + " value=>" + bundle.get(key));

                        }

                        Map<String, Object> param = new HashMap<>();

                        if (bundle.get("order") != null) {
                            param.put("order", bundle.get("order"));
                            param.put("orderSn", orderInfo.getOrderSn());
                        }
                        if (bundle.get("amount") != null) {
                            param.put("amount", bundle.get("amount"));
                        }
                        if (bundle.get("cardNo") != null) {
                            param.put("cardNo", bundle.get("cardNo"));
                        }
                        if (bundle.get("traceNo") != null) {
                            param.put("traceNo", bundle.get("traceNo"));
                        }
                        if (bundle.get("shopId") != null) {
                            param.put("shopId", bundle.get("shopId"));
                        }
                        if (bundle.get("shopName") != null) {
                            param.put("shopName", bundle.get("shopName"));
                        }
                        if (bundle.get("batchNo") != null) {
                            param.put("batchNo", bundle.get("batchNo"));
                        }
                        if (bundle.get("posId") != null) {
                            param.put("posId", bundle.get("posId"));
                        }
                        if (bundle.get("refNo") != null) {
                            param.put("refNo", bundle.get("refNo"));
                        }
                        if (bundle.get("transDate") != null) {
                            param.put("transDate", bundle.get("transDate"));
                        }
                        if (bundle.get("transTime") != null) {
                            param.put("transTime", bundle.get("transTime"));
                        }
                        if (bundle.get("transType") != null) {
                            param.put("transType", bundle.get("transType"));
                        }
                        if (bundle.get("authCode") != null) {
                            param.put("authCode", bundle.get("authCode"));
                        }


                        postWithMy(Config.URL.orderPayResultNotify, param, null, new HttpResponseHandler() {

                            @Override
                            public void onSuccess(String response) {
                                super.onSuccess(response);

                                LogUtil.i(TAG, "onSuccess===>>" + response);

                                ApiResultBean<Object> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<Object>>() {
                                });

                                if (rt.getResult() == Result.SUCCESS) {

                                    switch (orderInfo.getOrderType()) {
                                        case OrderType.ServiceFee:
                                            Intent intent = new Intent(PayConfirmActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                            break;
                                        case OrderType.LllegalQueryRecharg:
                                            ApiResultBean<PayResultNotifyByLllegalQueryRechargeBean> rt601 = JSON.parseObject(response, new TypeReference<ApiResultBean<PayResultNotifyByLllegalQueryRechargeBean>>() {
                                            });
                                            AppCacheManager.setLllegalQueryScore(rt601.getData().getScore());
                                            setResult(1, null);
                                            finish();
                                            break;
                                        case OrderType.LllegalDealt:
                                            setResult(1, null);
                                            finish();
                                            break;
                                    }

                                } else {
                                    showToast(rt.getMessage());
                                }

                            }

                            @Override
                            public void onFailure(Request request, Exception e) {
                                super.onFailure(request, e);
                                LogUtil.e(TAG, e);
                                showToast("发生异常");
                            }
                        });

                    }
                }
            }
        }
        catch (Exception ex) {
            showToast("支付结果反馈失败，如付款成功，请联系客服");
        }
    }


//    public void getPayQrCode() {
//
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("userId", this.getAppContext().getUser().getId());
//        params.put("merchantId", this.getAppContext().getUser().getMerchantId() + "");
//        params.put("posMachineId", this.getAppContext().getUser().getPosMachineId() + "");
//        params.put("orderSn", orderInfo.getOrderSn());
//        params.put("payWay", layout_paymethod.getTag().toString());
//        params.put("termId", "12345678");
//        params.put("spbillIp", IpAdressUtil.getIPAddress(this));
//
//        postWithMy(Config.URL.orderQrCodeDownload, params, null, new HttpResponseHandler() {
//            @Override
//            public void onSuccess(String response) {
//                super.onSuccess(response);
//
//                LogUtil.i(TAG, "onSuccess====>>>" + response);
//
//                ApiResultBean<PayQrCodeDownloadBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<PayQrCodeDownloadBean>>() {
//                });
//
//
//                if (rt.getResult() == Result.SUCCESS) {
//                    Bundle b = new Bundle();
//                    b.putSerializable("dataBean", rt.getData());
//                    Intent intent = new Intent(PayConfirmActivity.this, PayQrcodeActivity.class);
//                    intent.putExtras(b);
//                    startActivity(intent);
//                } else {
//                    showToast(rt.getMessage());
//                }
//            }
//
//            @Override
//            public void onFailure(Request request, Exception e) {
//                super.onFailure(request, e);
//                LogUtil.e(TAG, "onFailure====>>>" + e.getMessage());
//                showToast("支付异常");
//            }
//        });
//
//
//    }

}
