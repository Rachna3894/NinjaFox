package com.mojodigi.ninjafox.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mojodigi.ninjafox.Adapter.BlockListAdapter;
//import com.mojodigi.ninjafox.AddsUtility.AddMobUtils;
import com.mojodigi.ninjafox.Model.BlockListModel;
import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.SharedPrefs.SharedPreferenceUtil;
import com.mojodigi.ninjafox.Unit.CommonUtility;

import java.util.ArrayList;
import java.util.List;

public class BlockListActivity extends Activity  {

    Context mContext;
    SharedPreferenceUtil prefs;
    ListView listView;
    TextView textView;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_list);
        mContext=this;
        prefs=new SharedPreferenceUtil(mContext);

        TextView title_txt = findViewById(R.id.title_txt);
        LinearLayout backButton = findViewById(R.id.backButton);
        listView = findViewById(R.id.record_list);
        textView = (TextView)findViewById(R.id.record_list_empty);
        TextView unblockAllText=findViewById(R.id.unblockAllText);

        title_txt.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        textView.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        unblockAllText.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        unblockAllText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(prefs.getBlockedArrayList(mContext)!=null)
                {
                    prefs.saveBlockedList(new ArrayList<BlockListModel>(), CommonUtility.PREF_BLOCK_LIST); //reset all data
                    setUpData();
                }
            }
        });

        List<BlockListModel> list=null;
        if(prefs.getBlockedArrayList(mContext)!=null) {
            list = prefs.getBlockedArrayList(mContext);
        }


        listView.setEmptyView(textView);
        if(list!=null) {
            BlockListAdapter adapter = new BlockListAdapter(mContext, R.layout.block_list_item, list, 0);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

//        AddMobUtils addMobUtils=new AddMobUtils();
//        addMobUtils.dispFacebookBannerAdd(BlockListActivity.this, BlockListActivity.this);

    }

    private void setUpData()
    {
        List<BlockListModel> list=null;
        if(prefs.getBlockedArrayList(mContext)!=null) {
            list = prefs.getBlockedArrayList(mContext);
        }

        listView.setEmptyView(textView);
        if(list!=null) {
            BlockListAdapter adapter = new BlockListAdapter(mContext, R.layout.block_list_item, list, 0);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

}
