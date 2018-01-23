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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.uplink.carins.Own.AppCacheManager;
import com.uplink.carins.Own.AppManager;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.http.HttpClient;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.model.api.TalentDemandWorkJobBean;
import com.uplink.carins.ui.ViewHolder;
import com.uplink.carins.ui.dialog.CustomConfirmDialog;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;


public class TalentDemandActivity extends SwipeBackActivity implements View.OnClickListener{

    String TAG = "TalentDemandActivity";

    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;

    private LinearLayout layout_talentdemand_sel_workjob;
    private TextView form_talentdemand_txt_workjob;
    private EditText form_talentdemand_txt_quantity;
    private Button btn_submit_talentdemand;


    private PopupWindow popupTalentdemandWorkJobsWindow;
    private View popupTalentdemandWorkJobsView;
    private ListView popupTalentdemandWorkJobsListView;
    private List<TalentDemandWorkJobBean> talentDemandWorkJobs;

    private LayoutInflater inflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talentdemand);

        initView();
        initEvent();


        inflater = LayoutInflater.from(TalentDemandActivity.this);

        talentDemandWorkJobs = AppCacheManager.getTalentDemandWorkJob();

        if(talentDemandWorkJobs!=null) {
            TalentDemandWorkJobsAdapter popupCompanysListView_Adapter = new TalentDemandWorkJobsAdapter(talentDemandWorkJobs);
            popupTalentdemandWorkJobsListView.setAdapter(popupCompanysListView_Adapter);
        }


    }

    private class TalentDemandWorkJobsAdapter extends BaseAdapter {

        private List<TalentDemandWorkJobBean> talentDemandWorkJobs;

        TalentDemandWorkJobsAdapter(List<TalentDemandWorkJobBean> carInsCompanys) {
            this.talentDemandWorkJobs = carInsCompanys;
        }

        @Override
        public int getCount() {
            return talentDemandWorkJobs.size();
        }

        @Override
        public Object getItem(int position) {

            return talentDemandWorkJobs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if(convertView==null) {
                convertView = inflater.inflate(R.layout.item_company, null);
            }
            TextView item_tv = ViewHolder.get(convertView, R.id.item_company);
            TalentDemandWorkJobBean bean = talentDemandWorkJobs.get(position);
            item_tv.setText(bean.getName() + "");
            return convertView;
        }

    }


    private void showPopuTalentDemandWorkJobs(View v) {

        popupTalentdemandWorkJobsWindow.setFocusable(true);
        popupTalentdemandWorkJobsWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupTalentdemandWorkJobsWindow.setBackgroundDrawable(new BitmapDrawable());
        popupTalentdemandWorkJobsWindow.showAsDropDown(v);
        popupTalentdemandWorkJobsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                TalentDemandWorkJobBean bean = talentDemandWorkJobs.get(position);
                form_talentdemand_txt_workjob.setText(bean.getName() + "");
                form_talentdemand_txt_workjob.setTag(bean.getId() + "");
                if (popupTalentdemandWorkJobsWindow != null)
                    popupTalentdemandWorkJobsWindow.dismiss();
            }
        });
    }


    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("填写人才需求");


        layout_talentdemand_sel_workjob = (LinearLayout) findViewById(R.id.layout_talentdemand_sel_workjob);
        form_talentdemand_txt_workjob = (TextView) findViewById(R.id.form_talentdemand_txt_workjob);
        form_talentdemand_txt_quantity = (EditText) findViewById(R.id.form_talentdemand_txt_quantity);

        btn_submit_talentdemand = (Button) findViewById(R.id.btn_submit_talentdemand);

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        popupTalentdemandWorkJobsView = layoutInflater.inflate(R.layout.popu_list, null);
        popupTalentdemandWorkJobsListView = (ListView) popupTalentdemandWorkJobsView.findViewById(R.id.popu_list);
        popupTalentdemandWorkJobsWindow = new PopupWindow(popupTalentdemandWorkJobsView, 680, 500);
    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);

        btn_submit_talentdemand.setOnClickListener(this);
        layout_talentdemand_sel_workjob.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.btn_submit_talentdemand:
                submitTalentDemand();
                break;
            case R.id.layout_talentdemand_sel_workjob:
                showPopuTalentDemandWorkJobs(v);
                break;
        }
    }

    private void submitTalentDemand() {

        String workJob = form_talentdemand_txt_workjob.getTag() + "";
        String quantity = form_talentdemand_txt_quantity.getText() + "";


        if (StringUtil.isEmptyNotNull(workJob)) {
            showToast("请选择工种");
            return;
        }

        if (StringUtil.isEmptyNotNull(quantity)) {
            showToast("请输入人数");
            return;
        }



        Map<String, Object> params = new HashMap<>();
        params.put("userId", this.getAppContext().getUser().getId());
        params.put("merchantId", this.getAppContext().getUser().getMerchantId()+"");
        params.put("posMachineId","1");
        params.put("workJob", workJob);
        params.put("quantity", quantity);


        HttpClient.postWithMy(Config.URL.submitTalentDemand, params,null, new  HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                LogUtil.i(TAG,"onSuccess====>>>" +response);

                ApiResultBean<Object> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<Object>>() {
                });

                showToast(rt.getMessage());

                if (rt.getResult() == Result.SUCCESS) {
                    showSuccessDialog();
                }

                removeProgressDialog();
            }

            @Override
            public void onFailure(Request request, Exception e) {
                super.onFailure(request, e);
                LogUtil.e(TAG,"onFailure====>>>" + e.getMessage());
                removeProgressDialog();
                showToast("提交失败");
            }
        });
    }

    private CustomConfirmDialog dialog_Success;
    private void showSuccessDialog() {
        if (dialog_Success == null) {

            dialog_Success = new CustomConfirmDialog(TalentDemandActivity.this, "提交成功",false);

            dialog_Success.getBtnSure().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog_Success.dismiss();

                    Intent l_Intent = new Intent(TalentDemandActivity.this, OrderListActivity.class);
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