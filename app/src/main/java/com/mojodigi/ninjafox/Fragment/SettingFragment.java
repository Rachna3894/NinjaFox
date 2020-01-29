package com.mojodigi.ninjafox.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojodigi.ninjafox.Activity.BlockListActivity;
import com.mojodigi.ninjafox.Activity.LanguagePrefsActivity;
import com.mojodigi.ninjafox.Activity.SettingActivity;
import com.mojodigi.ninjafox.Adapter.LanguageAdapter;
import com.mojodigi.ninjafox.Adapter.NewsCategoryAdapter;
import com.mojodigi.ninjafox.AddsUtility.AddMobUtils;
import com.mojodigi.ninjafox.Model.BlockListModel;
import com.mojodigi.ninjafox.Model.CategoryModel;
import com.mojodigi.ninjafox.Model.LanguageCategoryModel;
import com.mojodigi.ninjafox.Model.NewsCategoryModel;
import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.SharedPrefs.AppConstants;
import com.mojodigi.ninjafox.SharedPrefs.SharedPreferenceUtil;
import com.mojodigi.ninjafox.Task.ApiRequestTask;
import com.mojodigi.ninjafox.Unit.BrowserUtility;
import com.mojodigi.ninjafox.Unit.CommonUtility;
import com.mojodigi.ninjafox.Unit.JsonParser;
import com.mojodigi.ninjafox.View.jmmToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener, ApiRequestTask.JsonLoadListener {

    private ListPreference mDownLoadPrefs,mShowImagesPrefs   /*, mShowLanguagePrefs*/;
    private MultiSelectListPreference mClearDataPrefs;

    Preference feedback;
    Preference default_browserPrefs;
    private String[] downLoadEntries,showImagesEntries , showLanguageEntries;
    SharedPreferences settingsPrefs;

    private boolean spChange = false;

    public boolean isSPChange() {
        return spChange;
    }

    private boolean dbChange = false;

    SharedPreferenceUtil prefs;

    public boolean isDBChange() {
        return dbChange;
    }

    public void setDBChange(boolean dbChange) {
        this.dbChange = dbChange;
    }

    /*permission vars*/
    private String[] permissionsRequired = new String[]{Manifest.permission.CALL_PHONE};
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;

    /*permission vars*/

    private RecyclerView newsCategoryRecycler;
    private NewsCategoryAdapter newsCategoryAdapter;

   SettingFragment mInstance;
    private ArrayList<NewsCategoryModel> newsCategoryList;
    private ArrayList<NewsCategoryModel> prefJsonNewsCategoryList;
    static ArrayList<NewsCategoryModel> prefNewsCategory ;

    PreferenceCategory  defaultBrowserCategoty;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_setting);

        AddMobUtils addMobUtils=CommonUtility.getAddMobInstance();
        addMobUtils.dispFacebookInterestialAdds(getActivity());

        permissionStatus = getActivity().getSharedPreferences("permissionStatus", getActivity().MODE_PRIVATE);
        prefs = new SharedPreferenceUtil(getActivity());
           mInstance=this;


        Preference news_language_prefs=findPreference(getActivity().getResources().getString(R.string.sp_language));
        PreferenceCategory  languageCategoty= (PreferenceCategory) findPreference(getActivity().getResources().getString(R.string.setting_title_Language));


        PreferenceCategory  deleteAccountCategoty= (PreferenceCategory) findPreference(getActivity().getResources().getString(R.string.sp_delete_account));
        Preference delete_account_prefs=findPreference(getActivity().getResources().getString(R.string.sp_delete_account_summary));

        Preference log_out_prefs=findPreference(getActivity().getResources().getString(R.string.sp_log_out_summary));
        PreferenceCategory  logOutCategoty= (PreferenceCategory) findPreference(getActivity().getResources().getString(R.string.sp_log_out));




        if(!CommonUtility.isLoggedInUser(prefs)) {
            //first  remove  preference
            languageCategoty.removePreference(news_language_prefs);

            logOutCategoty.removePreference(log_out_prefs);
            deleteAccountCategoty.removePreference(delete_account_prefs);

            // then remove category
            PreferenceScreen screen = getPreferenceScreen();

            screen.removePreference(languageCategoty);
            screen.removePreference(logOutCategoty);
            screen.removePreference(deleteAccountCategoty);
        }


        /*default broser*/

        defaultBrowserCategoty= (PreferenceCategory) findPreference(getActivity().getResources().getString(R.string.sp_browser_setting));
        default_browserPrefs=findPreference(getActivity().getResources().getString(R.string.sp_default_browser));

        if(!CommonUtility.checkAndroidSdkVersion()) {
            //first  remove  preference
            defaultBrowserCategoty.removePreference(default_browserPrefs);

        }



        default_browserPrefs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                try {

                    Intent defaultSetingsIntent = new Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS);
                    //  startActivityForResult(callGPSSettingIntent,100);
                    startActivity(defaultSetingsIntent);
                    jmmToast.show(getActivity(), R.string.default_set_msg);
                }
                catch (ActivityNotFoundException e)
                {
                    jmmToast.show(getActivity(), getActivity().getResources().getString(R.string.default_browser_error));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                return false;
            }
        });


        /*default broser*/


        delete_account_prefs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                JSONObject delObj=new JSONObject();
                try {
                    delObj.put("userId",prefs.getStringValue(AppConstants.PREFS_USER_ID, "0"));
                    delObj.put("token",prefs.getStringValue(AppConstants.PREFS_TOKEN, "0"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                new ApiRequestTask(getActivity(),mInstance , CommonUtility.API_DELETE_ACCOUNT, false, true, "Wait...",delObj.toString() , AppConstants.deleteAccountApiRequestCode).execute();
                return false;
            }
        });

        news_language_prefs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent=new Intent(getActivity(), LanguagePrefsActivity.class);
                startActivity(intent);
                return false;
            }
        });

        log_out_prefs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                JSONObject logoutObject=new JSONObject();
                try {
                    logoutObject.put("userId",prefs.getStringValue(AppConstants.PREFS_USER_ID, "0"));
                    logoutObject.put("token",prefs.getStringValue(AppConstants.PREFS_TOKEN, "0"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new ApiRequestTask(getActivity(),mInstance , CommonUtility.API_LOG_OUT, false, true, "Wait...",logoutObject.toString() , AppConstants.logoutApiRequestCode).execute();

                return false;
            }
        });





    }





    @Override
    public void onResume() {
        super.onResume();

        settingsPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        sp.registerOnSharedPreferenceChangeListener(this);

        /*download prefs*/
        downLoadEntries = getResources().getStringArray(R.array.setting_entries_download);
        showImagesEntries = getResources().getStringArray(R.array.setting_entries_show_images);
        //showLanguageEntries = getResources().getStringArray(R.array.setting_entries_languages);

        mDownLoadPrefs = (ListPreference) findPreference(getString(R.string.sp_download));
        mShowImagesPrefs = (ListPreference) findPreference(getString(R.string.sp_show_images));
        //mShowLanguagePrefs = (ListPreference) findPreference(getString(R.string.sp_language));



        String  dFlag = sp.getString(getString(R.string.sp_download), "0");
        String dSummary="";

        if(dFlag.equalsIgnoreCase("0"))
        {
            dSummary=getString(R.string.download_setting_both);
        }
        else if(dFlag.equalsIgnoreCase("1"))
        {
            dSummary=getString(R.string.download_setting_wifi);
        }
        else if(dFlag.equalsIgnoreCase("2"))
        {
            dSummary=getString(R.string.download_setting_mobile);
        }
        else
        {
            dSummary=getString(R.string.download_setting_both);
        }
        mDownLoadPrefs.setSummary(""+dSummary);

        /*download prefs*/



        /*show images prefs*/
        String  showFlag = sp.getString(getString(R.string.sp_show_images), "0");
        String showSummary="";

        if(showFlag.equalsIgnoreCase("0"))
        {
            showSummary=getString(R.string.show_image_always);
        }
        else if(showFlag.equalsIgnoreCase("1"))
        {
            showSummary=getString(R.string.show_image_wifi);
        }
        else if(showFlag.equalsIgnoreCase("2"))
        {
            showSummary=getString(R.string.show_image_blocked);
        }
        else
        {
            showSummary=getString(R.string.show_image_always);
        }
        mShowImagesPrefs.setSummary(showSummary);
        /*show images prefs*/



        /*Set Summary Default Browser Prefs*/
        default_browserPrefs.setSummary(getActivity().getResources().getString(R.string.current_default_browser)+getDefaultBrowserName());
        /*Set Summary Default Browser Prefs*/


        /*clear  data  prefs*/

        mClearDataPrefs=(MultiSelectListPreference)findPreference(getString(R.string.sp_clear_data));
        mClearDataPrefs.setDefaultValue(getResources().getStringArray(R.array.values_clear_data_default));    /*  <!--initilize with  blank  array to  keep all unselected-->*/

        mClearDataPrefs.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                int a =23_12;

                Set<String> selected   = (Set) o;
                System.out.print(""+selected);
                Iterator<String> itr = selected.iterator();

                while(itr.hasNext()){
                    String selectedOption = itr.next();

                    /**/
                    switch (selectedOption)
                    { case ("0"):  // form  data
                    {
                        BrowserUtility.clearFormData(getActivity());
                        break;
                    }
                        case ("1"):
                        {
                            BrowserUtility.clearHistory(getActivity());
                            break;
                        }

                        case ("2"):
                        {
                            BrowserUtility.clearBookmarks(getActivity());
                            break;
                        }
                        case ("3"):
                        {
                            BrowserUtility.clearCookie(getActivity());
                            break;
                        }
                        case ("4"):
                        {
                            BrowserUtility.clearCache(getActivity());
                            break;
                        }
                    }
                    /**/


                }
                jmmToast.show(getActivity(), R.string.toast_clear_successful);

                return false;
            }
        });

        /*clear  data  prefs*/



        Preference verPref = findPreference(getString(R.string.setting_title_version));
        if (verPref != null) {
            String version = "";
            try {
                PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                version = pInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            verPref.setSummary(version);
        }

        final CheckBoxPreference sp_location = (CheckBoxPreference) findPreference(getString(R.string.sp_location));


        boolean st = isGpsEnabled();

        if (checkIfLocationOpened()) {
            sp_location.setChecked(true);
        }
        else
        {
            sp_location.setChecked(false);
        }


        sp_location.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {


                if (sp_location.isChecked()) {
                    disableLocation();

                } else {
                    statusCheck();
                }
                return false;
            }
        });

        /* <!-- for the time being not required-->*/
       /* final CheckBoxPreference sp_passwords = (CheckBoxPreference) findPreference(getString(R.string.sp_passwords));
        sp_passwords.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                if (sp_passwords.isChecked()) {
                    setPassWordPrefs(false);
                    sp_passwords.setChecked(false);
                } else {
                    setPassWordPrefs(true);
                    sp_passwords.setChecked(true);
                }
                return false;
            }
        });*/
        /* <!-- for the time being not required-->*/

        // set  the download path  in perefrence screen;

        String dPath=  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        Preference dPathPrefs = findPreference(getString(R.string.download_path_0));
        if (dPath != null && dPathPrefs != null) {
            dPathPrefs.setSummary(dPath);
        }

        /*  boolean dstatus= CommonUtility.checkOrCreateDownloadDirectory(getActivity());
        if(dstatus) {
            File file =  new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+AppConstants.downloadDirectory);
            String dPath = file.getAbsolutePath();
            Preference dPathPrefs = findPreference(getString(R.string.download_path_0));
            if (dPath != null && dPathPrefs != null) {
                dPathPrefs.setSummary(dPath);
            }
        }
*/



        /*feedback*/
