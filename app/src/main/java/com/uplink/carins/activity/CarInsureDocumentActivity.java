package com.uplink.carins.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.CarInsCompanyBean;
import com.uplink.carins.model.api.CarInsKindBean;
import com.uplink.carins.model.api.OrderType;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.ViewHolder;
import com.uplink.carins.ui.choicephoto.ChoicePhotoAndCropAndSwipeBackActivity;
import com.uplink.carins.ui.dialog.CustomConfirmDialog;
import com.uplink.carins.ui.my.MyHorizontalListView;
import com.uplink.carins.utils.BitmapUtil;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.NoDoubleClickUtils;
import com.uplink.carins.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class CarInsureDocumentActivity extends ChoicePhotoAndCropAndSwipeBackActivity {

    private String TAG = "CarInsureDocumentActivity";

    private int choice_index = -1;
    private final int choice_index_xingshizheng = 101;
    private final int choice_index_shenfenzheng = 102;
    private final int choice_index_CHECHUANSHUI = 103;
    private final int choice_index_YANCHEZHENG = 104;

    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;

    private Button btn_submit_carinsure;
    private MyHorizontalListView list_selected_carinscompany;
    private SelectedCarInsCompanyAdapter list_selected_carinscompany_adapter;

    // 车辆行驶证
    private LinearLayout layout_carinsure_xingshizheng;
    private ImageView img_carinsure_xingshizheng;
    private String path_carinsure_xingshizheng = "";

    // 车辆行驶证
    private LinearLayout layout_carinsure_shenfenzheng;
    private ImageView img_carinsure_shenfenzheng;
    private String path_carinsure_shenfenzheng = "";

    private int carInsPlanId = 0;
    private List<CarInsKindBean> carInsKinds = new ArrayList<>();
    private List<CarInsCompanyBean> carInsCompanys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carinsuredocument);

        initView();
        initEvent();

        getIntentData();
    }

    public void getIntentData() {

        carInsPlanId = (int) getIntent().getSerializableExtra("carInsPlanId");
        carInsKinds = (List<CarInsKindBean>) getIntent().getSerializableExtra("carInsKinds");
        carInsCompanys = (List<CarInsCompanyBean>) getIntent().getSerializableExtra("carInsCompanys");

        list_selected_carinscompany_adapter = new SelectedCarInsCompanyAdapter();
        list_selected_carinscompany.setAdapter(list_selected_carinscompany_adapter);
        list_selected_carinscompany_adapter.setData(carInsCompanys);
    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("上传证件");
        btn_submit_carinsure = (Button) findViewById(R.id.btn_submit_carinsure);
        list_selected_carinscompany = (MyHorizontalListView) findViewById(R.id.list_selected_carinscompany);



        // 车辆行驶证
        layout_carinsure_xingshizheng = (LinearLayout) findViewById(R.id.layout_carinsure_xingshizheng);
        img_carinsure_xingshizheng = (ImageView) findViewById(R.id.img_carinsure_xingshizheng);

        // 身份证
        layout_carinsure_shenfenzheng = (LinearLayout) findViewById(R.id.layout_carinsure_shenfenzheng);
        img_carinsure_shenfenzheng = (ImageView) findViewById(R.id.img_carinsure_shenfenzheng);
    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);

        btn_submit_carinsure.setOnClickListener(this);

        //车辆行驶证
        layout_carinsure_xingshizheng.setOnClickListener(this);
        layout_carinsure_shenfenzheng.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.btn_submit_carinsure:
                if (!NoDoubleClickUtils.isDoubleClick()) {
                    submitInsure();
                }
                break;
            case R.id.layout_carinsure_xingshizheng:
                choice_index = choice_index_xingshizheng;
                path_carinsure_xingshizheng = "";
                showChoiceDialog(CropType.need_crop_no_cropimage);
                break;
            case R.id.layout_carinsure_shenfenzheng:
                choice_index = choice_index_shenfenzheng;
                path_carinsure_shenfenzheng = "";
                showChoiceDialog(CropType.need_crop_no_cropimage);
                break;
        }
    }


    private void submitInsure() {

        if (StringUtil.isEmpty(path_carinsure_xingshizheng)) {
            showToast("请上传车辆行驶证");
            return;
        }

        if (StringUtil.isEmpty(path_carinsure_shenfenzheng)) {
            showToast("请上传身份证");
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("userId",this.getAppContext().getUser().getId());
        params.put("merchantId", this.getAppContext().getUser().getMerchantId());
        params.put("posMachineId", this.getAppContext().getUser().getPosMachineId());
        params.put("type", OrderType.CarInsure);
        params.put("insurePlanId", carInsPlanId);


        JSONArray json_CarInsCompanys = new JSONArray();
        for (CarInsCompanyBean carInsCompany : carInsCompanys) {
            json_CarInsCompanys.put(carInsCompany.getId());
        }

        params.put("insuranceCompanyId", json_CarInsCompanys);


        JSONArray json_CarInsKinds = new JSONArray();

        try {
            for (CarInsKindBean carInsKind : carInsKinds) {
                if (carInsKind.getIsCheck()) {
                    JSONObject jsonFa1 = new JSONObject();
                    jsonFa1.put("id", carInsKind.getId());
                    jsonFa1.put("value", carInsKind.getInputValue());
                    jsonFa1.put("isWaiverDeductible", carInsKind.getIsWaiverDeductible());
                    json_CarInsKinds.put(jsonFa1);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }


        params.put("insureKind", json_CarInsKinds);

        Map<String, String> files = new HashMap<>();
        if (!StringUtil.isEmpty(path_carinsure_xingshizheng)) {
            files.put("CZ_CL_XSZ_Img", path_carinsure_xingshizheng);
        }

        if (!StringUtil.isEmpty(path_carinsure_shenfenzheng)) {
            files.put("CZ_SFZ_Img", path_carinsure_shenfenzheng);
        }

        postWithMy(Config.URL.submitInsure, params, files,true,"正在提交中",  new HttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                ApiResultBean<Object> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<Object>>() {
                });


                if (rt.getResult() == Result.SUCCESS) {
                    showSuccessDialog();
                } else {
                    showToast(rt.getMessage());
                }
            }
        });

    }


    @Override
    public void OnCropSuccess(String photo_path) {
        switch (choice_index) {
            case choice_index_xingshizheng:
                path_carinsure_xingshizheng = photo_path;
                img_carinsure_xingshizheng.setVisibility(View.VISIBLE);
                loadImageHandler.sendEmptyMessage(choice_index_xingshizheng);
                break;
            case choice_index_shenfenzheng:
                path_carinsure_shenfenzheng = photo_path;
                img_carinsure_shenfenzheng.setVisibility(View.VISIBLE);
                loadImageHandler.sendEmptyMessage(choice_index_shenfenzheng);
                break;
        }
        LogUtil.d(TAG, "choice_index=" + choice_index + ">>>" + photo_path);
        choice_index = 0;
    }

    private Handler loadImageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case choice_index_xingshizheng:
                    onLoad(img_carinsure_xingshizheng, path_carinsure_xingshizheng);
                    break;
                case choice_index_shenfenzheng:
                    onLoad(img_carinsure_shenfenzheng, path_carinsure_shenfenzheng);
                    break;
            }
        }
    };

    private void onLoad(ImageView iv, String imgPath) {
        try {
            Bitmap bm = BitmapUtil.decodeSampledBitmapFromFd(imgPath, dip2px(150), dip2px(100));
            iv.setImageBitmap(bm);
        } catch (Exception e) {
            LogUtil.e(e.getMessage() + "");
        }
    }

    @Override
    public void OnCropFail(String error_tx) {
        LogUtil.i(TAG, "choice_index=" + choice_index + ">error>>" + error_tx);
    }

    private CustomConfirmDialog dialog_Success;
    private void showSuccessDialog() {
        if (dialog_Success == null) {

            dialog_Success = new CustomConfirmDialog(CarInsureDocumentActivity.this, "投保订单提交成功",false);

            dialog_Success.getBtnSure().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog_Success.dismiss();

                    Intent l_Intent = new Intent(CarInsureDocumentActivity.this, OrderListActivity.class);
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

    private class SelectedCarInsCompanyAdapter extends BaseAdapter {


        private List<CarInsCompanyBean> carInsCompanys;


        SelectedCarInsCompanyAdapter() {

            this.carInsCompanys = new ArrayList<>();
        }


        public void setData(List<CarInsCompanyBean> carInsCompanys) {
            this.carInsCompanys = carInsCompanys;
            notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return carInsCompanys.size();
        }

        @Override
        public Object getItem(int position) {

            return carInsCompanys.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CarInsCompanyBean carInsCompany = carInsCompanys.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(CarInsureDocumentActivity.this).inflate(R.layout.item_carinsure_selectedcompany, parent, false);
            }
            ImageView item_img = ViewHolder.get(convertView, R.id.item_company_choice_img);
            CommonUtil.loadImageFromUrl(CarInsureDocumentActivity.this, item_img, carInsCompany.getImgUrl() + "");
            return convertView;
        }
    }
}
