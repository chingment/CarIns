package com.uplink.carins.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.http.HttpClient;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.HandMerchantBean;
import com.uplink.carins.model.api.OrderDetailsCarClaimsBean;
import com.uplink.carins.model.api.OrderListBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.choicephoto.ChoicePhotoAndCropAndSwipeBackActivity;
import com.uplink.carins.utils.BitmapUtil;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class OrderDetailsCarClaimsActivity extends ChoicePhotoAndCropAndSwipeBackActivity {

    private String TAG = "OrderDetailsCarClaimsActivity";

    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;
    private OrderListBean order;

    private TextView txt_order_sn;
    private TextView txt_order_statusname;
    private TextView txt_order_insurancecompanyname;
    private TextView txt_order_repairstype;
    private TextView txt_order_carplateno;
    private TextView txt_order_handperson;
    private TextView txt_order_handpersonphone;
    private TextView txt_order_remarks;
    private TextView txt_order_submittime;
    private TextView txt_order_paytime;
    private TextView txt_order_completetime;
    private TextView txt_order_cancletime;

    private TextView txt_handmerchant_headtitle;
    private TextView txt_handmerchant_name;
    private TextView txt_handmerchant_contact;
    private TextView txt_handmerchant_contactphone;
    private TextView txt_handmerchant_contactaddress;

    private ImageView img_order_estimatelistimgurl;
    private TextView txt_order_workinghoursprice;
    private TextView txt_order_accessoriesprice;
    private TextView txt_order_estimateprice;
    private TextView txt_order_price;

    private Button btn_submit;

    private LinearLayout layout_submittime;
    private LinearLayout layout_paytime;
    private LinearLayout layout_completetime;
    private LinearLayout layout_cancletime;
    private LinearLayout layout_handmerchant;
    private LinearLayout layout_estimatelist;
    private View layout_estimatelist_line;

    private LinearLayout layout_uploadimgs;
    private LinearLayout layout_imgs_row1;
    private List<ImageView> layout_imgs_view;
    private int index_photo = 0;
    private List<String> list_order_zj_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails_carclaims);

        order = (OrderListBean) getIntent().getSerializableExtra("dataBean");
        list_order_zj_path = new ArrayList<>();

        initView();
        initEvent();
        loadData();
    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("订单详情");


        txt_order_sn = (TextView) findViewById(R.id.txt_order_sn);
        txt_order_statusname = (TextView) findViewById(R.id.txt_order_statusname);
        txt_order_insurancecompanyname = (TextView) findViewById(R.id.txt_order_insurancecompanyname);
        txt_order_repairstype = (TextView) findViewById(R.id.txt_order_repairstype);
        txt_order_carplateno = (TextView) findViewById(R.id.txt_order_carplateno);
        txt_order_handperson = (TextView) findViewById(R.id.txt_order_handperson);
        txt_order_handpersonphone = (TextView) findViewById(R.id.txt_order_handpersonphone);
        txt_order_remarks = (TextView) findViewById(R.id.txt_order_remarks);


        txt_order_submittime = (TextView) findViewById(R.id.txt_order_submittime);
        txt_order_paytime = (TextView) findViewById(R.id.txt_order_paytime);
        txt_order_completetime = (TextView) findViewById(R.id.txt_order_completetime);
        txt_order_cancletime = (TextView) findViewById(R.id.txt_order_cancletime);

        txt_handmerchant_headtitle = (TextView) findViewById(R.id.txt_handmerchant_headtitle);
        txt_handmerchant_name = (TextView) findViewById(R.id.txt_handmerchant_name);
        txt_handmerchant_contact = (TextView) findViewById(R.id.txt_handmerchant_contact);
        txt_handmerchant_contactphone = (TextView) findViewById(R.id.txt_handmerchant_contactphone);
        txt_handmerchant_contactaddress = (TextView) findViewById(R.id.txt_handmerchant_contactaddress);

        img_order_estimatelistimgurl = (ImageView) findViewById(R.id.img_order_estimatelistimgurl);
        txt_order_workinghoursprice = (TextView) findViewById(R.id.txt_order_workinghoursprice);
        txt_order_accessoriesprice = (TextView) findViewById(R.id.txt_order_accessoriesprice);
        txt_order_estimateprice = (TextView) findViewById(R.id.txt_order_estimateprice);
        txt_order_price = (TextView) findViewById(R.id.txt_order_price);


        btn_submit = (Button) findViewById(R.id.btn_submit);

        layout_submittime = (LinearLayout) findViewById(R.id.layout_submittime);
        layout_paytime = (LinearLayout) findViewById(R.id.layout_paytime);
        layout_completetime = (LinearLayout) findViewById(R.id.layout_completetime);
        layout_cancletime = (LinearLayout) findViewById(R.id.layout_cancletime);

        layout_handmerchant = (LinearLayout) findViewById(R.id.layout_handmerchant);

        layout_estimatelist = (LinearLayout) findViewById(R.id.layout_estimatelist);
        layout_estimatelist_line = (View) findViewById(R.id.layout_estimatelist_line);


        layout_imgs_row1 = (LinearLayout) findViewById(R.id.layout_imgs_row1);
        layout_uploadimgs = (LinearLayout) findViewById(R.id.layout_uploadimgs);

        layout_imgs_view = new ArrayList<>();
        layout_imgs_view.add((ImageView) findViewById(R.id.img_zhengjian1));

    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        for (int i = 0; i < layout_imgs_view.size(); i++) {
            ImageView imageView = layout_imgs_view.get(i);
            imageView.setTag(i);
            imageView.setOnClickListener(imgClick);
        }

    }

    View.OnClickListener imgClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = Integer.parseInt(v.getTag() + "");
            index_photo = index;
            showChoiceDialog(CropType.need_crop_no_cropimage);

        }
    };

    private void loadData() {

        Map<String, String> params = new HashMap<>();
        params.put("userId", this.getAppContext().getUser().getId()+"");
        params.put("merchantId", this.getAppContext().getUser().getMerchantId()+"");
        params.put("posMachineId", this.getAppContext().getUser().getPosMachineId()+"");
        params.put("orderId", order.getId() + "");
        params.put("type", order.getType() + "");
        HttpClient.getWithMy(Config.URL.getDetails, params, new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                LogUtil.i(TAG,"onSuccess====>>>" + response);

                ApiResultBean<OrderDetailsCarClaimsBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<OrderDetailsCarClaimsBean>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {
                    setView(rt.getData());
                }
            }

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
                LogUtil.e(TAG,"onFailure====>>>" + e.getMessage());
            }

        });
    }

    public void setView(OrderDetailsCarClaimsBean bean) {

        txt_order_sn.setText(bean.getSn());
        txt_order_statusname.setText(bean.getStatusName());
        txt_order_insurancecompanyname.setText(bean.getInsuranceCompanyName());
        txt_order_repairstype.setText(bean.getRepairsType());
        txt_order_carplateno.setText(bean.getCarPlateNo());
        txt_order_handperson.setText(bean.getHandPerson());
        txt_order_handpersonphone.setText(bean.getHandPersonPhone());
        txt_order_remarks.setText(bean.getRemarks());
        txt_order_submittime.setText(bean.getSubmitTime());
        txt_order_paytime.setText(bean.getPayTime());
        txt_order_completetime.setText(bean.getCompleteTime());
        txt_order_cancletime.setText(bean.getCancleTime());

        CommonUtil.loadImageFromUrl(OrderDetailsCarClaimsActivity.this, img_order_estimatelistimgurl, bean.getEstimateListImgUrl());

        txt_order_workinghoursprice.setText(bean.getWorkingHoursPrice());
        txt_order_accessoriesprice.setText(bean.getAccessoriesPrice());
        txt_order_estimateprice.setText(bean.getEstimatePrice());
        txt_order_price.setText(bean.getPrice());

        HandMerchantBean handMerchant = bean.getHandMerchant();
        if (handMerchant != null) {
            txt_handmerchant_headtitle.setText(handMerchant.getHeadTitle());
            txt_handmerchant_name.setText(handMerchant.getName());
            txt_handmerchant_contact.setText(handMerchant.getContact());
            txt_handmerchant_contactphone.setText(handMerchant.getContactPhone());
            txt_handmerchant_contactaddress .setText(handMerchant.getContactAddress());
        }


        switch (bean.getStatus()) {
            case 1:
                layout_submittime.setVisibility(View.VISIBLE);
                break;
            case 2:
                layout_submittime.setVisibility(View.VISIBLE);
                layout_handmerchant.setVisibility(View.VISIBLE);

                switch (bean.getFollowStatus()) {
                    case 1:

                        break;
                    case 2:
                        layout_uploadimgs.setVisibility(View.VISIBLE);
                        btn_submit.setVisibility(View.VISIBLE);
                        btn_submit.setText("提交");
                        break;
                }



                break;
            case 3:
                layout_estimatelist.setVisibility(View.VISIBLE);
                layout_estimatelist_line.setVisibility(View.VISIBLE);
                layout_submittime.setVisibility(View.VISIBLE);
                layout_handmerchant.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.GONE);
                btn_submit.setText("立即支付");
                break;
            case 4:
                layout_estimatelist.setVisibility(View.VISIBLE);
                layout_estimatelist_line.setVisibility(View.VISIBLE);
                layout_submittime.setVisibility(View.VISIBLE);
                layout_paytime.setVisibility(View.VISIBLE);
                layout_completetime.setVisibility(View.VISIBLE);
                layout_handmerchant.setVisibility(View.VISIBLE);
                break;
            case 5:
                layout_cancletime.setVisibility(View.VISIBLE);

                break;
        }

    }

    private void submitEstimateList() {

        if (list_order_zj_path.size() <= 0) {
            showToast("请上传定损单");
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("userId", this.getAppContext().getUser().getId()+"");
        params.put("orderId", "" + order.getId());
        params.put("merchantId", this.getAppContext().getUser().getMerchantId()+"");

        Map<String, String> files = new HashMap<>();

        files.put("estimateListImg",list_order_zj_path.get(0));

        postWithMy(Config.URL.submitEstimateList, params, files, true,"正在提交中",new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                LogUtil.i("onSuccess===>>" + response);

                ApiResultBean<Object> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<Object>>() {
                });

                showToast(rt.getMessage());

                if (rt.getResult() == Result.SUCCESS) {
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
                LogUtil.e("onFailure===>>" + e.getMessage());
                showToast("提交失败");
            }
        });

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.btn_submit:
                submitEstimateList();
                break;
        }
    }

    @Override
    public void OnCropSuccess(String photo_path) {
        LogUtil.i("photo_path:" + photo_path);

        LogUtil.i("photo_path:" + photo_path);
        list_order_zj_path.add(index_photo, photo_path);

//        if (index_photo < 3) {
//            layout_imgs_view.get(index_photo + 1).setVisibility(View.VISIBLE);
//        }

        loadImageHandler.sendEmptyMessage(layout_imgs_view.get(index_photo).getId());
    }

    private Handler loadImageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            try {
                ImageView iv = layout_imgs_view.get(index_photo);
                String imgPath = list_order_zj_path.get(index_photo);
                Bitmap bm = BitmapUtil.decodeSampledBitmapFromFd(imgPath, dip2px(150), dip2px(100));
                iv.setImageBitmap(bm);
            } catch (Exception e) {
                LogUtil.e(e.getMessage() + "");
            }

        }
    };

    @Override
    public void OnCropFail(String error_tx) {

    }
}
