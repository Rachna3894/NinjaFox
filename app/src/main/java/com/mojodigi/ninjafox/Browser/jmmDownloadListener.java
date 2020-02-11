package com.mojodigi.ninjafox.Browser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;

import com.mojodigi.ninjafox.BroadCast.DownLoadUtility;
import com.mojodigi.ninjafox.DownlaodModule.DownloadBinder;
import com.mojodigi.ninjafox.DownlaodModule.DownloadService;
import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.Unit.BrowserUtility;
import com.mojodigi.ninjafox.Unit.IntentUtility;

import static android.content.Context.BIND_AUTO_CREATE;


public class jmmDownloadListener implements DownloadListener {

    private Context context;
    private DownloadBinder downloadBinder = null;


    public jmmDownloadListener(Context context) {
        super();
        this.context = context;

         //startAndBindDownloadService();
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




    private void startAndBindDownloadService() {
        Intent downloadIntent = new Intent(context, DownloadService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(downloadIntent);
        } else {
            context.startService(downloadIntent);
        }
        context.bindService(downloadIntent, serviceConnection, BIND_AUTO_CREATE);

    }




    @Override
    public void onDownloadStart(final String url, String userAgent, final String contentDisposition, final String mimeType, long contentLength) {
        startAndBindDownloadService();
        // code to download file from link
        final Context holder = IntentUtility.getContext();
        if (holder == null || !(holder instanceof Activity)) {
            //BrowserUtility.download(context, url, contentDisposition, mimeType);
            //DownLoadUtility downLoadUtility=new DownLoadUtility(context);
            //downLoadUtility.download(context, url, contentDisposition, mimeType);
            if(downloadBinder!=null) {
                downloadBinder.startDownload(context,url, 0);
            }
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(holder);
        builder.setCancelable(false);

        builder.setTitle(R.string.dialog_title_download);
        builder.setMessage(URLUtil.guessFileName(url, contentDisposition, mimeType));

        builder.setPositiveButton(R.string.dialog_button_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //BrowserUtility.download(holder, url, contentDisposition, mimeType);
               // DownLoadUtility downLoadUtility=new DownLoadUtility(holder);
                //downLoadUtility.download(holder, url, contentDisposition, mimeType);

                if(downloadBinder!=null) {
                    downloadBinder.startDownload(context,url, 0);
                }
            }
        });

        builder.setNegativeButton(R.string.dialog_button_negative, null);
        builder.create().show();

    }

}
