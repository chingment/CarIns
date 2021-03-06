package com.uplink.carins.activity.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.uplink.carins.model.api.ImgSetBean;
import com.uplink.carins.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chingment on 2018/6/5.
 */

public class BannerAdapter extends PagerAdapter {

    private Context context;
    private List<ImgSetBean> beans = new ArrayList<>();
    private ImageView.ScaleType scaleType;

    public BannerAdapter(Context context, List<ImgSetBean> beans, ImageView.ScaleType scaleType) {

        this.context = context;
        if (beans != null) {
            this.beans = beans;
        }
        this.scaleType = scaleType;
    }

    @Override
    public int getCount() {
        return beans.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView item = new ImageView(context);
        if (beans.size() > 0) {
            ImgSetBean bean = beans.get(position);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(-1, -1);
            item.setLayoutParams(params);
            //item.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            //ImageView.ScaleType.FIT_XY
            item.setScaleType(scaleType);
            CommonUtil.loadImageFromUrl(context, item, bean.getImgUrl());
            container.addView(item);
        }

        return item;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }


}
