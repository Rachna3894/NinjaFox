package com.mojodigi.ninjafox.Task;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.mojodigi.ninjafox.SharedPrefs.AppConstants;
import com.mojodigi.ninjafox.SharedPrefs.SharedPreferenceUtil;
import com.mojodigi.ninjafox.Unit.OkhttpMethods;
import com.mojodigi.ninjafox.View.CustomProgressDialog;


public class ApiRequestTask extends AsyncTask<String,String,String>{

    private Context mContext;

    private SharedPreferenceUtil prefs;

    JsonLoadListener listener;
    boolean isGetmethod;
    String url;
    String postJson;
    boolean dispProgress;
    String msg;
    int mRequestCode;/*determine  the  request api  on activity  in onJsonLoad function*/
    public  interface JsonLoadListener
    {
       void onJsonLoad(String json, int mRequestCode);
       void onLoadFailed(String msg);
    }

    public ApiRequestTask(Context mContext, JsonLoadListener listener, String url , boolean isGetMethod, boolean dispProgress, String msg, String postJson, int mRequestCode) {
        this.mContext = mContext;
        this.listener=listener;
        this.isGetmethod=isGetMethod;
        this.url=url;
        this.postJson=postJson;
        this.mRequestCode=mRequestCode;
        this.msg=msg;
        this.dispProgress=dispProgress;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        prefs = new SharedPreferenceUtil(mContext);
        if(dispProgress && msg!=null) {

            CustomProgressDialog.show(mContext, msg);
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            if(isGetmethod)
            //return OkhttpMethods.CallApiGetMethod(mContext,url);
            return OkhttpMethods.CallApiGetMethodCached(mContext,url);
            else
            {
                return  OkhttpMethods.CallApi(mContext, url, postJson);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ""+e.getMessage();
        }
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //Log.e("JsonResponse", s);

        if (prefs != null) {
            int responseCode = prefs.getIntValue(AppConstants.API_RESPONSE_CODE, 0);
            if (s != null && responseCode == 200) {
                listener.onJsonLoad(s,mRequestCode);
            }
            else
            {
                listener.onLoadFailed("Code "+responseCode);
            }


            try {
                if (dispProgress && msg != null) {
                    CustomProgressDialog.dismiss();
                }
            }catch (Exception e){}

        }
    }
}