package com.uplink.carins.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.uplink.carins.R;
import com.uplink.carins.model.api.ProductKindBean;
import com.uplink.carins.ui.BaseFragmentActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chingment on 2018/3/27.
 */

public class ProductKindNameAdapter extends BaseAdapter {

    private BaseFragmentActivity context;
    private List<ProductKindBean> beans = new ArrayList<>();
    private int current_position;

    public ProductKindNameAdapter(BaseFragmentActivity context, List<ProductKindBean> beans, int position) {
        this.context = context;
        this.beans = beans;
        this.current_position = position;
    }
//
//    public void notifyDataSetChanged(Context context, List<ProductKindBean> beans, int position) {
//        this.context = context;
//        this.beans = beans;
//        current_position = position;
//        notifyDataSetChanged();
//    }

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

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_productkind_name, parent, false);
        }

        ProductKindBean bean = beans.get(position);


        TextView txt_name = (TextView) convertView.findViewById(R.id.txt_name);

        txt_name.setText(bean.getName());

        if (position == current_position) {

            convertView.setBackgroundColor(context.getResources().getColor(R.color.white));

        } else {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.text_f4f4f4));
        }
        return convertView;
    }
}
