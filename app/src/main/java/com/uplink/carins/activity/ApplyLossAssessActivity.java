package com.uplink.carins.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.uplink.carins.model.api.CarInsCompanyBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.ViewHolder;
import com.uplink.carins.ui.dialog.CustomConfirmDialog;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.NoDoubleClickUtils;
import com.uplink.carins.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;


public class ApplyLossAssessActivity extends SwipeBackActivity implements View.OnClickListener {

    String TAG = "ApplyLossAssessActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;

    private LinearLayout form_applylossassess_select_company;
    private TextView form_applylossassess_txt_company;
    private CheckBox form_applylossassess_isagreeservice;
    private Button btn_submit_applylossassess;
    private TextView btn_serviceagreement;

    private LayoutInflater inflater;
    private List<CarInsCompanyBean> carInsCompanyCanApplyLossAssess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applylossassess);
        inflater = LayoutInflater.from(ApplyLossAssessActivity.this);
        initView();
        initEvent();
    }


    public void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("定损点申请");

        form_applylossassess_select_company = (LinearLayout) findViewById(R.id.form_applylossassess_select_company);
        form_applylossassess_txt_company = (TextView) findViewById(R.id.form_applylossassess_txt_company);
        form_applylossassess_isagreeservice = (CheckBox) findViewById(R.id.form_applylossassess_isagreeservice);

        btn_submit_applylossassess = (Button) findViewById(R.id.btn_submit_applylossassess);
        btn_serviceagreement = (TextView) findViewById(R.id.btn_serviceagreement);

        View popupViewForCompanys = inflater.inflate(R.layout.popu_list, null);
        popupListViewForCompanys = (ListView) popupViewForCompanys.findViewById(R.id.popu_list);


        carInsCompanyCanApplyLossAssess = AppCacheManager.getCarInsCompanyCanApplyLossAssess();
        if (carInsCompanyCanApplyLossAssess != null) {
            CarInsCompanysPopuAdapter popupCompanysListView_Adapter = new CarInsCompanysPopuAdapter(carInsCompanyCanApplyLossAssess);
            popupListViewForCompanys.setAdapter(popupCompanysListView_Adapter);
        }

        popupWindowForCompanys = new PopupWindow(popupViewForCompanys, getWindowsDisplay().getWidth(), 500);

    }


    public void initEvent() {

        btnHeaderGoBack.setOnClickListener(this);
        form_applylossassess_select_company.setOnClickListener(this);
        btn_submit_applylossassess.setOnClickListener(this);
        btn_serviceagreement.setOnClickListener(this);

        popupListViewForCompanys.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                CarInsCompanyBean bean = carInsCompanyCanApplyLossAssess.get(position);
                form_applylossassess_txt_company.setText(bean.getName() + "");
                form_applylossassess_txt_company.setTag(bean.getId() + "");
                if (popupWindowForCompanys != null)
                    popupWindowForCompanys.dismiss();
            }
        });

    }

    private PopupWindow popupWindowForCompanys;
    private ListView popupListViewForCompanys;

    private void showPopuCompanys(View v) {

        popupWindowForCompanys.setFocusable(true);
        popupWindowForCompanys.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindowForCompanys.setBackgroundDrawable(new BitmapDrawable());
        popupWindowForCompanys.showAsDropDown(v);

    }

    private class CarInsCompanysPopuAdapter extends BaseAdapter {

        private List<CarInsCompanyBean> carInsCompanys;

        CarInsCompanysPopuAdapter(List<CarInsCompanyBean> carInsCompanys) {
            this.carInsCompanys = carInsCompanys;
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
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_company, null);
            }
            TextView item_tv = ViewHolder.get(convertView, R.id.item_company);
            CarInsCompanyBean bean = carInsCompanys.get(position);
            item_tv.setText(bean.getName() + "");
            return convertView;
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.btn_submit_applylossassess:
                if (!NoDoubleClickUtils.isDoubleClick()) {
                    submitApplyLossAccess();
                }
                break;
            case R.id.form_applylossassess_select_company:
                showPopuCompanys(v);
                break;
            case R.id.btn_serviceagreement:
                Intent intent = new Intent(ApplyLossAssessActivity.this, WebViewActivity.class);
                intent.putExtra("title", "服务协议");
                intent.putExtra("url", Config.URL.dingshundianshenqingxieyi);
                startActivity(intent);
                break;

        }
    }

    public void submitApplyLossAccess() {


        String insuranceCompanyId = form_applylossassess_txt_company.getTag() + "";

        if (StringUtil.isEmptyNotNull(insuranceCompanyId)) {
            showToast("请选择保险公司");
            return;
        }

        if (!form_applylossassess_isagreeservice.isChecked()) {
            showToast("必须同意服务协议，请认真阅读");
            return;
        }


        Map<String, Object> params = new HashMap<>();
        params.put("userId", this.getAppContext().getUser().getId());
        params.put("merchantId", this.getAppContext().getUser().getMerchantId() + "");
        params.put("posMachineId", this.getAppContext().getUser().getPosMachineId() + "");
        params.put("insuranceCompanyId", insuranceCompanyId);

        postWithMy(Config.URL.submitApplyLossAssess, params, null,true,"正在提交中", new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                LogUtil.i(TAG, "onSuccess====>>>" + response);

                ApiResultBean<Object> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<Object>>() {
                });

                showToast(rt.getMessage());

                if (rt.getResult() == Result.SUCCESS) {
                    showSuccessDialog();
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

    private CustomConfirmDialog dialog_Success;

    private void showSuccessDialog() {
        if (dialog_Success == null) {

            dialog_Success = new CustomConfirmDialog(ApplyLossAssessActivity.this, "提交成功", false);

            dialog_Success.getBtnSure().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog_Success.dismiss();

                    Intent l_Intent = new Intent(ApplyLossAssessActivity.this, OrderListActivity.class);
                    l_Intent.putExtra("status", 1);
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
}
