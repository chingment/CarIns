package com.uplink.carins.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.uplink.carins.Own.AppCacheManager;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.CarInfoResultBean;
import com.uplink.carins.model.api.CarInsKindBean;
import com.uplink.carins.model.api.CarInsPlanBean;
import com.uplink.carins.model.api.CarInsPlanKindParentBean;
import com.uplink.carins.model.api.CustomerBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.SlideSwitch;
import com.uplink.carins.ui.ViewHolder;
import com.uplink.carins.ui.dialog.CustomChooseListDialog;
import com.uplink.carins.ui.dialog.CustomEditTextDialog;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.AnimationUtil;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class NwCarInsKindActivity extends SwipeBackActivity implements View.OnClickListener {

    String TAG = "NwCarInsKindActivity";


    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;

    private LayoutInflater inflater;

    private RadioGroup form_carinsurekind_rb_insurekind;

    private ListView form_carinsurekind_list;
    private Button btn_submit;


    private List<CarInsPlanBean> carInsPlans;// 投保套餐

    private NwCarInsKindActivity.KindParentAdapter kindParentAdapter;
    private NwCarInsKindActivity.KindChildAdapter kindChildAdapter;

    private Map<Integer, List<CarInsKindBean>> carInsPlanKindMap = new HashMap();

    private CarInfoResultBean carInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nwcarins_kind);
        initView();
        initEvent();

        carInfo = (CarInfoResultBean) getIntent().getSerializableExtra("dataBean");

        setCarInsPlan(AppCacheManager.getCarInsPlan());

    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("车险方案详情");

        inflater = LayoutInflater.from(NwCarInsKindActivity.this);


        form_carinsurekind_rb_insurekind = (RadioGroup) findViewById(R.id.form_carinsurekind_rb_insurekind);
        form_carinsurekind_list = (ListView) findViewById(R.id.form_carinsurekind_list);
        btn_submit = (Button) findViewById(R.id.btn_submit);

    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {

            String checkedIndex = form_carinsurekind_rb_insurekind.getTag().toString();
            if (checkedIndex.equals("0")) {
                CommonUtil.setRadioGroupCheckedByStringTag(form_carinsurekind_rb_insurekind, String.valueOf(0));
            }
        }
    }

    private void setCarInsPlan(List<CarInsPlanBean> carInsPlans) {

        this.carInsPlans = carInsPlans;

        if (carInsPlans != null && carInsPlans.size() > 0) {


            int rd_Id_Prefix = (int) (Math.random() * 1000);

            DisplayMetrics dm = getResources().getDisplayMetrics();

            int width = dm.widthPixels / carInsPlans.size();

            LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(width,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);

            for (int i = 0; i < carInsPlans.size(); i++) {

                CarInsPlanBean item = carInsPlans.get(i);

                RadioButton rb = (RadioButton) inflater.inflate(R.layout.tab1_item, null);

                rb.setLayoutParams(layoutparams);
                rb.setText(item.getName() + "");
                rb.setTag(i);
                rb.setId((rd_Id_Prefix + i));

                form_carinsurekind_rb_insurekind.addView(rb);


                carInsPlanKindMap.put(item.getId(), AppCacheManager.getCarInsKindByPlanId(item.getId()));

            }

            form_carinsurekind_rb_insurekind.setOnCheckedChangeListener(rb_insurekind_CheckedChangeListener);
        }
    }


    public CarInsKindBean getCarInsPlanKind(int planiId, int kindId) {
        List<CarInsKindBean> carInsKinds = carInsPlanKindMap.get(planiId);

        CarInsKindBean carInsKind = null;
        for (CarInsKindBean bean : carInsKinds) {

            if (bean.getId() == kindId) {
                carInsKind = bean;
                break;
            }
        }

        return carInsKind;
    }

    public void setCarInsPlanKind(int planiId, CarInsKindBean carInsKind) {

        List<CarInsKindBean> carInsKinds = carInsPlanKindMap.get(planiId);

        for (int i = 0; i < carInsKinds.size(); i++) {

            if (carInsKinds.get(i).getId() == carInsKind.getId()) {
                carInsKinds.set(i, carInsKind);
                break;
            }
        }

    }

    // 单选框监听
    RadioGroup.OnCheckedChangeListener rb_insurekind_CheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            RadioButton currentCheckedRadio = (RadioButton) findViewById(group.getCheckedRadioButtonId());

            int tabCurrentSelectPisition = (int) currentCheckedRadio.getTag();


            CarInsPlanBean carInsPlan = carInsPlans.get(tabCurrentSelectPisition);

            if (kindParentAdapter == null) {
                kindParentAdapter = new NwCarInsKindActivity.KindParentAdapter();
                form_carinsurekind_list.setAdapter(kindParentAdapter);
            }

            kindParentAdapter.setData(carInsPlan.getId(), carInsPlan.getKindParent());


            AnimationUtil.SetTab1ImageSlide(group, NwCarInsKindActivity.this);

        }
    };


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.btn_submit:
                submit();
