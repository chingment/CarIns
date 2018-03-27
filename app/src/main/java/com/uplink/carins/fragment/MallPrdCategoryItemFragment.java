package com.uplink.carins.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uplink.carins.R;

/**
 * Created by chingment on 2018/3/27.
 */

public class MallPrdCategoryItemFragment extends Fragment {

    public static final String TAG = "MallPrdCategoryItemFragment";
    private String str;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.mallfragment_prdcategory_item, null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        //得到数据
        str = getArguments().getString(TAG);
        tv_title.setText(str);
        return view;
    }
}
