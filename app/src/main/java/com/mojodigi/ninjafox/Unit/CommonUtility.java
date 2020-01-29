package com.mojodigi.ninjafox.Unit;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.mojodigi.ninjafox.Adapter.NewsAdapter;
import com.mojodigi.ninjafox.AddsUtility.AddMobUtils;
import com.mojodigi.ninjafox.BroadCast.DownLoadUtility;
import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.SharedPrefs.AppConstants;
import com.mojodigi.ninjafox.SharedPrefs.SharedPreferenceUtil;
import com.mojodigi.ninjafox.View.jmmRelativeLayout;
import com.mojodigi.ninjafox.View.jmmToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import okhttp3.CacheControl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.mojodigi.ninjafox.Unit.OkhttpMethods.JSON;

public class CommonUtility {


    public static DownLoadUtility downLoadUtility;
    private static String googleAddId=null;
    public static final String NOT_FOUND="googleId not found";
           public static  int newsCount=0;
    public static int scrollX = 0;
    public static int scrollY = -1;
          public static ArrayList<String> ChekedNewsList = null;

          public static String Album_View_Deleted = "false";

          public static String News_List_Data = "MediaName";
          public static String News_Category_ID = "News_Category_ID";
          public static String News_Category_Name = "News_Category_Name";
          public static String News_Swipe = "News_Swipe";
          public static String News_Offline = "News_Offline";
          public static String Get_News_First = "Get_News_First";
          public static jmmRelativeLayout mHomeLayout  = null;

         public static int MOST_VISITED_REQUEST_CODE=1001;
         public static int MOST_RECENT_REQUEST_CODE=1002;
         public static String INTENT_DATA_KEY="dataKey";


         public static  final String APPMETRICA_KEY="9e61f813-e9e7-46b8-ac9b-1ca0ff7720d7";
         public static final String API_RESPONSE_CODE_GET_NEWS="apiResponseCodeGetNews";
         public static final String PREF_NEWS_LIST = "PrefNewsList";
         public static final String PREF_SOCIAL_LIST = "PrefSocialList";
         public static final String PREF_BLOCK_LIST = "PrefBloclList";
         public static final String GET_NEWS_DATA = "GetNewsData";
         public static final String GET_NEWS_CATEGORIES = "GetNewsCategories";
         public static final String SELECTED_NEWS_CATEGORIES = "PREF_NEWS_CATEGORIES";
         public static final String SELECTED_NEWS_LANGUAGES = "PREF_NEWS_LANGUAGES";
         public static final String CATEGORIES_STRINGS = "CATEGORIES_STRINGS";
         public static final String CATEGORIES_OBJ = "CATEGORIES_OBJ";
         public static String Pref_CategoryId = "prefCategoryId";
         public static String PREF_NEWS_LANGUAGE = "PREF_LANGUAGE";


/*json keys*/

     static final String APP_NAME_POSTFIX= "_JMM";  // will  identify the client like  lava ,carbon etc ;
     static final String VENDOR_ID="JMMNB001";
     static final String key_appName="appName";
     static final String key_packageName="packageName";
     static final String key_appVendorId="appVendorId";
     static final String key_appManufacturer="appManufacturer";
     static final String key_deviceModel="deviceModel";
     static final String key_deviceId="deviceId";
     static final String key_AppVersioName="versionName";
     static final String key_AppversionCode="versionCode";
     static final String key_Locale="locale";
     static final String key_countryCode="countryCode";

    /*json keys*/

    static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    static OkHttpClient client = new OkHttpClient();
    public static final String API_RESPONSE_CODE="apiResponseCode";
    public static final String AutoStartKey="autostartkey";
    public static final String isFcmRegistered="fcm";

    /*add url*/
    public static  final String FB_ADD_BASE_URL="http://development.bdigimedia.com/";
    public static final String FB_ADD_URL=FB_ADD_BASE_URL+"riccha_dev/App-Ad-Mgmt/getAdDetailsByAppName.php";
    /*add url*/

