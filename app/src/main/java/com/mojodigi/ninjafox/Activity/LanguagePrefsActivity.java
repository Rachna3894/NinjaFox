package com.mojodigi.ninjafox.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.IntProperty;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.mojodigi.ninjafox.Adapter.BlockListAdapter;
import com.mojodigi.ninjafox.Adapter.CategoryAdapter;
import com.mojodigi.ninjafox.Adapter.LanguageAdapter;
import com.mojodigi.ninjafox.Model.BlockListModel;
import com.mojodigi.ninjafox.Model.CategoryModel;
import com.mojodigi.ninjafox.Model.LanguageCategoryModel;
import com.mojodigi.ninjafox.Model.NewsSuperParentModel;
import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.SharedPrefs.AppConstants;
import com.mojodigi.ninjafox.SharedPrefs.SharedPreferenceUtil;
import com.mojodigi.ninjafox.Task.ApiRequestTask;
import com.mojodigi.ninjafox.Unit.CommonUtility;
import com.mojodigi.ninjafox.Unit.JsonParser;
import com.mojodigi.ninjafox.View.jmmToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class LanguagePrefsActivity extends Activity implements ApiRequestTask.JsonLoadListener,LanguageAdapter.languageSelectListener , CategoryAdapter.categoryAddRemoveListener {

    Context mContext;
    SharedPreferenceUtil mPrefs;
    ArrayList<LanguageCategoryModel> languageCategoryModelArrayList = new ArrayList<>();
    RecyclerView language_recycler_view;
    LanguageAdapter adapter;
    Set<String> languageSet = new HashSet<String>();
    Set<String> categorySet= new HashSet<String>();
    LanguagePrefsActivity mInstance;
    SharedPreferenceUtil mUsersharedPrefs;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_prefs_activity);
        mContext = this;
        mInstance=this;
        mUsersharedPrefs=new SharedPreferenceUtil(mContext);
        mPrefs = new SharedPreferenceUtil(mContext);

        categorySet=mPrefs.getSetCollectionValue(CommonUtility.SELECTED_NEWS_CATEGORIES,null);
        languageSet=mPrefs.getSetCollectionValue(CommonUtility.SELECTED_NEWS_LANGUAGES,null);

         if(categorySet==null)
         {
             categorySet=new HashSet<>();
         }
         if(languageSet==null)
         {
             languageSet=new HashSet<>();
         }


        TextView title_txt = findViewById(R.id.title_txt);
        LinearLayout backButton = findViewById(R.id.backButton);
        title_txt.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));

        TextView submit_button=findViewById(R.id.submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject prefsObject=preparePrefsObject();
                if(prefsObject!=null) {
                    new ApiRequestTask(mContext, mInstance, CommonUtility.API_SET_LANGUAGES, false, true, mContext.getResources().getString(R.string.update_language), prefsObject.toString(), AppConstants.setLanguageRequestCode).execute();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getNewLanguagesForUserPrefs();
    }

    private JSONObject preparePrefsObject() {


        JSONObject prefsObject=new JSONObject();
        try {
            prefsObject.put("userId", mUsersharedPrefs.getStringValue(AppConstants.PREFS_USER_ID, "0"));
            prefsObject.put("token", mUsersharedPrefs.getStringValue(AppConstants.PREFS_TOKEN, "0"));

            JSONArray languageArray=new JSONArray();

            for (Iterator<String> it = languageSet.iterator(); it.hasNext(); ) {
                String  lanCode = it.next();
                languageArray.put(lanCode);
            }

            JSONArray catArray=new JSONArray();

            for (Iterator<String> it = categorySet.iterator(); it.hasNext(); ) {
                String  catCode = it.next();
                catArray.put(catCode);
            }

       prefsObject.put("category", catArray);
       prefsObject.put("language", languageArray);


            return  prefsObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void getNewLanguagesForUserPrefs() {
        new ApiRequestTask(mContext, this, CommonUtility.API_URL_GET_LANGUAGES, true, true, mContext.getResources().getString(R.string.msg_loading), null, AppConstants.userPrefsApiRequestCode).execute();
    }

    @Override
    public void onJsonLoad(String json, int mRequestCode) {

        //json=AppConstants.dummyRespose;
        if(mRequestCode==AppConstants.userPrefsApiRequestCode)
        {
            languageCategoryModelArrayList.clear();
            try {
                JSONObject mainJosn=new JSONObject(json);
                String msg= JsonParser.getkeyValue_Str(mainJosn, "message");
                if(msg.equalsIgnoreCase("success"))
                {
                    if(mainJosn.has("languages"))
                    {
                        JSONArray languageArray=mainJosn.getJSONArray("languages");
                        for(int i=0;i<languageArray.length();i++)
                        {
                            JSONObject languageObj=languageArray.getJSONObject(i);
                            LanguageCategoryModel model = new LanguageCategoryModel();
                            String languageCode=JsonParser.getkeyValue_Str(languageObj, "languageCode");
                            String languageName=JsonParser.getkeyValue_Str(languageObj, "languageName");


                            JSONArray categoryArray=languageObj.getJSONArray("category");
                            ArrayList <CategoryModel>catList=new ArrayList<>();
                            for(int j=0;j<categoryArray.length();j++)
                            {
                                CategoryModel categoryModel = new CategoryModel();
                                JSONObject object = categoryArray.getJSONObject(j);
                                String categoryCode=JsonParser.getkeyValue_Str(object, "categoryCode");
                                String categoryName=JsonParser.getkeyValue_Str(object, "categoryName");
                                categoryModel.setCategoryCode(categoryCode);
                                categoryModel.setCategoryName(categoryName);
                                if(categorySet!=null && categorySet.contains(categoryCode)) {
                                    categoryModel.setIsChecked(true);
                                }
                                catList.add(categoryModel);
                            }
                            if(languageSet!=null && languageSet.contains(languageCode)) {
                                model.setIsChecked(true);
                            }
                            model.setLanguageCode(languageCode);
                            model.setLanguageName(languageName);
                            model.setCateGoryList(catList);

                            languageCategoryModelArrayList.add(model);
                        }

                        System.out.print(""+languageCategoryModelArrayList);

                        language_recycler_view=findViewById(R.id.language_recycler_view);
                        adapter=new LanguageAdapter(languageCategoryModelArrayList,this, mInstance,mContext);
                        language_recycler_view.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                        language_recycler_view.setAdapter(adapter);


                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch (Exception e)
            {

            }

        }

        if(mRequestCode==AppConstants.setLanguageRequestCode)
        {
              //jmmToast.show(mContext, json.toString());

            JSONObject mainJosn= null;
            try {
                mainJosn = new JSONObject(json);
                String msg= JsonParser.getkeyValue_Str(mainJosn, "message");
                jmmToast.show(mContext, ""+msg);
                String status= JsonParser.getkeyValue_Str(mainJosn, "status");
                if(status.equalsIgnoreCase("true"))
                {
                    if(categorySet!=null)
                    {
                        mPrefs.saveSetCollectionValue(CommonUtility.SELECTED_NEWS_CATEGORIES, categorySet);
                        Set <String> data=mPrefs.getSetCollectionValue(CommonUtility.SELECTED_NEWS_CATEGORIES,null);
                        System.out.print("savedData"+data);
                        Log.d("savedData", ""+data);
                    }
                    if(languageSet!=null)
                    {
                        mPrefs.saveSetCollectionValue(CommonUtility.SELECTED_NEWS_LANGUAGES, languageSet);
                        Set <String> data=mPrefs.getSetCollectionValue(CommonUtility.SELECTED_NEWS_LANGUAGES,null);
                        System.out.print("savedData"+data);
                        Log.d("savedData", ""+data);
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }






        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private  ArrayList<String> getSetData(Set set)
    {
        ArrayList<String> listData =new ArrayList<>();
          if(!set.isEmpty())
          {
              for (Iterator<String> it = set.iterator(); it.hasNext(); ) {
                      String  setValue = it.next();
                     listData.add(setValue);
                  }

          }
          return  listData;
    }

    @Override
    public void onLoadFailed(String msg) {

    }

    @Override
    public void addToset(String lancode,int position) {

        languageSet.add(lancode);
        System.out.print(""+languageSet);
        languageCategoryModelArrayList.get(position).setIsChecked(true);

    }

    @Override
    public void removeFromset(String lancode,int position) {
        languageSet.remove(lancode);
        languageCategoryModelArrayList.get(position).setIsChecked(false);
        for(int i=0;i<languageCategoryModelArrayList.get(position).getCateGoryList().size();i++)
        {
            //set  all  the category to  uncheck on language removal;
            languageCategoryModelArrayList.get(position).getCateGoryList().get(i).setIsChecked(false);
            //remove all  the  category  belonging to  the language  on language  removal;
            categorySet.remove(languageCategoryModelArrayList.get(position).getCateGoryList().get(i).getCategoryCode());

        }

        System.out.print(""+languageSet);
        System.out.print(""+categorySet);

        if(!language_recycler_view.isComputingLayout())
        {
            language_recycler_view.post(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }



    }

    @Override
    public void addCategoryToCatSet(String catCode,int position,int parentPosition) {
        categorySet.add(catCode);
        languageCategoryModelArrayList.get(parentPosition).setIsChecked(true);
        languageCategoryModelArrayList.get(parentPosition).getCateGoryList().get(position).setIsChecked(true);
        System.out.print(""+categorySet);

        if(!language_recycler_view.isComputingLayout()) {
            language_recycler_view.post(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }



    }

    @Override
    public void removeCategoryFromCatSet(String catCode, int position, int parentPosition) {
         categorySet.remove(catCode);
         if(categorySet.size()==0)
         {
             //languageCategoryModelArrayList.get(parentPosition).setIsChecked(false);
         }
         boolean st=isLastRemovedCategoryForLanguage(catCode);
         System.out.print(st);
         if(st)
         {
             languageCategoryModelArrayList.get(parentPosition).setIsChecked(false);
             String lan=languageCategoryModelArrayList.get(parentPosition).getLanguageCode();
             System.out.print(""+lan);
             languageSet.remove(languageCategoryModelArrayList.get(parentPosition).getLanguageCode());
             System.out.print(""+languageSet);
         }
        languageCategoryModelArrayList.get(parentPosition).getCateGoryList().get(position).setIsChecked(false);
         System.out.print(""+categorySet);

         if(!language_recycler_view.isComputingLayout()) {
             language_recycler_view.post(new Runnable() {
                 @Override
                 public void run() {
                     adapter.notifyDataSetChanged();
                 }
             });
         }
    }

private boolean isLastRemovedCategoryForLanguage(String pCatCode)
{
    String catCode=pCatCode.substring(0, 3);
    System.out.print(catCode);
    if(categorySet!=null) {
        Object[] keySet = categorySet.toArray();
        System.out.print(""+keySet);
        if(keySet.length==0)
            return true;

        for(int i=0;i<keySet.length;i++)
        {
            String keyVale=keySet[i].toString();
              String string = keyVale.substring(0,3);

              if(catCode.equalsIgnoreCase(string))
              {
                   // means any category of particular laguage exist in set
                  return  false;
              }
              else
              {
                  return  true;
              }


        }

    }
    return true;

}

}
