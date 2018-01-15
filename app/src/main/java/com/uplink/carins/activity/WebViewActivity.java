package com.uplink.carins.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.uplink.carins.R;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.StringUtil;

/**
 * WebView
 *
 * @author tuchg
 * @version 1.0 文件名称：WebViewActivity.java 类说明：
 */
public class WebViewActivity extends SwipeBackActivity implements View.OnClickListener {

    private String TAG = "WebViewActivity";

    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;

    // 主界面
    private WebView mainWebView = null;
    private ProgressBar mainProgressBar = null;


    @Override
    public void onPause() {
        mainWebView.reload();
        super.onPause();
        mainWebView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mainWebView.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        initView();
        initEvent();

        Intent intent = getIntent();
        String title="";
        String url="";

        if (intent != null) {
            title = intent.getStringExtra("title");

            txtHeaderTitle.setText(title);

            url = intent.getStringExtra("url");

        }


        // 设置支持JavaScript脚本
        WebSettings webSettings = mainWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        // 设置默认缩放方式尺寸是far
        webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDefaultFontSize(20);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 访问assets目录下的文件

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        // webSettings.setSavePassword(true);
        // webSettings.setSaveFormData(true);

        // enable navigator.geolocation
        webSettings.setGeolocationEnabled(true);
        webSettings.setGeolocationDatabasePath("/data/data/cn.car.insurance/databases/");
        // enable Web Storage: localStorage, sessionStorage
        webSettings.setDomStorageEnabled(true);
        mainWebView.loadUrl(url);


        // 设置WebViewClient
        mainWebView.setWebViewClient(new WebViewClient() {
            // url拦截
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 使用自己的WebView组件来响应Url加载事件，而不是使用默认浏览器器加载页面
                view.loadUrl(url);
                // 相应完成返回true
                return true;
            }

            // 页面开始加载
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mainProgressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            // 页面加载完成
            @Override
            public void onPageFinished(WebView view, String url) {
                mainProgressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }

        });

        // 设置WebChromeClient
        mainWebView.setWebChromeClient(new WebChromeClient() {
            // @Override
            // // 处理javascript中的alert
            // public boolean onJsAlert(WebView view, String url, String
            // message, final JsResult result) {
            // return super.onJsAlert(view, url, message, result);
            // };
            //
            // @Override
            // // 处理javascript中的confirm
            // public boolean onJsConfirm(WebView view, String url, String
            // message, final JsResult result) {
            // return super.onJsConfirm(view, url, message, result);
            // };
            //
            // @Override
            // // 处理javascript中的prompt
            // public boolean onJsPrompt(WebView view, String url, String
            // message, String defaultValue, final JsPromptResult result) {
            // return super.onJsPrompt(view, url, message, defaultValue,
            // result);
            // };

            // 设置网页加载的进度条
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mainProgressBar.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }

            // // 设置程序的Title
            // @Override
            // public void onReceivedTitle(WebView view, String title) {
            // super.onReceivedTitle(view, title);
            // }
        });
    }

    private void initEvent() {
        btnHeaderGoBack.setOnClickListener(this);
    }

    private void initView() {
        btnHeaderGoBack = (ImageView)findViewById(R.id.btn_main_header_goback);
        txtHeaderTitle = (TextView)findViewById(R.id.txt_main_header_title);
        mainWebView = (WebView) findViewById(R.id.webView);
        mainProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mainWebView.canGoBack()) {
            mainWebView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            default:
                break;
        }
    }

}