    /*apis*/
    public static final String API_PUSH_NOTIFICATION = "http://onetracky.com/Apps/ninjafox/pushNotifications/setFcmToken.php";
    public static final String API_URL_CRICK_SCORE ="http://cricapi.khulasa-news.com/cricketApi/getLiveScoreForAppV1.php";

    public static final String API_URL_NEWS_DATA ="http://news.khulasa-news.com/server/getNewsByCategory.php";
    public static final String API_URL_SWIPE_DATA ="http://news.khulasa-news.com/server/newsSwipe.php";
    public static final String API_URL_NEWS_BY_PREFRENCE ="http://news.khulasa-news.com/server/getNewsByUserPreference.php";


    public static final String API_URL_HINDI_NEWS_DATA ="http://news.khulasa-news.com/server/hindi/getNewsByCategory.php";
    public static final String API_URL_HINDI_SWIPE_DATA ="http://news.khulasa-news.com/server/hindi/newsSwipe.php";
    public static final String API_URL_HINDI_NEWS_BY_PREFRENCE ="http://news.khulasa-news.com/server/hindi/getNewsByUserPreference.php";
    public static final String API_URL_LANGUGE_NEWS_DATA ="http://news.khulasa-news.com/server/getNewsByCategory.php?language=";

    public static final String APP_NEWS_API_BASE_URL="http://news.khulasa-news.com/server/";

    public static final String API_URL_STATE_NEWS_DATA =APP_NEWS_API_BASE_URL+"getNewsByCategory.php?location=";
    public static final String API_URL_USER_CREDENTIALS_NEWS_DATA =APP_NEWS_API_BASE_URL+"getNewsByCategory.php?";
    public static final String API_URL_LANGUGE_SWIPE_DATA =APP_NEWS_API_BASE_URL+"newsSwipe.php";
    public static final String API_URL_LANGUGE_NEWS_BY_PREFRENCE =APP_NEWS_API_BASE_URL+"getNewsByUserPreference.php";

    public static final String APP_API_BASE_URL="https://khulasa-news.com/ninjafox/";  //live

    //public static final String APP_API_BASE_URL="https://khulasa-news.com/ninjafox/demo/";    //demo

    public static final String loginApi=APP_API_BASE_URL+"login.php";
    public static final String registerApi=APP_API_BASE_URL+"registration.php";
    public static final String API_USER_DEVICE_DETAILS=APP_API_BASE_URL+"InstalledAppDataByUser.php";
    public static final String API_URL_GET_LANGUAGES=APP_API_BASE_URL+"getAllNewsLanguage.php";
    public static final String API_SET_LANGUAGES=APP_API_BASE_URL+"setUserPrefrence.php";
    public static final String API_FORGOT_PASSWORD=APP_API_BASE_URL+"mail/vendor/verifyEmail.php";
    public static final String API_LOG_OUT=APP_API_BASE_URL+"logout.php";
    public static final String API_DELETE_ACCOUNT=APP_API_BASE_URL+"deleteUserProfile.php";

    /*apis*/
    public static Typeface typeFace_Calibri_Bold(Context ctx) {

        return Typeface.createFromAsset(ctx.getAssets(), "calibri_bold.ttf");
    }

    public static Typeface typeFace_Calibri_Regular(Context ctx) {

        return Typeface.createFromAsset(ctx.getAssets(), "calibri_regular.ttf");
    }

    public static boolean checkIsOnline(Context mContext)
    {
        ConnectivityManager ConnectionManager=(ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected())
        {
            return true;

        }
        else
        {
            return false;

        }
    }

    public static String getProgressDisplayLine(long currentBytes, long totalBytes) {
        return getBytesToMBString(currentBytes) + "/" + getBytesToMBString(totalBytes);
    }

