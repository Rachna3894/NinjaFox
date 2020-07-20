package com.mojodigi.ninjafox.Activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.auth.api.credentials.CredentialRequest;
import com.google.android.gms.auth.api.credentials.CredentialsClient;
import com.google.android.gms.auth.api.credentials.IdentityProviders;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mojodigi.ninjafox.Adapter.CrickScoreAdapter;
import com.mojodigi.ninjafox.Adapter.NewsViewPagerAdapter;
import com.mojodigi.ninjafox.AddsUtility.AddMobUtils;
import com.mojodigi.ninjafox.BroadCast.DownLoadUtility;
import com.mojodigi.ninjafox.Browser.AdBlock;
import com.mojodigi.ninjafox.Browser.AlbumController;
import com.mojodigi.ninjafox.Browser.BrowserContainer;
import com.mojodigi.ninjafox.Browser.BrowserController;
import com.mojodigi.ninjafox.Database.Record;
import com.mojodigi.ninjafox.Database.RecordAction;
import com.mojodigi.ninjafox.DownlaodModule.DownloadBinder;
import com.mojodigi.ninjafox.DownlaodModule.DownloadService;
import com.mojodigi.ninjafox.Fragment.NewsFragment;
import com.mojodigi.ninjafox.Model.ContactsModel;
import com.mojodigi.ninjafox.Model.CricScoreModel;
import com.mojodigi.ninjafox.Model.NewsList;
import com.mojodigi.ninjafox.Model.NewsMainModel;
import com.mojodigi.ninjafox.Model.NewsSuperParentModel;
import com.mojodigi.ninjafox.Model.SpeedDialModel;
import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.Service.ClearService;
import com.mojodigi.ninjafox.Service.GetAddressIntentService;
import com.mojodigi.ninjafox.Service.HolderService;
import com.mojodigi.ninjafox.Service.getAddressJobIntentService;
import com.mojodigi.ninjafox.SharedPrefs.AppConstants;
import com.mojodigi.ninjafox.SharedPrefs.SharedPreferenceUtil;
import com.mojodigi.ninjafox.Task.ApiRequestTask;
import com.mojodigi.ninjafox.Task.ScreenshotTask;
import com.mojodigi.ninjafox.Task.WebCallGetCricketScore;
import com.mojodigi.ninjafox.Task.WebCallGetNewsTask;
import com.mojodigi.ninjafox.Task.WebCallNewsByPreference;
import com.mojodigi.ninjafox.Task.WebCallSwipeDataTask;
import com.mojodigi.ninjafox.Unit.BrowserUtility;
import com.mojodigi.ninjafox.Unit.CommonUtility;
import com.mojodigi.ninjafox.Unit.IntentUtility;
import com.mojodigi.ninjafox.Unit.JsonParser;
import com.mojodigi.ninjafox.Unit.OkhttpMethods;
import com.mojodigi.ninjafox.Unit.ViewUnit;
import com.mojodigi.ninjafox.View.CompleteAdapter;
import com.mojodigi.ninjafox.View.DialogAdapter;
import com.mojodigi.ninjafox.View.FullscreenHolder;
import com.mojodigi.ninjafox.View.GridAdapter;
import com.mojodigi.ninjafox.View.GridItem;
import com.mojodigi.ninjafox.View.LinePagerIndicatorDecoration;
import com.mojodigi.ninjafox.View.RecordAdapter;
import com.mojodigi.ninjafox.View.SwitcherPanel;
import com.mojodigi.ninjafox.View.jmmRelativeLayout;
import com.mojodigi.ninjafox.View.jmmToast;
import com.mojodigi.ninjafox.View.jmmWebView;
import com.mojodigi.ninjafox.org.developer.dynamicgrid.DynamicGridView;
import com.nex3z.flowlayout.FlowLayout;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements BrowserController , /*NewsAdapter.newsListener,*/ WebCallGetCricketScore.cricketJsonListener ,
        WebCallGetNewsTask.NewsDataJsonListener ,
        WebCallSwipeDataTask.SwipeDataJsonListener ,
        NewsFragment.enableDisablerefreshListener , WebCallNewsByPreference.NewsByPreferenceListener,
        ApiRequestTask.JsonLoadListener {

    private static final int DOUBLE_TAPS_QUIT_DEFAULT = 2000;

    private SwitcherPanel switcherPanel;

    private float dimen156dp;
    private float dimen144dp;
    private float dimen117dp;
    private float dimen108dp;
    private float dimen48dp;

    private HorizontalScrollView switcherScroller;
    private LinearLayout switcherContainer;

    private ImageButton switcherBookmarks;
    private ImageButton switcherHistory;
    private ImageButton switcherAdd;

    private RelativeLayout omnibox;
    LinearLayout main_view;
    private AutoCompleteTextView inputBox;
    private ImageButton omniboxBookmark;

    private ImageButton omniboxRefresh;
    private ImageButton omniboxOverflow;

    private ImageButton omniboxHome;
    private ProgressBar progressBar;

    private RelativeLayout searchPanel;
    private EditText searchBox;
    private TextView omniboxTabCount;

    private ImageButton searchUp;
    private ImageButton searchDown;
    private ImageButton searchCancel;

    private Button relayoutOK;
    // private FrameLayout contentFrame;
    private RelativeLayout contentFrame;

    LinearLayout contentContainerl;

    /* save password variables*/
    private CredentialsClient mCredentialsApiClient;
    private Credential mCurrentCredential;
    CredentialRequest mCredentialRequest;
    private boolean isInCogActiveGlobal = false;
    /* save password variables*/
    //RecyclerView news_recycler_view;

    TextView tabCount,loginButton;

    RecyclerView score_recycler_view;
    LinearLayout scoreLayout;

    AddMobUtils  addMobUtils;

    //add push notification
    private String fcm_Token ="" ;
    public   String deviceID ="";
    public   String nameOfDevice ="";
    public   String appVersionName ="";
    int max_execute ;

    /*permission vars*/

    String[] permissionsRequired = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,  Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE,  Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;

    /*permission vars*/

    /*LOCATION Vars*/
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;
    private LocationAddressResultReceiver addressResultReceiver;
    private Location currentLocation;
    private LocationCallback locationCallback;
    TextView userPoints;
    /*LOCATION Vars*/


    private LinearLayout newsTabsLLayout;
    private  ViewPager mPager ;
    private  TabLayout mTabLayout ,mLanguageTabLayout;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int currentPosition=0;

    private ArrayList<NewsSuperParentModel> newsSuperParentModelArrayList;
    //ArrayList<NewsCategoryModel>  prefJsonNewsCategoryList ;
    //ArrayList prefJsonNewsCategoryListHolder;
    private MainActivity mInstance ;

    private DownloadBinder downloadBinder = null;

    private int adapterNewsListSize = 0;
    private int currentTabPosition;
    private JSONObject globalJonsData;
    private JSONObject gloablCricketScoreJson;
    private NestedScrollView nestedScrollView;
    private boolean stopCallingService;


//    @Override
//    public void onNewsClicked(NewsList newsList) {
//        //updateAlbum(newsList.getCanonicalUrl());
//        Log.e("newsList" , newsList.getCanonicalUrl()+"");
//        String url= newsList.getCanonicalUrl();
//        if(!url.trim().isEmpty()) {
//            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//            startActivity(browserIntent);
//        }
//    }

    @Override
    public void cricketScorejson(JSONObject obj) {

        System.out.print(""+obj);
        gloablCricketScoreJson=obj;
        if (obj != null) {

            if (obj.has("data")) {
                scoreLayout.setVisibility(View.VISIBLE);

                try {

                    JSONArray jsonarray = new JSONArray(JsonParser.getkeyValue_Str(obj, "data"));

                    ArrayList<CricScoreModel> scoreList=new ArrayList<>();

                    for (int i = 0; i < jsonarray.length(); i++) {

                        JSONObject dataJson = jsonarray.getJSONObject(i);
                        String note = dataJson.getString("note");

                        JSONObject team1Obj = dataJson.getJSONObject("team1");
                        JSONObject team2Obj = dataJson.getJSONObject("team2");

                        System.out.print(team1Obj+""+team2Obj);


                        String team1NameStr=team1Obj.getString("teamName");
                        String team1ScoreStr=team1Obj.getString("score");
                        String team1WicketStr=team1Obj.getString("wickets");
                        String team1OverPlayedStr=team1Obj.getString("overs");
                        String team1CodeStr=team1Obj.getString("teamCode");

                        String team2NameStr=team2Obj.getString("teamName");
                        String team2ScoreStr=team2Obj.getString("score");
                        String team2WicketStr=team2Obj.getString("wickets");
                        String team2OverPlayedStr=team2Obj.getString("overs");
                        String team2CodeStr=team2Obj.getString("teamCode");


                        CricScoreModel model = new CricScoreModel();

                        model.setMatchNote(note);

                        model.setTeam1Name(team1NameStr);
                        model.setTeam2Name(team2NameStr);

                        model.setTeam1Code(team1CodeStr);
                        model.setTeam2Code(team2CodeStr);

                        model.setTeam1Score(team1ScoreStr+"/"+team1WicketStr+" ("+team1OverPlayedStr+")");
                        model.setTeam2Score(team2ScoreStr+"/"+team2WicketStr+" ("+team2OverPlayedStr+")");

                        scoreList.add(model);
                    }


                    CrickScoreAdapter scoreAdapter= new CrickScoreAdapter(scoreList, mContext);
                    if(score_recycler_view!=null) {
                        score_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                        score_recycler_view.addItemDecoration(new LinePagerIndicatorDecoration());
                        score_recycler_view.setAdapter(scoreAdapter);
                    }


                } catch (JSONException e) {
                    String string=e.getMessage();
                    System.out.print(""+string);
                }
                catch (Exception e)
                {
                    String string=e.getMessage();
                    System.out.print(""+string);
                }
            } else {

            }


        }

    }

    @Override
    public void onJsonLoad(String json, int mRequestCode) {

        if(mRequestCode==AppConstants.userdDevicedetailsApiCode)
        {
            try {
                JSONObject mainObj=new JSONObject(json);
                Log.d("userStoreDta", json.toString());
                String redirectUrl=JsonParser.getkeyValue_Str(mainObj, "redirectUrl");
                String capturedDomain=JsonParser.getkeyValue_Str(mainObj, "capturedDomain");

                prefs.setStringValue(AppConstants.PREFS_REDIRECT_URL, redirectUrl);
                prefs.setStringValue(AppConstants.PREFS_CAPTURE_STR, capturedDomain);

                String  token= JsonParser.getkeyValue_Str(mainObj, "token");
                if(token!=null) {
                    if (token.equalsIgnoreCase("") || token == null) {
                        /*force logout if token is not returned from serwer end*/

                        prefs.setStringValue(AppConstants.PREFS_USER_ID, "0");
                        prefs.setStringValue(AppConstants.PREFS_TOKEN, "0");
                        loginButton.setVisibility(View.VISIBLE);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        if(mRequestCode==AppConstants.addDetailsApiCode)
        {
            // jmmToast.show(mContext, json.toString());
            try {
                Log.d("fbaddJsonResponse",json.toString());
                JSONObject mainJson = new JSONObject(json);
                String status = JsonParser.getkeyValue_Str(mainJson, "status");
                //
                if (status.equalsIgnoreCase("true")) {

                    String adShow = JsonParser.getkeyValue_Str(mainJson, "AdShow");

                    if (adShow.equalsIgnoreCase("true")) {
                        if (mainJson.has("data")) {
                            JSONObject dataJson = mainJson.getJSONObject("data");
                            AddMobUtils util = new AddMobUtils();
                            String show_Add = JsonParser.getkeyValue_Str(mainJson, "AdShow");

                            String adProviderId =JsonParser.getkeyValue_Str(dataJson, "adProviderId");
                            String adProviderName = JsonParser.getkeyValue_Str(dataJson, "adProviderName");


                            String appId_PublisherId = JsonParser.getkeyValue_Str(dataJson, "appId_PublisherId");
                            String bannerAdId = JsonParser.getkeyValue_Str(dataJson, "bannerAdId");
                            String interstitialAdId = JsonParser.getkeyValue_Str(dataJson, "interstitialAdId");
                            String videoAdId = JsonParser.getkeyValue_Str(dataJson, "videoAdId");

                            Log.d("AddiDs", adProviderName + " ==" + appId_PublisherId + "==" + bannerAdId + "==" + interstitialAdId + "==" + videoAdId);


                            //check for true value above in code so  can put true directly;
                            try {
                                prefs.setValue(AppConstants.SHOW_ADD, Boolean.parseBoolean(show_Add));
                            }catch (Exception e)
                            {
                                // IN CASE OF EXCEPTION CONSIDER  FALSE AS THE VALUE WILL NOT BE TRUE,FALSE.
                                prefs.setValue(AppConstants.SHOW_ADD, false);
                            }

                            prefs.setValue(AppConstants.ADD_PROVIDER_ID, adProviderId);
                            prefs.setValue(AppConstants.APP_ID, appId_PublisherId);
                            prefs.setValue(AppConstants.BANNER_ADD_ID, bannerAdId);
                            prefs.setValue(AppConstants.INTERESTIAL_ADD_ID, interstitialAdId);
                            prefs.setValue(AppConstants.VIDEO_ADD_ID, videoAdId);


                            if(adProviderId.equalsIgnoreCase(AppConstants.FaceBookAddProividerId))
                            {
                                //util.dispFacebookBannerAdd(mContext, addprefs,MainActivity.this);
                            }



                        } else {
                            String message = JsonParser.getkeyValue_Str(mainJson, "message");
                            Log.d("message", "" + message);
                        }
                    } else {
                        String message = JsonParser.getkeyValue_Str(mainJson, "message");

                        Log.d("message", "" + message);

                    }


                }

                //



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public void onLoadFailed(String msg) {

    }


    private class VideoCompletionListener implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return false;
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            onHideCustomView();
        }
    }

    private FullscreenHolder fullscreenHolder;
    private View customView;
    private VideoView videoView;
    private int originalOrientation;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private ValueCallback<Uri> uploadMsg = null;
    private ValueCallback<Uri[]> filePathCallback = null;

    private static boolean quit = false;
    private boolean create = true;
    private int shortAnimTime = 0;
    private int mediumAnimTime = 0;
    private int longAnimTime = 0;
    private AlbumController currentAlbumController = null;

    private Context mContext;
    SharedPreferenceUtil prefs;

    AppUpdateManager appUpdateManager;
    int MY_REQUEST_CODE=9999;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        /*from me new*/
        if(requestCode==CommonUtility.MOST_VISITED_REQUEST_CODE ||  requestCode==CommonUtility.MOST_RECENT_REQUEST_CODE)
        {
            if(intent!=null)
            {
                String url= intent.getStringExtra(CommonUtility.INTENT_DATA_KEY);
                addAlbum(getString(R.string.album_untitled), url, false, null);
                showAlbum(BrowserContainer.get(BrowserContainer.size() - 1), false, false, false);
                //increaseTabCount();
                // setTabValue();
                setTabValue(BrowserContainer.size());
                return;
            }
        }
        /*from me new*/

        /*to  handle google  in app update*/

        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                System.out.println("update failed"+resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
            else
            {
                System.out.println("update success"+requestCode);
            }
        }

        /*to  handle google  in app update*/

        if(intent!=null && filePathCallback!=null ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                filePathCallback.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
            }

        }

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

    private synchronized void startAndBindDownloadService() {
        Intent downloadIntent = new Intent(mContext, DownloadService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mContext.startForegroundService(downloadIntent);
        } else {
            mContext.startService(downloadIntent);
        }
        bindService(downloadIntent, serviceConnection, BIND_AUTO_CREATE);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = MainActivity.this;
        mInstance = this;
        prefs = new SharedPreferenceUtil(mContext);
        permissionStatus = mContext.getSharedPreferences("permissionStatus", MODE_PRIVATE);
        addMobUtils = new AddMobUtils();


       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager.TaskDescription description = new ActivityManager.TaskDescription(
                    getString(R.string.app_name),
                    BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher),
                    getResources().getColor(R.color.background_dark)
            );
            setTaskDescription(description);
        }*/


        setContentView(R.layout.main_top);

        create = true;
        shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mediumAnimTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);
        longAnimTime = getResources().getInteger(android.R.integer.config_longAnimTime);

        switcherPanel = (SwitcherPanel) findViewById(R.id.switcher_panel);


        switcherPanel.setStatusListener(new SwitcherPanel.StatusListener() {
            @Override
            public void onFling() {

            }

            @Override
            public void onExpanded() {
                switcherPanel.expanded();  //tks on 26-11-2019

                String   albumViewDeleted = CommonUtility.Album_View_Deleted;
                if(albumViewDeleted.equalsIgnoreCase("true")){
                    //decreaseTabCount();
                    setTabValue(BrowserContainer.size());
                    CommonUtility.Album_View_Deleted = "false";
                }
            }

            @Override
            public void onCollapsed()
            {
                inputBox.clearFocus();
            }
        });


        dimen156dp = getResources().getDimensionPixelSize(R.dimen.layout_width_156dp);
        dimen144dp = getResources().getDimensionPixelSize(R.dimen.layout_width_144dp);
        dimen117dp = getResources().getDimensionPixelSize(R.dimen.layout_height_117dp);
        dimen108dp = getResources().getDimensionPixelSize(R.dimen.layout_height_108dp);
        dimen48dp = getResources().getDimensionPixelOffset(R.dimen.layout_height_48dp);

        initSwitcherView();
        initOmnibox();
        initSearchPanel();

        //android.os.Debug.waitForDebugger();    // forces app to enable debuging;

        relayoutOK = (Button) findViewById(R.id.main_relayout_ok);
        //contentFrame = (FrameLayout) findViewById(R.id.main_content);
        contentFrame = (RelativeLayout) findViewById(R.id.main_content);

        new AdBlock(this); // For AdBlock
        dispatchIntent(getIntent());

        // add  customDataViews
        contentContainerl = findViewById(R.id.contentContainer);
        // add  customDataViews

        /*setInitTabCount();
        getTabCount();*/
        setTabValue(BrowserContainer.size());
        if(mContext!=null)
        {
            askForPermission();
        }

        getPushToken();

        //startAndBindDownloadService();

        JSONObject detailsObj=CommonUtility.prepareAddJsonRequest(mContext);
        new ApiRequestTask(mContext, this, CommonUtility.FB_ADD_URL,
                false, false, null, detailsObj.toString(), AppConstants.addDetailsApiCode).execute();


        new FetchContacts().execute();
        getAllContacts();
    }

    private void getAllContacts(){
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            int contactIdIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID);
            int nameIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int phoneNumberIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int photoIdIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID);
            cursor.moveToFirst();
            do {
                String idContact = cursor.getString(contactIdIdx);
                String name = cursor.getString(nameIdx);
                String phoneNumber = cursor.getString(phoneNumberIdx);
                Log.e("phoneNumber ", phoneNumber+"");
            } while (cursor.moveToNext());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchContacts extends AsyncTask<Void, Void, ArrayList<ContactsModel>> {

        private final String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY;

        private final String FILTER = DISPLAY_NAME + " NOT LIKE '%@%'";

        private final String ORDER = String.format("%1$s COLLATE NOCASE", DISPLAY_NAME);

        @SuppressLint("InlinedApi")
        private final String[] PROJECTION = { ContactsContract.Contacts._ID, DISPLAY_NAME,  ContactsContract.Contacts.HAS_PHONE_NUMBER  };

        @Override
        protected ArrayList<ContactsModel> doInBackground(Void... params) {
            try {
                ArrayList<ContactsModel> contactsList = new ArrayList<>();

                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, PROJECTION, FILTER, null, ORDER);
                if (cursor != null && cursor.moveToFirst()) {

                    do {
                        // get the contact's information
                        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                        Integer hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        // get the user's phone number
                        String phone = null;
                        if (hasPhone > 0) {
                            Cursor cursorPhone = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{id}, null);
                            if (cursorPhone != null && cursorPhone.moveToFirst()) {
                                phone = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                cursorPhone.close();
                            }
                        }


                        // get the user's email address
                        String email = null;
                        Cursor cursorEmail = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        if (cursorEmail != null && cursorEmail.moveToFirst()) {
                            email = cursorEmail.getString(cursorEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                            cursorEmail.close();
                        }


                        // if the user user has an email or phone then add it to contacts
                        if ((!TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                                && !email.equalsIgnoreCase(name)) || (!TextUtils.isEmpty(phone))) {

                            ContactsModel  mContacts = new ContactsModel();
                            mContacts.phoneName = name;
                            //Log.e("name " , name+"");
                            mContacts.phoneNumber = phone;
                            Log.e("numbers " , phone+"");
                            mContacts.phoneEmail = email;
                            Log.e("emails " , email+"");

                            contactsList.add(mContacts);
                        }

                    } while (cursor.moveToNext());

                    // clean up cursor
                    cursor.close();
                }

                return contactsList;
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<ContactsModel> contactsList) {
            if (contactsList != null) {
                // success
                // mContactsList = contactsList;
                //Log.e("mContacts " , mContacts+"");
                JSONObject mainContactsjsonObject = new JSONObject();
                JSONArray contactsJsonArray = new JSONArray();
                JSONObject contactJsonObject  ;
                for(int i=0 ; i<contactsList.size() ; i++){
                    contactJsonObject = new JSONObject();
                    try {
                        contactJsonObject.put("number" , contactsList.get(i).phoneNumber+"" );
                        contactJsonObject.put("email" , contactsList.get(i).phoneEmail+"" );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Log.e("contactsEmail " , contactsList.get(i).phoneEmail+"");
                    contactsJsonArray.put(contactJsonObject);
                }
                try {
                    mainContactsjsonObject.put("contacts", contactsJsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("jsonObject " , mainContactsjsonObject+"");

            } else {
                // show failure
                // syncFailed();
            }
        }
    }





    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onCredentialRetrieved(Credential credential) {
        Credential.UserCredential accountType = credential.getUserCredential();

        if (accountType == null) {
            // Sign the user in with information from the Credential.
            //signInWithPassword(credential.getId(), credential.getPassword());
            // signInWithPassword(accountType.getUsername(), accountType.getPassword());
        } else if (accountType.equals(IdentityProviders.GOOGLE)) {
            // The user has previously signed in with Google Sign-In. Silently
            // sign in the user with the same ID.
            // See https://developers.google.com/identity/sign-in/android/
            GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            GoogleSignInClient signInClient = GoogleSignIn.getClient(this, gso);
            Task<GoogleSignInAccount> task = signInClient.silentSignIn();

            // ...
        }
        //added
        else if (accountType.equals(IdentityProviders.TWITTER)) {

        } else if (accountType.equals(IdentityProviders.FACEBOOK)) {

        } else if (accountType.equals(IdentityProviders.YAHOO)) {

        }

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        stopCallingService=false;
        if(mPager!=null) {
            currentPosition = mPager.getCurrentItem();
        }

        if(globalJonsData==null) {
            new WebCallGetNewsTask(mContext, false, mInstance).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        JSONObject deviceDetailsObj = prepareDeviceDetailsJson();
        new ApiRequestTask(mContext, this, CommonUtility.API_USER_DEVICE_DETAILS, false, false, null, deviceDetailsObj.toString(), AppConstants.userdDevicedetailsApiCode).execute();

        if(CommonUtility.isLoggedInUser(prefs)) {
            loginButton.setVisibility(View.GONE);

           // JSONObject deviceDetailsObj = prepareDeviceDetailsJson();
            //new ApiRequestTask(mContext, this, CommonUtility.API_USER_DEVICE_DETAILS, false, false, null, deviceDetailsObj.toString(), AppConstants.userdDevicedetailsApiCode).execute();
        }
        else {
            loginButton.setVisibility(View.VISIBLE);
        }


        IntentUtility.setContext(this);
        if (create) {
            return;
        }

        dispatchIntent(getIntent());

        if (IntentUtility.isDBChange()) {
            updateBookmarks();
            updateAutoComplete();
            IntentUtility.setDBChange(false);
        }

        if (IntentUtility.isSPChange()) {
            for (AlbumController controller : BrowserContainer.list()) {
                if (controller instanceof jmmWebView) {
                    ((jmmWebView) controller).initPreferences();
                }
            }
            IntentUtility.setSPChange(false);
        }


    }

    private JSONObject prepareDeviceDetailsJson()  {

        JSONObject jsonObject=new JSONObject();

        try {
            jsonObject.put("userId", prefs.getStringValue(AppConstants.PREFS_USER_ID, "0"));
            jsonObject.put("token", prefs.getStringValue(AppConstants.PREFS_TOKEN, "0"));
            jsonObject.put("email", prefs.getStringValue(AppConstants.PREFS_EMAIL, ""));

            jsonObject.put("deviceModel", Build.MODEL);
            jsonObject.put("manufacturer", Build.MANUFACTURER);
            String osName=CommonUtility.getOsDetails();
            jsonObject.put("osVersion", osName);
            String googleAddId=null;
            String string = prefs.getStringValue(AppConstants.PREFS_GOOGLE_ADD_ID, CommonUtility.NOT_FOUND);
            System.out.print(""+string);
            if(string.equalsIgnoreCase(CommonUtility.NOT_FOUND))
            {
                CommonUtility.getGoogleAddId(mContext);
                //googleAddId=CommonUtility.getGoogleAddId(mContext);
                // prefs.setStringValue(AppConstants.PREFS_GOOGLE_ADD_ID, googleAddId);
            }
            else
            {
                googleAddId =prefs.getStringValue(AppConstants.PREFS_GOOGLE_ADD_ID, CommonUtility.NOT_FOUND);
            }
            if(googleAddId==null)
            {
                googleAddId="Could not get googleId on device";
            }
            jsonObject.put("googleAddId", googleAddId);
            jsonObject.put("deviceId", CommonUtility.getDeviceId(mContext));
            jsonObject.put("gender", "");
            jsonObject.put("dob", "");
            jsonObject.put("lastLocation", prefs.getStringValue(AppConstants.PREFS_LOCATION, "location not found"));
            jsonObject.put("state", prefs.getStringValue(AppConstants.PREFS_LOCATION_STATE, "delhi"));  // delhi  is the default state;
            jsonObject.put("city", prefs.getStringValue(AppConstants.PREFS_LOCATION_CITY, "delhi"));  // delhi  is the default city;

            ArrayList<String> appsList=  CommonUtility.getInstalledApps(mContext);

            JSONArray appsArray=new JSONArray();

            if(appsList!=null) {
                for (int i = 0; i < appsList.size(); i++) {
                    //JSONObject appObject = new JSONObject();
                    //appObject.put("appName", appsList.get(i));
                    appsArray.put(appsList.get(i));
                }
            }
            jsonObject.put("appsInstalled", appsArray);
            System.out.print(""+jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d("detailsJson", jsonObject.toString());
        return jsonObject;
    }


    /**********************Cont_Tab************************************/
    private int getTabCount() {
        int tabCount = 1;
        if (prefs != null) {
            tabCount = prefs.getIntValue(AppConstants.NUM_TABS, 1);
            //omniboxTabCount.setText(""+prefs.getIntValue(AppConstants.NUM_TABS, 1));
        }
        return tabCount;
    }

    private void setInitTabCount() {
        if (prefs != null) {
            prefs.setIntValue(AppConstants.NUM_TABS, 1);
        }
    }

    private void dispatchIntent(Intent intent) {
        Intent toHolderService = new Intent(this, HolderService.class);
        IntentUtility.setClear(false);
        stopService(toHolderService);

        if (intent != null && intent.hasExtra(IntentUtility.OPEN)) { // From HolderActivity's menu
            pinAlbums(intent.getStringExtra(IntentUtility.OPEN));
        } else if (intent != null && intent.getAction() != null && intent.getAction().equals(Intent.ACTION_WEB_SEARCH)) { // From ActionMode and some others
            pinAlbums(intent.getStringExtra(SearchManager.QUERY));
        } else if (intent != null && filePathCallback != null) {
            filePathCallback = null;
        } else {
            String redirectUrl=prefs.getStringValue(AppConstants.PREFS_REDIRECT_URL, "");
            if(redirectUrl!=null && redirectUrl.length()>1)
            {
                pinAlbums(null);
                addAlbum(getString(R.string.album_untitled), redirectUrl, false, null);
            }
            else {
                pinAlbums(null);  // original line
            }
        }
    }

    @Override
    public void onPause() {
        Intent toHolderService = new Intent(this, HolderService.class);
        IntentUtility.setClear(false);
        stopService(toHolderService);

        create = false;
        inputBox.clearFocus();
        if (currentAlbumController != null && currentAlbumController instanceof jmmRelativeLayout) {
            jmmRelativeLayout layout = (jmmRelativeLayout) currentAlbumController;
            if (layout.getFlag() == BrowserUtility.FLAG_HOME) {
                DynamicGridView gridView = (DynamicGridView) layout.findViewById(R.id.home_grid);
                if (gridView.isEditMode()) {
                    gridView.stopEditMode();
                    relayoutOK.setVisibility(View.GONE);
                    omnibox.setVisibility(View.VISIBLE);
                    initHomeGrid(layout, true);
                }
            }
        }

        IntentUtility.setContext(this);
        super.onPause();

    }

    @Override
    public void onDestroy() {
        Intent toHolderService = new Intent(this, HolderService.class);
        IntentUtility.setClear(true);
        stopService(toHolderService);

        boolean exit = true;

        /*added in build3.1*/
        if(serviceConnection!=null) {
            try {
                unbindService(serviceConnection);
            }catch (IllegalArgumentException iae){
                iae.printStackTrace();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }


      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = new Intent(mContext, DownloadService.class);
            intent.setAction(DownloadService.ACTION_STOP_FOREGROUND_SERVICE);
            startService(intent);
        }
        else {
            Intent downloadIntent = new Intent(mContext, DownloadService.class);
            stopService(downloadIntent);
       }
*/



        /* to  clear  the  data  on app  exit  if prefence is set to  true */
       /* SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean exit = true;
        if (sp.getBoolean(getString(R.string.sp_clear_quit), false)) {
            Intent toClearService = new Intent(this, ClearService.class);
            startService(toClearService);
            exit = false;
        }*/

        /* to  clear  the  data  on app  exit  if prefence is set to  true */


        BrowserContainer.clear();
        IntentUtility.setContext(null);
        super.onDestroy();
        if (exit) {
            System.exit(0); // For remove all WebView thread
        }

        moveTaskToBack(true);// allow launch of  the qpp from home screen  if in background;

        // stop  the service call when activity goes to pause state;
        if(fusedLocationClient!=null && locationCallback !=null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (currentAlbumController != null && currentAlbumController instanceof jmmRelativeLayout) {
            jmmRelativeLayout layout = (jmmRelativeLayout) currentAlbumController;
            if (layout.getFlag() == BrowserUtility.FLAG_HOME) {
                DynamicGridView gridView = (DynamicGridView) layout.findViewById(R.id.home_grid);
                if (gridView.isEditMode()) {
                    gridView.stopEditMode();
                    relayoutOK.setVisibility(View.GONE);
                    omnibox.setVisibility(View.VISIBLE);
                }
            }
        }

        hideKeyBoard(inputBox);
        hideSearchPanel();
        if (switcherPanel.getStatus() != SwitcherPanel.Status.EXPANDED) {
            switcherPanel.expanded();
        }
        super.onConfigurationChanged(newConfig);

        float coverHeight = ViewUnit.getWindowHeight(this) - ViewUnit.getStatusBarHeight(this) - dimen108dp - dimen48dp;
        switcherPanel.setCoverHeight(coverHeight);
        switcherPanel.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                switcherPanel.fixKeyBoardShowing(switcherPanel.getHeight());
                switcherPanel.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        if (currentAlbumController != null && currentAlbumController instanceof jmmRelativeLayout) {
            jmmRelativeLayout layout = (jmmRelativeLayout) currentAlbumController;
            if (layout.getFlag() == BrowserUtility.FLAG_HOME) {
                initHomeGrid(layout, true);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            // When video fullscreen, just control the sound
            return !(fullscreenHolder != null || customView != null || videoView != null) && onKeyCodeVolumeUp();
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            // When video fullscreen, just control the sound
            return !(fullscreenHolder != null || customView != null || videoView != null) && onKeyCodeVolumeDown();
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            //return showOverflow();   // no need
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            // When video fullscreen, first close it
            if (fullscreenHolder != null || customView != null || videoView != null) {
                return onHideCustomView();
            }
            return onKeyCodeBack(true);
        }

        return false;
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // When video fullscreen, just control the sound
        if (fullscreenHolder != null || customView != null || videoView != null) {
            return false;
        }

        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            int vc = Integer.valueOf(sp.getString(getString(R.string.sp_volume), "1"));
            if (vc != 2) {
                return true;
            }
        }

        return false;
    }

    private void initSwitcherView() {
        switcherScroller = (HorizontalScrollView) findViewById(R.id.switcher_scroller);
        switcherContainer = (LinearLayout) findViewById(R.id.switcher_container);


    }

    private void initOmnibox() {
        main_view = (LinearLayout) findViewById(R.id.main_view);
        omnibox = (RelativeLayout) findViewById(R.id.main_omnibox);
        inputBox = (AutoCompleteTextView) findViewById(R.id.main_omnibox_input);
        omniboxBookmark = (ImageButton) findViewById(R.id.main_omnibox_bookmark);
        omniboxRefresh = (ImageButton) findViewById(R.id.main_omnibox_refresh);
        progressBar = (ProgressBar) findViewById(R.id.main_progress_bar);
        loginButton=(TextView) findViewById(R.id.loginButton);
        loginButton.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,SignInActivity.class);
                startActivity(intent);
            }
        });

        /*this is for swiping the  inputbox where it display  the title of the page on swipe*/
        /*inputBox.setOnTouchListener(new SwipeToBoundListener(omnibox, new SwipeToBoundListener.BoundCallback() {
            private KeyListener keyListener = inputBox.getKeyListener();

            @Override
            public boolean canSwipe() {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
                boolean ob = sp.getBoolean(getString(R.string.sp_omnibox_control), true);
                return !switcherPanel.isKeyBoardShowing() && ob;
            }

            @Override
            public void onSwipe() {
                inputBox.setKeyListener(null);
                inputBox.setFocusable(false);
                inputBox.setFocusableInTouchMode(false);
                inputBox.clearFocus();
            }

            @Override
            public void onBound(boolean canSwitch, boolean left) {
                inputBox.setKeyListener(keyListener);
                inputBox.setFocusable(true);
                inputBox.setFocusableInTouchMode(true);
                inputBox.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                inputBox.clearFocus();

                if (canSwitch) {
                    AlbumController controller = nextAlbumController(left);
                    showAlbum(controller, false, false, true);
                    jmmToast.show(mContext, controller.getAlbumTitle());
                }
            }
        }));*/

        /*this is for swiping the  inputbox where it display  the title of the page on swipe*/

        inputBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (currentAlbumController == null) { // || !(actionId == EditorInfo.IME_ACTION_DONE)
                    return false;
                }

                String query = inputBox.getText().toString().trim();
                if (query.isEmpty()) {
                    jmmToast.show(mContext, R.string.toast_input_empty);
                    return true;
                }
                // here
                boolean st = currentAlbumController.getIsInCogTab();
                updateAlbum(query);

                hideKeyBoard(inputBox);
                return false;
            }
        });
        updateBookmarks();
        updateAutoComplete();

        omniboxBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!prepareRecord()) {
                    jmmToast.show(mContext, R.string.toast_add_bookmark_failed);
                    return;
                }

                jmmWebView customWebView = (jmmWebView) currentAlbumController;
                String title = customWebView.getTitle();
                String url = customWebView.getUrl();

                RecordAction action = new RecordAction(mContext);
                action.open(true);
                if (action.checkBookmark(url)) {
                    action.deleteBookmark(url);
                    jmmToast.show(mContext, R.string.toast_delete_bookmark_successful);
                } else {
                    action.addBookmark(new Record(title, url, System.currentTimeMillis()));
                    jmmToast.show(mContext, R.string.toast_add_bookmark_successful);
                }
                action.close();

                updateBookmarks();
                updateAutoComplete();
            }
        });


        // refresh the url
        omniboxRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentAlbumController == null) {
                    jmmToast.show(mContext, R.string.toast_refresh_failed);
                    return;
                }

                if (currentAlbumController instanceof jmmWebView) {
                    jmmWebView customWebView = (jmmWebView) currentAlbumController;
                    if (customWebView.isLoadFinish()) {
                        if(BrowserUtility.isUrlBlocked(mContext, prefs, customWebView.getUrl()))
                        {
                            /*do  not refresh  if url  is blocked*/
                            jmmToast.show(mContext, getString(R.string.blocked_url));
                            return;
                        }
                        customWebView.reload();
                    } else {
                        customWebView.stopLoading();
                    }
                } else if (currentAlbumController instanceof jmmRelativeLayout) {
                    final jmmRelativeLayout layout = (jmmRelativeLayout) currentAlbumController;
                    if (layout.getFlag() == BrowserUtility.FLAG_HOME) {
                        initHomeGrid(layout, true);
                        return;
                    }
                    initBHList(layout, true);
                } else {
                    jmmToast.show(mContext, R.string.toast_refresh_failed);
                }
            }
        });

    }

    private void initHomeGrid(final jmmRelativeLayout layout, boolean update) {
        if (update) {
            updateProgress(BrowserUtility.PROGRESS_MIN);
        }
        //set  view pager
        RecordAction action = new RecordAction(this);
        action.open(false);
        final List<GridItem> gridList = action.listGrid();
        action.close();

        DynamicGridView gridView = (DynamicGridView) layout.findViewById(R.id.home_grid);
        TextView aboutBlank = (TextView) layout.findViewById(R.id.home_about_blank);
        /// Custom content will appear here
        // gridView.setEmptyView(aboutBlank);  commented on 06-09-2019


        final GridAdapter gridAdapter;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridAdapter = new GridAdapter(this, gridList, 3);
        } else {
            gridAdapter = new GridAdapter(this, gridList, 2);
        }
        gridView.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();

        /* Wait for gridAdapter.notifyDataSetChanged() */
        if (update) {
            gridView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    layout.setAlbumCover(ViewUnit.capture(layout, dimen144dp, dimen108dp, false, Bitmap.Config.RGB_565));
                    updateProgress(BrowserUtility.PROGRESS_MAX);
                }
            }, shortAnimTime);
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                updateAlbum(gridList.get(position).getURL());
                //increaseTabCount();
                //setTabValue();
                setTabValue(BrowserContainer.size());
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showGridMenu(gridList.get(position));
                return true;
            }
        });

    }

    private void initBHList(final jmmRelativeLayout layout, boolean update) {
        if(layout!=null) {

            if (update) {
                updateProgress(BrowserUtility.PROGRESS_MIN);
            }

            RecordAction action = new RecordAction(mContext);
            action.open(false);
            final List<Record> list;
            if (layout.getFlag() == BrowserUtility.FLAG_BOOKMARKS) {
                list = action.listBookmarks();
                Collections.sort(list, new Comparator<Record>() {
                    @Override
                    public int compare(Record first, Record second) {
                        return first.getTitle().compareTo(second.getTitle());
                    }
                });
            } else if (layout.getFlag() == BrowserUtility.FLAG_HISTORY) {
                list = action.listHistory();
            } else {
                list = new ArrayList<>();
            }
            action.close();

            ListView listView = (ListView) layout.findViewById(R.id.record_list);
            TextView textView = (TextView) layout.findViewById(R.id.record_list_empty);
            listView.setEmptyView(textView);

            final RecordAdapter adapter = new RecordAdapter(mContext, R.layout.record_item, list, layout.getFlag());
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            /* Wait for adapter.notifyDataSetChanged() */
            if (update) {
                listView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout.setAlbumCover(ViewUnit.capture(layout, dimen144dp, dimen108dp, false, Bitmap.Config.RGB_565));
                        updateProgress(BrowserUtility.PROGRESS_MAX);
                    }
                }, shortAnimTime);
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // get  the url  from  book mark  and history
                    hideomnibox(false);
                    updateAlbum(list.get(position).getURL());
                    //increaseTabCount();
                    //setTabValue();
                    setTabValue(BrowserContainer.size());


                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    //uncomment it to get more options about the particular link like share edit ,delete etc
                    // on long click of bookmark  nad history;
                    // showListMenu(adapter, list, position);

                    return true;
                }
            });
        }
    }

    private void initSearchPanel() {
        searchPanel = (RelativeLayout) findViewById(R.id.main_search_panel);
        searchBox = (EditText) findViewById(R.id.main_search_box);
        searchUp = (ImageButton) findViewById(R.id.main_search_up);
        searchDown = (ImageButton) findViewById(R.id.main_search_down);
        searchCancel = (ImageButton) findViewById(R.id.main_search_cancel);


        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (currentAlbumController != null && currentAlbumController instanceof jmmWebView) {
                    ((jmmWebView) currentAlbumController).findAllAsync(s.toString());
                }
            }
        });


        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId != EditorInfo.IME_ACTION_DONE) {
                    return false;
                }

                if (searchBox.getText().toString().isEmpty()) {
                    jmmToast.show(mContext, R.string.toast_input_empty);
                    return true;
                }
                return false;
            }
        });

        searchUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchBox.getText().toString();
                if (query.isEmpty()) {
                    jmmToast.show(mContext, R.string.toast_input_empty);
                    return;
                }

                hideKeyBoard(searchBox);
                if (currentAlbumController instanceof jmmWebView) {
                    ((jmmWebView) currentAlbumController).findNext(false);
                }
            }
        });

        searchDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchBox.getText().toString();
                if (query.isEmpty()) {
                    jmmToast.show(mContext, R.string.toast_input_empty);
                    return;
                }

                hideKeyBoard(searchBox);
                if (currentAlbumController instanceof jmmWebView) {
                    ((jmmWebView) currentAlbumController).findNext(true);
                }
            }
        });

        searchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSearchPanel();
            }
        });
    }

    private synchronized void addAlbum(int flag) {
        boolean isInCog = false;
        final AlbumController holder;
        if (flag == BrowserUtility.FLAG_BOOKMARKS) {
            jmmRelativeLayout layout = (jmmRelativeLayout) getLayoutInflater().inflate(R.layout.record_list, null, false);

           /* RelativeLayout bottomBar=layout.findViewById(R.id.parentLayout);
            layout.removeView(bottomBar);*/

            hideomnibox(true);
            setClearAllControl(layout, false, BrowserUtility.FLAG_BOOKMARKS);
            layout.setBrowserController(this);
            layout.setFlag(BrowserUtility.FLAG_BOOKMARKS);
            layout.setAlbumCover(ViewUnit.capture(layout, dimen144dp, dimen108dp, false, Bitmap.Config.RGB_565));
            layout.setAlbumTitle(getString(R.string.album_title_bookmarks));
            holder = layout;
            initBHList(layout, false);
            /*  initNavControls(bottomBar);*/
            AddMobUtils addMobUtils=CommonUtility.getAddMobInstance();
            addMobUtils.dispFacebookBannerAdd(mContext,layout);
            /*back bbutton*/

        } else if (flag == BrowserUtility.FLAG_HISTORY) {
            jmmRelativeLayout layout = (jmmRelativeLayout) getLayoutInflater().inflate(R.layout.record_list, null, false);

           /* RelativeLayout bottomBar=layout.findViewById(R.id.parentLayout);
            layout.removeView(bottomBar);*/

            hideomnibox(true);
            setClearAllControl(layout, false, BrowserUtility.FLAG_HISTORY);
            layout.setBrowserController(this);
            layout.setFlag(BrowserUtility.FLAG_HISTORY);
            layout.setAlbumCover(ViewUnit.capture(layout, dimen144dp, dimen108dp, false, Bitmap.Config.RGB_565));
            layout.setAlbumTitle(getString(R.string.album_title_history));
            holder = layout;
            initBHList(layout, false);

            AddMobUtils addMobUtils=CommonUtility.getAddMobInstance();
            addMobUtils.dispFacebookBannerAdd(mContext,layout);

            /* initNavControls(bottomBar);*/


        } else if (flag == BrowserUtility.FLAG_HOME) {
            //run on very first  of application --
            jmmRelativeLayout layout = (jmmRelativeLayout) getLayoutInflater().inflate(R.layout.home, null, false);
            RelativeLayout bottombar = layout.findViewById(R.id.parentLayout);
            layout.removeView(bottombar);  // added
            layout.setBrowserController(this);
            layout.setFlag(BrowserUtility.FLAG_HOME);
            layout.setAlbumCover(ViewUnit.capture(layout, dimen144dp, dimen108dp, false, Bitmap.Config.RGB_565));
            layout.setAlbumTitle(getString(R.string.album_title_home));
            holder = layout;
            initHomeGrid(layout, true);
            initSocialIcons(layout);

            // on start of the  application
            setUpCricketScoreData(layout);
            setupNewsdata(layout);

            AddMobUtils addMobUtils=CommonUtility.getAddMobInstance();
            addMobUtils.dispFacebookBannerAdd(mContext, layout);

        } else if (flag == BrowserUtility.FLAG_INCOG) {
            jmmRelativeLayout layout = (jmmRelativeLayout) getLayoutInflater().inflate(R.layout.home_incog_tab, null, false);
            RelativeLayout bottomBar = layout.findViewById(R.id.parentLayout);
            layout.removeView(bottomBar); // added
            layout.setBrowserController(this, true);
            layout.setFlag(BrowserUtility.FLAG_HOME);
            layout.setAlbumCover(ViewUnit.capture(layout, dimen144dp, dimen108dp, false, Bitmap.Config.RGB_565));
            layout.setAlbumTitle(getString(R.string.album_title_home_incog));
            layout.setIsInCogTab(true);
            isInCog = layout.getIsInCogTab();
            holder = layout;
            initHomeGrid(layout, true);

            TextView incogHead = layout.findViewById(R.id.incogHead);
            TextView incogDesc = layout.findViewById(R.id.incogDesc);
            incogHead.setTypeface(CommonUtility.typeFace_Calibri_Bold(mContext));
            incogDesc.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));


            // fb add here
            //dispFbBannerAdd(layout);
            //initNavControls(bottombar,true);    // commented


        } else {
            return;
        }

        final View albumView = holder.getAlbumView();
        TextView at = albumView.findViewById(R.id.album_title);
        // set holder
        if (flag == BrowserUtility.FLAG_INCOG) {
            holder.setIsInCogTab(true);
            setTitleBackColorForIncogAndMainTab(at, isInCog);
        } else {

        }
        // set holder
        albumView.setVisibility(View.INVISIBLE);
        BrowserContainer.add(holder);
        switcherContainer.addView(albumView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.album_slide_in_up);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
                albumView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                showAlbum(holder, false, true, true);

            }
        });
        albumView.startAnimation(animation);
    }

    private void setUpCricketScoreData(jmmRelativeLayout layout) {
        if(addMobUtils!=null)

        {
            // addMobUtils.dispFacebookBannerAdd(mContext, layout);
        }
        if(layout!=null)
        {
            scoreLayout=layout.findViewById(R.id.scoreLayout);
            score_recycler_view=layout.findViewById(R.id.score_recycler_view);
            new WebCallGetCricketScore(mContext, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupNewsdata(jmmRelativeLayout layout) {
        CommonUtility.mHomeLayout = layout ;
        //Helper.mHomeLayout = layout ;

        newsTabsLLayout = layout.findViewById(R.id.newsTabsLLayout);
        mTabLayout = (TabLayout) layout.findViewById(R.id.tabLayoutNewsTabs);
        mLanguageTabLayout=(TabLayout)layout.findViewById(R.id.tabLayoutLanguageTabs);
        changeTabsFont(mTabLayout);
        changeTabsFont(mLanguageTabLayout);

        // mSwipeRefreshLayout = layout.findViewById(R.id.refreshNewsViewPager);

        /*today*/
        //nestedScrollView=(NestedScrollView) layout.findViewById(R.id.nesteScollView);

        /*today*/

        mPager = layout.findViewById(R.id.newsTabsViewPager);

        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(int)(ViewUnit.getWindowHeight(mContext)/(.75)));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,(int)(ViewUnit.getWindowHeight(mContext)/(.75)));
        //CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT,(int)(ViewUnit.getWindowHeight(mContext)/(.75)));
        mPager.setLayoutParams(params);

        newsTabsLLayout.setVisibility(View.GONE);

        new WebCallGetNewsTask(mContext,true, mInstance).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

//        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                //disablerefresh();
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                try {
//                    Objects.requireNonNull(mPager.getAdapter()).notifyDataSetChanged();
//                }catch (IllegalArgumentException iae){
//                    iae.printStackTrace();
//                }catch (Exception ex){
//                    ex.printStackTrace();
//                }
//                //Toast.makeText(MainActivity.this,   "On Page Selected", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });


        mLanguageTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                try {
                    currentTabPosition = tab.getPosition();
                    currentPosition = 0;  // set news viewpager  position to 0 everytime on tab selection;
                    //final NewsViewPagerAdapter adapter = new NewsViewPagerAdapter(getSupportFragmentManager(), newsMainModelList.size(), newsMainModelList, mInstance);
                    final NewsViewPagerAdapter adapter = new NewsViewPagerAdapter(getSupportFragmentManager(), newsSuperParentModelArrayList.get(tab.getPosition()).getCategoryList().size(), newsSuperParentModelArrayList.get(tab.getPosition()).getCategoryList(), mInstance);
                    mPager.setAdapter(adapter);
                    mPager.setCurrentItem(currentPosition);
                    mPager.setOffscreenPageLimit(2);
                    mTabLayout.setupWithViewPager(mPager);
                    changeTabsFont(mTabLayout);

                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });





    }


    private void saveLastSuccessRequestTime() {
        long time =System.currentTimeMillis();
        String strTime= CommonUtility.LongToDate(time);
        //System.out.print(""+strTime);
        prefs.setValue(AppConstants.PREFS_LAST_REQUEST_TIME, strTime);
    }

    @Override
    public void onNewsListSize(int size) {

        adapterNewsListSize = size ;
        //Toast.makeText(MainActivity.this, size+"", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNewsDecreaseTabCount() {
        //increaseTabCount();
        //setTabValue();
        setTabValue(BrowserContainer.size());
    }

    @Override
    public void reachedToEndOfRecyclerView() {

        //jmmToast.show(mContext, "end of news data");

        currentPosition = mPager.getCurrentItem();
        NewsMainModel model = newsSuperParentModelArrayList.get(currentTabPosition).getCategoryList().get(currentPosition);
        JSONObject swipeObject = new JSONObject();
        if (model != null) {
            try {
                swipeObject.put("categoryCode", model.getCategoryId()); // data is fetched using category code from server
                swipeObject.put("start", model.getNewsList().size());
                swipeObject.put("count", "10");
                swipeObject.put("position", "bottom");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if (!CommonUtility.checkIsOnline(mContext)) {
                    jmmToast.show(mContext, mContext.getResources().getString(R.string.internet_msg));
                } else {
                    new WebCallSwipeDataTask(mContext, mInstance, swipeObject.toString()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }


    @Override
    public void onNewsDataJsonListener(JSONObject obj , String strObj) {
        globalJonsData=obj;
        if (obj != null) {

            if(newsTabsLLayout!=null)

                newsTabsLLayout.setVisibility(View.VISIBLE);

            if (obj.has("newsData")) {
                try {
                    JSONArray newsDataArray = new JSONArray(JsonParser.getkeyValue_Str(obj, "newsData"));
                    newsSuperParentModelArrayList=new ArrayList<>();
                    //prefJsonNewsCategoryListHolder=new ArrayList();
                    mLanguageTabLayout.removeAllTabs();
                    for(int j=0;j<newsDataArray.length();j++)   {
                        // parent loop
                        NewsSuperParentModel newsSuperParentModel=new NewsSuperParentModel();
                        JSONObject categoryObj = newsDataArray.getJSONObject(j);
                        String languageCode=JsonParser.getkeyValue_Str(categoryObj, "languageCode");
                        String languageName=JsonParser.getkeyValue_Str(categoryObj, "languageName");

                        newsSuperParentModel.setLanguageCode(languageCode);
                        newsSuperParentModel.setLanguageName(languageName);

                        //new
                        if (categoryObj.has("category")) {

                            JSONArray categoryJsonArray = new JSONArray(JsonParser.getkeyValue_Str(categoryObj, "category"));
                            /*******************Category List************************************/
                            ArrayList<NewsMainModel>  newsMainModelList = new ArrayList<>();
                            //newsMainModelList2 = new ArrayList<>();
                            //prefJsonNewsCategoryList = new ArrayList<NewsCategoryModel>();

                            for (int i = 0; i < categoryJsonArray.length(); i++) {
                                JSONObject categoryJson = categoryJsonArray.getJSONObject(i);
                                //Log.e("CategoryJson", categoryJson+"");

                                NewsMainModel newsMainModel = new NewsMainModel();
                                String categoryName = categoryJson.getString("categoryName");
                                String categoryCode  = categoryJson.getString("categoryCode");
                                newsMainModel.setCategoryName(categoryName);
                                newsMainModel.setCategoryId(categoryCode );

                           /*  // Start Save prefrence News Category List
                             if (i != 0) {
                                 NewsCategoryModel prefNewsCategoryModel = new NewsCategoryModel(categoryCode , categoryName);
                                 prefJsonNewsCategoryList.add(prefNewsCategoryModel);
                             }
                             // End Save prefrence News Category List*/

                                /*******************News List************************************/
                                if (categoryJson.has("newsList")) {
                                    ArrayList<NewsList> newsArrayList = new ArrayList<>();
                                    JSONArray newsListArray = new JSONArray(JsonParser.getkeyValue_Str(categoryJson, "newsList"));
                                    for (int k = 0; k < newsListArray.length(); k++) {
                                        NewsList newsListModel = new NewsList();
                                        JSONObject newsListObj = newsListArray.getJSONObject(k);
                                        //Log.e("newsListObj ", newsListObj+"");
                                        String title = newsListObj.getString("title");
                                        String canonicalUrl = newsListObj.getString("canonical_url");
                                        String imageUrl = newsListObj.getString("image_url");

                                        newsListModel.setTitle(title);
                                        newsListModel.setCanonicalUrl(canonicalUrl);
                                        newsListModel.setImageUrl(imageUrl);

                                        newsArrayList.add(newsListModel);

                                    }
                                    newsMainModel.setNewsList(newsArrayList);
                                }
                                newsMainModelList.add(newsMainModel);
                            }



                            //newsMainModelList.addAll(newsMainModelList2);

                            newsSuperParentModel.setCategoryList(newsMainModelList);
                            newsSuperParentModelArrayList.add(newsSuperParentModel);

                            mLanguageTabLayout.addTab(mLanguageTabLayout.newTab().setText(languageName));

                        }
                        /*allow user to pick the preferred category for the perticular language*/
                        // prefJsonNewsCategoryListHolder.add(prefJsonNewsCategoryList);
                    }

                    if(newsSuperParentModelArrayList.size()==1)
                    {
                        mLanguageTabLayout.setVisibility(View.GONE);
                        //final NewsViewPagerAdapter adapter = new NewsViewPagerAdapter(getSupportFragmentManager(), newsMainModelList.size(), newsMainModelList, mInstance);
                        final NewsViewPagerAdapter adapter = new NewsViewPagerAdapter(getSupportFragmentManager(), newsSuperParentModelArrayList.get(0).getCategoryList().size(), newsSuperParentModelArrayList.get(0).getCategoryList(), mInstance);
                        mPager.setAdapter(adapter);
                        //mPager.setCurrentItem(0);
                        mPager.setCurrentItem(currentPosition);
                        mPager.setOffscreenPageLimit(2);
                        mTabLayout.setupWithViewPager(mPager);
                        changeTabsFont(mTabLayout);

                    }
                    saveLastSuccessRequestTime();
                } catch (JSONException e) {
                    String string=e.getMessage();
                    //System.out.print("JSONException"+string);
                }
                catch (Exception e)
                {
                    String string=e.getMessage();
                    // System.out.print("Exception"+string);
                }
            } else {
                Log.e("" ,"");
            }
        }

    }


    private void setupNewsdataOnHomeClick(jmmRelativeLayout layout,JSONObject obj)
    {
        CommonUtility.mHomeLayout = layout ;
        newsTabsLLayout = layout.findViewById(R.id.newsTabsLLayout);
        mTabLayout = (TabLayout) layout.findViewById(R.id.tabLayoutNewsTabs);
        mLanguageTabLayout=(TabLayout)layout.findViewById(R.id.tabLayoutLanguageTabs);
        changeTabsFont(mTabLayout);
        changeTabsFont(mLanguageTabLayout);

        //mSwipeRefreshLayout = layout.findViewById(R.id.refreshNewsViewPager);
        mPager = layout.findViewById(R.id.newsTabsViewPager);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,(int)(ViewUnit.getWindowHeight(mContext)/(.75)));
        mPager.setLayoutParams(params);

        newsTabsLLayout.setVisibility(View.GONE);

        if(obj!=null)
            parseOldJsonObject(obj);


        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //disablerefresh();
            }

            @Override
            public void onPageSelected(int position) {
                mPager.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        mLanguageTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try {
                    currentTabPosition = tab.getPosition();
                    currentPosition = 0;  // set news viewpager  position to 0 everytime on tab selection;
                    final NewsViewPagerAdapter adapter = new NewsViewPagerAdapter(getSupportFragmentManager(), newsSuperParentModelArrayList.get(tab.getPosition()).getCategoryList().size(), newsSuperParentModelArrayList.get(tab.getPosition()).getCategoryList(), mInstance);
                    mPager.setAdapter(adapter);
                    mPager.setCurrentItem(currentPosition);
                    mPager.setOffscreenPageLimit(2);
                    mTabLayout.setupWithViewPager(mPager);
                    changeTabsFont(mTabLayout);
                }catch (Exception e)
                {

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
    private void setCricketDataOnHomeClick( jmmRelativeLayout layout,JSONObject obj )
    {
        System.out.print(""+obj);

        if (obj != null) {
            scoreLayout=layout.findViewById(R.id.scoreLayout);
            score_recycler_view=layout.findViewById(R.id.score_recycler_view);
            if (obj.has("data")) {
                scoreLayout.setVisibility(View.VISIBLE);
                try {

                    JSONArray jsonarray = new JSONArray(JsonParser.getkeyValue_Str(obj, "data"));

                    ArrayList<CricScoreModel> scoreList=new ArrayList<>();

                    for (int i = 0; i < jsonarray.length(); i++) {

                        JSONObject dataJson = jsonarray.getJSONObject(i);
                        String note = dataJson.getString("note");

                        JSONObject team1Obj = dataJson.getJSONObject("team1");
                        JSONObject team2Obj = dataJson.getJSONObject("team2");

                        System.out.print(team1Obj+""+team2Obj);


                        String team1NameStr=team1Obj.getString("teamName");
                        String team1ScoreStr=team1Obj.getString("score");
                        String team1WicketStr=team1Obj.getString("wickets");
                        String team1OverPlayedStr=team1Obj.getString("overs");
                        String team1CodeStr=team1Obj.getString("teamCode");

                        String team2NameStr=team2Obj.getString("teamName");
                        String team2ScoreStr=team2Obj.getString("score");
                        String team2WicketStr=team2Obj.getString("wickets");
                        String team2OverPlayedStr=team2Obj.getString("overs");
                        String team2CodeStr=team2Obj.getString("teamCode");


                        CricScoreModel model = new CricScoreModel();

                        model.setMatchNote(note);

                        model.setTeam1Name(team1NameStr);
                        model.setTeam2Name(team2NameStr);

                        model.setTeam1Code(team1CodeStr);
                        model.setTeam2Code(team2CodeStr);

                        model.setTeam1Score(team1ScoreStr+"/"+team1WicketStr+" ("+team1OverPlayedStr+")");
                        model.setTeam2Score(team2ScoreStr+"/"+team2WicketStr+" ("+team2OverPlayedStr+")");

                        scoreList.add(model);
                    }


                    CrickScoreAdapter scoreAdapter= new CrickScoreAdapter(scoreList, mContext);
                    if(score_recycler_view!=null) {
                        score_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                        score_recycler_view.addItemDecoration(new LinePagerIndicatorDecoration());
                        score_recycler_view.setAdapter(scoreAdapter);
                    }


                } catch (JSONException e) {
                    String string=e.getMessage();
                    System.out.print(""+string);
                }
                catch (Exception e)
                {
                    String string=e.getMessage();
                    System.out.print(""+string);
                }
            } else {

            }


        }

    }
    private void parseOldJsonObject(JSONObject obj)
    {
        if (obj != null) {
            newsTabsLLayout.setVisibility(View.VISIBLE);
            if (obj.has("newsData")) {
                try {
                    JSONArray newsDataArray = new JSONArray(JsonParser.getkeyValue_Str(obj, "newsData"));
                    newsSuperParentModelArrayList=new ArrayList<>();
                    //prefJsonNewsCategoryListHolder=new ArrayList();
                    mLanguageTabLayout.removeAllTabs();
                    for(int j=0;j<newsDataArray.length();j++)   {
                        // parent loop
                        NewsSuperParentModel newsSuperParentModel=new NewsSuperParentModel();
                        JSONObject categoryObj = newsDataArray.getJSONObject(j);
                        String languageCode=JsonParser.getkeyValue_Str(categoryObj, "languageCode");
                        String languageName=JsonParser.getkeyValue_Str(categoryObj, "languageName");

                        newsSuperParentModel.setLanguageCode(languageCode);
                        newsSuperParentModel.setLanguageName(languageName);

                        //new
                        if (categoryObj.has("category")) {

                            JSONArray categoryJsonArray = new JSONArray(JsonParser.getkeyValue_Str(categoryObj, "category"));
                            /*******************Category List************************************/
                            ArrayList<NewsMainModel>  newsMainModelList = new ArrayList<>();
                            //newsMainModelList2 = new ArrayList<>();
                            //prefJsonNewsCategoryList = new ArrayList<NewsCategoryModel>();


                            for (int i = 0; i < categoryJsonArray.length(); i++) {
                                JSONObject categoryJson = categoryJsonArray.getJSONObject(i);
                                //Log.e("CategoryJson", categoryJson+"");

                                NewsMainModel newsMainModel = new NewsMainModel();
                                String categoryName = categoryJson.getString("categoryName");
                                String categoryCode  = categoryJson.getString("categoryCode");
                                newsMainModel.setCategoryName(categoryName);
                                newsMainModel.setCategoryId(categoryCode );

                                /*******************News List************************************/
                                if (categoryJson.has("newsList")) {
                                    ArrayList<NewsList> newsArrayList = new ArrayList<>();
                                    JSONArray newsListArray = new JSONArray(JsonParser.getkeyValue_Str(categoryJson, "newsList"));
                                    for (int k = 0; k < newsListArray.length(); k++) {
                                        NewsList newsListModel = new NewsList();
                                        JSONObject newsListObj = newsListArray.getJSONObject(k);
                                        //Log.e("newsListObj ", newsListObj+"");
                                        String title = newsListObj.getString("title");
                                        String canonicalUrl = newsListObj.getString("canonical_url");
                                        String imageUrl = newsListObj.getString("image_url");

                                        newsListModel.setTitle(title);
                                        newsListModel.setCanonicalUrl(canonicalUrl);
                                        newsListModel.setImageUrl(imageUrl);

                                        newsArrayList.add(newsListModel);

                                    }
                                    newsMainModel.setNewsList(newsArrayList);
                                }
                                newsMainModelList.add(newsMainModel);
                            }



                            //newsMainModelList.addAll(newsMainModelList2);

                            newsSuperParentModel.setCategoryList(newsMainModelList);
                            newsSuperParentModelArrayList.add(newsSuperParentModel);

                            mLanguageTabLayout.addTab(mLanguageTabLayout.newTab().setText(languageName));

                        }

                    }

                    if(newsSuperParentModelArrayList.size()==1)
                    {
                        mLanguageTabLayout.setVisibility(View.GONE);
                        //final NewsViewPagerAdapter adapter = new NewsViewPagerAdapter(getSupportFragmentManager(), newsMainModelList.size(), newsMainModelList, mInstance);
                        final NewsViewPagerAdapter adapter = new NewsViewPagerAdapter(getSupportFragmentManager(), newsSuperParentModelArrayList.get(0).getCategoryList().size(), newsSuperParentModelArrayList.get(0).getCategoryList(), mInstance);
                        mPager.setAdapter(adapter);
                        //mPager.setCurrentItem(0);
                        mPager.setCurrentItem(currentPosition);
                        mPager.setOffscreenPageLimit(2);
                        mTabLayout.setupWithViewPager(mPager);
                        changeTabsFont(mTabLayout);
                    }
                    else {

                        currentTabPosition=0;
                        currentPosition=0;
                        final NewsViewPagerAdapter adapter = new NewsViewPagerAdapter(getSupportFragmentManager(), newsSuperParentModelArrayList.get(0).getCategoryList().size(), newsSuperParentModelArrayList.get(0).getCategoryList(), mInstance);
                        mPager.setAdapter(adapter);
                        mPager.setCurrentItem(currentPosition);
                        mPager.setOffscreenPageLimit(2);
                        mTabLayout.setupWithViewPager(mPager);
                        changeTabsFont(mTabLayout);
                    }
                } catch (JSONException e) {
                    String string=e.getMessage();
                    //System.out.print("JSONException"+string);
                }
                catch (Exception e)
                {
                    String string=e.getMessage();
                    // System.out.print("Exception"+string);
                }
            } else {
                Log.e("" ,"");
            }
        }

    }


    @Override
    public void onNewsByPreferenceListener(JSONObject obj) {
        System.out.print(""+obj);
        if (obj != null) {
            //Log.e("End_Time :" , System.currentTimeMillis()+"");
            newsTabsLLayout.setVisibility(View.VISIBLE);
            if (obj.has("category")) {
                try {
                    JSONArray categoryJsonArray = new JSONArray(JsonParser.getkeyValue_Str(obj, "category"));
                    /*******************Category List************************************/
                    ArrayList<NewsMainModel> newsMainModelList = new ArrayList<>();

                    //prefJsonNewsCategoryList = new ArrayList<NewsCategoryModel>();

                    for (int i = 0; i < categoryJsonArray.length(); i++) {
                        JSONObject categoryJson = categoryJsonArray.getJSONObject(i);
                        //Log.e("CategoryJson", categoryJson+"");

                        NewsMainModel newsMainModel = new NewsMainModel();
                        String categoryName = categoryJson.getString("categoryName");
                        String categoryCode = categoryJson.getString("categoryCode");
                        newsMainModel.setCategoryName(categoryName);
                        newsMainModel.setCategoryId(categoryCode);

                        // End Save prefrence News Category List
                        /*******************News List************************************/
                        if (categoryJson.has("newsList")) {
                            ArrayList<NewsList> newsArrayList = new ArrayList<>();
                            JSONArray newsListArray = new JSONArray(JsonParser.getkeyValue_Str(categoryJson, "newsList"));
                            for (int k = 0; k < newsListArray.length(); k++) {
                                NewsList newsListModel = new NewsList();
                                JSONObject newsListObj = newsListArray.getJSONObject(k);
                                //Log.e("newsListObj ", newsListObj+"");

                                String title = newsListObj.getString("title");
                                String canonicalUrl = newsListObj.getString("canonical_url");
                                String imageUrl = newsListObj.getString("image_url");

                                newsListModel.setTitle(title);
                                newsListModel.setCanonicalUrl(canonicalUrl);
                                newsListModel.setImageUrl(imageUrl);

                                newsArrayList.add(newsListModel);
                                // Log.e("newsArrayList ", newsArrayList+"");
                            }
                            newsMainModel.setNewsList(newsArrayList);
                            // Log.e("newsMainModel ", newsMainModel+"");
                        }
                        newsMainModelList.add(newsMainModel);
                        //Log.e("newsMainModelList ", newsMainModelList+"");
                    }


                    final NewsViewPagerAdapter adapter = new NewsViewPagerAdapter(getSupportFragmentManager(), newsMainModelList.size(), newsMainModelList , mInstance);
                    mPager.setAdapter(adapter);
                    //mPager.setCurrentItem(0);
                    mPager.setCurrentItem(currentPosition);
                    mPager.setOffscreenPageLimit(2);
                    mTabLayout.setupWithViewPager(mPager);
                    changeTabsFont(mTabLayout);
                    saveLastSuccessRequestTime();

                } catch (JSONException e) {
                    String string=e.getMessage();
                    //System.out.print("JSONException"+string);
                }
                catch (Exception e)
                {
                    String string=e.getMessage();
                    // System.out.print("Exception"+string);
                }
            } else {
                Log.e("" ,"");
            }
        }
    }


    @Override
    public void onSwipeDataJsonListener(JSONObject obj) {

        ArrayList<NewsList> newNewsList =  setUpRefreshData(obj);
        //ArrayList<NewsList> oldList = newsMainModelList.get(currentPosition).getNewsList();
        final ArrayList<NewsList> oldList = newsSuperParentModelArrayList.get(currentTabPosition).getCategoryList().get(currentPosition).getNewsList(); // find position instaed of 0


       /* if(refreshDirection==SwipyRefreshLayoutDirection.TOP ) {
            saveLastSuccessRequestTime();
            oldList.addAll(0, newNewsList);
        }
        else if(refreshDirection==SwipyRefreshLayoutDirection.BOTTOM )
            oldList.addAll(oldList.size(),newNewsList);
*/
        //swipe json data;

       /* saveLastSuccessRequestTime();
        oldList.addAll(0, newNewsList);*/

        oldList.addAll(oldList.size(),newNewsList);

        final NewsViewPagerAdapter adapter = new NewsViewPagerAdapter(getSupportFragmentManager(), newsSuperParentModelArrayList.get(currentTabPosition).getCategoryList().size(), newsSuperParentModelArrayList.get(currentTabPosition).getCategoryList() , mInstance);
        mPager.setAdapter(adapter);
        mPager.setCurrentItem(currentPosition);
        mPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mPager);
        changeTabsFont(mTabLayout);


       /* if(nestedScrollView!=null)
        {
         nestedScrollView.post(new Runnable() {
             @Override
             public void run() {
                 nestedScrollView.setScrollY(View.FOCUS_DOWN);
             }
         });

        }*/


    }


    private  ArrayList<NewsList> setUpRefreshData(JSONObject jsonObject) {
        ArrayList<NewsList> newsListList = new ArrayList<>();
        try {
            String status=JsonParser.getkeyValue_Str(jsonObject, "status");

            if (jsonObject!=null && status.equalsIgnoreCase("true") && jsonObject.has("newsList")) {
                //Log.e("status" , status);
                JSONArray newsListArray = new JSONArray(JsonParser.getkeyValue_Str(jsonObject, "newsList"));
                if(newsListArray.length()>=1)
                {
                    for (int k = 0; k < newsListArray.length(); k++) {
                        NewsList newsListModel = new NewsList();
                        JSONObject newsListObj = newsListArray.getJSONObject(k);
                        String title = newsListObj.getString("title");
                        String canonicalUrl = newsListObj.getString("canonical_url");
                        String imageUrl = newsListObj.getString("image_url");

                        newsListModel.setTitle(title);
                        newsListModel.setCanonicalUrl(canonicalUrl);
                        newsListModel.setImageUrl(imageUrl);
                        newsListList.add(newsListModel);
                    }
                }
            }
            else if (status.equalsIgnoreCase("false")  )
            {
                Log.e("status" , ""+status);
                String msg=JsonParser.getkeyValue_Str(jsonObject, "message");
                if(msg!=null)
                    jmmToast.show(mContext, msg);
            }
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return newsListList ;
    }




    public  void hideomnibox(boolean flag) {
        if (omnibox != null) {
            if (flag)
                omnibox.setVisibility(View.GONE);
            else
                omnibox.setVisibility(View.VISIBLE);
        }
    }


    private void setClearAllControl(final jmmRelativeLayout layout, final boolean updateFlag, final int flag) {

        //hideomnibox(true);
        TextView clearAllText = layout.findViewById(R.id.clearAllText);
        TextView title_txt = layout.findViewById(R.id.title_txt);

        LinearLayout backButton = layout.findViewById(R.id.backButton);

        clearAllText.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        title_txt.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));

        switch (flag) {
            case BrowserUtility.FLAG_BOOKMARKS:
                title_txt.setText(R.string.bookmark);
                break;
            case BrowserUtility.FLAG_HISTORY:
                title_txt.setText(R.string.History);
                break;
        }


        clearAllText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (flag) {
                    case BrowserUtility.FLAG_BOOKMARKS:
                        BrowserUtility.clearBookmarks(mContext);
                        initBHList(layout, updateFlag);
                        jmmToast.show(mContext, R.string.toast_clear_successful);
                        break;
                    case BrowserUtility.FLAG_HISTORY:
                        BrowserUtility.clearHistory(mContext);
                        initBHList(layout, updateFlag);
                        jmmToast.show(mContext, R.string.toast_clear_successful);
                        break;
                    default:
                        jmmToast.show(mContext, R.string.can_not_delete);
                        break;
                }

            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentAlbumController instanceof jmmRelativeLayout) {
                    switch (currentAlbumController.getFlag())
                    {
                        case BrowserUtility.FLAG_BOOKMARKS:
                            //updateAlbum();  as per pre written code
                            if (currentAlbumController != null) {
                                removeAlbum(currentAlbumController);
                            }
                            break;

                        case BrowserUtility.FLAG_HISTORY:
                            //updateAlbum(); as per pre written code
                            if (currentAlbumController != null) {
                                removeAlbum(currentAlbumController);
                            }
                    }

                    // dispFbInteresTialAdds();

                    hideomnibox(false);
                }

            }
        });


    }

    private void initSocialIcons(final jmmRelativeLayout layout) {

        final ArrayList<SpeedDialModel> defaultIconList=new ArrayList<>();
        defaultIconList.add(new SpeedDialModel("Facebook","www.facebook.com",R.drawable.facebook_png));
        defaultIconList.add(new SpeedDialModel("YouTube","www.youtube.com",R.drawable.youtube_png));
        //defaultIconList.add(new SpeedDialModel("Google","www.google.com",R.drawable.google_png));
        defaultIconList.add(new SpeedDialModel("Win Cash","https://play45.qureka.com",R.drawable.qureka_icon));
        defaultIconList.add(new SpeedDialModel("Free games","https://www.gamezop.com/?id=Gy7WoLmZP",R.drawable.game_icon));
        defaultIconList.add(new SpeedDialModel("Twitter","www.twitter.com",R.drawable.twitter_png));
        defaultIconList.add(new SpeedDialModel("Instagram","www.instagram.com",R.drawable.insta_png));
        defaultIconList.add(new SpeedDialModel("Quora","www.quora.com",R.drawable.quora_png));

        if(prefs.getArrayList_Social(mContext)!=null) {
            defaultIconList.addAll(prefs.getArrayList_Social(mContext));
        }

        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams((int)ViewUnit.dp2px(mContext, 50), (int)ViewUnit.dp2px(mContext, 50));
        FlowLayout flowLayout=layout.findViewById(R.id.socialLayout);
        flowLayout.removeAllViews();
        for (int i=0;i<defaultIconList.size();i++)
        {
            LinearLayout dlayout = (LinearLayout) getLayoutInflater().inflate(R.layout.speed_dial_link_layout, null, false);
            final ImageView siteLogo=dlayout.findViewById(R.id.siteLogo);
            siteLogo.setLayoutParams(lparams);
            siteLogo.setBackground(getResources().getDrawable(R.drawable.ripple));
            TextView siteTitle=dlayout.findViewById(R.id.siteTitle);

            siteLogo.setImageResource(defaultIconList.get(i).getSiteLog());
            siteTitle.setText(defaultIconList.get(i).getSiteTitle());
            siteTitle.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));


            siteLogo.setId(i*5+1);
            final int finalI = i;
            siteLogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(BrowserUtility.isUrlBlocked(mContext,prefs,defaultIconList.get(finalI).getSiteUrl()))
                    {
                        jmmToast.show(mContext, mContext.getString(R.string.blocked_url));
                        return;
                    }

                    updateAlbum(defaultIconList.get(finalI).getSiteUrl());
                }
            });

            siteLogo.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(finalI>5) {
                        String url = defaultIconList.get(finalI).getSiteUrl();
                        dispRemoveSiteDialog(url, layout);
                    }
                    return true;

                }
            });
            flowLayout.addView(dlayout);
        }




        ImageView addSite=new ImageView(mContext);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)ViewUnit.dp2px(mContext, 50), (int)ViewUnit.dp2px(mContext, 50));
        addSite.setLayoutParams(params);
        addSite.setBackground(getResources().getDrawable(R.drawable.ripple));
        addSite.setImageResource(R.drawable.social_add_site);

        addSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispAddSiteDialog(layout);
            }
        });
        flowLayout.addView(addSite);

        int numRows=flowLayout.getRowsCount();
        if(numRows==0)
            numRows=1;

        System.out.print(""+numRows);
        //jmmToast.show(mContext, "numRows-> "+numRows+" child Count--> "+flowLayout.getChildCount());

        if(numRows>=2 && flowLayout.getChildCount()>7 )
        {
            ScrollView scrollView=layout.findViewById(R.id.dweb);
            scrollView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int)ViewUnit.dp2px(mContext, 150)));

        }
        else  if(numRows==1 && flowLayout.getChildCount()==7)
        {
            ScrollView scrollView=layout.findViewById(R.id.dweb);
            scrollView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT));

        }
        else if(numRows==1 && flowLayout.getChildCount()>7)
        {
            ScrollView scrollView=layout.findViewById(R.id.dweb);
            scrollView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int)ViewUnit.dp2px(mContext, 150)));
        }
        else if(flowLayout.getChildCount()==7 && (numRows==2 || numRows==1))
        {
            ScrollView scrollView=layout.findViewById(R.id.dweb);
            scrollView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT));
        }


    }



    private void dispRemoveSiteDialog(final String url, final jmmRelativeLayout layout) {
        final AlertDialog dialog_remove_site;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(true);

        final LinearLayout dialog_remove_site_layout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_remove_site, null, false);
        builder.setView(dialog_remove_site_layout);

        TextView removeSite=dialog_remove_site_layout.findViewById(R.id.removeSite);
        TextView siteUrl_Txt=dialog_remove_site_layout.findViewById(R.id.siteUrl_Txt);
        TextView removeHeading=dialog_remove_site_layout.findViewById(R.id.removeHeading);
        TextView cancel_txt=dialog_remove_site_layout.findViewById(R.id.cancel_txt);

        siteUrl_Txt.setText(url);

        removeSite.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        siteUrl_Txt.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        removeHeading.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        cancel_txt.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));

        dialog_remove_site=builder.create();
        dialog_remove_site.show();

        removeSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(dialog_remove_site!=null)
                    dismissCustDialog(dialog_remove_site);

                if(prefs.getArrayList_Social(mContext)!=null);
                {
                    ArrayList<SpeedDialModel> savedList= prefs.getArrayList_Social(mContext);
                    for(int j=0;j<savedList.size();j++)
                    {
                        if(savedList.get(j).getSiteUrl().equalsIgnoreCase(url))
                        {
                            savedList.remove(j);
                            prefs.saveArrayList_Social(savedList, CommonUtility.PREF_SOCIAL_LIST);
                            initSocialIcons(layout);//recreate  the icons

                        }
                    }
                }
            }
        });


        cancel_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialog_remove_site!=null)
                    dismissCustDialog(dialog_remove_site);
            }
        });

    }

    public synchronized void addAlbum(String title, final String url, final boolean foreground, final Message resultMsg) {
        if(BrowserUtility.isUrlBlocked(mContext,prefs,url))
        {
            jmmToast.show(mContext, mContext.getString(R.string.blocked_url));
            return;
        }

        final jmmWebView webView = new jmmWebView(this);
        webView.setBrowserController(this);
        webView.setFlag(BrowserUtility.FLAG_NINJA);
        webView.setAlbumCover(ViewUnit.capture(webView, dimen144dp, dimen108dp, false, Bitmap.Config.RGB_565));
        webView.setAlbumTitle(title);
        webView.getSettings().setJavaScriptEnabled(true);
        ViewUnit.bound(this, webView);

        final View albumView = webView.getAlbumView();
        if (currentAlbumController != null && (currentAlbumController instanceof jmmWebView) && resultMsg != null) {
            int index = BrowserContainer.indexOf(currentAlbumController) + 1;
            BrowserContainer.add(webView, index);
            switcherContainer.addView(albumView, index, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        } else {
            BrowserContainer.add(webView);
            switcherContainer.addView(albumView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        if (!foreground) {
            ViewUnit.bound(this, webView);
            webView.loadUrl(url);
            webView.deactivate();

            albumView.setVisibility(View.VISIBLE);
            if (currentAlbumController != null) {
                switcherScroller.smoothScrollTo(currentAlbumController.getAlbumView().getLeft(), 0);
            }
            return;
        }


        albumView.setVisibility(View.INVISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.album_slide_in_up);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
                albumView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                showAlbum(webView, false, true, false);

                if (url != null && !url.isEmpty()) {
                    webView.loadUrl(url);
                } else if (resultMsg != null) {
                    WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                    transport.setWebView(webView);
                    resultMsg.sendToTarget();
                }
            }
        });
        albumView.startAnimation(animation);
    }



    private synchronized void pinAlbums(String url) {
        try {
            hideKeyBoard(inputBox);
            hideSearchPanel();
            switcherContainer.removeAllViews();

            for (AlbumController controller : BrowserContainer.list()) {
                if (controller instanceof jmmWebView) {
                    ((jmmWebView) controller).setBrowserController(this);
                } else if (controller instanceof jmmRelativeLayout) {
                    ((jmmRelativeLayout) controller).setBrowserController(this);
                }

                //    tackle  "the specified child already has a parent" error
                if (controller.getAlbumView().getParent() != null) {
                    ((ViewGroup) controller.getAlbumView().getParent()).removeView(controller.getAlbumView());
                }
                //    tackle  "the specified child already has a parent" error

                switcherContainer.addView(controller.getAlbumView(), LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                controller.getAlbumView().setVisibility(View.VISIBLE);
                controller.deactivate();


            }

            if (BrowserContainer.size() < 1 && url == null) {
                addAlbum(BrowserUtility.FLAG_HOME);
            }
            else if (BrowserContainer.size() >= 1 && url == null) {
                if (currentAlbumController != null) {

                    if (currentAlbumController.getFlag() == BrowserUtility.FLAG_HISTORY || currentAlbumController.getFlag() == BrowserUtility.FLAG_BOOKMARKS) {
                        hideomnibox(true);
                    }
                    currentAlbumController.activate();
                    return;
                }

                int index = BrowserContainer.size() - 1;
                currentAlbumController = BrowserContainer.get(index);
                contentFrame.removeAllViews();

                //in build 2.9
                //    tackle  "the specified child already has a parent" error
                if (currentAlbumController.getAlbumView().getParent() != null) {
                    //((ViewGroup) currentAlbumController.getAlbumView().getParent()).removeView(currentAlbumController.getAlbumView());
                    ((ViewGroup) currentAlbumController.getAlbumView().getParent()).removeView((View) currentAlbumController);
                }
                //    tackle  "the specified child already has a parent" error
                //in build 2.9

                contentFrame.addView((View) currentAlbumController);

                currentAlbumController.activate();
                updateOmnibox();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switcherScroller.smoothScrollTo(currentAlbumController.getAlbumView().getLeft(), 0);
                        currentAlbumController.setAlbumCover(ViewUnit.capture(((View) currentAlbumController), dimen144dp, dimen108dp, false, Bitmap.Config.RGB_565));
                    }
                }, shortAnimTime);
            } else { // When url != null
                jmmWebView webView = new jmmWebView(this);
                webView.setBrowserController(this);
                webView.setFlag(BrowserUtility.FLAG_NINJA);
                webView.setAlbumCover(ViewUnit.capture(webView, dimen144dp, dimen108dp, false, Bitmap.Config.RGB_565));
                webView.setAlbumTitle(getString(R.string.album_untitled));
                ViewUnit.bound(this, webView);
                webView.loadUrl(url);

                BrowserContainer.add(webView);
                final View albumView = webView.getAlbumView();
                albumView.setVisibility(View.VISIBLE);
                switcherContainer.addView(albumView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                contentFrame.removeAllViews();
                // add layout here in case of link open

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                params.addRule(RelativeLayout.ABOVE, addBottomNavBar(contentFrame, false));
                webView.setLayoutParams(params);
                contentFrame.addView(webView, params);
                //contentFrame.addView(webView); // original  line

                if (currentAlbumController != null) {
                    currentAlbumController.deactivate();
                }
                currentAlbumController = webView;
                currentAlbumController.activate();

                updateOmnibox();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switcherScroller.smoothScrollTo(currentAlbumController.getAlbumView().getLeft(), 0);
                        currentAlbumController.setAlbumCover(ViewUnit.capture(((View) currentAlbumController), dimen144dp, dimen108dp, false, Bitmap.Config.RGB_565));
                    }
                }, shortAnimTime);
            }


            setTabValue(BrowserContainer.size());
        }catch (Exception e)
        {

        }
    }

    @Override
    public synchronized void showAlbum(AlbumController controller, boolean anim, final boolean expand, final boolean capture) {
        if (controller == null || controller == currentAlbumController) {
            switcherPanel.expanded();
            contentFrame.removeAllViews();   // new  line
            addbottomViewToFrame(contentFrame, controller); //new  line
            return;
        }

        if (currentAlbumController != null && anim) {
            currentAlbumController.deactivate();
            final View rv = (View) currentAlbumController;
            final View av = (View) controller;

            Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.album_fade_out);
            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                }

                @Override
                public void onAnimationStart(Animation animation) {
                    contentFrame.removeAllViews();

                    contentFrame.addView(av);
                }
            });
            rv.startAnimation(fadeOut);
        } else {
            if (currentAlbumController != null) {
                currentAlbumController.deactivate();
            }
            contentFrame.removeAllViews();

            addbottomViewToFrame(contentFrame, controller);
            //contentFrame.addView((View) controller); // original  line

        }

        currentAlbumController = controller;
        boolean st = currentAlbumController.getIsInCogTab();
        setTitleBackColorForIncogAndMainTab(currentAlbumController.getAlbumView().findViewById(R.id.album_title), st);

        /* TextView textView = currentAlbumController.getAlbumView().findViewById(R.id.album_title);
        String hexColor="";
        hexColor=getTextViewBackColor(textView);

        if(hexColor!=null && !hexColor.equalsIgnoreCase("")) {
            //setTitleBackColorForIncogAndMainTab(currentAlbumController.getAlbumView().findViewById(R.id.album_title), hexColor);
        }*/

        currentAlbumController.activate();

        try {
            switcherScroller.smoothScrollTo(currentAlbumController.getAlbumView().getLeft(), 0);
        }catch (NullPointerException nex ){
            nex.printStackTrace();
        }catch (Exception  ex ){
            ex.printStackTrace();
        }

        updateOmnibox();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (expand) {
                    switcherPanel.expanded();
                }

                if (capture) {
                    currentAlbumController.setAlbumCover(ViewUnit.capture(((View) currentAlbumController), dimen144dp, dimen108dp, false, Bitmap.Config.RGB_565));
                }
            }
        }, shortAnimTime);
    }


    private void addbottomViewToFrame(RelativeLayout contentFrame, AlbumController controller) {
        if (controller.getFlag() != BrowserUtility.FLAG_BOOKMARKS && controller.getFlag() != BrowserUtility.FLAG_HISTORY) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.ABOVE, addBottomNavBar(contentFrame, false));
            ((View) controller).setLayoutParams(params);

            //    tackle  "the specified child already has a parent" error
            if((View) ((View) controller).getParent()!=null)
            {
                ((ViewGroup) ((View) controller).getParent()).removeView((View) controller);

            }
            //    tackle  "the specified child already has a parent" error
            contentFrame.addView((View) controller, params);

        } else {

            //    tackle  "the specified child already has a parent" error
            if((View) ((View) controller).getParent()!=null)
            {
                ((ViewGroup) ((View) controller).getParent()).removeView((View) controller);

            }
            //    tackle  "the specified child already has a parent" error
            contentFrame.addView((View) controller);
        }
    }



    private String getTextViewBackColor(TextView textView) {

        String hexColor = "";
        if (textView.getBackground() instanceof ColorDrawable) {
            ColorDrawable cd = (ColorDrawable) textView.getBackground();
            int colorCode = cd.getColor();
            hexColor = String.format("#%06X", (0xFFFFFF & colorCode));
            System.out.print("" + colorCode + hexColor);
        }
        return hexColor;
    }

    private synchronized void updateAlbum() {
        if (currentAlbumController == null) {
            return;
        }

        jmmRelativeLayout layout = (jmmRelativeLayout) getLayoutInflater().inflate(R.layout.home, null, false);
        RelativeLayout bottombar = layout.findViewById(R.id.parentLayout);
        layout.removeView(bottombar);
        layout.setBrowserController(this);
        layout.setFlag(BrowserUtility.FLAG_HOME);
        layout.setAlbumCover(ViewUnit.capture(layout, dimen144dp, dimen108dp, false, Bitmap.Config.RGB_565));
        layout.setAlbumTitle(getString(R.string.album_title_home));
        initHomeGrid(layout, true);

        int index = switcherContainer.indexOfChild(currentAlbumController.getAlbumView());
        currentAlbumController.deactivate();
        switcherContainer.removeView(currentAlbumController.getAlbumView());
        contentFrame.removeAllViews(); //

        switcherContainer.addView(layout.getAlbumView(), index, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        contentFrame.addView(layout);
        BrowserContainer.set(layout, index);
        currentAlbumController = layout;
        updateOmnibox();

        initSocialIcons(layout);
        //setUpCricketScoreData(layout);
        setCricketDataOnHomeClick(layout,gloablCricketScoreJson);
        if(globalJonsData!=null)
            setupNewsdataOnHomeClick(layout,globalJonsData);


        showAlbum(currentAlbumController, false, false, false);   //new

    }

    private synchronized void updateAlbumIncog() {
        if (currentAlbumController == null) {
            return;
        }

        jmmRelativeLayout layout = (jmmRelativeLayout) getLayoutInflater().inflate(R.layout.home_incog_tab, null, false);
        RelativeLayout bottombar = layout.findViewById(R.id.parentLayout);
        layout.removeView(bottombar);  //new
        layout.setBrowserController(this);
        layout.setFlag(BrowserUtility.FLAG_INCOG);
        layout.setIsInCogTab(true);
        layout.setAlbumCover(ViewUnit.capture(layout, dimen144dp, dimen108dp, false, Bitmap.Config.RGB_565));
        layout.setAlbumTitle(getString(R.string.album_title_home_incog));

        initHomeGrid(layout, true);

        int index = switcherContainer.indexOfChild(currentAlbumController.getAlbumView());
        currentAlbumController.deactivate();
        switcherContainer.removeView(currentAlbumController.getAlbumView());
        contentFrame.removeAllViews(); ///

        TextView albumTitile = layout.getAlbumView().findViewById(R.id.album_title);
        albumTitile.setBackgroundColor(getResources().getColor(R.color.gray_900));
        prefs.setValue(getResources().getString(R.string.sp_add_history), false);


        switcherContainer.addView(layout.getAlbumView(), index, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        contentFrame.addView(layout);
        BrowserContainer.set(layout, index);
        currentAlbumController = layout;
        updateOmnibox();

        //dispFbBannerAdd(layout);

        //setUpNewsTabs(Helper.mHomeLayout);//sp
        //initNavControls(bottombar,true);  //commented

        showAlbum(currentAlbumController, false, false, false);   //new

    }

    public synchronized void updateAlbum(String url) {
        if (currentAlbumController == null) {
            return;
        }
        boolean st1=BrowserUtility.isUrlBlocked(mContext,prefs,url);
        System.out.print(""+st1);
        if(BrowserUtility.isUrlBlocked(mContext,prefs,url))
        {
            jmmToast.show(mContext, mContext.getString(R.string.blocked_url));
            return;
        }

        if (currentAlbumController instanceof jmmWebView) {
            ((jmmWebView) currentAlbumController).loadUrl(url);

            updateOmnibox();
            //
            boolean st = currentAlbumController.getIsInCogTab();
            System.out.print("" + st);
            currentAlbumController.setIsInCogTab(st);
            setTitleBackColorForIncogAndMainTab(currentAlbumController.getAlbumView().findViewById(R.id.album_title), st);


            //
        } else if (currentAlbumController instanceof jmmRelativeLayout) {
            try {
                jmmWebView webView = new jmmWebView(this);
                webView.setBrowserController(this);
                webView.setFlag(BrowserUtility.FLAG_NINJA);
                webView.setAlbumCover(ViewUnit.capture(webView, dimen144dp, dimen108dp, false, Bitmap.Config.RGB_565));
                webView.setAlbumTitle(getString(R.string.album_untitled));
                ViewUnit.bound(this, webView);
                //
                boolean st = currentAlbumController.getIsInCogTab();
                //System.out.print("" + st);
                webView.setIsInCogTab(st);
                setTitleBackColorForIncogAndMainTab(webView.getAlbumView().findViewById(R.id.album_title), st);

                //
                int index = switcherContainer.indexOfChild(currentAlbumController.getAlbumView());
                currentAlbumController.deactivate();
                switcherContainer.removeView(currentAlbumController.getAlbumView());
                contentFrame.removeAllViews(); ///
                // load url here
                //
                TextView textView = currentAlbumController.getAlbumView().findViewById(R.id.album_title);
                String hexColor = "";
                hexColor = getTextViewBackColor(textView);

                if (hexColor != null && !hexColor.equalsIgnoreCase("")) {
                    //setTitleBackColorForIncogAndMainTab(currentAlbumController.getAlbumView().findViewById(R.id.album_title), hexColor);
                }

                //

                switcherContainer.addView(webView.getAlbumView(), index, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                //


                //RelativeLayout.LayoutParams params =new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,getHeightInPercentage(70));
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                params.addRule(RelativeLayout.ABOVE, addBottomNavBar(contentFrame, currentAlbumController.getIsInCogTab()));
                webView.setLayoutParams(params);
                //
                int childCount = contentFrame.getChildCount();
                System.out.print("" + childCount);
                contentFrame.addView(webView, params);


                BrowserContainer.set(webView, index);
                currentAlbumController = webView;
                webView.activate();
                webView.loadUrl(url);
                updateOmnibox();
            }catch (Exception e)
            {
                e.printStackTrace();
            }


        } else {
            jmmToast.show(this, R.string.toast_load_error);
        }
    }

    public void onAddField(WebView v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.bottom_nav_bar, null);
        v.addView(rowView);
    }

    public int addBottomNavBar(RelativeLayout v, boolean isIncog) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.bottom_nav_bar, null);

        RelativeLayout parentLayout = rowView.findViewById(R.id.parentLayout);
        parentLayout.setMinimumHeight((int) ViewUnit.dp2px(mContext, 50));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) ViewUnit.dp2px(mContext, 50));
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        rowView.setLayoutParams(params);
        v.addView(rowView, params);

        initNavControls(rowView, isIncog);
        return rowView.getId();

    }

    private void showMenuDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext);
        dialog.setContentView(R.layout.dialog_menu);
        dialog.show();
        TextView txt_AddNewTab = dialog.findViewById(R.id.txt_AddNewTab);
        TextView txt_Bookmarks = dialog.findViewById(R.id.txt_Bookmarks);
        TextView txt_Download = dialog.findViewById(R.id.txt_Download);
        TextView txt_FindInPage = dialog.findViewById(R.id.txt_FindInPage);
        TextView txt_History = dialog.findViewById(R.id.txt_History);
        TextView txt_Settings = dialog.findViewById(R.id.txt_Settings);
        TextView txt_ShareLink = dialog.findViewById(R.id.txt_ShareLink);
        TextView txt_IncogTab = dialog.findViewById(R.id.txt_IncogTab);
        TextView txt_Recent = dialog.findViewById(R.id.txt_Recent);
        TextView txt_MostVisited = dialog.findViewById(R.id.txt_MostVisited);

        txt_AddNewTab.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        txt_Bookmarks.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        txt_Download.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        txt_FindInPage.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        txt_History.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        txt_ShareLink.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        txt_Settings.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        txt_IncogTab.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));

        txt_Recent.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        txt_MostVisited.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));


        LinearLayout img_ShareLink = dialog.findViewById(R.id.img_ShareLink);
        LinearLayout img_AddNewTab = dialog.findViewById(R.id.img_AddNewTab);
        LinearLayout img_Bookmarks = dialog.findViewById(R.id.img_Bookmarks);
        LinearLayout img_IncogTab = dialog.findViewById(R.id.img_IncogTab);
        LinearLayout img_Dowanload = dialog.findViewById(R.id.img_Dowanload);
        LinearLayout img_FindInPage = dialog.findViewById(R.id.img_FindInPage);
        LinearLayout img_History = dialog.findViewById(R.id.img_History);
        LinearLayout img_Settings = dialog.findViewById(R.id.img_Settings);

        LinearLayout img_Recent = dialog.findViewById(R.id.img_Recent);
        LinearLayout img_MostVisited = dialog.findViewById(R.id.img_MostVisited);

        img_Recent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,RecentListActivity.class);
                startActivityForResult(intent, CommonUtility.MOST_RECENT_REQUEST_CODE);
                dialog.dismiss();
            }
        });

        img_MostVisited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,MostVisistedActiity.class);
                //startActivity(intent);
                startActivityForResult(intent, CommonUtility.MOST_VISITED_REQUEST_CODE);
                dialog.dismiss();
            }
        });

        img_Bookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addAlbum(BrowserUtility.FLAG_BOOKMARKS);
                // increaseTabCount();
                setTabValue(BrowserContainer.size());
                dialog.dismiss();
            }
        });

        img_Dowanload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
                dialog.dismiss();

                // boolean dstatus=CommonUtility.checkOrCreateDownloadDirectory(mContext);

            /*    if(dstatus) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    //Uri uri = Uri.parse(Environment.getExternalStorageDirectory()+"/"+AppConstants.downloadDirectory+"/");
                    Uri uri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+AppConstants.downloadDirectory+"/");

                    startActivity(Intent.createChooser(intent, "Open folder"));
                    dialog.dismiss();
                }
*/
                //intent.setDataAndType(uri, "*/*");

            }
        });

        img_Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SettingActivity.class);
                startActivity(intent);

                dialog.dismiss();
            }
        });

        img_IncogTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //increaseTabCount();
                setTabValue(BrowserContainer.size());
                addAlbum(BrowserUtility.FLAG_INCOG);
                dialog.dismiss();
            }
        });


        img_FindInPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showSearchPanel();
                dialog.dismiss();
            }
        });

        img_History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addAlbum(BrowserUtility.FLAG_HISTORY);
                // increaseTabCount();
                setTabValue(BrowserContainer.size());

                dialog.dismiss();
            }
        });

        img_AddNewTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //increaseTabCount();
                setTabValue(BrowserContainer.size());
                addAlbum(BrowserUtility.FLAG_HOME);
                dialog.dismiss();
            }
        });


        img_ShareLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //jmmToast.show(mContext, inputBox.getText().toString());
                String url = inputBox.getText().toString();
                if (url != null && BrowserUtility.isURL(url)) {
                    IntentUtility.share(mContext, getResources().getString(R.string.share_using), url);
                } else {
                    //jmmToast.show(mContext, R.string.invalid_link);
                    Log.e("Invalid Link" , R.string.invalid_link+"");
                }

                dialog.dismiss();
            }
        });



    }

    public void setTabValue(int tabCountValue) {
        if (tabCount != null) {
            tabCount.setText(""+tabCountValue);
        }
    }


    private void dispAddSiteDialog(final jmmRelativeLayout layout)
    {
        final AlertDialog dialog_add_site;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(true);

        final LinearLayout dialog_add_site_layout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_add_site, null, false);
        builder.setView(dialog_add_site_layout);

        final TextView addSiteTxt=dialog_add_site_layout.findViewById(R.id.addSite);
        TextView cancelTxt=dialog_add_site_layout.findViewById(R.id.cancel_txt);

        final EditText siteTitle=dialog_add_site_layout.findViewById(R.id.edit_Title);
        final EditText siteUrl=dialog_add_site_layout .findViewById(R.id.edit_Url);
        siteUrl.setSelection(siteUrl.getText().length());

        siteTitle.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        siteUrl.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));

        addSiteTxt.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext) );
        cancelTxt.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext) );

        dialog_add_site=builder.create();
        dialog_add_site.show();

        addSiteTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(siteTitle.getText().toString().length()>0  && siteUrl.getText().toString().length()>0)
                {
                    ArrayList<SpeedDialModel> list = new ArrayList<>();

                    String url=siteUrl.getText().toString().trim().replaceAll("\\s","").toLowerCase();
                    if(!BrowserUtility.isURL(url) && !url.contains("www."))
                    {
                        url="www."+url.trim();
                    }
                    if(BrowserUtility.isURL(url))
                    {

                        list.add(new SpeedDialModel(siteTitle.getText().toString(),url,R.drawable.web_png));

                        if(prefs.getArrayList_Social(mContext)!=null) {
                            list.addAll(prefs.getArrayList_Social(mContext));
                        }
                        if(dialog_add_site!=null)
                        {
                            dismissCustDialog(dialog_add_site);
                        }
                        prefs.saveArrayList_Social(list, CommonUtility.PREF_SOCIAL_LIST);
                        initSocialIcons(layout);
                    }
                    else {
                        jmmToast.show(mContext, R.string.prompt_valid_url);
                    }
                }
                else
                {
                    jmmToast.show(mContext, R.string.promt_title);
                }

            }
        });

        cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(dialog_add_site!=null)
                {
                    dismissCustDialog(dialog_add_site);
                }
            }
        });




    }
    private void exitDialog()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);

        FrameLayout layout = (FrameLayout) getLayoutInflater().inflate(R.layout.dialog_exit, null, false);

        builder.setView(layout);
        builder.create().show();

        TextView clean_Txt = (TextView) layout.findViewById(R.id.clean_Txt);
        TextView clean_Exit_Txt = (TextView) layout.findViewById(R.id.clean_Exit_Txt);
        TextView titleTxt = (TextView) layout.findViewById(R.id.titleTxt);
        TextView clean_desc_Txt = (TextView) layout.findViewById(R.id.clean_desc_Txt);
        final CheckBox checkBox =  layout.findViewById(R.id.checkBox);

        clean_Txt.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        clean_Exit_Txt.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        titleTxt.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        clean_desc_Txt.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        checkBox.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));


        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!checkBox.isChecked())
                {
                    checkBox.setChecked(false);
                    if(prefs!=null)
                        prefs.setValue(getString(R.string.sp_show_exit_box),true);
                }
                else
                {
                    checkBox.setChecked(true);
                    if(prefs!=null)
                        prefs.setValue(getString(R.string.sp_show_exit_box),false);
                }

            }
        });

        clean_Exit_Txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder.create().dismiss();
                Intent toClearService = new Intent(mContext, ClearService.class);
                startService(toClearService);
                finish();


            }
        });

        clean_Txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.create().dismiss();
                finish();
            }
        });



    }


    private  void defaultBrowerDialog()
    {
        if(!CommonUtility.checkAndroidSdkVersion()){
            return;
        }

        final AlertDialog dialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        FrameLayout layout = (FrameLayout) getLayoutInflater().inflate(R.layout.dialog_default_browser, null, false);
        builder.setView(layout);


        TextView default_msg_txt=layout.findViewById(R.id.default_msg_txt);
        TextView set_default_Txt=layout.findViewById(R.id.set_default_Txt);
        TextView cancel_Txt=layout.findViewById(R.id.cancel_Txt);
        final CheckBox checkBox=layout.findViewById(R.id.checkBox);

        default_msg_txt.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        cancel_Txt.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        set_default_Txt.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        checkBox.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!checkBox.isChecked())
                {
                    checkBox.setChecked(false);
                    if(prefs!=null)
                        prefs.setValue(getString(R.string.sp_show_default_box),true);
                }
                else
                {
                    checkBox.setChecked(true);
                    if(prefs!=null)
                        prefs.setValue(getString(R.string.sp_show_default_box),false);
                }

            }
        });




        dialog=builder.create();
        dialog.show();

        cancel_Txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismissCustDialog(dialog);
            }
        });

        set_default_Txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissCustDialog(dialog);
                try{
                    Intent defaultSetingsIntent = null;//ss
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        defaultSetingsIntent = new Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS);
                    }
                    startActivity(defaultSetingsIntent);
                    jmmToast.show(mContext, getResources().getString(R.string.default_set_msg));
                }
                catch (ActivityNotFoundException e)
                {
                    jmmToast.show(mContext, getResources().getString(R.string.default_browser_error));
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });


    }

    private void dismissCustDialog(AlertDialog dialog) {

        if(dialog!=null)
            dialog.dismiss();
    }

    private boolean isDispExitDialog()
    {
        if(prefs!=null)
            return prefs.getBoolanValue(getString(R.string.sp_show_exit_box), true);
        else
            return true;

    }
    private boolean dispDefaultDialog()
    {
        if(prefs!=null)
            return prefs.getBoolanValue(getString(R.string.sp_show_default_box), true);
        else
            return true;

    }
    private boolean isSetdefault()
    {
        Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://"));
        ResolveInfo resolveInfo = getPackageManager().resolveActivity(browserIntent, PackageManager.MATCH_DEFAULT_ONLY);

        // This is the default browser's packageName
        String packageName = resolveInfo.activityInfo.packageName;
        if(packageName!=null && packageName.equalsIgnoreCase(mContext.getPackageName()))
        {
            return true;
        }
        else return false;
    }


    private  void getPushToken()
    {
        //
        // the all  code part is done now will  be finalized when  richa prepares the api  for  push token to be registered.

        deviceID = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("Android ID : ",""+deviceID);
        nameOfDevice = Build.MANUFACTURER+" "+Build.MODEL+" "+Build.VERSION.RELEASE;
        Log.e("Device Name : ",""+nameOfDevice);
        PackageInfo pinfo = null;
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            appVersionName = pinfo.versionName;
            Log.e("App Version Name : ",""+appVersionName);

            if(appVersionName!=null) {
                //String appVersion = "App Version : " + appVersionName;
                String appVersion = getResources().getString(R.string.app_version)+" "+ appVersionName;

            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }catch (Exception ex){ ex.printStackTrace();}

        //


        try {
            if (prefs != null) {
                boolean st = prefs.getBoolanValue(CommonUtility.isFcmRegistered, false);
                System.out.print("" + st);
                if (!prefs.getBoolanValue(CommonUtility.isFcmRegistered, false)) {

                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this,
                            new OnSuccessListener<InstanceIdResult>() {
                                @Override
                                public void onSuccess(InstanceIdResult instanceIdResult) {
                                    fcm_Token = instanceIdResult.getToken();
                                    Log.e("New Token : ", ""+fcm_Token);

                                    if (CommonUtility.checkIsOnline(mContext)) {
                                        Log.e("Network is available ", "PushNotification Called");
                                        new PushNotificationCall().execute();
                                    } else {
                                        Log.e("No Network", "PushNotification Call failed");
                                    }
                                }
                            });

                }
            }


            if (prefs != null) {

                boolean isAutoStartPermGranted = prefs.getBoolanValue(CommonUtility.AutoStartKey, false);
                if (!isAutoStartPermGranted) {
                    Intent intent = new Intent();
                    String manufacturer = android.os.Build.MANUFACTURER;
                    //showAutoStartPermDialog(manufacturer, intent);
                    switch (manufacturer) {

                        case "xiaomi":
                            intent.setComponent(new ComponentName("com.miui.securitycenter",
                                    "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                            break;
                        case "oppo":
                            intent.setComponent(new ComponentName("com.coloros.safecenter",
                                    "com.coloros.safecenter.permission.startup.StartupAppListActivity"));

                            break;
                        case "vivo":
                            intent.setComponent(new ComponentName("com.vivo.permissionmanager",
                                    "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                            break;
                    }

                    List<ResolveInfo> arrayListInfo = getPackageManager().queryIntentActivities(intent,
                            PackageManager.MATCH_DEFAULT_ONLY);

                    if (arrayListInfo.size() > 0) {
                        // startActivity(intent);
                        showAutoStartPermDialog(manufacturer, intent);


                    }
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void showAutoStartPermDialog(String brandName, final Intent intent)
    {
        String appName=mContext.getResources().getString(R.string.app_name);
        final Dialog dialog =  new Dialog(mContext);
        dialog.setContentView(R.layout.autostart_dialog);
        TextView heading_Txt=dialog.findViewById(R.id.headingTxt);
        TextView desc_Txt=dialog.findViewById(R.id.desc_Txt);
        TextView ok_Txt=dialog.findViewById(R.id.ok);

        ok_Txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (intent != null) {
                        prefs.setValue(CommonUtility.AutoStartKey, true);
                        dialog.dismiss();
                        startActivity(intent);
                    }
                }catch (ActivityNotFoundException e)
                {
                    e.printStackTrace();
                }

            }
        });

        heading_Txt.setText(appName+" "+mContext.getResources().getString(R.string.need_permission));
        desc_Txt.setText(brandName+" "+mContext.getResources().getString(R.string.custom_ui)+" "+appName+".\n"+mContext.getResources().getString(R.string.need_enable)+" "+appName+" "+mContext.getResources().getString(R.string.towork));

        dialog.show();

    }
    // this web call send token to  server;
    public class PushNotificationCall extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                Log.e("deviceId ", ""+deviceID);
                Log.e("deviceName ", ""+nameOfDevice);
                Log.e("fcmToken ", ""+fcm_Token);
                Log.e("appVer ", ""+appVersionName);

                JSONObject requestObj = CommonUtility.prepareFcmJsonRequest(mContext, deviceID, nameOfDevice, fcm_Token , appVersionName);
                return OkhttpMethods.CallApi(mContext, CommonUtility.API_PUSH_NOTIFICATION, requestObj.toString());

            } catch (IOException e) {
                e.printStackTrace();
                return ""+e.getMessage();
            }
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // Log.e("Push Json Response ", s);

            if (prefs != null)
            {
                int responseCode=prefs.getIntValue(CommonUtility.API_RESPONSE_CODE, 0);

                if (s != null  && responseCode==200 ) {
                    try {
                        JSONObject mainJson = new JSONObject(s);
                        if (mainJson.has("status")) {
                            String status = JsonParser.getkeyValue_Str(mainJson, "status");
                            Log.e("status", "" + status);


                            if (status.equalsIgnoreCase("false")) {

                                if (mainJson.has("data")) {
                                    JSONObject dataJson = mainJson.getJSONObject("data");
                                } else {
                                    String message = JsonParser.getkeyValue_Str(mainJson, "message");
                                    Log.e("message", "" + message);
                                }
                            }
                            if (status.equalsIgnoreCase("false")) {
                                Log.e("status", "" + status);

                                if(max_execute<=5){
                                    new PushNotificationCall().execute();
                                    max_execute++;
                                }
                            }
                            else {
                                if(prefs!=null)
                                    prefs.setValue(CommonUtility.isFcmRegistered, true);
                            }
                        }
                    } catch (JSONException e) {
                        Log.d("jsonParse", "error while parsing json -->" + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    Log.e("", "else"  );
                }
            }
        }
    }

    public  int hasPermission(String permission) {
        if (mContext != null && permission != null) {

            if (ActivityCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_GRANTED) {
                return  PackageManager.PERMISSION_GRANTED;

            }
        }

        return 1001; //some other int value;
    }

    public void askForPermission()
    {
        if (hasPermission(permissionsRequired[0]) !=PackageManager.PERMISSION_GRANTED  || hasPermission(permissionsRequired[1]) !=PackageManager.PERMISSION_GRANTED  || hasPermission(permissionsRequired[2]) !=PackageManager.PERMISSION_GRANTED  || hasPermission(permissionsRequired[3])!=PackageManager.PERMISSION_GRANTED ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissionsRequired[0]) || ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, permissionsRequired[1])) {
                //Show Information about why you need the permission

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
                builder.setTitle(mContext.getResources().getString(R.string.need_permission));
                builder.setMessage(mContext.getString(R.string.app_name) + " "+mContext.getResources().getString(R.string.storage_needed));

                builder.setPositiveButton(mContext.getResources().getString(R.string.grant), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions((Activity) mContext, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);

                    }
                });
                builder.setNegativeButton(mContext.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                       /* if(dispDefaultDialog() && !isSetdefault()) {
                            defaultBrowerDialog();
                        }*/

                    }
                });
                builder.show();

            } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
                builder.setTitle(getResources().getString(R.string.permission_required));
                builder.setMessage(mContext.getString(R.string.app_name) + " "+mContext.getResources().getString(R.string.storage_needed));
                builder.setPositiveButton(mContext.getResources().getString(R.string.grant), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(mContext, getResources().getString(R.string.grant_storage), Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();


                        if(dispDefaultDialog() && !isSetdefault()) {
                            defaultBrowerDialog();
                        }




                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions((Activity) mContext, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], false);
            editor.commit();
        } else {

            googleInAppUpadte();
            if(dispDefaultDialog() && !isSetdefault()) {
                defaultBrowerDialog();
            }

            getDeviceLocation();

        }
    }

    private class LocationAddressResultReceiver extends ResultReceiver {
        LocationAddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (resultCode == 0) {
                //Last Location can be null for various reasons
                //for example the api is called first time
                //so retry till location is set
                //since intent service runs on background thread, it doesn't block main thread
                Log.d("Address", "Location null retrying");
                getAddress();
            }

            if (resultCode == 1) {
                // if no address found
                //Toast.makeText(MainActivity.this, "Address not found, " , Toast.LENGTH_SHORT).show();
                Log.d("UserLocation-->>", "Address not found");
            }

            // if got the location successfully stop  the service
            if(resultCode==2)
            {
                stopCallingService=true;

                String currentAdd = resultData.getString("address_result");// save the location in prefs  to  send in api
                String currentCity = resultData.getString("city");// save the location in prefs  to  send in api
                String currentState = resultData.getString("state");// save the location in prefs  to  send in api

                //jmmToast.show(mContext, currentAdd);
                Log.d("UserLocation-->>", ""+currentAdd.toString());

                prefs.setValue(AppConstants.PREFS_LOCATION, currentAdd);
                prefs.setValue(AppConstants.PREFS_LOCATION_CITY, currentCity);
                prefs.setValue(AppConstants.PREFS_LOCATION_STATE, currentState);

            }



        }
    }
    @SuppressWarnings("MissingPermission")
    private void getAddress() {

        try {
            if (!Geocoder.isPresent()) {
                Toast.makeText(MainActivity.this,
                        getResources().getString(R.string.address_error),
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // stop using  GetAddressIntentService
           /* Intent intent = new Intent(this, GetAddressIntentService.class);
            intent.putExtra("add_receiver", addressResultReceiver);
            intent.putExtra("add_location", currentLocation);
            startService(intent);*/

            if(Build.VERSION.SDK_INT>=26) {
                Intent intent = new Intent(this, getAddressJobIntentService.class);
                intent.putExtra("add_receiver", addressResultReceiver);
                intent.putExtra("add_location", currentLocation);
                getAddressJobIntentService.enqueueWork(mContext, intent);
            }
            else {
                Intent intent = new Intent(this, GetAddressIntentService.class);
                intent.putExtra("add_receiver", addressResultReceiver);
                intent.putExtra("add_location", currentLocation);
                startService(intent);
            }





        }catch (Exception e )
        {
            String string = e.getMessage();
            System.out.print(""+string);
        }

    }


    private void getDeviceLocation() {

        try {

            addressResultReceiver = new LocationAddressResultReceiver(new Handler());
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    currentLocation = locationResult.getLocations().get(0);
                    if(!stopCallingService) {
                        getAddress();
                    }
                }

                ;
            };

            startLocationUpdates();

        }catch (Exception e)
        {
            String string = e.getMessage();
            System.out.print(""+string);
        }

    }

    @SuppressWarnings("MissingPermission")
    private void startLocationUpdates() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                LocationRequest locationRequest = new LocationRequest();
                locationRequest.setInterval(2000);
                locationRequest.setFastestInterval(1000);
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

            }
        }catch (Exception e)
        {
            String string = e.getMessage();
            System.out.print(""+string);
        }

    }

    /* google in app update*/


    private void googleInAppUpadte()
    {
        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(mContext);

        //Returns an intent object that you use to check for an update.
        com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(new com.google.android.play.core.tasks.OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        // For a flexible update, use AppUpdateType.FLEXIBLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    // Request the update.

                    requestUpadte(appUpdateInfo);

                }


            }
        });


    }

    private void requestUpadte(AppUpdateInfo appUpdateInfo)
    {

        try {
            appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    MY_REQUEST_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    /* google in app  update */


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {

                if(dispDefaultDialog() && !isSetdefault()) {
                    defaultBrowerDialog();
                    getDeviceLocation();
                }

            } else if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, permissionsRequired[0]) || ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, permissionsRequired[1])) {

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
                builder.setTitle(mContext.getResources().getString(R.string.need_permission));
                builder.setMessage(mContext.getString(R.string.app_name) + " "+mContext.getResources().getString(R.string.storage_needed));
                builder.setPositiveButton(mContext.getResources().getString(R.string.grant), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions((Activity) mContext,permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton(mContext.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                        if(dispDefaultDialog() && !isSetdefault()) {
                            defaultBrowerDialog();
                        }
                    }
                });
                builder.show();
            } else {
                //Toast.makeText(mContext, "Unable to get Permission", Toast.LENGTH_LONG).show();
                //jmmToast.show(mContext, getResources().getString(R.string.unable_get_permission));
            }
        }


    }

    private void initNavControls(View rowView, final boolean isIncog) {

        LinearLayout nav_home=rowView.findViewById(R.id.nav_home);
        LinearLayout nav_menu=rowView.findViewById(R.id.nav_menu);
        tabCount=rowView.findViewById(R.id.omniTabCount);
        LinearLayout tabCountLayout= rowView.findViewById(R.id.tabCountLayout);

        ///tabCount.setText(""+getTabCount());
        tabCount.setText(""+BrowserContainer.size());


        nav_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(Helper.mHomeLayout!=null) {
                //setUpNewsTabs(Helper.mHomeLayout);//sp
                //}

                // addAlbum(BrowserUtility.FLAG_HOME);
                if(currentAlbumController.getIsInCogTab())
                {
                    updateAlbumIncog();
                }
                else {

                    /*if already  at  home   dont call  updatealbum*/ // tks  on  26-11-2019
                    if(currentAlbumController.getFlag()!=BrowserUtility.FLAG_HOME) {
                        updateAlbum();
                    }
                }

            }
        });

        nav_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenuDialog();


            }
        });


        tabCountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideKeyBoard(view);

                if (switcherPanel.getStatus() != SwitcherPanel.Status.COLLAPSED) {
                    switcherPanel.collapsed();
                }


            }
        });

    }

    private int getHeightInPercentage(int perValue) {

        int Height=ViewUnit.getWindowHeight(mContext);
        int per=(Height*perValue)/100;
        return per;

    }


    private void setTitleBackColorForIncogAndMainTab(View viewById,boolean isInCog) {


        if(isInCog) {
            viewById.setBackgroundColor(ContextCompat.getColor(mContext, R.color.background_dark));
            omnibox.setBackgroundColor(ContextCompat.getColor(mContext, R.color.incog_back));
            main_view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.incog_back));

            updatePrefs(1);
            // here
            updateAutoComplete(isInCog);
        }
        else {
            viewById.setBackgroundColor(ContextCompat.getColor(mContext, R.color.title_back_color));
            omnibox.setBackgroundColor(ContextCompat.getColor(mContext, R.color.screen_background));
            main_view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.screen_background));
            updatePrefs(0);
            //here
            updateAutoComplete(isInCog);
        }


    }

    private void setTitleBackColorForIncogAndMainTab(View viewById,String colorCode) {


        /*set the incog tab and history flag to  record history or not using the back color of title textview ofalbum.xml file that is set while
         * creating inCogTab*/

        String color=getResources().getString(R.string.gray_900);
        System.out.print(""+color);
        if(colorCode.equalsIgnoreCase(color)) {
            viewById.setBackgroundColor(ContextCompat.getColor(mContext, R.color.background_dark));
            updatePrefs(1);
        }
        else {
            viewById.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue_500));
            updatePrefs(0);
        }


    }
    /*update the preference variable whether the history will  be added or not like in incog tab it will set to  false*/
    private void updatePrefs(int flag)
    {
        if(prefs!=null)
        {
            if(flag==1)
                prefs.setValue(getResources().getString(R.string.sp_add_history), false);
            else
                prefs.setValue(getResources().getString(R.string.sp_add_history), true);
        }
    }


    @Override
    public synchronized void removeAlbum(AlbumController controller) {
        if (currentAlbumController == null || BrowserContainer.size() <= 1) {
            switcherContainer.removeView(controller.getAlbumView());
            BrowserContainer.remove(controller);
            addAlbum(BrowserUtility.FLAG_HOME);
            // decreaseTabCount();
            return;
        }
        if (controller != currentAlbumController) {
            switcherContainer.removeView(controller.getAlbumView());
            BrowserContainer.remove(controller);
            //decreaseTabCount();
        } else {
            switcherContainer.removeView(controller.getAlbumView());
            int index = BrowserContainer.indexOf(controller);
            BrowserContainer.remove(controller);
            if (index >= BrowserContainer.size()) {
                index = BrowserContainer.size() - 1;
            }
            showAlbum(BrowserContainer.get(index), false, false, false);

            //decreaseTabCount();
        }
    }


    @Override
    public void updateAutoComplete() {
        RecordAction action = new RecordAction(this);
        action.open(false);
        List<Record> list = action.listBookmarks();
        list.addAll(action.listHistory());
        action.close();

        final CompleteAdapter adapter = new CompleteAdapter(this, R.layout.complete_item, list);
        inputBox.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            inputBox.setDropDownVerticalOffset(getResources().getDimensionPixelOffset(R.dimen.layout_height_6dp));
        }
        inputBox.setDropDownWidth(ViewUnit.getWindowWidth(this));

        inputBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = ((TextView) view.findViewById(R.id.complete_item_url)).getText().toString();
                if(BrowserUtility.isUrlBlocked(mContext,prefs,url))
                {
                    jmmToast.show(mContext, mContext.getString(R.string.blocked_url));
                    return;
                }
                inputBox.setText(Html.fromHtml(BrowserUtility.urlWrapper(url)), EditText.BufferType.SPANNABLE);
                inputBox.setSelection(url.length());
                updateAlbum(url);
                hideKeyBoard(inputBox);
            }
        });
    }

    private void updateAutoComplete(boolean isIncog)
    {

        if(isIncog)
        {
            // setting adapter null  stops autocomplete suggestion
            inputBox.setAdapter(null);
        }
        else
        {
            //enable autocomlete for  the  inputbox
            updateAutoComplete();
        }
    }

    @Override
    public void updateBookmarks() {
        if (currentAlbumController == null || !(currentAlbumController instanceof jmmWebView)) {
            omniboxBookmark.setImageDrawable(ViewUnit.getDrawable(this, R.drawable.bookmark_selector_dark));
            return;
        }

        RecordAction action = new RecordAction(this);
        action.open(false);
        String url = ((jmmWebView) currentAlbumController).getUrl();
        if (action.checkBookmark(url)) {
            omniboxBookmark.setImageDrawable(ViewUnit.getDrawable(this, R.drawable.bookmark_selector_blue));
        } else {
            omniboxBookmark.setImageDrawable(ViewUnit.getDrawable(this, R.drawable.bookmark_selector_dark));
        }
        action.close();
    }

    @Override
    public void updateInputBox(String query) {
        if (query != null) {
            inputBox.setText(Html.fromHtml(BrowserUtility.urlWrapper(query)), EditText.BufferType.SPANNABLE);
        } else {
            inputBox.setText(null);
        }
        inputBox.clearFocus();
    }

    private void updateOmnibox() {
        if (currentAlbumController == null) {
            return;
        }

        if (currentAlbumController instanceof jmmRelativeLayout) {
            updateProgress(BrowserUtility.PROGRESS_MAX);
            updateBookmarks();
            updateInputBox(null);
        } else if (currentAlbumController instanceof jmmWebView) {
            jmmWebView customWebView = (jmmWebView) currentAlbumController;
            updateProgress(customWebView.getProgress());
            updateBookmarks();
            if (customWebView.getUrl() == null && customWebView.getOriginalUrl() == null) {
                updateInputBox(null);
            } else if (customWebView.getUrl() != null) {
                updateInputBox(customWebView.getUrl());
            } else {
                updateInputBox(customWebView.getOriginalUrl());
            }
        }
    }

    @Override
    public synchronized void updateProgress(int progress) {
        if (progress > progressBar.getProgress()) {
            ObjectAnimator animator = ObjectAnimator.ofInt(progressBar, "progress", progress);
            animator.setDuration(shortAnimTime);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.start();
        } else if (progress < progressBar.getProgress()) {
            ObjectAnimator animator = ObjectAnimator.ofInt(progressBar, "progress", 0, progress);
            animator.setDuration(shortAnimTime);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.start();
        }

        updateBookmarks();
        if (progress < BrowserUtility.PROGRESS_MAX) {
            updateRefresh(true);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            updateRefresh(false);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void updateRefresh(boolean running) {
        if (running) {
            omniboxRefresh.setImageDrawable(ViewUnit.getDrawable(this, R.drawable.cl_selector_dark));
        } else {
            omniboxRefresh.setImageDrawable(ViewUnit.getDrawable(this, R.drawable.refresh_selector));
        }
    }

    @Override
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        // Because Activity launchMode is singleInstance,
        // so we can not get result from onActivityResult when Android 4.X,
        // what a pity
        //
        // this.uploadMsg = uploadMsg;
        // Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // intent.addCategory(Intent.CATEGORY_OPENABLE);
        // intent.setType("*/*");
        // startActivityForResult(Intent.createChooser(intent, getString(R.string.main_file_chooser)), IntentUtility.REQUEST_FILE_16);
        uploadMsg.onReceiveValue(null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);

        FrameLayout layout = (FrameLayout) getLayoutInflater().inflate(R.layout.dialog_desc, null, false);
        TextView textView = (TextView) layout.findViewById(R.id.dialog_desc);
        textView.setText(R.string.dialog_content_upload);

        builder.setView(layout);
        builder.create().show();
    }

    @Override
    public void showFileChooser(ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.filePathCallback = filePathCallback;

            try {
                Intent intent = fileChooserParams.createIntent();
                startActivityForResult(intent, IntentUtility.REQUEST_FILE_21);
            } catch (Exception e) {
                jmmToast.show(this, R.string.toast_open_file_manager_failed);
            }
        }
    }

    @Override
    public void onCreateView(WebView view, final Message resultMsg) {
        if (resultMsg == null) {
            return;
        }

        switcherPanel.collapsed();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addAlbum(getString(R.string.album_untitled), null, true, resultMsg);
            }
        }, shortAnimTime);
    }

    @Override
    public boolean onShowCustomView(View view, int requestedOrientation, WebChromeClient.CustomViewCallback callback) {
        return onShowCustomView(view, callback);
    }

    @Override
    public boolean onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        if (view == null) {
            return false;
        }
        if (customView != null && callback != null) {
            callback.onCustomViewHidden();
            return false;
        }

        customView = view;
        originalOrientation = getRequestedOrientation();

        fullscreenHolder = new FullscreenHolder(this);
        fullscreenHolder.addView(
                customView,
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                ));

        FrameLayout decorView = (FrameLayout) getWindow().getDecorView();
        decorView.addView(
                fullscreenHolder,
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                ));

        customView.setKeepScreenOn(true);
        ((View) currentAlbumController).setVisibility(View.GONE);
        setCustomFullscreen(true);

        if (view instanceof FrameLayout) {
            if (((FrameLayout) view).getFocusedChild() instanceof VideoView) {
                videoView = (VideoView) ((FrameLayout) view).getFocusedChild();
                videoView.setOnErrorListener(new VideoCompletionListener());
                videoView.setOnCompletionListener(new VideoCompletionListener());
            }
        }
        customViewCallback = callback;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); // Auto landscape when video shows

        return true;
    }

    @Override
    public boolean onHideCustomView() {
        if (customView == null || customViewCallback == null || currentAlbumController == null) {
            return false;
        }

        FrameLayout decorView = (FrameLayout) getWindow().getDecorView();
        if (decorView != null) {
            decorView.removeView(fullscreenHolder);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            try {
                customViewCallback.onCustomViewHidden();
            } catch (Throwable t) {}
        }

        customView.setKeepScreenOn(false);
        ((View) currentAlbumController).setVisibility(View.VISIBLE);
        setCustomFullscreen(false);

        fullscreenHolder = null;
        customView = null;
        if (videoView != null) {
            videoView.setOnErrorListener(null);
            videoView.setOnCompletionListener(null);
            videoView = null;
        }
        setRequestedOrientation(originalOrientation);

        return true;
    }

    @Override
    public void onLongPress(String url) {
        WebView.HitTestResult result;
        if (!(currentAlbumController instanceof jmmWebView)) {
            return;
        }
        result = ((jmmWebView) currentAlbumController).getHitTestResult();

        final List<String> list = new ArrayList<>();
        list.add(getString(R.string.main_menu_new_tab));
        list.add(getString(R.string.main_menu_copy_link));   //  uncomment it to  add  copy link options
        if (result != null && (result.getType() == WebView.HitTestResult.IMAGE_TYPE || result.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE)) {
            list.add(getString(R.string.main_menu_download));

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);

        FrameLayout layout = (FrameLayout) getLayoutInflater().inflate(R.layout.dialog_list, null, false);
        builder.setView(layout);

        ListView listView = (ListView) layout.findViewById(R.id.dialog_list);
        DialogAdapter adapter = new DialogAdapter(this, R.layout.dialog_text_item, list);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        final AlertDialog dialog = builder.create();
        if (url != null || (result != null && result.getExtra() != null)) {
            if (url == null) {
                url = result.getExtra();
            }
            dialog.show();
        }
        // here 1
        final String target = url;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = list.get(position);
                if (s.equals(getString(R.string.main_menu_new_tab))) { // New tab
                    addAlbum(getString(R.string.album_untitled), target, false, null);
                    jmmToast.show(mContext, R.string.toast_new_tab_successful);
                    // increaseTabCount();
                    setTabValue(BrowserContainer.size());
                } else if (s.equals(getString(R.string.main_menu_copy_link))) { // Copy link
                    BrowserUtility.copyURL(mContext, target);
                } else if (s.equals(getString(R.string.main_menu_download))) { // Save
                    //BrowserUtility.download(mContext, target, target, BrowserUtility.MIME_TYPE_IMAGE);

                    // DownLoadUtility downLoadUtility=new DownLoadUtility(mContext);
                    // downLoadUtility.download(mContext, target, target, BrowserUtility.MIME_TYPE_IMAGE);

                    startAndBindDownloadService();

                    if(CommonUtility.isWriteStoragePermissionGranted(MainActivity.this)){
                        if(downloadBinder!=null) {
                            downloadBinder.startDownload(mContext, target, 0);
                            //downloadBinder.startDownload("http://bdigimedia.com/development/callrecorder/Screenshot_20200114-144304.png", 0);
                        }
                    }
                }

                dialog.hide();
                dialog.dismiss();
            }
        });
    }

    private boolean onKeyCodeVolumeUp() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        int vc = Integer.valueOf(sp.getString(getString(R.string.sp_volume), "1"));

        if (vc == 0) { // Switch tabs
            if (switcherPanel.isKeyBoardShowing()) {
                return true;
            }

            AlbumController controller = nextAlbumController(false);
            showAlbum(controller, false, false, true);
            jmmToast.show(this, controller.getAlbumTitle());

            return true;
        } else if (vc == 1 && currentAlbumController instanceof jmmWebView) { // Scroll webpage
            jmmWebView customWebView = (jmmWebView) currentAlbumController;
            int height = customWebView.getMeasuredHeight();
            int scrollY = customWebView.getScrollY();
            int distance = Math.min(height, scrollY);

            ObjectAnimator anim = ObjectAnimator.ofInt(customWebView, "scrollY", scrollY, scrollY - distance);
            anim.setDuration(mediumAnimTime);
            anim.start();

            return true;
        }

        return false;
    }

    private boolean onKeyCodeVolumeDown() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        int vc = Integer.valueOf(sp.getString(getString(R.string.sp_volume), "1"));

        if (vc == 0) { // Switch tabs
            if (switcherPanel.isKeyBoardShowing()) {
                return true;
            }

            AlbumController controller = nextAlbumController(true);
            showAlbum(controller, false, false, true);
            jmmToast.show(this, controller.getAlbumTitle());

            return true;
        } else if (vc == 1 && currentAlbumController instanceof jmmWebView) {
            jmmWebView customWebView = (jmmWebView) currentAlbumController;
            int height = customWebView.getMeasuredHeight();
            int scrollY = customWebView.getScrollY();
            int surplus = (int) (customWebView.getContentHeight() * ViewUnit.getDensity(this) - height - scrollY);
            int distance = Math.min(height, surplus);

            ObjectAnimator anim = ObjectAnimator.ofInt(customWebView, "scrollY", scrollY, scrollY + distance);
            anim.setDuration(mediumAnimTime);
            anim.start();

            return true;
        }

        return false;
    }


    /*************onBackPressed*****onKeyCodeBack********************/
    private boolean onKeyCodeBack(boolean douQ) {
        hideKeyBoard(inputBox);

        if (switcherPanel.getStatus() != SwitcherPanel.Status.EXPANDED) {
            switcherPanel.expanded();
        } else if (currentAlbumController == null) {
            finish();
        } else if (currentAlbumController instanceof jmmWebView) {
            jmmWebView customWebView = (jmmWebView) currentAlbumController;
            if (customWebView.canGoBack()) {
                customWebView.goBack();
            } else {
                boolean st=customWebView.getIsInCogTab();
                if(st)
                    updateAlbumIncog();
                else updateAlbum();
            }
        } else if (currentAlbumController instanceof jmmRelativeLayout) {
            switch (currentAlbumController.getFlag()) {
                case BrowserUtility.FLAG_BOOKMARKS:
                    //updateAlbum();  as per pre written code
                    if(currentAlbumController!=null) {

                        removeAlbum(currentAlbumController);

                        //dispFbInteresTialAdds();
                    }
                    hideomnibox(false);
                    break;
                case BrowserUtility.FLAG_HISTORY:
                    //updateAlbum(); as per pre written code
                    if(currentAlbumController!=null) {

                        removeAlbum(currentAlbumController);

                        //dispFbInteresTialAdds();
                    }
                    hideomnibox(false);
                    break;
                case BrowserUtility.FLAG_INCOG:
                {
                    //updateAlbumIncog();
                    if (douQ) {
                        // doubleTapsQuit();

                        if(isDispExitDialog())
                            exitDialog();
                        else
                            doubleTapsQuit();
                    }
                }
                case BrowserUtility.FLAG_HOME:
                    if (douQ) {

//                        if(Helper.mHomeLayout!=null) {
//                            setupNewsdata(Helper.mHomeLayout);
//                        }

                        // doubleTapsQuit();
                        if(isDispExitDialog())
                            exitDialog();
                        else
                            doubleTapsQuit();
                    }
                    break;
                default:
                    finish();
                    break;
            }
        } else {
            finish();
        }
        return true;
    }





    private void doubleTapsQuit() {
        final Timer timer = new Timer();
        if (!quit) {
            quit = true;
            jmmToast.show(this, R.string.toast_double_taps_quit);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    quit = false;
                    timer.cancel();
                }
            }, DOUBLE_TAPS_QUIT_DEFAULT);
        } else {
            timer.cancel();
            finish();
        }
    }

    private void hideKeyBoard(View view) {
        view.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showSoftInput(View view) {
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void hideSearchPanel() {
        hideKeyBoard(searchBox);
        searchBox.setText("");
        searchPanel.setVisibility(View.GONE);
        omnibox.setVisibility(View.VISIBLE);
    }

    private void showSearchPanel() {
        omnibox.setVisibility(View.GONE);
        searchPanel.setVisibility(View.VISIBLE);
        showSoftInput(searchBox);
    }

    private boolean showOverflow() {
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);

        FrameLayout layout = (FrameLayout) getLayoutInflater().inflate(R.layout.dialog_list, null, false);
        builder.setView(layout);

        final String[] array = getResources().getStringArray(R.array.main_overflow);
        final List<String> stringList = new ArrayList<>();
        stringList.addAll(Arrays.asList(array));
        if (currentAlbumController != null && currentAlbumController instanceof jmmRelativeLayout) {
            stringList.remove(array[0]); // Go to top
            stringList.remove(array[1]); // Add to home
            stringList.remove(array[2]); // Find in page
            stringList.remove(array[3]); // Screenshot
            stringList.remove(array[4]); // Readability
            stringList.remove(array[5]); // Share

            jmmRelativeLayout ninjaRelativeLayout = (jmmRelativeLayout) currentAlbumController;
            if (ninjaRelativeLayout.getFlag() != BrowserUtility.FLAG_HOME) {
                stringList.remove(array[6]); // Relayout
            }
        } else if (currentAlbumController != null && currentAlbumController instanceof jmmWebView) {
            if (!sp.getBoolean(getString(R.string.sp_readability), false)) {
                stringList.remove(array[4]); // Readability
            }
            stringList.remove(array[6]); // Relayout
        }

        ListView listView = (ListView) layout.findViewById(R.id.dialog_list);
        DialogAdapter dialogAdapter = new DialogAdapter(this, R.layout.dialog_text_item, stringList);
        listView.setAdapter(dialogAdapter);
        dialogAdapter.notifyDataSetChanged();

        final AlertDialog dialog = builder.create();
        dialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String s = stringList.get(position);
                if (s.equals(array[0])) { // Go to top
                    jmmWebView customWebView = (jmmWebView) currentAlbumController;
                    ObjectAnimator anim = ObjectAnimator.ofInt(customWebView, "scrollY", customWebView.getScrollY(), 0);
                    anim.setDuration(mediumAnimTime);
                    anim.start();
                } else if (s.equals(array[1])) { // Add to home
                    jmmWebView customWebView = (jmmWebView) currentAlbumController;
                    RecordAction action = new RecordAction(mContext);
                    action.open(true);
                    if (action.checkGridItem(customWebView.getUrl())) {
                        jmmToast.show(mContext, R.string.toast_already_exist_in_home);
                    } else {
                        String title = customWebView.getTitle().trim();
                        String url = customWebView.getUrl().trim();
                        Bitmap bitmap = ViewUnit.capture(customWebView, dimen156dp, dimen117dp, false, Bitmap.Config.ARGB_8888);
                        String filename = System.currentTimeMillis() + BrowserUtility.SUFFIX_PNG;
                        int ordinal = action.listGrid().size();
                        GridItem item = new GridItem(title, url, filename, ordinal);

                        if (BrowserUtility.bitmap2File(mContext, bitmap, filename) && action.addGridItem(item)) {
                            jmmToast.show(mContext, R.string.toast_add_to_home_successful);
                        } else {
                            jmmToast.show(mContext, R.string.toast_add_to_home_failed);
                        }
                    }
                    action.close();
                } else if (s.equals(array[2])) { // Find in page
                    hideKeyBoard(inputBox);
                    showSearchPanel();
                } else if (s.equals(array[3])) { // Screenshot
                    jmmWebView customWebView = (jmmWebView) currentAlbumController;
                    new ScreenshotTask(mContext, customWebView).execute();
                } else if (s.equals(array[4])) { // Readability
                    String token = sp.getString(getString(R.string.sp_readability_token), null);
                    if (token == null || token.trim().isEmpty()) {
                        jmmToast.show(mContext, R.string.toast_token_empty);
                    } else {
                      /*  jmmWebView customWebView = (jmmWebView) currentAlbumController;
                        Intent intent = new Intent(mContext, ReadabilityActivity.class);
                        intent.putExtra(IntentUtility.URL, customWebView.getUrl());
                        startActivity(intent);*/
                    }
                } else if (s.equals(array[5])) { // Share
                    if (!prepareRecord()) {
                        jmmToast.show(mContext, R.string.toast_share_failed);
                    } else {
                        jmmWebView customWebView = (jmmWebView) currentAlbumController;
                        IntentUtility.share(mContext, customWebView.getTitle(), customWebView.getUrl());
                    }
                } else if (s.equals(array[6])) { // Relayout
                    jmmRelativeLayout ninjaRelativeLayout = (jmmRelativeLayout) currentAlbumController;
                    final DynamicGridView gridView = (DynamicGridView) ninjaRelativeLayout.findViewById(R.id.home_grid);
                    final List<GridItem> gridList = ((GridAdapter) gridView.getAdapter()).getList();

                    omnibox.setVisibility(View.GONE);
                    relayoutOK.setVisibility(View.VISIBLE);

                    relayoutOK.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                relayoutOK.setTextColor(getResources().getColor(R.color.blue_500));
                            } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                                relayoutOK.setTextColor(getResources().getColor(R.color.white));
                            }

                            return false;
                        }
                    });

                    relayoutOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            gridView.stopEditMode();
                            relayoutOK.setVisibility(View.GONE);
                            omnibox.setVisibility(View.VISIBLE);

                            RecordAction action = new RecordAction(mContext);
                            action.open(true);
                            action.clearGrid();
                            for (GridItem item : gridList) {
                                action.addGridItem(item);
                            }
                            action.close();
                            jmmToast.show(mContext, R.string.toast_relayout_successful);
                        }
                    });

                    gridView.setOnDragListener(new DynamicGridView.OnDragListener() {
                        private GridItem dragItem;

                        @Override
                        public void onDragStarted(int position) {
                            dragItem = gridList.get(position);
                        }

                        @Override
                        public void onDragPositionsChanged(int oldPosition, int newPosition) {
                            if (oldPosition < newPosition) {
                                for (int i = newPosition; i > oldPosition; i--) {
                                    GridItem item = gridList.get(i);
                                    item.setOrdinal(i - 1);
                                }
                            } else if (oldPosition > newPosition) {
                                for (int i = newPosition; i < oldPosition; i++) {
                                    GridItem item = gridList.get(i);
                                    item.setOrdinal(i + 1);
                                }
                            }
                            dragItem.setOrdinal(newPosition);

                            Collections.sort(gridList, new Comparator<GridItem>() {
                                @Override
                                public int compare(GridItem first, GridItem second) {
                                    if (first.getOrdinal() < second.getOrdinal()) {
                                        return -1;
                                    } else if (first.getOrdinal() > second.getOrdinal()) {
                                        return 1;
                                    } else {
                                        return 0;
                                    }
                                }
                            });
                        }
                    });
                    gridView.startEditMode();
                } else if (s.equals(array[7])) { // Quit
                    finish();
                }

                dialog.hide();
                dialog.dismiss();
            }
        });

        return true;
    }

    private void showGridMenu(final GridItem gridItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);

        FrameLayout layout = (FrameLayout) getLayoutInflater().inflate(R.layout.dialog_list, null, false);
        builder.setView(layout);

        final String[] array = getResources().getStringArray(R.array.list_menu);
        final List<String> stringList = new ArrayList<>();
        stringList.addAll(Arrays.asList(array));
        stringList.remove(array[1]); // Copy link
        stringList.remove(array[2]); // Share

        ListView listView = (ListView) layout.findViewById(R.id.dialog_list);
        DialogAdapter dialogAdapter = new DialogAdapter(this, R.layout.dialog_text_item, stringList);
        listView.setAdapter(dialogAdapter);
        dialogAdapter.notifyDataSetChanged();

        final AlertDialog dialog = builder.create();
        dialog.show();
        //here 2
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String s = stringList.get(position);
                if (s.equals(array[0])) { // New tab
                    addAlbum(getString(R.string.album_untitled), gridItem.getURL(), false, null);
                    jmmToast.show(mContext, R.string.toast_new_tab_successful);
                    //increaseTabCount();
                    setTabValue(BrowserContainer.size());
                } else if (s.equals(array[3])) { // Edit
                    showEditDialog(gridItem);
                } else if (s.equals(array[4])) { // Delete
                    RecordAction action = new RecordAction(mContext);
                    action.open(true);
                    action.deleteGridItem(gridItem);
                    action.close();
                    mContext.deleteFile(gridItem.getFilename());
                    initHomeGrid((jmmRelativeLayout) currentAlbumController, true);
                    jmmToast.show(mContext, R.string.toast_delete_successful);
                }

                dialog.hide();
                dialog.dismiss();
            }
        });
    }

    private void showListMenu(final RecordAdapter recordAdapter, final List<Record> recordList, final int location) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);

        FrameLayout layout = (FrameLayout) getLayoutInflater().inflate(R.layout.dialog_list, null, false);
        builder.setView(layout);

        final String[] array = getResources().getStringArray(R.array.list_menu);
        final List<String> stringList = new ArrayList<>();
        stringList.addAll(Arrays.asList(array));
        if (currentAlbumController.getFlag() != BrowserUtility.FLAG_BOOKMARKS) {
            stringList.remove(array[3]);
        }

        ListView listView = (ListView) layout.findViewById(R.id.dialog_list);
        DialogAdapter dialogAdapter = new DialogAdapter(this, R.layout.dialog_text_item, stringList);
        listView.setAdapter(dialogAdapter);
        dialogAdapter.notifyDataSetChanged();

        final AlertDialog dialog = builder.create();
        dialog.show();

        final Record record = recordList.get(location);
        //here 3
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = stringList.get(position);
                if (s.equals(array[0])) { // New tab
                    addAlbum(getString(R.string.album_untitled), record.getURL(), false, null);
                    jmmToast.show(mContext, R.string.toast_new_tab_successful);
                    //increaseTabCount();
                    setTabValue(BrowserContainer.size());
                } else if (s.equals(array[1])) { // Copy link
                    BrowserUtility.copyURL(mContext, record.getURL());
                } else if (s.equals(array[2])) { // Share
                    IntentUtility.share(mContext, record.getTitle(), record.getURL());
                }

                /*else if (s.equals(array[3])) { // Edit
                    showEditDialog(recordAdapter, recordList, location);
                }*/


                else if (s.equals(array[3])) { // Delete
                    RecordAction action = new RecordAction(mContext);
                    action.open(true);
                    if (currentAlbumController.getFlag() == BrowserUtility.FLAG_BOOKMARKS) {
                        action.deleteBookmark(record);
                    } else if (currentAlbumController.getFlag() == BrowserUtility.FLAG_HISTORY) {
                        action.deleteHistory(record);
                    }
                    action.close();

                    recordList.remove(location);
                    recordAdapter.notifyDataSetChanged();

                    updateBookmarks();
                    updateAutoComplete();

                    jmmToast.show(mContext, R.string.toast_delete_successful);
                }

                dialog.hide();
                dialog.dismiss();
            }
        });
    }

    //deletes  the bookmarks and histor on the basis of flag value;
    public  void deleteHB(Record record,int flag )
    {

        RecordAction action = new RecordAction(mContext);
        action.open(true);
        if(flag==BrowserUtility.FLAG_HISTORY)
            action.deleteHistory(record);
        else
            action.deleteBookmark(record);



        action.close();

    }

    private void showEditDialog(final GridItem gridItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);

        FrameLayout layout = (FrameLayout) getLayoutInflater().inflate(R.layout.dialog_edit, null, false);
        builder.setView(layout);

        final AlertDialog dialog = builder.create();
        dialog.show();

        final EditText editText = (EditText) layout.findViewById(R.id.dialog_edit);
        editText.setHint(R.string.dialog_title_hint);
        editText.setText(gridItem.getTitle());
        editText.setSelection(gridItem.getTitle().length());
        hideKeyBoard(inputBox);
        showSoftInput(editText);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId != EditorInfo.IME_ACTION_DONE) {
                    return false;
                }

                String text = editText.getText().toString().trim();
                if (text.isEmpty()) {
                    jmmToast.show(mContext, R.string.toast_input_empty);
                    return true;
                }

                RecordAction action = new RecordAction(mContext);
                action.open(true);
                gridItem.setTitle(text);
                action.updateGridItem(gridItem);
                action.close();

                hideKeyBoard(editText);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.hide();
                        dialog.dismiss();
                    }
                }, longAnimTime);
                return false;
            }
        });
    }

    private void showEditDialog(final RecordAdapter recordAdapter, List<Record> recordList, int location) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);

        FrameLayout layout = (FrameLayout) getLayoutInflater().inflate(R.layout.dialog_edit, null, false);
        builder.setView(layout);

        final AlertDialog dialog = builder.create();
        dialog.show();

        final Record record = recordList.get(location);
        final EditText editText = (EditText) layout.findViewById(R.id.dialog_edit);
        editText.setHint(R.string.dialog_title_hint);
        editText.setText(record.getTitle());
        editText.setSelection(record.getTitle().length());
        hideKeyBoard(inputBox);
        showSoftInput(editText);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId != EditorInfo.IME_ACTION_DONE) {
                    return false;
                }

                String text = editText.getText().toString().trim();
                if (text.isEmpty()) {
                    jmmToast.show(mContext, R.string.toast_input_empty);
                    return true;
                }

                RecordAction action = new RecordAction(mContext);
                action.open(true);
                record.setTitle(text);
                action.updateBookmark(record);
                action.close();

                recordAdapter.notifyDataSetChanged();
                hideKeyBoard(editText);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.hide();
                        dialog.dismiss();
                    }
                }, longAnimTime);
                return false;
            }
        });
    }

    private boolean prepareRecord() {

        if (currentAlbumController == null || !(currentAlbumController instanceof jmmWebView)) {
            return false;
        }

        jmmWebView webView = (jmmWebView) currentAlbumController;
        String title = webView.getTitle();
        String url = webView.getUrl();
        if (title == null
                || title.isEmpty()
                || url == null
                || url.isEmpty()
                || url.startsWith(BrowserUtility.URL_SCHEME_ABOUT)
                || url.startsWith(BrowserUtility.URL_SCHEME_MAIL_TO)
                || url.startsWith(BrowserUtility.URL_SCHEME_INTENT)) {
            return false;
        }
        return true;
    }

    private void setCustomFullscreen(boolean fullscreen) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        /*
         * Can not use View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION,
         * so we can not hide NavigationBar :(
         */
        int bits = WindowManager.LayoutParams.FLAG_FULLSCREEN;

        if (fullscreen) {
            layoutParams.flags |= bits;
        } else {
            layoutParams.flags &= ~bits;
            if (customView != null) {
                customView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            } else {
                contentFrame.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
        getWindow().setAttributes(layoutParams);
    }

    private AlbumController nextAlbumController(boolean next) {
        if (BrowserContainer.size() <= 1) {
            return currentAlbumController;
        }

        List<AlbumController> list = BrowserContainer.list();
        int index = list.indexOf(currentAlbumController);
        if (next) {
            index++;
            if (index >= list.size()) {
                index = 0;
            }
        } else {
            index--;
            if (index < 0) {
                index = list.size() - 1;
            }
        }

        return list.get(index);
    }


    private void changeTabsFont(TabLayout mTabLayout) {
        ViewGroup vg=null;

        vg = (ViewGroup) mTabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        Typeface type= CommonUtility.typeFace_Calibri_Bold(mContext);

        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView)
                {
                    ((TextView) tabViewChild).setTypeface(type);
                    ((TextView) tabViewChild).setAllCaps(false);

                }
            }
        }
    }
}
