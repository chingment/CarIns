package com.uplink.carins.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uplink.carins.R;
import com.uplink.carins.activity.adapter.BannerAdapter;
import com.uplink.carins.model.api.CartOperateType;
import com.uplink.carins.model.api.ProductListBean;
import com.uplink.carins.model.api.ProductSkuBean;
import com.uplink.carins.ui.BaseFragmentActivity;
import com.uplink.carins.ui.loopviewpager.AutoLoopViewPager;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.ui.viewpagerindicator.CirclePageIndicator;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.StringUtil;

import java.net.URL;

public class ProductDetailsByGoodsActivity extends SwipeBackActivity implements View.OnClickListener {

    private String TAG = "ProductDetailsByGoodsActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;
    private ProductListBean product;

    private BannerAdapter banner_adapter;//banner数据配置
    private AutoLoopViewPager banner_pager;//banner 页面
    private CirclePageIndicator banner_indicator;//banner 底部小图标
    private TextView txt_name;
    private TextView txt_briefInfo;
    private TextView txt_price_unit;
    private TextView txt_price_integer;
    private TextView txt_price_decimal;
    private WebView webview;
    private ProductSkuBean productSku;


    private RelativeLayout btn_cart;
    private LinearLayout btn_increase;
    private LinearLayout btn_buy;
    private TextView txt_cartcount;

    private BaseFragmentActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetailsbygoods);
        context = this;
        productSku = (ProductSkuBean) getIntent().getSerializableExtra("dataBean");
        initView();
        initEvent();
        setData(productSku);
    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("商品详情");

        banner_pager = (AutoLoopViewPager) findViewById(R.id.banner_pager);
        banner_indicator = (CirclePageIndicator) findViewById(R.id.banner_indicator);

        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_briefInfo = (TextView) findViewById(R.id.txt_briefInfo);
        txt_price_unit = (TextView) findViewById(R.id.txt_price_unit);
        txt_price_integer = (TextView) findViewById(R.id.txt_price_integer);
        txt_price_decimal = (TextView) findViewById(R.id.txt_price_decimal);
        txt_cartcount = (TextView) findViewById(R.id.txt_cartcount);

        btn_increase = (LinearLayout) findViewById(R.id.btn_increase);
        btn_buy = (LinearLayout) findViewById(R.id.btn_buy);
        btn_cart = (RelativeLayout) findViewById(R.id.btn_cart);

        webview = (WebView) findViewById(R.id.webview);

        banner_pager.setFocusable(true);
        banner_pager.setFocusableInTouchMode(true);
        banner_pager.requestFocus();
        banner_pager.setInterval(5000);

        banner_indicator.setPadding(5, 5, 10, 5);

    }

    private void initEvent() {

        btnHeaderGoBack.setOnClickListener(this);
        btn_increase.setOnClickListener(this);
        btn_buy.setOnClickListener(this);
        btn_cart.setOnClickListener(this);
    }

    private void setData(ProductSkuBean bean) {


        //LogUtil.e("bean.getDisplayImgs():" + bean.getDisplayImgs().size());

        banner_adapter = new BannerAdapter(ProductDetailsByGoodsActivity.this, bean.getDisplayImgs(), ImageView.ScaleType.CENTER_INSIDE);
        banner_pager.setAdapter(banner_adapter);
        banner_indicator.setViewPager(banner_pager);

        txt_name.setText(bean.getName());
        txt_briefInfo.setText(bean.getBriefIntro());


        String[] price = CommonUtil.getPrice(String.valueOf(bean.getUnitPrice()));
        txt_price_integer.setText(price[0]);
        txt_price_decimal.setText(price[1]);


        String detailsDes = "";
        if (!StringUtil.isEmptyNotNull(bean.getDetailsDesc())) {
            detailsDes = bean.getDetailsDesc();
        }

        String html = "<html><head><title></title></head><body>"
                + detailsDes
                + "</body></html>";

        webview.loadData(html, "text/html", "uft-8");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.btn_increase:
                CartActivityActivity.operate(ProductDetailsByGoodsActivity.this, CartOperateType.INCREASE, productSku.getSkuId());
                break;
            case R.id.btn_buy:
                Intent l_Intent1 = new Intent(ProductDetailsByGoodsActivity.this, MallOrderConfirmActivity.class);
                startActivity(l_Intent1);
                break;
            case R.id.btn_cart:

                Intent l_Intent2 = new Intent(ProductDetailsByGoodsActivity.this, CartActivityActivity.class);
                startActivity(l_Intent2);
                break;
        }
    }


    Html.ImageGetter imgGetter = new Html.ImageGetter() {
        public Drawable getDrawable(String source) {

            Drawable drawable = null;
            URL url;
            try {
                url = new URL(source);

                drawable = Drawable.createFromStream(url.openStream(), ""); // 获取网路图片
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());

            return drawable;
        }
    };
}
