package com.uplink.carins.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.appcompat.BuildConfig;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.reflect.TypeToken;
import com.uplink.carins.Own.AppManager;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.activity.OrderDetailsApplyLossAssessActivity;
import com.uplink.carins.activity.OrderDetailsCarClaimsActivity;
import com.uplink.carins.activity.OrderDetailsCarInsrueActivity;
import com.uplink.carins.activity.OrderDetailsServiceFeeActivity;
import com.uplink.carins.activity.OrderDetailsTalentDemandActivity;
import com.uplink.carins.activity.OrderListActivity;
import com.uplink.carins.activity.adapter.OrderListAdapter;
import com.uplink.carins.http.HttpClient;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.OrderListBean;
import com.uplink.carins.ui.BaseLazyFragment;
import com.uplink.carins.ui.refreshview.ItemDivider;
import com.uplink.carins.ui.refreshview.SuperRefreshLayout;
import com.uplink.carins.ui.refreshview.OnRefreshHandler;
import com.uplink.carins.utils.GsonUtil;
import com.uplink.carins.utils.LogUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;


public class OrderListFragment extends BaseLazyFragment {

    private String TAG = "OrderListFragment";
    private OrderListActivity context;
    private SuperRefreshLayout refresh;
    private RecyclerView mylistview;
    private OrderListAdapter adapter;

    private int productType = 0;//产品类型
    private int pageIndex = 0;
    private int status = 0;//订单状态，0：全部，1：已提交，2：跟进中，3：待支付，4：已完成,5:已取消
    private boolean mHasLoadedOnce;//是否已被加载过一次，第二次就不再去请求数据了
    private View view;

    public static OrderListFragment newInstance(int status,int productType) {
        Bundle bundle = new Bundle();
        bundle.putInt("status", status);
        bundle.putInt("productType", productType);
        OrderListFragment fragment = new OrderListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_orderlist, container, false);
        status = getArguments().getInt("status");
        productType = getArguments().getInt("productType");


        LogUtil.e("当前状态:" + status);
        LogUtil.e("当前类型4333333:" + productType);

        LogUtil.i(TAG, "onCreateView()");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = (OrderListActivity) getActivity();
        LogUtil.i(TAG, "onActivityCreated()");
        initView();
    }

    void initView() {

        refresh = (SuperRefreshLayout) view.findViewById(R.id.refresh);
        mylistview = (RecyclerView) view.findViewById(R.id.mylistview);

        TextView txt1 = (TextView) view.findViewById(R.id.tx1);

        txt1.setText("状态：" + status + "");

        mylistview.setLayoutManager(new LinearLayoutManager(context));
        mylistview.addItemDecoration(new ItemDivider().setDividerWith(16).setDividerColor(getResources().getColor(R.color.default_bg)));
        adapter = new OrderListAdapter();
        adapter.setOnButtonClickListener(new OrderListAdapter.onButtonClickListener() {
            @Override
            public void openOrderDetail(OrderListBean dataBean) {
                int status = dataBean.getStatus();
                // 2011车险投保 2012车险续保 2013车险理赔
                int productType = dataBean.getProductType();
                Intent intent = null;

                switch (productType) {
                    case 2011:
                        intent = new Intent(context, OrderDetailsCarInsrueActivity.class);
                        break;
                    case 2013:
                        intent = new Intent(context, OrderDetailsCarClaimsActivity.class);
                        break;
                    case 301:
                        intent = new Intent(context, OrderDetailsServiceFeeActivity.class);
                        break;
                    case 401:
                        intent = new Intent(context, OrderDetailsTalentDemandActivity.class);
                        break;
                    case 501:
                        intent = new Intent(context, OrderDetailsApplyLossAssessActivity.class);
                        break;
                }


                if (intent != null) {
                    Bundle b = new Bundle();
                    b.putSerializable("dataBean", dataBean);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            }

        });
        refresh.setAdapter(mylistview, adapter);
        refresh.setOnRefreshHandler(new OnRefreshHandler() {
            @Override
            public void refresh() {
                LogUtil.i("refresh()---->status:" + status);
                context.setRefreshOne(status, false);
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

    @Override
    public void onResume() {
        super.onResume();
        if (!mHasLoadedOnce && context.isFirstLoad && context.currentStatus == status) {
            mHasLoadedOnce = true;
            refresh.setRefreshing(true);
            pageIndex = 0;
            onLoadData();
            context.isFirstLoad = false;
            context.setRefreshOne(status, false);
        } else if (context.getRefreshFlag(status) && context.isFirstLoad && context.currentStatus == status) {
            context.setRefreshOne(status, false);
            refresh.setRefreshing(true);
            pageIndex = 0;
            context.isFirstLoad = false;
            onLoadData();
        }

    }

    private void onLoadData() {
        LogUtil.i(TAG, "onLoadData()");
        Map<String, String> params = new HashMap<>();

        LogUtil.i(" context.getAppContext().getUser().getId():" + context.getAppContext().getUser().getId());

        params.put("userId", context.getAppContext().getUser().getId() + "");
        params.put("merchantId", context.getAppContext().getUser().getMerchantId() + "");
        params.put("posMachineId", context.getAppContext().getUser().getPosMachineId() + "");
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("status", String.valueOf(status));

        LogUtil.e("当前类型224454554545:" + productType);

        params.put("productType", String.valueOf(productType));

        HttpClient.getWithMy(Config.URL.getOrderList, params, new onLoadDataCallBack());
    }

    @Override
    protected void lazyLoad() {
        LogUtil.i(TAG, "lazyLoad()=====status>>>>" + status);
        LogUtil.i(TAG, "lazyLoad()=====mHasLoadedOnce>>>>" + mHasLoadedOnce);

        if (!mHasLoadedOnce && context != null) {
            mHasLoadedOnce = true;
            refresh.setRefreshing(true);
            pageIndex = 0;
            onLoadData();
            context.setRefreshOne(status, false);
        } else if (context != null && context.getRefreshFlag(status)) {
            context.setRefreshOne(status, false);
            refresh.setRefreshing(true);
            pageIndex = 0;
            onLoadData();
        }

    }

    class onLoadDataCallBack extends HttpResponseHandler {
        @Override
        public void onSuccess(String response) {
            super.onSuccess(response);

            LogUtil.i(TAG, "onSuccess====>>>" + response);


            ApiResultBean<List<OrderListBean>> result = JSON.parseObject(response, new TypeReference<ApiResultBean<List<OrderListBean>>>() {
            });

            boolean isHasData = false;
            List<OrderListBean> data = result.getData();
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
                    adapter.setData(new ArrayList<OrderListBean>());
                    refresh.setRefreshing(false);
                }
            }
        }

        @Override
        public void onFailure(Request request, Exception e) {
            super.onFailure(request, e);
            LogUtil.e(TAG, "onFailure====>>>" + e.getMessage());

            if (pageIndex > 0) {
                refresh.loadError();
            } else {
                refresh.setRefreshing(false);
            }
        }
    }
}
