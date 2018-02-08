package com.uplink.carins.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.uplink.carins.Own.AppCacheManager;
import com.uplink.carins.Own.AppContext;
import com.uplink.carins.Own.AppManager;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.activity.CarClaimActivity;
import com.uplink.carins.activity.CarInsServiceAppActivity;
import com.uplink.carins.activity.CarInsureDocumentActivity;
import com.uplink.carins.activity.CarInsureKindActivity;
import com.uplink.carins.activity.ClaimsServiceAppActivity;
import com.uplink.carins.activity.LoginActivity;
import com.uplink.carins.activity.MainActivity;
import com.uplink.carins.activity.OrderListActivity;
import com.uplink.carins.activity.PayConfirmActivity;
import com.uplink.carins.activity.TalentDemandActivity;
import com.uplink.carins.activity.WebViewActivity;
import com.uplink.carins.http.HttpClient;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.BannerBean;
import com.uplink.carins.model.api.ExtendedAppBean;
import com.uplink.carins.model.api.HomePageBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.model.common.NineGridItemBean;
import com.uplink.carins.model.common.NineGridItemType;
import com.uplink.carins.ui.BaseFragment;
import com.uplink.carins.ui.ViewHolder;
import com.uplink.carins.ui.dialog.CustomConfirmDialog;
import com.uplink.carins.ui.loopviewpager.AutoLoopViewPager;
import com.uplink.carins.ui.my.MyGridView;
import com.uplink.carins.ui.viewpagerindicator.CirclePageIndicator;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.NoDoubleClickUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by chingment on 2017/8/23.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private String TAG = "HomeFragment";
    private View root;
    private MainActivity context;
    private RelativeLayout home_banner;//banner根节点
    private GalleryPagerAdapter home_banner_adapter;//banner数据配置
    private AutoLoopViewPager home_banner_pager;//banner 页面
    private CirclePageIndicator home_banner_indicator;//banner 底部小图标
    private ApiResultBean<HomePageBean> home_page_result;//api数据
    private CustomConfirmDialog dialog_logout;
    //private Handler handler;
    //private Runnable runnable;
    private LayoutInflater inflater;
    private MyGridView gridview_ninegrid_thirdpartyapp;
    private MyGridView gridview_ninegrid_haoyilianapp;
    private RelativeLayout gridview_ninegrid_thirdpartyapp_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return root = inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = (MainActivity) getActivity();

        inflater = LayoutInflater.from(context);

        initView();

        loadData();

        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                LogUtil.i("定时任务:" + CommonUtil.getCurrentTime());
                loadData();
                context.getMyTaskHandler().postDelayed(this,2000);
            }
        };

        context.setMyTask(handler,runnable);
        context.startMyTask();


        setBanner(AppCacheManager.getBanner());
        setThirdPartyApp(AppCacheManager.getExtendedAppByThirdPartyApp());
        setHaoYiLianApp(AppCacheManager.getExtendedAppByHaoYiLianApp());
    }

    public void initView() {


        home_banner = (RelativeLayout) root.findViewById(R.id.home_banner);
        home_banner_pager = (AutoLoopViewPager) root.findViewById(R.id.home_banner_pager);
        home_banner_pager.setFocusable(true);
        home_banner_pager.setFocusableInTouchMode(true);
        home_banner_pager.requestFocus();
        home_banner_indicator = (CirclePageIndicator) root.findViewById(R.id.home_banner_indicator);

        gridview_ninegrid_haoyilianapp = (MyGridView) context.findViewById(R.id.gridview_ninegrid_haoyilian);
        gridview_ninegrid_thirdpartyapp = (MyGridView) context.findViewById(R.id.gridview_ninegrid_thirdpartyapp);
        gridview_ninegrid_thirdpartyapp_title= (RelativeLayout) context.findViewById(R.id.gridview_ninegrid_thirdpartyapp_title);

    }


    @Override
    public void onClick(View view) {

    }


    private void setBanner(List<BannerBean> banner) {


        if (banner != null) {
            home_banner_adapter = new GalleryPagerAdapter(banner);
            home_banner_pager.setAdapter(home_banner_adapter);
            home_banner_indicator.setViewPager(home_banner_pager);
            home_banner_indicator.setPadding(5, 5, 10, 5);
        }
    }

    private void setThirdPartyApp(List<ExtendedAppBean> thirdPartyApp) {

        if(thirdPartyApp==null)
        {
            gridview_ninegrid_thirdpartyapp_title.setVisibility(View.GONE);
            gridview_ninegrid_thirdpartyapp.setVisibility(View.GONE);
            return;
        }

        int size=thirdPartyApp.size();
        if(size==0)
        {
            gridview_ninegrid_thirdpartyapp_title.setVisibility(View.GONE);
            gridview_ninegrid_thirdpartyapp.setVisibility(View.GONE);
            return;
        }

        gridview_ninegrid_thirdpartyapp_title.setVisibility(View.VISIBLE);
        gridview_ninegrid_thirdpartyapp.setVisibility(View.VISIBLE);

        List<NineGridItemBean> gridviewitems_thirdpartyapp = new ArrayList<NineGridItemBean>();

        for (ExtendedAppBean bean : thirdPartyApp) {
            gridviewitems_thirdpartyapp.add(new NineGridItemBean(bean.getId(),bean.getName(), NineGridItemType.Url, bean.getLinkUrl(), bean.getImgUrl()));
        }

        NineGridItemdapter nineGridItemdapter = new NineGridItemdapter(gridviewitems_thirdpartyapp);

        gridview_ninegrid_thirdpartyapp.setAdapter(nineGridItemdapter);
    }


    private void setHaoYiLianApp(List<ExtendedAppBean> thirdPartyApp) {
        List<NineGridItemBean>  gridviewitems_haoyilian = new ArrayList<NineGridItemBean>();

        gridviewitems_haoyilian.add(new NineGridItemBean(0,"投保服务", NineGridItemType.Window, "com.uplink.carins.activity.CarInsServiceAppActivity", R.drawable.ic_app_yjtb));
        gridviewitems_haoyilian.add(new NineGridItemBean(0,"理赔服务", NineGridItemType.Window, "com.uplink.carins.activity.ClaimsServiceAppActivity", R.drawable.ic_app_yjlp));
        gridviewitems_haoyilian.add(new NineGridItemBean(0,"人才输送", NineGridItemType.Window, "com.uplink.carins.activity.TalentDemandActivity", R.drawable.ic_app_rcss));

        if(thirdPartyApp!=null) {
            if(thirdPartyApp.size()>0) {
                for (ExtendedAppBean bean : thirdPartyApp) {
                    gridviewitems_haoyilian.add(new NineGridItemBean(bean.getId(), bean.getName(), NineGridItemType.Url, bean.getLinkUrl(), bean.getImgUrl()));
                }
            }
        }

        //gridviewitems_haoyilian.add(new NineGridItemBean("违章缴罚", NineGridItemType.Url, "http://www.baidu.com", R.drawable.ic_app_wzcx));

        gridviewitems_haoyilian.add(new NineGridItemBean(0,"我的订单", NineGridItemType.Window, "com.uplink.carins.activity.OrderListActivity", R.drawable.ic_app_wddd));
        gridviewitems_haoyilian.add(new NineGridItemBean(0,"退出", NineGridItemType.Window, "com.uplink.carins.activity.LoginActivity", R.drawable.ic_app_tc));

        NineGridItemdapter nineGridItemdapter = new NineGridItemdapter(gridviewitems_haoyilian);

        gridview_ninegrid_haoyilianapp.setAdapter(nineGridItemdapter);

    }

    private void loadData() {

        Map<String, String> params = new HashMap<>();
        params.put("userId", context.getAppContext().getUser().getId() + "");
        params.put("merchantId", context.getAppContext().getUser().getMerchantId() + "");
        params.put("posMachineId", context.getAppContext().getUser().getPosMachineId() + "");
        params.put("datetime", AppCacheManager.getLastUpdateTime());


        LogUtil.i("datetime:" + AppCacheManager.getLastUpdateTime());

        HttpClient.getWithMy(Config.URL.home, params, new CallBack());
    }




    private class GalleryPagerAdapter extends PagerAdapter {

        private List<BannerBean> banner;

        GalleryPagerAdapter(List<BannerBean> banner) {
            this.banner = banner;
        }

        @Override
        public int getCount() {
            return banner.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BannerBean bannerBean = banner.get(position);

            ImageView item = new ImageView(context);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(-1, -1);
            item.setLayoutParams(params);
            item.setScaleType(ImageView.ScaleType.CENTER_CROP);
            item.setTag("" + position);
            CommonUtil.loadImageFromUrl(context, item, bannerBean.getImgUrl() + "");
            container.addView(item);

            return item;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }
    }

    private class CallBack extends HttpResponseHandler {
        @Override
        public void onSuccess(String response) {
            super.onSuccess(response);


            LogUtil.i(TAG, "onSuccess====>>>" + response);

            ApiResultBean<HomePageBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<HomePageBean>>() {
            });

            if (rt.getResult() == Result.SUCCESS) {


                HomePageBean bean = rt.getData();

                LogUtil.i("更新缓存数据" + bean.getLastUpdateTime());

                AppCacheManager.setLastUpdateTime(bean.getLastUpdateTime());
                AppCacheManager.setBanner(bean.getBanner());
                AppCacheManager.setCarInsCompany(bean.getCarInsCompany());
                AppCacheManager.setCarInsPlan(bean.getCarInsPlan());
                AppCacheManager.setCarInsKind(bean.getCarInsKind());
                AppCacheManager.setTalentDemandWorkJob(bean.getTalentDemandWorkJob());
                AppCacheManager.setExtendedApp(bean.getExtendedApp());

                setBanner(bean.getBanner());
                setThirdPartyApp(AppCacheManager.getExtendedAppByThirdPartyApp());
                setHaoYiLianApp(AppCacheManager.getExtendedAppByHaoYiLianApp());

                if (bean.getOrderInfo() != null) {

                    Bundle b = new Bundle();
                    b.putSerializable("dataBean", bean.getOrderInfo());

                    LogUtil.i("d=>>>>>>>>getOrderInfo().getProductName" + bean.getOrderInfo().getProductName());
                    LogUtil.i("d=>>>>>>>>getOrderInfo().getProductType" + bean.getOrderInfo().getProductType());

                    Intent intent = new Intent(context, PayConfirmActivity.class);
                    intent.putExtras(b);


                    context.stopMyTask();

                    startActivity(intent);

                    AppManager.getAppManager().finishAllActivity();
                    //finish();
                }

            }
        }

        @Override
        public void onFailure(Request request, Exception e) {
            super.onFailure(request, e);
            LogUtil.e(TAG, "onFailure====>>>" + e.getMessage());
            showToast("数据加载失败");
        }
    }


    private class NineGridItemdapter extends BaseAdapter {

        private List<NineGridItemBean> items;

        NineGridItemdapter(List<NineGridItemBean> items) {
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {

            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_ninegrid, null);
            }

            NineGridItemBean bean = items.get(position);

            LinearLayout item_ninegrid = ViewHolder.get(convertView, R.id.item_ninegrid);
            item_ninegrid.setTag(position);
            item_ninegrid.setOnClickListener(myClickListener);


            TextView item_title = ViewHolder.get(convertView, R.id.item_ninegrid_title);
            item_title.setText(bean.getTitle());

            ImageView item_img = ViewHolder.get(convertView, R.id.item_ninegrid_icon);
            if (bean.getIcon() instanceof Integer) {
                item_img.setImageDrawable(getResources().getDrawable(((int) bean.getIcon())));
            } else {
                CommonUtil.loadImageFromUrl(context, item_img, bean.getIcon().toString());
            }

            return convertView;
        }


        private LinearLayout.OnClickListener myClickListener = new LinearLayout.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!NoDoubleClickUtils.isDoubleClick()) {
                    int position = (int) v.getTag();

                    NineGridItemBean gridviewitem = items.get(position);
                    NineGridItemType type = gridviewitem.getType();
                    String action = gridviewitem.getAction();
                    String title = gridviewitem.getTitle();
                    Intent intent;


                    switch (type) {
                        case Window:
                            intent = new Intent();
                            switch (action) {
                                case "com.uplink.carins.activity.LoginActivity":

                                    if (dialog_logout == null) {

                                        dialog_logout = new CustomConfirmDialog(context, "确定要退出？", true);

                                        dialog_logout.getBtnSure().setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                context.stopMyTask();

                                                dialog_logout.dismiss();

                                                AppContext.getInstance().setUser(null);
                                                AppCacheManager.setLastUpdateTime(null);
                                                Intent l_Intent = new Intent(context, LoginActivity.class);
                                                startActivity(l_Intent);
                                                AppManager.getAppManager().finishAllActivity();


                                            }
                                        });

                                        dialog_logout.getBtnCancle().setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {


                                                dialog_logout.dismiss();
                                            }
                                        });


                                    }

                                    dialog_logout.show();

                                    break;
                                case "com.uplink.carins.activity.CarInsServiceAppActivity":

                                    intent = new Intent(context, CarInsServiceAppActivity.class);
                                    startActivity(intent);

                                    break;
                                case "com.uplink.carins.activity.OrderListActivity":

                                    intent = new Intent(context, OrderListActivity.class);

                                    intent.putExtra("status", 0);//默认选择状态为 全部

                                    startActivity(intent);

                                    break;
                                case "com.uplink.carins.activity.TalentDemandActivity":

                                    intent = new Intent(context, TalentDemandActivity.class);
                                    startActivity(intent);

                                    break;
                                case "com.uplink.carins.activity.ClaimsServiceAppActivity":

                                    //intent.setClassName(context,action);  //方法3 此方式可用于打开其它的应用
                                    //context.startActivity(intent);

                                    intent = new Intent(context, ClaimsServiceAppActivity.class);
                                    startActivity(intent);


                                    break;
                            }
                            break;
                        case Url:

                            intent = new Intent(context, WebViewActivity.class);
                            intent.putExtra("title", title);
                            intent.putExtra("url", CommonUtil.getExtendedAppUrl(gridviewitem));

                            startActivity(intent);
                            break;

                    }
                }


            }
        };
    }
}
