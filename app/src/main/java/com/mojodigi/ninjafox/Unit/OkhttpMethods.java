package com.mojodigi.ninjafox.Unit;

import android.content.Context;
import android.util.Log;


import com.mojodigi.ninjafox.SharedPrefs.SharedPreferenceUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OkhttpMethods {

    static String apiResponse;
    static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    static OkHttpClient client = new OkHttpClient();



    public static String CallApiGetNews(Context mContext, String Url) throws Exception {
        Request request = new Request.Builder()
                .url(Url)
                .get().cacheControl(new CacheControl.Builder()
                        .maxAge(0, TimeUnit.SECONDS)
                        .build())
                .build();
        Response response = client.newCall(request).execute();
        SharedPreferenceUtil appPref=new  SharedPreferenceUtil(mContext);

        int code=response.code();

        if(response.isSuccessful())
        {
            appPref.setValue(CommonUtility.API_RESPONSE_CODE_GET_NEWS, code );
            System.out.print("Data -->" + response.toString());
        }
        else
        {
            appPref.setValue(CommonUtility.API_RESPONSE_CODE_GET_NEWS,code);
            return "Error in connection";
        }
        return response.body().string();

    }

    public static String CallCricketApiGetMethodCached(final Context mContext, String Url) throws Exception {


        OkHttpClient client1 = new OkHttpClient.Builder()
                // Enable response caching
                .addInterceptor(new OfflineResponseCacheInterceptor(mContext))
                .addNetworkInterceptor(new ResponseCacheInterceptor())

                // Set the cache location and size (5 MB)
                .cache(new Cache(new File(mContext.getCacheDir(), "cricApiResponses"), 510241024))
                .build();


        //Request request = new Request.Builder().url(Url).get().cacheControl(new CacheControl.Builder().maxAge(0, TimeUnit.SECONDS).build()).build();
        Request request = new Request.Builder().url(Url).get().build();
        Response response = client1.newCall(request).execute();
        SharedPreferenceUtil appPref=new SharedPreferenceUtil(mContext);

        int code=response.code();

        if(response.isSuccessful())
        {
            appPref.setValue(CommonUtility.API_RESPONSE_CODE_GET_NEWS, code );
            System.out.print("Data -->" + response.toString());
        }
        else
        {
            String string = response.body().toString();
            System.out.print(""+string);
            appPref.setValue(CommonUtility.API_RESPONSE_CODE_GET_NEWS,code);
            //return "Error in connection";
        }
        String string=response.body().toString();
        System.out.print(""+string);
        return response.body().string();

    }

    public static String CallApiGetMethodCached(final Context mContext, String Url) throws Exception {


        OkHttpClient client1 = new OkHttpClient.Builder()
                // Enable response caching
                .addInterceptor(new OfflineResponseCacheInterceptor(mContext))
                .addNetworkInterceptor(new ResponseCacheInterceptor())

                // Set the cache location and size (5 MB)
                .cache(new Cache(new File(mContext.getCacheDir(), "apiResponses"), 510241024))
                .build();


       //Request request = new Request.Builder().url(Url).get().cacheControl(new CacheControl.Builder().maxAge(0, TimeUnit.SECONDS).build()).build();
        Request request = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(Url).get().build();
        Response response = client1.newCall(request).execute();
        SharedPreferenceUtil appPref=new SharedPreferenceUtil(mContext);

        int code=response.code();

        if(response.isSuccessful())
        {
            appPref.setValue(CommonUtility.API_RESPONSE_CODE_GET_NEWS, code );
            System.out.print("Data -->" + response.toString());
        }
        else
        {
            String string = response.body().toString();
            System.out.print(""+string);
            appPref.setValue(CommonUtility.API_RESPONSE_CODE_GET_NEWS,code);
            //return "Error in connection";
        }
        String string=response.body().toString();
        System.out.print(""+string);
        return response.body().string();

    }

    public static String CallApiGetMethodForcedCached(final Context mContext, String Url) throws Exception {


        OkHttpClient client1 = new OkHttpClient.Builder()
                // Enable response caching
                .addInterceptor(new OfflineResponseCacheInterceptor(mContext))
                .addNetworkInterceptor(new ResponseCacheInterceptor())

                // Set the cache location and size (5 MB)
                .cache(new Cache(new File(mContext.getCacheDir(), "apiResponses"), 510241024))
                .build();



        //Request request = new Request.Builder().url(Url).get().build();
        Request request = new Request.Builder().cacheControl(CacheControl.FORCE_CACHE).url(Url).get().build();
        Response response = client1.newCall(request).execute();
        SharedPreferenceUtil appPref=new SharedPreferenceUtil(mContext);

        int code=response.code();

        if(response.isSuccessful())
        {
            appPref.setValue(CommonUtility.API_RESPONSE_CODE_GET_NEWS, code );
            System.out.print("Data -->" + response.toString());
        }
        else
        {
            String string = response.body().toString();
            System.out.print(""+string);
            appPref.setValue(CommonUtility.API_RESPONSE_CODE_GET_NEWS,code);
            //return "Error in connection";
        }
        String string=response.body().toString();
        System.out.print(""+string);
        return response.body().string();

    }

    /**
     * Interceptor to cache data and maintain it for 1 minute.
     * If the same network request is sent within a minute,
     * the response is retrieved from cache.
     */
    private static class ResponseCacheInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            okhttp3.Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, max-age=" + 60).removeHeader("Pragma")
                    .build();
        }
    }

    /**
     * Interceptor to cache data and maintain it for four weeks.
     *
     * If the device is offline, stale (at most four weeks old)
     * response is fetched from the cache.
     */
    private static class OfflineResponseCacheInterceptor implements Interceptor {

        Context mContext;
        private OfflineResponseCacheInterceptor(Context mContext)
        {
            this.mContext=mContext;
        }
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!CommonUtility.checkIsOnline(mContext)) {
                request = request.newBuilder()
                        .header("Cache-Control",
                                "public, only-if-cached, max-stale=" + 2419200)
                        .build();
            }
            return chain.proceed(request);
        }
    }


    public static String CallApi(Context mContext, String url, String json) throws IOException
    {
        System.out.print("Data -->" + url);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body).cacheControl(new CacheControl.Builder()
                        .maxAge(0, TimeUnit.SECONDS)
                        .build())
                .build();
        Response response = client.newCall(request).execute();
        SharedPreferenceUtil appPref=new  SharedPreferenceUtil(mContext);
        int code=response.code();
        if(response.isSuccessful())
        {
            appPref.setValue(CommonUtility.API_RESPONSE_CODE,code );
            System.out.print("Data -->" + response.toString());
        }
        else
        {
            appPref.setValue(CommonUtility.API_RESPONSE_CODE,code);
            return "Error in connection";
        }
        return response.body().string();

    }





}
