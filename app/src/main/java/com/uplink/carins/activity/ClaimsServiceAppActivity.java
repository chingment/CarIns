package com.uplink.carins.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.uplink.carins.Own.AppContext;
import com.uplink.carins.Own.AppManager;
import com.uplink.carins.R;
import com.uplink.carins.model.common.NineGridItemBean;
import com.uplink.carins.model.common.NineGridItemType;
import com.uplink.carins.ui.dialog.CustomConfirmDialog;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.NoDoubleClickUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClaimsServiceAppActivity extends SwipeBackActivity implements View.OnClickListener {

    String TAG = "ClaimsServiceAppActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claimsserviceapp);

        initView();
        initEvent();
    }

    private void initView() {

        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("理赔服务");


        GridView gridview = (GridView) findViewById(R.id.gridview_ninegrid);


        final List<NineGridItemBean> gridviewitems = new ArrayList<NineGridItemBean>();

        NineGridItemBean gridviewitem1 = new NineGridItemBean();
        gridviewitems.add(new NineGridItemBean("一键理赔", NineGridItemType.Window, "com.uplink.carins.activity.CarClaimActivity", R.drawable.ic_app_yjlp));
        gridviewitems.add(new NineGridItemBean("定损点申请", NineGridItemType.Window, "com.uplink.carins.activity.ApplyLossAssessActivity", R.drawable.ic_app_yjtb));

        int length = gridviewitems.size();

        //生成动态数组，并且转入数据
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < gridviewitems.size(); i++) {

            NineGridItemBean gridviewitem = gridviewitems.get(i);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", gridviewitem.getIcon());//添加图像资源的ID
            map.put("ItemText", gridviewitem.getTitle());//按序号做ItemText
            lstImageItem.add(map);
        }
        //生成适配器的ImageItem 与动态数组的元素相对应
        SimpleAdapter saImageItems = new SimpleAdapter(this,
                lstImageItem,//数据来源
                R.layout.item_ninegrid,//item的XML实现

                //动态数组与ImageItem对应的子项
                new String[]{"ItemImage", "ItemText"},

                //ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[]{R.id.item_ninegrid_icon, R.id.item_ninegrid_title});
        //添加并且显示
        gridview.setAdapter(saImageItems);
        //添加消息处理
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!NoDoubleClickUtils.isDoubleClick()) {
                    NineGridItemBean gridviewitem = gridviewitems.get(position);

                    NineGridItemType type = gridviewitem.getType();
                    String action = gridviewitem.getAction();
                    String title = gridviewitem.getTitle();
                    Intent intent;


                    switch (type) {
                        case Window:
                            intent = new Intent();
                            switch (action) {
                                case "com.uplink.carins.activity.CarClaimActivity":
                                    intent = new Intent(ClaimsServiceAppActivity.this, CarClaimActivity.class);
                                    startActivity(intent);
                                    break;
                                case "com.uplink.carins.activity.ApplyLossAssessActivity":
                                    intent = new Intent(ClaimsServiceAppActivity.this, ApplyLossAssessActivity.class);
                                    startActivity(intent);
                                    break;
                            }
                            break;
                        case Url:
                            intent = new Intent(ClaimsServiceAppActivity.this, WebViewActivity.class);
                            intent.putExtra("title", title);
                            intent.putExtra("url", action);
                            startActivity(intent);
                            break;

                    }
                }
            }
        });
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
}
