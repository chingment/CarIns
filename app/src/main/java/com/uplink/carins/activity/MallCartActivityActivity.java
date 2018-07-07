package com.uplink.carins.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.uplink.carins.Own.AppManager;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.activity.adapter.CartProductSkuAdapter;
import com.uplink.carins.http.HttpClient;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.CartComfirmOrderData;
import com.uplink.carins.model.api.CartOperateType;
import com.uplink.carins.model.api.CartPageDataBean;
import com.uplink.carins.model.api.CartProductSkuByOpreateBean;
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

public class MallCartActivityActivity extends SwipeBackActivity implements View.OnClickListener {
    private final static String TAG = "MallCartActivityActivity";

    public static CartShoppingDataBean mCartShoppingData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mallfragment_cart);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;

        }
    }


    public static void operate(final BaseFragmentActivity context, int type, int skuId) {


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

                ApiResultBean<CartShoppingDataBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<CartShoppingDataBean>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {
                    setCartView(context, rt.getData());
                }
            }

        });
    }

    public static void getPageData(final BaseFragmentActivity context) {


        Map<String, String> params = new HashMap<>();
        params.put("userId", context.getAppContext().getUser().getId() + "");
        params.put("merchantId", context.getAppContext().getUser().getMerchantId() + "");
        params.put("posMachineId", context.getAppContext().getUser().getPosMachineId() + "");
        HttpClient.getWithMy(Config.URL.mallCartGetPageData, params, new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                ApiResultBean<CartPageDataBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<CartPageDataBean>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {
                    setCartView(context, rt.getData().getShoppingData());
                }

            }

        });
    }


    public static void goComfirmPage(final BaseFragmentActivity context, List<CartProductSkuByOpreateBean> skus) {


        Map<String, Object> params = new HashMap<>();
        params.put("userId", context.getAppContext().getUser().getId());
        params.put("merchantId", context.getAppContext().getUser().getMerchantId());
        params.put("posMachineId", context.getAppContext().getUser().getPosMachineId());

        JSONArray jsonObj_Skus = new JSONArray();

        try {

            for (CartProductSkuByOpreateBean sku :
                    skus) {

                JSONObject jsonFa1 = new JSONObject();
                jsonFa1.put("cartId", sku.getCartId());
                jsonFa1.put("skuId", sku.getSkuId());
                jsonFa1.put("quantity", sku.getQuantity());
                jsonObj_Skus.put(jsonFa1);
            }


        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }


        params.put("skus", jsonObj_Skus);


        HttpClient.postWithMy(Config.URL.mallCartGetComfirmOrderData, params, null, new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);


                ApiResultBean<CartComfirmOrderData> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<CartComfirmOrderData>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {

                    Intent l_Intent1 = new Intent(context, MallOrderConfirmActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("dataBean", rt.getData());
                    l_Intent1.putExtras(b);

                    context.startActivity(l_Intent1);
                } else {
                    context.showToast(rt.getMessage());
                }


            }
        });


    }

    private static void setCartView(final BaseFragmentActivity context, CartShoppingDataBean bean) {

        mCartShoppingData = bean;

        LinkedList<Activity> activityStack = AppManager.getAppManager().getActivityStack();


        for (Activity activity : activityStack) {
            LogUtil.e("activity instanceof" + activity.getLocalClassName());
            if (activity instanceof ProductDetailsByGoodsActivity) {

                ProductDetailsByGoodsActivity ac = (ProductDetailsByGoodsActivity) activity;
                TextView txt_cartcount = (TextView) ac.findViewById(R.id.txt_cartcount);

                if (txt_cartcount != null) {
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

            if (activity instanceof MallMainActivity) {

                MallMainActivity ac = (MallMainActivity) activity;
                if (ac != null) {
                    TextView txt_countbyselected = (TextView) ac.findViewById(R.id.txt_countbyselected);
                    TextView txt_sumpricebyselected = (TextView) ac.findViewById(R.id.txt_sumpricebyselected);
                    ListView list_skus = (ListView) ac.findViewById(R.id.list_skus);
                    ImageView data_empty_tip = (ImageView) ac.findViewById(R.id.data_empty_tip);

                    if (txt_countbyselected != null) {
                        txt_countbyselected.setText(bean.getCountBySelected() + "");
                    }

                    if (txt_sumpricebyselected != null) {
                        txt_sumpricebyselected.setText(bean.getSumPriceBySelected() + "");
                    }

                    if (list_skus != null) {

                        if(bean.getSkus()!=null) {
                            if (bean.getSkus().size() > 0) {
                                list_skus.setVisibility(View.VISIBLE);
                                data_empty_tip.setVisibility(View.GONE);
                            } else {
                                list_skus.setVisibility(View.GONE);
                                data_empty_tip.setVisibility(View.VISIBLE);
                            }
                        }

                        CartProductSkuAdapter productSkuAdapter = new CartProductSkuAdapter(ac, bean.getSkus());
                        list_skus.setAdapter(productSkuAdapter);
                    }
                }

            }
        }
    }


}
