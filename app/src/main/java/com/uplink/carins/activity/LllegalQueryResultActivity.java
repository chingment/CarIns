package com.uplink.carins.activity;

import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.uplink.carins.R;
import com.uplink.carins.model.api.LllegalPriceRecordBean;
import com.uplink.carins.model.api.LllegalQueryResultBean;
import com.uplink.carins.ui.ViewHolder;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class LllegalQueryResultActivity extends SwipeBackActivity implements View.OnClickListener {

    String TAG = "LllegalQueryActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;
    private LayoutInflater inflater;
    private ListView form_lllegalqueryresult_list;

    private LllegalQueryResultBean queryResult;

    private TextView txt_lllegal_carno;
    private TextView txt_lllegal_sumcount;
    private TextView txt_lllegal_sumpoint;
    private TextView txt_lllegal_sumfine;
    private Button btn_submit;

    private List<LllegalPriceRecordBean> lllegalPriceRecord = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lllegalqueryresult);

        initView();
        initEvent();

        queryResult = (LllegalQueryResultBean) getIntent().getSerializableExtra("dataBean");

        if (queryResult != null) {

            txt_lllegal_carno.setText(queryResult.getCarNo());
            txt_lllegal_sumcount.setText(queryResult.getSumCount());
            txt_lllegal_sumpoint.setText(queryResult.getSumPoint());
            txt_lllegal_sumfine.setText(queryResult.getSumFine());

            if (queryResult.getRecord() != null) {
                lllegalPriceRecord = queryResult.getRecord();
                LllegalPriceRecordItemAdapter nineGridItemdapter = new LllegalPriceRecordItemAdapter();
                form_lllegalqueryresult_list.setAdapter(nineGridItemdapter);
            }
        }

    }

    private void initView() {

        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("查询结果");

        inflater = LayoutInflater.from(this);
        btn_submit= (Button) findViewById(R.id.btn_submit);
        txt_lllegal_carno = (TextView) findViewById(R.id.txt_lllegal_carno);
        txt_lllegal_sumcount = (TextView) findViewById(R.id.txt_lllegal_sumcount);
        txt_lllegal_sumpoint = (TextView) findViewById(R.id.txt_lllegal_sumpoint);
        txt_lllegal_sumfine = (TextView) findViewById(R.id.txt_lllegal_sumfine);

        form_lllegalqueryresult_list = (ListView) findViewById(R.id.form_lllegalqueryresult_list);
    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.btn_submit:
                submit();
                break;
        }
    }

    private  void  submit() {

        int dealtCount=0;
        for(LllegalPriceRecordBean item : lllegalPriceRecord)
        {
            if(item.getNeedDealt())
            {
                dealtCount+=1;
            }
        }

        if (dealtCount==0) {
            showToast("请选择要处理的违章");
            return;
        }

    }

    private class LllegalPriceRecordItemAdapter extends BaseAdapter {


        LllegalPriceRecordItemAdapter() {

        }

        @Override
        public int getCount() {
            return lllegalPriceRecord.size();
        }

        @Override
        public Object getItem(int position) {

            return lllegalPriceRecord.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_lllegalqueryresult, null);
            }

            LllegalPriceRecordBean bean = lllegalPriceRecord.get(position);

            TextView txt_city = ViewHolder.get(convertView, R.id.item_lllegal_city);
            TextView txt_address = ViewHolder.get(convertView, R.id.item_lllegal_address);
            TextView txt_desc = ViewHolder.get(convertView, R.id.item_lllegal_desc);
            TextView txt_time = ViewHolder.get(convertView, R.id.item_lllegal_time);
            TextView txt_point = ViewHolder.get(convertView, R.id.item_lllegal_point);
            TextView txt_fine = ViewHolder.get(convertView, R.id.item_lllegal_fine);
            TextView txt_servicefee = ViewHolder.get(convertView, R.id.item_lllegal_servicefee);
            TextView txt_latefees = ViewHolder.get(convertView, R.id.item_lllegal_late_fees);
            TextView txt_content = ViewHolder.get(convertView, R.id.item_lllegal_content);
            TextView txt_status = ViewHolder.get(convertView, R.id.item_lllegal_status);
            CheckBox cb_candealt = ViewHolder.get(convertView, R.id.item_lllegal_candealt);
            cb_candealt.setTag(position);
            cb_candealt.setOnClickListener(canDealtClick);

            txt_city.setText(bean.getLllegalCity());
            txt_address.setText(bean.getAddress());
            txt_desc.setText(bean.getLllegalDesc());
            txt_time.setText(bean.getLllegalTime());
            txt_point.setText(bean.getPoint());
            txt_fine.setText(bean.getFine());
            txt_servicefee.setText(bean.getServiceFee());
            txt_latefees.setText(bean.getLate_fees());
            txt_content.setText(bean.getContent());
            txt_status.setText(bean.getStatus());

            if (bean.getCanDealt()) {
                cb_candealt.setVisibility(View.VISIBLE);
            }

            return convertView;
        }

        View.OnClickListener canDealtClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = Integer.parseInt(v.getTag().toString());
                LllegalPriceRecordBean bean = lllegalPriceRecord.get(position);
                CheckBox cb = (CheckBox) v;
                if (cb.isChecked()) {
                    bean.setNeedDealt(true);
                } else {
                    bean.setNeedDealt(false);
                }
            }
        };

    }
}