/*

        feedback=findPreference(getString(R.string.setting_title_feedback));
        feedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

               Intent feedbackIntent= new Intent(getActivity(), FeedBackActivity.class);
               startActivity(feedbackIntent);

                return false;

            }
        });

*/

        /*feedback*/




        /*call  us */

        Preference call_is_prefs=findPreference(getActivity().getResources().getString(R.string.sp_place_call));
        call_is_prefs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                askForPermission();
                return false;
            }
        });
        /*call  us */

        /*email  us */
        Preference email_us=findPreference(getActivity().getResources().getString(R.string.email_str));
        email_us.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/html");
                    intent.putExtra(Intent.EXTRA_EMAIL, getActivity().getString(R.string.contact_email));
                    intent.putExtra(Intent.EXTRA_SUBJECT, getActivity().getString(R.string.app_name)+" Feedback");
                    intent.putExtra(Intent.EXTRA_TEXT, "");
                    startActivity(Intent.createChooser(intent, "Send Email"));
                }catch (ActivityNotFoundException e)
                {
                    jmmToast.show(getActivity(), getActivity().getResources().getString(R.string.can_not_perform));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }



                return false;
            }
        });
        /*email  us */



        /*block  site*/

        Preference block_site_prefs=findPreference(getActivity().getResources().getString(R.string.sp_block_site));
        block_site_prefs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                if(CommonUtility.isManualPasswordSet())
                {
                    addSiteToBlockListDialog();
                }
                else {
                    setPasswordDialog();
                }



                return false;
            }
        });


        /*block  site*/


        /*block sites list*/
        Preference blocked_site_prefs=findPreference(getActivity().getResources().getString(R.string.sp_blocked_site));
        blocked_site_prefs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {


                if(CommonUtility.isManualPasswordSet())
                {

                    askPasswordDialog();
                }
                else
                {
                    setPasswordDialog();

                }
                // Intent intent=new Intent(getActivity(), BlockListActivity.class);
                //startActivity(intent);
                return false;
            }
        });
        /*block sites list*/


        /*News Category List*/

       /* Preference news_categies_prefs=findPreference(getActivity().getResources().getString(R.string.sp_news_categies));
           news_categies_prefs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                newsCategoriesDialog();
                return false;
            }
        });*/


        /*News Category List*/
    }


  /*  private void newsLanguagesDialog()
    {
        *//*not being used now*//*
        final AlertDialog dialog_news_language;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        final LinearLayout dialog_news_language_layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.dialog_news_language, null, false);
        builder.setView(dialog_news_language_layout);

        dialog_news_language=builder.create();
        dialog_news_language.show();

        RecyclerView  language_recycler_view =dialog_news_language.findViewById(R.id.language_recycler_view);
        LanguageAdapter adapter =new LanguageAdapter(languageCategoryModelArrayList, getActivity());
        language_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        language_recycler_view.setAdapter(adapter);
        TextView languageDoneText = (TextView) dialog_news_language_layout.findViewById(R.id.languageDoneText);


        languageDoneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


              *//*  try {

                    JSONObject jsonObj = new JSONObject(prefs.getStringValue(CommonUtility.CATEGORIES_OBJ , ""));
                    jsonObj.put("language" , prefs.getStringValue(CommonUtility.PREF_NEWS_LANGUAGE , ""));

                    prefs.setStringValue(CommonUtility.CATEGORIES_OBJ , jsonObj.toString());

                    //Toast.makeText(getActivity()," Categories: \n" + jsonObj, Toast.LENGTH_LONG).show();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
*//*

                dialog_news_language.dismiss();
                // Toast.makeText(getActivity(), selectedLanguage, Toast.LENGTH_LONG).show(); // print the value of selected super star
            }
        });

        TextView languageCancelText = (TextView) dialog_news_language_layout.findViewById(R.id.languageCancelText);
        languageCancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog_news_language.dismiss();
            }
        });
    }
*/

    private void newsCategoriesDialog()
    {

        newsCategoryList = new ArrayList<NewsCategoryModel>();

        NewsCategoryModel newsCategoryModel = new NewsCategoryModel();
        //newsCategoryList.add(newsCategoryModel);


        //tks  commented
       /* if(prefs.getPrefJsonNewsCategoryList(getActivity())!=null) {
            newsCategoryList = prefs.getPrefJsonNewsCategoryList(getActivity());
        }
*/



        for (int i = 0; i < newsCategoryList.size(); i++) {
            newsCategoryModel.setNewsCategoryId(newsCategoryList.get(i).getNewsCategoryId());
            newsCategoryModel.setNewsCategoryName(newsCategoryList.get(i).getNewsCategoryName());
            newsCategoryModel.setSelected(newsCategoryList.get(i).isSelected());
        }

        final AlertDialog dialog_news_category;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        final LinearLayout dialog_news_category_layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.dialog_news_category, null, false);
        builder.setView(dialog_news_category_layout);

        newsCategoryRecycler = dialog_news_category_layout.findViewById(R.id.newsCategoryRecycler);
        newsCategoryRecycler.setHasFixedSize(true);
        newsCategoryRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        //newsCategoryRecycler.addItemDecoration(new LinePagerIndicatorDecoration());

        if(prefs.getNewsCategoryList(getActivity())!=null) {
            prefNewsCategory = prefs.getNewsCategoryList(getActivity());
        }
        // create an Object for Adapter
        newsCategoryAdapter = new NewsCategoryAdapter(getActivity() , newsCategoryList , prefNewsCategory);
        newsCategoryRecycler.setAdapter(newsCategoryAdapter);

        dialog_news_category=builder.create();
        dialog_news_category.show();

        TextView newsCategoryCancel = dialog_news_category_layout.findViewById(R.id.newsCategoryCancel);
        TextView newsCategoryDone = dialog_news_category_layout.findViewById(R.id.newsCategoryDone);

        newsCategoryCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog_news_category.dismiss();
            }
        });


        newsCategoryDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<NewsCategoryModel> newsArrayList = new ArrayList<>();

                ArrayList<NewsCategoryModel> newsCategoryList = ((NewsCategoryAdapter) newsCategoryAdapter).getNewsCategoryList();

                ArrayList<String> chekedNewsList = new ArrayList<>();

                for (int i = 0; i < newsCategoryList.size(); i++) {

                    NewsCategoryModel newsCategoryModel = new NewsCategoryModel();

                    if (newsCategoryList.get(i).isSelected()==true) {
                        newsCategoryModel.setSelected(true);
                        newsCategoryModel.setNewsCategoryId(newsCategoryList.get(i).getNewsCategoryId());
                        newsCategoryModel.setNewsCategoryName(newsCategoryList.get(i).getNewsCategoryName());

                        chekedNewsList.add(newsCategoryList.get(i).getNewsCategoryId());
                    }
                    else if (newsCategoryList.get(i).isSelected()==false) {
                        newsCategoryModel.setSelected(false);
                        newsCategoryModel.setNewsCategoryId(newsCategoryList.get(i).getNewsCategoryId());
                        newsCategoryModel.setNewsCategoryName(newsCategoryList.get(i).getNewsCategoryName());
                    }
                    newsArrayList.add(newsCategoryModel);
                }

                CommonUtility.ChekedNewsList = chekedNewsList;

                try {
                    JSONObject jsonObj=new JSONObject();
                    JSONArray jsonArray = new JSONArray();
                    for(int i=0 ; i<chekedNewsList.size() ; i++){
                        jsonArray.put(chekedNewsList.get(i));
                    }

                    jsonObj.put("category" , jsonArray);
                    jsonObj.put("language" , prefs.getStringValue(CommonUtility.PREF_NEWS_LANGUAGE , ""));

                    prefs.setStringValue(CommonUtility.CATEGORIES_OBJ , jsonObj.toString());

                    // Log.e("jsonObj" , jsonObj+"");
                    //Toast.makeText(getActivity()," Categories: \n" + jsonObj, Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    e.printStackTrace();
                }

                prefs.saveNewsCategoryList(newsArrayList, CommonUtility.GET_NEWS_CATEGORIES);