    private static String getBytesToMBString(long bytes){
        return String.format(Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00));
    }

    public static String getRootDirPath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = ContextCompat.getExternalFilesDirs(context.getApplicationContext(),
                    null)[0];
            return file.getAbsolutePath();
        } else {
            return context.getApplicationContext().getFilesDir().getAbsolutePath();
        }
    }

    public static JSONObject prepareFcmJsonRequest(Context mContext, String deviceID , String nameOfDevice , String fcm_Token , String appVersion)
    {
        JSONObject object  =  new JSONObject();
        try {
            object.put("deviceId", deviceID);
            object.put("deviceName", nameOfDevice);
            object.put("fcmToken", fcm_Token);
            object.put("appVer", appVersion);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Json request", ""+object.toString());
        return object;
    }


    public static  String passDir=".ninjaFox";
    public static String passwordFile="ninjafox.txt";
    public static  String passwordFileDes="ninjafox.des";
    public static String encryptionPassword="x!23ghjt@#";
    public static String encryptedFilePassword=".des";

    public static boolean isManualPasswordSet()
    {
        // boolean status=false;
        try {
            String path = Environment.getExternalStorageDirectory() + "/" + CommonUtility.passDir+"/"+CommonUtility.passwordFileDes;
            File f = new File(path);
            return f.exists();
        }catch (Exception e)
        {
            return  false;
        }

    }



    public static JSONObject prepareAddJsonRequest(Context mContext)
    {
        String deviceModel="";
        String deviceManufacturer="";
        String deviceId="";
        String versioName="";
        int versionCode=0;
        try
        {
            //get device information
            deviceModel = android.os.Build.MODEL;
            deviceManufacturer = android.os.Build.MANUFACTURER;
            deviceId= Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
            versioName = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
            versionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(),0 ).versionCode;
            System.out.print(""+deviceId);
            //get device information
        }
        catch (Exception e) {
            String str=e.getMessage();
            Log.d("Exception",""+str);
        }


        JSONObject object = new JSONObject();
        try {

            object.put(key_appName, mContext.getString(com.mojodigi.ninjafox.R.string.app_name)+APP_NAME_POSTFIX);
            object.put(key_packageName,mContext.getPackageName());
            object.put(key_appVendorId,VENDOR_ID );
            //device manufacturer;
            object.put(key_appManufacturer, deviceManufacturer);
            object.put(key_deviceModel, deviceModel);
            object.put(key_deviceId, deviceId);
            object.put(key_AppVersioName, versioName);
            object.put(key_AppversionCode, versionCode);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("JsonRequest",object.toString() );
        return object;
    }
static  AddMobUtils addMobUtils;
    public static AddMobUtils getAddMobInstance()
    {
        if(addMobUtils==null) {
            AddMobUtils addMobUtils = new AddMobUtils();
            return addMobUtils;
        }
        else {
            return addMobUtils;
        }

    }

    public static boolean checkAndroidSdkVersion( ) {
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            return true;
        }
        else {
            return false;
        }
    }



    public static boolean deleteDir(File dir) {


        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        }
        else if(dir!= null && dir.isFile())
        {
            return dir.delete();
        } else {
            return false;
        }
    }

    public static  int  createPasswordFile(Context ctx,String userPassword,boolean resetPassWord) {
        try {

            String path = Environment.getExternalStorageDirectory() + "/" + CommonUtility.passDir;

            File f = new File(path);
            /*delete  the old file and in case reset password  */

            if(resetPassWord)
            {
                if(f.exists()) {
                 boolean st= deleteDir(f);
                 System.out.print(""+st);
                }
            }

            if (!f.exists()) {

                if (f.mkdir()) {
                    String cPath=path+"/"+CommonUtility.passwordFile;
                    String data = userPassword;
                    FileOutputStream out = new FileOutputStream(cPath);
                    out.write(data.getBytes());
                    out.close();
                    File file=new File(cPath);

                    if (file.exists())
                    {
                        try {
                            FileInputStream inFile = new FileInputStream(file);
                            FileOutputStream outFile = new FileOutputStream(path+"/"+CommonUtility.passwordFileDes);

                            String password = CommonUtility.encryptionPassword;
                            PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
                            // SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndTripleDES");  //in java
                            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBEWITHSHA256AND256BITAES-CBC-BC");  //in android
                            SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);




                            byte[] salt = new byte[8];
                            Random random = new Random();
                            random.nextBytes(salt);

                            PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, 100);
                            //Cipher cipher = Cipher.getInstance("PBEWithMD5AndTripleDES");  //in java
                            Cipher cipher = Cipher.getInstance("PBEWITHSHA256AND256BITAES-CBC-BC");  // in android
                            cipher.init(Cipher.ENCRYPT_MODE, secretKey, pbeParameterSpec);
                            outFile.write(salt);

                            byte[] input = new byte[64];
                            int bytesRead;
                            while ((bytesRead = inFile.read(input)) != -1) {
                                byte[] output = cipher.update(input, 0, bytesRead);
                                if (output != null)
                                    outFile.write(output);
                            }

                            byte[] output = cipher.doFinal();
                            if (output != null)
                                outFile.write(output);

                            inFile.close();
                            outFile.flush();
                            outFile.close();

                            // delete  the  temporary file;
                            if(file.exists()) {
                                file.delete();
                                CommonUtility.RunMediaScan(ctx,file);
                            }

                        }
                        catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (InvalidKeySpecException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (BadPaddingException e) {
                            e.printStackTrace();
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        } catch (InvalidAlgorithmParameterException e) {
                            e.printStackTrace();
                        } catch (NoSuchPaddingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (IllegalBlockSizeException e) {
                            e.printStackTrace();
                        }

                        return 1;

                    }


                } else {

                    jmmToast.show(ctx, ctx.getResources().getString(R.string.filenotcreated)); // remove this message
                    return 0;
                }
            } else {
                jmmToast.show(ctx, ctx.getResources().getString(R.string.password_create_error));
                return 0;
            }
        }
        catch (Exception e)
        {
            return  0;
        }
        return  0;
    }

    public  static  String getOsDetails()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("android : ").append(Build.VERSION.RELEASE);

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append(" : ").append(fieldName).append(" : ");
                builder.append("sdk=").append(fieldValue);
            }
        }

        return "OS:"+builder.toString();

    }

    public static String getGoogleAddId(final Context mContext)
    {
          final SharedPreferenceUtil mPrefs=new SharedPreferenceUtil(mContext);
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>()
        {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(Void... params) {
                AdvertisingIdClient.Info idInfo = null;
                try {
                    idInfo = AdvertisingIdClient.getAdvertisingIdInfo(mContext);
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String advertId = null;
                try{
                    advertId = idInfo.getId();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

                return advertId;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                googleAddId=s;
                if(s!=null && !s.equalsIgnoreCase(""))
                {
                    mPrefs.setStringValue(AppConstants.PREFS_GOOGLE_ADD_ID, s);
                }
            }
        };
        task.execute();

        if(googleAddId!=null)
            return googleAddId;
        else  return
                NOT_FOUND;


    }


    public static boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
    public static String enCodeString(String string)
    {
        byte[] encodeValue = string.getBytes();
        String Base64encodeString=android.util.Base64.encodeToString(encodeValue, android.util.Base64.DEFAULT);

        return Base64encodeString;

    }
    public static boolean isLoggedInUser(SharedPreferenceUtil  mPrefs) {

        if(mPrefs.getStringValue(AppConstants.PREFS_USER_ID, "0").equalsIgnoreCase("0") && mPrefs.getStringValue(AppConstants.PREFS_TOKEN, "0").equalsIgnoreCase("0"))
            return false;
        return true;

    }
    public static ArrayList<String> getInstalledApps(Context mContext) {
        ArrayList<String> appsList = new ArrayList<String>();
        List<PackageInfo> packs = mContext.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((isSystemPackage(p) == false)) {
                String appName = p.applicationInfo.loadLabel(mContext.getPackageManager()).toString();
                appsList.add(appName.toLowerCase());
            }
        }
        return appsList;
    }
    private static boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
    }

    public static String getString(Context mContext,int resourceId)
    {
        return mContext.getResources().getString(resourceId);
    }

    public static  String readPasswordFile()
    {
        String path = Environment.getExternalStorageDirectory() + "/" + CommonUtility.passDir+"/"+CommonUtility.passwordFileDes;
        try {
            String password = CommonUtility.encryptionPassword;
            PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
            // SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndTripleDES");  //in java
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBEWITHSHA256AND256BITAES-CBC-BC");  //in android
            SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);
            File inputFile=new File(path);
            if(inputFile.exists())
            {
                FileInputStream fis = new FileInputStream(inputFile);

                byte[] salt = new byte[8];
                fis.read(salt);

                PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, 100);

                //Cipher cipher = Cipher.getInstance("PBEWithMD5AndTripleDES");  //in java
                Cipher cipher = Cipher.getInstance("PBEWITHSHA256AND256BITAES-CBC-BC");  // in android
                cipher.init(Cipher.DECRYPT_MODE, secretKey, pbeParameterSpec);

                //FileOutputStream fos = new FileOutputStream(outputFile);
                // FileOutputStream fos = new FileOutputStream("G:\\EncryptTest\\image\\Takendra_decrypted.jpg");
                byte[] in = new byte[64];
                int read;
                while ((read = fis.read(in)) != -1) {
                    byte[] output = cipher.update(in, 0, read);
                    // if (output != null)
                    // fos.write(output);
                }

                byte[] output = cipher.doFinal();
                if (output != null) {
                    // fos.write(output);
                    String s = new String(output);
                    return s;
                }

                fis.close();
                // fos.flush();
                //fos.close();
                // Utility.RunMediaScan(ctx,outputFile);
            }

        }catch (InvalidKeyException e)
        {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }



        return  "";
    }

    // the scan usually  runs  when device is restarted or sdcard is  plugged in
    public static void RunMediaScan(Context context, File fileName) {
        MediaScannerConnection.scanFile(
                context, new String[]{fileName.getPath()}, null,
                new MediaScannerConnection.MediaScannerConnectionClient() {
                    @Override
                    public void onMediaScannerConnected() {
                        System.out.println("acn connected");
                    }

                    @Override
                    public void onScanCompleted(String path, Uri uri) {

                        System.out.println("scan completed");
                    }
                });
    }


    public static boolean IsNotEmpty(EditText view) {
        if (view.getText().length() > 0)
            return true;
        else
            return false;

    }



    public static String LongToDate(Long date) {
        Date Date = new Date(date);
// SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(Date);
        return formattedDate;
    }



    public static synchronized void saveFileTocache(String json , Context mContext) {
       // File file =new File(mContext.getCacheDir()+"/"+"cachedata.txt");
        File file =new File(Environment.getExternalStorageDirectory()+"/"+"cachedata.txt");
        try {
            //jmmToast.show(mContext, ""+json);
            if (file.createNewFile())
            {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(json.getBytes());
                fos.flush();
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        readCacheData(mContext);
    }


    public static synchronized String readCacheData(Context mContext)
    {
        File file =new File(Environment.getExternalStorageDirectory()+"/"+"cachedata.txt");
        if(file.exists())
        {
            BufferedReader br = null;
            try {
                FileInputStream fis = new FileInputStream(file);
                byte[] data = new byte[(int) file.length()];
                fis.read(data);
                fis.close();
                String str = new String(data, "UTF-8");
                Log.d("fileData", str);
                 jmmToast.show(mContext, ""+str);
                return str;
            }
            catch (Exception e)
            {
            }
        }
        return null;
    }

    public static boolean checkOrCreateDownloadDirectory(Context mContext) {

        //File file =  new File(Environment.getExternalStorageDirectory()+"/"+AppConstants.downloadDirectory);
        File file =  new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+AppConstants.downloadDirectory);
        if(file.exists())
            return  true;
        else
        {
            return  file.mkdir();
        }
    }

    public static String getCountryDialCode(Context mContext){
        String contryId = null;
        String contryDialCode = null;

        TelephonyManager telephonyMngr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);

         contryId = telephonyMngr.getSimCountryIso().toUpperCase();
        String[] arrContryCode=mContext.getResources().getStringArray(R.array.DialingCountryCode);
        for(int i=0; i<arrContryCode.length; i++){
            String[] arrDial = arrContryCode[i].split(",");
            if(arrDial[1].trim().equals(contryId.trim())){
                contryDialCode = arrDial[0];
                break;
            }
        }
        if(contryDialCode!=null)
        return "+"+contryDialCode;
        else
            return "+91";
    }
}
