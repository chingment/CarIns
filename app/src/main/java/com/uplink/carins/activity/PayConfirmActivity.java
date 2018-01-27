package com.uplink.carins.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uplink.carins.R;
import com.uplink.carins.model.api.ConfirmFieldBean;
import com.uplink.carins.model.api.OrderInfoBean;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class PayConfirmActivity extends SwipeBackActivity implements View.OnClickListener {

    private String TAG = "PayConfirmActivity";

    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;

    private LayoutInflater inflater;

    private LinearLayout layout_paymethod;

    Button btn_submit_gopay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payconfirm);


        initView();
        initEvent();

        LinearLayout list_confirmfields = (LinearLayout) this.findViewById(R.id.list_confirmfields);
        inflater = LayoutInflater.from(PayConfirmActivity.this);

        OrderInfoBean orderInfo = (OrderInfoBean) getIntent().getSerializableExtra("dataBean");

        List<ConfirmFieldBean> confirmFields = orderInfo.getConfirmField();


        if (confirmFields != null && confirmFields.size() > 0)
            for (int i = 0; i < confirmFields.size(); i++) {
                ConfirmFieldBean confirmField = confirmFields.get(i);
                View view = inflater.inflate(R.layout.item_confirmfield, null);
                TextView item_name = (TextView) view.findViewById(R.id.item_field);
                TextView item_value = (TextView) view.findViewById(R.id.item_value);
                item_name.setText(confirmField.getField() + "");
                item_value.setText(confirmField.getValue() + "");
                list_confirmfields.addView(view);
            }


    }

    public void initView() {

        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("支付信息");

        layout_paymethod = (LinearLayout) findViewById(R.id.layout_paymethod);

        btn_submit_gopay= (Button) findViewById(R.id.btn_submit_gopay);
    }

    public void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);
        btn_submit_gopay.setOnClickListener(this);

        int layout_paymethod_count = layout_paymethod.getChildCount();

        for (int i = 0; i < layout_paymethod_count; i++) {
            View view = layout_paymethod.getChildAt(i);
            if (view instanceof RelativeLayout) {

                view.setOnClickListener(myPayMethodClick);
            }
        }
    }


    View.OnClickListener myPayMethodClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            int layout_paymethod_count = layout_paymethod.getChildCount();

            for (int i = 0; i < layout_paymethod_count; i++) {
                View view = layout_paymethod.getChildAt(i);
                if (view instanceof RelativeLayout) {
                    RelativeLayout layout_paymethod_item = ((RelativeLayout) view);
                    int layout_paymethod_child_count = layout_paymethod.getChildCount();
                    // view.get
                    for (int j = 0; j <layout_paymethod_child_count;j++) {
                        View view1 = layout_paymethod_item.getChildAt(j);
                        if (view1 instanceof CheckBox) {
                            CheckBox cb = (CheckBox) view1;
                            if(v.equals(layout_paymethod_item))
                            {
                                cb.setChecked(true);
                            }
                            else {
                                cb.setChecked(false);
                            }
                        }
                    }
                }
            }

            LogUtil.i("点击");
        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.btn_submit_gopay:

                Intent intent = new Intent(PayConfirmActivity.this,PayQrcodeActivity.class);
                startActivity(intent);

                break;
        }
    }
}
