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

public class WebCallSwipeDataTask extends AsyncTask<String, String, String> {

    private Context mContext;
    private SharedPreferenceUtil addprefs;
    private SwipeDataJsonListener listener;
    private String swipeObject ;

    public WebCallSwipeDataTask(Context mContext, SwipeDataJsonListener listener) {
        this.mContext = mContext;
        this.listener = listener;
    }

    public WebCallSwipeDataTask(Context mContext, SwipeDataJsonListener listener , String swipeObject) {
        this.mContext = mContext;
        this.listener = listener;
        this.swipeObject = swipeObject;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        addprefs = new SharedPreferenceUtil(mContext);
    }


    @Override
    protected String doInBackground(String... strings) {
        try {
            return OkhttpMethods.CallApi(mContext, CommonUtility.API_URL_LANGUGE_SWIPE_DATA, swipeObject);
        } catch (Exception e) {
            e.printStackTrace();
            return "" + e.getMessage();
        }
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("WebCallSwipeDataTask", s);
        try {
            int responseCode = addprefs.getIntValue(CommonUtility.API_RESPONSE_CODE_GET_NEWS, 0);
            if (s != null && responseCode == 200) {
                JSONObject mainJson = new JSONObject(s);
                if (mainJson.has("status")) {
                    String status = JsonParser.getkeyValue_Str(mainJson, "status");
                    String message = JsonParser.getkeyValue_Str(mainJson, "message");

                    if (status.equalsIgnoreCase("true") && message.equalsIgnoreCase("Success")  ) {
                        listener.onSwipeDataJsonListener(mainJson);
                    }else if (status.equalsIgnoreCase("false")) {
                        listener.onSwipeDataJsonListener(mainJson);
                    }
                    else {
                        listener.onSwipeDataJsonListener(null);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public interface SwipeDataJsonListener {
          void onSwipeDataJsonListener(JSONObject obj);
    }
}