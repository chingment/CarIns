package com.uplink.carins.activity;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.uplink.carins.R;
import com.uplink.carins.activity.adapter.NwItemParentFieldAdapter;
import com.uplink.carins.ui.city.CitycodeUtil;
import com.uplink.carins.ui.city.ScrollerNumberPicker;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.NoDoubleClickUtils;

public class MallOrderConfirmActivity extends SwipeBackActivity implements View.OnClickListener {

    String TAG = "MallOrderConfirmActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;
    private Button btn_submit;

    private EditText txt_receiptaddress_consignee;
    private EditText txt_receiptaddress_mobile;
    private EditText txt_receiptaddress_address;
    private TextView sel_area;
    private ListView list_skus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall_order_confirm);

        initView();
        initEvent();
        initData();
    }

    private void initView() {
        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("确认订单");

        btn_submit = (Button) findViewById(R.id.btn_submit);
        txt_receiptaddress_consignee = (EditText) findViewById(R.id.txt_receiptaddress_consignee);
        txt_receiptaddress_mobile = (EditText) findViewById(R.id.txt_receiptaddress_mobile);
        txt_receiptaddress_address = (EditText) findViewById(R.id.txt_receiptaddress_address);
        list_skus = (ListView) findViewById(R.id.list_item_parent);
        sel_area = (TextView) findViewById(R.id.sel_area);
    }

    private void initData() {

//        txt_receiptaddress_consignee.setText(insureInfo.getReceiptAddress().getConsignee());
//        txt_receiptaddress_mobile.setText(insureInfo.getReceiptAddress().getMobile());
//        txt_receiptaddress_address.setText(insureInfo.getReceiptAddress().getAddress());
//
//        NwItemParentFieldAdapter adapter = new NwItemParentFieldAdapter(NwCarInsInsureResultActivity.this, insureInfo.getInfoItems());
//
//        list_item_parent.setAdapter(adapter);

    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        sel_area.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.btn_submit:
                if (!NoDoubleClickUtils.isDoubleClick()) {
                    submit();
                }
                break;
            case R.id.sel_area:

                AlertDialog.Builder builder = new AlertDialog.Builder(MallOrderConfirmActivity.this);
                View view = LayoutInflater.from(MallOrderConfirmActivity.this).inflate(R.layout.dialog_address, null);
                builder.setView(view);
                LinearLayout addressdialog_linearlayout = (LinearLayout) view.findViewById(R.id.addressdialog_linearlayout);
                final ScrollerNumberPicker provincePicker = (ScrollerNumberPicker) view.findViewById(R.id.province);
                final ScrollerNumberPicker cityPicker = (ScrollerNumberPicker) view.findViewById(R.id.city);
                final ScrollerNumberPicker counyPicker = (ScrollerNumberPicker) view.findViewById(R.id.couny);
                final AlertDialog dialog = builder.show();
                addressdialog_linearlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        sel_area.setText(provincePicker.getSelectedText() + cityPicker.getSelectedText() + counyPicker.getSelectedText());

                        String code = CitycodeUtil.getSingleton().getCouny_list_code().get(counyPicker.getSelected());
                        sel_area.setTag(code);
                        Log.i("kkkk", provincePicker.getSelectedText() + cityPicker.getSelectedText() + counyPicker.getSelectedText());
                        dialog.dismiss();

                    }
                });

                break;
        }
    }

    private void submit() {

    }
}
