package com.mojodigi.ninjafox.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mojodigi.ninjafox.Model.NewsCategoryModel;
import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.SharedPrefs.SharedPreferenceUtil;
import java.util.ArrayList;

public class NewsCategoryAdapter  extends RecyclerView.Adapter<NewsCategoryAdapter.ViewHolder> {

    private ArrayList<NewsCategoryModel> newsCategoryList;
    private ArrayList<NewsCategoryModel> prefNewsCategory;
    private ArrayList <String>  chekedNewsList ;
    private String newsCategoryName ;
    private boolean isCheckedItem ;
    private NewsCategoryModel newsCategoryModel;
    private Context mContext;
    SharedPreferenceUtil prefs;

    public NewsCategoryAdapter(Context context ,  ArrayList<NewsCategoryModel> newsCategoryList , ArrayList<NewsCategoryModel> prefNewsCategory) {
        this.mContext = context;
        this.newsCategoryList = newsCategoryList;
        this.prefNewsCategory = prefNewsCategory;
        prefs = new SharedPreferenceUtil(mContext);
    }

    @Override
    public NewsCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_category_checkbox_row, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final NewsCategoryAdapter.ViewHolder viewHolder, int position) {

        viewHolder.newsCategoryName.setText(newsCategoryList.get(position).getNewsCategoryName());

        if(prefNewsCategory!=null) {
                if (prefNewsCategory.get(position).isSelected()==true) {
                    viewHolder.newsCategoryCheckBox.setChecked(true);
                    newsCategoryList.get(position).setSelected(true);
                    //Toast.makeText(mContext,  " true", Toast.LENGTH_SHORT).show();
                }
                else {
                    viewHolder.newsCategoryCheckBox.setChecked(false);
                    newsCategoryList.get(position).setSelected(false);
                    //Toast.makeText(mContext,  " false", Toast.LENGTH_SHORT).show();
                }
        }


        viewHolder.newsCategoryCheckBox.setTag(position);

        viewHolder.newsCategoryCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer pos = (Integer) viewHolder.newsCategoryCheckBox.getTag();
                if (newsCategoryList.get(pos).isSelected()) {
                    newsCategoryList.get(pos).setSelected(false);
                } else {
                    newsCategoryList.get(pos).setSelected(true);
                    //Toast.makeText(mContext,  "Set Selected true", Toast.LENGTH_SHORT).show();
                }
            }
        });


/********************************************************************************************************/


//        isCheckedItem = newsCategoryList.get(position).isSelected();
//        Log.e("isCheckedItem", isCheckedItem+"");
//
//        if(!isCheckedItem){
//            Log.e("isCheckedItem", isCheckedItem+"");
//        }
//
//        //        if(isCheckedItem){
////            newsCategoryModel.setSelected(true);
////            viewHolder.newsCategoryCheckBox.setChecked(true);
////        }else {
////            newsCategoryModel.setSelected(false);
////            viewHolder.newsCategoryCheckBox.setChecked(false);
////        }
//
//         chekedNewsList = CommonUtility.ChekedNewsList;
//
//        Log.e("isCheckedItem", isCheckedItem+"");
//         newsCategoryName = newsCategoryList.get(position).getNewsCategoryName();
//        if(chekedNewsList!=null) {
//            for (int i = 0; i < chekedNewsList.size(); i++) {
//                if (chekedNewsList.get(i).equalsIgnoreCase(newsCategoryName)) {
//                    viewHolder.newsCategoryCheckBox.setChecked(true);
//                }
//            }
//        }
//
//        viewHolder.newsCategoryCheckBox.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                CheckBox checkBox = (CheckBox) v;
//
//                newsCategoryModel = (NewsCategoryModel) checkBox.getTag();
//
//                newsCategoryList.get(pos).setSelected(checkBox.isChecked());
//
//            }
//        });
//









    }

    // Return the size arraylist
    @Override
    public int getItemCount() {
        return newsCategoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CheckBox newsCategoryCheckBox;
        public TextView newsCategoryName;
        public LinearLayout catPlayout;
        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            newsCategoryName = (TextView) itemLayoutView.findViewById(R.id.newsCategoryName);
            newsCategoryCheckBox = (CheckBox) itemLayoutView.findViewById(R.id.newsCategoryCheckBox);
            catPlayout=(LinearLayout)itemLayoutView.findViewById(R.id.catPlayout);
        }
    }

    // method to access in activity after updating selection
    public ArrayList<NewsCategoryModel> getNewsCategoryList() {
        return newsCategoryList;
    }

}