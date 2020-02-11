package com.mojodigi.ninjafox.DownlaodModule;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.mojodigi.ninjafox.SharedPrefs.AppConstants;

import static android.content.Context.BIND_AUTO_CREATE;
import static android.content.Context.NOTIFICATION_SERVICE;


public class DownloadBinder extends Binder {

    private com.mojodigi.ninjafox.DownlaodModule.DownloadManager downloadManager = null;

    private DownloadListener downloadListener = null;


    public static final String NOTIFICATION_CHANNEL_ID = "1001";
    public static final String NOTIFICATION_CHANNEL_NAME = "Ninjafox";

    private String currDownloadUrl = "";

    Context mContext;

    private DownloadBinder downloadBinder = null;

    public DownloadManager getDownloadManager() {
        return downloadManager;
    }

    public DownloadBinder() {

        if(downloadListener == null)
        {
            downloadListener = new DownloadListener();
        }


    }




    public void startDownload(Context mContext,String downloadUrl, int progress)
    {

        /* Because downloadManager is a subclass of AsyncTask, and AsyncTask can only be executed once,
         * So each download need a new downloadManager. */
        downloadManager = new DownloadManager(downloadListener);

        /* Because DownloadUtil has a static variable of downloadManger, so each download need to use new downloadManager. */

        DownloadUtil.setDownloadManager(downloadManager);

        // Execute download manager, this will invoke downloadManager's doInBackground() method.
        downloadManager.execute(downloadUrl);

        // Save current download file url.
        currDownloadUrl = downloadUrl;

        // Create and start foreground service with notification.
        NotificationCompat.Builder notification = downloadListener.getDownloadNotification("Downloading...", progress);
        NotificationManager notificationManager = (NotificationManager)mContext.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(AppConstants.NOTIFICATION_ID);
        String str = "NinjaFox";
        this.mContext=mContext;
        startChannel(mContext, notification,  notificationManager,   str);
        downloadListener.getDownloadService().startForeground(1, notification.build());
    }
    /* startChannel */
    public static void startChannel(Context context, NotificationCompat.Builder  mBuilder,NotificationManager mNotificationManager, String str) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
            //notificationChannel.enableLights(true);
            //notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(false);
            //notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

    }

    public void continueDownload()
    {
        if(currDownloadUrl != null && !TextUtils.isEmpty(currDownloadUrl))
        {
            int lastDownloadProgress = downloadManager.getLastDownloadProgress();
            if(mContext!=null)

            startAndBindDownloadService();

            startDownload(mContext, currDownloadUrl, lastDownloadProgress);

        }
    }

    private synchronized void startAndBindDownloadService() {

        Intent downloadIntent = new Intent(mContext, DownloadService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mContext.startForegroundService(downloadIntent);
        } else {
            mContext.startService(downloadIntent);
        }
        mContext.bindService(downloadIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };




    public void cancelDownload()
    {
        if(downloadManager!=null) {
            downloadManager.cancelDownload();
        }
    }

    public void pauseDownload()
    {
        if(downloadManager!=null) {
            downloadManager.pauseDownload();
        }
    }

    public DownloadListener getDownloadListener() {
        return downloadListener;
    }
}