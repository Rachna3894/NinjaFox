package com.mojodigi.ninjafox.Application;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.webkit.WebView;

import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.facebook.ads.AudienceNetworkAds;
import com.mojodigi.ninjafox.Unit.CommonUtility;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

public class MyApplication  extends android.support.multidex.MultiDexApplication   {

    @Override
    public void onCreate() {
        super.onCreate();


        PRDownloaderConfig dconfig = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(this, dconfig);

        //App mertrica sdk

        // Creating an extended library configuration.

        //Api key in appMetrica Dashboard settings  is tracking key--

        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder(CommonUtility.APPMETRICA_KEY).build();
        // Initializing the AppMetrica SDK.

        YandexMetrica.activate(getApplicationContext(), config);   // uncommnet  it
        // Automatic tracking of user activity.
        YandexMetrica.enableActivityAutoTracking(this);   // uncommnet  it

        //App mertrica sdk

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            String processName = getProcessName(this);
            Log.d("ProcessName", processName);
            if (!"com.mojodigi.ninjafox".equals(processName)) {
                WebView.setDataDirectorySuffix(processName);
            }

        }



        //for fb adds
        //https://developers.facebook.com/docs/audience-network/reference/android/com/facebook/ads/audiencenetworkads.html/
        AudienceNetworkAds.initialize(getApplicationContext());
        AudienceNetworkAds.isInAdsProcess(getApplicationContext());


    }

    public String getProcessName(Context context) {
        if (context == null) return null;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == android.os.Process.myPid()) {
                return processInfo.processName;
            }
        }
        return null;
    }
}
