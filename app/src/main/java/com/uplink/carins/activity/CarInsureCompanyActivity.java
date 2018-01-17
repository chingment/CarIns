package com.uplink.carins.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.uplink.carins.Own.AppCacheManager;
import com.uplink.carins.R;
import com.uplink.carins.model.api.CarInsCompanyBean;
import com.uplink.carins.model.api.CarInsKindBean;
import com.uplink.carins.ui.ViewHolder;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.CommonUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CarInsureCompanyActivity extends SwipeBackActivity implements View.OnClickListener {

    private String TAG = "CarInsureCompanyActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;
    private ListView list_carinscompany;
    private CarInsCompanyAdapter list_carinscompany_adapter;
    private Button btn_submit_carinsurecompany;
    private List<CarInsCompanyBean> carInsCompanys = new ArrayList<>();
    private List<String> selecedCarInsCompanyIds=new ArrayList<>();// 记录已选公司I
    private int carInsPlanId=0;
    private List<CarInsKindBean> carInsKinds= new ArrayList<>();
    private int canInsCount = 3;//可投保报价的数量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carinsurecompany);

        initView();
        initEvent();

        carInsCompanys = AppCacheManager.getCarInsCompanyCanInsure();

        carInsPlanId=(int)getIntent().getSerializableExtra("carInsPlanId");
        carInsKinds= (List<CarInsKindBean>)getIntent().getSerializableExtra("carInsKinds");

        //carInsCompanys.add(new CarInsCompanyBean(1, "平安保险","http://res.17fanju.com/webback/content/base/images/bg_login.jpg",true,true));
        //carInsCompanys.add(new CarInsCompanyBean(2,"太平洋保险","http://res.17fanju.com/webback/content/base/images/bg_login.jpg",true,true));
        //carInsCompanys.add(new CarInsCompanyBean(3,"阳光保险","http://res.17fanju.com/webback/content/base/images/bg_login.jpg",true,true));

        list_carinscompany_adapter = new CarInsCompanyAdapter();
        list_carinscompany.setAdapter(list_carinscompany_adapter);
        list_carinscompany_adapter.setData(carInsCompanys);

    }


    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        TextView txt_carinsurecounttips = (TextView) findViewById(R.id.txt_carinsurecounttips);
        list_carinscompany= (ListView) findViewById(R.id.list_carinscompany);
        btn_submit_carinsurecompany = (Button) findViewById(R.id.btn_submit_carinsurecompany);
        txtHeaderTitle.setText("选择保险公司");
        txt_carinsurecounttips.setText("最多只能选"+canInsCount+"间保险公司");
    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);

        btn_submit_carinsurecompany.setOnClickListener(this);

        list_carinscompany.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CarInsCompanyBean bean = carInsCompanys.get(position);

                CheckBox item_cb = ViewHolder.get(view, R.id.item_company_choice_cb);

                if(item_cb.isChecked()) {
                    item_cb.setChecked(false);
                    selecedCarInsCompanyIds.remove(bean.getId()+"");
                }
                else {

                    if(selecedCarInsCompanyIds.size()<canInsCount) {
                        item_cb.setChecked(true);
                        selecedCarInsCompanyIds.add(bean.getId() + "");
                    }
                    else {
                        showToast("最多只能选择" + canInsCount + "间保险公司");
                    }
                }
            }
        });

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.btn_submit_carinsurecompany:

                Intent intent = new Intent(CarInsureCompanyActivity.this, CarInsureDocumentActivity.class);

                List<CarInsCompanyBean> selecedCarInsCompanys = new ArrayList<>();

                for (CarInsCompanyBean compBean : carInsCompanys) {
                    if(selecedCarInsCompanyIds.contains(compBean.getId()+"")) {
                        selecedCarInsCompanys.add(compBean);
                    }
                }

                intent.putExtra("carInsPlanId", carInsPlanId);//投保计划Id
                intent.putExtra("carInsKinds", (Serializable)carInsKinds);//投保计划险种
                intent.putExtra("carInsCompanys", (Serializable)selecedCarInsCompanys);//选择所需报价的保险公司

                startActivity(intent);

                break;
        }
    }

    private class CarInsCompanyAdapter extends BaseAdapter { // shoplist适配器

        private  List<CarInsCompanyBean> carInsCompanys;

        CarInsCompanyAdapter() {

            this.carInsCompanys = new ArrayList<>();
        }

        public void setData(List<CarInsCompanyBean> carInsCompanys) {
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
            CarInsCompanyBean carInsCompany = carInsCompanys.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(CarInsureCompanyActivity.this).inflate(R.layout.item_company_choice, parent, false);
            }
            ImageView item_img = ViewHolder.get(convertView, R.id.item_company_choice_img);
            CheckBox item_cb = ViewHolder.get(convertView, R.id.item_company_choice_cb);
            CommonUtil.loadImageFromUrl(CarInsureCompanyActivity.this,item_img,carInsCompany.getImgUrl() + "");
            item_cb.setChecked(false);
            return convertView;
        }
    }
}
