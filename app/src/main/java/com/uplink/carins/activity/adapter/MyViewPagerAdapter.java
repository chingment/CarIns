package com.uplink.carins.activity.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.uplink.carins.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：Pro_carInsurance
 * 类描述：
 * 创建人：tuchg
 * 创建时间：17/1/18 17:33
 */

public class MyViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> list = new ArrayList<Fragment>();

    public MyViewPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int arg0) {
       // LogUtil.i("MyViewPagerAdapter的getItem:" + arg0);
        return list.get(arg0);
    }

    @Override
    public int getCount() {
       // LogUtil.i("MyViewPagerAdapter的数量:" + list.size());
        return list.size();
    }

}
