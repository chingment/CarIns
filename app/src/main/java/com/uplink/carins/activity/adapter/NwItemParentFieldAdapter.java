package com.uplink.carins.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.uplink.carins.model.api.NwItemParentFieldBean;
import com.uplink.carins.R;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chingment on 2018/3/27.
 */

public class NwItemParentFieldAdapter extends BaseAdapter {

    private Context context;
    private List<NwItemParentFieldBean> beans = new ArrayList<>();
    public int current_position;

    public NwItemParentFieldAdapter(Context context, List<NwItemParentFieldBean> beans) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_nwcarins_offresult_parant, parent, false);
        }
        NwItemParentFieldBean bean = beans.get(position);


        TextView item_name = (TextView) convertView.findViewById(R.id.item_name);
        TextView item_value = (TextView) convertView.findViewById(R.id.item_value);
        ListView item_child = (ListView) convertView.findViewById(R.id.item_child);
        item_name.setText(bean.getField());
        item_value.setText(bean.getValue());


        NwItemChildFieldAdapter adapter=new NwItemChildFieldAdapter(context,bean.getChild());

        item_child.setAdapter(adapter);
        item_child.setFocusable(false);
        item_child.setClickable(false);
        item_child.setPressed(false);
        item_child.setEnabled(false);

        CommonUtil.setListViewHeightBasedOnChildren(item_child);

        return convertView;
    }
}
