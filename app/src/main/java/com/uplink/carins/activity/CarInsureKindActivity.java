package com.uplink.carins.activity;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

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
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.uplink.carins.Own.AppCacheManager;
import com.uplink.carins.R;
import com.uplink.carins.model.api.CarInsKindBean;
import com.uplink.carins.model.api.CarInsPlanBean;
import com.uplink.carins.model.api.CarInsPlanKindParentBean;
import com.uplink.carins.ui.dialog.CustomChooseListDialog;
import com.uplink.carins.ui.dialog.CustomEditTextDialog;
import com.uplink.carins.ui.SlideSwitch;
import com.uplink.carins.ui.ViewHolder;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.AnimationUtil;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CarInsureKindActivity extends SwipeBackActivity implements View.OnClickListener {

    String TAG = "CarInsureKindActivity";


    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;

    private LayoutInflater inflater;

    private RadioGroup form_carinsurekind_rb_insurekind;

    private ListView form_carinsurekind_list;
    private Button btn_submit_carinsurekind;


    private List<CarInsPlanBean> carInsPlans;// 投保套餐

    private KindParentAdapter kindParentAdapter;
    private KindChildAdapter kindChildAdapter;

    private Map<Integer, List<CarInsKindBean>> carInsPlanKindMap = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carinsurekind);
        initView();
        initEvent();


        setCarInsPlan(AppCacheManager.getCarInsPlan());

    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("车险方案详情");

        inflater = LayoutInflater.from(CarInsureKindActivity.this);


        form_carinsurekind_rb_insurekind = (RadioGroup) findViewById(R.id.form_carinsurekind_rb_insurekind);
        form_carinsurekind_list = (ListView) findViewById(R.id.form_carinsurekind_list);
        btn_submit_carinsurekind = (Button) findViewById(R.id.btn_submit_carinsurekind);

    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);
        btn_submit_carinsurekind.setOnClickListener(this);
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

            LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(270,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);

            for (int i = 0; i < carInsPlans.size(); i++) {

                CarInsPlanBean item = carInsPlans.get(i);

                RadioButton rb = (RadioButton) inflater.inflate(R.layout.tab1_item, null);

                rb.setLayoutParams(layoutparams);
                rb.setText(item.getName() + "");
                rb.setTag(i);

                int rd_Id = rd_Id_Prefix + i;
                LogUtil.i("rd_Id:" + rd_Id);
                rb.setId(rd_Id);

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

        for (int i=0;i<carInsKinds.size();i++) {

            if (carInsKinds.get(i).getId() == carInsKind.getId()) {
                carInsKinds.set(i,carInsKind);
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
                kindParentAdapter = new KindParentAdapter();
                form_carinsurekind_list.setAdapter(kindParentAdapter);
            }

            kindParentAdapter.setData(carInsPlan.getId(), carInsPlan.getKindParent());


            AnimationUtil.SetTab1ImageSlide(group, CarInsureKindActivity.this);

        }
    };


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.btn_submit_carinsurekind:

                Intent intent = new Intent(CarInsureKindActivity.this, CarInsureCompanyActivity.class);
                startActivity(intent);

                break;
        }
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


                    convertView = inflater.inflate(R.layout.item_carinsplankind_content, parent, false);//交强险或车船税视图

                    TextView txt_carinskind_id = ViewHolder.get(convertView, R.id.item_carinskind_id);
                    TextView txt_carinskind_name = ViewHolder.get(convertView, R.id.item_carinskind_name);

                    txt_carinskind_id.setText(carInsKind.getId() + "");
                    txt_carinskind_name.setText(carInsKind.getName() + "");


                    ListView list_carinskind_childs = ViewHolder.get(convertView, R.id.form_carinsurekind_list_content);


                    kindChildAdapter = new KindChildAdapter();
                    list_carinskind_childs.setAdapter(kindChildAdapter);


                    kindChildAdapter.setData(plandId,carInsPlanKindParent.getChild());

                    CommonUtil.setListViewHeightBasedOnChildren(list_carinskind_childs);

                }

            }


            return convertView;
        }
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
//            * id                   int       险种id
//            * name                 string    险种名称
//            * aliasName            string    险种名称别名
//            * canWaiverDeductible  bool      能否选择不计免赔额,当true,显示1个勾选框可选
//            * type                 int       类型，1：交强险或车船税；2：商业险；3：附加险；
//            * inputType            string    文本输入类型，1：无；2：文本，3：下拉选择
//            * inputUnit            string    文本单位，当inputType为1为空
//            * inputValue           string    文本默认值和下拉值，当inputType为1为空
//            * isHasDetails         string    是否有投保明细
//            *
//            * APP加存数据：
//            * value                string    投保的值
//            * details              string    投保明细
//            * isWaiverDeductible   bool      是否选择不计免赔 false/true
//            * isCheck              bool      是否选择当前险种


            if (carInsKindIds != null) {

                CarInsKindBean carInsKind = getCarInsPlanKind(this.plandId,carInsKindIds.get(position));
                if (carInsKind != null) {

                    //carInsKind=carInsPlanKindMap.get(carInsKind.getId());

                    int id = carInsKind.getId();//险种id
                    String name = carInsKind.getName();//险种名称
                    String aliasName = carInsKind.getAliasName();//险种名称别名
                    boolean canWaiverDeductible = carInsKind.getCanWaiverDeductible();//能否选择不计免赔额,当true,显示1个勾选框可选
                    boolean isWaiverDeductible = carInsKind.getIsWaiverDeductible();//是否不计免赔额
                    int type = carInsKind.getType();//类型，1：交强险或车船税；2：商业险；3：附加险；
                    int inputType = carInsKind.getInputType();//文本输入类型，1：无；2：文本，3：下拉选择
                    String inputUnit = carInsKind.getInputUnit();//文本单位，当inputType为1为空
                    CarInsKindBean.InputValueBean inputValue = carInsKind.getInputValue();//文本默认值和下拉值，当inputType为1为空
                    boolean isHasDetails = carInsKind.getIsHasDetails();//是否有投保明细
                    boolean isCheck = carInsKind.getIsCheck();//是否选择当前险种


                    String value = carInsKind.getValue();//投保的值

                    convertView = inflater.inflate(R.layout.item_carinsplankind_content_list_content, parent, false);


                    TextView txt_id = ViewHolder.get(convertView, R.id.item_carinskind_id);
                    txt_id.setText(id + "");

                    TextView txt_name = ViewHolder.get(convertView, R.id.item_carinskind_name);
                    txt_name.setEnabled(isCheck);
                    txt_name.setText(name);

                    CheckBox cb_isWaiverDeductible = ViewHolder.get(convertView, R.id.item_carinskind_canWaiverDeductible);
                    cb_isWaiverDeductible.setOnCheckedChangeListener(myCheckxCanWaiverDeductibleChangeListener);
                    cb_isWaiverDeductible.setTag(id);
                    cb_isWaiverDeductible.setEnabled(isCheck);

                    SlideSwitch ch_isCheck = ViewHolder.get(convertView, R.id.item_carinskind_isCheck);
                    ch_isCheck.setSlideCheckListener(mySlideIsCheckListener);
                    ch_isCheck.setTag(id);
                    ch_isCheck.setState(isCheck);

                    TextView txt_input = ViewHolder.get(convertView, R.id.item_carinskind_input);
                    txt_input.setEnabled(isCheck);

                    TextView txt_inputunit = ViewHolder.get(convertView, R.id.item_carinskind_inputunit);
                    txt_inputunit.setEnabled(isCheck);

                    TextView txt_details = ViewHolder.get(convertView, R.id.item_carinskind_details);
                    txt_details.setEnabled(isCheck);



                    //是否可以选择不计免赔
                    if (canWaiverDeductible) {
                        cb_isWaiverDeductible.setVisibility(View.VISIBLE);
                        cb_isWaiverDeductible.setChecked(isWaiverDeductible);
                    } else {
                        cb_isWaiverDeductible.setVisibility(View.GONE);
                    }

                    //文本单位
                    if (StringUtil.isEmpty(inputUnit)) {
                        txt_inputunit.setVisibility(View.GONE);
                    } else {
                        txt_inputunit.setVisibility(View.VISIBLE);
                        //txt_inputunit.setText(inputUnit);
                    }

                    //文本类型
                    switch (inputType) {
                        case 1:
                            txt_input.setVisibility(View.VISIBLE);
                            txt_input.setOnClickListener(editEditClickListener);
                            break;
                        case 2:
                            txt_input.setVisibility(View.VISIBLE);
                            txt_input.setOnClickListener(editEditClickListener);

                            break;
                        case 3:
                            txt_input.setVisibility(View.VISIBLE);

                            Drawable drawable = getResources().getDrawable(R.drawable.selector_arrow_down);
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//对图片进行压缩
                            txt_input.setCompoundDrawables(null, null, drawable, null);

                            txt_input.setTag(inputValue);//默认值和值列表放入Tag
                            txt_input.setOnClickListener(chooseListClickListener);

                            break;
                        default:
                            txt_input.setVisibility(View.GONE);
                            break;
                    }

                    if (inputValue != null) {
                        //LogUtil.i("默认值：" + inputValue.getDefaultVal());

                        if (inputValue.getDefaultVal() != null) {
                            txt_input.setText(inputValue.getDefaultVal() + "");
                        }
                    }

                    if (isHasDetails) {
                        txt_details.setVisibility(View.VISIBLE);
                    } else {
                        txt_details.setVisibility(View.GONE);
                    }


                }

            }


            return convertView;
        }

        private SlideSwitch.SlideCheckListener mySlideIsCheckListener = new SlideSwitch.SlideCheckListener() {
            @Override
            public void check(View v, boolean ifCheck) {
                int kindId =Integer.parseInt(v.getTag().toString());
                CarInsKindBean carInsKind = getCarInsPlanKind(plandId,kindId);
                carInsKind.setIsCheck(ifCheck);
                setCarInsPlanKind(plandId,carInsKind);

                //((View)v.getParent()

            }
        };

        private CheckBox.OnCheckedChangeListener myCheckxCanWaiverDeductibleChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                int kindId =Integer.parseInt(v.getTag().toString());
                CarInsKindBean carInsKind = getCarInsPlanKind(plandId,kindId);
                carInsKind.setIsWaiverDeductible(isChecked);
                setCarInsPlanKind(plandId,carInsKind);
            }
        };
    }


    private CustomEditTextDialog dialog_EditText;
    private TextView.OnClickListener editEditClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            LogUtil.i("点击");
            String title = "金额输入";
            if (dialog_EditText == null) {

                dialog_EditText = new CustomEditTextDialog(CarInsureKindActivity.this, title);


                dialog_EditText.getBtnSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        String val = dialog_EditText.getTxtEdit().getText() + "";
                        dialog_EditText.getTriggerView().setText(val);

                        dialog_EditText.dismiss();

                    }
                });

                dialog_EditText.getBtnCancle().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        dialog_EditText.dismiss();
                    }
                });

            }

            dialog_EditText.setTriggerView((TextView) v);

            String val = dialog_EditText.getTriggerView().getText() + "";
            dialog_EditText.getTxtEdit().setText(val);

            dialog_EditText.show();

        }
    };

    private CustomChooseListDialog dialog_ChooseList;
    private TextView.OnClickListener chooseListClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            String title = "请选择";
            if (dialog_ChooseList == null) {

                dialog_ChooseList = new CustomChooseListDialog(CarInsureKindActivity.this, title);


                dialog_ChooseList.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        TextView txt_item = ViewHolder.get(view, R.id.dialog_chooselist_item);
                        String val = txt_item.getText() + "";

                        dialog_ChooseList.getTriggerView().setText(val);

                        dialog_ChooseList.dismiss();


                    }
                });

            }


            CarInsKindBean.InputValueBean inputValue = (CarInsKindBean.InputValueBean) v.getTag();


            TextView txt = (TextView) v;
            LogUtil.i("默认值1:" + inputValue.getDefaultVal());
            LogUtil.i("默认值2:" + txt.getText());

            List<String> items = inputValue.getValue();

            dialog_ChooseList.setTriggerView((TextView) v);
            dialog_ChooseList.getAdapter().setData(items, txt.getText() + "");

            dialog_ChooseList.show();

        }
    };


}
