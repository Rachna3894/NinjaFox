package com.mojodigi.ninjafox.Task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.mojodigi.ninjafox.SharedPrefs.SharedPreferenceUtil;
import com.mojodigi.ninjafox.Unit.CommonUtility;
import com.mojodigi.ninjafox.Unit.JsonParser;
import com.mojodigi.ninjafox.Unit.OkhttpMethods;
import org.json.JSONException;
import org.json.JSONObject;


public class WebCallNewsByPreference extends AsyncTask<String, String, String> {

    private Context mContext;
    private SharedPreferenceUtil addprefs;
    private NewsByPreferenceListener listener;
    private String jsonObject ;

    public WebCallNewsByPreference(Context mContext, NewsByPreferenceListener listener , String jsonObject) {
        this.mContext = mContext;
        this.listener = listener;
        this.jsonObject = jsonObject;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        addprefs = new SharedPreferenceUtil(mContext);
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
          // return OkhttpMethods.CallApi(mContext, CommonUtility.API_URL_NEWS_BY_PREFRENCE, jsonObject); // English news
            //return OkhttpMethods.CallApi(mContext, CommonUtility.API_URL_HINDI_NEWS_BY_PREFRENCE, jsonObject); // Hindi news
            return OkhttpMethods.CallApi(mContext, CommonUtility.API_URL_LANGUGE_NEWS_BY_PREFRENCE, jsonObject); // Language news

        } catch (Exception ex) {
            ex.printStackTrace();
            return "" + ex.getMessage();
        }
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //Log.e("JsonResponse", s);

        try {
            int responseCode = addprefs.getIntValue(CommonUtility.API_RESPONSE_CODE_GET_NEWS, 0);
            if (s != null && responseCode == 200) {
                JSONObject mainJson = new JSONObject(s);

                if (mainJson.has("status")) {
                    String status = JsonParser.getkeyValue_Str(mainJson, "status");
                    String message = JsonParser.getkeyValue_Str(mainJson, "message");

                    if (status.equalsIgnoreCase("true") && message.equalsIgnoreCase("Success")  ) {
                        listener.onNewsByPreferenceListener(mainJson);
                    } else {
                        listener.onNewsByPreferenceListener(null);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSONException" ,"Internet is slow.");
        }
    }


    public interface NewsByPreferenceListener {
          void onNewsByPreferenceListener(JSONObject obj);
    }
}