//                Intent intent = new Intent(NwCarInsKindActivity.this, CarInsureCompanyActivity.class);
//                int plandIndex = Integer.parseInt(form_carinsurekind_rb_insurekind.getTag().toString());
//                CarInsPlanBean carInsPlan = carInsPlans.get(plandIndex);
//                List<CarInsKindBean> carInsKinds = carInsPlanKindMap.get(carInsPlan.getId());
//                intent.putExtra("carInsKinds", (Serializable) carInsKinds);
//                startActivity(intent);

                break;
        }
    }


    private void submit() {

        Map<String, Object> params = new HashMap<>();
        params.put("userId", this.getAppContext().getUser().getId());
        params.put("merchantId", this.getAppContext().getUser().getMerchantId());
        params.put("posMachineId", this.getAppContext().getUser().getPosMachineId());
        params.put("carInfoOrderId", carInfo.getCarInfoOrderId());
        params.put("auto", carInfo.getAuto());

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
            jsonObj_CarInfo.put("ratedPassengerCapacity", carInfo.getCar().getRatedPassengerCapacity());
            jsonObj_CarInfo.put("replacementValue", carInfo.getCar().getReplacementValue());
            jsonObj_CarInfo.put("chgownerType", carInfo.getCar().getChgownerType());
            jsonObj_CarInfo.put("chgownerDate", carInfo.getCar().getChgownerDate());
            jsonObj_CarInfo.put("tonnage", carInfo.getCar().getTonnage());
            jsonObj_CarInfo.put("wholeWeight", carInfo.getCar().getWholeWeight());
            jsonObj_CarInfo.put("licensePicKey", carInfo.getCar().getLicensePicKey());
            jsonObj_CarInfo.put("licensePicUrl", carInfo.getCar().getLicensePicUrl());
            jsonObj_CarInfo.put("licenseOtherPicKey", carInfo.getCar().getLicenseOtherPicKey());
            jsonObj_CarInfo.put("licenseOtherPicUrl", carInfo.getCar().getLicenseOtherPicUrl());
            jsonObj_CarInfo.put("carCertPicKey", carInfo.getCar().getCarCertPicKey());
            jsonObj_CarInfo.put("carCertPicUrl", carInfo.getCar().getCarCertPicUrl());
            jsonObj_CarInfo.put("carInvoicePicKey", carInfo.getCar().getCarInvoicePicKey());
            jsonObj_CarInfo.put("carInvoicePicUrl", carInfo.getCar().getCarInvoicePicUrl());
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        params.put("car", jsonObj_CarInfo);

        JSONArray json_Customers = new JSONArray();

        try {
            for (CustomerBean item : carInfo.getCustomers()) {
                JSONObject jsonObj_Customer = new JSONObject();
                jsonObj_Customer.put("insuredFlag", item.getInsuredFlag());
                jsonObj_Customer.put("name", item.getName());
                jsonObj_Customer.put("certNo", item.getCertNo());
                jsonObj_Customer.put("mobile", item.getMobile());
                jsonObj_Customer.put("address", item.getAddress());
                jsonObj_Customer.put("identityFacePicKey", item.getIdentityFacePicKey());
                jsonObj_Customer.put("identityFacePicUrl", item.getIdentityFacePicUrl());
                jsonObj_Customer.put("identityBackPicKey", item.getIdentityBackPicKey());
                jsonObj_Customer.put("identityBackPicUrl", item.getIdentityBackPicUrl());
                jsonObj_Customer.put("orgPicKey", item.getOrgPicKey());
                jsonObj_Customer.put("orgPicUrl", item.getOrgPicUrl());
                json_Customers.put(jsonObj_Customer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }


        params.put("customers", json_Customers);

        postWithMy(Config.URL.carInsEditBaseInfo, params, null, true, "正在提交中", new HttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                ApiResultBean<CarInfoResultBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<CarInfoResultBean>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {

                    Intent intent = new Intent(NwCarInsKindActivity.this, NwCarInsCompanyActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("carInfoBean", rt.getData());

                    int plandIndex = Integer.parseInt(form_carinsurekind_rb_insurekind.getTag().toString());
                    CarInsPlanBean carInsPlan = carInsPlans.get(plandIndex);
                    List<CarInsKindBean> carInsKinds = carInsPlanKindMap.get(carInsPlan.getId());
                    b.putSerializable("insKindsBean", (Serializable) carInsKinds);

                    intent.putExtras(b);
                    startActivity(intent);

                } else {
                    showToast(rt.getMessage());
                }
            }
        });

    }


    private class KindParentAdapter extends BaseAdapter {
        protected static final String TAG = "KindParentAdapter";
        private int plandId = 0;
        private List<CarInsPlanKindParentBean> carInsPlanKindParents;//车险父类别

        public void setData(int plandId, List<CarInsPlanKindParentBean> carInsPlanKindParents) {

            this.plandId = plandId;
            this.carInsPlanKindParents = carInsPlanKindParents;

            notifyDataSetChanged();
        }

        KindParentAdapter() {

            this.carInsPlanKindParents = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return carInsPlanKindParents.size();
        }

        @Override
        public Object getItem(int position) {
            return carInsPlanKindParents.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            if (carInsPlanKindParents != null) {

                CarInsPlanKindParentBean carInsPlanKindParent = carInsPlanKindParents.get(position);
                if (carInsPlanKindParent != null) {

                    CarInsKindBean carInsKind = AppCacheManager.getCarInsKind(carInsPlanKindParent.getId());

                    NwCarInsKindActivity.PlanKind planKind = new NwCarInsKindActivity.PlanKind(this.plandId, carInsKind.getId(), null);

                    CarInsKindBean carInsKind2 = getCarInsPlanKind(planKind.planId, planKind.kindId);

                    convertView = inflater.inflate(R.layout.item_carinsplankind_content, parent, false);//交强险或车船税视图

                    TextView txt_carinskind_id = ViewHolder.get(convertView, R.id.item_carinskind_id);
                    TextView txt_carinskind_name = ViewHolder.get(convertView, R.id.item_carinskind_name);
                    SlideSwitch ch_carinskind_isCheck = ViewHolder.get(convertView, R.id.item_carinskind_isCheck);

                    txt_carinskind_id.setText(carInsKind.getId() + "");
                    txt_carinskind_name.setText(carInsKind.getName() + "");

                    boolean isCheck = carInsKind2.getIsCheck();//是否选择当前险种

                    if (carInsKind.getId() == 1) {
                        ch_carinskind_isCheck.setVisibility(View.VISIBLE);
                        ch_carinskind_isCheck.setSlideCheckListener(mySlideIsCheckListener);
                        ch_carinskind_isCheck.setTag(planKind);
                        ch_carinskind_isCheck.setState(isCheck);
                    }


                    ListView list_carinskind_childs = ViewHolder.get(convertView, R.id.form_carinsurekind_list_content);


                    kindChildAdapter = new NwCarInsKindActivity.KindChildAdapter();
                    list_carinskind_childs.setAdapter(kindChildAdapter);


                    kindChildAdapter.setData(plandId, carInsPlanKindParent.getChild());

                    CommonUtil.setListViewHeightBasedOnChildren(list_carinskind_childs);

                }

            }


            return convertView;
        }

        //投保开关
        private SlideSwitch.SlideCheckListener mySlideIsCheckListener = new SlideSwitch.SlideCheckListener() {
            @Override
            public void check(View v, boolean ifCheck) {

                NwCarInsKindActivity.PlanKind planKind = (NwCarInsKindActivity.PlanKind) v.getTag();
                CarInsKindBean carInsKind = getCarInsPlanKind(planKind.planId, planKind.kindId);
                carInsKind.setIsCheck(ifCheck);
                setCarInsPlanKind(planKind.planId, carInsKind);
                notifyDataSetChanged();
            }
        };
    }


    private class KindChildAdapter extends BaseAdapter {
        protected static final String TAG = "KindChildAdapter";

        private int plandId = 0;
        private List<Integer> carInsKindIds;//车险父类别


        public void setData(int plandId, List<Integer> carInsKindIds) {

            this.plandId = plandId;
            this.carInsKindIds = carInsKindIds;


            notifyDataSetChanged();
        }


        KindChildAdapter() {

            this.carInsKindIds = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return carInsKindIds.size();
        }

        @Override
        public Object getItem(int position) {
            return carInsKindIds.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            convertView = inflater.inflate(R.layout.item_carinsplankind_content_list_content, parent, false);

            if (carInsKindIds != null) {

                CarInsKindBean carInsKind = getCarInsPlanKind(this.plandId, carInsKindIds.get(position));
                if (carInsKind != null) {


                    int id = carInsKind.getId();//险种id
                    String name = carInsKind.getName();//险种名称
                    String aliasName = carInsKind.getAliasName();//险种名称别名
                    boolean canWaiverDeductible = carInsKind.getCanWaiverDeductible();//能否选择不计免赔额,当true,显示1个勾选框可选
                    boolean isWaiverDeductible = carInsKind.getIsWaiverDeductible();//是否不计免赔额
                    int type = carInsKind.getType();//类型，1：交强险或车船税；2：商业险；3：附加险；
                    int inputType = carInsKind.getInputType();//文本输入类型，1：无；2：文本，3：下拉选择
                    String inputUnit = carInsKind.getInputUnit();//文本单位，当inputType为1为空
                    List<String> inputOption = carInsKind.getInputOption();//文本默认值和下拉值，当inputType为1为空
                    String inputValue = carInsKind.getInputValue();//投保的值
                    boolean isHasDetails = carInsKind.getIsHasDetails();//是否有投保明细
                    boolean isCheck = carInsKind.getIsCheck();//是否选择当前险种

                    NwCarInsKindActivity.PlanKind planKind = new NwCarInsKindActivity.PlanKind(this.plandId, carInsKind.getId(), inputOption);

                    //险种Id
                    TextView txt_id = ViewHolder.get(convertView, R.id.item_carinskind_id);
                    txt_id.setText(id + "");

                    //险种名称
                    TextView txt_name = ViewHolder.get(convertView, R.id.item_carinskind_name);
                    txt_name.setEnabled(isCheck);
                    txt_name.setText(name);

                    //是否可以选择不计免赔
                    CheckBox cb_isWaiverDeductible = ViewHolder.get(convertView, R.id.item_carinskind_canWaiverDeductible);
                    cb_isWaiverDeductible.setOnCheckedChangeListener(myCheckCanWaiverDeductibleChangeListener);
                    cb_isWaiverDeductible.setTag(planKind);
                    cb_isWaiverDeductible.setEnabled(isCheck);

                    if (canWaiverDeductible) {
                        cb_isWaiverDeductible.setVisibility(View.VISIBLE);
                        cb_isWaiverDeductible.setChecked(isWaiverDeductible);
                    } else {
                        cb_isWaiverDeductible.setVisibility(View.GONE);
                    }

                    //投保开关
                    SlideSwitch ch_isCheck = ViewHolder.get(convertView, R.id.item_carinskind_isCheck);
                    ch_isCheck.setSlideCheckListener(mySlideIsCheckListener);
                    ch_isCheck.setTag(planKind);
                    ch_isCheck.setState(isCheck);

                    //文本框和类型
                    TextView txt_input = ViewHolder.get(convertView, R.id.item_carinskind_input);
                    txt_input.setTag(planKind);
                    txt_input.setEnabled(isCheck);


                    switch (inputType) {
                        case 1:
                            txt_input.setVisibility(View.GONE);
                            break;
                        case 2:
                            txt_input.setVisibility(View.VISIBLE);
                            txt_input.setOnClickListener(myTextlickListener);
                            break;
                        case 3:
                            txt_input.setVisibility(View.VISIBLE);

                            Drawable drawable = getResources().getDrawable(R.drawable.selector_arrow_down);
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//对图片进行压缩
                            txt_input.setCompoundDrawables(null, null, drawable, null);
                            txt_input.setOnClickListener(myChooseListClickListener);

                            break;
                        default:
                            txt_input.setVisibility(View.GONE);
                            break;
                    }

                    if (inputValue != null) {
                        txt_input.setText(inputValue);
                    }


                    //文本单位
                    TextView txt_inputunit = ViewHolder.get(convertView, R.id.item_carinskind_inputunit);
                    txt_inputunit.setEnabled(isCheck);


                    if (StringUtil.isEmpty(inputUnit)) {
                        txt_inputunit.setVisibility(View.GONE);
                    } else {
                        txt_inputunit.setVisibility(View.VISIBLE);
                        txt_inputunit.setText(inputUnit);
                    }

                    //// TODO: 2018/1/16 暂时不启用投保明细
                    //投保明细
                    TextView txt_details = ViewHolder.get(convertView, R.id.item_carinskind_details);
                    txt_details.setEnabled(isCheck);

                    if (isHasDetails) {
                        txt_details.setVisibility(View.GONE);
                    } else {
                        txt_details.setVisibility(View.GONE);
                    }

                }

            }

            return convertView;
        }

        //文本输入
        private TextView.OnClickListener myTextlickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showEditTextDialog(v, "金额输入", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                NwCarInsKindActivity.PlanKind planKind = (NwCarInsKindActivity.PlanKind) v.getTag();

                                CarInsKindBean carInsKind = getCarInsPlanKind(planKind.planId, planKind.kindId);

                                String val = dialog_EditText.getTxtEdit().getText() + "";

                                carInsKind.setInputValue(val);
                                setCarInsPlanKind(planKind.planId, carInsKind);

                                dialog_EditText.getTriggerView().setText(val);

                                dialog_EditText.dismiss();

                            }
                        },
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                dialog_EditText.dismiss();
                            }
                        }
                );

            }
        };

        //下拉选择
        private TextView.OnClickListener myChooseListClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseListDialog(v, "请选择", new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                        TextView txt_item = ViewHolder.get(v, R.id.dialog_chooselist_item);
                        String val = txt_item.getText() + "";

                        NwCarInsKindActivity.PlanKind planKind = (NwCarInsKindActivity.PlanKind) parent.getTag();

                        CarInsKindBean carInsKind = getCarInsPlanKind(planKind.planId, planKind.kindId);
                        carInsKind.setInputValue(val);
                        setCarInsPlanKind(planKind.planId, carInsKind);

                        dialog_ChooseList.getTriggerView().setText(val);
                        dialog_ChooseList.dismiss();


                    }
                });
            }
        };

        //投保开关
        private SlideSwitch.SlideCheckListener mySlideIsCheckListener = new SlideSwitch.SlideCheckListener() {
            @Override
            public void check(View v, boolean ifCheck) {

                NwCarInsKindActivity.PlanKind planKind = (NwCarInsKindActivity.PlanKind) v.getTag();
                CarInsKindBean carInsKind = getCarInsPlanKind(planKind.planId, planKind.kindId);
                carInsKind.setIsCheck(ifCheck);
                setCarInsPlanKind(planKind.planId, carInsKind);
                notifyDataSetChanged();
            }
        };

        //不计免赔选择
        private CheckBox.OnCheckedChangeListener myCheckCanWaiverDeductibleChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                NwCarInsKindActivity.PlanKind planKind = (NwCarInsKindActivity.PlanKind) v.getTag();
                CarInsKindBean carInsKind = getCarInsPlanKind(planKind.planId, planKind.kindId);
                carInsKind.setIsWaiverDeductible(isChecked);
                setCarInsPlanKind(planKind.planId, carInsKind);
            }
        };
    }

    private CustomEditTextDialog dialog_EditText;

    private void showEditTextDialog(View v, String title, View.OnClickListener sure, View.OnClickListener cancle) {

        if (dialog_EditText == null) {
            dialog_EditText = new CustomEditTextDialog(NwCarInsKindActivity.this, title);
            dialog_EditText.getBtnSure().setOnClickListener(sure);
            dialog_EditText.getBtnCancle().setOnClickListener(cancle);
        }

        dialog_EditText.getBtnSure().setTag(v.getTag());

        dialog_EditText.setTriggerView((TextView) v);
        String val = dialog_EditText.getTriggerView().getText() + "";
        dialog_EditText.getTxtEdit().setText(val);
        dialog_EditText.show();
    }

    private CustomChooseListDialog dialog_ChooseList;

    private void showChooseListDialog(View v, String title, AdapterView.OnItemClickListener sure) {

        if (dialog_ChooseList == null) {

            dialog_ChooseList = new CustomChooseListDialog(NwCarInsKindActivity.this, title);

            dialog_ChooseList.getListView().setOnItemClickListener(sure);

        }

        dialog_ChooseList.getListView().setTag(v.getTag());

        NwCarInsKindActivity.PlanKind planKind = (NwCarInsKindActivity.PlanKind) v.getTag();

        List<String> inputOption = planKind.getInputOption();

        TextView txt = (TextView) v;


        dialog_ChooseList.setTriggerView((TextView) v);
        dialog_ChooseList.getAdapter().setData(inputOption, txt.getText() + "");

        dialog_ChooseList.show();

    }

    private class PlanKind {
        private int kindId;
        private int planId;
        private List<String> inputOption;

        public void setKindId(int kindId) {
            this.kindId = kindId;
        }

        public int getKindId() {
            return kindId;
        }

        public void setPlanId(int planId) {
            this.planId = planId;
        }

        public int getPlanId() {
            return planId;
        }

        public PlanKind() {

        }

        public List<String> getInputOption() {
            return inputOption;
        }

        public void setInputOption(List<String> inputOption) {
            this.inputOption = inputOption;
        }

        public PlanKind(int planId, int kindId, List<String> inputOption) {
            this.planId = planId;
            this.kindId = kindId;
            this.inputOption = inputOption;
        }
    }
}
