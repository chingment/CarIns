package com.uplink.carins.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.ImgSetBean;
import com.uplink.carins.model.api.OrderInfoBean;
import com.uplink.carins.model.api.ProductListBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.MerchantWebView;
import com.uplink.carins.ui.dialog.CustomConfirmDialog;
import com.uplink.carins.ui.loopviewpager.AutoLoopViewPager;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.ui.viewpagerindicator.CirclePageIndicator;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class ProductDetailsByInsuranceActivity extends SwipeBackActivity implements View.OnClickListener {

    private String TAG = "OrderDetailsServiceFeeActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;
    private ProductListBean product;
    private Button btn_submit;

    private RelativeLayout banner;//banner根节点
    private GalleryPagerAdapter banner_adapter;//banner数据配置
    private AutoLoopViewPager banner_pager;//banner 页面
    private CirclePageIndicator banner_indicator;//banner 底部小图标

    private TextView txt_briefintro;
    private TextView txt_name;
    private TextView txt_showprice;
    private TextView txt_details;

    private MerchantWebView txt_details_webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetailsbyinsurance);

        product = (ProductListBean) getIntent().getSerializableExtra("dataBean");

        initView();
        initEvent();
        setView(product);
    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("产品详情");

        btn_submit = (Button) findViewById(R.id.btn_submit);

        banner = (RelativeLayout) findViewById(R.id.banner);
        banner_pager = (AutoLoopViewPager) findViewById(R.id.banner_pager);
        banner_pager.setFocusable(true);
        banner_pager.setFocusableInTouchMode(true);
        banner_pager.requestFocus();
        banner_indicator = (CirclePageIndicator) findViewById(R.id.banner_indicator);

        txt_briefintro = (TextView) findViewById(R.id.txt_briefintro);
        txt_name = (TextView) findViewById(R.id.txt_showprice);
        txt_showprice = (TextView) findViewById(R.id.txt_showprice);
        //txt_details = (TextView) findViewById(R.id.txt_details);

        txt_details_webView = (MerchantWebView) findViewById(R.id.txt_details_webView);

//        //WebView加载web资源
        txt_details_webView.loadUrl(product.getDetailsUrl());
//        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        txt_details_webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.btn_submit:
                showConfirmDialog();
                break;
        }
    }

    private void submit() {


        Map<String, Object> params = new HashMap<>();
        params.put("userId", this.getAppContext().getUser().getId());
        params.put("merchantId", this.getAppContext().getUser().getMerchantId());
        params.put("posMachineId", this.getAppContext().getUser().getPosMachineId());
        params.put("productSkuId", product.getSkuId());

        postWithMy(Config.URL.submitInsurance, params, null, new HttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                LogUtil.i(TAG, "onSuccess====>>>" + response);

                ApiResultBean<OrderInfoBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<OrderInfoBean>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {

                    Intent l_Intent = new Intent(ProductDetailsByInsuranceActivity.this, OrderListActivity.class);
                    l_Intent.putExtra("status", 1);
                    startActivity(l_Intent);

                } else {
                    showToast(rt.getMessage());
                }

                dialog_Success.dismiss();
            }

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
                LogUtil.e(TAG, "onFailure====>>>" + e.getMessage());
                showToast("提交失败");
            }
        });
    }

    private CustomConfirmDialog dialog_Success;

    private void showConfirmDialog() {
        if (dialog_Success == null) {
            dialog_Success = new CustomConfirmDialog(ProductDetailsByInsuranceActivity.this, "确定提交订单？", true);
            dialog_Success.getBtnSure().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submit();
                }
            });
            dialog_Success.getBtnCancle().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_Success.dismiss();
                }
            });
        }
        dialog_Success.show();
    }


    public void setView(ProductListBean bean) {

        banner_adapter = new GalleryPagerAdapter(bean.getDispalyImgs());
        banner_pager.setAdapter(banner_adapter);
        banner_indicator.setViewPager(banner_pager);
        banner_indicator.setPadding(5, 5, 10, 5);


        txt_briefintro.setText(bean.getBriefIntro());
        txt_name.setText(bean.getName());
        txt_showprice.setText(bean.getShowPrice());
    }

    private class GalleryPagerAdapter extends PagerAdapter {

        private List<ImgSetBean> banner;

        GalleryPagerAdapter(List<ImgSetBean> banner) {
            this.banner = banner;
        }

        @Override
        public int getCount() {
            return banner.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImgSetBean bannerBean = banner.get(position);

            ImageView item = new ImageView(ProductDetailsByInsuranceActivity.this);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(-1, -1);
            item.setLayoutParams(params);
            item.setScaleType(ImageView.ScaleType.CENTER_CROP);
            item.setTag("" + position);
            CommonUtil.loadImageFromUrl(ProductDetailsByInsuranceActivity.this, item, bannerBean.getImgUrl() + "");
            container.addView(item);

            return item;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }
    }
}
