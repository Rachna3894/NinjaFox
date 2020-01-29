package com.mojodigi.ninjafox.SharedPrefs;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mojodigi.ninjafox.Model.BlockListModel;
import com.mojodigi.ninjafox.Model.NewsCategoryModel;
import com.mojodigi.ninjafox.Model.NewsDataModel;
import com.mojodigi.ninjafox.Model.SpeedDialModel;
import com.mojodigi.ninjafox.Unit.CommonUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Takendra
 */

public class SharedPreferenceUtil {

    protected Context mContext;
    private static SharedPreferenceUtil mSharedPreferenceUtils;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferencesEditor;

    public SharedPreferenceUtil(Context context) {
        mContext = context;
        mSharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        mSharedPreferencesEditor = mSharedPreferences.edit();
    }

    /**
     * Creates single instance of SharedPreferenceUtils
     *
     * @param context context of Activity or Service
     * @return Returns instance of SharedPreferenceUtils
     */

    public static synchronized SharedPreferenceUtil getInstance(Context context) {

        if (mSharedPreferenceUtils == null) {
            mSharedPreferenceUtils = new SharedPreferenceUtil(context.getApplicationContext());
        }
        return mSharedPreferenceUtils;
    }

    /**
     * Stores String value in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */

    public void setValue(String key, String value) {
        mSharedPreferencesEditor.putString(key, value);
        mSharedPreferencesEditor.commit();
    }

    /**
     * Stores int value in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */
    public void setValue(String key, int value) {
        mSharedPreferencesEditor.putInt(key, value);
        mSharedPreferencesEditor.commit();
    }
    public void setIntValue(String key, int value) {
        mSharedPreferencesEditor.putInt(key, value);
        mSharedPreferencesEditor.commit();
    }

    /**
     * Stores Double value in String format in preference
     * @param key   key of preference
     * @param value value for that key
     */
    public void setValue(String key, double value) {
        setValue(key, Double.toString(value));
    }

    /**
     * Stores long value in preference
     * @param key   key of preference
     * @param value value for that key
     */
    public void setValue(String key, long value) {
        mSharedPreferencesEditor.putLong(key, value);
        mSharedPreferencesEditor.commit();
    }

    /**
     * Stores boolean value in preference
     * @param key   key of preference
     * @param value value for that key
     */
    public void setValue(String key, boolean value) {
        mSharedPreferencesEditor.putBoolean(key, value);
        mSharedPreferencesEditor.commit();
    }

    public void setStringValue(String key, String value) {
        mSharedPreferencesEditor.putString(key, value);
        mSharedPreferencesEditor.commit();
    }

