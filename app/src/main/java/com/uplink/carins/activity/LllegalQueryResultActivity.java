package com.uplink.carins.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.uplink.carins.Own.AppCacheManager;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.LllegalPriceRecordBean;
import com.uplink.carins.model.api.LllegalQueryResultBean;
import com.uplink.carins.model.api.OrderInfoBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.ViewHolder;
import com.uplink.carins.ui.dialog.CustomConfirmDialog;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class LllegalQueryResultActivity extends SwipeBackActivity implements View.OnClickListener {

    String TAG = "LllegalQueryActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;
    private LayoutInflater inflater;
    private ListView form_lllegalqueryresult_list;

    private LllegalQueryResultBean queryResult;

    public  TextView txt_lllegal_dealttip;
    private TextView txt_lllegal_carno;
    private TextView txt_lllegal_sumcount;
    private TextView txt_lllegal_sumpoint;
    private TextView txt_lllegal_sumfine;
    private Button btn_submit;

    private List<LllegalPriceRecordBean> lllegalPriceRecord = new ArrayList<>();

    private RelativeLayout ll2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lllegalqueryresult);

        initView();
        initEvent();

        queryResult = (LllegalQueryResultBean) getIntent().getSerializableExtra("dataBean");

        if (queryResult != null) {

            txt_lllegal_carno.setText(queryResult.getCarNo());
            txt_lllegal_sumcount.setText(queryResult.getSumCount());
            txt_lllegal_sumpoint.setText(queryResult.getSumPoint());
            txt_lllegal_sumfine.setText(queryResult.getSumFine());

            if (queryResult.getRecord() != null) {
                lllegalPriceRecord = queryResult.getRecord();
                LllegalPriceRecordItemAdapter nineGridItemdapter = new LllegalPriceRecordItemAdapter();
                form_lllegalqueryresult_list.setAdapter(nineGridItemdapter);
            }

            if (queryResult.getIsOfferPrice()) {
                btn_submit.setVisibility(View.VISIBLE);
            } else {
                btn_submit.setVisibility(View.GONE);
            }

            if(StringUtil.isEmpty(queryResult.getDealtTip())) {
                ll2.setVisibility(View.GONE);
            }
            else {
                txt_lllegal_dealttip.setText(queryResult.getDealtTip());
                ll2.setVisibility(View.VISIBLE);
            }
        }


    }

    private void initView() {

        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("查询结果");

        inflater = LayoutInflater.from(this);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        txt_lllegal_dealttip= (TextView) findViewById(R.id.txt_lllegal_dealttip);
        txt_lllegal_carno = (TextView) findViewById(R.id.txt_lllegal_carno);
        txt_lllegal_sumcount = (TextView) findViewById(R.id.txt_lllegal_sumcount);
        txt_lllegal_sumpoint = (TextView) findViewById(R.id.txt_lllegal_sumpoint);
        txt_lllegal_sumfine = (TextView) findViewById(R.id.txt_lllegal_sumfine);

        form_lllegalqueryresult_list = (ListView) findViewById(R.id.form_lllegalqueryresult_list);

        ll2 = (RelativeLayout) findViewById(R.id.ll2);
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
                submit();
                break;
        }
    }

    private void submit() {

        int dealtCount = 0;
        for (LllegalPriceRecordBean item : lllegalPriceRecord) {
            if (item.getNeedDealt()) {
                dealtCount += 1;
            }
        }

        if (dealtCount == 0) {
            showToast("请选择要处理的违章");
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("userId", this.getAppContext().getUser().getId());
        params.put("merchantId", this.getAppContext().getUser().getMerchantId());
        params.put("posMachineId", this.getAppContext().getUser().getPosMachineId());
        params.put("carNo", queryResult.getCarNo());

        JSONArray json_Records = new JSONArray();

        try {
            for (LllegalPriceRecordBean item : lllegalPriceRecord) {
                if (item.getNeedDealt()) {
                    JSONObject jsonFa1 = new JSONObject();
                    jsonFa1.put("bookNo", item.getBookNo() + "");
                    jsonFa1.put("bookType", item.getBookType());
                    jsonFa1.put("bookTypeName", item.getBookTypeName());
                    jsonFa1.put("lllegalCode", item.getLllegalCode());
                    jsonFa1.put("cityCode", item.getCityCode());
                    jsonFa1.put("lllegalTime", item.getLllegalTime());
                    jsonFa1.put("point", item.getPoint());
                    jsonFa1.put("offerType", item.getOfferType());
                    jsonFa1.put("ofserTypeName", item.getOfserTypeName());
                    jsonFa1.put("fine", item.getFine());
                    jsonFa1.put("serviceFee", item.getServiceFee());
                    jsonFa1.put("late_fees", item.getLate_fees());
                    jsonFa1.put("content", item.getContent());
                    jsonFa1.put("lllegalDesc", item.getLllegalDesc());
                    jsonFa1.put("lllegalCity", item.getLllegalCity());
                    jsonFa1.put("address", item.getAddress());
                    json_Records.put(jsonFa1);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        params.put("lllegalRecord", json_Records);


        postWithMy(Config.URL.submitLllegalDealt, params, null, new HttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                LogUtil.i(TAG, "onSuccess====>>>" + response);

                ApiResultBean<OrderInfoBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<OrderInfoBean>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {

                    Intent intent = new Intent(LllegalQueryResultActivity.this, PayConfirmActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("dataBean", rt.getData());
                    intent.putExtras(b);
                    startActivityForResult(intent, 1);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            showSuccessDialog();
        }
    }

    private CustomConfirmDialog dialog_Success;

    private void showSuccessDialog() {
        if (dialog_Success == null) {
            dialog_Success = new CustomConfirmDialog(LllegalQueryResultActivity.this, "订单提交成功", false);
            dialog_Success.getBtnSure().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_Success.dismiss();
                    Intent l_Intent = new Intent(LllegalQueryResultActivity.this, OrderListActivity.class);
                    l_Intent.putExtra("status", 4);
                    startActivity(l_Intent);
                    finish();
                }
            });
            dialog_Success.getBtnCancle().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_Success.dismiss();
                }
            });
        }
        dialog_Success.show();
    }

    private class LllegalPriceRecordItemAdapter extends BaseAdapter {


        LllegalPriceRecordItemAdapter() {

        }

        @Override
        public int getCount() {
            return lllegalPriceRecord.size();
        }

        @Override
        public Object getItem(int position) {

            return lllegalPriceRecord.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_lllegalqueryresult, null);
            }

            LllegalPriceRecordBean bean = lllegalPriceRecord.get(position);

            TextView txt_city = ViewHolder.get(convertView, R.id.item_lllegal_city);
            TextView txt_address = ViewHolder.get(convertView, R.id.item_lllegal_address);
            TextView txt_desc = ViewHolder.get(convertView, R.id.item_lllegal_desc);
            TextView txt_time = ViewHolder.get(convertView, R.id.item_lllegal_time);
            TextView txt_point = ViewHolder.get(convertView, R.id.item_lllegal_point);
            TextView txt_fine = ViewHolder.get(convertView, R.id.item_lllegal_fine);
            TextView txt_servicefee = ViewHolder.get(convertView, R.id.item_lllegal_servicefee);
            TextView txt_latefees = ViewHolder.get(convertView, R.id.item_lllegal_late_fees);
            TextView txt_content = ViewHolder.get(convertView, R.id.item_lllegal_content);
            TextView txt_status = ViewHolder.get(convertView, R.id.item_lllegal_status);
            CheckBox cb_candealt = ViewHolder.get(convertView, R.id.item_lllegal_candealt);
            cb_candealt.setTag(position);
            cb_candealt.setOnClickListener(canDealtClick);

            if (bean.getNeedDealt()) {
                cb_candealt.setChecked(true);
            } else {
                cb_candealt.setChecked(false);
            }


            txt_city.setText(bean.getLllegalCity());
            txt_address.setText(bean.getAddress());
            txt_desc.setText(bean.getLllegalDesc());
            txt_time.setText(bean.getLllegalTime());
            txt_point.setText(bean.getPoint());
            txt_fine.setText(bean.getFine());
            txt_servicefee.setText(bean.getServiceFee());
            txt_latefees.setText(bean.getLate_fees());
            txt_content.setText(bean.getContent());
            txt_status.setText(bean.getStatus());


            if (queryResult.getIsOfferPrice()) {
                if (bean.getCanDealt()) {
                    cb_candealt.setVisibility(View.VISIBLE);
                } else {
                    cb_candealt.setVisibility(View.GONE);
                }

                txt_status.setVisibility(View.VISIBLE);
            } else {
                cb_candealt.setChecked(false);
                cb_candealt.setVisibility(View.GONE);
                txt_servicefee.setText("0");
                txt_latefees.setText("0");
                txt_content.setText("");
                txt_status.setVisibility(View.GONE);
            }

            return convertView;
        }

        View.OnClickListener canDealtClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = Integer.parseInt(v.getTag().toString());
                LllegalPriceRecordBean bean = lllegalPriceRecord.get(position);
                CheckBox cb = (CheckBox) v;
                if (cb.isChecked()) {
                    bean.setNeedDealt(true);
                } else {
                    bean.setNeedDealt(false);
                }
            }
        };

    }
}
