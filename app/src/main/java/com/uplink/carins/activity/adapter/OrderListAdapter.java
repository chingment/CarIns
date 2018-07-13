package com.uplink.carins.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.uplink.carins.R;
import com.uplink.carins.model.api.OrderListBean;
import com.uplink.carins.model.api.OrderType;
import com.uplink.carins.ui.ViewHolder;
import com.uplink.carins.ui.refreshview.MyViewHolder;
import com.uplink.carins.ui.refreshview.RefreshAdapter;
import com.uplink.carins.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 项目名称：Pro_carInsurance
 * 类描述：
 * 创建人：tuchg
 * 创建时间：17/1/20 15:59
 */
public class OrderListAdapter extends RefreshAdapter {

    private List<OrderListBean> data = new ArrayList<>();
    private OrderFieldsAdapter orderFieldsAdapter;
    private LayoutInflater inflater;

    public void setData(List<OrderListBean> data) {
        this.data = data;

        notifyDataSetChanged();

    }

    public void addData(List<OrderListBean> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateItemHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderlist, parent, false)) {
        };
    }

    @Override
    public void onBindItemHolder(final RecyclerView.ViewHolder holder, int position) {

        OrderListBean bean = data.get(position);

        TextView txt_sn = (TextView) holder.itemView.findViewById(R.id.item_order_sn);
        TextView txt_product_name = (TextView) holder.itemView.findViewById(R.id.item_order_product_name);
        TextView txt_status_name = (TextView) holder.itemView.findViewById(R.id.item_order_status_name);
        TextView txt_remarks = (TextView) holder.itemView.findViewById(R.id.item_order_remarks);
        ListView list_fields = (ListView) holder.itemView.findViewById(R.id.item_order_fields);


        list_fields.setFocusable(false);
        list_fields.setClickable(false);
        list_fields.setPressed(false);
        list_fields.setEnabled(false);

        TextView btn_cancle = (TextView) holder.itemView.findViewById(R.id.item_order_btn_cancel);
        TextView btn_details = (TextView) holder.itemView.findViewById(R.id.item_order_btn_details);


        txt_sn.setText(bean.getSn() + "");
        txt_product_name.setText(bean.getTypeName() + "");
        txt_status_name.setText(bean.getStatusName() + "");
        txt_remarks.setText(bean.getRemarks());

        int status = bean.getStatus();
        int order_type = bean.getType();
        int follow_status = bean.getFollowStatus();

        switch (status) {
            case 1://已提交

                break;
            case 2://跟进中

                if (order_type == OrderType.CarInsure) {
                    if (follow_status == 1) {

                        btn_cancle.setVisibility(View.VISIBLE);
                    } else if (follow_status == 2) {

                    }
                } else if (order_type == OrderType.CarClaims) {
                    if (follow_status == 2) {

                    } else {

                    }
                }

                break;
            case 3://待支付
                if (order_type == OrderType.CarInsure) {
                    //btn_cancle.setVisibility(View.VISIBLE);
                } else if (order_type == OrderType.CarClaims) {
                }
                break;
            case 4://已完成
                break;
            case 5://已取消
                break;

        }

        if (btn_details != null) {
            btn_details.setTag(position);
            btn_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = Integer.parseInt(v.getTag() + "");
                    if (buttonsListner != null) {
                        buttonsListner.openOrderDetail(data.get(position));
                    }
                }
            });
        }


        inflater = LayoutInflater.from(holder.itemView.getContext());

        orderFieldsAdapter = new OrderFieldsAdapter();
        list_fields.setAdapter(orderFieldsAdapter);
        orderFieldsAdapter.setData(bean.getOrderField());
        CommonUtil.setListViewHeightBasedOnChildren(list_fields);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    private onButtonClickListener buttonsListner;

    public void setOnButtonClickListener(onButtonClickListener buttonsListner) {
        this.buttonsListner = buttonsListner;
    }

    public interface onButtonClickListener {
        void openOrderDetail(OrderListBean dataBean);
    }

    private class OrderFieldsAdapter extends BaseAdapter {
        protected static final String TAG = "OrderFieldsAdapter";
        private List<OrderListBean.OrderFieldBean> orderFields;


        public void setData(List<OrderListBean.OrderFieldBean> orderFields) {
            if (orderFields != null) {
                this.orderFields = orderFields;
            }
            notifyDataSetChanged();
        }


        OrderFieldsAdapter() {

            this.orderFields = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return orderFields.size();
        }

        @Override
        public Object getItem(int position) {
            return orderFields.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            OrderListBean.OrderFieldBean bean = orderFields.get(position);

           // LogUtil.i("field-->>>>:" + bean.getField());
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_orderlist_fields, parent, false);
            }
//
            TextView txt_name = ViewHolder.get(convertView, R.id.item_order_field_name);
            TextView txt_value = ViewHolder.get(convertView, R.id.item_order_field_value);
//
//
            txt_name.setText(bean.getField() + "");
            txt_value.setText(bean.getValue() + "");

            return convertView;
        }


    }


}
