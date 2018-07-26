package com.uplink.carins.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.uplink.carins.model.api.NwCarInsInsureResult;
import com.uplink.carins.model.api.NwUploadResultBean;
import com.uplink.carins.model.api.NwUploadResultByIdentityBean;
import com.uplink.carins.model.api.NwUploadResultByLicenseBean;
import com.uplink.carins.model.api.OrderType;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.choicephoto.ChoicePhotoAndCropAndSwipeBackActivity;
import com.uplink.carins.ui.dialog.CustomConfirmDialog;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.AbFileUtil;
import com.uplink.carins.utils.BitmapUtil;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.NoDoubleClickUtils;
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

    private ImageView company_img;
    private TextView company_name;
    private TextView company_offerpremium;
    private LinearLayout layout_company_info;

    private EditText txt_carowner_name;
    private EditText txt_carowner_certno;
    private EditText txt_carowner_address;

    private int choice_index = -1;
    private final int choice_index_carowner_xingshizheng = 101;
    private final int choice_index_carowner_shenfenzheng_face = 102;
    private final int choice_index_carowner_shenfenzheng_back = 103;

    private LinearLayout layout_carowner_xingshizheng;
    private LinearLayout layout_carowner_xingshizheng_select;
    private ImageView img_carowner_xingshizheng;
    private ImageView img_carowner_xingshizheng_loading;
    private String path_carowner_xingshizheng = "";

    private LinearLayout layout_carowner_shenfenzheng_face;
    private LinearLayout layout_carowner_shenfenzheng_face_select;
    private ImageView img_carowner_shenfenzheng_face;
    private ImageView img_carowner_shenfenzheng_face_loading;
    private String path_carowner_shenfenzheng_face = "";

    private LinearLayout layout_carowner_shenfenzheng_back;
    private LinearLayout layout_carowner_shenfenzheng_back_select;
    private ImageView img_carowner_shenfenzheng_back;
    private ImageView img_carowner_shenfenzheng_back_loading;
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

        LogUtil.e("offerInfo.getAuto():" + offerInfo.getAuto());
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

        if (offerInfo.getAuto().equals("0")) {
            btn_submit.setText("提交核保");
        } else {
            btn_submit.setText("快速核保");
        }

        company_img = (ImageView) findViewById(R.id.company_img);
        company_name = (TextView) findViewById(R.id.company_name);
        company_offerpremium = (TextView) findViewById(R.id.company_offerpremium);
        layout_company_info = (LinearLayout) findViewById(R.id.layout_company_info);
        txt_carowner_name = (EditText) findViewById(R.id.txt_carowner_name);
        txt_carowner_certno = (EditText) findViewById(R.id.txt_carowner_certno);
        txt_carowner_address = (EditText) findViewById(R.id.txt_carowner_address);

        // 车辆行驶证
        layout_carowner_xingshizheng = (LinearLayout) findViewById(R.id.layout_carowner_xingshizheng);
        layout_carowner_xingshizheng_select = (LinearLayout) findViewById(R.id.layout_carowner_xingshizheng_select);
        img_carowner_xingshizheng = (ImageView) findViewById(R.id.img_carowner_xingshizheng);
        img_carowner_xingshizheng_loading = (ImageView) findViewById(R.id.img_carowner_xingshizheng_loading);
        // 身份证正面
        layout_carowner_shenfenzheng_face = (LinearLayout) findViewById(R.id.layout_carowner_shenfenzheng_face);
        layout_carowner_shenfenzheng_face_select = (LinearLayout) findViewById(R.id.layout_carowner_shenfenzheng_face_select);
        img_carowner_shenfenzheng_face = (ImageView) findViewById(R.id.img_carowner_shenfenzheng_face);
        img_carowner_shenfenzheng_face_loading = (ImageView) findViewById(R.id.img_carowner_shenfenzheng_face_loading);
        // 身份证反面
        layout_carowner_shenfenzheng_back = (LinearLayout) findViewById(R.id.layout_carowner_shenfenzheng_back);
        layout_carowner_shenfenzheng_back_select = (LinearLayout) findViewById(R.id.layout_carowner_shenfenzheng_back_select);
        img_carowner_shenfenzheng_back = (ImageView) findViewById(R.id.img_carowner_shenfenzheng_back);
        img_carowner_shenfenzheng_back_loading = (ImageView) findViewById(R.id.img_carowner_shenfenzheng_back_loading);

    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        layout_company_info.setOnClickListener(this);
        layout_carowner_xingshizheng.setOnClickListener(this);
        layout_carowner_shenfenzheng_face.setOnClickListener(this);
        layout_carowner_shenfenzheng_back.setOnClickListener(this);
    }

    private void initData() {

        CommonUtil.loadImageFromUrl(NwCarInsInsureActivity.this, company_img, offerInfo.getImgUrl() + "");
        company_name.setText(offerInfo.getName());
        company_offerpremium.setText(offerInfo.getOfferSumPremium() + "");

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
            case R.id.layout_company_info:
                finish();
                break;
            case R.id.btn_submit:
                if (!NoDoubleClickUtils.isDoubleClick()) {
                    submit();
                }
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

    private CustomConfirmDialog dialog_ConfirmArtificial;


    private void submit() {

        if (offerInfo.getAuto().equals("1")) {
            insure("正在核保中");
        } else {
            if (dialog_ConfirmArtificial == null) {

                dialog_ConfirmArtificial = new CustomConfirmDialog(NwCarInsInsureActivity.this, "提交核保需要等候约10分钟，请注意查看我的订单？", true);

                dialog_ConfirmArtificial.getBtnSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        insure("正在提交中");

                        dialog_ConfirmArtificial.dismiss();
                    }
                });

                dialog_ConfirmArtificial.getBtnCancle().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_ConfirmArtificial.dismiss();
                    }
                });
            }

            dialog_ConfirmArtificial.show();
        }


    }

    private CustomConfirmDialog dialog_ArtificialSuccess;

    private void showArtificialSuccessDialog() {
        if (dialog_ArtificialSuccess == null) {

            dialog_ArtificialSuccess = new CustomConfirmDialog(NwCarInsInsureActivity.this, "核保订单提交成功", false);

            dialog_ArtificialSuccess.getBtnSure().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog_ArtificialSuccess.dismiss();

                    Intent l_Intent = new Intent(NwCarInsInsureActivity.this, OrderListActivity.class);
                    l_Intent.putExtra("status", 1);
                    startActivity(l_Intent);
                    finish();

                }
            });

            dialog_ArtificialSuccess.getBtnCancle().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog_ArtificialSuccess.dismiss();
                }
            });


        }

        dialog_ArtificialSuccess.show();
    }


    public void insure(String loadingmsg) {

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
        params.put("auto", offerInfo.getAuto() + "");

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

        postWithMy(Config.URL.carInsInsure, params, null, true, loadingmsg, new HttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                ApiResultBean<NwCarInsInsureResult> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<NwCarInsInsureResult>>() {
                });

                if (offerInfo.getAuto().equals("1")) {

                    if (rt.getResult() == Result.SUCCESS) {
                        Intent intent = new Intent(NwCarInsInsureActivity.this, NwCarInsInsureResultActivity.class);
                        Bundle b = new Bundle();
                        b.putSerializable("offerInfo", offerInfo);
                        b.putSerializable("insureInfo", rt.getData());
                        intent.putExtras(b);
                        startActivity(intent);
                    } else {

                        if (dialog_ConfirmArtificial == null) {

                            dialog_ConfirmArtificial = new CustomConfirmDialog(NwCarInsInsureActivity.this, "核保失败，提交人工核保需要等候约10分钟，请注意查看我的订单？", true);

                            dialog_ConfirmArtificial.getBtnSure().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    insure("正在提交中");

                                    dialog_ConfirmArtificial.dismiss();
                                }
                            });

                            dialog_ConfirmArtificial.getBtnCancle().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog_ConfirmArtificial.dismiss();
                                }
                            });
                        }

                        dialog_ConfirmArtificial.show();

                    }

                } else {

                    if (rt.getResult() == Result.SUCCESS) {
                        showArtificialSuccessDialog();
                    } else {
                        showToast(rt.getMessage());
                    }
                }
            }
        });

    }

    @Override
    public void OnCropSuccess(String photo_path) {

        Animation m_animation = AnimationUtils.loadAnimation(NwCarInsInsureActivity.this,
                R.anim.dialog_load_animation2);

        switch (choice_index) {
            case choice_index_carowner_xingshizheng:
                path_carowner_xingshizheng = photo_path;
                img_carowner_xingshizheng.setVisibility(View.GONE);
                layout_carowner_xingshizheng_select.setVisibility(View.GONE);
                img_carowner_xingshizheng_loading.setVisibility(View.VISIBLE);
                img_carowner_xingshizheng_loading.startAnimation(m_animation);
                loadImageHandler.sendEmptyMessage(choice_index_carowner_xingshizheng);
                break;
            case choice_index_carowner_shenfenzheng_face:
                path_carowner_shenfenzheng_face = photo_path;
                img_carowner_shenfenzheng_face.setVisibility(View.GONE);
                layout_carowner_shenfenzheng_face_select.setVisibility(View.GONE);
                img_carowner_shenfenzheng_face_loading.setVisibility(View.VISIBLE);
                img_carowner_shenfenzheng_face_loading.startAnimation(m_animation);
                loadImageHandler.sendEmptyMessage(choice_index_carowner_shenfenzheng_face);
                break;
            case choice_index_carowner_shenfenzheng_back:
                path_carowner_shenfenzheng_back = photo_path;
                img_carowner_shenfenzheng_face_loading.setVisibility(View.VISIBLE);
                img_carowner_shenfenzheng_back.setVisibility(View.GONE);
                layout_carowner_shenfenzheng_back_select.setVisibility(View.GONE);
                img_carowner_shenfenzheng_back_loading.setVisibility(View.VISIBLE);
                img_carowner_shenfenzheng_back_loading.startAnimation(m_animation);
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
            iv.setVisibility(View.VISIBLE);
        } catch (Exception e) {

        }
    }

    private void getImageInfo(final int choice_index, String type, String filePath) {

        Map<String, Object> params = new HashMap<>();
        params.put("type", type);

        Map<String, String> files = new HashMap<>();
        if (!StringUtil.isEmpty(filePath)) {
            files.put("certPic", filePath);
        }

        postWithMy(Config.URL.carInsUploadImg, params, files, false, "正在上传中", new HttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                ApiResultBean<NwUploadResultBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<NwUploadResultBean>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {

                    switch (choice_index) {
                        case choice_index_carowner_xingshizheng:

                            ApiResultBean<NwUploadResultByLicenseBean> xingshizheng = JSON.parseObject(response, new TypeReference<ApiResultBean<NwUploadResultByLicenseBean>>() {
                            });

                            if (xingshizheng.getData().getKey() == null) {
                                failureDealt(choice_index);
                            } else {
                                imgKey_carowner_xingshizheng = xingshizheng.getData().getKey();
                                imgUrl_carowner_xingshizheng = xingshizheng.getData().getUrl();
                                onLoad(img_carowner_xingshizheng, path_carowner_xingshizheng);
                            }
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

                            if (shenfenzheng_face.getData().getKey() == null) {
                                failureDealt(choice_index);
                            } else {
                                onLoad(img_carowner_shenfenzheng_face, path_carowner_shenfenzheng_face);
                            }
                            break;
                        case choice_index_carowner_shenfenzheng_back:
                            ApiResultBean<NwUploadResultBean> shenfenzheng_back = JSON.parseObject(response, new TypeReference<ApiResultBean<NwUploadResultBean>>() {
                            });

                            imgKey_carowner_shenfenzheng_back = shenfenzheng_back.getData().getKey();
                            imgUrl_carowner_shenfenzheng_back = shenfenzheng_back.getData().getUrl();

                            if (shenfenzheng_back.getData().getKey() == null) {
                                failureDealt(choice_index);
                            } else {
                                onLoad(img_carowner_shenfenzheng_back, path_carowner_shenfenzheng_back);
                            }
                            break;
                    }

                } else {
                    showToast(rt.getMessage());

                    failureDealt(choice_index);
                }
            }

            @Override
            public void onFailure(Request request, Exception e) {
                failureDealt(choice_index);
            }
        });
    }


    private void failureDealt(int choice_index) {
        switch (choice_index) {
            case choice_index_carowner_xingshizheng:
                imgKey_carowner_xingshizheng = "";
                imgUrl_carowner_xingshizheng = "";

                img_carowner_xingshizheng.setVisibility(View.GONE);
                layout_carowner_xingshizheng_select.setVisibility(View.VISIBLE);
                img_carowner_xingshizheng_loading.setVisibility(View.GONE);
                img_carowner_xingshizheng_loading.clearAnimation();

                break;
            case choice_index_carowner_shenfenzheng_face:
                imgKey_carowner_shenfenzheng_face = "";
                imgUrl_carowner_shenfenzheng_face = "";
                img_carowner_shenfenzheng_face.setVisibility(View.GONE);
                layout_carowner_shenfenzheng_face_select.setVisibility(View.VISIBLE);
                img_carowner_shenfenzheng_face_loading.setVisibility(View.GONE);
                img_carowner_shenfenzheng_face_loading.clearAnimation();

                break;
            case choice_index_carowner_shenfenzheng_back:
                imgKey_carowner_shenfenzheng_back = "";
                imgUrl_carowner_shenfenzheng_back = "";
                img_carowner_shenfenzheng_back.setVisibility(View.GONE);
                layout_carowner_shenfenzheng_back_select.setVisibility(View.VISIBLE);
                img_carowner_shenfenzheng_back_loading.setVisibility(View.GONE);
                img_carowner_shenfenzheng_back_loading.clearAnimation();

                break;
        }
    }

    @Override
    public void OnCropFail(String error_tx) {
        LogUtil.i(TAG, "choice_index=" + choice_index + ">error>>" + error_tx);
    }


}
