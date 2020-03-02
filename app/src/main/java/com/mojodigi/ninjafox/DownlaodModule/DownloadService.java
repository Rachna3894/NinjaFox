package com.mojodigi.ninjafox.DownlaodModule;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.mojodigi.ninjafox.Activity.MainActivity;
import com.mojodigi.ninjafox.R;

public class DownloadService extends Service {

    public static final int NOTIFICATION_CHANNEL_ID =  1001 ;
    public static final String NOTIFICATION_CHANNEL_NAME = "Ninjafox";
    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";


    public static final String ACTION_PAUSE_DOWNLOAD = "ACTION_PAUSE_DOWNLOAD";
    public static final String ACTION_CONTINUE_DOWNLOAD = "ACTION_CONTINUE_DOWNLOAD";
    public static final String ACTION_CANCEL_DOWNLOAD = "ACTION_CANCEL_DOWNLOAD";
    private DownloadBinder downloadBinder = new DownloadBinder();

    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        downloadBinder.getDownloadListener().setDownloadService(this);
        return downloadBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        /*createNotificationChannel();


        Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_NAME)
                *//* .setContentTitle(getString(R.string.app_name))*//*
                *//*.setContentText("Your data is downloading and saving to your phone.")*//*
                .setSmallIcon(R.drawable.ic_launcher)
                *//*.setContentIntent(pendingIntent)*//*
                .setContentIntent(null)
                .setAutoCancel(true)
                .build();
        DownloadService.this.startForeground(NOTIFICATION_CHANNEL_ID, notification);*/


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        createNotificationChannel();


        Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_NAME)
                /* .setContentTitle(getString(R.string.app_name))*/
                /*.setContentText("Your data is downloading and saving to your phone.")*/
                .setSmallIcon(R.drawable.ic_launcher)
                /*.setContentIntent(pendingIntent)*/
                .setContentIntent(null)
                .setAutoCancel(true)
                .build();
        DownloadService.this.startForeground(NOTIFICATION_CHANNEL_ID, notification);



        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                if (ACTION_PAUSE_DOWNLOAD.equals(action)) {
                    downloadBinder.pauseDownload();
                    Toast.makeText(getApplicationContext(), "Download is paused", Toast.LENGTH_LONG).show();
                } else if (ACTION_CANCEL_DOWNLOAD.equals(action)) {
                    downloadBinder.cancelDownload();
                    Toast.makeText(getApplicationContext(), "Download is canceled", Toast.LENGTH_LONG).show();
                } else if (ACTION_CONTINUE_DOWNLOAD.equals(action)) {
                    downloadBinder.continueDownload();
                    Toast.makeText(getApplicationContext(), "Download continue", Toast.LENGTH_LONG).show();
                }

            }
            return super.onStartCommand(intent, flags, startId);
        }
        return super.onStartCommand(intent, flags, startId);
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_NAME,
                    getString(R.string.app_name), NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setShowBadge(true);
            notificationChannel.enableVibration(false);
            //notificationChannel.setDescription(getString(R.string.download_dialog_title));
            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //stopForegroundService();
    }


    private void stopForegroundService()
    {
        // Stop foreground service and remove the notification.
        DownloadService.this.stopForeground(true);
        // Stop the foreground service.
        stopSelf();
    }
}
