package com.uplink.carins.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.uplink.carins.R;
import com.uplink.carins.model.api.CartProductSkuBean;
import com.uplink.carins.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chingment on 2018/7/5.
 */

public class CartProductSkuAdapter extends BaseAdapter {

    private Context context;
    private List<CartProductSkuBean> beans = new ArrayList<>();


    public CartProductSkuAdapter(Context context, List<CartProductSkuBean> beans) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_cart_sku, parent, false);
        }

        final CartProductSkuBean bean = beans.get(position);

        ImageView img_main = (ImageView) convertView.findViewById(R.id.img_main);
        TextView txt_name = (TextView) convertView.findViewById(R.id.txt_name);
        TextView txt_unitprice = (TextView) convertView.findViewById(R.id.txt_unitprice);
        TextView txt_quantity = (TextView) convertView.findViewById(R.id.txt_quantity);


        //img_main.setTag(bean.getId());

        txt_name.setText(bean.getName());
        txt_unitprice.setText(bean.getUnitPrice() + "");
        txt_quantity.setText(bean.getQuantity() + "");
        CommonUtil.loadImageFromUrl(context, img_main, bean.getMainImg());
        return convertView;
    }

}
