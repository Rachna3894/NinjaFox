package com.mojodigi.ninjafox.Task;

import android.content.Context;
import android.media.audiofx.AudioEffect;
import android.os.AsyncTask;
import android.util.Log;

import com.mojodigi.ninjafox.SharedPrefs.AppConstants;
import com.mojodigi.ninjafox.SharedPrefs.SharedPreferenceUtil;
import com.mojodigi.ninjafox.Unit.CommonUtility;
import com.mojodigi.ninjafox.Unit.JsonParser;
import com.mojodigi.ninjafox.Unit.OkhttpMethods;

import org.json.JSONException;
import org.json.JSONObject;



public class WebCallGetNewsTask extends AsyncTask<String, String, String> {

    private Context mContext;
    private SharedPreferenceUtil addprefs;
    //public static ArrayList<NewsDataModel> newsList;
    NewsDataJsonListener listener;
    boolean forceCacheResponse;


    public WebCallGetNewsTask(Context mContext,    boolean forceCacheResponse, NewsDataJsonListener listener) {
        this.mContext = mContext;
        this.forceCacheResponse=forceCacheResponse;
        this.listener = listener;
    }

    /*not needed now as the  language prefrenece will  be decided from server end */
   /* public WebCallGetNewsTask(Context mContext, NewsDataJsonListener listener , String language) {
        this.mContext = mContext;
        this.listener = listener;
        this.prefNewsLanguage = language;
    }*/


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        addprefs = new SharedPreferenceUtil(mContext);
    }


    @Override
    protected String doInBackground(String... strings) {
        try {
            if(CommonUtility.isLoggedInUser(addprefs)) {
                 String userCredentialis= "userId="+addprefs.getStringValue(AppConstants.PREFS_USER_ID, "0")+"&token="+addprefs.getStringValue(AppConstants.PREFS_TOKEN, "0");
                 if(forceCacheResponse)
                    return OkhttpMethods.CallApiGetMethodForcedCached(mContext, CommonUtility.API_URL_USER_CREDENTIALS_NEWS_DATA + userCredentialis); //language news with Cache
                 else
                    return OkhttpMethods.CallApiGetMethodCached(mContext, CommonUtility.API_URL_USER_CREDENTIALS_NEWS_DATA + userCredentialis); //language news with Cache
            }
            else {
                  String location=addprefs.getStringValue(AppConstants.PREFS_LOCATION_STATE, "delhi");
                   if(forceCacheResponse)
                    return OkhttpMethods.CallApiGetMethodForcedCached(mContext, CommonUtility.API_URL_STATE_NEWS_DATA + location);
                   else
                     return OkhttpMethods.CallApiGetMethodCached(mContext, CommonUtility.API_URL_STATE_NEWS_DATA + location);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "" + e.getMessage();
        }
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("JsonResponse", s);
        try {
            int responseCode = addprefs.getIntValue(CommonUtility.API_RESPONSE_CODE_GET_NEWS, 0);
            if (s != null && responseCode == 200) {
                JSONObject mainJson = new JSONObject(s);
                //JSONObject mainJson = new JSONObject(AppConstants.dummyRespose);
                if (mainJson.has("status")) {
                    String status = JsonParser.getkeyValue_Str(mainJson, "status");
                    String message = JsonParser.getkeyValue_Str(mainJson, "message");
                    if (status.equalsIgnoreCase("true") && message.equalsIgnoreCase("Success")  ) {
                        listener.onNewsDataJsonListener(mainJson , s);
                    } else {
                        listener.onNewsDataJsonListener(null ,"");
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSONException" ,"Internet is slow.");
        }
    }


    public interface NewsDataJsonListener {
        public void onNewsDataJsonListener(JSONObject obj , String s);
    }


}