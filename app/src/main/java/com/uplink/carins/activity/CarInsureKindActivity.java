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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.uplink.carins.R;
import com.uplink.carins.model.api.CarInsPlanBean;
import com.uplink.carins.model.api.CarInsPlanKindChildBean;
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
import java.util.List;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carinsurekind);
        initView();
        initEvent();

        setTestData();
        setInsurePlan();

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
        if (hasFocus){
            CommonUtil.setRadioGroupCheckedByStringTag(form_carinsurekind_rb_insurekind, String.valueOf(0));
        }
    }

    private void setInsurePlan() {

        if (carInsPlans != null && carInsPlans.size() > 0) {


            int rd_Id_Prefix = (int) (Math.random() * 1000);

            LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(270,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);

            for (int i = 0; i < carInsPlans.size(); i++) {

                CarInsPlanBean item = carInsPlans.get(i);

                RadioButton rb = (RadioButton)inflater.inflate(R.layout.tab1_item, null);

                rb.setLayoutParams(layoutparams);
                rb.setText(item.getName() + "");
                rb.setTag(i);
                rb.setId(rd_Id_Prefix + i);

                form_carinsurekind_rb_insurekind.addView(rb);
            }

            form_carinsurekind_rb_insurekind.setOnCheckedChangeListener(rb_insurekind_CheckedChangeListener);
        }
    }

    private void setTestData() {

        carInsPlans = new ArrayList<CarInsPlanBean>();

        List<CarInsPlanKindParentBean> carInsPlanKindParents = new ArrayList<>();

        List<CarInsPlanKindParentBean> carInsPlanKindParents2 = new ArrayList<>();

        List<CarInsPlanKindChildBean> carInsPlanKindChilds = new ArrayList<>();

        CarInsPlanKindChildBean.InputValueBean inputValueBean = new CarInsPlanKindChildBean.InputValueBean();
        inputValueBean.setDefaultVal("国产");


        CarInsPlanKindChildBean.InputValueBean inputValueBean2 = new CarInsPlanKindChildBean.InputValueBean();
        inputValueBean2.setDefaultVal("100W");


        List<String> s = new ArrayList<>();
        s.add("国产");
        s.add("进口");
        inputValueBean.setValue(s);

        List<String> s1 = new ArrayList<>();
        s1.add("50W");
        s1.add("100W");
        s1.add("200W");
        inputValueBean2.setValue(s1);

        //    public CarInsPlanKindChildBean(int id, String name, String aliasName, String value, boolean isCheck, boolean canWaiverDeductible, int type, int inputType, String inputUnit, InputValueBean inputValue, boolean isHasDetails) {
        carInsPlanKindChilds.add(new CarInsPlanKindChildBean(1, "交强险", "交强险", "", true, true, 1, 1, "元", inputValueBean, false));
        carInsPlanKindChilds.add(new CarInsPlanKindChildBean(2, "车船税", "车船税", "", true, true, 1, 2, "", inputValueBean, false));
        carInsPlanKindChilds.add(new CarInsPlanKindChildBean(3, "第三者", "第三者", "", true, true, 2, 3, "", inputValueBean2, false));
        carInsPlanKindChilds.add(new CarInsPlanKindChildBean(4, "盗抢险", "盗抢险", "", true, true, 2, 1, "", null, false));
        carInsPlanKindChilds.add(new CarInsPlanKindChildBean(5, "玻璃单独破碎险", "玻璃单独破碎险", "", true, false, 2, 3, "", inputValueBean, false));


        carInsPlanKindChilds.add(new CarInsPlanKindChildBean(4, "新增加设备损失险", "新增加设备损失险", "", true, false, 2, 1, "元", null, true));


        List<CarInsPlanKindChildBean> carInsPlanKindChilds2 = new ArrayList<>();


        carInsPlanKindChilds2.add(new CarInsPlanKindChildBean(1, "交强险", "交强险", "", true, false, 1, 1, "", null, false));


        carInsPlanKindParents.add(new CarInsPlanKindParentBean(1, "交强险", null));
        carInsPlanKindParents.add(new CarInsPlanKindParentBean(2, "车船税（买交强且未完税者必买）", carInsPlanKindChilds));
        carInsPlanKindParents.add(new CarInsPlanKindParentBean(3, "商业险", carInsPlanKindChilds2));
        carInsPlanKindParents.add(new CarInsPlanKindParentBean(4, "附加险", carInsPlanKindChilds2));


        carInsPlanKindParents2.add(new CarInsPlanKindParentBean(3, "商业险", carInsPlanKindChilds2));
        carInsPlanKindParents2.add(new CarInsPlanKindParentBean(4, "附加险", carInsPlanKindChilds2));

        carInsPlans.add(new CarInsPlanBean(1, "基础套餐a", "", carInsPlanKindParents));
        carInsPlans.add(new CarInsPlanBean(2, "基础套餐b", "", carInsPlanKindParents));
        carInsPlans.add(new CarInsPlanBean(3, "基础套餐c", "", carInsPlanKindParents2));
        carInsPlans.add(new CarInsPlanBean(4, "基础套餐d", "", carInsPlanKindParents2));

    }

    // 单选框监听
    RadioGroup.OnCheckedChangeListener rb_insurekind_CheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            RadioButton currentCheckedRadio = (RadioButton) findViewById(group.getCheckedRadioButtonId());

            int tabCurrentSelectPisition =(int)currentCheckedRadio.getTag();


            CarInsPlanBean carInsPlan = carInsPlans.get(tabCurrentSelectPisition);

            if (kindParentAdapter == null) {
                kindParentAdapter = new KindParentAdapter();
                form_carinsurekind_list.setAdapter(kindParentAdapter);
            }

            kindParentAdapter.setData(carInsPlan.getKindParent());


            AnimationUtil.SetTab1ImageSlide(group);

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
        private List<CarInsPlanKindParentBean> carInsPlanKindParents;//车险父类别

        public void setData(List<CarInsPlanKindParentBean> carInsPlanKindParents) {

            if (carInsPlanKindParents != null) {
                this.carInsPlanKindParents = carInsPlanKindParents;
            }

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

                    int id = carInsPlanKindParent.getId();//父类别名称
                    String name = carInsPlanKindParent.getName();//父类别名称

                    convertView = inflater.inflate(R.layout.item_carinsplankind_content, parent, false);//交强险或车船税视图

                    LinearLayout view_big = ViewHolder.get(convertView, R.id.item_carinsplankind_view_big);
                    RelativeLayout view_small = ViewHolder.get(convertView, R.id.item_carinsplankind_view_small);
                    TextView txt_bigtitle = ViewHolder.get(convertView, R.id.item_carinsplankind_txt_bigtitle);

                    txt_bigtitle.setText(name);

                    if (id == 1 || id == 2) {
                        // view_small.setVisibility(View.GONE);
                    }

                    ListView list_childs = ViewHolder.get(convertView, R.id.form_carinsurekind_list_content);


                    kindChildAdapter = new KindChildAdapter();
                    list_childs.setAdapter(kindChildAdapter);


                    kindChildAdapter.setData(carInsPlanKindParent.getChild());

                    CommonUtil.setListViewHeightBasedOnChildren(list_childs);

                    list_childs.setVisibility(View.GONE);


                }

            }


            return convertView;
        }
    }


   private class KindChildAdapter extends BaseAdapter {
        protected static final String TAG = "KindChildAdapter";
        private List<CarInsPlanKindChildBean> carInsPlanKindChilds;//车险父类别


        public void setData(List<CarInsPlanKindChildBean> carInsPlanKindChilds) {

            if (carInsPlanKindChilds != null) {
                this.carInsPlanKindChilds = carInsPlanKindChilds;
            }

            notifyDataSetChanged();
        }


       KindChildAdapter() {

            this.carInsPlanKindChilds = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return carInsPlanKindChilds.size();
        }

        @Override
        public Object getItem(int position) {
            return carInsPlanKindChilds.get(position);
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


            if (carInsPlanKindChilds != null) {

                CarInsPlanKindChildBean carInsPlanKindChild = carInsPlanKindChilds.get(position);
                if (carInsPlanKindChild != null) {

                    int id = carInsPlanKindChild.getId();//险种id
                    String name = carInsPlanKindChild.getName();//险种名称
                    String aliasName = carInsPlanKindChild.getAliasName();//险种名称别名
                    boolean canWaiverDeductible = carInsPlanKindChild.getCanWaiverDeductible();//能否选择不计免赔额,当true,显示1个勾选框可选
                    int type = carInsPlanKindChild.getType();//类型，1：交强险或车船税；2：商业险；3：附加险；
                    int inputType = carInsPlanKindChild.getInputType();//文本输入类型，1：无；2：文本，3：下拉选择
                    String inputUnit = carInsPlanKindChild.getInputUnit();//文本单位，当inputType为1为空
                    CarInsPlanKindChildBean.InputValueBean inputValue = carInsPlanKindChild.getInputValue();//文本默认值和下拉值，当inputType为1为空
                    boolean isHasDetails = carInsPlanKindChild.getIsHasDetails();//是否有投保明细
                    boolean isCheck = carInsPlanKindChild.getIsCheck();//是否选择当前险种
                    String value = carInsPlanKindChild.getValue();//投保的值

                    convertView = inflater.inflate(R.layout.item_carinsplankind_content_list_content, parent, false);


                    TextView txt_id = ViewHolder.get(convertView, R.id.item_carinsplankind_txt_id);
                    TextView txt_name = ViewHolder.get(convertView, R.id.item_carinsplankind_txt_name);
                    CheckBox cb_canWaiverDeductible = ViewHolder.get(convertView, R.id.item_carinsplankind_cb_canWaiverDeductible);
                    SlideSwitch ch_isCheck = ViewHolder.get(convertView, R.id.item_carinsplankind_ch_isCheck);
                    TextView txt_input = ViewHolder.get(convertView, R.id.item_carinsplankind_txt_input);
                    TextView txt_inputunit = ViewHolder.get(convertView, R.id.item_carinsplankind_txt_inputunit);
                    TextView txt_details = ViewHolder.get(convertView, R.id.item_carinsplankind_txt_details);

                    txt_id.setText(id + "");
                    txt_name.setText(name);

                    //是否可以选择不计免赔
                    if (canWaiverDeductible) {
                        cb_canWaiverDeductible.setVisibility(View.VISIBLE);
                    } else {
                        cb_canWaiverDeductible.setVisibility(View.GONE);
                    }

                    //文本单位
                    if (StringUtil.isEmpty(inputUnit)) {
                        txt_inputunit.setVisibility(View.GONE);
                    } else {
                        txt_inputunit.setVisibility(View.VISIBLE);
                        txt_inputunit.setText(inputUnit);
                    }

                    //文本类型
                    switch (inputType) {
                        case 1:
                            txt_input.setVisibility(View.VISIBLE);
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
                        LogUtil.i("默认值：" + inputValue.getDefaultVal());

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
    }


    private CustomEditTextDialog dialog_EditText;
    private TextView.OnClickListener editEditClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


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


            CarInsPlanKindChildBean.InputValueBean inputValue = (CarInsPlanKindChildBean.InputValueBean) v.getTag();


            TextView txt=(TextView)v;
            LogUtil.i("默认值1:" + inputValue.getDefaultVal());
            LogUtil.i("默认值2:" + txt.getText());

            List<String> items = inputValue.getValue();

            dialog_ChooseList.setTriggerView((TextView) v);
            dialog_ChooseList.getAdapter().setData(items,txt.getText()+"");

            dialog_ChooseList.show();

        }
    };


}
