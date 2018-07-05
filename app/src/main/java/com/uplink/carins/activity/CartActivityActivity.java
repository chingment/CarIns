package com.uplink.carins.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.uplink.carins.Own.AppManager;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.activity.handler.CarOperateHandler;
import com.uplink.carins.http.HttpClient;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.CarInsKindBean;
import com.uplink.carins.model.api.CartOperateType;
import com.uplink.carins.model.api.CartShoppingDataBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.BaseFragmentActivity;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class CartActivityActivity extends SwipeBackActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartactivity);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;

        }
    }

    public static void operate(final BaseFragmentActivity context, int type, String skuId) {


        switch (type) {
            case CartOperateType.INCREASE:

                break;
            case CartOperateType.DECREASE:

                break;
            case CartOperateType.DELETE:

                break;
        }


        Map<String, Object> params = new HashMap<>();
        params.put("userId", context.getAppContext().getUser().getId());
        params.put("merchantId", context.getAppContext().getUser().getMerchantId());
        params.put("posMachineId", context.getAppContext().getUser().getPosMachineId());
        params.put("operate", type + "");


        JSONArray jsonObj_Skus = new JSONArray();

        try {

            JSONObject jsonFa1 = new JSONObject();
            jsonFa1.put("skuId", skuId);
            jsonFa1.put("quantity", "1");
            jsonFa1.put("selected", "1");
            jsonObj_Skus.put(jsonFa1);


        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }


        params.put("skus", jsonObj_Skus);


        HttpClient.postWithMy(Config.URL.mallCartOperate, params, null, new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                LogUtil.i("", "onSuccess====>>>" + response);

                ApiResultBean<CartShoppingDataBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<CartShoppingDataBean>>() {
                });


                if (rt.getResult() == Result.SUCCESS) {

                    setCartView(context, rt.getData());
                }

            }
        });

    }

    public static void getShoppingData(final BaseFragmentActivity context) {


        Map<String, String> params = new HashMap<>();
        params.put("userId", context.getAppContext().getUser().getId() + "");
        params.put("merchantId", context.getAppContext().getUser().getMerchantId() + "");
        params.put("posMachineId", context.getAppContext().getUser().getPosMachineId() + "");
        HttpClient.getWithMy(Config.URL.mallCartGetShoppingData, params, new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                LogUtil.i("getShoppingData", "onSuccess====>>>" + response);

                ApiResultBean<CartShoppingDataBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<CartShoppingDataBean>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {
                    setCartView(context, rt.getData());
                }
            }

        });
    }


    private static void setCartView(final BaseFragmentActivity context, CartShoppingDataBean bean) {

        LinkedList<Activity> activityStack = AppManager.getAppManager().getActivityStack();


        for (Activity activity : activityStack) {

            if (activity instanceof ProductDetailsByGoodsActivity) {

                ProductDetailsByGoodsActivity ac = (ProductDetailsByGoodsActivity) context;
                TextView txt_cartcount = (TextView) ac.findViewById(R.id.txt_cartcount);
                if (bean.getCount() <= 0) {
                    txt_cartcount.setVisibility(View.GONE);
                } else {
                    txt_cartcount.setVisibility(View.VISIBLE);

                    if (bean.getCount() < 99) {
                        if (bean.getCount() < 10) {
                            txt_cartcount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
                        } else {
                            txt_cartcount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 6);
                        }
                        txt_cartcount.setText(bean.getCount() + "");
                    } else {
                        txt_cartcount.setText("..");
                    }
                }
            }
        }
    }


}
