package com.uplink.carins.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.uplink.carins.Own.AppCacheManager;
import com.uplink.carins.Own.AppContext;
import com.uplink.carins.Own.AppManager;
import com.uplink.carins.Own.Config;
import com.uplink.carins.R;
import com.uplink.carins.activity.CarClaimActivity;
import com.uplink.carins.activity.CarInsureKindActivity;
import com.uplink.carins.activity.LoginActivity;
import com.uplink.carins.activity.MainActivity;
import com.uplink.carins.activity.OrderListActivity;
import com.uplink.carins.activity.TalentDemandActivity;
import com.uplink.carins.activity.WebViewActivity;
import com.uplink.carins.http.HttpClient;
import com.uplink.carins.http.HttpResponseHandler;
import com.uplink.carins.model.api.ApiResultBean;
import com.uplink.carins.model.api.BannerBean;
import com.uplink.carins.model.api.HomePageBean;
import com.uplink.carins.model.api.Result;
import com.uplink.carins.model.common.NineGridItemBean;
import com.uplink.carins.model.common.NineGridItemType;
import com.uplink.carins.ui.BaseFragment;
import com.uplink.carins.ui.dialog.CustomConfirmDialog;
import com.uplink.carins.ui.loopviewpager.AutoLoopViewPager;
import com.uplink.carins.ui.viewpagerindicator.CirclePageIndicator;
import com.uplink.carins.utils.CommonUtil;
import com.uplink.carins.utils.LogUtil;

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

    private List<NineGridItemBean> gridviewitems;//9宫格子数据

    private ApiResultBean<HomePageBean> home_page_result;//api数据

    private CustomConfirmDialog dialog_logout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return root = inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = (MainActivity) getActivity();

        initView();

        loadData();


        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

               // TODO: 2018/1/19 暂时没有实现定时任务功能更新缓存
                //AppCacheManager.setLastUpdateTime(CommonUtil.getCurrentTime());
                LogUtil.i("测试定时任务:" + CommonUtil.getCurrentTime());
                //loadData();
                handler.postDelayed(this, 2000);
            }
        };


        //handler.postDelayed(runnable, 2000);//每两秒执行一次runnable.


    }

    public void initView() {

        //homebanner
        home_banner = (RelativeLayout) root.findViewById(R.id.home_banner);
        home_banner_pager = (AutoLoopViewPager) root.findViewById(R.id.home_banner_pager);
        home_banner_pager.setFocusable(true);
        home_banner_pager.setFocusableInTouchMode(true);
        home_banner_pager.requestFocus();
        home_banner_indicator = (CirclePageIndicator) root.findViewById(R.id.home_banner_indicator);


        GridView gridview = (GridView) context.findViewById(R.id.gridview_ninegrid);


        gridviewitems = new ArrayList<NineGridItemBean>();

        NineGridItemBean gridviewitem1 = new NineGridItemBean();
        gridviewitems.add(new NineGridItemBean("一键投保", NineGridItemType.Window, "com.uplink.carins.activity.CarInsureKindActivity", R.drawable.ic_app_yjtb));
        gridviewitems.add(new NineGridItemBean("理赔服务", NineGridItemType.Window, "com.uplink.carins.activity.CarClaimActivity", R.drawable.ic_app_yjlp));
        gridviewitems.add(new NineGridItemBean("人才输送", NineGridItemType.Window, "com.uplink.carins.activity.TalentDemandActivity", R.drawable.ic_app_rcss));
        gridviewitems.add(new NineGridItemBean("违章缴罚", NineGridItemType.Url, "http://www.baidu.com", R.drawable.ic_app_wzcx));
        gridviewitems.add(new NineGridItemBean("我的订单", NineGridItemType.Window, "com.uplink.carins.activity.OrderListActivity", R.drawable.ic_app_wddd));
        gridviewitems.add(new NineGridItemBean("退出", NineGridItemType.Window, "com.uplink.carins.activity.LoginActivity", R.drawable.ic_app_tc));

        int length = gridviewitems.size();

        //生成动态数组，并且转入数据
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < gridviewitems.size(); i++) {

            NineGridItemBean gridviewitem = gridviewitems.get(i);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", gridviewitem.getIcon());//添加图像资源的ID
            map.put("ItemText", gridviewitem.getTitle());//按序号做ItemText
            lstImageItem.add(map);
        }
        //生成适配器的ImageItem 与动态数组的元素相对应
        SimpleAdapter saImageItems = new SimpleAdapter(context,
                lstImageItem,//数据来源
                R.layout.item_ninegrid,//item的XML实现

                //动态数组与ImageItem对应的子项
                new String[]{"ItemImage", "ItemText"},

                //ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[]{R.id.item_ninegrid_icon, R.id.item_ninegrid_title});
        //添加并且显示
        gridview.setAdapter(saImageItems);
        //添加消息处理
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                NineGridItemBean gridviewitem = gridviewitems.get(position);

                //view.setVisibility(View.INVISIBLE);
                //Toast.makeText(context,gridviewitem.getTitle(),Toast.LENGTH_LONG).show();

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

                                            dialog_logout.dismiss();

                                            AppContext.getInstance().setUser(null);

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
                            case "com.uplink.carins.activity.CarInsureKindActivity":

                                intent = new Intent(context, CarInsureKindActivity.class);
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
                            case "com.uplink.carins.activity.CarClaimActivity":

                                //intent.setClassName(context,action);  //方法3 此方式可用于打开其它的应用
                                //context.startActivity(intent);

                                intent = new Intent(context, CarClaimActivity.class);
                                startActivity(intent);


                                break;
                        }
                        break;
                    case Url:

                        intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("title", title);
                        intent.putExtra("url", action);

                        startActivity(intent);
                        break;

                }
            }
        });

    }


    @Override
    public void onClick(View view) {

    }


    private void setBanner(List<BannerBean> banner) {

        home_banner_adapter = new GalleryPagerAdapter(banner);
        home_banner_pager.setAdapter(home_banner_adapter);
        home_banner_indicator.setViewPager(home_banner_pager);
        home_banner_indicator.setPadding(5, 5, 10, 5);
    }

    private void loadData() {

        Map<String, String> params = new HashMap<>();
        params.put("userId", context.getAppContext().getUser().getId() + "");
        params.put("merchantId", context.getAppContext().getUser().getMerchantId() + "");
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


            LogUtil.i(TAG,"onSuccess====>>>"+response);

            ApiResultBean<HomePageBean> rt = JSON.parseObject(response, new TypeReference<ApiResultBean<HomePageBean>>() {
            });

            if (rt.getResult() == Result.SUCCESS) {

                HomePageBean bean = rt.getData();

                Boolean isFlag = false;
                if (bean.getLastUpdateTime() == null) {
                    isFlag = true;
                } else if (bean.getLastUpdateTime() != AppCacheManager.getLastUpdateTime()) {
                    isFlag = true;
                }

                if (isFlag) {
                    LogUtil.i("更新缓存数据");
                    AppCacheManager.setLastUpdateTime(bean.getLastUpdateTime());
                    setBanner(bean.getBanner());//设置banner
                    AppCacheManager.setCarInsCompany(bean.getCarInsCompany());
                    AppCacheManager.setCarInsKind(bean.getCarInsKind());
                    AppCacheManager.setCarInsPlan(bean.getCarInsPlan());
                    AppCacheManager.setTalentDemandWorkJob(bean.getTalentDemandWorkJob());
                }
            }
        }

        @Override
        public void onFailure(Request request, Exception e) {
            super.onFailure(request, e);
            LogUtil.e(TAG,"onFailure====>>>"+e.getMessage());
            showToast("数据加载失败");
        }
    }
}
