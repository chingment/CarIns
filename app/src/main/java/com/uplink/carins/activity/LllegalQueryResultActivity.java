package com.uplink.carins.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.uplink.carins.R;
import com.uplink.carins.model.api.LllegalPriceRecordBean;
import com.uplink.carins.model.api.LllegalQueryResultBean;
import com.uplink.carins.ui.ViewHolder;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;

import java.util.List;

public class LllegalQueryResultActivity extends SwipeBackActivity implements View.OnClickListener {

    String TAG = "LllegalQueryActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;
    private LayoutInflater inflater;
    private ListView form_lllegalqueryresult_list;

    private LllegalQueryResultBean queryResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lllegalqueryresult);

        initView();
        initEvent();

        queryResult = (LllegalQueryResultBean) getIntent().getSerializableExtra("dataBean");

        if (queryResult != null) {
            if (queryResult.getLllegalPriceRecord() != null) {
                LllegalPriceRecordItemAdapter nineGridItemdapter = new LllegalPriceRecordItemAdapter(queryResult.getLllegalPriceRecord());
                form_lllegalqueryresult_list.setAdapter(nineGridItemdapter);
            }
        }

    }

    private void initView() {

        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("查询结果");

        form_lllegalqueryresult_list = (ListView) findViewById(R.id.form_lllegalqueryresult_list);
    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
        }
    }

    private class LllegalPriceRecordItemAdapter extends BaseAdapter {

        private List<LllegalPriceRecordBean> items;

        LllegalPriceRecordItemAdapter(List<LllegalPriceRecordBean> items) {
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
                convertView = inflater.inflate(R.layout.item_lllegalqueryresult, null);
            }

            LllegalPriceRecordBean bean = items.get(position);

            TextView txt_city = ViewHolder.get(convertView, R.id.item_lllegal_city);
            TextView txt_address = ViewHolder.get(convertView, R.id.item_lllegal_address);
            TextView txt_desc = ViewHolder.get(convertView, R.id.item_lllegal_desc);
            TextView txt_time = ViewHolder.get(convertView, R.id.item_lllegal_time);
            TextView txt_point = ViewHolder.get(convertView, R.id.item_lllegal_point);
            TextView txt_fine = ViewHolder.get(convertView, R.id.item_lllegal_fine);
            TextView txt_servicefee = ViewHolder.get(convertView, R.id.item_lllegal_servicefee);
            TextView txt_latefees = ViewHolder.get(convertView, R.id.item_lllegal_late_fees);
            TextView txt_content = ViewHolder.get(convertView, R.id.item_lllegal_content);


            txt_city.setText(bean.getLllegalCity());
            txt_address.setText(bean.getAddress());
            txt_desc.setText(bean.getLllegalDesc());
            txt_time.setText(bean.getLllegalTime());
            txt_point.setText(bean.getPoint());
            txt_fine.setText(bean.getFine());
            txt_servicefee.setText(bean.getServiceFee());
            txt_latefees.setText(bean.getLate_fees());
            txt_content.setText(bean.getContent());


            return convertView;
        }


    }
}
