package com.mojodigi.ninjafox.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mojodigi.ninjafox.Model.CategoryModel;
import com.mojodigi.ninjafox.Model.LanguageCategoryModel;
import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.Unit.CommonUtility;
import com.mojodigi.ninjafox.View.jmmToast;

import java.util.ArrayList;

public class CategoryAdapter extends  RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private ArrayList<CategoryModel> categoryList;
    Context mContext;
    int parentPosition;

    categoryAddRemoveListener callBack;
    public CategoryAdapter(ArrayList<CategoryModel> categoryList, categoryAddRemoveListener callBack,int parentPosition, Context mContext) {
        this.categoryList = categoryList;
        this.mContext = mContext;
        this.callBack=callBack;
        this.parentPosition=parentPosition;

    }


    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.roaw_language_category_adapter, parent, false);
        return new CategoryAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CategoryAdapter.ViewHolder holder, final int position) {

        holder.categoryText.setText(categoryList.get(position).getCategoryName());
        holder.categoryCheckBox.setChecked(categoryList.get(position).getIsChecked());


        holder.categoryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {




                if(isChecked)
                {
                    //add
                    //jmmToast.show(mContext, categoryList.get(position).getCategoryCode());

                    callBack.addCategoryToCatSet(categoryList.get(position).getCategoryCode(),position,parentPosition);
                }
                else
                    {

                        //remove
                        //jmmToast.show(mContext, categoryList.get(position).getCategoryCode());
                        callBack.removeCategoryFromCatSet(categoryList.get(position).getCategoryCode(),position,parentPosition);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView categoryText;
        CheckBox categoryCheckBox;
        RecyclerView recyclerView_category;


        public ViewHolder(View view) {
            super(view);

            categoryCheckBox=view.findViewById(R.id.cat_checkBoc);
            categoryText=view.findViewById(R.id.cat_Text);
            recyclerView_category=view.findViewById(R.id.recyclerView_category);
            categoryText.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));


        }
    }
    public interface categoryAddRemoveListener
    {
        void  addCategoryToCatSet(String catCode,int position,int parentPosition);
        void  removeCategoryFromCatSet(String catCode,int position,int parentPosition);
    }
}




