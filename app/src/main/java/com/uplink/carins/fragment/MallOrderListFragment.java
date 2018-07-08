package com.uplink.carins.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.activity.MallMainActivity;
import com.uplink.carins.activity.OrderDetailsApplyLossAssessActivity;
import com.uplink.carins.activity.OrderDetailsCarClaimsActivity;
import com.uplink.carins.activity.OrderDetailsCreditActivity;
import com.uplink.carins.activity.OrderDetailsInsuranceActivity;
import com.uplink.carins.activity.OrderDetailsLllegalDealtActivity;
import com.uplink.carins.activity.OrderDetailsLllegalQueryRechargeActivity;
import com.uplink.carins.activity.OrderDetailsServiceFeeActivity;
import com.uplink.carins.activity.OrderDetailsShoppingActivity;
import com.uplink.carins.activity.OrderDetailsTalentDemandActivity;
import com.uplink.carins.activity.WebViewActivity;
import com.uplink.carins.activity.adapter.OrderListAdapter;
import com.uplink.carins.http.HttpClient;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.OrderListBean;
import com.uplink.carins.model.api.OrderType;
import com.uplink.carins.ui.BaseLazyFragment;
import com.uplink.carins.ui.crop.util.PaintUtil;
import com.uplink.carins.ui.refreshview.ItemDivider;
import com.uplink.carins.ui.refreshview.OnRefreshHandler;
import com.uplink.carins.ui.refreshview.SuperRefreshLayout;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by chingment on 2018/7/8.
 */

public class MallOrderListFragment extends BaseLazyFragment {

    private String TAG = "MallOrderListFragment";
    private MallMainActivity context;
    private SuperRefreshLayout refresh;
    private RecyclerView mylistview;
    private OrderListAdapter adapter;

    private int orderType = 0;//订单类型
    private int pageIndex = 0;
    private int status = 0;//订单状态，0：全部，1：已提交，2：跟进中，3：待支付，4：已完成,5:已取消
    private View view;
    private View data_empty_tip;

    public static MallOrderListFragment newInstance(int status, int orderType) {
        Bundle bundle = new Bundle();
        bundle.putInt("status", status);
        bundle.putInt("orderType", orderType);
        MallOrderListFragment fragment = new MallOrderListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_orderlist, container, false);
        status = getArguments().getInt("status");
        orderType = getArguments().getInt("orderType");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = (MallMainActivity) getActivity();
        initView();
    }

    void initView() {

        refresh = (SuperRefreshLayout) view.findViewById(R.id.refresh);
        mylistview = (RecyclerView) view.findViewById(R.id.mylistview);
        data_empty_tip = (View) view.findViewById(R.id.data_empty_tip);
        mylistview.setLayoutManager(new LinearLayoutManager(context));
        mylistview.addItemDecoration(new ItemDivider().setDividerWith(16).setDividerColor(getResources().getColor(R.color.default_bg)));
        adapter = new OrderListAdapter();

        adapter.setOnButtonClickListener(new OrderListAdapter.onButtonClickListener() {
            @Override
            public void openOrderDetail(OrderListBean dataBean) {
                Bundle b = new Bundle();
                Intent intent = new Intent(context, OrderDetailsShoppingActivity.class);
                b.putSerializable("dataBean", dataBean);
                intent.putExtras(b);
                startActivity(intent);
            }

        });


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

    }

    private void onLoadData() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", context.getAppContext().getUser().getId() + "");
        params.put("merchantId", context.getAppContext().getUser().getMerchantId() + "");
        params.put("posMachineId", context.getAppContext().getUser().getPosMachineId() + "");
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("status", String.valueOf(status));
        params.put("type", String.valueOf(orderType));
        HttpClient.getWithMy(Config.URL.getOrderList, params, new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);


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
                    refresh.setVisibility(View.VISIBLE);
                    data_empty_tip.setVisibility(View.GONE);
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

                        refresh.setVisibility(View.GONE);
                        data_empty_tip.setVisibility(View.VISIBLE);

                        adapter.setData(new ArrayList<OrderListBean>());
                        refresh.setRefreshing(false);
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh.setRefreshing(true);
        pageIndex = 0;
        onLoadData();
    }

    @Override
    public void lazyLoad() {

    }

}
