package com.uplink.carins.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.activity.adapter.NwItemParentFieldAdapter;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.CarInsCompanyBean;
import com.uplink.carins.model.api.CarInsKindBean;
import com.uplink.carins.model.api.NwCarInsBaseInfoBean;
import com.uplink.carins.model.api.NwCarInsCompanyBean;
import com.uplink.carins.model.api.OrderType;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.choicephoto.ChoicePhotoAndCropAndSwipeBackActivity;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.AbFileUtil;
import com.uplink.carins.utils.BitmapUtil;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

public class NwCarInsInsureActivity extends ChoicePhotoAndCropAndSwipeBackActivity {


    String TAG = "NwCarInsInsureActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;
    private Button btn_submit;

    private ImageView img_company_logo;
    private TextView txt_company_name;
    private TextView txt_company_sumpremium;

    private EditText txt_carowner_name;
    private EditText txt_carowner_certno;
    private EditText txt_carowner_address;

    private int choice_index = -1;
    private final int choice_index_carowner_xingshizheng = 101;
    private final int choice_index_carowner_shenfenzheng_face = 102;
    private final int choice_index_carowner_shenfenzheng_back = 103;

    private LinearLayout layout_carowner_xingshizheng;
    private ImageView img_carowner_xingshizheng;
    private String path_carowner_xingshizheng = "";

    private LinearLayout layout_carowner_shenfenzheng_face;
    private ImageView img_carowner_shenfenzheng_face;
    private String path_carowner_shenfenzheng_face = "";

    private LinearLayout layout_carowner_shenfenzheng_back;
    private ImageView img_carowner_shenfenzheng_back;
    private String path_carowner_shenfenzheng_back = "";

