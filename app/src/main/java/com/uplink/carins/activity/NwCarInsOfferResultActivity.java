package com.uplink.carins.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.uplink.carins.R;
import com.uplink.carins.model.api.NwCarInsInsInquiryResultBean;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;

public class NwCarInsOfferResultActivity extends SwipeBackActivity implements View.OnClickListener {


    String TAG = "NwCarInsKindActivity";


    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;
    private Button btn_submit;
    private NwCarInsInsInquiryResultBean offerResult;


    private ImageView company_img;
    private TextView company_name;
    private TextView company_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nwcarins_offerresult);

        initView();
        initEvent();

        offerResult = (NwCarInsInsInquiryResultBean) getIntent().getSerializableExtra("dataBean");

        initData(offerResult);
    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("报价详情");
        btn_submit = (Button) findViewById(R.id.btn_submit);
        company_img = (ImageView) findViewById(R.id.company_img);
        company_name = (TextView) findViewById(R.id.company_name);
        company_desc = (TextView) findViewById(R.id.company_desc);
    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    private void initData(NwCarInsInsInquiryResultBean bean) {

        if (bean == null) {
            LogUtil.e("bean为空");
            return;
        }

        if (bean.getChannel() == null) {
            LogUtil.e("bean.getChannel为空");
            return;
        }

        CommonUtil.loadImageFromUrl(NwCarInsOfferResultActivity.this, company_img, bean.getChannel().getCompanyImg() + "");
        company_name.setText(bean.getChannel().getName());
        company_desc.setText(bean.getChannel().getDescp());

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.btn_submit:


                break;
        }
    }
}
