package com.uplink.carins.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.activity.adapter.ProductSkuAdapter;
import com.uplink.carins.activity.adapter.TestAdapter;
import com.uplink.carins.http.HttpClient;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.ProductChildKindBean;
import com.uplink.carins.model.api.ProductKindBean;
import com.uplink.carins.model.api.ProductListBean;
import com.uplink.carins.model.api.ProductSkuBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.refreshview.ItemDivider;
import com.uplink.carins.ui.refreshview.OnRefreshHandler;
import com.uplink.carins.ui.refreshview.SuperRefreshLayout;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class ProductListByGoodsActivity extends SwipeBackActivity implements View.OnClickListener {

    private String TAG = "ProductListByGoodsActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;

    private SuperRefreshLayout list_refresh;
    private RecyclerView list_data;
    private ProductSkuAdapter list_adapter;
    private int list_pageIndex = 0;
    private View data_empty_tip;
    private ProductChildKindBean productChildKind;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productlistbygoods);


        productChildKind = (ProductChildKindBean) getIntent().getSerializableExtra("dataBean");

        initView();
        initEvent();


//        myAdapter = new TestAdapter(this, mData);
//        list_data.setAdapter(myAdapter);//设置适配器
//
//
//        //设置布局管理器 , 将布局设置成纵向
////        LinearLayoutManager linerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
////        list_data.setLayoutManager(linerLayoutManager);
//
//        list_data.setLayoutManager(new GridLayoutManager(this, 3));
//
//        //设置分隔线
//        //mRecycleView.addItemDecoration(new DividerItemDecoration(this , DividerItemDecoration.VERTICAL_LIST));
//
//        //设置增加或删除条目动画
//        list_data.setItemAnimator(new DefaultItemAnimator());

    }

    private void initView() {

        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText(productChildKind.getName());

        list_refresh = (SuperRefreshLayout) findViewById(R.id.list_refresh);
        list_data = (RecyclerView) findViewById(R.id.list_data);
        data_empty_tip = (View) findViewById(R.id.data_empty_tip);
        ;
    }


    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);

        //list_data.setLayoutManager(new LinearLayoutManager(this));
        //list_data.addItemDecoration(new ItemDivider().setDividerWith(16).setDividerColor(getResources().getColor(R.color.default_bg)));

        list_data.setLayoutManager(new GridLayoutManager(getAppContext(), 2));

        list_data.setItemAnimator(new DefaultItemAnimator());

        list_adapter = new ProductSkuAdapter();

        list_refresh.setAdapter(list_data, list_adapter);
        list_refresh.setOnRefreshHandler(new OnRefreshHandler() {
            @Override
            public void refresh() {
                list_refresh.setRefreshing(true);
                list_pageIndex = 0;
                onLoadData();
            }

            @Override
            public void loadMore() {
                super.loadMore();
                list_pageIndex++;
                onLoadData();
            }
        });

        list_pageIndex = 0;
        onLoadData();
    }

    private void onLoadData() {

        Map<String, String> params = new HashMap<>();

        params.put("userId", getAppContext().getUser().getId() + "");
        params.put("merchantId", getAppContext().getUser().getMerchantId() + "");
        params.put("posMachineId", getAppContext().getUser().getPosMachineId() + "");
        params.put("pageIndex", String.valueOf(list_pageIndex));
        params.put("type", "1");
        params.put("categoryId", "0");
        params.put("kindId", productChildKind.getId() + "");
        params.put("name", "");


        HttpClient.getWithMy(Config.URL.mallProductGetSkuList, params, new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                ApiResultBean<List<ProductSkuBean>> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<List<ProductSkuBean>>>() {
                });


                if (rt.getResult() == Result.SUCCESS) {

                    List<ProductSkuBean> list = rt.getData();

                    if (list != null && list.size() > 0) {
                         list_refresh.setVisibility(View.VISIBLE);
                         data_empty_tip.setVisibility(View.GONE);
                        if (list_pageIndex == 0) {
                            list_refresh.setRefreshing(false);
                            list_refresh.loadComplete(true);
                            list_adapter.setData(list, ProductListByGoodsActivity.this);
                        } else {
                            list_refresh.loadComplete(true);
                            list_adapter.addData(list, ProductListByGoodsActivity.this);
                        }
                    } else {
                        list_refresh.setVisibility(View.GONE);
                        data_empty_tip.setVisibility(View.VISIBLE);
                        if (list_pageIndex == 0) {
                            list_refresh.setRefreshing(false);
                            list_adapter.setData(new ArrayList<ProductSkuBean>(), ProductListByGoodsActivity.this);
                        }
                        list_refresh.loadComplete(false);
                    }


                }

            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
        }
    }
}
