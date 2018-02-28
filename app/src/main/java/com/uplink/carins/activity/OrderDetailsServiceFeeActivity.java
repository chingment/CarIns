package com.uplink.carins.activity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.newland.mtype.module.common.printer.PrintContext;
import com.newland.mtype.module.common.printer.Printer;
import com.newland.mtype.module.common.printer.PrinterResult;
import com.newland.mtype.module.common.printer.PrinterStatus;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.http.HttpClient;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.OrderDetailsServiceFeeBean;
import com.uplink.carins.model.api.OrderListBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Request;

public class OrderDetailsServiceFeeActivity extends SwipeBackActivity implements View.OnClickListener {

    private String TAG = "OrderDetailsServiceFeeActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;
    private OrderListBean order;

    private TextView txt_order_sn;
    private TextView txt_order_statusname;
    private TextView txt_order_remarks;
    private TextView txt_order_submittime;
    private TextView txt_order_paytime;
    private TextView txt_order_completetime;
    private TextView txt_order_cancletime;
    private TextView txt_order_price;

    private TextView txt_order_expirytime;
    private TextView txt_order_deposit;
    private TextView txt_order_mobiletrafficfee;


    private LinearLayout layout_submittime;
    private LinearLayout layout_paytime;
    private LinearLayout layout_completetime;
    private LinearLayout layout_cancletime;
    private LinearLayout layout_deposit;

