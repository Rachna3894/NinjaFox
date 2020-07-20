package com.mojodigi.ninjafox.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mojodigi.ninjafox.Activity.WebViewActivity;
import com.mojodigi.ninjafox.Adapter.NewsAdapter;
import com.mojodigi.ninjafox.Model.NewsList;
import com.mojodigi.ninjafox.Model.NewsMainModel;
import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.SharedPrefs.SharedPreferenceUtil;
import com.mojodigi.ninjafox.Unit.CommonUtility;
import com.mojodigi.ninjafox.View.jmmToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class NewsFragment extends Fragment implements NewsAdapter.newsListener  {

    private View view;
    private NewsAdapter.newsListener   newsListener ;
    private RecyclerView news_recycler_view;
    static ArrayList<NewsList> newsListData = null;

    static String categoryId ;
    //static String prefCategoryId ;
    static String categoryName ;
    ArrayList<String> prefCategoryIdList;

    static  enableDisablerefreshListener listener;

    public static int scrollX = 0;
    public static int scrollY = -1;

    public static NewsFragment newInstance(ArrayList<NewsMainModel> parentList , int position ,  enableDisablerefreshListener listener1) {
        listener=listener1;
        NewsFragment fragment = new NewsFragment();
        newsListData = parentList.get(position).getNewsList();
        //categoryId = parentList.get(position).getCategoryId();
        //categoryName = parentList.get(position).getCategoryName();

        Bundle bundle = new Bundle();
        bundle.clear();
        bundle.putSerializable(CommonUtility.News_List_Data, parentList.get(position).getNewsList());
        //bundle.putSerializable(CommonUtility.News_Category_ID, parentList.get(position).getCategoryId());
        //bundle.putSerializable(CommonUtility.News_Category_Name, parentList.get(position).getCategoryName());
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onSaveInstanceState(Bundle oldInstanceState)
    {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_news, container, false);
        this.newsListener = this;
        newsListData = (ArrayList<NewsList>) getArguments().getSerializable(CommonUtility.News_List_Data);
        news_recycler_view = (RecyclerView) view.findViewById(R.id.news_recycler_view);
        final ProgressBar progressBar=view.findViewById(R.id.progressBar);
      /*today*/
        final NestedScrollView scrollView=view.findViewById(R.id.nestedScrollView);
        /*today*/
        news_recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                    progressBar.setVisibility(View.VISIBLE);
                    listener.reachedToEndOfRecyclerView();

                }
            }
        });

        if(newsListData.size()>=1) {

            final NewsAdapter adapter = new NewsAdapter(getActivity(), newsListData, newsListener);
            news_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            news_recycler_view.setAdapter(adapter);

            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);

            news_recycler_view.post(new Runnable() {
                @Override
                public void run() {

                   // news_recycler_view.scrollToPosition(adapter.getItemCount()-CommonUtility.newsCount);
                }
            });


            scrollView.post(new Runnable() {
                @Override
                public void run() {

                    // scroll to the last selected position after getting data on swipe from bottom;
                    scrollView.scrollTo(CommonUtility.scrollX,CommonUtility.scrollY);

                }
            });

            scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView nestedScrollView, int x, int y, int oldX, int oldY) {

                    CommonUtility.scrollX = x;
                    CommonUtility.scrollY = y;

                }
            });



            CommonUtility.newsCount=newsListData.size();


        }
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    String url ="";

    @Override
    public void onNewsClicked(NewsList newsDataModel) {

        url = newsDataModel.getCanonicalUrl();

        if(!url.trim().isEmpty()) {

           /* Intent browserIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ((Activity)view.getContext()).startActivity(browserIntent);*/



             listener.onNewsDecreaseTabCount();

            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("url", url);
            getActivity().startActivity(intent);

             url = "";



        }

    }



    @Override
    public void onNewsListSize(int size) {
        listener.onNewsListSize(size);
    }




    public  interface  enableDisablerefreshListener
    {

        //boolean enablerefresh();
        //boolean disablerefresh();
        //boolean isEnableRefresh();
        //boolean isDisableRefresh();
        //boolean isRefreshing();
        //boolean setRefreshing();

        void  onNewsListSize(int size);
        void  onNewsDecreaseTabCount();
        void reachedToEndOfRecyclerView();



    }

}
