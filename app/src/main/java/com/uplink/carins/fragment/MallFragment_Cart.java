package com.uplink.carins.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.activity.MallCartActivityActivity;
import com.uplink.carins.activity.MallMainActivity;
import com.uplink.carins.activity.adapter.ProductKindBodyAdapter;
import com.uplink.carins.activity.adapter.ProductKindNameAdapter;
import com.uplink.carins.http.HttpClient;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.ProductKindBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.BaseFragment;
import com.uplink.carins.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by chingment on 2018/7/5.
 */

public class MallFragment_Cart extends BaseFragment {

    private String TAG = "MallFragment_Cart";
    private View root;
    private LayoutInflater inflater;
    private MallMainActivity context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return root = inflater.inflate(R.layout.mallfragment_cart, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = (MallMainActivity) getActivity();
        inflater = LayoutInflater.from(context);

        MallCartActivityActivity.getPageData(context);
    }
    
}
