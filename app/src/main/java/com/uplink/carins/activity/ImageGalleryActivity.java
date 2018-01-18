package com.uplink.carins.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.uplink.carins.R;
import com.uplink.carins.ui.photoview.PhotoViewAdapter;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;

/**
 * Created by tiansj on 15/8/6.
 */
public class ImageGalleryActivity extends SwipeBackActivity {

    private int position;
    private List<String> imgUrls; //图片列表
    private TextView headTitle;
    private ImageView headBackBtn;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);

        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        imgUrls = intent.getStringArrayListExtra("images");
        if(imgUrls == null) {
            imgUrls = new ArrayList<>();
        }
        initView();
        initEvent();
        initGalleryViewPager();
    }

    public void initView() {
        headTitle = (TextView)findViewById(R.id.txt_main_header_title);
        headTitle.setText("1/" + imgUrls.size());
        headBackBtn = (ImageView)findViewById(R.id.btn_main_header_goback);
        headBackBtn.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void initEvent() {
        headBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initGalleryViewPager() {
        PhotoViewAdapter pagerAdapter = new PhotoViewAdapter(this, imgUrls);
        pagerAdapter.setOnItemChangeListener(new PhotoViewAdapter.OnItemChangeListener() {
            int len = imgUrls.size();
            @Override
            public void onItemChange(int currentPosition) {
                headTitle.setText((currentPosition+1) + "/" + len);
            }
        });
        mViewPager = (ViewPager)findViewById(R.id.viewer);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(position);
    }

}
