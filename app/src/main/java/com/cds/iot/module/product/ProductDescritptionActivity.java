package com.cds.iot.module.product;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cds.iot.R;
import com.cds.iot.base.BaseActivity;

/**
 * 产品说明
 */
public class ProductDescritptionActivity extends BaseActivity implements View.OnClickListener {

    private TextView textviewTitle;
    private FrameLayout contentLayout;

    private WebView webView;

    private String url = "http://47.106.148.192:8080//appls/produst_list.html";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_product_description;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        findViewById(R.id.back_button).setVisibility(View.VISIBLE);
        findViewById(R.id.back_button).setOnClickListener(this);
        textviewTitle = (TextView) findViewById(R.id.title);
        contentLayout = (FrameLayout) findViewById(R.id.content_layout);
        webView = new WebView(this);
        contentLayout.addView(webView, 0);

        if (getIntent().getExtras() != null) {
            url = getIntent().getStringExtra("url");
            String title = getIntent().getStringExtra("title");
            if (!TextUtils.isEmpty(title))
                textviewTitle.setText(title);
        }

        WebSettings webSettings = webView.getSettings();
        webSettings.setDatabaseEnabled(true);
        final String dbPath = getApplicationContext().getDir("db", Context.MODE_PRIVATE).getPath();
        webSettings.setDatabasePath(dbPath);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAppCacheEnabled(true);
        final String cachePath = getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        webSettings.setAppCachePath(cachePath);
        webSettings.setAppCacheMaxSize(5 * 1024 * 1024);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });
        webView.loadUrl(url);
        //点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {  //表示按返回键
                        webView.goBack();   //后退
                        //webview.goForward();//前进
                        return true;    //已处理
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        ((TextView)findViewById(R.id.title)).setText("产品说明");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            contentLayout.removeView(webView);

            webView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearHistory();
            webView.clearView();
            webView.removeAllViews();

            try {
                webView.destroy();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_button:
                finish();
                break;
        }
    }
}
