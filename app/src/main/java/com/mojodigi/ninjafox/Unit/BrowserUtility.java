package com.mojodigi.ninjafox.Unit;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.GeneratedAdapter;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.util.Patterns;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebIconDatabase;
import android.webkit.WebViewDatabase;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.mojodigi.ninjafox.BroadCast.DownloadReceiver;
import com.mojodigi.ninjafox.Browser.AdBlock;
import com.mojodigi.ninjafox.Database.Record;
import com.mojodigi.ninjafox.Database.RecordAction;
import com.mojodigi.ninjafox.Model.BlockListModel;
import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.SharedPrefs.AppConstants;
import com.mojodigi.ninjafox.SharedPrefs.SharedPreferenceUtil;
import com.mojodigi.ninjafox.View.jmmToast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import static java.util.Calendar.getInstance;


public class BrowserUtility   {
    public static final int PROGRESS_MAX = 100;
    public static final int PROGRESS_MIN = 0;
    public static final String SUFFIX_HTML = ".html";
    public static final String SUFFIX_PNG = ".png";
    public static final String SUFFIX_TXT = ".txt";

    public static final int FLAG_BOOKMARKS = 0x100;
    public static final int FLAG_HISTORY = 0x101;
    public static final int FLAG_HOME = 0x102;
    public static final int FLAG_NINJA = 0x103;

    public static final int FLAG_INCOG=0x104;

    public static final String MIME_TYPE_TEXT_HTML = "text/html";
    public static final String MIME_TYPE_TEXT_PLAIN = "text/plain";
    public static final String MIME_TYPE_IMAGE = "image/*";

    public static final String BASE_URL = "file:///android_asset/";
    public static final String BOOKMARK_TYPE = "<DT><A HREF=\"{url}\" ADD_DATE=\"{time}\">{title}</A>";
    public static final String BOOKMARK_TITLE = "{title}";
    public static final String BOOKMARK_URL = "{url}";
    public static final String BOOKMARK_TIME = "{time}";

    //public static final String INTRODUCTION_EN = "ninja_introduction_en.html";
    // public static final String INTRODUCTION_ZH = "ninja_introduction_zh.html";

    public static final String SEARCH_ENGINE_GOOGLE = "https://www.google.com/search?q=";
    public static final String SEARCH_ENGINE_DUCKDUCKGO = "https://duckduckgo.com/?q=";
    public static final String SEARCH_ENGINE_STARTPAGE = "https://startpage.com/do/search?query=";
    public static final String SEARCH_ENGINE_BING = "http://www.bing.com/search?q=";
    public static final String SEARCH_ENGINE_BAIDU = "http://www.baidu.com/s?wd=";

    public static final String UA_DESKTOP = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
    public static final String URL_ENCODING = "UTF-8";
    public static final String URL_ABOUT_BLANK = "about:blank";
    public static final String URL_SCHEME_ABOUT = "about:";
    public static final String URL_SCHEME_MAIL_TO = "mailto:";
    public static final String URL_SCHEME_FILE = "file://";
    public static final String URL_SCHEME_FTP = "ftp://";
    public static final String URL_SCHEME_HTTP = "http://";
    public static final String URL_SCHEME_HTTPS = "https://";
    public static final String URL_SCHEME_INTENT = "intent://";

    public static final String URL_PREFIX_GOOGLE_PLAY = "www.google.com/url?q=";
    public static final String URL_SUFFIX_GOOGLE_PLAY = "&sa";
    public static final String URL_PREFIX_GOOGLE_PLUS = "plus.url.google.com/url?q=";
    public static final String URL_SUFFIX_GOOGLE_PLUS = "&rct";


    public static final String URL_YOUTUBE="www.youtube.com";
    public static final String URl_GOOGLE="www.google.com";
    public static final String URL_FACEBOOK="ww.facebook.com";
    public static final String URL_INSTA="www.instagram.com";
    public static final String URL_QOURA="www.quora.com";
    public static final String URL_TWITTER="www.twitter.com";
    public static final String URL_KHULASA="https://m.khulasa-news.com/";
 public Context mContext;

