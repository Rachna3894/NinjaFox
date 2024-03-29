package com.mojodigi.ninjafox.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.location.SettingInjectorService;
import android.net.MailTo;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.AndroidException;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.mojodigi.ninjafox.Browser.AdBlock;
import com.mojodigi.ninjafox.Browser.AlbumController;
import com.mojodigi.ninjafox.Browser.BrowserController;
import com.mojodigi.ninjafox.Browser.NinjaClickHandler;
import com.mojodigi.ninjafox.Browser.jmmDownloadListener;
import com.mojodigi.ninjafox.Browser.NinjaGestureListener;
import com.mojodigi.ninjafox.Browser.NinjaWebChromeClient;
import com.mojodigi.ninjafox.Browser.NinjaWebViewClient;
import com.mojodigi.ninjafox.Database.Record;
import com.mojodigi.ninjafox.Database.RecordAction;
import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.SharedPrefs.SharedPreferenceUtil;
import com.mojodigi.ninjafox.Unit.BrowserUtility;
import com.mojodigi.ninjafox.Unit.IntentUtility;
import com.mojodigi.ninjafox.Unit.ViewUnit;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;


public class jmmWebView extends WebView implements AlbumController {
    private static final float[] NEGATIVE_COLOR = {
            -1.0f, 0, 0, 0, 255, // Red
            0, -1.0f, 0, 0, 255, // Green
            0, 0, -1.0f, 0, 255, // Blue
            0, 0, 0, 1.0f, 0     // Alpha
    };



    private Context context;
    private int flag = BrowserUtility.FLAG_NINJA;
    private int dimen144dp;
    private int dimen108dp;
    private int animTime;

    private Album album;
    private NinjaWebViewClient webViewClient;
    private NinjaWebChromeClient webChromeClient;
    private jmmDownloadListener downloadListener;
    private NinjaClickHandler clickHandler;
    private GestureDetector gestureDetector;

    private AdBlock adBlock;
    private boolean isIncogTab;
    private String tmpURl="";

    public AdBlock getAdBlock() {
        return adBlock;
    }

    private boolean foreground;
    public boolean isForeground() {
        return foreground;
    }

    private String userAgentOriginal;
    SharedPreferenceUtil prefs;
    public String getUserAgentOriginal() {
        return userAgentOriginal;
    }

    private BrowserController browserController = null;
    public BrowserController getBrowserController() {
        return browserController;
    }
    public void setBrowserController(BrowserController browserController) {
        this.browserController = browserController;
        this.album.setBrowserController(browserController);
    }

    public jmmWebView(Context context) {
        super(context);

        this.context = context;
        this.dimen144dp = getResources().getDimensionPixelSize(R.dimen.layout_width_144dp);
        this.dimen108dp = getResources().getDimensionPixelSize(R.dimen.layout_height_108dp);
        this.animTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        this.foreground = false;

        this.adBlock = new AdBlock(this.context);
        this.album = new Album(this.context, this, this.browserController);
        this.webViewClient = new NinjaWebViewClient(this);
        this.webChromeClient = new NinjaWebChromeClient(this);

        this.downloadListener = new jmmDownloadListener(this.context);

        this.clickHandler = new NinjaClickHandler(this);
        this.gestureDetector = new GestureDetector(context, new NinjaGestureListener(this));

        prefs=new SharedPreferenceUtil(context);

        initWebView();
        initWebSettings();
        initPreferences();
        initAlbum();
    }

