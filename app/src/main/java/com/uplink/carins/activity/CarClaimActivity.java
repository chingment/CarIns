package com.uplink.carins.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.uplink.carins.Own.AppCacheManager;
import com.uplink.carins.R;
import com.uplink.carins.model.api.CarInsCompanyBean;
import com.uplink.carins.model.common.NineGridItemBean;
import com.uplink.carins.model.common.NineGridItemType;
import com.uplink.carins.ui.ViewHolder;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

public class CarClaimActivity extends SwipeBackActivity implements View.OnClickListener{

    String TAG = "CarClaimActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;
    private boolean ifOnlyDingsun = false;


    private LinearLayout form_carclaim_select_company;
    private TextView form_carclaim_txt_company;
    private EditText form_carclaim_txt_carplateno;
    private EditText form_carclaim_txt_handperson;
    private EditText form_carclaim_txt_handpersonphone;
    private CheckBox form_carclaim_claimtype1;
    private CheckBox form_carclaim_claimtype2;


    private LayoutInflater inflater;
    private List<CarInsCompanyBean> carInsCompanyCanClaims;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carclaim);
        inflater = LayoutInflater.from(CarClaimActivity.this);
        initView();
        initEvent();

        carInsCompanyCanClaims=AppCacheManager.getCarInsCompanyCanClaims();
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

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        popupCompanysView = layoutInflater.inflate(R.layout.popu_list, null);
        popupCompanysListView = (ListView)popupCompanysView.findViewById(R.id.popu_list);

        popupCompanysWindow = new PopupWindow(popupCompanysView, 672, 500);
    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);
        form_carclaim_select_company.setOnClickListener(this);
        form_carclaim_claimtype1.setOnCheckedChangeListener(form_carclaim_claimtype_CheckListener);
        form_carclaim_claimtype2.setOnCheckedChangeListener(form_carclaim_claimtype_CheckListener);
    }


    CheckBox.OnCheckedChangeListener  form_carclaim_claimtype_CheckListener = new CheckBox.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked)
                switch (buttonView.getId()) {
                    case R.id.form_carclaim_claimtype1:
                        ifOnlyDingsun = false;
                        form_carclaim_claimtype2.setChecked(false);
                        break;
                    case R.id.form_carclaim_claimtype2:
                        ifOnlyDingsun = true;
                        form_carclaim_claimtype1.setChecked(false);
                        break;
                }
        }
    };

    private PopupWindow popupCompanysWindow;
    private View popupCompanysView;
    private ListView popupCompanysListView;
    private void showPopuCompanys(View v) {
//        if (popupCompanysWindow == null) {
//            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//            popupCompanysView = layoutInflater.inflate(R.layout.popu_list, null);
//            popupCompanysListView = (ListView)popupCompanysView.findViewById(R.id.popu_list);
//
//            MyPopuAdapter groupAdapter = new MyPopuAdapter();
//            popupCompanysListView.setAdapter(groupAdapter);
//            popupCompanysWindow = new PopupWindow(popupCompanysView, 672, 500);
//        }
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

        private  List<CarInsCompanyBean> carInsCompanys;

        CarInsCompanysPopuAdapter(List<CarInsCompanyBean> carInsCompanys)
        {
            this.carInsCompanys=carInsCompanys;
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
            case R.id.form_carclaim_select_company:
                showPopuCompanys(v);
                break;
        }
    }
}
