package com.mojodigi.ninjafox.Adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mojodigi.ninjafox.Activity.LanguagePrefsActivity;
import com.mojodigi.ninjafox.Model.CricScoreModel;
import com.mojodigi.ninjafox.Model.LanguageCategoryModel;
import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.Unit.CommonUtility;
import com.mojodigi.ninjafox.Unit.ViewUnit;
import com.mojodigi.ninjafox.View.jmmToast;

import java.util.ArrayList;

public class LanguageAdapter extends  RecyclerView.Adapter<LanguageAdapter.ViewHolder> {

    private ArrayList<LanguageCategoryModel> languageList;
    Context mContext;
    LanguagePrefsActivity activity;
   languageSelectListener callBack;
    public LanguageAdapter(ArrayList<LanguageCategoryModel> languageList, languageSelectListener callBack, LanguagePrefsActivity activity,Context mContext) {
        this.languageList = languageList;
        this.mContext = mContext;
        this.callBack=callBack;
        this.activity=activity;
    }


    @Override
    public LanguageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_language_adapter, parent, false);
        return new LanguageAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LanguageAdapter.ViewHolder holder, final int position) {

        holder.languageText.setText(languageList.get(position).getLanguageName());

        boolean st=languageList.get(position).getIsChecked();
        System.out.print(""+st);
        holder.langaugeCheckBox.setChecked(languageList.get(position).getIsChecked());

        CategoryAdapter adapter=new CategoryAdapter(languageList.get(position).getCateGoryList(),activity,position, mContext);

        holder.recyclerView_category.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        holder.recyclerView_category.setAdapter(adapter);

        holder.langaugeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if(isChecked)
                {
                    //add
                      //jmmToast.show(mContext, languageList.get(position).getLanguageCode());
                       callBack.addToset(languageList.get(position).getLanguageCode(),position);

                }
                else
                    {

                    //remove
                        //jmmToast.show(mContext, languageList.get(position).getLanguageCode());
                        callBack.removeFromset(languageList.get(position).getLanguageCode(),position);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return languageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView entertainTitle;

        TextView languageText;
        CheckBox langaugeCheckBox;
        RecyclerView recyclerView_category;


        public ViewHolder(View view) {
            super(view);


            langaugeCheckBox=view.findViewById(R.id.lan_checkBoc);
            languageText=view.findViewById(R.id.lan_Text);
            recyclerView_category=view.findViewById(R.id.recyclerView_category);
            languageText.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));


        }
    }
    public   interface  languageSelectListener
    {
        void addToset(String lancode,int position);
        void removeFromset(String lancode,int position);
    }
}