    private synchronized void initWebView() {
        setAlwaysDrawnWithCacheEnabled(true);
        setAnimationCacheEnabled(true);
        setDrawingCacheBackgroundColor(0x00000000);
        setDrawingCacheEnabled(true);
        setWillNotCacheDrawing(false);
        setSaveEnabled(true);

        setBackground(null);
        getRootView().setBackground(null);
        setBackgroundColor(context.getResources().getColor(R.color.white));

        setFocusable(true);
        setFocusableInTouchMode(true);
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
        setScrollbarFadingEnabled(true);

        setWebViewClient(webViewClient);
        setWebChromeClient(webChromeClient);
        setDownloadListener(downloadListener);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        });
    }

    private synchronized void initWebSettings() {
        WebSettings webSettings = getSettings();
        userAgentOriginal = webSettings.getUserAgentString();

        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(context.getCacheDir().toString());
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        //new
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.getAllowUniversalAccessFromFileURLs();
        webSettings.setLoadWithOverviewMode(true);
        //new
        webSettings.setGeolocationDatabasePath(context.getFilesDir().toString());

        webSettings.setUseWideViewPort(true);

        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setPluginState(WebSettings.PluginState.ON);

        webSettings.setDefaultTextEncodingName(BrowserUtility.URL_ENCODING);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webSettings.setLoadsImagesAutomatically(true);
        } else {
            webSettings.setLoadsImagesAutomatically(false);
        }




    }

    public synchronized void initPreferences() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        WebSettings webSettings = getSettings();

        webSettings.setLoadWithOverviewMode(true);
        webSettings.setTextZoom(100);
        webSettings.setUseWideViewPort(true);

        String  showFlag = sp.getString(context.getString(R.string.sp_show_images), "0");
        System.out.print(""+showFlag);
         if(showFlag.equalsIgnoreCase("0"))  // always
           webSettings.setBlockNetworkImage(false);
         else if(showFlag.equalsIgnoreCase("1")) // over wifi
             webSettings.setBlockNetworkImage(false);
         else if(showFlag.equalsIgnoreCase("2")) // blocked
             webSettings.setBlockNetworkImage(true);
         else // in in rest  case if any
             webSettings.setBlockNetworkImage(false);


       /* webSettings.setJavaScriptEnabled(sp.getBoolean(context.getString(R.string.sp_javascript), true));
        webSettings.setJavaScriptCanOpenWindowsAutomatically(sp.getBoolean(context.getString(R.string.sp_javascript), true));*/


        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);


        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);

        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);


        webSettings.setGeolocationEnabled(sp.getBoolean(context.getString(R.string.sp_location), true));
        webSettings.setSupportMultipleWindows(sp.getBoolean(context.getString(R.string.sp_multiple_windows), false));
        boolean st=sp.getBoolean(context.getString(R.string.sp_passwords), true);

        System.out.print(""+st);
        webSettings.setSaveFormData(sp.getBoolean(context.getString(R.string.sp_passwords), true));
        webSettings.setSavePassword(sp.getBoolean(context.getString(R.string.sp_passwords), true));




        boolean textReflow = sp.getBoolean(context.getString(R.string.sp_text_reflow), true);
        if (textReflow) {
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try {
                    webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
                } catch (Exception e) {}
            }
        } else {
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }

        int userAgent = Integer.valueOf(sp.getString(context.getString(R.string.sp_user_agent), "0"));
        if (userAgent == 1) {
            webSettings.setUserAgentString(BrowserUtility.UA_DESKTOP);
        } else if (userAgent == 2) {
            webSettings.setUserAgentString(sp.getString(context.getString(R.string.sp_user_agent_custom), userAgentOriginal));
        } else {
            webSettings.setUserAgentString(userAgentOriginal);
        }

        int mode = Integer.valueOf(sp.getString(context.getString(R.string.sp_rendering), "0"));
        initRendering(mode);

        webViewClient.enableAdBlock(sp.getBoolean(context.getString(R.string.sp_ad_block), false));
    }

    private synchronized void initAlbum() {
        album.setAlbumCover(null);
        album.setAlbumTitle(context.getString(R.string.album_untitled));
        album.setBrowserController(browserController);
    }

    private void initRendering(int mode) {
        Paint paint = new Paint();

        switch (mode) {
            case 0: { // Default
                paint.setColorFilter(null);
                break;
            } case 1: { // Grayscale
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                paint.setColorFilter(filter);
                break;
            } case 2: { // Inverted
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(NEGATIVE_COLOR);
                paint.setColorFilter(filter);
                break;
            } case 3: { // Inverted grayscale
                ColorMatrix matrix = new ColorMatrix();
                matrix.set(NEGATIVE_COLOR);

                ColorMatrix gcm = new ColorMatrix();
                gcm.setSaturation(0);

                ColorMatrix concat = new ColorMatrix();
                concat.setConcat(matrix, gcm);

                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(concat);
                paint.setColorFilter(filter);

                break;
            } default: {
                paint.setColorFilter(null);
                break;
            }
        }

        // maybe sometime LAYER_TYPE_NONE would better?
        setLayerType(View.LAYER_TYPE_HARDWARE, paint);
    }

    @Override
    public synchronized void loadUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            jmmToast.show(context, R.string.toast_load_error);
            return;
        }
           //fix here the url  issue
        /* the below line is commented to  prevent google search  for user redirect task told by vipul on  25-05-2020*/

          url = BrowserUtility.queryWrapper(context, url.trim());

        if (url.startsWith(BrowserUtility.URL_SCHEME_MAIL_TO)) {
            Intent intent = IntentUtility.getEmailIntent(MailTo.parse(url));
            context.startActivity(intent);
            reload();

            return;
        } else if (url.startsWith(BrowserUtility.URL_SCHEME_INTENT)) {
            Intent intent;
            try {
                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                context.startActivity(intent);
            } catch (URISyntaxException u) {}

            return;
        }

        webViewClient.updateWhite(adBlock.isWhite(url));
        super.loadUrl(url);
        if (browserController != null && foreground) {
            browserController.updateBookmarks();
        }
    }

    @Override
    public void reload() {
        webViewClient.updateWhite(adBlock.isWhite(getUrl()));
        super.reload();
    }

    @Override
    public int getFlag() {
        return flag;
    }

    @Override
    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public View getAlbumView() {
        return album.getAlbumView();
    }

    @Override
    public void setAlbumCover(Bitmap bitmap) {
        album.setAlbumCover(bitmap);
    }

    @Override
    public String getAlbumTitle() {
        return album.getAlbumTitle();
    }

    @Override
    public void setAlbumTitle(String title) {
        album.setAlbumTitle(title);
    }

    @Override
    public void setIsInCogTab(boolean flag) {
        this.isIncogTab=flag;
        //album.setIncogTab(flag);

    }

    @Override
    public boolean getIsInCogTab() {
        return  isIncogTab;
        //return  album.getIsIncogTab();
    }

    @Override
    public synchronized void activate() {
        requestFocus();
        foreground = true;
        album.activate();
    }

    @Override
    public synchronized void deactivate() {
        clearFocus();
        foreground = false;
        album.deactivate();
    }

    public synchronized void update(int progress) {
        if (foreground) {
            browserController.updateProgress(progress);
        }

        setAlbumCover(ViewUnit.capture(this, dimen144dp, dimen108dp, false, Bitmap.Config.RGB_565));
        if (isLoadFinish()) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            if (sp.getBoolean(context.getString(R.string.sp_scroll_bar), true)) {
                setHorizontalScrollBarEnabled(true);
                setVerticalScrollBarEnabled(true);
            } else {
                setHorizontalScrollBarEnabled(false);
                setVerticalScrollBarEnabled(false);
            }
            setScrollbarFadingEnabled(true);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setAlbumCover(ViewUnit.capture(jmmWebView.this, dimen144dp, dimen108dp, false, Bitmap.Config.RGB_565));
                }
            }, animTime);

            boolean st=prefs.getBoolanValue(context.getResources().getString(R.string.sp_add_history), true);

            if(prefs.getBoolanValue(context.getResources().getString(R.string.sp_add_history), true)) {
                if (prepareRecord()) {
                    RecordAction action = new RecordAction(context);
                    action.open(true);
                    // adds the url to history

                    if(!tmpURl.equalsIgnoreCase(getUrl())) {
                            tmpURl=getUrl();
                            action.addHistory(new Record(getTitle(), getUrl(), System.currentTimeMillis()));
                        }

                    //jmmToast.show(context, "added to history");
                    action.close();
                    browserController.updateAutoComplete();
                }
            }
        }
    }

    public synchronized void update(String title, String url) {
        album.setAlbumTitle(title);
        if (foreground) {
            browserController.updateBookmarks();
            browserController.updateInputBox(url);
        }
    }

    public synchronized void pause() {
        onPause();
        pauseTimers();
    }

    public synchronized void resume() {
        onResume();
        resumeTimers();
    }

    @Override
    public synchronized void destroy() {
        stopLoading();
        onPause();
        clearHistory();
        setVisibility(GONE);
        removeAllViews();
        destroyDrawingCache();
        super.destroy();
    }

    public boolean isLoadFinish() {
        return getProgress() >= BrowserUtility.PROGRESS_MAX;
    }

    public void onLongPress() {
        Message click = clickHandler.obtainMessage();
        if (click != null) {
            click.setTarget(clickHandler);
        }
        requestFocusNodeHref(click);
    }

    private boolean prepareRecord() {
        String title = getTitle();
        String url = getUrl();

        if (title == null
                || title.isEmpty()
                || url == null
                || url.isEmpty()
                || url.startsWith(BrowserUtility.URL_SCHEME_ABOUT)
                || url.startsWith(BrowserUtility.URL_SCHEME_MAIL_TO)
                || url.startsWith(BrowserUtility.URL_SCHEME_INTENT)) {
            return false;
        }
        return true;
    }
}
