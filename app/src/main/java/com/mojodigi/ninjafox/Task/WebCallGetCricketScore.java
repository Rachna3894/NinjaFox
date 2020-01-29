package com.mojodigi.ninjafox.Task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.mojodigi.ninjafox.Model.NewsDataModel;
import com.mojodigi.ninjafox.SharedPrefs.SharedPreferenceUtil;
import com.mojodigi.ninjafox.Unit.CommonUtility;
import com.mojodigi.ninjafox.Unit.JsonParser;
import com.mojodigi.ninjafox.Unit.OkhttpMethods;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class WebCallGetCricketScore extends AsyncTask<String,String,String>{

    private Context mContext;

    private SharedPreferenceUtil addprefs;

    public static ArrayList<NewsDataModel> newsList;
    cricketJsonListener listener;

    public WebCallGetCricketScore(Context mContext, cricketJsonListener listener) {
        this.mContext = mContext;
        this.listener=listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        addprefs = new SharedPreferenceUtil(mContext);
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            //return OkhttpMethods.CallApiGetNews(mContext, CommonUtility.API_URL_CRICK_SCORE );
            return OkhttpMethods.CallCricketApiGetMethodCached(mContext, CommonUtility.API_URL_CRICK_SCORE);

        } catch (Exception e) {
            e.printStackTrace();
            return ""+e.getMessage();
        }
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("JsonResponse", s);

        try {
            int responseCode = addprefs.getIntValue(CommonUtility.API_RESPONSE_CODE_GET_NEWS, 0);
            if (s != null && responseCode == 200) {
                //
                JSONObject mainJson = new JSONObject(s);
                if (mainJson.has("status")) {
                    String status = JsonParser.getkeyValue_Str(mainJson, "status");
                    String message = JsonParser.getkeyValue_Str(mainJson, "message");
                    String isMatchBeingPlayed = JsonParser.getkeyValue_Str(mainJson, "isMatchBeingPlayed");

                    if(status.equalsIgnoreCase("true") && message.equalsIgnoreCase("Success") &&  isMatchBeingPlayed.equalsIgnoreCase("true") )
                    {
                      listener.cricketScorejson(mainJson);
                    }
                    else
                    {
                        listener.cricketScorejson(null);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public  interface  cricketJsonListener
    {
        public void  cricketScorejson(JSONObject obj);
    }
}