    private NwCarInsBaseInfoBean carInsBaseInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nwcarins_insure);
        carInsBaseInfo = (NwCarInsBaseInfoBean) getIntent().getSerializableExtra("dataBean");
        initView();
        initEvent();
        //initData(carInsBaseInfo);
    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("投保信息");

        btn_submit = (Button) findViewById(R.id.btn_submit);

        img_company_logo = (ImageView) findViewById(R.id.img_company_logo);
        txt_company_name = (TextView) findViewById(R.id.txt_company_name);
        txt_company_sumpremium = (TextView) findViewById(R.id.txt_company_sumpremium);

        txt_carowner_name = (EditText) findViewById(R.id.txt_carowner_name);
        txt_carowner_certno = (EditText) findViewById(R.id.txt_carowner_certno);
        txt_carowner_address = (EditText) findViewById(R.id.txt_carowner_address);

        // 车辆行驶证
        layout_carowner_xingshizheng = (LinearLayout) findViewById(R.id.layout_carowner_xingshizheng);
        img_carowner_xingshizheng = (ImageView) findViewById(R.id.img_carowner_xingshizheng);
        // 身份证正面
        layout_carowner_shenfenzheng_face = (LinearLayout) findViewById(R.id.layout_carowner_shenfenzheng_face);
        img_carowner_shenfenzheng_face = (ImageView) findViewById(R.id.img_carowner_shenfenzheng_face);
        // 身份证反面
        layout_carowner_shenfenzheng_back = (LinearLayout) findViewById(R.id.layout_carowner_shenfenzheng_back);
        img_carowner_shenfenzheng_back = (ImageView) findViewById(R.id.img_carowner_shenfenzheng_back);

    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        layout_carowner_xingshizheng.setOnClickListener(this);
        layout_carowner_shenfenzheng_face.setOnClickListener(this);
        layout_carowner_shenfenzheng_back.setOnClickListener(this);
    }

    private void initData(NwCarInsBaseInfoBean bean) {


    }

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
            case R.id.layout_carowner_xingshizheng:
                choice_index = choice_index_carowner_xingshizheng;
                path_carowner_xingshizheng = "";
                showChoiceDialog(CropType.need_crop_no_cropimage);
                break;
            case R.id.layout_carowner_shenfenzheng_face:
                choice_index = choice_index_carowner_shenfenzheng_face;
                path_carowner_shenfenzheng_face = "";
                showChoiceDialog(CropType.need_crop_no_cropimage);
                break;
            case R.id.layout_carowner_shenfenzheng_back:
                choice_index = choice_index_carowner_shenfenzheng_back;
                path_carowner_shenfenzheng_back = "";
                showChoiceDialog(CropType.need_crop_no_cropimage);
                break;
        }
    }


    private void submit() {


        if (StringUtil.isEmpty(carInsBaseInfo.getCustomers().get(0).getIdentityBackPicKey())) {
            showToast("请上传身份证（正面）");
            return;
        }

        if (StringUtil.isEmpty(carInsBaseInfo.getCustomers().get(0).getIdentityBackPicKey())) {
            showToast("请上传身份证（背面）");
            return;
        }


        if (StringUtil.isEmpty(carInsBaseInfo.getCar().getLicensePicKey())) {
            showToast("请上传车辆行驶证");
            return;
        }

        String carowner_certno = txt_carowner_certno.getText().toString();
        String carowner_address = txt_carowner_address.getText().toString();

        if (StringUtil.isEmpty(carowner_certno)) {
            showToast("请输入证件号码");
            return;
        }

        if (StringUtil.isEmpty(carowner_address)) {
            showToast("请输入详细地址");
            return;
        }


        Map<String, Object> params = new HashMap<>();
        params.put("userId", this.getAppContext().getUser().getId());
        params.put("merchantId", this.getAppContext().getUser().getMerchantId());
        params.put("posMachineId", this.getAppContext().getUser().getPosMachineId());
        params.put("offerId", carInsBaseInfo.getCar());

        postWithMy(Config.URL.submitInsure, params, null, true, "正在提交中", new HttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                LogUtil.i(TAG, "onSuccess====>>>" + response);

                ApiResultBean<Object> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<Object>>() {
                });


                if (rt.getResult() == Result.SUCCESS) {

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
        switch (choice_index) {
            case choice_index_carowner_xingshizheng:
                path_carowner_xingshizheng = photo_path;
                img_carowner_xingshizheng.setVisibility(View.VISIBLE);
                loadImageHandler.sendEmptyMessage(choice_index_carowner_xingshizheng);
                break;
            case choice_index_carowner_shenfenzheng_face:
                path_carowner_shenfenzheng_face = photo_path;
                img_carowner_shenfenzheng_face.setVisibility(View.VISIBLE);
                loadImageHandler.sendEmptyMessage(choice_index_carowner_shenfenzheng_face);
                break;
            case choice_index_carowner_shenfenzheng_back:
                path_carowner_shenfenzheng_back = photo_path;
                img_carowner_shenfenzheng_back.setVisibility(View.VISIBLE);
                loadImageHandler.sendEmptyMessage(choice_index_carowner_shenfenzheng_back);
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
                case choice_index_carowner_xingshizheng:
                    onLoad(img_carowner_xingshizheng, path_carowner_xingshizheng);
                    getImageInfo("10", path_carowner_xingshizheng);
                    break;
                case choice_index_carowner_shenfenzheng_face:
                    onLoad(img_carowner_shenfenzheng_face, path_carowner_shenfenzheng_face);
                    getImageInfo("11", path_carowner_shenfenzheng_face);
                    break;
                case choice_index_carowner_shenfenzheng_back:
                    onLoad(img_carowner_shenfenzheng_back, path_carowner_shenfenzheng_back);
                    getImageInfo("1", path_carowner_shenfenzheng_back);
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

    private void getImageInfo(String type, String filePath) {

        Map<String, Object> params = new HashMap<>();
        params.put("type", type);

        Map<String, String> files = new HashMap<>();
        if (!StringUtil.isEmpty(filePath)) {
            files.put("certPic", filePath);
        }

        postWithMy(Config.URL.carInsUploadImg, params, files, true, "正在上传中", new HttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                LogUtil.i(TAG, "onSuccess====>>>" + response);

                ApiResultBean<Object> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<Object>>() {
                });


            }
        });
    }

    @Override
    public void OnCropFail(String error_tx) {
        LogUtil.i(TAG, "choice_index=" + choice_index + ">error>>" + error_tx);
    }


}
