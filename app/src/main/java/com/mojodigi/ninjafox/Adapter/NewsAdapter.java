package com.mojodigi.ninjafox.Adapter;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mojodigi.ninjafox.Activity.MainActivity;
import com.mojodigi.ninjafox.Model.NewsDataModel;
import com.mojodigi.ninjafox.Model.NewsList;
import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.SharedPrefs.AppConstants;
import com.mojodigi.ninjafox.SharedPrefs.SharedPreferenceUtil;
import com.mojodigi.ninjafox.Unit.CommonUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private List<NewsList> newsList;
    private newsListener listener;
    Context ctx;

    public NewsAdapter(Context ctx ) {
        this.ctx=ctx;

    }

    public NewsAdapter( Context ctx ,  List<NewsList> newsList) {
        this.newsList = newsList;
        this.ctx=ctx;

    }

    public NewsAdapter(Context ctx , List<NewsList> newsList, newsListener listener ) {
        this.newsList = newsList;
        this.listener = listener;
        this.ctx=ctx;

    }
    @Override
    public  RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View  itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_view, parent, false);
        return new NewsAdapter.NewsViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        try {
            ((NewsViewHolder) holder).newsTitle.setText(newsList.get(position).getTitle() + "");
            ((NewsViewHolder) holder).newsTitle.setSelected(true);
            ((NewsViewHolder) holder).newsTitle.setTypeface(CommonUtility.typeFace_Calibri_Regular(ctx));
            ((NewsViewHolder) holder).newsTitle.requestFocus();

            String imagePath = newsList.get(position).getImageUrl();

            if (imagePath != null) {
                Glide.with(ctx).load(imagePath)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.IMMEDIATE)
                        .skipMemoryCache(false).placeholder(R.drawable.image_holder).error(R.drawable.image_holder)
                        .into(((NewsViewHolder) holder).newsIcon);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        listener.onNewsListSize(newsList.size());
        return newsList.size();
    }


    public class NewsViewHolder extends RecyclerView.ViewHolder
    {
        public TextView newsTitle;
        public ImageView newsIcon;


        public NewsViewHolder(View view) {
            super(view);
            newsTitle = (TextView) view.findViewById(R.id.newsTitle);
            newsIcon=(ImageView)view.findViewById(R.id.newsIcon);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos=getAdapterPosition();
                    if(pos!= RecyclerView.NO_POSITION)
                        listener.onNewsClicked(newsList.get(getAdapterPosition()));

                }
            });
        }
    }



    public interface newsListener {

        void onNewsListSize(int size);
        void onNewsClicked(NewsList newsDataModel);

    }


}