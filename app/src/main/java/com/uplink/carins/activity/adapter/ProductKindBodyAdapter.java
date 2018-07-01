package com.uplink.carins.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.uplink.carins.R;
import com.uplink.carins.model.api.ProductKindBean;
import com.uplink.carins.ui.loopviewpager.AutoLoopViewPager;
import com.uplink.carins.ui.viewpagerindicator.CirclePageIndicator;
import com.uplink.carins.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chingment on 2018/6/8.
 */

public class ProductKindBodyAdapter extends BaseAdapter {

    private Context context;
    private List<ProductKindBean> beans = new ArrayList<>();
    private int current_position;

    public ProductKindBodyAdapter(Context context, List<ProductKindBean> beans, int position) {
        this.context = context;
        this.beans = beans;
        this.current_position = position;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return beans.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return beans.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_productkind_body, parent, false);
        }


        ProductKindBean bean = beans.get(position);


        AutoLoopViewPager banner_pager = (AutoLoopViewPager) convertView.findViewById(R.id.banner_pager);
        CirclePageIndicator banner_indicator = (CirclePageIndicator) convertView.findViewById(R.id.banner_indicator);
        banner_pager.setFocusable(true);
        banner_pager.setFocusableInTouchMode(true);
        banner_pager.requestFocus();
        banner_pager.setInterval(5000);
        banner_indicator.setPadding(5, 5, 10, 5);


        BannerAdapter banner_adapter = new BannerAdapter(context, bean.getBanners(), ImageView.ScaleType.CENTER_INSIDE);
        banner_pager.setAdapter(banner_adapter);
        banner_indicator.setViewPager(banner_pager);

        TextView txt_name = (TextView) convertView.findViewById(R.id.txt_name);

        txt_name.setText(bean.getName());


        GridView list_kind_childs = (GridView) convertView.findViewById(R.id.list_kind_childs);
        list_kind_childs.setFocusable(false);
        list_kind_childs.setClickable(false);
        list_kind_childs.setPressed(false);
        list_kind_childs.setEnabled(false);


        ProductChildKindAdapter productSkuAdapter = new ProductChildKindAdapter(context, bean.getChilds());

        list_kind_childs.setAdapter(productSkuAdapter);

        return convertView;
    }
}
