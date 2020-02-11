package com.mojodigi.ninjafox.BroadCast;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.webkit.URLUtil;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.SharedPrefs.AppConstants;
import com.mojodigi.ninjafox.Unit.BrowserUtility;
import com.mojodigi.ninjafox.Unit.CommonUtility;

public class DownloadReceiver extends BroadcastReceiver {
    public static final String ACTION_PAUSE = "ACTION_PAUSE";
    public static final String ACTION_CANCEL = "ACTION_CANCEL";
    public static final String ACTION_DOWANLOAD = "ACTION_DOWANLOAD";
    public static final String ACTION_RESUME = "ACTION_RESUME";

    downlaodListener listener;

    @Override
    public void onReceive(Context context, Intent intent) {
        BrowserUtility utility=new BrowserUtility(context);
        String action = intent.getAction();
        assert action != null;
        String toastStr = "you touch";
        if (action.equals(ACTION_DOWANLOAD)) {
            //download......
            Toast.makeText(context, toastStr + "downlaod", Toast.LENGTH_SHORT).show();



        } else if (action.equals(ACTION_CANCEL)) {
            //cancle download......

            if(CommonUtility.downLoadUtility!=null)
                CommonUtility.downLoadUtility.cancel_Downlaod();

        }
        else if(action.equals(ACTION_PAUSE))
        {
            if(CommonUtility.downLoadUtility!=null)
                CommonUtility.downLoadUtility.paused_Downlaod();
        }

        else if(action.equals(ACTION_RESUME))
        {
            Toast.makeText(context, toastStr + "resume", Toast.LENGTH_SHORT).show();

        }
    }
    public interface downlaodListener
    {
        void paused_Downlaod();
        void resume_Downlaod();
        void cancel_Downlaod();
    }

}