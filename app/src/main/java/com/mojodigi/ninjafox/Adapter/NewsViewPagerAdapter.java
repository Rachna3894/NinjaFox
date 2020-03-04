package com.mojodigi.ninjafox.Adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.mojodigi.ninjafox.Activity.MainActivity;
import com.mojodigi.ninjafox.Fragment.NewsFragment;
import com.mojodigi.ninjafox.Model.NewsMainModel;

import java.util.ArrayList;

public class NewsViewPagerAdapter extends FragmentStatePagerAdapter {

    private int mNumOfTabs;

    private Fragment fragment = null;
    private ArrayList<NewsMainModel> parentList;
    private MainActivity mainActivity;

    public NewsViewPagerAdapter(FragmentManager fm, int NumOfTabs, ArrayList<NewsMainModel> parentList , MainActivity activity) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.parentList = parentList;
        this.mainActivity=activity;
    }
    @Override
    public Fragment getItem(int position) {
        return new NewsFragment().newInstance(parentList  , position , mainActivity);
    }



    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }


    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return parentList.get(position).getCategoryName();
    }


}