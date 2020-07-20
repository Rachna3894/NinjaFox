package com.mojodigi.ninjafox.Activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.mojodigi.ninjafox.AddsUtility.AddMobUtils;
import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.Unit.CommonUtility;
import com.mojodigi.ninjafox.View.jmmToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class WebViewActivity extends Activity {

    Context mContext;
    WebView webview;
    long startTime;
    long endTime;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web_view);
        startTime = System.currentTimeMillis();
        mContext=this;



        String url=getIntent().getStringExtra("url");
        TextView title_txt = findViewById(R.id.title_txt);
        LinearLayout backButton = findViewById(R.id.backButton);
        ListView listView = findViewById(R.id.record_list);

        title_txt.setTypeface(CommonUtility.typeFace_Calibri_Bold(mContext));


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });



        webview = (WebView) findViewById(R.id.webview01);

        webview.setWebViewClient(new myWebClient());

        webview.getSettings().setJavaScriptEnabled(true);

        webview.getSettings().setDomStorageEnabled(true);

        webview.setWebContentsDebuggingEnabled(true);

        if(url!=null)
          webview.loadUrl(url);
        else
            jmmToast.show(mContext, "can't open url");

        AddMobUtils addMobUtils=CommonUtility.getAddMobInstance();
        addMobUtils.dispFacebookInterestialAdds(mContext);
    }

    @Override
    protected void onStart() {
        super.onStart();

        AddMobUtils addMobUtils=CommonUtility.getAddMobInstance();
        addMobUtils.dispFacebookBannerAdd(mContext,WebViewActivity.this);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
            return super.onRenderProcessGone(view, detail);

        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            super.onPageFinished(view, url);
            Log.e("onpagefinish", url);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // CustomProgressDialog.show(WebviewActivity.this,"Loading");
            view.loadUrl(url);
            return true;

        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        }


}
