package com.uplink.carins.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.InsPlanBean;
import com.uplink.carins.model.api.InsPlanProductSkuBean;
import com.uplink.carins.model.api.ItemFieldBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.ViewHolder;
import com.uplink.carins.ui.choicephoto.ChoicePhotoAndCropAndSwipeBackActivity;
import com.uplink.carins.ui.dialog.CustomConfirmDialog;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.BitmapUtil;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class InsProductPlanDetailsActivity extends ChoicePhotoAndCropAndSwipeBackActivity {

    private String TAG = "InsProductPlanDetailsActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;


    private TextView txt_companyname;
    private TextView txt_productskuname;
    private ListView list_attritem;
    private Button btn_submit;

    private InsPlanBean insPlanBean;
    private InsPlanProductSkuBean insPlanProductSkuBean;

    private LinearLayout layout_uploadimgs;
    private LinearLayout layout_imgs_row1;
    private List<ImageView> layout_imgs_view;
    private int index_photo = 0;
    private List<String> list_order_zj_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insproductplandetails);
        list_order_zj_path = new ArrayList<>();
        insPlanBean = (InsPlanBean) getIntent().getSerializableExtra("dataBean");
        insPlanProductSkuBean = insPlanBean.getProductSkus().get(getIntent().getIntExtra("checkPosition", 0));
        initView();
        initEvent();
        setView();
    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);

        txt_companyname = (TextView) findViewById(R.id.txt_companyname);
        txt_productskuname = (TextView) findViewById(R.id.txt_productskuname);
        list_attritem = (ListView) findViewById(R.id.list_attritem);

        layout_imgs_row1 = (LinearLayout) findViewById(R.id.layout_imgs_row1);
        layout_uploadimgs = (LinearLayout) findViewById(R.id.layout_uploadimgs);
        layout_imgs_view = new ArrayList<>();
        layout_imgs_view.add((ImageView) findViewById(R.id.img_zhengjian1));
        layout_imgs_view.add((ImageView) findViewById(R.id.img_zhengjian2));
        layout_imgs_view.add((ImageView) findViewById(R.id.img_zhengjian3));
        layout_imgs_view.add((ImageView) findViewById(R.id.img_zhengjian4));

        btn_submit= (Button) findViewById(R.id.btn_submit);
    }


    public void setView() {

        txt_companyname.setText(insPlanBean.getCompanyName());
        txt_productskuname.setText(insPlanProductSkuBean.getProductSkuName());

        AttrItemsAdapter attrItemsAdapter = new AttrItemsAdapter();
        list_attritem.setAdapter(attrItemsAdapter);
        attrItemsAdapter.setData(insPlanProductSkuBean.getAttrItems());
        CommonUtil.setListViewHeightBasedOnChildren(list_attritem);

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
            showChoiceDialog(ChoicePhotoAndCropAndSwipeBackActivity.CropType.need_crop_no_cropimage);

        }
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.btn_submit:
                submit();
                break;
        }
    }

    private CustomConfirmDialog dialog_Success;
    private void showSuccessDialog() {
        if (dialog_Success == null) {

            dialog_Success = new CustomConfirmDialog(InsProductPlanDetailsActivity.this, "订单提交成功",false);

            dialog_Success.getBtnSure().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog_Success.dismiss();

                    Intent l_Intent = new Intent(InsProductPlanDetailsActivity.this, OrderListActivity.class);
                    l_Intent.putExtra("status", 1);
                    startActivity(l_Intent);
                    finish();

                    //AppManager.getAppManager().finishAllActivity();
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


    private void submit() {
        if (list_order_zj_path.size() <= 0) {
            showToast("至少上传一张身份证");
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("userId", this.getAppContext().getUser().getId() + "");
        params.put("merchantId", this.getAppContext().getUser().getMerchantId() + "");
        params.put("posMachineId", this.getAppContext().getUser().getPosMachineId() + "");
        params.put("productSkuId", insPlanProductSkuBean.getProductSkuId() + "");

        Map<String, String> files = new HashMap<>();

        for (int i = 0; i < list_order_zj_path.size(); i++) {
            if (!StringUtil.isEmpty(list_order_zj_path.get(i))) {
                files.put("ZJ" + i, list_order_zj_path.get(i));
            }
        }

        postWithMy(Config.URL.submitInsurance, params, files, true, "正在提交中", new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);


                LogUtil.i(TAG, "onSuccess====>>>" + response);

                ApiResultBean<Object> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<Object>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {
                    showSuccessDialog();
                } else {
                    showToast(rt.getMessage());
                }

            }

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
                LogUtil.e(TAG, "onFailure====>>>" + e.getMessage());
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

    private class AttrItemsAdapter extends BaseAdapter {
        private List<ItemFieldBean> beans;

        public void setData(List<ItemFieldBean> beans) {
            this.beans = beans;
            notifyDataSetChanged();
        }

        AttrItemsAdapter() {

            this.beans = new ArrayList<>();
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }

        @Override
        public int getCount() {
            return beans.size();
        }

        @Override
        public Object getItem(int position) {
            return beans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ItemFieldBean bean = beans.get(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(InsProductPlanDetailsActivity.this).inflate(R.layout.item_attritem_field, parent, false);
            }

            TextView txt_name = ViewHolder.get(convertView, R.id.item_order_field_name);
            TextView txt_value = ViewHolder.get(convertView, R.id.item_order_field_value);

            txt_name.setText(bean.getField() + "");
            txt_value.setText(bean.getValue() + "");

            return convertView;
        }

    }
}
