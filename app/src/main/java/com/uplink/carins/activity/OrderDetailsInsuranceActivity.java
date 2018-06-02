package com.uplink.carins.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.ItemFieldBean;
import com.uplink.carins.model.api.OrderDetailsInsuranceBean;
import com.uplink.carins.model.api.OrderListBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.model.api.ZjBean;
import com.uplink.carins.ui.ViewHolder;
import com.uplink.carins.ui.my.MyHorizontalListView;
import com.uplink.carins.ui.my.MyListView;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class OrderDetailsInsuranceActivity extends SwipeBackActivity implements View.OnClickListener {

    private String TAG = "OrderDetailsInsuranceActivity";

    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;
    private OrderListBean order;
    private LayoutInflater inflater;

    private TextView txt_order_sn;
    private TextView txt_order_statusname;
    private TextView txt_order_remarks;
    private TextView txt_order_submittime;
    //private TextView txt_order_paytime;
    private TextView txt_order_completetime;
    private TextView txt_order_cancletime;
    private LinearLayout layout_submittime;
    private LinearLayout layout_paytime;
    private LinearLayout layout_completetime;
    private LinearLayout layout_cancletime;

    private TextView txt_order_insurancecompanyname;
    private TextView txt_order_productskuname;

    private MyHorizontalListView list_zj;
    private Zjdapter list_zj_adapter;

    private MyListView list_attritem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails_insurance);

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
        //txt_order_paytime = (TextView) findViewById(R.id.txt_order_paytime);
        layout_submittime = (LinearLayout) findViewById(R.id.layout_submittime);
        layout_completetime = (LinearLayout) findViewById(R.id.layout_completetime);
        layout_cancletime = (LinearLayout) findViewById(R.id.layout_cancletime);
        layout_paytime = (LinearLayout) findViewById(R.id.layout_paytime);

        txt_order_insurancecompanyname = (TextView) findViewById(R.id.txt_order_insurancecompanyname);
        txt_order_productskuname = (TextView) findViewById(R.id.txt_order_productskuname);

        list_attritem = (MyListView) findViewById(R.id.list_attritem);
        list_zj = (MyHorizontalListView) findViewById(R.id.list_zj);
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

                ApiResultBean<OrderDetailsInsuranceBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<OrderDetailsInsuranceBean>>() {
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

    public void setView(OrderDetailsInsuranceBean bean) {

        txt_order_sn.setText(bean.getSn());
        txt_order_statusname.setText(bean.getStatusName());
        txt_order_remarks.setText(bean.getRemarks());
        txt_order_submittime.setText(bean.getSubmitTime());
        //txt_order_paytime.setText(bean.getPayTime());
        txt_order_completetime.setText(bean.getCompleteTime());
        txt_order_cancletime.setText(bean.getCancleTime());

        txt_order_insurancecompanyname.setText(bean.getInsCompanyName());
        txt_order_productskuname.setText(bean.getProductSkuName());

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
                //layout_paytime.setVisibility(View.VISIBLE);
                layout_completetime.setVisibility(View.VISIBLE);
                break;
            case 5:
                layout_cancletime.setVisibility(View.VISIBLE);
                break;
        }


        AttrItemsAdapter attrItemsAdapter = new AttrItemsAdapter();
        list_attritem.setAdapter(attrItemsAdapter);
        attrItemsAdapter.setData(bean.getProductSkuAttrItems());
        CommonUtil.setListViewHeightBasedOnChildren(list_attritem);


        list_zj_adapter = new Zjdapter();
        list_zj.setAdapter(list_zj_adapter);
        list_zj_adapter.setData(bean.getCredentialsImgs());


    }

    private class AttrItemsAdapter extends BaseAdapter {
        private List<ItemFieldBean> beans=new ArrayList<>();

        public void setData(List<ItemFieldBean> beans) {
            if(beans!=null) {
                this.beans = beans;
            }
            notifyDataSetChanged();
        }

        AttrItemsAdapter() {

            this.beans = new ArrayList<>();
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }

        @Override
        public int getCount() {
            return beans.size();
        }

        @Override
        public Object getItem(int position) {
            return beans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ItemFieldBean bean = beans.get(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(OrderDetailsInsuranceActivity.this).inflate(R.layout.item_attritem_field, parent, false);
            }

            TextView txt_name = ViewHolder.get(convertView, R.id.item_order_field_name);
            TextView txt_value = ViewHolder.get(convertView, R.id.item_order_field_value);

            txt_name.setText(bean.getField() + "");
            txt_value.setText(bean.getValue() + "");

            return convertView;
        }


    }

    private class Zjdapter extends BaseAdapter {


        private List<ZjBean> zjs=new ArrayList<>();


        Zjdapter() {

            this.zjs = new ArrayList<>();
        }


        public void setData(List<ZjBean> zjs) {
            if(zjs!=null) {
                this.zjs = zjs;
            }
            notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return zjs.size();
        }

        @Override
        public Object getItem(int position) {

            return zjs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ZjBean zj = zjs.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(OrderDetailsInsuranceActivity.this).inflate(R.layout.item_zj, parent, false);
            }
            ImageView item_img = ViewHolder.get(convertView, R.id.item_company_choice_img);
            item_img.setTag(position);
            item_img.setOnClickListener(myImgClick);
            LogUtil.i("");
            CommonUtil.loadImageFromUrl(convertView.getContext(), item_img, zj.getUrl());
            return convertView;
        }

        View.OnClickListener myImgClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = Integer.parseInt(v.getTag() + "");
                Intent intent = new Intent(OrderDetailsInsuranceActivity.this, ImageGalleryActivity.class);

                ArrayList<String> img_gallery_Urls = new ArrayList<String>();
                for (ZjBean b : zjs) {
                    img_gallery_Urls.add(b.getUrl());
                }

                intent.putStringArrayListExtra("images", img_gallery_Urls);
                intent.putExtra("position", index);
                startActivity(intent);
            }
        };

    }
}
