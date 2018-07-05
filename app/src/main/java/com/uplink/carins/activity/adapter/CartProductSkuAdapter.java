package com.uplink.carins.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.uplink.carins.R;
import com.uplink.carins.activity.MallCartActivityActivity;
import com.uplink.carins.model.api.CartOperateType;
import com.uplink.carins.model.api.CartProductSkuBean;
import com.uplink.carins.ui.BaseFragmentActivity;
import com.uplink.carins.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chingment on 2018/7/5.
 */

public class CartProductSkuAdapter extends BaseAdapter {

    private BaseFragmentActivity mContext;
    private List<CartProductSkuBean> beans = new ArrayList<>();


    public CartProductSkuAdapter(BaseFragmentActivity context, List<CartProductSkuBean> beans) {
        this.mContext = context;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_cart_sku, parent, false);
        }

        final CartProductSkuBean bean = beans.get(position);

        CheckBox cb_selected = (CheckBox) convertView.findViewById(R.id.cb_selected);
        ImageView img_main = (ImageView) convertView.findViewById(R.id.img_main);
        TextView txt_name = (TextView) convertView.findViewById(R.id.txt_name);
        TextView txt_unitprice = (TextView) convertView.findViewById(R.id.txt_unitprice);
        TextView txt_quantity = (TextView) convertView.findViewById(R.id.txt_quantity);
        ImageView btn_decrease = (ImageView) convertView.findViewById(R.id.btn_decrease);
        ImageView btn_increase = (ImageView) convertView.findViewById(R.id.btn_increase);

        if (bean.getSelected()) {
            cb_selected.setChecked(true);
        } else {
            cb_selected.setChecked(false);

        }

        txt_name.setText(bean.getName());
        txt_unitprice.setText(bean.getSumPrice() + "");
        txt_quantity.setText(bean.getQuantity() + "");
        CommonUtil.loadImageFromUrl(mContext, img_main, bean.getMainImg());


        cb_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MallCartActivityActivity.operate(mContext, CartOperateType.SELECTED, bean.getSkuId() + "");
            }
        });
        ;

        btn_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MallCartActivityActivity.operate(mContext, CartOperateType.DECREASE, bean.getSkuId() + "");
            }
        });

        btn_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MallCartActivityActivity.operate(mContext, CartOperateType.INCREASE, bean.getSkuId() + "");
            }
        });


        return convertView;
    }

}
