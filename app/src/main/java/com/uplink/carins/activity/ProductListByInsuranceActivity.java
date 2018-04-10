package com.uplink.carins.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.activity.adapter.ProductListAdapter;
import com.uplink.carins.fragment.OrderListFragment;
import com.uplink.carins.http.HttpClient;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.OrderListBean;
import com.uplink.carins.model.api.ProductListBean;
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

public class ProductListByInsuranceActivity extends SwipeBackActivity implements View.OnClickListener {

    private String TAG = "ProductListByInsuranceActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;

    private SuperRefreshLayout refresh;
    private RecyclerView mylistview;
    private ProductListAdapter adapter;
    private int pageIndex = 0;
    private boolean mHasLoadedOnce;//是否已被加载过一次，第二次就不再去请求数据了
    private boolean refreshFlag = false;//刷新标识

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
                setRefreshOne(false);
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
    }

    public void setRefreshOne(boolean flag) {
        refreshFlag = false;
    }

    private void onLoadData() {

        LogUtil.e("onLoadData 数据，第" + pageIndex + "页");
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

            ApiResultBean<List<ProductListBean>> result = JSON.parseObject(response, new TypeReference<ApiResultBean<List<ProductListBean>>>() {
            });

            boolean isHasData = false;
            List<ProductListBean> data = result.getData();
            if (data != null) {
                if (data.size() > 0) {
                    isHasData = true;
                }
            }

            if (isHasData) {

                if (pageIndex == 0) {
                    refresh.setRefreshing(false);
                    adapter.setData(data);

                } else {
                    adapter.addData(data);
                }
                adapter.notifyDataSetChanged();

                if (data.size() > 5) {
                    refresh.loadComplete(true);
                } else {
                    refresh.loadComplete(false);
                }

            } else {
                if (pageIndex > 0) {
                    refresh.loadComplete(false);
                } else {
                    adapter.setData(new ArrayList<ProductListBean>());
                    refresh.setRefreshing(false);
                }
            }
        }

        @Override
        public void onFailure(Request request, Exception e) {
            super.onFailure(request, e);

            if (pageIndex > 0) {
                refresh.loadError();
            } else {
                refresh.setRefreshing(false);
            }
        }
    }
}
