package com.uplink.carins.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.uplink.carins.R;
import com.uplink.carins.activity.adapter.NwItemParentFieldAdapter;
import com.uplink.carins.model.api.NwCarInsCompanyBean;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;

public class NwCarInsCompanyOfferResultActivity extends SwipeBackActivity implements View.OnClickListener {


    String TAG = "NwCarInsCompanyOfferResultActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;
    private Button btn_submit;

    private NwCarInsCompanyBean offerResult;


    private ImageView company_img;
    private TextView company_name;
    private TextView company_desc;
    private ListView list_offer_parent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nwcarins_companyofferresult);

        initView();
        initEvent();

        offerResult = (NwCarInsCompanyBean) getIntent().getSerializableExtra("dataBean");

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
        list_offer_parent= (ListView) findViewById(R.id.list_offer_parent);
    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    private void initData(NwCarInsCompanyBean bean) {

        if (bean == null) {
            LogUtil.e("bean为空");
            return;
        }



        CommonUtil.loadImageFromUrl(NwCarInsCompanyOfferResultActivity.this, company_img, bean.getImgUrl() + "");
        company_name.setText(bean.getName());
        company_desc.setText(bean.getDescp());


        NwItemParentFieldAdapter adapter=new NwItemParentFieldAdapter(NwCarInsCompanyOfferResultActivity.this,bean.getOfferInquirys());

        list_offer_parent.setAdapter(adapter);


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
