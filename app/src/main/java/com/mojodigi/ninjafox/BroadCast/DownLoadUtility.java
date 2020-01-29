package com.mojodigi.ninjafox.BroadCast;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.webkit.URLUtil;
import android.widget.RemoteViews;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.SharedPrefs.AppConstants;
import com.mojodigi.ninjafox.Unit.BrowserUtility;
import com.mojodigi.ninjafox.Unit.CommonUtility;
import com.mojodigi.ninjafox.View.jmmToast;

import java.io.Serializable;

import static com.mojodigi.ninjafox.Unit.BrowserUtility.checkdownloadSettings;
import static com.mojodigi.ninjafox.Unit.BrowserUtility.getFileName;

public class DownLoadUtility implements DownloadReceiver.downlaodListener {


    Context mContext;
    Thread downloadThread;
    public static final String NOTIFICATION_CHANNEL_ID = "1001";
    public static final String NOTIFICATION_CHANNEL_NAME = "Ninjafox";
    private int downloadId;

    public DownLoadUtility(Context mContext)
    {
        this.mContext=mContext;
    }


    public  void download(final Context context, final String url, String contentDisposition, String mimeType)
    {

        final int notifyID=101;
        try
        {

            boolean st = checkdownloadSettings(context);
            System.out.print("" + st);
            if (checkdownloadSettings(context))   // if download settings return false terminate  the downlaod ;
            {
                return;
            }

            boolean dstatus = CommonUtility.checkOrCreateDownloadDirectory(context);

            if (dstatus) {

                String filename = URLUtil.guessFileName(url, contentDisposition, mimeType); // Maybe unexpected filename.

                if (filename.equalsIgnoreCase("") || filename == null || filename.contains(".bin") || filename.length() < 3) {
                    filename = getFileName() + BrowserUtility.getFileExtension(url);
                }
                final RemoteViews collapsedView = new RemoteViews(context.getPackageName(), R.layout.notification_small);
                final RemoteViews strechedView = new RemoteViews(context.getPackageName(), R.layout.notification_large);





                final String finalFilename = filename;

                String dpath=CommonUtility.getRootDirPath(context);
                System.out.print(""+dpath);

                // int downloadId = PRDownloader.download(url, Environment.DIRECTORY_DOWNLOADS+"/"+AppConstants.downloadDirectory+"/", filename)

                String pth= Environment.DIRECTORY_DOWNLOADS+"/"+ AppConstants.downloadDirectory+"/";
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


                Intent pauseInetnt = new Intent(DownloadReceiver.ACTION_PAUSE);
                CommonUtility.downLoadUtility=this;
                final PendingIntent pausePendingInetnt = PendingIntent.getBroadcast(context, 0, pauseInetnt, 0);


                Intent cancelIntent = new Intent(DownloadReceiver.ACTION_CANCEL);
                CommonUtility.downLoadUtility=this;
                final PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, 0, cancelIntent, 0);


                strechedView.setOnClickPendingIntent(R.id.pauseDownload, pausePendingInetnt);
                strechedView.setOnClickPendingIntent(R.id.cancelDownload, cancelPendingIntent);


                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
                    //notificationChannel.enableLights(true);
                    //notificationChannel.setLightColor(Color.RED);
                    //notificationChannel.enableVibration(true);
                    //notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    assert mNotificationManager != null;
                    mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                    mNotificationManager.createNotificationChannel(notificationChannel);
                }

                //assert mNotificationManager != null;
                //mNotificationManager.notify(notifyID, mBuilder.build());

                        // generate notification

                final String finalFilename1 = filename;



                downloadThread=new Thread(new Runnable() {
                   @Override
                   public void run() {

                        downloadId = PRDownloader.download(url, Environment.getExternalStorageDirectory()+"/", finalFilename1).build()
                               .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                                   @Override
                                   public void onStartOrResume() {


                                       assert mNotificationManager != null;
                                       mNotificationManager.notify(notifyID  , mBuilder.build());



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

                                       Thread.currentThread().interrupt();
                                       mNotificationManager.cancel(notifyID);

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
                                       mNotificationManager.notify(notifyID   , mBuilder.build());



                                      /* try {
                                           Thread.sleep(1000);
                                       } catch (InterruptedException e) {
                                           e.printStackTrace();
                                       }*/



                                   }
                               })
                               .start(new OnDownloadListener() {
                                   @Override
                                   public void onDownloadComplete() {

                                       jmmToast.show(context, "download complete");
                                       collapsedView.setTextViewText(R.id.statusText,"download complete");
                                       strechedView.setTextViewText(R.id.statusText, "download complete");
                                       mNotificationManager.notify(notifyID , mBuilder.build());



                                   }

                                   @Override
                                   public void onError(Error error) {
                                       String errMsg=error.getServerErrorMessage();
                                       jmmToast.show(context, errMsg);

                                       Thread.currentThread().interrupt();
                                   }


                               });

                   }
               });

                downloadThread.start();

            }

        }
        catch (Exception e)
        {
            String string=e.getMessage();
            System.out.print(""+string);
        }
}


    @Override
    public void paused_Downlaod() {

        jmmToast.show(mContext, "pause");
    }

    @Override
    public void resume_Downlaod() {

        jmmToast.show(mContext, "resume");
    }

    @Override
    public void cancel_Downlaod() {

        jmmToast.show(mContext, "cancel");
        downloadThread.interrupt();
        if(downloadId!=0)
        PRDownloader.cancel(downloadId);
    }

}
