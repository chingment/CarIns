package com.uplink.carins.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.activity.adapter.AcountBaseInfoHandler;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.AcountBaseInfoResultBean;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.CarTypeBean;
import com.uplink.carins.model.api.LllegalQueryLogBean;
import com.uplink.carins.model.api.LllegalQueryResultBean;
import com.uplink.carins.model.api.OrderInfoBean;
import com.uplink.carins.model.api.OrderType;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.ui.ViewHolder;
import com.uplink.carins.ui.my.MyGridView;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.CarKeyboardUtil;
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

    private LinearLayout form_lllegalquery_cb_isofferprice;
    private CheckBox form_lllegalquery_cb_isofferprice1;
    private CheckBox form_lllegalquery_cb_isofferprice2;

    private EditText form_lllegalquery_txt_rackno;
    private EditText form_lllegalquery_txt_enginno;
    private TextView txt_queryscore;
    private Button btn_submit_query;
    private TextView btn_recharge;

    private TextView txt_main_header_right;
    private CarKeyboardUtil keyboardUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lllegalquery);
        initView();
        initEvent();
        getAccountBaseInfo();
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

        txt_main_header_right = (TextView) findViewById(R.id.txt_main_header_right);

        gridview_querylog = (MyGridView) findViewById(R.id.gridview_querylog);
        form_lllegalquery_txt_carno = (EditText) findViewById(R.id.form_lllegalquery_txt_carno);
        keyboardUtil = new CarKeyboardUtil(this, form_lllegalquery_txt_carno);
        sel_lllegalquery_cartype = (LinearLayout) findViewById(R.id.sel_lllegalquery_cartype);
        form_lllegalquery_txt_cartype = (TextView) findViewById(R.id.form_lllegalquery_txt_cartype);
        form_lllegalquery_cb_isCompany = (LinearLayout) findViewById(R.id.form_lllegalquery_cb_isCompany);
        form_lllegalquery_cb_isCompany1 = (CheckBox) findViewById(R.id.form_lllegalquery_cb_isCompany1);
        form_lllegalquery_cb_isCompany2 = (CheckBox) findViewById(R.id.form_lllegalquery_cb_isCompany2);

        form_lllegalquery_cb_isofferprice = (LinearLayout) findViewById(R.id.form_lllegalquery_cb_isofferprice);
        form_lllegalquery_cb_isofferprice1 = (CheckBox) findViewById(R.id.form_lllegalquery_cb_isofferprice1);
        form_lllegalquery_cb_isofferprice2 = (CheckBox) findViewById(R.id.form_lllegalquery_cb_isofferprice2);

        form_lllegalquery_txt_rackno = (EditText) findViewById(R.id.form_lllegalquery_txt_rackno);
        form_lllegalquery_txt_enginno = (EditText) findViewById(R.id.form_lllegalquery_txt_enginno);
        btn_submit_query = (Button) findViewById(R.id.btn_submit_query);

        inflater = LayoutInflater.from(this);

        txt_queryscore = (TextView) findViewById(R.id.txt_queryscore);

        btn_recharge = (TextView) findViewById(R.id.btn_recharge);

        View popupViewForCarTypes = inflater.inflate(R.layout.popu_list, null);
        popupListViewForCarTypes = (ListView) popupViewForCarTypes.findViewById(R.id.popu_list);


        carTypes = new ArrayList<>();
        carTypes.add(new CarTypeBean("02", "小型汽车"));
        carTypes.add(new CarTypeBean("16", "教练汽车"));
        carTypes.add(new CarTypeBean("51", "大型新能源汽车"));
        carTypes.add(new CarTypeBean("52", "小型新能源汽车"));
        carTypes.add(new CarTypeBean("01A1", "A1大型客车"));
        carTypes.add(new CarTypeBean("01A2", "A2牵引货车"));
        carTypes.add(new CarTypeBean("01B1", "B1中型客车"));
        carTypes.add(new CarTypeBean("01B2", "B2大型货车"));

        CarTypesPopuAdapter popupCarTypesListView_Adapter = new CarTypesPopuAdapter(carTypes);
        popupListViewForCarTypes.setAdapter(popupCarTypesListView_Adapter);


        popupWindowForCarTypes = new PopupWindow(popupViewForCarTypes, getWindowsDisplay().getWidth(), 500);
    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);
        txt_main_header_right.setOnClickListener(this);
        btn_recharge.setOnClickListener(this);

        sel_lllegalquery_cartype.setOnClickListener(this);

        form_lllegalquery_cb_isCompany1.setOnCheckedChangeListener(form_lllegalquery_cb_isCompany_CheckListener);
        form_lllegalquery_cb_isCompany2.setOnCheckedChangeListener(form_lllegalquery_cb_isCompany_CheckListener);

        form_lllegalquery_cb_isofferprice1.setOnCheckedChangeListener(form_lllegalquery_cb_isofferprice_CheckListener);
        form_lllegalquery_cb_isofferprice2.setOnCheckedChangeListener(form_lllegalquery_cb_isofferprice_CheckListener);

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

        form_lllegalquery_txt_carno.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {

                switch (view.getId()) {
                    case R.id.form_lllegalquery_txt_carno:
                        keyboardUtil.hideSystemKeyBroad();
                        keyboardUtil.hideSoftInputMethod();
                        if (!keyboardUtil.isShow())
                            keyboardUtil.showKeyboard();
                        break;
                    default:
                        if (keyboardUtil.isShow())
                            keyboardUtil.hideKeyboard();
                        break;
                }

                return false;
            }
        });

        form_lllegalquery_txt_carno.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
