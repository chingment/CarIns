package com.uplink.carins.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.uplink.carins.R;
import com.uplink.carins.activity.ProductListByGoodsActivity;
import com.uplink.carins.model.api.ProductChildKindBean;
import com.uplink.carins.ui.BaseFragmentActivity;
import com.uplink.carins.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chingment on 2018/6/8.
 */

public class ProductChildKindAdapter extends BaseAdapter {

    private BaseFragmentActivity context;
    private List<ProductChildKindBean> beans = new ArrayList<>();


    public ProductChildKindAdapter(BaseFragmentActivity context, List<ProductChildKindBean> beans) {
        this.context = context;
        this.beans = beans;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_productkind_child, parent, false);
        }

        final ProductChildKindBean bean = beans.get(position);


        TextView txt_name = (TextView) convertView.findViewById(R.id.txt_name);
        ImageView img_main = (ImageView) convertView.findViewById(R.id.img_main);
        img_main.setTag(bean.getId());

        txt_name.setText(bean.getName());

        CommonUtil.loadImageFromUrl(context, img_main, bean.getImgUrl());


        img_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProductListByGoodsActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("dataBean", bean);
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

}