    /**
     * Retrieves String value from preference
     *
     * @param key key of preference
     * @param defaultValue default value if no key found
     */
    public String getStringValue(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    /**
     * Retrieves int value from preference
     *
     * @param key  key of preference
     * @param defaultValue default value if no key found
     */
    public int getIntValue(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    /**
     * Retrieves long value from preference
     *
     * @param key  key of preference
     * @param defaultValue default value if no key found
     */
    public long getLongValue(String key, long defaultValue) {
        return mSharedPreferences.getLong(key, defaultValue);
    }

    public void setBoolanValue(String key, boolean value) {
        mSharedPreferencesEditor.putBoolean(key, value);
        mSharedPreferencesEditor.commit();
    }
    public void saveSetCollectionValue(String key,Set<String> set)
    {
         mSharedPreferencesEditor.putStringSet(key,set);
         mSharedPreferencesEditor.commit();
    }
    public Set<String> getSetCollectionValue(String key ,Set<String> blank)
    {
        return mSharedPreferences.getStringSet(key, blank);
    }

    /**
     * Retrieves boolean value from preference
     * @param keyFlag      key of preference
     * @param defaultValue default value if no key found
     */
    public boolean getBoolanValue(String keyFlag, boolean defaultValue) {
        return mSharedPreferences.getBoolean(keyFlag, defaultValue);
    }

    /**
     * Removes key from preference
     * @param key key of preference that is to be deleted
     */
    public void removeKey(String key) {
        if (mSharedPreferencesEditor != null) {
            mSharedPreferencesEditor.remove(key);
            mSharedPreferencesEditor.commit();
        }
    }

    public void saveArrayList(ArrayList<NewsDataModel> list, String key) {

        if (list == null)
            return;
        if (key == null)
            return;
        Gson gson = new Gson();
        String json = gson.toJson(list);
        mSharedPreferencesEditor.putString(key, json);
        mSharedPreferencesEditor.commit();
    }

    public ArrayList<NewsDataModel> getArrayList(Context context) {
        if (context == null)
            return null;

        List<NewsDataModel> arrayList;
        if (mSharedPreferences.contains(CommonUtility.PREF_NEWS_LIST)) {
            String jsonArrayList = mSharedPreferences.getString(CommonUtility.PREF_NEWS_LIST, null);

            Gson gson = new Gson();
            NewsDataModel[] itemsArrayList = gson.fromJson(jsonArrayList, NewsDataModel[].class);

            arrayList = Arrays.asList(itemsArrayList);
            arrayList = new ArrayList<NewsDataModel>(arrayList);
        } else
            return null;

        return (ArrayList<NewsDataModel>) arrayList;
    }



    public void saveArrayList_Social(ArrayList<SpeedDialModel> list, String key) {

        if (list == null)
            return;
        if (key == null)
            return;
        Gson gson = new Gson();
        String json = gson.toJson(list);
        mSharedPreferencesEditor.putString(key, json);
        mSharedPreferencesEditor.commit();


    }

    public ArrayList<SpeedDialModel> getArrayList_Social(Context context) {
        if (context == null)
            return null;

        List<SpeedDialModel> arrayList;
        if (mSharedPreferences.contains(CommonUtility.PREF_SOCIAL_LIST)) {
            String jsonArrayList = mSharedPreferences.getString(CommonUtility.PREF_SOCIAL_LIST, null);
            Gson gson = new Gson();
            SpeedDialModel[] itemsArrayList = gson.fromJson(jsonArrayList, SpeedDialModel[].class);

            arrayList = Arrays.asList(itemsArrayList);
            arrayList = new ArrayList<SpeedDialModel>(arrayList);
        } else
            return null;

        return (ArrayList<SpeedDialModel>) arrayList;
    }


    public void saveBlockedList(ArrayList<BlockListModel> list, String key) {

        if (list == null)
            return;
        if (key == null)
            return;
        Gson gson = new Gson();
        String json = gson.toJson(list);
        mSharedPreferencesEditor.putString(key, json);
        mSharedPreferencesEditor.commit();
    }

    public ArrayList<BlockListModel> getBlockedArrayList(Context context) {
        if (context == null)
            return null;

        List<BlockListModel> arrayList;
        if (mSharedPreferences.contains(CommonUtility.PREF_BLOCK_LIST)) {
            String jsonArrayList = mSharedPreferences.getString(CommonUtility.PREF_BLOCK_LIST, null);
            Gson gson = new Gson();
            BlockListModel[] itemsArrayList = gson.fromJson(jsonArrayList, BlockListModel[].class);

            arrayList = Arrays.asList(itemsArrayList);
            arrayList = new ArrayList<BlockListModel>(arrayList);
        } else
            return null;

        return (ArrayList<BlockListModel>) arrayList;
    }



    public void saveNewsCategoryList(ArrayList<NewsCategoryModel> list, String key) {
        if (list == null)
            return;
        if (key == null)
            return;
        Gson gson = new Gson();
        String json = gson.toJson(list);
        mSharedPreferencesEditor.putString(key, json);
        mSharedPreferencesEditor.commit();
    }

    public ArrayList<NewsCategoryModel> getNewsCategoryList(Context context) {
        if (context == null)
            return null;
        List<NewsCategoryModel> arrayList;
        if (mSharedPreferences.contains(CommonUtility.GET_NEWS_CATEGORIES)) {
            String jsonArrayList = mSharedPreferences.getString(CommonUtility.GET_NEWS_CATEGORIES, null);
            Gson gson = new Gson();
            NewsCategoryModel[] itemsArrayList = gson.fromJson(jsonArrayList, NewsCategoryModel[].class);
            arrayList = Arrays.asList(itemsArrayList);
            arrayList = new ArrayList<NewsCategoryModel>(arrayList);
        } else
            return null;

        return (ArrayList<NewsCategoryModel>) arrayList;
    }


    public void saveSelectedCategoryList(ArrayList<String> list, String key) {
        if (list == null)
            return;
        if (key == null)
            return;
        Gson gson = new Gson();
        String json = gson.toJson(list);
        mSharedPreferencesEditor.putString(key, json);
        mSharedPreferencesEditor.commit();
    }

    /*public ArrayList<String> getSelectedCategoryList(Context context) {
        if (context == null)
            return null;
        List<String> arrayList;
        if (mSharedPreferences.contains(CommonUtility.PREF_JSON_NEWS_CATEGORIES)) {
            String jsonArrayList = mSharedPreferences.getString(CommonUtility.PREF_JSON_NEWS_CATEGORIES, null);
            Gson gson = new Gson();
            //NewsCategoryModel[] itemsArrayList = gson.fromJson(jsonArrayList, NewsCategoryModel[].class);
            arrayList = Arrays.asList(jsonArrayList);
            arrayList = new ArrayList<String>(arrayList);
        } else
            return null;

        return (ArrayList<String>) arrayList;
    }*/



    /**
     * Clears all the preferences stored
     */
    public void clear() {
        mSharedPreferencesEditor.clear().commit();
    }
}