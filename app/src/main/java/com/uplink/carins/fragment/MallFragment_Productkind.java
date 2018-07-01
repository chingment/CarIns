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
import com.uplink.carins.activity.MallMainActivity;
import com.uplink.carins.activity.adapter.ProductKindBodyAdapter;
import com.uplink.carins.activity.adapter.ProductKindNameAdapter;
import com.uplink.carins.http.HttpClient;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.OrderDetailsCarClaimsBean;
import com.uplink.carins.model.api.ProductKindBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.BaseFragment;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by chingment on 2018/3/27.
 */

public class MallFragment_Productkind extends BaseFragment {
    private String TAG = "MallFragment_Productkind";
    private View root;
    private LayoutInflater inflater;
    private MallMainActivity context;
    private ListView list_kind_name;
    private ListView list_kind_body;

    private List<ProductKindBean> productKinds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return root = inflater.inflate(R.layout.mallfragment_productkind, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = (MallMainActivity) getActivity();
        inflater = LayoutInflater.from(context);
        initView();
        initEvent();
        loadData();
    }

    private void initView() {

        list_kind_name = (ListView) context.findViewById(R.id.list_kind_name);
        list_kind_body = (ListView) context.findViewById(R.id.list_kind_body);
    }

    private void initEvent() {

        list_kind_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                set_list_kind_name_position(position);
                //list_kind_body.setSelection(position);
            }
        });
    }


    private void set_list_kind_name_position(int position) {

        ProductKindNameAdapter list_kind_name_adapter = new ProductKindNameAdapter(context.getAppContext(), productKinds, position);
        list_kind_name.setAdapter(list_kind_name_adapter);

        ProductKindBean child = productKinds.get(position);

        List<ProductKindBean> childs = new ArrayList<>();

        childs.add(child);

        ProductKindBodyAdapter list_kind_body_adapter = new ProductKindBodyAdapter(context.getAppContext(), childs, position);
        list_kind_body.setAdapter(list_kind_body_adapter);

        //CommonUtil.setListViewHeightBasedOnChildren(list_kind_body);

        //CommonUtil.setListViewHeightBasedOnChildren(list_kind_body);
    }

    private void loadData() {

        Map<String, String> params = new HashMap<>();
        params.put("userId", context.getAppContext().getUser().getId() + "");
        params.put("merchantId", context.getAppContext().getUser().getMerchantId() + "");
        params.put("posMachineId", context.getAppContext().getUser().getPosMachineId() + "");
        HttpClient.getWithMy(Config.URL.mallProductGetKinds, params, new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                LogUtil.i(TAG, "onSuccess====>>>" + response);

                ApiResultBean<List<ProductKindBean>> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<List<ProductKindBean>>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {
                    productKinds = rt.getData();
                    set_list_kind_name_position(0);
                }
            }

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
                LogUtil.e(TAG, "onFailure====>>>" + e.getMessage());
            }
        });
    }
}
