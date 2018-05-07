package com.uplink.carins.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.uplink.carins.Own.AppCacheManager;
import com.uplink.carins.R;
import com.uplink.carins.model.api.ExtendedAppBean;
import com.uplink.carins.model.common.NineGridItemBean;
import com.uplink.carins.model.common.NineGridItemType;
import com.uplink.carins.ui.ViewHolder;
import com.uplink.carins.ui.my.MyGridView;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.NoDoubleClickUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CarInsServiceAppActivity extends SwipeBackActivity implements View.OnClickListener {

    String TAG = "CarInsServiceAppActivity";
    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;
    private LayoutInflater inflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carinsserviceapp);
        inflater = LayoutInflater.from(CarInsServiceAppActivity.this);
        initView();
        initEvent();
    }

    private void initView() {

        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("投保服务");



        MyGridView gridview = (MyGridView) findViewById(R.id.gridview_ninegrid);


        final List<NineGridItemBean> gridviewitems = new ArrayList<NineGridItemBean>();

        gridviewitems.add(new NineGridItemBean(0,"车险投保", NineGridItemType.Window, "com.uplink.carins.activity.CarInsureKindActivity", R.drawable.ic_app_yjtb));

        //gridviewitems.add(new NineGridItemBean(0,"车险投保", NineGridItemType.Window, "com.uplink.carins.activity.NwCarInsGetCarInfoActivity", R.drawable.ic_app_yjtb));

        gridviewitems.add(new NineGridItemBean(0,"团体意外险", NineGridItemType.Window, "com.uplink.carins.activity.ProductListByInsuranceActivity", R.drawable.ic_app_ywbx));

        List<ExtendedAppBean> extendedApp = AppCacheManager.getExtendedAppByCarInsService();

        for (ExtendedAppBean bean : extendedApp) {
            gridviewitems.add(new NineGridItemBean(bean.getId(),bean.getName(), NineGridItemType.Url, bean.getLinkUrl(), bean.getImgUrl()));
        }

        NineGridItemdapter nineGridItemdapter=new NineGridItemdapter(gridviewitems);

        gridview.setAdapter(nineGridItemdapter);

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
                                case "com.uplink.carins.activity.CarInsureKindActivity":
                                    intent = new Intent(CarInsServiceAppActivity.this, CarInsureKindActivity.class);
                                    startActivity(intent);
                                    break;
                                case "com.uplink.carins.activity.NwCarInsGetCarInfoActivity":
                                    intent = new Intent(CarInsServiceAppActivity.this, NwCarInsGetCarInfoActivity.class);
                                    startActivity(intent);
                                    break;
                                case "com.uplink.carins.activity.ProductListByInsuranceActivity":
                                    intent = new Intent(CarInsServiceAppActivity.this, ProductListByInsuranceActivity.class);
                                    startActivity(intent);
                                    break;
                            }
                            break;
                        case Url:
                            intent = new Intent(CarInsServiceAppActivity.this, WebViewActivity.class);
                            intent.putExtra("title", title);
                            intent.putExtra("url", CommonUtil.getExtendedAppUrl(gridviewitem));
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


    private class NineGridItemdapter extends BaseAdapter {

        private List<NineGridItemBean> items;

        NineGridItemdapter(List<NineGridItemBean> items) {
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
                convertView = inflater.inflate(R.layout.item_ninegrid, null);
            }

            NineGridItemBean bean = items.get(position);

            ImageView item_img = ViewHolder.get(convertView, R.id.item_ninegrid_icon);
            TextView item_title = ViewHolder.get(convertView, R.id.item_ninegrid_title);
            item_title.setText(bean.getTitle());



            if(bean.getIcon() instanceof Integer) {
                item_img.setImageDrawable(getResources().getDrawable(((int) bean.getIcon())));
            }
            else
            {
                CommonUtil.loadImageFromUrl(CarInsServiceAppActivity.this,item_img,bean.getIcon().toString());
            }

            return convertView;
        }

    }
}
