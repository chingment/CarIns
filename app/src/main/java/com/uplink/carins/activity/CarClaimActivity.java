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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.uplink.carins.Own.AppManager;
import com.uplink.carins.Own.Config;
import com.uplink.carins.http.HttpClient;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.ui.dialog.CustomConfirmDialog;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.StringUtil;
import com.uplink.carins.Own.AppCacheManager;
import com.uplink.carins.R;
import com.uplink.carins.model.api.CarInsCompanyBean;
import com.uplink.carins.model.common.NineGridItemBean;
import com.uplink.carins.model.common.NineGridItemType;
import com.uplink.carins.ui.ViewHolder;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class CarClaimActivity extends SwipeBackActivity implements View.OnClickListener {

    String TAG = "CarClaimActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;

    private int repairsType=1;

    private LinearLayout form_carclaim_select_company;
    private TextView form_carclaim_txt_company;
    private EditText form_carclaim_txt_carplateno;
    private EditText form_carclaim_txt_handperson;
    private EditText form_carclaim_txt_handpersonphone;
    private CheckBox form_carclaim_claimtype1;
    private CheckBox form_carclaim_claimtype2;
    private Button btn_submit_carclaims;

    private LayoutInflater inflater;
    private List<CarInsCompanyBean> carInsCompanyCanClaims;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carclaim);
        inflater = LayoutInflater.from(CarClaimActivity.this);
        initView();
        initEvent();

        carInsCompanyCanClaims = AppCacheManager.getCarInsCompanyCanClaims();
        CarInsCompanysPopuAdapter popupCompanysListView_Adapter = new CarInsCompanysPopuAdapter(carInsCompanyCanClaims);
        popupCompanysListView.setAdapter(popupCompanysListView_Adapter);

    }

    private void initView() {

        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("填写理赔需求");

        form_carclaim_select_company = (LinearLayout) findViewById(R.id.form_carclaim_select_company);
        form_carclaim_txt_company = (TextView) findViewById(R.id.form_carclaim_txt_company);
        form_carclaim_txt_carplateno = (EditText) findViewById(R.id.form_carclaim_txt_carplateno);
        form_carclaim_txt_handperson = (EditText) findViewById(R.id.form_carclaim_txt_handperson);
        form_carclaim_txt_handpersonphone = (EditText) findViewById(R.id.form_carclaim_txt_handpersonphone);
        form_carclaim_claimtype1 = (CheckBox) findViewById(R.id.form_carclaim_claimtype1);
        form_carclaim_claimtype2 = (CheckBox) findViewById(R.id.form_carclaim_claimtype2);
        btn_submit_carclaims=(Button) findViewById(R.id.btn_submit_carclaims);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        popupCompanysView = layoutInflater.inflate(R.layout.popu_list, null);
        popupCompanysListView = (ListView) popupCompanysView.findViewById(R.id.popu_list);

        popupCompanysWindow = new PopupWindow(popupCompanysView, 672, 500);
    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);
        form_carclaim_select_company.setOnClickListener(this);
        btn_submit_carclaims.setOnClickListener(this);
        form_carclaim_claimtype1.setOnCheckedChangeListener(form_carclaim_claimtype_CheckListener);
        form_carclaim_claimtype2.setOnCheckedChangeListener(form_carclaim_claimtype_CheckListener);
    }


    CheckBox.OnCheckedChangeListener form_carclaim_claimtype_CheckListener = new CheckBox.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked)
                switch (buttonView.getId()) {
                    case R.id.form_carclaim_claimtype1:
                        form_carclaim_claimtype2.setChecked(false);
                        repairsType=1;
                        break;
                    case R.id.form_carclaim_claimtype2:
                        form_carclaim_claimtype1.setChecked(false);
                        repairsType=2;
                        break;
                }
        }
    };

    private PopupWindow popupCompanysWindow;
    private View popupCompanysView;
    private ListView popupCompanysListView;

    private void showPopuCompanys(View v) {

        popupCompanysWindow.setFocusable(true);
        popupCompanysWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupCompanysWindow.setBackgroundDrawable(new BitmapDrawable());
        popupCompanysWindow.showAsDropDown(v);
        popupCompanysListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                CarInsCompanyBean bean = carInsCompanyCanClaims.get(position);
                form_carclaim_txt_company.setText(bean.getName() + "");
                form_carclaim_txt_company.setTag(bean.getId() + "");
                if (popupCompanysWindow != null)
                    popupCompanysWindow.dismiss();
            }
        });
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
            convertView = inflater.inflate(R.layout.item_company, null);
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
            case R.id.btn_submit_carclaims:
                submitClaims();
                break;
            case R.id.form_carclaim_select_company:
                showPopuCompanys(v);
                break;
        }
    }

    private void submitClaims() {

        String insuranceCompanyId = form_carclaim_txt_company.getTag() + "";
        String carLicenseNumber = form_carclaim_txt_carplateno.getText() + "";
        String handPerson = form_carclaim_txt_handperson.getText() + "";
        String handPersonPhone = form_carclaim_txt_handpersonphone.getText() + "";

        if (StringUtil.isEmptyNotNull(insuranceCompanyId)) {
            showToast("请选择保险公司");
            return;
        }

        if (StringUtil.isEmptyNotNull(carLicenseNumber)) {
            showToast("请输入车牌号");
            return;
        }

        if (StringUtil.isEmptyNotNull(handPerson)) {
            showToast("请输入对接人");
            return;
        }

        if (StringUtil.isEmptyNotNull(handPersonPhone)) {
            showToast("请输入对接人电话号码");
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("userId", "1027");
        params.put("insuranceCompanyId", insuranceCompanyId);
        params.put("carLicenseNumber", carLicenseNumber);
        params.put("handPerson", handPerson);
        params.put("handPersonPhone", handPersonPhone);
        params.put("posMachineId","1");
        params.put("merchantId", "20");
        params.put("repairsType", repairsType);

        HttpClient.postWithMy(Config.URL.submitClaim, params,null, new CallBack());
    }

    class CallBack extends HttpResponseHandler {
        @Override
        public void onSuccess(String response) {
            super.onSuccess(response);
            LogUtil.e(">>onSuccess>>>>" + response);

            ApiResultBean<Object> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<Object>>() {
            });

            if (rt.getResult() == 1) {
                showSuccessDialog();
            } else {
                showToast("提交理赔失败！");
            }
            removeProgressDialog();
        }

        @Override
        public void onFailure(Request request, Exception e) {
            super.onFailure(request, e);
            removeProgressDialog();
            showToast("提交理赔失败！");
        }
    }

    private CustomConfirmDialog dialog_Success;
    private void showSuccessDialog() {
        if (dialog_Success == null) {

            dialog_Success = new CustomConfirmDialog(CarClaimActivity.this, "理赔订单提交成功",false);

            dialog_Success.getBtnSure().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog_Success.dismiss();

                    Intent l_Intent = new Intent(CarClaimActivity.this, OrderListActivity.class);
                    l_Intent.putExtra("status", 1);
                    startActivity(l_Intent);
                    AppManager.getAppManager().finishAllActivity();


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
