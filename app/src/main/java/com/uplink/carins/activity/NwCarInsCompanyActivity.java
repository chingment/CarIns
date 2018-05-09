package com.uplink.carins.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.uplink.carins.model.api.CarInfoResultBean;
import com.uplink.carins.model.api.CarInsCompanyBean;
import com.uplink.carins.model.api.NwCarInsChannelBean;
import com.uplink.carins.model.api.NwCarInsCompanyResultBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.ViewHolder;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class NwCarInsCompanyActivity extends SwipeBackActivity implements View.OnClickListener {
    private String TAG = "NwCarInsCompanyActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;
    private ListView list_carinscompany;

    private CarInfoResultBean carInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nwcarins_company);
        carInfo = (CarInfoResultBean) getIntent().getSerializableExtra("dataBean");

        initView();
        initEvent();

        getComanyInfo();
    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);

        list_carinscompany = (ListView) findViewById(R.id.list_carinscompany);
        txtHeaderTitle.setText("报价");
    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);
    }

    private void getComanyInfo() {

        Map<String, Object> params = new HashMap<>();

        params.put("orderSeq", carInfo.getOrderSeq());
        params.put("areaId", 440100);

        postWithMy(Config.URL.carInsComanyInfo, params, null, true, "正在加载中", new HttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                LogUtil.i(TAG, "onSuccess====>>>" + response);

                ApiResultBean<NwCarInsCompanyResultBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<NwCarInsCompanyResultBean>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {
                    CarInsCompanyAdapter list_carinscompany_adapter = new CarInsCompanyAdapter();
                    list_carinscompany.setAdapter(list_carinscompany_adapter);
                    list_carinscompany_adapter.setData(rt.getData().getChannels());

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
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
        }
    }

    private class CarInsCompanyAdapter extends BaseAdapter { // shoplist适配器

        private List<NwCarInsChannelBean> carInsCompanys;

        CarInsCompanyAdapter() {

            this.carInsCompanys = new ArrayList<>();
        }

        public void setData(List<NwCarInsChannelBean> carInsCompanys) {
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
            NwCarInsChannelBean bean = carInsCompanys.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(NwCarInsCompanyActivity.this).inflate(R.layout.item_company_insoffer, parent, false);
            }
            ImageView item_img = ViewHolder.get(convertView, R.id.item_company_img);
            TextView item_name = ViewHolder.get(convertView, R.id.item_company_name);
            TextView item_desc = ViewHolder.get(convertView, R.id.item_company_desc);
            Button item_btnoffer = ViewHolder.get(convertView, R.id.item_company_btnoffer);
            LinearLayout item_arrow_right = ViewHolder.get(convertView, R.id.item_layout_arrow_right);

            CommonUtil.loadImageFromUrl(NwCarInsCompanyActivity.this, item_img, bean.getCompanyImg() + "");

            item_name.setText(bean.getName());
            item_desc.setText(bean.getDescp());
            return convertView;
        }
    }
}
