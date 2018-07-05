package com.uplink.carins.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.http.HttpClient;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.PayUnifiedOrderResultBean;
import com.uplink.carins.model.api.PayResultQueryResultBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

public class PayQrcodeActivity extends SwipeBackActivity implements View.OnClickListener {

    private String TAG = "PayQrcodeActivity";

    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;
    private PayUnifiedOrderResultBean payQrCodeDownload;


    private TextView tv_test;
    private ImageView pic_test;
    private Button btn_test;
    private Button btn_test2;

    private Handler handler;
    private Runnable runnable;
    private RelativeLayout layout_bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payqrcode);

        initView();
        initEvent();

        payQrCodeDownload = (PayUnifiedOrderResultBean) getIntent().getSerializableExtra("dataBean");

        switch (payQrCodeDownload.getPayWay()) {
            case 2:
                layout_bg.setBackgroundColor(getResources().getColor(R.color.wechat_bg));
                break;
            case 3:
                layout_bg.setBackgroundColor(getResources().getColor(R.color.zhifubao_bg));
                break;
        }

        Bitmap bitmap = createBitmap(payQrCodeDownload.getMwebUrl());
        pic_test.setImageBitmap(bitmap);


        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                LogUtil.i("支付结果查询:" + CommonUtil.getCurrentTime());
                payQuery();
                handler.postDelayed(this, 2000);
            }
        };

        handler.postDelayed(runnable, 2000);//每两秒执行一次runnable.

    }

    public void payQuery() {

        Map<String, String> params = new HashMap<>();
        params.put("userId", this.getAppContext().getUser().getId() + "");
        params.put("merchantId", this.getAppContext().getUser().getMerchantId() + "");
        params.put("posMachineId", this.getAppContext().getUser().getPosMachineId() + "");
        params.put("orderSn", payQrCodeDownload.getOrderSn());

        HttpClient.getWithMy(Config.URL.orderPayResultQuery, params, new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                ApiResultBean<PayResultQueryResultBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<PayResultQueryResultBean>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {

                    PayResultQueryResultBean d = rt.getData();
                    //4 为 已完成支付
                    if (d.getStatus() == 4) {
                        handler.removeCallbacks(runnable); //关闭定时执行操作
                        printTicket(d.getPrintData());

                        Intent intent=new Intent();
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("dataBean", d);
                        intent.putExtras(bundle);
                        setResult(1, intent);
                        finish();

                    }
                }
            }
        });


    }

    public void initView() {

        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("支付信息");

        layout_bg = (RelativeLayout) findViewById(R.id.layout_bg);

        //tv_test = (TextView) findViewById(R.id.tv_test);
        pic_test = (ImageView) findViewById(R.id.pic_test);
        //btn_test = (Button) findViewById(R.id.btn_test);
        //btn_test2 = (Button) findViewById(R.id.btn_test2);
    }


    public void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);

        //btn_test.setOnClickListener(this);
        //btn_test2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.btn_test:
                Bitmap bitmap = createBitmap("hahahaha");
                pic_test.setImageBitmap(bitmap);
                break;
            case R.id.btn_test2:

                new IntentIntegrator(this).setOrientationLocked(false).setCaptureActivity(PayScanQrcodeActivity.class).initiateScan();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);

    }

    public static Bitmap createBitmap(String str) {
        Bitmap bitmap = null;
        BitMatrix result = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            DisplayMetrics dm = new DisplayMetrics();
            int width = dm.widthPixels-100;
            result = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, 400, 400);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(result);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException iae) { // ?
            return null;
        }
        return bitmap;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                ToastUtil.showMessage(this, "内容为空", Toast.LENGTH_LONG);

            } else {
                ToastUtil.showMessage(this, "扫描成功", Toast.LENGTH_LONG);
                // ScanResult 为 获取到的字符串
                String ScanResult = intentResult.getContents();
                tv_test.setText(ScanResult);

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
