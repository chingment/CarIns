package com.uplink.carins.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.CarInfoResultBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.choicephoto.ChoicePhotoAndCropAndSwipeBackActivity;
import com.uplink.carins.utils.AbFileUtil;
import com.uplink.carins.utils.BitmapUtil;
import com.uplink.carins.utils.CarKeyboardUtil;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.NoDoubleClickUtils;
import com.uplink.carins.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

public class NwCarInsGetCarInfoActivity extends ChoicePhotoAndCropAndSwipeBackActivity {

    private String TAG = "NwCarInsGetCarInfoActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;

    private int choice_index = -1;
    private final int choice_index_xingshizheng = 101;
    // 车辆行驶证
    private LinearLayout layout_carinsure_xingshizheng;
    private ImageView img_carinsure_xingshizheng;
    private String path_carinsure_xingshizheng = "";

    private Button btn_submit;
    private EditText txt_keyword;
    private CarKeyboardUtil keyboardUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nwcarins_getcarinfo);

        initView();
        initEvent();
    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("车牌号码");

        btn_submit = (Button) findViewById(R.id.btn_submit);
        txt_keyword = (EditText) findViewById(R.id.txt_keyword);

        txt_keyword.setText("粤A9RS97");
        keyboardUtil = new CarKeyboardUtil(this, txt_keyword);
        // 车辆行驶证
        layout_carinsure_xingshizheng = (LinearLayout) findViewById(R.id.layout_carinsure_xingshizheng);
        img_carinsure_xingshizheng = (ImageView) findViewById(R.id.img_carinsure_xingshizheng);

    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);

        btn_submit.setOnClickListener(this);
        //车辆行驶证
        layout_carinsure_xingshizheng.setOnClickListener(this);

        txt_keyword.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {

                switch (view.getId()) {
                    case R.id.txt_keyword:
                        keyboardUtil.hideSystemKeyBroad();
                        keyboardUtil.hideSoftInputMethod();
                        if (!keyboardUtil.isShow())
                            keyboardUtil.showKeyboard();
                        break;
                    default:
                        if (keyboardUtil.isShow())
                            keyboardUtil.hideKeyboard();
                        break;
                }

                return false;
            }
        });

        txt_keyword.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
// 此处为得到焦点时的处理内容
                } else {
                    if (keyboardUtil.isShow())
                        keyboardUtil.hideKeyboard();
                }
            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (keyboardUtil.isShow()) {
            keyboardUtil.hideKeyboard();
        }
        return super.onTouchEvent(event);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyboardUtil.isShow()) {
                keyboardUtil.hideKeyboard();
            } else {
                finish();
            }
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.btn_submit:
                if (!NoDoubleClickUtils.isDoubleClick()) {

                    String keyword = txt_keyword.getText() + "";
                    if (StringUtil.isEmpty(keyword)) {
                        showToast("请输入车牌号码");
                        return;
                    }
                    submit(2, keyword);
                }
                break;
            case R.id.layout_carinsure_xingshizheng:
                choice_index = choice_index_xingshizheng;
                path_carinsure_xingshizheng = "";
                showChoiceDialog(CropType.need_crop_no_cropimage);
                break;
        }
    }


    private void submit(int keywordType, String keyword) {

        Map<String, Object> params = new HashMap<>();
        //params.put("userId", this.getAppContext().getUser().getId());
        //params.put("merchantId", this.getAppContext().getUser().getMerchantId());
        //params.put("posMachineId", this.getAppContext().getUser().getPosMachineId());
        params.put("keywordType", keywordType);
        params.put("keyword", keyword);


        postWithMy(Config.URL.carInsGetCarInfo, params, null,true,"正在查询中", new HttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                LogUtil.i(TAG, "onSuccess====>>>" + response);

                ApiResultBean<CarInfoResultBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<CarInfoResultBean>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {

                    Intent intent = new Intent(NwCarInsGetCarInfoActivity.this, NwCarInsConfirmCarInfoActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("dataBean", rt.getData());
                    intent.putExtras(b);

                    startActivity(intent);


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
            case choice_index_xingshizheng:
                path_carinsure_xingshizheng = photo_path;
                img_carinsure_xingshizheng.setVisibility(View.VISIBLE);
                loadImageHandler.sendEmptyMessage(choice_index_xingshizheng);
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
                    String filePath = path_carinsure_xingshizheng;
                    String type = filePath.substring(filePath.lastIndexOf("."));
                    String base64ImgStr = AbFileUtil.GetBase64ImageStr(filePath);
                    base64ImgStr = base64ImgStr + "@" + type;
                    submit(1, base64ImgStr);
                    onLoad(img_carinsure_xingshizheng, path_carinsure_xingshizheng);
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
}
