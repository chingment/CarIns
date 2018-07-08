package com.uplink.carins.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.activity.MallCartActivityActivity;
import com.uplink.carins.activity.MallMainActivity;
import com.uplink.carins.activity.NwCarInsCompanyOfferResultActivity;
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
import com.uplink.carins.activity.adapter.MyViewPagerAdapter;
import com.uplink.carins.activity.adapter.OrderListAdapter;
import com.uplink.carins.http.HttpClient;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.CartProductSkuBean;
import com.uplink.carins.model.api.CartProductSkuByOpreateBean;
import com.uplink.carins.model.api.NwCarInsCompanyBean;
import com.uplink.carins.model.api.NwCarInsGetFollowStatusBean;
import com.uplink.carins.model.api.NwCarInsGetInsInsureInfoBean;
import com.uplink.carins.model.api.OrderListBean;
import com.uplink.carins.model.api.OrderType;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.BaseFragment;
import com.uplink.carins.ui.BaseLazyFragment;
import com.uplink.carins.ui.RadioGroupCheckBean;
import com.uplink.carins.ui.my.MyViewPager;
import com.uplink.carins.ui.refreshview.ItemDivider;
import com.uplink.carins.ui.refreshview.OnRefreshHandler;
import com.uplink.carins.ui.refreshview.SuperRefreshLayout;
import com.uplink.carins.utils.AnimationUtil;
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

public class MallFragment_Order extends BaseFragment {

    private String TAG = "MallFragment_Order";
    private View root;
    private MallMainActivity context;
    private RelativeLayout tab_header;
    private RadioGroup tab_header_status;
    private List<Fragment> tab_fragments = new ArrayList<Fragment>();
    private MyViewPager tab_viewpager;
    private MyViewPagerAdapter tab_viewpager_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return root = inflater.inflate(R.layout.activity_mallorderlist, container, false);
    }

    public void setContext(MallMainActivity context) {
        this.context = context;
    }

    public MallMainActivity getContext() {
        return this.context;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initEvent();
    }

    private void initView() {
        tab_header = (RelativeLayout) root.findViewById(R.id.tab_header);
        tab_header_status = (RadioGroup) root.findViewById(R.id.tab_header_status);
        tab_header_status.setTag(new RadioGroupCheckBean(0, 0));
        tab_viewpager = (MyViewPager) root.findViewById(R.id.tab_viewpager);

        int tab_header_status_count = tab_header_status.getChildCount();

        tab_fragments = new ArrayList<>();

        for (int i = 0; i < tab_header_status_count; i++) {
            RadioButton rb = (RadioButton) tab_header_status.getChildAt(i);
            int status = Integer.parseInt(rb.getTag().toString());
            tab_fragments.add(MallOrderListFragment.newInstance(status, 1));
        }

        tab_viewpager_adapter = new MyViewPagerAdapter(context.getSupportFragmentManager(), tab_fragments);
        tab_viewpager.setPagingEnabled(false);
        tab_viewpager.setAdapter(tab_viewpager_adapter);
        tab_viewpager.setCurrentItem(0, false);
    }

    private void initEvent() {
        tab_header_status.setOnCheckedChangeListener(rb_status_ChangeListener);
    }

    RadioGroup.OnCheckedChangeListener rb_status_ChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            RadioGroupCheckBean bean = (RadioGroupCheckBean) group.getTag();
            int tab_header_status_count = group.getChildCount();
            int tabCurrentPisition = 0;
            for (int i = 0; i < tab_header_status_count; i++) {
                RadioButton tb = (RadioButton) group.getChildAt(i);
                if (tb.getId() == checkedId) {
                    tabCurrentPisition = i;
                }
            }
            bean.setCurrentPosition(tabCurrentPisition);
            group.setTag(bean);
            tab_viewpager.setCurrentItem(tabCurrentPisition, false);
            AnimationUtil.SetTab1ImageSlide2(group, context);

        }
    };
}