    private Button btn_printer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails_servicefee);

        order = (OrderListBean) getIntent().getSerializableExtra("dataBean");

        initView();
        initEvent();
        loadData();
    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("订单详情");


        txt_order_sn = (TextView) findViewById(R.id.txt_order_sn);
        txt_order_statusname = (TextView) findViewById(R.id.txt_order_statusname);
        txt_order_price = (TextView) findViewById(R.id.txt_order_price);

        txt_order_remarks = (TextView) findViewById(R.id.txt_order_remarks);
        txt_order_submittime = (TextView) findViewById(R.id.txt_order_submittime);
        txt_order_paytime = (TextView) findViewById(R.id.txt_order_paytime);
        txt_order_completetime = (TextView) findViewById(R.id.txt_order_completetime);
        txt_order_cancletime = (TextView) findViewById(R.id.txt_order_cancletime);

        layout_submittime = (LinearLayout) findViewById(R.id.layout_submittime);
        layout_paytime = (LinearLayout) findViewById(R.id.layout_paytime);
        layout_completetime = (LinearLayout) findViewById(R.id.layout_completetime);
        layout_cancletime = (LinearLayout) findViewById(R.id.layout_cancletime);
        layout_deposit = (LinearLayout) findViewById(R.id.layout_deposit);

        txt_order_expirytime = (TextView) findViewById(R.id.txt_order_expirytime);
        txt_order_deposit = (TextView) findViewById(R.id.txt_order_deposit);
        txt_order_mobiletrafficfee = (TextView) findViewById(R.id.txt_order_mobiletrafficfee);

        btn_printer = (Button) findViewById(R.id.btn_printer);

    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);
        btn_printer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.btn_printer:

                try {
                    Printer printer = getn900Device().getPrinter();

                    if (printer.getStatus() != PrinterStatus.NORMAL) {
                        showToast("打印失败，打印机状态不正常,请检查设备");
                        return;
                    }

//                    String bill = "\n\n   " + "签购单" + "      \n";// 交易明细信息
//                    bill += "商户名称：钓鱼岛\n";
//                    bill += "操作员号：001\n";
//                    bill += "消费类型：消费\n";
//                    bill += "商户编号:123455432112345\n";
//                    bill += "商户名称:机器服务费\n";
//                    bill += "-------------------------------\n";
//                    bill += "+++++++++++++++++++++++++++++++\n";
//                    bill += "\n\n\n\n";
//                    printer.init();
//                    PrinterResult result = printer.print(bill, 30, TimeUnit.SECONDS);

                    Map<String, Bitmap> map = new HashMap<String, Bitmap>();

                    StringBuffer scriptBuffer = new StringBuffer();
                    scriptBuffer.append("*text l ++++++++++++++ X ++++++++++++++ \n");
                    scriptBuffer.append("!hz l\n !asc l\n !gray 5\n");// 设置标题字体为大号
                    scriptBuffer.append("!yspace 5\n");// 设置行间距,取值【0,60】，默认6
                    scriptBuffer.append("*text c 签购单\n");
                    scriptBuffer.append("!hz n\n !asc n !gray 5\n");// 设置内容字体为中号
                    scriptBuffer.append("!yspace 10\n");// 设置内容行间距
                    scriptBuffer.append("*line" + "\n");// 打印虚线
                    scriptBuffer.append("*text l 商户存根/MERCHANT COPY\n");
                    scriptBuffer.append("*line" + "\n");// 打印虚线
                    scriptBuffer.append("*text l 商户名称:比亚迪1\n");
                    scriptBuffer.append("*text l 商户编号:123455432112345\n");
                    scriptBuffer.append("*text l 终端编号:20130717\n");
                    scriptBuffer.append("*line" + "\n");// 打印虚线
                    scriptBuffer.append("*text l 交易类型:消费/SALE\n");
                    scriptBuffer.append("*text l 支付方式:微信支付\n");
                    scriptBuffer.append("*text l 日期时间:2015/12/25 11:41:06\n");
                    scriptBuffer.append("*text l 金 额:RMB 1.16\n");
                    scriptBuffer.append("*text l 操作员号:001\n");
                    scriptBuffer.append("*text l 程序版本:1.0.18\n");
                    scriptBuffer.append("*underline l 服务热线:888888888\n");
                    scriptBuffer.append("*text l ++++++++++++++ X ++++++++++++++ \n");
                    scriptBuffer.append("!NLPRNOVER"); // 走纸//10

                    PrinterResult printerResult = printer.printByScript(PrintContext.defaultContext(), scriptBuffer.toString().getBytes("GBK"), map, 60, TimeUnit.SECONDS);


                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("打印字符串异常：" + e);
                }

                break;
        }
    }

    public void setView(OrderDetailsServiceFeeBean bean) {

        txt_order_sn.setText(bean.getSn());
        txt_order_statusname.setText(bean.getStatusName());
        txt_order_remarks.setText(bean.getRemarks());
        txt_order_submittime.setText(bean.getSubmitTime());
        txt_order_paytime.setText(bean.getPayTime());
        txt_order_completetime.setText(bean.getCompleteTime());
        txt_order_cancletime.setText(bean.getCancleTime());

        txt_order_price.setText(bean.getPrice());
        txt_order_expirytime.setText(bean.getExpiryTime());
        txt_order_deposit.setText(bean.getDeposit());
        txt_order_mobiletrafficfee.setText(bean.getMobileTrafficFee());


        if (StringUtil.isEmptyNotNull(bean.getDeposit())) {
            layout_deposit.setVisibility(View.GONE);
        }

        switch (bean.getStatus()) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                layout_paytime.setVisibility(View.VISIBLE);
                layout_completetime.setVisibility(View.VISIBLE);
                btn_printer.setVisibility(View.VISIBLE);
                break;
            case 5:
                layout_cancletime.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void loadData() {

        Map<String, String> params = new HashMap<>();
        params.put("userId", this.getAppContext().getUser().getId() + "");
        params.put("merchantId", this.getAppContext().getUser().getMerchantId() + "");
        params.put("posMachineId", this.getAppContext().getUser().getPosMachineId() + "");
        params.put("orderId", order.getId() + "");
        params.put("productType", order.getProductType() + "");
        getWithMy(Config.URL.getDetails, params, new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                LogUtil.i(TAG, "onSuccess====>>>" + response);

                ApiResultBean<OrderDetailsServiceFeeBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<OrderDetailsServiceFeeBean>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {
                    setView(rt.getData());
                }
            }

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
                LogUtil.e(TAG, "onFailure====>>>" + e.getMessage());
            }

        });
    }
}
