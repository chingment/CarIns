package com.uplink.carins.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.uplink.carins.R;
import com.uplink.carins.model.api.NwItemChildFieldBean;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by chingment on 2018/6/12.
 */

public class NwItemChildFieldAdapter extends BaseAdapter {
    private Context context;
    private List<NwItemChildFieldBean> beans = new ArrayList<>();
    public int current_position;

    public NwItemChildFieldAdapter(Context context, List<NwItemChildFieldBean> beans) {
        this.context = context;
        this.beans = beans;
    }

    public void notifyDataSetChanged(int position) {
        current_position = position;
        notifyDataSetChanged();
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

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_nwcarins_offresult_child, parent, false);
        }
        NwItemChildFieldBean bean = beans.get(position);


        TextView item_name = (TextView) convertView.findViewById(R.id.item_name);
        TextView item_value = (TextView) convertView.findViewById(R.id.item_value);

        item_name.setText(bean.getField());
        item_value.setText(bean.getValue());



        return convertView;
    }
}
