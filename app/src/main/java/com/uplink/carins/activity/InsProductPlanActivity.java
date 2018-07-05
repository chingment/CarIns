package com.uplink.carins.activity;


import android.content.Intent;
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
import com.uplink.carins.model.api.InsPlanBean;
import com.uplink.carins.model.api.InsPlanProductSkuBean;
import com.uplink.carins.model.api.ItemFieldBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.ViewHolder;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class InsProductPlanActivity extends SwipeBackActivity implements View.OnClickListener {

    private String TAG = "InsProductPlanActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;

    private String referenceId = "";
    private String title = "";

    private ImageView img_banner;
    private ListView list_plan;
    private InsPlanBean insPlanBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insproductplan);
        referenceId = getIntent().getStringExtra("referenceId");
        title = getIntent().getStringExtra("title");
        initView();
        initEvent();
        loadData();
    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText(title);

        img_banner = (ImageView) findViewById(R.id.img_banner);
        list_plan = (ListView) findViewById(R.id.list_plan);
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

    public void setView(InsPlanBean bean) {
        insPlanBean = bean;

        if(StringUtil.isEmptyNotNull(bean.getBannerImgUrl())) {
            img_banner.setVisibility(View.GONE);
        }
        else {
            CommonUtil.loadImageFromUrl(InsProductPlanActivity.this, img_banner, bean.getBannerImgUrl());
            img_banner.setVisibility(View.VISIBLE);
        }

        InsPlanProductSkuAdapter adapter = new InsPlanProductSkuAdapter();
        list_plan.setAdapter(adapter);
        adapter.setData(bean.getProductSkus());

    }

    private void loadData() {

        Map<String, String> params = new HashMap<>();
        params.put("userId", this.getAppContext().getUser().getId() + "");
        params.put("merchantId", this.getAppContext().getUser().getMerchantId() + "");
        params.put("posMachineId", this.getAppContext().getUser().getPosMachineId() + "");
        params.put("productId", referenceId);

        getWithMy(Config.URL.insPrdGetPlan, params, false, "", new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                ApiResultBean<InsPlanBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<InsPlanBean>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {
                    setView(rt.getData());
                }
            }
        });
    }

    private class InsPlanProductSkuAdapter extends BaseAdapter {

        private List<InsPlanProductSkuBean> beans;

        InsPlanProductSkuAdapter() {

            this.beans = new ArrayList<>();
        }

        public void setData(List<InsPlanProductSkuBean> beans) {
            this.beans = beans;

            notifyDataSetChanged();
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
            InsPlanProductSkuBean bean = beans.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(InsProductPlanActivity.this).inflate(R.layout.item_list_plan, parent, false);
            }
            LinearLayout box = ViewHolder.get(convertView, R.id.item_box);
            TextView txt_name = ViewHolder.get(convertView, R.id.item_name);
            ListView list_fields = ViewHolder.get(convertView, R.id.item_fields);
            txt_name.setText(bean.getProductSkuName());

            AttrItemsAdapter attrItemsAdapter = new AttrItemsAdapter();
            list_fields.setAdapter(attrItemsAdapter);
            attrItemsAdapter.setData(bean.getAttrItems());
            CommonUtil.setListViewHeightBasedOnChildren(list_fields);

            box.setTag(position);
            box.setOnClickListener(clickListener);

            list_fields.setFocusable(false);
            list_fields.setClickable(false);
            list_fields.setPressed(false);
            list_fields.setEnabled(false);

            //list_fields.setTag(position);
            //list_fields.setOnClickListener(clickListener);
            return convertView;
        }

        private View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = Integer.parseInt(v.getTag().toString());
                Intent l_Intent = new Intent(InsProductPlanActivity.this, InsProductPlanDetailsActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("dataBean", insPlanBean);
                l_Intent.putExtras(b);
                l_Intent.putExtra("checkPosition", position);
                startActivity(l_Intent);
            }
        };
    }

    private class AttrItemsAdapter extends BaseAdapter {
        private List<ItemFieldBean> beans;

        public void setData(List<ItemFieldBean> beans) {
            this.beans = beans;
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
                convertView = LayoutInflater.from(InsProductPlanActivity.this).inflate(R.layout.item_attritem_field, parent, false);
            }

            TextView txt_name = ViewHolder.get(convertView, R.id.item_order_field_name);
            TextView txt_value = ViewHolder.get(convertView, R.id.item_order_field_value);

            txt_name.setText(bean.getField() + "");
            txt_value.setText(bean.getValue() + "");

            return convertView;
        }


    }
}