    public static boolean isURL(String url) {
        if (url == null) {
            return false;
        }

        url = url.toLowerCase(Locale.getDefault());
        if (url.startsWith(URL_ABOUT_BLANK)
                || url.startsWith(URL_SCHEME_MAIL_TO)
                || url.startsWith(URL_SCHEME_FILE)) {
            return true;
        }

        String regex = "^((ftp|http|https|intent)?://)"
                + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?"
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}"
                + "|"
                + "([0-9a-z_!~*'()-]+\\.)*"
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\."
                + "[a-z]{2,6})"
                + "(:[0-9]{1,4})?"
                + "((/?)|"
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(url).matches();
    }

   /* public static boolean isValidUrl(String potentialUrl )
    {
      return   Patterns.WEB_URL.matcher(potentialUrl).matches();
    }
*/

   public BrowserUtility(Context mContext)
   {
       this.mContext=mContext;
   }

    public static String queryWrapper(Context context, String query) {
        // Use prefix and suffix to process some special links
        String temp = query.toLowerCase(Locale.getDefault());
        if (temp.contains(URL_PREFIX_GOOGLE_PLAY) && temp.contains(URL_SUFFIX_GOOGLE_PLAY)) {
            int start = temp.indexOf(URL_PREFIX_GOOGLE_PLAY) + URL_PREFIX_GOOGLE_PLAY.length();
            int end = temp.indexOf(URL_SUFFIX_GOOGLE_PLAY);
            query = query.substring(start, end);
        } else if (temp.contains(URL_PREFIX_GOOGLE_PLUS) && temp.contains(URL_SUFFIX_GOOGLE_PLUS)) {
            int start = temp.indexOf(URL_PREFIX_GOOGLE_PLUS) + URL_PREFIX_GOOGLE_PLUS.length();
            int end = temp.indexOf(URL_SUFFIX_GOOGLE_PLUS);
            query = query.substring(start, end);
        }

        if (isURL(query)) {
            if (query.startsWith(URL_SCHEME_ABOUT) || query.startsWith(URL_SCHEME_MAIL_TO)) {
                return query;
            }

            if (!query.contains("://")) {
                query = URL_SCHEME_HTTP + query;
            }

            return query;
        }

        try {
            query = URLEncoder.encode(query, URL_ENCODING);
        } catch (UnsupportedEncodingException u) {}

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String custom = sp.getString(context.getString(R.string.sp_search_engine_custom), SEARCH_ENGINE_GOOGLE);
        final int i = Integer.valueOf(sp.getString(context.getString(R.string.sp_search_engine), "0"));
        switch (i) {
            case 0:
                return SEARCH_ENGINE_GOOGLE + query;
            case 1:
                return SEARCH_ENGINE_DUCKDUCKGO + query;
            case 2:
                return SEARCH_ENGINE_STARTPAGE + query;
            case 3:
                return SEARCH_ENGINE_BING + query;
            case 4:
                return SEARCH_ENGINE_BAIDU + query;
            case 5:
                return custom + query;
            default:
                return SEARCH_ENGINE_GOOGLE + query;
        }
    }

    public static String urlWrapper(String url) {
        if (url == null) {
            return null;
        }

        String green500 = "<font color='#4CAF50'>{content}</font>";
        String gray500 = "<font color='#9E9E9E'>{content}</font>";

        if (url.startsWith(BrowserUtility.URL_SCHEME_HTTPS)) {
            String scheme = green500.replace("{content}", BrowserUtility.URL_SCHEME_HTTPS);
            url = scheme + url.substring(8);
        } else if (url.startsWith(BrowserUtility.URL_SCHEME_HTTP)) {
            String scheme = gray500.replace("{content}", BrowserUtility.URL_SCHEME_HTTP);
            url = scheme + url.substring(7);
        }

        return url;
    }

    public static boolean isUrlBlocked(Context mContext,SharedPreferenceUtil prefs,String url) {
        if(prefs!=null)
        {
            if(prefs.getBlockedArrayList(mContext)!=null)
            {
                ArrayList<BlockListModel> list = prefs.getBlockedArrayList(mContext);
                for(int i=0;i<list.size();i++)
                {
                    String indexString = list.get(i).getSiteUrl();//.replaceAll("\\.", "");    ;
                    System.out.println(""+indexString);
                    String mainDomain=findMainDomain(url);
                    if(mainDomain!=null) {
                        if (indexString.equalsIgnoreCase(url) || url.toLowerCase().contains(indexString.toLowerCase()) || indexString.toLowerCase().contains(mainDomain.toLowerCase()) ||indexString.toLowerCase().contains(mainDomain.toLowerCase()) || indexString.toLowerCase().contains(url.toLowerCase()))
                            return true;
                    }
                    else {
                        if (indexString.equalsIgnoreCase(url) || url.toLowerCase().contains(indexString.toLowerCase()) || indexString.toLowerCase().contains(url.toLowerCase()))
                            return true;
                    }

                }
            }
            return false;
        }
        return false;

    }

    private  static  String findMainDomain(String url)
    {
        String[] arr=url.split("\\.");
        if(arr.length==3)
            return arr[1];
        else if(arr.length==2)
            return arr[0];
        else
            return null;

    }



    public static boolean bitmap2File(Context context, Bitmap bitmap, String filename) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static Bitmap file2Bitmap(Context context, String filename) {
        try {
            FileInputStream fileInputStream = context.openFileInput(filename);
            return BitmapFactory.decodeStream(fileInputStream);
        } catch (Exception e) {
            return null;
        }
    }

    public static void copyURL(Context mContext, String url) {
        ClipboardManager manager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText(null, url.trim());
        manager.setPrimaryClip(data);
        jmmToast.show(mContext, R.string.toast_copy_successful);
    }

    public static final String NOTIFICATION_CHANNEL_ID = "1001";
    public static final String NOTIFICATION_CHANNEL_NAME = "Ninjafox";


    RemoteViews collapsedView;
    RemoteViews strechedView ;

    public static void download(final Context context, String url, String contentDisposition, String mimeType)
    {
        final int notifyID=101;
        try
        {
            // to  test  download files from sample-videos.com  here various kind of files are present;
            //String extension=getFileExtension(url);
            boolean st = checkdownloadSettings(context);
            System.out.print("" + st);
            if (!checkdownloadSettings(context))   // if download settings return false terminate  the downlaod ;
            {
                return;
            }

            boolean dstatus = CommonUtility.checkOrCreateDownloadDirectory(context);

            if (dstatus) {

                String filename = URLUtil.guessFileName(url, contentDisposition, mimeType); // Maybe unexpected filename.

                if (filename.equalsIgnoreCase("") || filename == null || filename.contains(".bin") || filename.length() < 3) {
                    filename = getFileName() + getFileExtension(url);
                }
                final RemoteViews collapsedView = new RemoteViews(context.getPackageName(), R.layout.notification_small);
                final RemoteViews strechedView = new RemoteViews(context.getPackageName(), R.layout.notification_large);

                final String finalFilename = filename;
                String dpath=CommonUtility.getRootDirPath(context);
                System.out.print(""+dpath);

                // int downloadId = PRDownloader.download(url, Environment.DIRECTORY_DOWNLOADS+"/"+AppConstants.downloadDirectory+"/", filename)

                String pth=Environment.DIRECTORY_DOWNLOADS+"/"+AppConstants.downloadDirectory+"/";
                System.out.print(""+pth);

                // generate notification

                // Get the layouts to use in the custom notification

                // Apply the layouts to the notification

                final NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(context);
                mBuilder.setSmallIcon(R.drawable.ic_fcm_icon)
                        //.setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                        .setCustomContentView(collapsedView)
                        .setCustomBigContentView(strechedView)
                        .build();

                final NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                collapsedView.setTextViewText(R.id.downLoadFileaName, finalFilename);
                strechedView.setTextViewText(R.id.downLoadFileaName, finalFilename);


                DownloadReceiver  downloadReceiver = new DownloadReceiver();
                IntentFilter intentFilter = new IntentFilter(DownloadReceiver.ACTION_DOWANLOAD);
                intentFilter.addAction(DownloadReceiver.ACTION_CANCEL);
                intentFilter.addAction(DownloadReceiver.ACTION_PAUSE);
                intentFilter.addAction(DownloadReceiver.ACTION_RESUME);
                context.registerReceiver(downloadReceiver, intentFilter);


                Intent button1I = new Intent(DownloadReceiver.ACTION_CANCEL);
                button1I.putExtra("url", url);
                PendingIntent button1PI = PendingIntent.getBroadcast(context, 0, button1I, 0);

                Intent button2I = new Intent(DownloadReceiver.ACTION_PAUSE);
                PendingIntent button2PI = PendingIntent.getBroadcast(context, 0, button2I, 0);

                strechedView.setOnClickPendingIntent(R.id.cancelDownload, button1PI);
                strechedView.setOnClickPendingIntent(R.id.pauseDownload, button2PI);


                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                {
                    int importance = NotificationManager.IMPORTANCE_LOW;
                    NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
                    //notificationChannel.enableLights(true);
                    //notificationChannel.setLightColor(Color.RED);
                    //notificationChannel.enableVibration(true);
                    //notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    assert mNotificationManager != null;
                    mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                    mNotificationManager.createNotificationChannel(notificationChannel);
                }
                // generate notification

                int downloadId = PRDownloader.download(url, Environment.getExternalStorageDirectory()+"/", filename).build()
                        .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                            @Override
                            public void onStartOrResume() {


                                assert mNotificationManager != null;
                                mNotificationManager.notify(notifyID /* Request Code */, mBuilder.build());



                            }
                        })
                        .setOnPauseListener(new OnPauseListener() {
                            @Override
                            public void onPause() {


                            }
                        })
                        .setOnCancelListener(new OnCancelListener() {
                            @Override
                            public void onCancel() {

                            }
                        })
                        .setOnProgressListener(new OnProgressListener() {
                            @Override
                            public void onProgress(final Progress progress) {
                                final long progressPercent = progress.currentBytes * 100 / progress.totalBytes;


                                collapsedView.setTextViewText(R.id.bytedispText, CommonUtility.getProgressDisplayLine(progress.currentBytes, progress.totalBytes));
                                strechedView.setTextViewText(R.id.bytedispText, CommonUtility.getProgressDisplayLine(progress.currentBytes, progress.totalBytes));

                                collapsedView.setProgressBar(R.id.download_progress, 100, (int)progressPercent, false);
                                strechedView.setProgressBar(R.id.download_progress, 100, (int)progressPercent, false);

                                collapsedView.setTextViewText(R.id.statusText,"Downloading");
                                strechedView.setTextViewText(R.id.statusText, "Downloading");


                                assert mNotificationManager != null;
                                mNotificationManager.notify(notifyID /* Request Code */, mBuilder.build());


                            }
                        })
                        .start(new OnDownloadListener() {
                            @Override
                            public void onDownloadComplete() {

                                jmmToast.show(context, "download complete");
                                collapsedView.setTextViewText(R.id.statusText,"download complete");
                                strechedView.setTextViewText(R.id.statusText, "download complete");
                                mNotificationManager.notify(notifyID /* Request Code */, mBuilder.build());

                            }

                            @Override
                            public void onError(Error error) {
                                String errMsg=error.getServerErrorMessage();
                                jmmToast.show(context, errMsg);
                            }

                        });
            }

        }
        catch (Exception e)
        {
            String string=e.getMessage();
            System.out.print(""+string);
        }
    }


    public static void download3(final Context context, String url, String contentDisposition, String mimeType)
    {
        try
        {
            // to  test  download files from sample-videos.com  here various kind of files are present;
            //String extension=getFileExtension(url);
            boolean st = checkdownloadSettings(context);
            System.out.print("" + st);
            if (!checkdownloadSettings(context))   // if download settings return false terminate  the downlaod ;
            {
                return;
            }

            boolean dstatus = CommonUtility.checkOrCreateDownloadDirectory(context);

            if (dstatus) {

                String filename = URLUtil.guessFileName(url, contentDisposition, mimeType); // Maybe unexpected filename.

                if (filename.equalsIgnoreCase("") || filename == null || filename.contains(".bin") || filename.length() < 3) {
                    filename = getFileName() + getFileExtension(url);
                }

                int downloadId = PRDownloader.download(url, Environment.DIRECTORY_DOWNLOADS+"/"+AppConstants.downloadDirectory+"/", filename)
                        .build()
                        .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                            @Override
                            public void onStartOrResume() {

                                // Get the layouts to use in the custom notification
                                RemoteViews notificationLayout = new RemoteViews(context.getPackageName(), R.layout.notification_small);
                                RemoteViews notificationLayoutExpanded = new RemoteViews(context.getPackageName(), R.layout.notification_large);

                                // Apply the layouts to the notification
                                Notification customNotification = new NotificationCompat.Builder(context, "1")
                                        .setSmallIcon(R.drawable.ic_fcm_icon)
                                        //.setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                                        .setCustomContentView(notificationLayout)
                                        .setCustomBigContentView(notificationLayoutExpanded)
                                        .build();


                                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                                notificationManager.notify(1, customNotification);

                                int id = notificationLayoutExpanded.getLayoutId();


                            }
                        })
                        .setOnPauseListener(new OnPauseListener() {
                            @Override
                            public void onPause() {

                            }
                        })
                        .setOnCancelListener(new OnCancelListener() {
                            @Override
                            public void onCancel() {

                            }
                        })
                        .setOnProgressListener(new OnProgressListener() {
                            @Override
                            public void onProgress(Progress progress) {
                                long progressPercent = progress.currentBytes * 100 / progress.totalBytes;

                                jmmToast.show(context, ""+progressPercent);


                            }
                        })
                        .start(new OnDownloadListener() {
                            @Override
                            public void onDownloadComplete() {

                                jmmToast.show(context, "download complete");
                            }

                            @Override
                            public void onError(Error error) {

                            }


                        });




            }

        }
        catch (Exception e)
        {
            String string=e.getMessage();
            System.out.print(""+string);
        }
    }

    public static void download2(Context context, String url, String contentDisposition, String mimeType) {
        try {
            // to  test  download files from sample-videos.com  here various kind of files are present;
            //String extension=getFileExtension(url);
            boolean st = checkdownloadSettings(context);
            System.out.print("" + st);
            if (!checkdownloadSettings(context))   // if download settings return false terminate  the downlaod ;
            {
                return;
            }

            boolean dstatus = CommonUtility.checkOrCreateDownloadDirectory(context);

            if (dstatus) {
                DownloadManager.Request dManager = new DownloadManager.Request(Uri.parse(url.trim()));
                String filename = URLUtil.guessFileName(url, contentDisposition, mimeType); // Maybe unexpected filename.

                if (filename.equalsIgnoreCase("") || filename == null || filename.contains(".bin") || filename.length() < 3) {
                    filename = getFileName() + getFileExtension(url);
                }

                dManager.allowScanningByMediaScanner();
                dManager.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                // download notification hidden;

                //dManager.setShowRunningNotification(true);

                dManager.setTitle(filename);
                dManager.setMimeType(mimeType);

                dManager.setDestinationInExternalPublicDir( Environment.DIRECTORY_DOWNLOADS+"/"+AppConstants.downloadDirectory+"/", filename);

                //dManager.setDestinationInExternalPublicDir( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+AppConstants.downloadDirectory+"/", filename);
                //dManager.setDestinationInExternalFilesDir(context, Environment.getExternalStorageDirectory()+"/"+AppConstants.downloadDirectory+"/", filename);
                // dManager.setDestinationInExternalPublicDir( "/"+AppConstants.downloadDirectory, filename);

                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(dManager);
                jmmToast.show(context, R.string.toast_start_download);

                //

                //
            }
        }

        catch(Exception e)
        {
            String s = e.getMessage();
            System.out.print("" + s);
        }


    }


    private static boolean isWifiEnabled(Context context)
    {
        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        boolean isWifiEnabled=wifi.isWifiEnabled();
        return isWifiEnabled;
    }
    private  static boolean isMobileDataEnabled(Context context)
    {
        boolean mobileDataEnabled = false; // Assume disabled
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true); // Make the method callable
            // get the setting for "mobile data"
            mobileDataEnabled = (Boolean)method.invoke(cm);
        } catch (Exception e) {
            // Some problem accessible private API
            // TODO do whatever error handling you want here
        }
        return mobileDataEnabled;
    }


    public static boolean  checkdownloadSettings(Context context)
    {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String  dFlag = sp.getString(context.getString(R.string.sp_download), "0");
        Log.d("DownalodSett", dFlag);
        //jmmToast.show(context, dFlag);

        if(dFlag.equalsIgnoreCase("0"))   // in case  both are allowed
        {
            //return  context.getString(R.string.download_setting_both);
            return  true;

        }
        else if(dFlag.equalsIgnoreCase("1")) // in case only  wifi  is allowed
        {
            //return  context.getString(R.string.download_setting_wifi);
            if(isWifiEnabled(context) )
            {
                return  true;
            }
            else jmmToast.show(context, context.getString(R.string.donwload_setiings_msg_wifi));
            //else Toast.makeText(context, context.getString(R.string.donwload_setiings_msg), Toast.LENGTH_SHORT).show();
        }
        else if(dFlag.equalsIgnoreCase("2"))   // in case  only  mobile data  is allowed
        {

            if(!isWifiEnabled(context) && isMobileDataEnabled(context))
            {
                return  true;
            }
            else jmmToast.show(context, context.getString(R.string.donwload_setiings_msg_mobile));
            //else  Toast.makeText(context, context.getString(R.string.donwload_setiings_msg), Toast.LENGTH_SHORT).show();

            //return  context.getString(R.string.download_setting_mobile);
        }
        else               // in some other case both will  be considered;
        {
            //return  context.getString(R.string.download_setting_both);
            return  true;
        }


        return false;
    }


    public static String getFileName()
    {
        //SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String date = df.format(getInstance().getTime());
        return date;
    }

    public static String getFileExtension(String url)
    {

        // extensiion  url;
        //https://www.computerhope.com/issues/ch001789.htm
        List<String> extensinList=new ArrayList<>();
        //image
        extensinList.add(".png");
        extensinList.add(".jpg");
        extensinList.add(".jpeg");
        extensinList.add(".gif");
        extensinList.add(".bmp");
        extensinList.add(".ico");
        extensinList.add(".psd");
        extensinList.add(".svg");
        extensinList.add(".tif");
        extensinList.add(".tiff");

        //image

        // vdo
        extensinList.add(".wmv");
        extensinList.add(".vob");
        extensinList.add(".swf");
        extensinList.add(".rm");
        extensinList.add(".mpg");
        extensinList.add(".mp4");
        extensinList.add(".mov");
        extensinList.add(".mkv");
        extensinList.add(".m4v");
        extensinList.add(".flv");
        extensinList.add(".avi");
        // vdo

        //audio  file
        extensinList.add(".mp3");
        extensinList.add(".aif");
        extensinList.add(".cda");
        extensinList.add(".mid");
        extensinList.add(".mpa");
        extensinList.add(".ogg");
        extensinList.add(".wav");
        extensinList.add(".wma");
        //audio  file

        //docs and exes
        extensinList.add(".pdf");
        extensinList.add(".docx");
        extensinList.add(".doc");
        extensinList.add(".rtf");
        extensinList.add(".rar");
        extensinList.add(".zip");
        extensinList.add(".xml");
        extensinList.add(".sql");
        extensinList.add(".db");
        extensinList.add(".log");
        extensinList.add(".mdb");
        extensinList.add(".csv");
        extensinList.add(".apk");
        extensinList.add(".bat");
        extensinList.add(".wsf");
        extensinList.add(".py");
        extensinList.add(".php");
        extensinList.add(".txt");
        extensinList.add(".ppt");
        extensinList.add(".pptx");
        extensinList.add(".odp");
        extensinList.add(".key");
        extensinList.add(".xlsx");
        extensinList.add(".xls");
        extensinList.add(".exe");

        String extension="";
        if(url.contains(".")) {
            extension = url.substring(url.lastIndexOf("."));
            if(extensinList.contains(extension.toLowerCase()))
            {
                return  extension;
            }
        }


        return "";
    }

    public static String screenshot(Context context, Bitmap bitmap, String name) {
        if (bitmap == null) {
            return null;
        }

        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (name == null || name.trim().isEmpty()) {
            name = String.valueOf(System.currentTimeMillis());
        }
        name = name.trim();

        int count = 0;
        File file = new File(dir, name + SUFFIX_PNG);
        while (file.exists()) {
            count++;
            file = new File(dir, name + "." + count + SUFFIX_PNG);
        }

        try {
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.flush();
            stream.close();
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            return file.getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }

    public static String exportBookmarks(Context context) {
        RecordAction action = new RecordAction(context);
        action.open(false);
        List<Record> list = action.listBookmarks();
        action.close();

        String filename = context.getString(R.string.bookmarks_filename);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename + SUFFIX_HTML);
        int count = 0;
        while (file.exists()) {
            count++;
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename + "." + count + SUFFIX_HTML);
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
            for (Record record : list) {
                String type = BOOKMARK_TYPE;
                type = type.replace(BOOKMARK_TITLE, record.getTitle());
                type = type.replace(BOOKMARK_URL, record.getURL());
                type = type.replace(BOOKMARK_TIME, String.valueOf(record.getTime()));
                writer.write(type);
                writer.newLine();
            }
            writer.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }

    public static String exportWhitelist(Context context) {
        RecordAction action = new RecordAction(context);
        action.open(false);
        List<String> list = action.listDomains();
        action.close();

        String filename = context.getString(R.string.whilelist_filename);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename + SUFFIX_TXT);
        int count = 0;
        while (file.exists()) {
            count++;
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename + "." + count + SUFFIX_TXT);
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
            for (String domain : list) {
                writer.write(domain);
                writer.newLine();
            }
            writer.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }



    public static int importBookmarks(Context context, File file) {
        if (file == null) {
            return -1;
        }

        List<Record> list = new ArrayList<>();

        try {
            RecordAction action = new RecordAction(context);
            action.open(true);

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!((line.startsWith("<dt><a ") && line.endsWith("</a>")) || (line.startsWith("<DT><A ") && line.endsWith("</A>")))) {
                    continue;
                }

                String title = getBookmarkTitle(line);
                String url = getBookmarkURL(line);
                if (title.trim().isEmpty() || url.trim().isEmpty()) {
                    continue;
                }

                Record record = new Record();
                record.setTitle(title);
                record.setURL(url);
                record.setTime(System.currentTimeMillis());
                if (!action.checkBookmark(record)) {
                    list.add(record);
                }
            }
            reader.close();

            Collections.sort(list, new Comparator<Record>() {
                @Override
                public int compare(Record first, Record second) {
                    return first.getTitle().compareTo(second.getTitle());
                }
            });

            for (Record record : list) {
                action.addBookmark(record);
            }
            action.close();
        } catch (Exception e) {}

        return list.size();
    }

    public static int importWhitelist(Context context, File file) {
        if (file == null) {
            return -1;
        }

        AdBlock adBlock = new AdBlock(context);
        int count = 0;

        try {
            RecordAction action = new RecordAction(context);
            action.open(true);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine().trim()) != null) {
                if (!action.checkDomain(line)) {
                    adBlock.addDomain(line);
                    count++;
                }
            }
            reader.close();
            action.close();
        } catch (Exception e) {}

        return count;
    }

    public static void clearBookmarks(Context context) {
        RecordAction action = new RecordAction(context);
        action.open(true);
        action.clearBookmarks();
        action.close();
    }

    public static boolean clearCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }

            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    // CookieManager.removeAllCookies() must be called on a thread with a running Looper.
    public static void clearCookie(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.flush();
            cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean value) {}
            });
        } else {
            CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(context);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();

        }
    }

    public static void clearFormData(Context context) {
        WebViewDatabase.getInstance(context).clearFormData();
    }

    public static void clearHistory(Context context) {
        RecordAction action = new RecordAction(context);
        action.open(true);
        action.clearHistory();
        action.close();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            WebIconDatabase.getInstance().removeAllIcons();
        }
    }

    public static void clearPasswords(Context context) {
        WebViewDatabase.getInstance(context).clearHttpAuthUsernamePassword();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            WebViewDatabase.getInstance(context).clearUsernamePassword();
        }

    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }

        return dir != null && dir.delete();
    }

    private static String getBookmarkTitle(String line) {
        line = line.substring(0, line.length() - 4); // Remove last </a>
        int index = line.lastIndexOf(">");
        return line.substring(index + 1, line.length());
    }

    private static String getBookmarkURL(String line) {
        for (String string : line.split(" +")) {
            if (string.startsWith("href=\"") || string.startsWith("HREF=\"")) {
                return string.substring(6, string.length() - 1); // Remove href=\" and \"
            }
        }

        return "";
    }


}
