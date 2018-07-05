package com.uplink.carins.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.uplink.carins.Own.AppContext;
import com.uplink.carins.R;
import com.uplink.carins.activity.ProductDetailsByGoodsActivity;
import com.uplink.carins.activity.ProductDetailsByInsuranceActivity;
import com.uplink.carins.activity.ProductListByInsuranceActivity;
import com.uplink.carins.model.api.ProductListBean;
import com.uplink.carins.model.api.ProductSkuBean;
import com.uplink.carins.ui.refreshview.*;
import com.uplink.carins.ui.refreshview.MyViewHolder;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chingment on 2018/6/13.
 */

public class ProductSkuAdapter extends RefreshAdapter {

    private Context context;
    private List<ProductSkuBean> beans = new ArrayList<>();
    private LayoutInflater inflater;

    public void setData(List<ProductSkuBean> beans, Context context) {
        this.beans = beans;
        this.context = context;
        LogUtil.e("测试2");
        notifyDataSetChanged();

    }

    public void addData(List<ProductSkuBean> beans, Context context)
    {   this.context = context;
        this.beans.addAll(beans);
        notifyDataSetChanged();
    }


    @Override
    public com.uplink.carins.ui.refreshview.MyViewHolder onCreateItemHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_sku_tmp1, parent, false)) {
        };
    }

    @Override
    public void onBindItemHolder(final RecyclerView.ViewHolder holder, int position) {

        final ProductSkuBean bean = beans.get(position);

        ImageView img_main = (ImageView) holder.itemView.findViewById(R.id.img_main);
        TextView txt_name = (TextView) holder.itemView.findViewById(R.id.txt_name);
        TextView txt_price_unit = (TextView) holder.itemView.findViewById(R.id.txt_price_unit);
        TextView txt_price_integer = (TextView) holder.itemView.findViewById(R.id.txt_price_integer);
        TextView txt_price_decimal = (TextView) holder.itemView.findViewById(R.id.txt_price_decimal);


        txt_name.setText(bean.getName());


        txt_price_unit.setText("¥");


        String[] price = getPrice(String.valueOf(bean.getUnitPrice()));
        txt_price_integer.setText(price[0]);
        txt_price_decimal.setText(price[1]);


        //点击图片
        img_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailsByGoodsActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("dataBean", bean);
                intent.putExtras(b);
                context.startActivity(intent);

            }
        });

        CommonUtil.loadImageFromUrl(context, img_main, bean.getMainImg());
    }


    private String[] getPrice(String price) {


        String[] arr = new String[]{"0", ".00"};


        String[] arrPrice = price.split("\\.");

        if (arrPrice.length == -1) {
            arr[0] = price;
        } else {
            if (arrPrice.length >= 2) {
                arr[0] = arrPrice[0];
                arr[1] = "." + arrPrice[1];
            }
        }


        return arr;

    }

    @Override
    public int getCount() {
        return beans.size();
    }
}
