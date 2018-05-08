package com.uplink.carins.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.http.HttpClient;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.LllegalPriceRecordBean;
import com.uplink.carins.model.api.OrderDetailsLllegalDealtBean;
import com.uplink.carins.model.api.OrderDetailsLllegalQueryRechargeBean;
import com.uplink.carins.model.api.OrderListBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.ViewHolder;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class OrderDetailsLllegalDealtActivity extends SwipeBackActivity implements View.OnClickListener {

    private String TAG = "OrderDetailsLllegalDealtActivity";

    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;
    private OrderListBean order;
    private LayoutInflater inflater;

    private TextView txt_order_sn;
    private TextView txt_order_statusname;
    private TextView txt_order_remarks;
    private TextView txt_order_submittime;
    private TextView txt_order_paytime;
    private TextView txt_order_completetime;
    private TextView txt_order_cancletime;
    private LinearLayout layout_submittime;
    private LinearLayout layout_paytime;
    private LinearLayout layout_completetime;
    private LinearLayout layout_cancletime;

    private TextView txt_order_price;
    private TextView txt_order_sumpoint;
    private TextView txt_order_sumcount;
    private TextView txt_order_sumservicefees;
    private TextView txt_order_sumlatefees;
    private TextView txt_order_sumfine;
    private ListView list_lllegalqueryresult;
    private List<LllegalPriceRecordBean> lllegalPriceRecord = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails_lllegaldealt);

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

        inflater = LayoutInflater.from(this);
        txt_order_sn = (TextView) findViewById(R.id.txt_order_sn);
        txt_order_statusname = (TextView) findViewById(R.id.txt_order_statusname);
        txt_order_remarks = (TextView) findViewById(R.id.txt_order_remarks);
        txt_order_submittime = (TextView) findViewById(R.id.txt_order_submittime);
        txt_order_completetime = (TextView) findViewById(R.id.txt_order_completetime);
        txt_order_cancletime = (TextView) findViewById(R.id.txt_order_cancletime);
        txt_order_paytime = (TextView) findViewById(R.id.txt_order_paytime);
        layout_submittime = (LinearLayout) findViewById(R.id.layout_submittime);
        layout_completetime = (LinearLayout) findViewById(R.id.layout_completetime);
        layout_cancletime = (LinearLayout) findViewById(R.id.layout_cancletime);
        layout_paytime = (LinearLayout) findViewById(R.id.layout_paytime);

        txt_order_price = (TextView) findViewById(R.id.txt_order_price);
        txt_order_sumpoint = (TextView) findViewById(R.id.txt_order_sumpoint);
        txt_order_sumcount = (TextView) findViewById(R.id.txt_order_sumcount);
        txt_order_sumservicefees = (TextView) findViewById(R.id.txt_order_sumservicefees);
        txt_order_sumlatefees = (TextView) findViewById(R.id.txt_order_sumlatefees);
        txt_order_sumfine = (TextView) findViewById(R.id.txt_order_sumfine);

        list_lllegalqueryresult = (ListView) findViewById(R.id.list_lllegalqueryresult);
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
        }
    }

    private void loadData() {

        Map<String, String> params = new HashMap<>();
        params.put("userId", this.getAppContext().getUser().getId() + "");
        params.put("merchantId", this.getAppContext().getUser().getMerchantId() + "");
        params.put("posMachineId", this.getAppContext().getUser().getPosMachineId() + "");
        params.put("orderId", order.getId() + "");
        params.put("type", order.getType() + "");
        getWithMy(Config.URL.getDetails, params,false,"", new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                LogUtil.i(TAG, "onSuccess====>>>" + response);

                ApiResultBean<OrderDetailsLllegalDealtBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<OrderDetailsLllegalDealtBean>>() {
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

    public void setView(OrderDetailsLllegalDealtBean bean) {

        txt_order_sn.setText(bean.getSn());
        txt_order_statusname.setText(bean.getStatusName());
        txt_order_remarks.setText(bean.getRemarks());
        txt_order_submittime.setText(bean.getSubmitTime());
        txt_order_paytime.setText(bean.getPayTime());
        txt_order_completetime.setText(bean.getCompleteTime());
        txt_order_cancletime.setText(bean.getCancleTime());

        txt_order_price.setText(bean.getPrice());
        txt_order_sumpoint.setText(bean.getSumPoint());
        txt_order_sumcount.setText(bean.getSumCount());
        txt_order_sumservicefees.setText(bean.getSumServiceFees());
        txt_order_sumlatefees.setText(bean.getSumLateFees());
        txt_order_sumfine.setText(bean.getSumFine());


        switch (bean.getStatus()) {
            case 1:
                layout_submittime.setVisibility(View.VISIBLE);
                break;
            case 2:
                layout_submittime.setVisibility(View.VISIBLE);
                break;
            case 3:
                layout_submittime.setVisibility(View.VISIBLE);
                break;
            case 4:
                layout_submittime.setVisibility(View.VISIBLE);
                layout_paytime.setVisibility(View.VISIBLE);
                layout_completetime.setVisibility(View.VISIBLE);
                break;
            case 5:
                layout_cancletime.setVisibility(View.VISIBLE);
                break;
        }

        if (bean.getLllegalRecord() != null) {
            lllegalPriceRecord = bean.getLllegalRecord();
            LllegalPriceRecordItemAdapter nineGridItemdapter = new LllegalPriceRecordItemAdapter();
            list_lllegalqueryresult.setAdapter(nineGridItemdapter);
        }

    }

    private class LllegalPriceRecordItemAdapter extends BaseAdapter {


        LllegalPriceRecordItemAdapter() {

        }

        @Override
        public int getCount() {
            return lllegalPriceRecord.size();
        }

        @Override
        public Object getItem(int position) {

            return lllegalPriceRecord.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_lllegalqueryresult, null);
            }

            LllegalPriceRecordBean bean = lllegalPriceRecord.get(position);

            TextView txt_city = ViewHolder.get(convertView, R.id.item_lllegal_city);
            TextView txt_address = ViewHolder.get(convertView, R.id.item_lllegal_address);
            TextView txt_desc = ViewHolder.get(convertView, R.id.item_lllegal_desc);
            TextView txt_time = ViewHolder.get(convertView, R.id.item_lllegal_time);
            TextView txt_point = ViewHolder.get(convertView, R.id.item_lllegal_point);
            TextView txt_fine = ViewHolder.get(convertView, R.id.item_lllegal_fine);
            TextView txt_servicefee = ViewHolder.get(convertView, R.id.item_lllegal_servicefee);
            TextView txt_latefees = ViewHolder.get(convertView, R.id.item_lllegal_late_fees);
            TextView txt_content = ViewHolder.get(convertView, R.id.item_lllegal_content);
            TextView txt_status = ViewHolder.get(convertView, R.id.item_lllegal_status);
            CheckBox cb_candealt = ViewHolder.get(convertView, R.id.item_lllegal_candealt);
            if (bean.getNeedDealt()) {
                cb_candealt.setChecked(true);
            } else {
                cb_candealt.setChecked(false);
            }

            if (bean.getCanDealt()) {
                cb_candealt.setVisibility(View.VISIBLE);
            }

            txt_city.setText(bean.getLllegalCity());
            txt_address.setText(bean.getAddress());
            txt_desc.setText(bean.getLllegalDesc());
            txt_time.setText(bean.getLllegalTime());
            txt_point.setText(bean.getPoint());
            txt_fine.setText(bean.getFine());
            txt_servicefee.setText(bean.getServiceFee());
            txt_latefees.setText(bean.getLate_fees());
            txt_content.setText(bean.getContent());
            txt_status.setText(bean.getStatus());

            cb_candealt.setVisibility(View.GONE);
            txt_status.setVisibility(View.VISIBLE);


            return convertView;
        }

    }

}
