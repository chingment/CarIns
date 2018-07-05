package com.uplink.carins.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.uplink.carins.Own.AppContext;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.http.HttpClient;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.ProductListBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.refreshview.ItemDivider;
import com.uplink.carins.ui.refreshview.MyViewHolder;
import com.uplink.carins.ui.refreshview.OnRefreshHandler;
import com.uplink.carins.ui.refreshview.RefreshAdapter;
import com.uplink.carins.ui.refreshview.SuperRefreshLayout;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class ProductListByInsuranceActivity extends SwipeBackActivity implements View.OnClickListener {

    private String TAG = "ProductListByInsuranceActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;

    private SuperRefreshLayout refresh;
    private RecyclerView mylistview;
    private ProductListAdapter adapter;
    private int pageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productlistbyinsurance);
        initView();
        initEvent();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
        }
    }

    private void initView() {

        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("保险产品");

        refresh = (SuperRefreshLayout) findViewById(R.id.refresh);
        mylistview = (RecyclerView) findViewById(R.id.mylistview);

    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);

        mylistview.setLayoutManager(new LinearLayoutManager(this));
        mylistview.addItemDecoration(new ItemDivider().setDividerWith(16).setDividerColor(getResources().getColor(R.color.default_bg)));
        adapter = new ProductListAdapter();
        refresh.setAdapter(mylistview, adapter);
        refresh.setOnRefreshHandler(new OnRefreshHandler() {
            @Override
            public void refresh() {
                refresh.setRefreshing(true);
                pageIndex = 0;
                onLoadData();
            }

            @Override
            public void loadMore() {
                super.loadMore();
                pageIndex++;
                onLoadData();
            }
        });

        pageIndex = 0;
        onLoadData();
    }

    private void onLoadData() {


        Map<String, String> params = new HashMap<>();

        params.put("userId", getAppContext().getUser().getId() + "");
        params.put("merchantId", getAppContext().getUser().getMerchantId() + "");
        params.put("posMachineId", getAppContext().getUser().getPosMachineId() + "");
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("type", "0");
        params.put("categoryId", "0");
        params.put("kindId", "0");
        params.put("name", "");

        HttpClient.getWithMy(Config.URL.getProductList, params, new onLoadDataCallBack());
    }

    private class onLoadDataCallBack extends HttpResponseHandler {
        @Override
        public void onSuccess(String response) {
            super.onSuccess(response);

            LogUtil.e("=>>>>>"+response);

            ApiResultBean<List<ProductListBean>> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<List<ProductListBean>>>() {
            });


            if (rt.getResult() == Result.SUCCESS) {

                List<ProductListBean> list = rt.getData();

                if (list != null && list.size() > 0) {
                    if (pageIndex == 0) {
                        refresh.setRefreshing(false);
                        refresh.loadComplete(true);
                        adapter.setData(list);
                    } else {
                        refresh.loadComplete(true);
                        adapter.addData(list);
                    }
                } else {
                    if (pageIndex == 0) {
                        refresh.setRefreshing(false);
                        adapter.setData(new ArrayList<ProductListBean>());
                    }
                    refresh.loadComplete(false);
                }

            }

        }

        @Override
        public void onFailure(Request request, Exception e) {
            super.onFailure(request, e);
            refresh.loadError();
            refresh.setRefreshing(false);
        }
    }

    private class ProductListAdapter extends RefreshAdapter {

        private List<ProductListBean> data = new ArrayList<>();
        private LayoutInflater inflater;

        public void setData(List<ProductListBean> data) {
            this.data = data;

            notifyDataSetChanged();

        }

        public void addData(List<ProductListBean> data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }

        @Override
        public MyViewHolder onCreateItemHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produtlist_1, parent, false)) {
            };
        }

        @Override
        public void onBindItemHolder(final RecyclerView.ViewHolder holder, int position) {

            ProductListBean bean = data.get(position);

            ImageView txt_product_mainimg = (ImageView) holder.itemView.findViewById(R.id.item_product_mainimg);
            TextView txt_product_name = (TextView) holder.itemView.findViewById(R.id.item_product_name);
            TextView txt_product_briefintro = (TextView) holder.itemView.findViewById(R.id.item_product_briefintro);
            TextView txt_product_showprice = (TextView) holder.itemView.findViewById(R.id.item_product_showprice);

            txt_product_name.setText(bean.getName() + "");
            txt_product_briefintro.setText(bean.getBriefIntro() + "");
            txt_product_showprice.setText(bean.getShowPrice() + "");
            txt_product_mainimg.setTag(position + "");
            CommonUtil.loadImageFromUrl(AppContext.getInstance(), txt_product_mainimg, bean.getMainImg().toString());

            txt_product_mainimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = Integer.parseInt(v.getTag() + "");
                    ProductListBean dataBean = data.get(position);

                    Intent intent = new Intent(ProductListByInsuranceActivity.this, ProductDetailsByInsuranceActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("dataBean", dataBean);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getCount() {
            return data.size();
        }


    }

}
