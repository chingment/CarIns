package com.uplink.carins.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import com.uplink.carins.ui.dialog.CustomConfirmDialog;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private List<CarInsKindBean> insKinds;
    private CustomConfirmDialog dialog_ConfirmArtificial;

    private List<NwCarInsCompanyBean> insChannels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nwcarins_company);
        carInfo = (CarInfoResultBean) getIntent().getSerializableExtra("carInfoBean");
        insKinds = (List<CarInsKindBean>) getIntent().getSerializableExtra("insKindsBean");
        initView();
        initEvent();

        getComanyInfo();

    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("报价");
        list_carinscompany = (ListView) findViewById(R.id.list_carinscompany);

    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);

        list_carinscompany.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NwCarInsCompanyBean bean = insChannels.get(position);

                if (bean.getOfferResult() == 1) {
                    Intent intent = new Intent(NwCarInsCompanyActivity.this, NwCarInsCompanyOfferResultActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("dataBean", bean);
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

                LogUtil.i(TAG, "onSuccess====>>>" + response);

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
            TextView item_name = ViewHolder.get(convertView, R.id.item_company_name);
            TextView item_desc = ViewHolder.get(convertView, R.id.item_company_desc);
            TextView item_offerpremium = ViewHolder.get(convertView, R.id.item_company_offerpremium);
            TextView item_offermsg = ViewHolder.get(convertView, R.id.item_company_offermsg);
            TextView item_btnoffer0 = ViewHolder.get(convertView, R.id.item_company_btnoffer0);
            item_btnoffer0.setTag(position);
            TextView item_btnoffer1 = ViewHolder.get(convertView, R.id.item_company_btnoffer1);
            item_btnoffer1.setTag(position);

            LinearLayout item_arrow_right = ViewHolder.get(convertView, R.id.item_layout_arrow_right);
            if (bean.getOfferResult() == 1) {
                item_arrow_right.setVisibility(View.VISIBLE);
                item_offerpremium.setVisibility(View.VISIBLE);
                item_offerpremium.setText(bean.getOfferSumPremium() + "");
                item_btnoffer0.setVisibility(View.GONE);
                item_btnoffer1.setVisibility(View.GONE);
                item_offermsg.setVisibility(View.INVISIBLE);
            } else if (bean.getOfferResult() == 2) {
                item_arrow_right.setVisibility(View.INVISIBLE);
                item_offerpremium.setVisibility(View.GONE);
                item_btnoffer0.setVisibility(View.VISIBLE);
                item_btnoffer1.setVisibility(View.GONE);
                item_offermsg.setVisibility(View.VISIBLE);
            }

            CommonUtil.loadImageFromUrl(NwCarInsCompanyActivity.this, item_img, bean.getImgUrl() + "");

            item_name.setText(bean.getName());
            item_desc.setText(bean.getDescp());

            item_btnoffer0.setOnClickListener(autoOfferClickListener);
            item_btnoffer1.setOnClickListener(autoOfferClickListener);

            return convertView;
        }

        //下拉选择
        private TextView.OnClickListener autoOfferClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = Integer.parseInt(v.getTag().toString());
                NwCarInsCompanyBean bean = carInsCompanys.get(position);
                if (bean.getOfferResult() == 2) {
                    carInfo.setAuto("0");
                    if (dialog_ConfirmArtificial == null) {

                        dialog_ConfirmArtificial = new CustomConfirmDialog(NwCarInsCompanyActivity.this, "提交人工报价需要等候1约0分钟，请注意查看我的订单？", true);

                        dialog_ConfirmArtificial.getBtnSure().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                gefOffer(position);

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
                } else {
                    carInfo.setAuto("1");
                    gefOffer(position);
                }
            }
        };

        private void gefOffer(final int position) {

            Map<String, Object> params = new HashMap<>();

            params.put("userId", getAppContext().getUser().getId());
            params.put("merchantId", getAppContext().getUser().getMerchantId());
            params.put("posMachineId", getAppContext().getUser().getPosMachineId());
            params.put("auto", carInfo.getAuto());
            params.put("carInfoOrderId", carInfo.getCarInfoOrderId());
            params.put("channelId", carInsCompanys.get(position).getPartnerChannelId());
            params.put("companyCode", carInsCompanys.get(position).getPartnerCode());
            params.put("carInfoOrderId", carInfo.getCarInfoOrderId());
            params.put("ciStartDate", "2018-06-20");
            params.put("biStartDate", "2018-06-20");

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


            JSONObject jsonObj_CarInfo = new JSONObject();
            try {
                jsonObj_CarInfo.put("belong", carInfo.getCar().getBelong());
                jsonObj_CarInfo.put("carType", carInfo.getCar().getCarType());
                jsonObj_CarInfo.put("licensePlateNo", carInfo.getCar().getLicensePlateNo());
                jsonObj_CarInfo.put("vin", carInfo.getCar().getVin());
                jsonObj_CarInfo.put("engineNo", carInfo.getCar().getEngineNo());
                jsonObj_CarInfo.put("modelCode", carInfo.getCar().getModelCode());
                jsonObj_CarInfo.put("modelName", carInfo.getCar().getModelName());
                jsonObj_CarInfo.put("firstRegisterDate", carInfo.getCar().getFirstRegisterDate());
                jsonObj_CarInfo.put("displacement", carInfo.getCar().getDisplacement());
                jsonObj_CarInfo.put("marketYear", carInfo.getCar().getMarketYear());
                jsonObj_CarInfo.put("ratedPassengerCapacity", carInfo.getCar().getReplacementValue());
                jsonObj_CarInfo.put("replacementValue", carInfo.getCar().getReplacementValue());
                jsonObj_CarInfo.put("chgownerType", carInfo.getCar().getChgownerType());
                jsonObj_CarInfo.put("chgownerDate", carInfo.getCar().getChgownerDate());
                jsonObj_CarInfo.put("tonnage", carInfo.getCar().getTonnage());
                jsonObj_CarInfo.put("wholeWeight", carInfo.getCar().getWholeWeight());
                jsonObj_CarInfo.put("licensePicKey", carInfo.getCar().getLicensePicKey());
                jsonObj_CarInfo.put("carCertPicKey", carInfo.getCar().getCarCertPicKey());
                jsonObj_CarInfo.put("carInvoicePicKey", carInfo.getCar().getCarInvoicePicKey());
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
            params.put("car", jsonObj_CarInfo);

            postWithMy(Config.URL.carInsInsInquiry, params, null, true, "正在报价中", new HttpResponseHandler() {

                @Override
                public void onSuccess(String response) {
                    super.onSuccess(response);

                    LogUtil.i(TAG, "onSuccess====>>>" + response);
                    ApiResultBean<NwCarInsCompanyBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<NwCarInsCompanyBean>>() {
                    });

                    if (rt.getResult() == Result.SUCCESS) {
                        carInsCompanys.get(position).setOfferResult(1);//1 为自动报价成功，2为自动报价失败
                        carInsCompanys.get(position).setOfferSumPremium(rt.getData().getOfferSumPremium());
                        carInsCompanys.get(position).setOfferInquirys(rt.getData().getOfferInquirys());
                        carInsCompanys.get(position).setOfferId(rt.getData().getOfferId());
                    } else {
                        carInsCompanys.get(position).setOfferResult(2);
                    }

                    carInsCompanys.get(position).setOfferMessage(rt.getMessage());

                    setData(carInsCompanys);
                }

                @Override
                public void onFailure(Request request, Exception e) {
                    super.onFailure(request, e);
                    LogUtil.e(TAG, "onFailure====>>>" + e.getMessage());
                    showToast("提交失败");
                }
            });
        }

    }
}
