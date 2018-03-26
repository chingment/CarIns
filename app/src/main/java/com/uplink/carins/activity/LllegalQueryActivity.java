package com.uplink.carins.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.uplink.carins.Own.AppCacheManager;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.CarTypeBean;
import com.uplink.carins.model.api.LllegalPriceRecordBean;
import com.uplink.carins.model.api.LllegalQueryLogBean;
import com.uplink.carins.model.api.LllegalQueryResultBean;
import com.uplink.carins.model.api.OrderInfoBean;
import com.uplink.carins.model.api.PayConfirmBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.ViewHolder;
import com.uplink.carins.ui.my.MyGridView;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.NoDoubleClickUtils;
import com.uplink.carins.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class LllegalQueryActivity extends SwipeBackActivity implements View.OnClickListener {

    String TAG = "LllegalQueryActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;
    private LayoutInflater inflater;

    private MyGridView gridview_querylog;

    private EditText form_lllegalquery_txt_carno;
    private LinearLayout sel_lllegalquery_cartype;
    private TextView form_lllegalquery_txt_cartype;
    private LinearLayout form_lllegalquery_cb_isCompany;
    private CheckBox form_lllegalquery_cb_isCompany1;
    private CheckBox form_lllegalquery_cb_isCompany2;
    private EditText form_lllegalquery_txt_rackno;
    private EditText form_lllegalquery_txt_enginno;
    private TextView txt_queryscore;
    private Button btn_submit_query;
    private TextView btn_recharge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lllegalquery);
        initView();
        initEvent();


        getQuerylog();
    }


    private PopupWindow popupWindowForCarTypes;
    private ListView popupListViewForCarTypes;
    private List<CarTypeBean> carTypes;

    private void showPopupCarTypes(View v) {

        popupWindowForCarTypes.setFocusable(true);
        popupWindowForCarTypes.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindowForCarTypes.setBackgroundDrawable(new BitmapDrawable());
        popupWindowForCarTypes.showAsDropDown(v);

    }

    private void initView() {

        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("违章查询");

        gridview_querylog = (MyGridView) findViewById(R.id.gridview_querylog);
        form_lllegalquery_txt_carno = (EditText) findViewById(R.id.form_lllegalquery_txt_carno);
        sel_lllegalquery_cartype = (LinearLayout) findViewById(R.id.sel_lllegalquery_cartype);
        form_lllegalquery_txt_cartype = (TextView) findViewById(R.id.form_lllegalquery_txt_cartype);
        form_lllegalquery_cb_isCompany = (LinearLayout) findViewById(R.id.form_lllegalquery_cb_isCompany);
        form_lllegalquery_cb_isCompany1 = (CheckBox) findViewById(R.id.form_lllegalquery_cb_isCompany1);
        form_lllegalquery_cb_isCompany2 = (CheckBox) findViewById(R.id.form_lllegalquery_cb_isCompany2);

        form_lllegalquery_txt_rackno = (EditText) findViewById(R.id.form_lllegalquery_txt_rackno);
        form_lllegalquery_txt_enginno = (EditText) findViewById(R.id.form_lllegalquery_txt_enginno);
        btn_submit_query = (Button) findViewById(R.id.btn_submit_query);

        inflater = LayoutInflater.from(this);

        txt_queryscore = (TextView) findViewById(R.id.txt_queryscore);
        txt_queryscore.setText(AppCacheManager.getLllegalQueryScore());

        btn_recharge = (TextView) findViewById(R.id.btn_recharge);

        View popupViewForCarTypes = inflater.inflate(R.layout.popu_list, null);
        popupListViewForCarTypes = (ListView) popupViewForCarTypes.findViewById(R.id.popu_list);


        carTypes = new ArrayList<>();
        carTypes.add(new CarTypeBean("1", "1"));
        carTypes.add(new CarTypeBean("2", "2"));
        carTypes.add(new CarTypeBean("3", "3"));
        carTypes.add(new CarTypeBean("4", "4"));
        carTypes.add(new CarTypeBean("5", "5"));
        carTypes.add(new CarTypeBean("6", "6"));
        carTypes.add(new CarTypeBean("7", "7"));

        CarTypesPopuAdapter popupCarTypesListView_Adapter = new CarTypesPopuAdapter(carTypes);
        popupListViewForCarTypes.setAdapter(popupCarTypesListView_Adapter);


        popupWindowForCarTypes = new PopupWindow(popupViewForCarTypes, getWindowsDisplay().getWidth(), 500);
    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);

        btn_recharge.setOnClickListener(this);

        sel_lllegalquery_cartype.setOnClickListener(this);
        form_lllegalquery_cb_isCompany1.setOnCheckedChangeListener(form_carclaim_claimtype_CheckListener);
        form_lllegalquery_cb_isCompany2.setOnCheckedChangeListener(form_carclaim_claimtype_CheckListener);
        btn_submit_query.setOnClickListener(this);

        popupListViewForCarTypes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                CarTypeBean bean = carTypes.get(position);
                form_lllegalquery_txt_cartype.setText(bean.getName() + "");
                form_lllegalquery_txt_cartype.setTag(bean.getCode() + "");
                if (popupWindowForCarTypes != null)
                    popupWindowForCarTypes.dismiss();
            }
        });
    }


    CheckBox.OnCheckedChangeListener form_carclaim_claimtype_CheckListener = new CheckBox.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked)
                switch (buttonView.getId()) {
                    case R.id.form_lllegalquery_cb_isCompany1:
                        form_lllegalquery_cb_isCompany2.setChecked(false);
                        form_lllegalquery_cb_isCompany.setTag("false");
                        break;
                    case R.id.form_lllegalquery_cb_isCompany2:
                        form_lllegalquery_cb_isCompany1.setChecked(false);
                        form_lllegalquery_cb_isCompany.setTag("true");
                        break;
                }
        }
    };


    public void getQuerylog() {


        Map<String, String> params = new HashMap<>();

        params.put("userId", getAppContext().getUser().getId() + "");
        params.put("merchantId", getAppContext().getUser().getMerchantId() + "");
        params.put("posMachineId", getAppContext().getUser().getPosMachineId() + "");

        getWithMy(Config.URL.lllegalQueryLog, params, new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                LogUtil.i(TAG, "onSuccess====>>>" + response);

                ApiResultBean<List<LllegalQueryLogBean>> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<List<LllegalQueryLogBean>>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {
                    LllegalQueryLogItemAdapter nineGridItemdapter = new LllegalQueryLogItemAdapter(rt.getData());

                    gridview_querylog.setAdapter(nineGridItemdapter);
                }

            }

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
                LogUtil.e(TAG, "onFailure====>>>" + e.getMessage());
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.btn_submit_query:
                submitQuery();
                break;
            case R.id.sel_lllegalquery_cartype:
                showPopupCarTypes(v);
                break;
            case R.id.btn_recharge:
                recharge();
                break;
        }
    }


    private void submitQuery() {

        String carno = form_lllegalquery_txt_carno.getText() + "";
        String cartype = form_lllegalquery_txt_cartype.getTag() + "";
        String isCompany = form_lllegalquery_cb_isCompany.getTag() + "";
        String rackno = form_lllegalquery_txt_rackno.getText() + "";
        String enginno = form_lllegalquery_txt_enginno.getText() + "";

        if (StringUtil.isEmptyNotNull(carno)) {
            showToast("请输入车牌号码");
            return;
        }

        if (StringUtil.isEmptyNotNull(cartype)) {
            showToast("请选择车辆类型");
            return;
        }

        if (StringUtil.isEmptyNotNull(isCompany)) {
            showToast("请选择营业性质");
            return;
        }

        if (StringUtil.isEmptyNotNull(rackno)) {
            showToast("请输入车架号");
            return;
        }

        if (StringUtil.isEmptyNotNull(enginno)) {
            showToast("请输入发动机号");
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("userId", this.getAppContext().getUser().getId());
        params.put("merchantId", this.getAppContext().getUser().getMerchantId());
        params.put("posMachineId", this.getAppContext().getUser().getPosMachineId());
        params.put("carNo", carno);
        params.put("carType", cartype);
        params.put("isCompany", isCompany);
        params.put("rackNo", rackno);
        params.put("enginNo", enginno);

        postWithMy(Config.URL.lllegalQuery, params, null, new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                LogUtil.i(TAG, "onSuccess====>>>" + response);

                ApiResultBean<LllegalQueryResultBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<LllegalQueryResultBean>>() {
                });

                showToast(rt.getMessage());
                if (rt.getResult() == Result.SUCCESS) {

                    txt_queryscore.setText(rt.getData().getQueryScore() + "");

                    Bundle b = new Bundle();
                    b.putSerializable("dataBean", rt.getData());
                    Intent intent = new Intent(LllegalQueryActivity.this, LllegalQueryResultActivity.class);
                    intent.putExtras(b);
                    startActivity(intent);
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


    private void recharge() {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", this.getAppContext().getUser().getId());
        params.put("merchantId", this.getAppContext().getUser().getMerchantId());
        params.put("posMachineId", this.getAppContext().getUser().getPosMachineId());
        params.put("merchantId", this.getAppContext().getUser().getMerchantId());
        params.put("score", "50");

        postWithMy(Config.URL.submitLllegalQueryScoreRecharge, params, null, new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                LogUtil.i(TAG, "onSuccess====>>>" + response);

                ApiResultBean<OrderInfoBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<OrderInfoBean>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {

                    Intent intent = new Intent(LllegalQueryActivity.this, PayConfirmActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("dataBean", rt.getData());
                    intent.putExtras(b);
                    startActivity(intent);
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

    private class LllegalQueryLogItemAdapter extends BaseAdapter {

        private List<LllegalQueryLogBean> items;

        LllegalQueryLogItemAdapter(List<LllegalQueryLogBean> items) {
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {

            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_lllegalquerylog, null);
            }

            LllegalQueryLogBean bean = items.get(position);

            LinearLayout item_ninegrid = ViewHolder.get(convertView, R.id.item_lllegalquerylog);
            item_ninegrid.setTag(position);
            item_ninegrid.setOnClickListener(myClickListener);


            TextView item_title = ViewHolder.get(convertView, R.id.item_lllegalquerylog_carno);
            item_title.setText(bean.getCarNo());

            return convertView;
        }

        private LinearLayout.OnClickListener myClickListener = new LinearLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();

                LllegalQueryLogBean item = items.get(position);


                form_lllegalquery_txt_carno.setText(item.getCarNo());
                form_lllegalquery_txt_cartype.setTag(item.getCarType() + "");
                form_lllegalquery_cb_isCompany.setTag(item.getIsCompany() + "");
                form_lllegalquery_txt_rackno.setText(item.getRackNo() + "");
                form_lllegalquery_txt_enginno.setText(item.getEnginNo() + "");

                submitQuery();
            }
        };

    }


    private class CarTypesPopuAdapter extends BaseAdapter {

        private List<CarTypeBean> carTypes;

        CarTypesPopuAdapter(List<CarTypeBean> carTypes) {
            this.carTypes = carTypes;
        }

        @Override
        public int getCount() {
            return carTypes.size();
        }

        @Override
        public Object getItem(int position) {

            return carTypes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_company, null);
            }
            TextView item_tv = ViewHolder.get(convertView, R.id.item_company);
            CarTypeBean bean = carTypes.get(position);
            item_tv.setText(bean.getName() + "");
            return convertView;
        }

    }

}
