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
import com.uplink.carins.activity.NwCarInsCompanyOfferResultActivity;
import com.uplink.carins.activity.NwCarInsInsureActivity;
import com.uplink.carins.activity.NwCarInsInsureResultActivity;
import com.uplink.carins.activity.OrderDetailsApplyLossAssessActivity;
import com.uplink.carins.activity.OrderDetailsCarClaimsActivity;
import com.uplink.carins.activity.OrderDetailsCarInsrueActivity;
import com.uplink.carins.activity.OrderDetailsCreditActivity;
import com.uplink.carins.activity.OrderDetailsInsuranceActivity;
import com.uplink.carins.activity.OrderDetailsLllegalDealtActivity;
import com.uplink.carins.activity.OrderDetailsLllegalQueryRechargeActivity;
import com.uplink.carins.activity.OrderDetailsServiceFeeActivity;
import com.uplink.carins.activity.OrderDetailsShoppingActivity;
import com.uplink.carins.activity.OrderDetailsTalentDemandActivity;
import com.uplink.carins.activity.OrderListActivity;
import com.uplink.carins.activity.WebViewActivity;
import com.uplink.carins.activity.adapter.OrderListAdapter;
import com.uplink.carins.http.HttpClient;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.NwCarInsBaseInfoBean;
import com.uplink.carins.model.api.NwCarInsCompanyBean;
import com.uplink.carins.model.api.NwCarInsCompanyResultBean;
import com.uplink.carins.model.api.NwCarInsGetFollowStatusBean;
import com.uplink.carins.model.api.NwCarInsGetInsInsureInfoBean;
import com.uplink.carins.model.api.NwCarInsInsureResult;
import com.uplink.carins.model.api.OrderListBean;
import com.uplink.carins.model.api.OrderType;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.BaseLazyFragment;
import com.uplink.carins.ui.refreshview.ItemDivider;
import com.uplink.carins.ui.refreshview.SuperRefreshLayout;
import com.uplink.carins.ui.refreshview.OnRefreshHandler;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.StringUtil;

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

    private int orderType = 0;//订单类型
    private int pageIndex = 0;
    private int status = 0;//订单状态，0：全部，1：已提交，2：跟进中，3：待支付，4：已完成,5:已取消
    private boolean mHasLoadedOnce;//是否已被加载过一次，第二次就不再去请求数据了
    private View view;

    public static OrderListFragment newInstance(int status, int productType) {
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
        orderType = getArguments().getInt("productType");
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

        mylistview.setLayoutManager(new LinearLayoutManager(context));
        mylistview.addItemDecoration(new ItemDivider().setDividerWith(16).setDividerColor(getResources().getColor(R.color.default_bg)));
        adapter = new OrderListAdapter();
        adapter.setOnButtonClickListener(new OrderListAdapter.onButtonClickListener() {
            @Override
            public void openOrderDetail(OrderListBean dataBean) {
                int status = dataBean.getStatus();

                int orderType = dataBean.getType();

                Intent intent = null;

                if (StringUtil.isEmpty(dataBean.getDetailsUrl())) {
                    Bundle b = new Bundle();
                    switch (orderType) {
                        case OrderType.CarInsure:

                            goFollow(dataBean);

                            break;
                        case OrderType.CarClaims:
                            intent = new Intent(context, OrderDetailsCarClaimsActivity.class);
                            b.putSerializable("dataBean", dataBean);
                            intent.putExtras(b);
                            startActivity(intent);
                            break;
                        case OrderType.ServiceFee:
                            intent = new Intent(context, OrderDetailsServiceFeeActivity.class);
                            b.putSerializable("dataBean", dataBean);
                            intent.putExtras(b);
                            startActivity(intent);
                            break;
                        case OrderType.TalentDeman:
                            intent = new Intent(context, OrderDetailsTalentDemandActivity.class);
                            b.putSerializable("dataBean", dataBean);
                            intent.putExtras(b);
                            startActivity(intent);
                            break;
                        case OrderType.ApplylossAssess:
                            intent = new Intent(context, OrderDetailsApplyLossAssessActivity.class);
                            b.putSerializable("dataBean", dataBean);
                            intent.putExtras(b);
                            startActivity(intent);
                            break;
                        case OrderType.LllegalQueryRecharg:
                            intent = new Intent(context, OrderDetailsLllegalQueryRechargeActivity.class);
                            b.putSerializable("dataBean", dataBean);
                            intent.putExtras(b);
                            startActivity(intent);
                            break;
                        case OrderType.LllegalDealt:
                            intent = new Intent(context, OrderDetailsLllegalDealtActivity.class);
                            b.putSerializable("dataBean", dataBean);
                            intent.putExtras(b);
                            startActivity(intent);
                            break;
                        case OrderType.Credit:
                            intent = new Intent(context, OrderDetailsCreditActivity.class);
                            b.putSerializable("dataBean", dataBean);
                            intent.putExtras(b);
                            startActivity(intent);
                            break;
                        case OrderType.Insurance:
                            intent = new Intent(context, OrderDetailsInsuranceActivity.class);
                            b.putSerializable("dataBean", dataBean);
                            intent.putExtras(b);
                            startActivity(intent);
                            break;
                        case OrderType.Goods:
                            intent = new Intent(context, OrderDetailsShoppingActivity.class);
                            b.putSerializable("dataBean", dataBean);
                            intent.putExtras(b);
                            startActivity(intent);
                            break;

                    }


                } else {
                    intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("title", "查看详情");
                    intent.putExtra("url", dataBean.getDetailsUrl());
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
        Map<String, String> params = new HashMap<>();
        params.put("userId", context.getAppContext().getUser().getId() + "");
        params.put("merchantId", context.getAppContext().getUser().getMerchantId() + "");
        params.put("posMachineId", context.getAppContext().getUser().getPosMachineId() + "");
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("status", String.valueOf(status));
        params.put("type", String.valueOf(orderType));
        HttpClient.getWithMy(Config.URL.getOrderList, params, new onLoadDataCallBack());
    }

    private void goInsure(final int orderId) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", context.getAppContext().getUser().getId() + "");
        params.put("merchantId", context.getAppContext().getUser().getMerchantId() + "");
        params.put("posMachineId", context.getAppContext().getUser().getPosMachineId() + "");
        params.put("orderId", orderId + "");
        context.getWithMy(Config.URL.carInsGetInsInquiryInfo, params, false, "", new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                LogUtil.i(TAG, "onSuccess====>>>" + response);
                ApiResultBean<NwCarInsCompanyBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<NwCarInsCompanyBean>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {

                    Intent intent = new Intent(context, NwCarInsCompanyOfferResultActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("offerInfo", rt.getData());
                    intent.putExtras(b);
                    startActivity(intent);

                } else {
                    showToast(rt.getMessage());
                }
            }
        });


    }

    private void goPay(final int orderId) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", context.getAppContext().getUser().getId() + "");
        params.put("merchantId", context.getAppContext().getUser().getMerchantId() + "");
        params.put("posMachineId", context.getAppContext().getUser().getPosMachineId() + "");
        params.put("orderId", orderId + "");

        context.getWithMy(Config.URL.carInsGetInsInsureInfo, params, false, "", new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                LogUtil.i(TAG, "onSuccess====>>>" + response);
                ApiResultBean<NwCarInsGetInsInsureInfoBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<NwCarInsGetInsInsureInfoBean>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {

                    Intent intent = new Intent(context, NwCarInsInsureResultActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("offerInfo", rt.getData().getOfferInfo());
                    b.putSerializable("insureInfo", rt.getData().getInsureInfo());
                    intent.putExtras(b);
                    startActivity(intent);

                } else {
                    showToast(rt.getMessage());
                }
            }
        });


    }

    private void goFollow(final OrderListBean order) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", context.getAppContext().getUser().getId() + "");
        params.put("merchantId", context.getAppContext().getUser().getMerchantId() + "");
        params.put("posMachineId", context.getAppContext().getUser().getPosMachineId() + "");
        params.put("orderId", order.getId() + "");

        context.getWithMy(Config.URL.carInsGetFollowStatus, params, false, "", new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                LogUtil.i(TAG, "onSuccess====>>>" + response);
                ApiResultBean<NwCarInsGetFollowStatusBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<NwCarInsGetFollowStatusBean>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {

                    if (StringUtil.isEmptyNotNull(rt.getData().getPartnerOrderId())) {
                        Intent intent = new Intent(context, OrderDetailsCarInsrueActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable("dataBean", order);
                        intent.putExtras(b);
                        startActivity(intent);
                    } else {
                        if (rt.getData().getFollowStatus() == 9) {
                            goInsure(order.getId());
                        }
                        if (rt.getData().getFollowStatus() == 14) {
                            goPay(order.getId());
                        }
                    }
                    
                } else {
                    showToast(rt.getMessage());
                }
            }
        });


    }

    @Override
    protected void lazyLoad() {
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
