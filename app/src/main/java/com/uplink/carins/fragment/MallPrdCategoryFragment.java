package com.uplink.carins.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.uplink.carins.R;
import com.uplink.carins.activity.MallMainActivity;
import com.uplink.carins.activity.adapter.MallPrdCategoryAdapter;
import com.uplink.carins.ui.BaseFragment;

/**
 * Created by chingment on 2018/3/27.
 */

public class MallPrdCategoryFragment extends BaseFragment implements
        AdapterView.OnItemClickListener {
    private String TAG = "MallPrdCategoryFragment";
    private View root;
    private LayoutInflater inflater;
    private MallMainActivity context;


    private String[] strs = {"常用分类", "服饰内衣", "鞋靴", "手机", "家用电器", "数码", "电脑办公",
            "个护化妆", "图书1", "图书2", "图书3", "图书4"};
    private ListView listView;
    private MallPrdCategoryAdapter adapter;
    private MallPrdCategoryItemFragment itemFragment;
    public static int mPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return root = inflater.inflate(R.layout.mallfragment_prdcategory, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = (MallMainActivity) getActivity();
        inflater = LayoutInflater.from(context);

        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        listView = (ListView) context.findViewById(R.id.list_prdcategory);

        adapter = new MallPrdCategoryAdapter(context, strs);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);

        getItemFrame(mPosition);
    }


    public void getItemFrame(int position) {
        itemFragment = new MallPrdCategoryItemFragment();
        FragmentTransaction fragmentTransaction = context.getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container1, itemFragment);
        Bundle bundle = new Bundle();
        bundle.putString(MallPrdCategoryItemFragment.TAG, strs[position]);
        itemFragment.setArguments(bundle);
        fragmentTransaction.commit();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
        //拿到当前位置
        mPosition = position;
        //即使刷新adapter
        adapter.notifyDataSetChanged();
        for (int i = 0; i < strs.length; i++) {
            getItemFrame(position);
        }
    }

}
