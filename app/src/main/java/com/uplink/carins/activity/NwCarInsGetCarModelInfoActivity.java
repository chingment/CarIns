package com.uplink.carins.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.CarInsCarModelInfoBean;
import com.uplink.carins.model.api.CarInsCarModelInfoResultBean;
import com.uplink.carins.model.api.CarInsGetCarModelInfoPmsBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.ViewHolder;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class NwCarInsGetCarModelInfoActivity extends SwipeBackActivity implements View.OnClickListener {
    private String TAG = "NwCarInsGetCarModelInfoActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;

    private EditText txt_keyword;
    private Button btn_submit;
    private LayoutInflater inflater;
    private CarInsGetCarModelInfoPmsBean pms;
    private ListView list_models;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nwcarins_getcarmodelinfo);

        pms = (CarInsGetCarModelInfoPmsBean) getIntent().getSerializableExtra("dataBean");

        initView();
        initEvent();

        txt_keyword.setText(pms.getVin());
    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("搜索车型");

        inflater = LayoutInflater.from(this);
        txt_keyword = (EditText) findViewById(R.id.txt_keyword);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        list_models = (ListView) findViewById(R.id.list_models);
    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.btn_submit:
                getCarModelInfo();
                break;
        }
    }

    private void getCarModelInfo() {

        String keyword = txt_keyword.getText() + "";
        if (StringUtil.isEmpty(keyword)) {
            showToast("请输入车牌号码");
            return;
        }

        String vin = keyword;
        String firstRegisterDate = pms.getFirstRegisterDate();

        Map<String, String> params = new HashMap<>();
        //params.put("userId", this.getAppContext().getUser().getId() + "");
        //params.put("merchantId", this.getAppContext().getUser().getMerchantId() + "");
        //params.put("posMachineId", this.getAppContext().getUser().getPosMachineId()+"");

        params.put("keyword", keyword);
        params.put("vin", vin);
        params.put("firstRegisterDate", firstRegisterDate);
        getWithMy(Config.URL.carInsGetCarModelInfo, params, true, "正在搜索中，请耐心等候", new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                LogUtil.i("onSuccess===>>>" + response);

                ApiResultBean<CarInsCarModelInfoResultBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<CarInsCarModelInfoResultBean>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {

                    CarModelsItemAdapter itemAdapter = new CarModelsItemAdapter();
                    if (rt.getData().getModels() != null) {
                        itemAdapter.setData(rt.getData().getModels());
                    }
                    list_models.setAdapter(itemAdapter);
                }
            }

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
                LogUtil.e("onFailure===>>>" + e.getMessage());
            }

        });
    }

    private class CarModelsItemAdapter extends BaseAdapter {

        private List<CarInsCarModelInfoBean> carInsCarModels = new ArrayList<>();

        CarModelsItemAdapter() {

        }

        public void setData(List<CarInsCarModelInfoBean> carInsCarModels) {
            this.carInsCarModels = carInsCarModels;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return carInsCarModels.size();
        }

        @Override
        public Object getItem(int position) {

            return carInsCarModels.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_list_carmodel, null);
            }
            CarInsCarModelInfoBean bean = carInsCarModels.get(position);
            LinearLayout item_box = ViewHolder.get(convertView, R.id.item_box);
            item_box.setTag(position);
            item_box.setOnClickListener(myClick);
            TextView item_text = ViewHolder.get(convertView, R.id.item_text);
            String txt = bean.getModelName() + ":排量" + bean.getDisplacement() + " " + bean.getMarketYear() + "款" + " " + bean.getRatedPassengerCapacity() + "座" + "（参考价：" + bean.getReplacementValue() + "）";
            item_text.setText(txt);
            return convertView;
        }

        View.OnClickListener myClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = Integer.parseInt(v.getTag().toString());
                CarInsCarModelInfoBean bean = carInsCarModels.get(position);
                Intent intent = new Intent();
                Bundle b = new Bundle();
                b.putString("vin", txt_keyword.getText() + "");
                b.putSerializable("dataBean", bean);
                intent.putExtras(b);
                setResult(RESULT_OK, intent);
                finish();
            }
        };

    }
}
