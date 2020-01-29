package com.mojodigi.ninjafox.DownlaodModule;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;


import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.SharedPrefs.AppConstants;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.support.v4.app.NotificationCompat.DEFAULT_SOUND;
import static android.support.v4.app.NotificationCompat.DEFAULT_VIBRATE;

/**
 * Created by Jerry on 3/9/2018.
 */

public class DownloadListener {

    private DownloadService downloadService = null;

    private int lastProgress = 0;

    public void setDownloadService(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    public DownloadService getDownloadService() {
        return downloadService;
    }

    public void onSuccess()
    {
        downloadService.stopForeground(true);
        sendDownloadNotification("Download success.", -1);
    }

    public void onFailed()
    {
        downloadService.stopForeground(true);
        sendDownloadNotification("Download failed.", -1);
    }
    public void onPaused()
    {
        sendDownloadNotification("Download paused.", lastProgress);
    }
    public void onCanceled()
    {
        downloadService.stopForeground(true);
        sendDownloadNotification("Download canceled.", -1);
    }

    public void onUpdateDownloadProgress(int progress)
    {
        try {
            lastProgress = progress;
            sendDownloadNotification("Downloading...", progress);

            // Thread sleep 0.2 seconds to let Pause, Continue and Cancel button in notification clickable.
            Thread.sleep(200);
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public static final String NOTIFICATION_CHANNEL_ID = "1001";
    public static final String NOTIFICATION_CHANNEL_NAME = "Ninjafox";

    public void sendDownloadNotification(String title, int progress)
    {
        NotificationCompat.Builder notification = getDownloadNotification(title, progress);

        NotificationManager notificationManager = (NotificationManager)downloadService.getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
            //notificationChannel.enableLights(true);
            //notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(false);
            //notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            //notificationChannel.setVibrationPattern(new long[]{0L});
            assert notificationManager != null;
            notification.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(AppConstants.NOTIFICATION_ID, notification.build());

    }

    public  NotificationCompat.Builder getDownloadNotification(String title, int progress)
    {
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(downloadService, 0, intent, 0);

        NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(downloadService);
        mBuilder.setSmallIcon(R.drawable.ic_launcher);

        Bitmap bitmap = BitmapFactory.decodeResource(downloadService.getResources(), android.R.drawable.stat_sys_download);
        mBuilder.setLargeIcon(bitmap);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setContentTitle(title);
        mBuilder.setFullScreenIntent(pendingIntent, true);

        if(progress > 0 && progress < 100)
        {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("Download progress ");
            stringBuffer.append(progress);
            stringBuffer.append("%");

            mBuilder.setContentText("Download progress " + progress + "%");

            mBuilder.setProgress(100, progress, false);

            // Add Pause download button intent in notification.

            if(title.equalsIgnoreCase("Downloading...") || title.equalsIgnoreCase("Continue")) {
                Intent pauseDownloadIntent = new Intent(getDownloadService(), DownloadService.class);
                pauseDownloadIntent.setAction(DownloadService.ACTION_PAUSE_DOWNLOAD);
                PendingIntent pauseDownloadPendingIntent = PendingIntent.getService(getDownloadService(), 0, pauseDownloadIntent, 0);
                NotificationCompat.Action pauseDownloadAction = new NotificationCompat.Action(android.R.drawable.ic_media_pause, "Pause", pauseDownloadPendingIntent);
                mBuilder.addAction(pauseDownloadAction);
            }
            // Add Continue download button intent in notification.
            if(title.equalsIgnoreCase("Download paused.") ) {
                Intent continueDownloadIntent = new Intent(getDownloadService(), DownloadService.class);
                continueDownloadIntent.setAction(DownloadService.ACTION_CONTINUE_DOWNLOAD);
                PendingIntent continueDownloadPendingIntent = PendingIntent.getService(getDownloadService(), 0, continueDownloadIntent, 0);
                NotificationCompat.Action continueDownloadAction = new NotificationCompat.Action(android.R.drawable.ic_media_pause, "Continue", continueDownloadPendingIntent);
                mBuilder.addAction(continueDownloadAction);
            }

            // Add Cancel download button intent in notification.
            Intent cancelDownloadIntent = new Intent(getDownloadService(), DownloadService.class);
            cancelDownloadIntent.setAction(DownloadService.ACTION_CANCEL_DOWNLOAD);
            PendingIntent cancelDownloadPendingIntent = PendingIntent.getService(getDownloadService(), 0, cancelDownloadIntent, 0);
            NotificationCompat.Action cancelDownloadAction = new NotificationCompat.Action(android.R.drawable.ic_delete, "Cancel", cancelDownloadPendingIntent);
            mBuilder.addAction(cancelDownloadAction);

        }

        NotificationCompat.Builder notification = mBuilder;

        return notification;
    }

}