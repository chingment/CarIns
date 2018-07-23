package com.uplink.carins.activity;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import com.uplink.carins.model.api.CarInfoResultBean;
import com.uplink.carins.model.api.CarInsKindBean;
import com.uplink.carins.model.api.NwCarInsCompanyBean;
import com.uplink.carins.model.api.NwCarInsCompanyResultBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.ViewHolder;
import com.uplink.carins.ui.dataPicker.CustomDatePicker;
import com.uplink.carins.ui.dialog.CustomConfirmDialog;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Request;

public class NwCarInsCompanyActivity extends SwipeBackActivity implements View.OnClickListener {
    private String TAG = "NwCarInsCompanyActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;

    private ListView list_carinscompany;

    private CarInfoResultBean carInfo;
    private List<CarInsKindBean> insKinds;
    private CustomConfirmDialog dialog_ConfirmArtificial;

    private List<NwCarInsCompanyBean> insChannels;

    private LinearLayout layout_ci;
    private LinearLayout layout_bi;

    private CustomDatePicker datePicker_ci;
    private CustomDatePicker datePicker_bi;


    private TextView txt_date_ci;
    private TextView txt_date_bi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nwcarins_company);
        carInfo = (CarInfoResultBean) getIntent().getSerializableExtra("carInfoBean");
        insKinds = (List<CarInsKindBean>) getIntent().getSerializableExtra("insKindsBean");


        initView();
        initEvent();

        boolean isHasCi = false;
        boolean isHasBi = false;
        for (CarInsKindBean bean : insKinds) {

            if (bean.getIsCheck()) {
                if (bean.getId() == 1) {
                    isHasCi = true;
                } else if (bean.getId() > 3) {
                    isHasBi = true;
                }
            }
        }

        if (isHasCi) {
            layout_ci.setVisibility(View.VISIBLE);
        } else {
            layout_ci.setVisibility(View.GONE);
        }

        if (isHasBi) {
            layout_bi.setVisibility(View.VISIBLE);
        } else {
            layout_bi.setVisibility(View.GONE);
        }

        getComanyInfo();

    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("报价");
        list_carinscompany = (ListView) findViewById(R.id.list_carinscompany);

        layout_ci = (LinearLayout) findViewById(R.id.layout_ci);
        layout_bi = (LinearLayout) findViewById(R.id.layout_bi);

        txt_date_ci = (TextView) findViewById(R.id.txt_date_ci);
        txt_date_bi = (TextView) findViewById(R.id.txt_date_bi);

        for (CarInsKindBean bean :
                insKinds) {

            if (bean.getId() == 1) {
                layout_ci.setVisibility(View.VISIBLE);
            } else if (bean.getId() == 2) {
                layout_ci.setVisibility(View.VISIBLE);
            } else if (bean.getId() == 3) {
                layout_bi.setVisibility(View.VISIBLE);
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        String now = sdf.format(c.getTime());
        datePicker_ci = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                txt_date_ci.setText(time.split(" ")[0]);
            }
        }, now, "2099-12-31 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行

        txt_date_ci.setText(now.split(" ")[0]);

        datePicker_ci.showSpecificTime(false); // 不显示时和分
        datePicker_ci.setIsLoop(false); // 不允许循环滚动

        datePicker_bi = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                txt_date_bi.setText(time.split(" ")[0]);
            }
        }, now, "2099-12-31 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行

        txt_date_bi.setText(now.split(" ")[0]);

        datePicker_bi.showSpecificTime(false); // 不显示时和分
        datePicker_bi.setIsLoop(false); // 不允许循环滚动
    }


    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);
        txt_date_ci.setOnClickListener(this);
        txt_date_bi.setOnClickListener(this);

        list_carinscompany.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NwCarInsCompanyBean bean = insChannels.get(position);

                if (bean.getOfferStatus() == 2) {
                    Intent intent = new Intent(NwCarInsCompanyActivity.this, NwCarInsCompanyOfferResultActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("offerInfo", bean);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            }
        });
    }

    private void getComanyInfo() {

        Map<String, Object> params = new HashMap<>();

        params.put("userId", appContext.getUser().getId());
        params.put("merchantId", appContext.getUser().getMerchantId());
        params.put("posMachineId", appContext.getUser().getPosMachineId());
        params.put("carInfoOrdeId", carInfo.getCarInfoOrderId());
        params.put("areaId", 440100);

        postWithMy(Config.URL.carInsComanyInfo, params, null, true, "正在加载中", new HttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                ApiResultBean<NwCarInsCompanyResultBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<NwCarInsCompanyResultBean>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {
                    insChannels = rt.getData().getCompanys();
                    CarInsCompanyAdapter list_carinscompany_adapter = new CarInsCompanyAdapter();
                    list_carinscompany.setAdapter(list_carinscompany_adapter);
                    list_carinscompany_adapter.setData(insChannels);

                } else {
                    showToast(rt.getMessage());
                }
            }

        });
    }

    @Override
    public void onClick(View v) {

        String date_ci = txt_date_ci.getText().toString();
        String date_bi = txt_date_bi.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.txt_date_ci:
                if (StringUtil.isEmptyNotNull(date_ci)) {
                    date_ci = now;
                }
                datePicker_ci.show(date_ci);
                break;
            case R.id.txt_date_bi:

                if (StringUtil.isEmptyNotNull(date_bi)) {
                    date_bi = now;
                }
                datePicker_bi.show(date_bi);
                break;
        }
    }


    private CustomConfirmDialog dialog_Success;

    private void showSuccessDialog() {
        if (dialog_Success == null) {

            dialog_Success = new CustomConfirmDialog(NwCarInsCompanyActivity.this, "报价订单提交成功", false);

            dialog_Success.getBtnSure().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog_Success.dismiss();

                    Intent l_Intent = new Intent(NwCarInsCompanyActivity.this, OrderListActivity.class);
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

    private class CarInsCompanyAdapter extends BaseAdapter { // shoplist适配器

        private List<NwCarInsCompanyBean> carInsCompanys;

        CarInsCompanyAdapter() {

            this.carInsCompanys = new ArrayList<>();
        }

        public void setData(List<NwCarInsCompanyBean> carInsCompanys) {
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
            NwCarInsCompanyBean bean = carInsCompanys.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(NwCarInsCompanyActivity.this).inflate(R.layout.item_company_insoffer, parent, false);
            }
            ImageView item_img = ViewHolder.get(convertView, R.id.item_company_img);
            Animation m_animation = AnimationUtils.loadAnimation(NwCarInsCompanyActivity.this,
                    R.anim.dialog_load_animation2);
            final ImageView item_loading = ViewHolder.get(convertView, R.id.item_loading_img);

            TextView item_name = ViewHolder.get(convertView, R.id.item_company_name);
            TextView item_pos = ViewHolder.get(convertView, R.id.item_company_pos);
            item_pos.setText(position + "");

            TextView item_desc = ViewHolder.get(convertView, R.id.item_company_desc);
            TextView item_offerpremium = ViewHolder.get(convertView, R.id.item_company_offerpremium);
            final TextView item_offermsg = ViewHolder.get(convertView, R.id.item_company_offermsg);
            TextView item_btnoffer0 = ViewHolder.get(convertView, R.id.item_company_btnoffer0);
            item_btnoffer0.setTag(position);
            final TextView item_btnoffer1 = ViewHolder.get(convertView, R.id.item_company_btnoffer1);
            item_btnoffer1.setTag(position);

            LinearLayout item_arrow_right = ViewHolder.get(convertView, R.id.item_layout_arrow_right);

            switch (bean.getOfferStatus()) {
                case 0://初始
                    item_arrow_right.setVisibility(View.GONE);
                    item_offerpremium.setVisibility(View.GONE);
                    item_offerpremium.setText("");
                    item_btnoffer0.setVisibility(View.GONE);
                    item_btnoffer1.setVisibility(View.VISIBLE);
                    item_offermsg.setVisibility(View.GONE);
                    item_offermsg.setText("");
                    item_loading.setVisibility(View.GONE);
                    item_loading.clearAnimation();

                    break;
                case 1://自动报价中
                    item_arrow_right.setVisibility(View.GONE);
                    item_offerpremium.setVisibility(View.GONE);
                    item_offerpremium.setText("");
                    item_btnoffer0.setVisibility(View.GONE);
                    item_btnoffer1.setVisibility(View.GONE);
                    item_offermsg.setVisibility(View.VISIBLE);
                    item_offermsg.setText("报价中，稍等");
                    item_loading.setVisibility(View.VISIBLE);
                    item_loading.startAnimation(m_animation);

                    break;
                case 2://自动报价成功
                    item_arrow_right.setVisibility(View.VISIBLE);
                    item_offerpremium.setVisibility(View.VISIBLE);
                    item_offerpremium.setText(bean.getOfferSumPremium());
                    item_btnoffer0.setVisibility(View.GONE);
                    item_btnoffer1.setVisibility(View.GONE);
                    item_offermsg.setVisibility(View.GONE);
                    item_offermsg.setText("");
                    item_loading.setVisibility(View.GONE);
                    item_loading.clearAnimation();
                    break;
                case 3://自动报价失败
                    item_arrow_right.setVisibility(View.GONE);
                    item_offerpremium.setVisibility(View.GONE);
                    item_offerpremium.setText("");
                    item_btnoffer0.setVisibility(View.VISIBLE);
                    item_btnoffer1.setVisibility(View.GONE);
                    item_offermsg.setVisibility(View.VISIBLE);
                    item_offermsg.setText(bean.getOfferMessage());
                    item_loading.setVisibility(View.GONE);
                    item_loading.clearAnimation();
                    break;
            }

            CommonUtil.loadImageFromUrl(NwCarInsCompanyActivity.this, item_img, bean.getImgUrl() + "");

            item_name.setText(bean.getName());
            item_desc.setText(bean.getDescp());


            item_btnoffer0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final int pos = Integer.parseInt(v.getTag().toString());


                    if (dialog_ConfirmArtificial == null) {

                        dialog_ConfirmArtificial = new CustomConfirmDialog(NwCarInsCompanyActivity.this, "提交人工报价需要等候约10分钟，请注意查看我的订单？", true);


                        dialog_ConfirmArtificial.getBtnSure().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                gefOffer(pos, 0, true, "正在提交中");

                                dialog_ConfirmArtificial.dismiss();
                            }
                        });

                        dialog_ConfirmArtificial.getBtnCancle().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog_ConfirmArtificial.dismiss();
                            }
                        });
                    }

                    dialog_ConfirmArtificial.show();

                }
            });


            item_btnoffer1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = Integer.parseInt(v.getTag().toString());
                    carInsCompanys.get(pos).setOfferStatus(1);// 自动报价中
                    setData(carInsCompanys);
                    gefOffer(pos, 1, false, "正在报价中");
                }
            });


            return convertView;
        }

        private void gefOffer(final int position, final int auto, final boolean isShowLoading, final String loadingmsg) {


            String data_ci = txt_date_ci.getText().toString();
            String data_bi = txt_date_bi.getText().toString();


            if (StringUtil.isEmptyNotNull(data_ci)) {
                showToast("请选择交强险日期");
                return;
            }

            if (StringUtil.isEmptyNotNull(data_bi)) {
                showToast("请选择商业险日期");
                return;
            }

            Map<String, Object> params = new HashMap<>();

            params.put("userId", getAppContext().getUser().getId());
            params.put("merchantId", getAppContext().getUser().getMerchantId());
            params.put("posMachineId", getAppContext().getUser().getPosMachineId());
            params.put("auto", auto + "");
            params.put("carInfoOrderId", carInfo.getCarInfoOrderId());
            params.put("channelId", carInsCompanys.get(position).getPartnerChannelId());
            params.put("companyCode", carInsCompanys.get(position).getPartnerCode());
            params.put("carInfoOrderId", carInfo.getCarInfoOrderId());
            params.put("ciStartDate", txt_date_ci.getText());
            params.put("biStartDate", txt_date_bi.getText());

            JSONArray json_CarInsKinds = new JSONArray();

            try {
                for (CarInsKindBean carInsKind : insKinds) {
                    if (carInsKind.getIsCheck()) {
                        JSONObject jsonFa1 = new JSONObject();
                        jsonFa1.put("id", carInsKind.getId());
                        jsonFa1.put("value", carInsKind.getInputValue());
                        jsonFa1.put("isWaiverDeductible", carInsKind.getIsWaiverDeductible());
                        json_CarInsKinds.put(jsonFa1);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }

            params.put("insureKind", json_CarInsKinds);


            postWithMy(Config.URL.carInsInsInquiry, params, null, isShowLoading, loadingmsg, new HttpResponseHandler() {

                @Override
                public void onSuccess(String response) {
                    super.onSuccess(response);

                    ApiResultBean<NwCarInsCompanyBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<NwCarInsCompanyBean>>() {
                    });

                    if (auto == 1) {
                        if (rt.getResult() == Result.SUCCESS) {
                            carInsCompanys.get(position).setOfferStatus(2);//2 为自动报价成功
                            carInsCompanys.get(position).setOfferSumPremium(rt.getData().getOfferSumPremium());
                            carInsCompanys.get(position).setOfferInquirys(rt.getData().getOfferInquirys());
                            carInsCompanys.get(position).setOfferId(rt.getData().getOfferId());
                        } else {
                            carInsCompanys.get(position).setOfferStatus(3);//3为自动报价失败
                        }
                    } else {

                        if (rt.getResult() == Result.SUCCESS) {
                            showSuccessDialog();
                        } else {

                            showToast(rt.getMessage());
                        }
                    }

                    carInsCompanys.get(position).setOfferMessage(rt.getMessage());

                    setData(carInsCompanys);
                }

            });
        }

    }
}
