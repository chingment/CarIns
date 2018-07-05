package com.uplink.carins.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.OfferCompanyBean;
import com.uplink.carins.model.api.OrderDetailsCarInsureBean;
import com.uplink.carins.model.api.OrderListBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.model.api.ZjBean;
import com.uplink.carins.ui.AutoNextLineLinearlayout;
import com.uplink.carins.ui.ViewHolder;
import com.uplink.carins.ui.choicephoto.ChoicePhotoAndCropAndSwipeBackActivity;
import com.uplink.carins.ui.my.MyHorizontalListView;
import com.uplink.carins.ui.my.MyListView;
import com.uplink.carins.utils.BitmapUtil;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class OrderDetailsCarInsrueActivity extends ChoicePhotoAndCropAndSwipeBackActivity {

    private String TAG = "OrderDetailsCarInsrueActivity";

    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;
    private OrderListBean order;

    private TextView txt_order_sn;
    private TextView txt_order_statusname;
    private TextView txt_order_carowner;
    private TextView txt_order_carowneridnumber;
    private TextView txt_order_carplateno;
    private TextView txt_order_remarks;

    private TextView txt_order_insurancecompanyname;
    private TextView txt_order_compulsoryprice;
    private TextView txt_order_traveltaxprice;
    private TextView txt_order_commercialprice;
    private TextView txt_order_price;

    private TextView txt_order_submittime;
    private TextView txt_order_paytime;
    private TextView txt_order_completetime;
    private TextView txt_order_cancletime;

    private Button btn_submit;

    private MyListView list_carinsoffercompany;
    private CarInsOfferCompanyAdapter list_carinsoffercompany_adapter;

    private MyHorizontalListView list_order_zj;
    private Zjdapter list_order_zj_adapter;

    private LinearLayout layout_insurecompany;
    private LinearLayout layout_submittime;
    private LinearLayout layout_paytime;
    private LinearLayout layout_completetime;
    private LinearLayout layout_cancletime;
    private AutoNextLineLinearlayout lll;

    private LinearLayout layout_uploadimgs;
    private LinearLayout layout_imgs_row1;
    private List<ImageView> layout_imgs_view;

    //private List<ImageView> imgviews;
    // 当前有几张图片
    private int index_photo = 0;
    private List<String> list_order_zj_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails_carinsrue);

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
        txt_order_carowner = (TextView) findViewById(R.id.txt_order_carowner);
        txt_order_carowneridnumber = (TextView) findViewById(R.id.txt_order_carowneridnumber);
        txt_order_carplateno = (TextView) findViewById(R.id.txt_order_carplateno);
        txt_order_remarks = (TextView) findViewById(R.id.txt_order_remarks);

        txt_order_insurancecompanyname = (TextView) findViewById(R.id.txt_order_insurancecompanyname);
        txt_order_compulsoryprice = (TextView) findViewById(R.id.txt_order_compulsoryprice);
        txt_order_traveltaxprice = (TextView) findViewById(R.id.txt_order_traveltaxprice);
        txt_order_commercialprice = (TextView) findViewById(R.id.txt_order_commercialprice);
        txt_order_price = (TextView) findViewById(R.id.txt_order_price);


        txt_order_submittime = (TextView) findViewById(R.id.txt_order_submittime);
        txt_order_paytime = (TextView) findViewById(R.id.txt_order_paytime);
        txt_order_completetime = (TextView) findViewById(R.id.txt_order_completetime);
        txt_order_cancletime = (TextView) findViewById(R.id.txt_order_cancletime);
        list_order_zj = (MyHorizontalListView) findViewById(R.id.list_order_zj);

        btn_submit = (Button) findViewById(R.id.btn_submit);


        list_carinsoffercompany = (MyListView) findViewById(R.id.list_carinsoffercompany);
        layout_insurecompany = (LinearLayout) findViewById(R.id.layout_insurecompany);
        layout_submittime = (LinearLayout) findViewById(R.id.layout_submittime);
        layout_paytime = (LinearLayout) findViewById(R.id.layout_paytime);
        layout_completetime = (LinearLayout) findViewById(R.id.layout_completetime);
        layout_cancletime = (LinearLayout) findViewById(R.id.layout_cancletime);

        layout_imgs_row1 = (LinearLayout) findViewById(R.id.layout_imgs_row1);
        layout_uploadimgs = (LinearLayout) findViewById(R.id.layout_uploadimgs);

        layout_imgs_view = new ArrayList<>();
        layout_imgs_view.add((ImageView) findViewById(R.id.img_zhengjian1));
        layout_imgs_view.add((ImageView) findViewById(R.id.img_zhengjian2));
        layout_imgs_view.add((ImageView) findViewById(R.id.img_zhengjian3));
        layout_imgs_view.add((ImageView) findViewById(R.id.img_zhengjian4));

        //lll= (AutoNextLineLinearlayout) findViewById(R.id.lll);
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
        params.put("userId", this.getAppContext().getUser().getId() + "");
        params.put("merchantId", this.getAppContext().getUser().getMerchantId() + "");
        params.put("posMachineId", this.getAppContext().getUser().getPosMachineId()+"");
        params.put("orderId", order.getId() + "");
        params.put("type", order.getType() + "");
        getWithMy(Config.URL.getDetails, params,false,"", new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                ApiResultBean<OrderDetailsCarInsureBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<OrderDetailsCarInsureBean>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {
                    setView(rt.getData());
                }
            }

        });
    }


    public void setView(OrderDetailsCarInsureBean bean) {


        txt_order_sn.setText(bean.getSn());
        txt_order_statusname.setText(bean.getStatusName());
        txt_order_carowner.setText(bean.getCarOwner());
        txt_order_carowneridnumber.setText(bean.getCarOwnerIdNumber());
        txt_order_carplateno.setText(bean.getCarPlateNo());
        txt_order_remarks.setText(bean.getRemarks());
        txt_order_submittime.setText(bean.getSubmitTime());
        txt_order_paytime.setText(bean.getPayTime());
        txt_order_completetime.setText(bean.getCompleteTime());
        txt_order_cancletime.setText(bean.getCancleTime());


        list_carinsoffercompany_adapter = new CarInsOfferCompanyAdapter();
        list_carinsoffercompany.setAdapter(list_carinsoffercompany_adapter);
        list_carinsoffercompany_adapter.setData(bean.getOfferCompany());

        list_order_zj_adapter = new Zjdapter();
        list_order_zj.setAdapter(list_order_zj_adapter);
        list_order_zj_adapter.setData(bean.getZj());

//        for (int i=0;i<bean.getZj().size();i++) {
//            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//
//
//            LinearLayout childBtn = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_zj, null);
//            childBtn.setLayoutParams(itemParams);
//
//            ImageView item_img = ViewHolder.get(childBtn, R.id.item_company_choice_img);
//
//            CommonUtil.loadImageFromUrl(childBtn.getContext(), item_img, bean.getZj().get(i).getUrl());
//
//
//
//            lll.addView(childBtn);
//        }


        switch (bean.getStatus()) {
            case 1:
                layout_submittime.setVisibility(View.VISIBLE);
                break;
            case 2:
                // list_carinsoffercompany.setVisibility(View.VISIBLE);
                list_order_zj.setVisibility(View.VISIBLE);
                layout_submittime.setVisibility(View.VISIBLE);
                switch (bean.getFollowStatus()) {
                    case 1:
                        layout_uploadimgs.setVisibility(View.VISIBLE);
                        btn_submit.setVisibility(View.VISIBLE);
                        btn_submit.setText("提交");
                        break;
                    case 2:

                        break;
                }
                break;
            case 3:
                layout_submittime.setVisibility(View.VISIBLE);
                layout_insurecompany.setVisibility(View.GONE);
                btn_submit.setVisibility(View.GONE);
                btn_submit.setText("立即支付");
                break;
            case 4:
                layout_submittime.setVisibility(View.VISIBLE);
                layout_paytime.setVisibility(View.VISIBLE);
                layout_completetime.setVisibility(View.VISIBLE);
                layout_insurecompany.setVisibility(View.GONE);
                list_carinsoffercompany.setVisibility(View.GONE);

                txt_order_insurancecompanyname.setText(bean.getInsuranceCompanyName());
                txt_order_compulsoryprice.setText(bean.getCompulsoryPrice());
                txt_order_traveltaxprice.setText(bean.getTravelTaxPrice());
                txt_order_commercialprice.setText(bean.getCommercialPrice());
                txt_order_price.setText(bean.getPrice());


                break;
            case 5:
                layout_cancletime.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.btn_submit:
                switch (order.getStatus()) {
                    case 2:
                        submitFollowInsure();
                        break;
                }
                break;
        }
    }

    private void submitFollowInsure() {
        if (list_order_zj_path.size() <= 0) {
            showToast("请上传证件");
            return;
        }


        Map<String, Object> params = new HashMap<>();
        params.put("userId", this.getAppContext().getUser().getId() + "");//21
        params.put("orderId", order.getId());
        params.put("merchantId", this.getAppContext().getUser().getMerchantId() + "");


        Map<String, String> files = new HashMap<>();

        for (int i = 0; i < list_order_zj_path.size(); i++) {
            if (!StringUtil.isEmpty(list_order_zj_path.get(i))) {
                files.put("ZJ" + (i + 1) + "_Img", list_order_zj_path.get(i));
            }
        }

        postWithMy(Config.URL.submitFollowInsure, params, files,true,"正在提交中", new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);


                LogUtil.i(TAG,"onSuccess====>>>" + response);

                ApiResultBean<Object> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<Object>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {
                    showToast(rt.getMessage());
                    setResult(RESULT_OK);
                    finish();
                } else {
                    showToast(rt.getMessage());
                }

            }

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
                LogUtil.e(TAG,"onFailure====>>>" + e.getMessage());
                showToast("提交失败");
            }
        });
    }

    @Override
    public void OnCropSuccess(String photo_path) {
        LogUtil.i("photo_path:" + photo_path);
        list_order_zj_path.add(index_photo, photo_path);

        if (index_photo < 3) {
            layout_imgs_view.get(index_photo + 1).setVisibility(View.VISIBLE);
        }

        loadImageHandler.sendEmptyMessage(layout_imgs_view.get(index_photo).getId());


    }

    @Override
    public void OnCropFail(String error_tx) {

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


    private class CarInsOfferCompanyAdapter extends BaseAdapter { // shoplist适配器

        private List<OfferCompanyBean> offerCompanys;

        CarInsOfferCompanyAdapter() {

            this.offerCompanys = new ArrayList<>();
        }

        public void setData(List<OfferCompanyBean> offerCompanys) {
            this.offerCompanys = offerCompanys;

            notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return offerCompanys.size();
        }

        @Override
        public Object getItem(int position) {

            return offerCompanys.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            OfferCompanyBean offerCompany = offerCompanys.get(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(OrderDetailsCarInsrueActivity.this).inflate(R.layout.item_orderdetails_carinsure_offercompany, parent, false);
            }
            ImageView img_offer = ViewHolder.get(convertView, R.id.item_offercompany_img);
            img_offer.setOnClickListener(myImgClick);
            img_offer.setTag(position);
            TextView txt_offercompany_name = ViewHolder.get(convertView, R.id.item_offercompany_name);
            TextView txt_offercompany_description = ViewHolder.get(convertView, R.id.item_offercompany_description);
            CheckBox cb_offercompany = ViewHolder.get(convertView, R.id.item_offercompany_cb);
            cb_offercompany.setTag(position);
            LinearLayout item_offercompany_info = ViewHolder.get(convertView, R.id.item_offercompany_info);
            cb_offercompany.setTag(position);
            cb_offercompany.setOnCheckedChangeListener(myChecChangeListener);

            int order_status = order.getStatus();

            txt_offercompany_name.setText(offerCompany.getInsuranceCompanyName());

            txt_offercompany_description.setText(offerCompany.getDescription() + "");

            CommonUtil.loadImageFromUrl(OrderDetailsCarInsrueActivity.this, img_offer, offerCompany.getInsureImgUrl());

            if (order_status == 3) {
                item_offercompany_info.setTag(cb_offercompany);
                //item_offercompany_info.setOnClickListener(myInfoClick);
                cb_offercompany.setVisibility(View.GONE);
            } else {
                cb_offercompany.setVisibility(View.GONE);
            }

            return convertView;
        }


        View.OnClickListener myImgClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = Integer.parseInt(v.getTag() + "");
                Intent intent = new Intent(OrderDetailsCarInsrueActivity.this, ImageGalleryActivity.class);

                ArrayList<String> img_gallery_Urls = new ArrayList<String>();
                for (OfferCompanyBean b : offerCompanys) {
                    img_gallery_Urls.add(b.getInsureImgUrl());
                }

                intent.putStringArrayListExtra("images", img_gallery_Urls);
                intent.putExtra("position", index);
                startActivity(intent);
            }
        };

        View.OnClickListener myInfoClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb_offercompany = (CheckBox) v.getTag();

                if (cb_offercompany.isChecked()) {
                    cb_offercompany.setChecked(false);
                } else {
                    cb_offercompany.setChecked(true);
                }
            }
        };

        CheckBox.OnCheckedChangeListener myChecChangeListener = new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                int position = Integer.parseInt(buttonView.getTag().toString());
                OfferCompanyBean offerCompany = offerCompanys.get(position);
                int count = list_carinsoffercompany.getChildCount();

                for (int i = 0; i < count; i++) {
                    CheckBox cb = (CheckBox) list_carinsoffercompany.getChildAt(i).findViewById(R.id.item_offercompany_cb);
                    cb.setChecked(false);
                }

                String order_insurancecompanyname = "";
                String order_compulsoryprice = "";
                String order_traveltaxprice = "";
                String order_commercialprice = "";
                String order_price = "";
                if (isChecked) {

                    order_insurancecompanyname = offerCompany.getInsuranceCompanyName() + "";
                    order_compulsoryprice = offerCompany.getCompulsoryPrice() + "";
                    order_traveltaxprice = offerCompany.getTravelTaxPrice() + "";
                    order_commercialprice = offerCompany.getCommercialPrice() + "";
                    order_price = offerCompany.getInsureTotalPrice() + "";
                }

                txt_order_insurancecompanyname.setText(order_insurancecompanyname);
                txt_order_compulsoryprice.setText(order_compulsoryprice);
                txt_order_traveltaxprice.setText(order_traveltaxprice);
                txt_order_commercialprice.setText(order_commercialprice);
                txt_order_price.setText(order_price);


                buttonView.setChecked(isChecked);

            }
        };


    }

    private class Zjdapter extends BaseAdapter {


        private List<ZjBean> zjs;


        Zjdapter() {

            this.zjs = new ArrayList<>();
        }


        public void setData(List<ZjBean> zjs) {
            this.zjs = zjs;
            notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return zjs.size();
        }

        @Override
        public Object getItem(int position) {

            return zjs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ZjBean zj = zjs.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(OrderDetailsCarInsrueActivity.this).inflate(R.layout.item_zj, parent, false);
            }
            ImageView item_img = ViewHolder.get(convertView, R.id.item_company_choice_img);
            item_img.setTag(position);
            item_img.setOnClickListener(myImgClick);
            LogUtil.i("");
            CommonUtil.loadImageFromUrl(convertView.getContext(), item_img, zj.getUrl());
            return convertView;
        }

        View.OnClickListener myImgClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = Integer.parseInt(v.getTag() + "");
                Intent intent = new Intent(OrderDetailsCarInsrueActivity.this, ImageGalleryActivity.class);

                ArrayList<String> img_gallery_Urls = new ArrayList<String>();
                for (ZjBean b : zjs) {
                    img_gallery_Urls.add(b.getUrl());
                }

                intent.putStringArrayListExtra("images", img_gallery_Urls);
                intent.putExtra("position", index);
                startActivity(intent);
            }
        };

    }


}
