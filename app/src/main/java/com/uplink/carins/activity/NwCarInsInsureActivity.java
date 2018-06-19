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
import com.uplink.carins.model.api.CustomerBean;
import com.uplink.carins.model.api.NwCarInsBaseInfoBean;
import com.uplink.carins.model.api.NwCarInsCompanyBean;
import com.uplink.carins.model.api.NwUploadResultBean;
import com.uplink.carins.model.api.NwUploadResultByIdentityBean;
import com.uplink.carins.model.api.NwUploadResultByLicenseBean;
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
    private NwCarInsCompanyBean offerInfo;

    private String imgKey_carowner_xingshizheng = "";
    private String imgUrl_carowner_xingshizheng = "";
    private String imgKey_carowner_shenfenzheng_face = "";
    private String imgUrl_carowner_shenfenzheng_face = "";
    private String imgKey_carowner_shenfenzheng_back = "";
    private String imgUrl_carowner_shenfenzheng_back = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nwcarins_insure);
        carInsBaseInfo = (NwCarInsBaseInfoBean) getIntent().getSerializableExtra("carInsBaseInfo");
        offerInfo = (NwCarInsCompanyBean) getIntent().getSerializableExtra("offerInfo");
        initView();
        initEvent();
        initData();
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

    private void initData() {

        CommonUtil.loadImageFromUrl(NwCarInsInsureActivity.this, img_company_logo, offerInfo.getImgUrl() + "");
        txt_company_name.setText(offerInfo.getName());

        CustomerBean carowner = carInsBaseInfo.getCustomers().get(0);

        txt_carowner_name.setText(carowner.getName());
        txt_carowner_certno.setText(carowner.getCertNo());
        txt_carowner_address.setText(carowner.getAddress());


        imgKey_carowner_xingshizheng = carInsBaseInfo.getCar().getLicensePicKey();
        imgUrl_carowner_xingshizheng = carInsBaseInfo.getCar().getLicensePicUrl();

        if (!StringUtil.isEmptyNotNull(imgUrl_carowner_xingshizheng)) {
            CommonUtil.loadImageFromUrl(NwCarInsInsureActivity.this, img_carowner_xingshizheng, imgUrl_carowner_xingshizheng);
        }


        imgKey_carowner_shenfenzheng_face = carInsBaseInfo.getCustomers().get(0).getIdentityFacePicKey();
        imgUrl_carowner_shenfenzheng_face = carInsBaseInfo.getCustomers().get(0).getIdentityFacePicUrl();

        LogUtil.e("imgUrl_carowner_shenfenzheng_face:"+imgUrl_carowner_shenfenzheng_face);
        LogUtil.e("imgUrl_carowner_shenfenzheng_back:"+imgUrl_carowner_shenfenzheng_back);
        if (!StringUtil.isEmptyNotNull(imgUrl_carowner_shenfenzheng_face)) {
            CommonUtil.loadImageFromUrl(NwCarInsInsureActivity.this, img_carowner_shenfenzheng_face, imgUrl_carowner_shenfenzheng_face);
        }

        imgKey_carowner_shenfenzheng_back = carInsBaseInfo.getCustomers().get(0).getIdentityBackPicKey();
        imgUrl_carowner_shenfenzheng_back = carInsBaseInfo.getCustomers().get(0).getIdentityBackPicUrl();

        if (!StringUtil.isEmptyNotNull(imgUrl_carowner_shenfenzheng_back)) {
            CommonUtil.loadImageFromUrl(NwCarInsInsureActivity.this, img_carowner_shenfenzheng_back, imgUrl_carowner_shenfenzheng_back);
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


        if (StringUtil.isEmpty(imgKey_carowner_shenfenzheng_face)) {
            showToast("请上传身份证（正面）");
            return;
        }

        if (StringUtil.isEmpty(imgKey_carowner_shenfenzheng_back)) {
            showToast("请上传身份证（反面）");
            return;
        }

        if (StringUtil.isEmpty(imgKey_carowner_xingshizheng)) {
            showToast("请上传车辆行驶证");
            return;
        }

        String carowner_name = txt_carowner_name.getText().toString();
        String carowner_certno = txt_carowner_certno.getText().toString();
        String carowner_address = txt_carowner_address.getText().toString();

        if (StringUtil.isEmpty(carowner_name)) {
            showToast("请输入车主姓名");
            return;
        }

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
        params.put("offerId", offerInfo.getOfferId() + "");


        JSONObject jsonObj_CarInfo = new JSONObject();
        try {
            jsonObj_CarInfo.put("belong", carInsBaseInfo.getCar().getBelong());
            jsonObj_CarInfo.put("carType", carInsBaseInfo.getCar().getCarType());
            jsonObj_CarInfo.put("licensePlateNo", carInsBaseInfo.getCar().getLicensePlateNo());
            jsonObj_CarInfo.put("vin", carInsBaseInfo.getCar().getVin());
            jsonObj_CarInfo.put("engineNo", carInsBaseInfo.getCar().getEngineNo());
            jsonObj_CarInfo.put("modelCode", carInsBaseInfo.getCar().getModelCode());
            jsonObj_CarInfo.put("modelName", carInsBaseInfo.getCar().getModelName());
            jsonObj_CarInfo.put("firstRegisterDate", carInsBaseInfo.getCar().getFirstRegisterDate());
            jsonObj_CarInfo.put("displacement", carInsBaseInfo.getCar().getDisplacement());
            jsonObj_CarInfo.put("marketYear", carInsBaseInfo.getCar().getMarketYear());
            jsonObj_CarInfo.put("ratedPassengerCapacity", carInsBaseInfo.getCar().getRatedPassengerCapacity());
            jsonObj_CarInfo.put("replacementValue", carInsBaseInfo.getCar().getReplacementValue());
            jsonObj_CarInfo.put("chgownerType", carInsBaseInfo.getCar().getChgownerType());
            jsonObj_CarInfo.put("chgownerDate", carInsBaseInfo.getCar().getChgownerDate());
            jsonObj_CarInfo.put("tonnage", carInsBaseInfo.getCar().getTonnage());
            jsonObj_CarInfo.put("wholeWeight", carInsBaseInfo.getCar().getWholeWeight());
            jsonObj_CarInfo.put("licensePicKey", imgKey_carowner_xingshizheng);
            jsonObj_CarInfo.put("licensePicUrl", imgUrl_carowner_xingshizheng);
            jsonObj_CarInfo.put("carCertPicKey", carInsBaseInfo.getCar().getCarCertPicKey());
            jsonObj_CarInfo.put("carInvoicePicKey", carInsBaseInfo.getCar().getCarInvoicePicKey());
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        params.put("car", jsonObj_CarInfo);

        JSONArray json_Customers = new JSONArray();

        try {
            for (CustomerBean item : carInsBaseInfo.getCustomers()) {
                JSONObject jsonObj_Customer = new JSONObject();
                jsonObj_Customer.put("insuredFlag", item.getInsuredFlag());
                jsonObj_Customer.put("name", txt_carowner_name.getText() + "");
                jsonObj_Customer.put("certNo", txt_carowner_certno.getText() + "");
                jsonObj_Customer.put("mobile", item.getMobile());
                jsonObj_Customer.put("address", txt_carowner_address.getText() + "");
                jsonObj_Customer.put("identityFacePicKey", imgKey_carowner_shenfenzheng_face);
                jsonObj_Customer.put("identityFacePicUrl", imgUrl_carowner_shenfenzheng_face);
                jsonObj_Customer.put("identityBackPicKey", imgKey_carowner_shenfenzheng_back);
                jsonObj_Customer.put("identityBackPicUrl", imgUrl_carowner_shenfenzheng_back);
                jsonObj_Customer.put("orgPicKey", item.getOrgPicKey());
                json_Customers.put(jsonObj_Customer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        params.put("customers", json_Customers);

        postWithMy(Config.URL.carInsInsure, params, null, true, "正在核保中", new HttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                ApiResultBean<Object> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<Object>>() {
                });
                showToast(rt.getMessage());
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
                case choice_index_carowner_shenfenzheng_face:
                    getImageInfo(choice_index_carowner_shenfenzheng_face, "11", path_carowner_shenfenzheng_face);
                    break;
                case choice_index_carowner_shenfenzheng_back:
                    getImageInfo(choice_index_carowner_shenfenzheng_back, "1", path_carowner_shenfenzheng_back);
                    break;
                case choice_index_carowner_xingshizheng:
                    getImageInfo(choice_index_carowner_xingshizheng, "10", path_carowner_xingshizheng);
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

    private void getImageInfo(final int choice_index, String type, String filePath) {

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

                ApiResultBean<NwUploadResultBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<NwUploadResultBean>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {

                    switch (choice_index) {
                        case choice_index_carowner_xingshizheng:

                            ApiResultBean<NwUploadResultByLicenseBean> xingshizheng = JSON.parseObject(response, new TypeReference<ApiResultBean<NwUploadResultByLicenseBean>>() {
                            });

                            imgKey_carowner_xingshizheng = xingshizheng.getData().getKey();
                            imgUrl_carowner_xingshizheng = xingshizheng.getData().getUrl();

                            onLoad(img_carowner_xingshizheng, path_carowner_xingshizheng);
                            break;
                        case choice_index_carowner_shenfenzheng_face:
                            ApiResultBean<NwUploadResultByIdentityBean> shenfenzheng_face = JSON.parseObject(response, new TypeReference<ApiResultBean<NwUploadResultByIdentityBean>>() {
                            });

                            if (shenfenzheng_face.getData() != null) {
                                if (shenfenzheng_face.getData().getInfo() != null) {
                                    txt_carowner_name.setText(shenfenzheng_face.getData().getInfo().getName());
                                    txt_carowner_certno.setText(shenfenzheng_face.getData().getInfo().getNum());
                                    txt_carowner_address.setText(shenfenzheng_face.getData().getInfo().getAddress());
                                }
                            }

                            imgKey_carowner_shenfenzheng_face = shenfenzheng_face.getData().getKey();
                            imgUrl_carowner_shenfenzheng_face = shenfenzheng_face.getData().getUrl();

                            onLoad(img_carowner_shenfenzheng_face, path_carowner_shenfenzheng_face);
                            break;
                        case choice_index_carowner_shenfenzheng_back:
                            ApiResultBean<NwUploadResultBean> shenfenzheng_back = JSON.parseObject(response, new TypeReference<ApiResultBean<NwUploadResultBean>>() {
                            });

                            imgKey_carowner_shenfenzheng_back = shenfenzheng_back.getData().getKey();
                            imgUrl_carowner_shenfenzheng_back = shenfenzheng_back.getData().getUrl();
                            onLoad(img_carowner_shenfenzheng_back, path_carowner_shenfenzheng_back);
                            break;
                    }

                } else {
                    showToast(rt.getMessage());

//                    switch (choice_index) {
//                        case choice_index_carowner_xingshizheng:
//                            imgKey_carowner_xingshizheng = "";
//                            imgUrl_carowner_xingshizheng = "";
//                            break;
//                        case choice_index_carowner_shenfenzheng_face:
//                            imgKey_carowner_shenfenzheng_face = "";
//                            imgUrl_carowner_shenfenzheng_face = "";
//                            break;
//                        case choice_index_carowner_shenfenzheng_back:
//                            imgKey_carowner_shenfenzheng_back = "";
//                            imgUrl_carowner_shenfenzheng_back = "";
//                            break;
//                    }
                }


            }
        });
    }

    @Override
    public void OnCropFail(String error_tx) {
        LogUtil.i(TAG, "choice_index=" + choice_index + ">error>>" + error_tx);
    }


}