//                for (int j = 0; j < listNewsCategory.size(); j++) {
//                    Toast.makeText(getActivity(),listNewsCategory.size()+"Categories: \n" + listNewsCategory.get(j).getNewsCategoryName(), Toast.LENGTH_LONG).show();
//                }

                dialog_news_category.dismiss();
            }
        });
    }


    boolean isChgPswdDlgActv ;
    private void askPasswordDialog()
    {
        isChgPswdDlgActv=false;
        final AlertDialog dialog_ask_password;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        final LinearLayout dialog_ask_password_layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.dialog_ask_password, null, false);
        builder.setView(dialog_ask_password_layout);

        final TextView button_ok=dialog_ask_password_layout.findViewById(R.id.button_ok);
        TextView button_cancel=dialog_ask_password_layout.findViewById(R.id.button_cancel);
        final TextView button_reset_password=dialog_ask_password_layout.findViewById(R.id.button_reset_password);
        final TextView password_error=dialog_ask_password_layout.findViewById(R.id.password_error);

        final EditText edit_SitePassword=dialog_ask_password_layout.findViewById(R.id.edit_password);
        final EditText edit_NewPassword=dialog_ask_password_layout.findViewById(R.id.edit_NewPassWord);
        final EditText edit_CPassword=dialog_ask_password_layout.findViewById(R.id.edit_ConfirmPassWord);


        final Animation mShakeAnimation= AnimationUtils.loadAnimation(getActivity(),R.anim.shake_animation);


        edit_SitePassword.setTypeface(CommonUtility.typeFace_Calibri_Regular(getActivity()));
        edit_NewPassword.setTypeface(CommonUtility.typeFace_Calibri_Regular(getActivity()));
        edit_CPassword.setTypeface(CommonUtility.typeFace_Calibri_Regular(getActivity()));
        password_error.setTypeface(CommonUtility.typeFace_Calibri_Regular(getActivity()));


        button_ok.setTypeface(CommonUtility.typeFace_Calibri_Regular(getActivity()) );
        button_reset_password.setTypeface(CommonUtility.typeFace_Calibri_Regular(getActivity()) );
        button_cancel.setTypeface(CommonUtility.typeFace_Calibri_Regular(getActivity()) );

        dialog_ask_password=builder.create();
        dialog_ask_password.show();

        edit_SitePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length()==0)
                {
                    password_error.setVisibility(View.GONE);
                }
            }
        });

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isChgPswdDlgActv) {
                    if (CommonUtility.IsNotEmpty(edit_SitePassword)) {
                        String s = CommonUtility.readPasswordFile();
                        //Utility.dispToast(ctx,""+s.toString());
                        if (s.equals(edit_SitePassword.getText().toString())) {
                            dialog_ask_password.dismiss();
                            Intent intent = new Intent(getActivity(), BlockListActivity.class);
                            startActivity(intent);
                        } else {
                            password_error.setVisibility(View.VISIBLE);
                            password_error.setText(getActivity().getResources().getString(R.string.incrct_pswd));
                            password_error.startAnimation(mShakeAnimation);
                        }
                    } else {
                        password_error.setVisibility(View.VISIBLE);
                        password_error.setText(getActivity().getResources().getString(R.string.write_password_error));
                        password_error.startAnimation(mShakeAnimation);
                    }
                }

                else {
                    if(CommonUtility.IsNotEmpty(edit_CPassword) && CommonUtility.IsNotEmpty(edit_NewPassword) && CommonUtility.IsNotEmpty(edit_SitePassword))
                    {
                        if(edit_CPassword.getText().toString().equals(edit_NewPassword.getText().toString()))
                        {
                            String oldPassword=CommonUtility.readPasswordFile();
                            if(oldPassword!=null)
                            {
                                if(oldPassword.equalsIgnoreCase(edit_SitePassword.getText().toString()))
                                {
                                    int status = CommonUtility.createPasswordFile(getActivity(), edit_NewPassword.getText().toString(),true);
                                    if (status == 1) {
                                        jmmToast.show(getActivity(), getActivity().getResources().getString(R.string.txt_password_set_success));
                                        dialog_ask_password.dismiss();
                                    }
                                }
                                else {
                                    //password_error.setVisibility(View.VISIBLE);
                                    password_error.setText(getActivity().getResources().getString(R.string.incrct_pswd));
                                    password_error.startAnimation(mShakeAnimation);
                                }
                            }
                        }
                        else
                        {
                            jmmToast.show(getActivity(), getActivity().getString(R.string.passwordnotmatch));
                        }
                    }
                    else
                    {
                        jmmToast.show(getActivity(), getActivity().getResources().getString(R.string.txt_fill_fields));
                    }
                }
            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_ask_password.dismiss();
            }
        });

        button_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isChgPswdDlgActv =true;
                edit_SitePassword.setHint("Old password");
                edit_NewPassword.setVisibility(View.VISIBLE);
                edit_CPassword.setVisibility(View.VISIBLE);
                password_error.setText("Reset password");
                password_error.setVisibility(View.VISIBLE);
                button_reset_password.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void setPasswordDialog()
    {
        final AlertDialog dialog_set_password;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        final LinearLayout dialog_set_password_layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.dialog_set_password, null, false);
        builder.setView(dialog_set_password_layout);

        final TextView button_set=dialog_set_password_layout.findViewById(R.id.button_set);
        TextView button_cancel=dialog_set_password_layout.findViewById(R.id.button_cancel);
        TextView password_caption=dialog_set_password_layout .findViewById(R.id.password_caption);
        final EditText sitePassword=dialog_set_password_layout .findViewById(R.id.edit_password);
        final EditText siteConfirmPassword=dialog_set_password_layout .findViewById(R.id.edit_confirm_password);

        sitePassword.setTypeface(CommonUtility.typeFace_Calibri_Regular(getActivity()));
        siteConfirmPassword.setTypeface(CommonUtility.typeFace_Calibri_Regular(getActivity()));
        password_caption.setTypeface(CommonUtility.typeFace_Calibri_Regular(getActivity()));

        button_set.setTypeface(CommonUtility.typeFace_Calibri_Regular(getActivity()) );
        button_cancel.setTypeface(CommonUtility.typeFace_Calibri_Regular(getActivity()) );

        dialog_set_password=builder.create();
        dialog_set_password.show();


        button_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(sitePassword.getText().toString().length()>=1 && siteConfirmPassword.getText().toString().length()>=1) {
                    if (sitePassword.getText().toString().equalsIgnoreCase(siteConfirmPassword.getText().toString())) {
                        int status = CommonUtility.createPasswordFile(getActivity(), sitePassword.getText().toString(),false);
                        if (status == 1)
                            jmmToast.show(getActivity(), getActivity().getResources().getString(R.string.txt_password_set_success));
                           dialog_set_password.dismiss();
                    } else {
                        jmmToast.show(getActivity(), getActivity().getResources().getString(R.string.passwordnotmatch));
                    }
                }
                else
                {
                    jmmToast.show(getActivity(),getActivity().getResources().getString(R.string.type_password));
                }



            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_set_password.dismiss();
            }
        });
    }



    private void addSiteToBlockListDialog()
    {

        final AlertDialog dialog_add_site;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        final LinearLayout dialog_add_site_layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.dialog_add_site_block, null, false);
        builder.setView(dialog_add_site_layout);
        final TextView addSiteTxt=dialog_add_site_layout.findViewById(R.id.addSite);
        TextView cancelTxt=dialog_add_site_layout.findViewById(R.id.cancel_txt);
        final EditText siteUrl=dialog_add_site_layout .findViewById(R.id.edit_Url);
        final TextView txtError=dialog_add_site_layout.findViewById(R.id.txtError);
        siteUrl.setSelection(siteUrl.getText().length());
        txtError.setTypeface(CommonUtility.typeFace_Calibri_Regular(getActivity()));
        siteUrl.setTypeface(CommonUtility.typeFace_Calibri_Regular(getActivity()));
        addSiteTxt.setTypeface(CommonUtility.typeFace_Calibri_Regular(getActivity()) );
        cancelTxt.setTypeface(CommonUtility.typeFace_Calibri_Regular(getActivity()) );
        dialog_add_site=builder.create();
        dialog_add_site.show();


        final Animation mShakeAnimation= AnimationUtils.loadAnimation(getActivity(),R.anim.shake_animation);

        siteUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                   if(s.length()==0)
                   {
                       txtError.setVisibility(View.GONE);
                   }
            }
        });


        addSiteTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(siteUrl.getText().toString().length()>0 )
                {
                    ArrayList<BlockListModel> list = new ArrayList<>();
                    if(prefs.getBlockedArrayList(getActivity())!=null)
                    {
                        list.addAll(prefs.getBlockedArrayList(getActivity()));
                    }

                    String url=siteUrl.getText().toString().trim().replaceAll("\\s","").toLowerCase();
                    if(!BrowserUtility.isURL(url) && !url.contains("www.")) {
                        url = "www." + url.trim();
                    }

                    if(BrowserUtility.isURL(url))
                    {
                       // jmmToast.show(getActivity(), ""+url);
                        for(int i=0;i<list.size();i++)
                        {
                            String listUrl=list.get(i).getSiteUrl();
                            if(listUrl.equalsIgnoreCase(url))
                            {
                                //jmmToast.show(getActivity(), url+" Already added to blocked list");
                                txtError.setText(url+" Already added to blocked list");
                                txtError.setVisibility(View.VISIBLE);
                                txtError.startAnimation(mShakeAnimation);
                                return;
                            }
                        }

                        BlockListModel model = new BlockListModel();
                        model.setSiteUrl(url);
                        model.setTime(System.currentTimeMillis());
                        list.add(model);
                        prefs.saveBlockedList(list, CommonUtility.PREF_BLOCK_LIST);
                        dialog_add_site.dismiss();

                    }
                    else {
                        jmmToast.show(getActivity(), R.string.prompt_valid_url);
                    }



                }
                else
                {
                    jmmToast.show(getActivity(), R.string.promt_title);
                }

            }
        });

        cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(dialog_add_site!=null)
                {
                    dialog_add_site.dismiss();
                }
            }
        });





    }

    private String getDefaultBrowserName()
    {
        Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://"));
        ResolveInfo resolveInfo = getActivity().getPackageManager().resolveActivity(browserIntent, PackageManager.MATCH_DEFAULT_ONLY);

        // This is the default browser's packageName
        String packageName = resolveInfo.activityInfo.packageName;

        if(packageName.contains(".")) {
            String[] pNameArr=packageName.split("\\.");
            packageName=pNameArr[pNameArr.length-1];
        }
        else {
            if (packageName.equalsIgnoreCase("android")) // in case no one is selected as default
            {
                packageName = "not set";
            }
        }
        return packageName;
    }

    public void askForPermission()
    {
        if (hasPermission(permissionsRequired[0])!= PackageManager.PERMISSION_GRANTED ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[0])) {
                //Show Information about why you need the permission

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                builder.setTitle(getActivity().getString(R.string.need_permission));
                builder.setMessage(getActivity().getString(R.string.app_name) + " "+getResources().getString(R.string.call_permission));

                builder.setPositiveButton(getActivity().getString(R.string.grant), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions((Activity) getActivity(), permissionsRequired, PERMISSION_CALLBACK_CONSTANT);

                    }
                });
                builder.setNegativeButton(getActivity().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                //android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());

                builder.setTitle(getActivity().getString(R.string.need_permission));
                builder.setMessage(getActivity().getString(R.string.app_name) + " "+getResources().getString(R.string.call_permission));
                builder.setPositiveButton(getActivity().getString(R.string.grant), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",getActivity().getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);

                    }
                });
                builder.setNegativeButton(getActivity().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions((Activity) getActivity(), permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], false);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            initiatePhoneCall();

        }
    }
    //new
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
                //redire to call
                initiatePhoneCall();

            } else if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getActivity(), permissionsRequired[0]) ) {

                //android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                builder.setTitle(getActivity().getString(R.string.need_permission));
                builder.setMessage(getActivity().getString(R.string.app_name) + " "+getResources().getString(R.string.call_permission));
                builder.setPositiveButton(getActivity().getString(R.string.grant), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions((Activity) getActivity(),permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {

            }
        }


    }


    private void setPassWordPrefs(boolean b) {

        /*the  value  is  being used  in jmmWebView class for storing from data  of a website*/
        SharedPreferences.Editor editor = settingsPrefs.edit();
        editor.putBoolean(getString(R.string.sp_passwords),b);
        editor.commit();
    }

    public  int hasPermission(String permission) {
        if (getActivity() != null && permission != null) {

            if (ActivityCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_GRANTED) {
                return  PackageManager.PERMISSION_GRANTED;

            }
        }

        return 1001; //some other int value;
    }
    private void initiatePhoneCall()
    {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+917303201279"));
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        switch (preference.getTitleRes()) {
            case R.string.setting_title_version:

                break;
            case R.string.download_path_0:

            default:
                break;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
        spChange = true;
        if (key.equals(getString(R.string.sp_download))) {
            String summary = downLoadEntries[Integer.valueOf(sp.getString(key, "0"))];
            mDownLoadPrefs.setSummary(summary);
        }
        if(key.equals(getString(R.string.sp_show_images)))
        {
            String summary = showImagesEntries[Integer.valueOf(sp.getString(key, "0"))];
            mShowImagesPrefs.setSummary(summary);
        }
//        if(key.equals(getString(R.string.sp_language)))
//        {
//            String summary = showLanguageEntries[Integer.valueOf(sp.getString(key, "0"))];
//            mShowLanguagePrefs.setSummary(summary);
//        }




    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    private boolean isGpsEnabled() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) || manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return true;
        } else {
            return false;
        }

    }
    private void disableLocation()
    {
        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    private boolean checkIfLocationOpened() {
        String provider = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (provider.contains("gps") || provider.contains("network"))
            return true;
        else
            // otherwise return false
            return false;
    }



    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getActivity().getResources().getString(R.string.gps_msg))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.txt_yes), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(getActivity().getResources().getString(R.string.txt_no), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onJsonLoad(String json, int mRequestCode) {

        if(mRequestCode==AppConstants.logoutApiRequestCode)
        {
            try {
                JSONObject jsonObject = new JSONObject(json);
                if(jsonObject.has("status"))
                {
                    String msg=JsonParser.getkeyValue_Str(jsonObject, "message");
                    jmmToast.show(getActivity(), ""+msg);
                    String status=JsonParser.getkeyValue_Str(jsonObject, "status");
                    if(status.equalsIgnoreCase("true"))
                    {
                        prefs.setStringValue(AppConstants.PREFS_USER_ID, "0");
                        prefs.setStringValue(AppConstants.PREFS_TOKEN, "0");
                        getActivity().finish();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        if(mRequestCode==AppConstants.deleteAccountApiRequestCode)
        {
            //jmmToast.show(getActivity(), json.toString());
            try {
                JSONObject jsonObject = new JSONObject(json);
                if(jsonObject.has("status"))
                {
                    String msg=JsonParser.getkeyValue_Str(jsonObject, "message");
                    jmmToast.show(getActivity(), ""+msg);
                    String status=JsonParser.getkeyValue_Str(jsonObject, "status");
                    if(status.equalsIgnoreCase("true"))
                    {
                        prefs.setStringValue(AppConstants.PREFS_USER_ID, "0");
                        prefs.setStringValue(AppConstants.PREFS_TOKEN, "0");
                        getActivity().finish();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onLoadFailed(String msg) {

    }
}