// 此处为得到焦点时的处理内容
                } else {
                    if (keyboardUtil.isShow())
                        keyboardUtil.hideKeyboard();
                }
            }
        });

    }


    CheckBox.OnCheckedChangeListener form_lllegalquery_cb_isCompany_CheckListener = new CheckBox.OnCheckedChangeListener() {
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

    CheckBox.OnCheckedChangeListener form_lllegalquery_cb_isofferprice_CheckListener = new CheckBox.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked)
                switch (buttonView.getId()) {
                    case R.id.form_lllegalquery_cb_isofferprice1:
                        form_lllegalquery_cb_isofferprice2.setChecked(false);
                        form_lllegalquery_cb_isofferprice.setTag("true");
                        break;
                    case R.id.form_lllegalquery_cb_isofferprice2:
                        form_lllegalquery_cb_isofferprice1.setChecked(false);
                        form_lllegalquery_cb_isofferprice.setTag("fasle");
                        break;
                }
        }
    };


    public void getQuerylog() {


        Map<String, String> params = new HashMap<>();

        params.put("userId", getAppContext().getUser().getId() + "");
        params.put("merchantId", getAppContext().getUser().getMerchantId() + "");
        params.put("posMachineId", getAppContext().getUser().getPosMachineId() + "");

        getWithMy(Config.URL.lllegalQueryLog, params, false, "", new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                ApiResultBean<List<LllegalQueryLogBean>> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<List<LllegalQueryLogBean>>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {
                    LllegalQueryLogItemAdapter nineGridItemdapter = new LllegalQueryLogItemAdapter(rt.getData());

                    gridview_querylog.setAdapter(nineGridItemdapter);
                }

            }
        });

    }

    @Override
    public void onClick(View v) {

        if (!NoDoubleClickUtils.isDoubleClick()) {
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
                case R.id.txt_main_header_right:
                    Intent intent = new Intent(this, OrderListActivity.class);
                    intent.putExtra("status", 0);//默认状态为 全部
                    intent.putExtra("productType", OrderType.LllegalDealt);//默认产品类型为 违章处理
                    startActivity(intent);
                    break;
            }
        }
    }


    private void submitQuery() {

        String carno = form_lllegalquery_txt_carno.getText() + "";
        String cartype = form_lllegalquery_txt_cartype.getTag() + "";
        String isCompany = form_lllegalquery_cb_isCompany.getTag() + "";
        String rackno = form_lllegalquery_txt_rackno.getText() + "";
        String enginno = form_lllegalquery_txt_enginno.getText() + "";
        String isOfferPrice = form_lllegalquery_cb_isofferprice.getTag() + "";

        //carno="粤E58H56";
        //rackno="085173";
        //enginno="713477";

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
        params.put("isOfferPrice", isOfferPrice);

        postWithMy(Config.URL.lllegalQuery, params, null, true, "正在提交中", new HttpResponseHandler() {
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
        params.put("score", "50");

        postWithMy(Config.URL.submitLllegalQueryScoreRecharge, params, null, true, "正在提交中", new HttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);

                ApiResultBean<OrderInfoBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<OrderInfoBean>>() {
                });

                if (rt.getResult() == Result.SUCCESS) {

                    Intent intent = new Intent(LllegalQueryActivity.this, PayConfirmActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("dataBean", rt.getData());
                    intent.putExtras(b);

                    startActivityForResult(intent, 1);
                } else {
                    showToast(rt.getMessage());
                }

            }
        });
    }

    private void getAccountBaseInfo() {
        getAcountBaseInfo(new AcountBaseInfoHandler() {
            @Override
            public void handler(AcountBaseInfoResultBean bean) {
                txt_queryscore.setText(bean.getLllegalQueryScore() + "");
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (keyboardUtil.isShow()) {
            keyboardUtil.hideKeyboard();
        }
        return super.onTouchEvent(event);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyboardUtil.isShow()) {
                keyboardUtil.hideKeyboard();
            } else {
                finish();
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getAccountBaseInfo();
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
