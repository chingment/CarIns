package com.uplink.carins.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.uplink.carins.R;
import com.uplink.carins.model.api.ProductListBean;
import com.uplink.carins.ui.ViewHolder;
import com.uplink.carins.ui.refreshview.MyViewHolder;
import com.uplink.carins.ui.refreshview.RefreshAdapter;
import com.uplink.carins.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chingment on 2018/4/10.
 */

public class ProductListAdapter extends RefreshAdapter {

    private List<ProductListBean> data = new ArrayList<>();
    private LayoutInflater inflater;

    public void setData(List<ProductListBean> data) {
        this.data = data;

        notifyDataSetChanged();

    }

    public void addData(List<ProductListBean> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateItemHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produtlist_1, parent, false)) {
        };
    }

    @Override
    public void onBindItemHolder(final RecyclerView.ViewHolder holder, int position) {

        ProductListBean bean = data.get(position);

        TextView txt_product_name = (TextView) holder.itemView.findViewById(R.id.item_product_name);

        txt_product_name.setText(bean.getName() + "");


    }

    @Override
    public int getCount() {
        return data.size();
    }


}
