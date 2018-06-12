package com.uplink.carins.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.uplink.carins.R;
import com.uplink.carins.ui.choicephoto.ChoicePhotoAndCropAndSwipeBackActivity;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.BitmapUtil;
import com.uplink.carins.utils.LogUtil;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nwcarins_insure);

        initView();
        initEvent();

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

        // 身份证
        layout_carowner_shenfenzheng_face = (LinearLayout) findViewById(R.id.layout_carowner_shenfenzheng_face);
        img_carowner_shenfenzheng_face = (ImageView) findViewById(R.id.img_carowner_shenfenzheng_face);

        // 身份证
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
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
                path_carowner_shenfenzheng_back= photo_path;
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
                    break;
                case choice_index_carowner_shenfenzheng_face:
                    onLoad(img_carowner_shenfenzheng_face, path_carowner_shenfenzheng_face);
                    break;
                case choice_index_carowner_shenfenzheng_back:
                    onLoad(img_carowner_shenfenzheng_back, path_carowner_shenfenzheng_back);
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
