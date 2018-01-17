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
import com.uplink.carins.model.api.OfferCompanyBean;
import com.uplink.carins.model.api.OrderDetailsCarInsureBean;
import com.uplink.carins.model.api.OrderListBean;
import com.uplink.carins.ui.ViewHolder;
import com.uplink.carins.ui.choicephoto.ChoicePhotoAndCropAndSwipeBackActivity;
import com.uplink.carins.ui.my.MyListView;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class OrderDetailsCarInsrueActivity extends ChoicePhotoAndCropAndSwipeBackActivity {

    private String TAG = "OrderDetailsCarInsrueActivity";

    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;
    private OrderListBean order;

    private TextView txt_order_sn;
    private TextView txt_order_statusname;

    private MyListView list_carinsoffercompany;
    private CarInsOfferCompanyAdapter list_carinsoffercompany_adapter;


    private LinearLayout layout_submittime;
    private LinearLayout layout_paytime;
    private LinearLayout layout_completetime;
    private LinearLayout layout_cancletime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails_carinsrue);

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
        list_carinsoffercompany = (MyListView) findViewById(R.id.list_carinsoffercompany);

        layout_submittime = (LinearLayout) findViewById(R.id.layout_submittime);
        layout_paytime = (LinearLayout) findViewById(R.id.layout_paytime);
        layout_completetime = (LinearLayout) findViewById(R.id.layout_completetime);
        layout_cancletime = (LinearLayout) findViewById(R.id.layout_cancletime);
    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);
    }

    private void loadData() {
        showProgressDialog(false);
        Map<String, String> params = new HashMap<>();
        params.put("userId", "21");
        params.put("merchantId", "2");
        params.put("orderId", order.getId() + "");
        params.put("productType", order.getProductType() + "");
        HttpClient.getWithMy(Config.URL.getDetails, params, new CallBack());
    }


    public void setView(OrderDetailsCarInsureBean bean) {
        LayoutInflater inflater = LayoutInflater.from(OrderDetailsCarInsrueActivity.this);
        LogUtil.i("SN:" + bean.getSn());

        txt_order_sn.setText(bean.getSn());
        txt_order_statusname.setText(bean.getStatusName());

        list_carinsoffercompany_adapter = new CarInsOfferCompanyAdapter();
        list_carinsoffercompany.setAdapter(list_carinsoffercompany_adapter);
        list_carinsoffercompany_adapter.setData(bean.getOfferCompany());

        switch (bean.getStatus())
        {
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
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
        }
    }

    @Override
    public void OnCropSuccess(String photo_path) {

    }

    @Override
    public void OnCropFail(String error_tx) {

    }

    class CallBack extends HttpResponseHandler {
        @Override
        public void onSuccess(String response) {
            super.onSuccess(response);
            removeProgressDialog();
            LogUtil.e("response===>>>" + response);

            ApiResultBean<OrderDetailsCarInsureBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<OrderDetailsCarInsureBean>>() {
            });

            if (rt.getResult() == 1) {
                setView(rt.getData());
            }
        }

        @Override
        public void onFailure(Request request, Exception e) {
            super.onFailure(request, e);
            removeProgressDialog();
        }
    }


    private class CarInsOfferCompanyAdapter extends BaseAdapter { // shoplist适配器

        private List<OfferCompanyBean> offerCompanys;

        CarInsOfferCompanyAdapter() {

            this.offerCompanys = new ArrayList<>();
        }

        public void setData(List<OfferCompanyBean> offerCompanys) {
            this.offerCompanys = offerCompanys;

            notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return offerCompanys.size();
        }

        @Override
        public Object getItem(int position) {

            return offerCompanys.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            OfferCompanyBean offerCompany = offerCompanys.get(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(OrderDetailsCarInsrueActivity.this).inflate(R.layout.item_orderdetails_carinsure_offercompany, parent, false);
            }
            ImageView img_offer = ViewHolder.get(convertView, R.id.item_offercompany_img);
            TextView txt_offercompany_name = ViewHolder.get(convertView, R.id.item_offercompany_name);
            TextView txt_offercompany_totalprice = ViewHolder.get(convertView, R.id.item_offercompany_totalprice);
            CheckBox cb_offercompany = ViewHolder.get(convertView, R.id.item_offercompany_cb);


            int order_status = order.getStatus();

            txt_offercompany_name.setText(offerCompany.getInsuranceCompanyName());

            txt_offercompany_totalprice.setText(offerCompany.getInsureTotalPrice() + "");



            CommonUtil.loadImageFromUrl(OrderDetailsCarInsrueActivity.this, img_offer, offerCompany.getInsureImgUrl());


            if (order_status == 3) {
                cb_offercompany.setVisibility(View.VISIBLE);
            } else {
                cb_offercompany.setVisibility(View.GONE);
            }

            return convertView;
        }
    }
}
