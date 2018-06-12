package com.uplink.carins.utils;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.uplink.carins.Own.AppCacheManager;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.model.api.UserBean;
import com.uplink.carins.model.common.NineGridItemBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chingment on 2017/12/18.
 */

public class CommonUtil {

    public static void loadImageFromUrl(Context context , final ImageView photoView, String imageUrl) {
        if (StringUtil.isEmptyNotNull(imageUrl)) {
            photoView.setBackgroundResource(R.drawable.default_image);
        } else
            //.centerCrop()
            Picasso.with(context).load(imageUrl)
                    .placeholder(R.drawable.default_image).fit().centerInside()
                    .into(photoView, new Callback() {
                        @Override
                        public void onSuccess() {

                            photoView.setVisibility(View.VISIBLE);

                        }

                        @Override
                        public void onError() {
                            photoView.setBackgroundResource(R.drawable.default_image);
                        }
                    });
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {

        // 获取ListView对应的Adapter

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {

            return;

        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目

            View listItem = listAdapter.getView(i, null, listView);

            listItem.measure(0, 0); // 计算子项View 的宽高

            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        // listView.getDividerHeight()获取子项间分隔符占用的高度

        // params.height最后得到整个ListView完整显示需要的高度

        listView.setLayoutParams(params);

    }

    public static int px2dip(int pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static float dip2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return  (dipValue * scale + 0.5f);
    }

    public static void setRadioGroupCheckedByStringTag(RadioGroup testRadioGroup, String checkedtag ) {
        for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
            RadioButton rb=(RadioButton)testRadioGroup.getChildAt(i);
            String tag= String.valueOf(rb.getTag());

            if(tag.equals(checkedtag))
            {
                rb.setChecked(true);
            }
        }
    }

    public static  String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String   str   =   formatter.format(curDate);
        return  str;
    }

    public static boolean isDateOneBigger(String str1, String str2) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1);
            dt2 = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() > dt2.getTime()) {
            isBigger = true;
        } else if (dt1.getTime() < dt2.getTime()) {
            isBigger = false;
        }
        return isBigger;
    }

    public static String getExtendedAppUrl(NineGridItemBean item)
    {
        //return item.getAction();
        UserBean user= AppCacheManager.getUser();

        String appId=item.getAppId()+"";
        String userId="";
        String merchantId="";
        String posMachineId="";

        if(user!=null)
        {
            userId= user.getId()+"";
            merchantId=user.getMerchantId()+"";
            posMachineId=user.getPosMachineId()+"";
        }

        String url=Config.URL.extendedAppGoTo+"?userId="+userId+"&merchantId="+merchantId+"&posMachineId="+posMachineId+"&appId="+appId;
        return url;
    }

}
