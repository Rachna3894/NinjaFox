package com.mojodigi.ninjafox.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
//import com.mojodigi.ninjafox.AddsUtility.AddMobUtils;
import com.mojodigi.ninjafox.AddsUtility.AddMobUtils;
import com.mojodigi.ninjafox.Database.Record;
import com.mojodigi.ninjafox.Database.RecordAction;
import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.Unit.CommonUtility;
import com.mojodigi.ninjafox.View.RecordAdapter;
import com.mojodigi.ninjafox.View.RecordAdapter_Rv_Mv;

import java.util.List;

public class RecentListActivity extends Activity implements  RecordAdapter_Rv_Mv.adapterListener {

  Context mContext;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);
         mContext=this;

         TextView title_txt = findViewById(R.id.title_txt);
         LinearLayout backButton = findViewById(R.id.backButton);
         ListView listView = findViewById(R.id.record_list);
         TextView textView = (TextView)findViewById(R.id.record_list_empty);


        title_txt.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        textView.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        RecordAction action = new RecordAction(mContext);
        action.open(true);
        List<Record> list=action.listMostRecent();

        listView.setEmptyView(textView);
        RecordAdapter_Rv_Mv adapter = new RecordAdapter_Rv_Mv(mContext, R.layout.record_item, list,0,this);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

//        AddMobUtils addMobUtils=new AddMobUtils();
//        addMobUtils.dispFacebookBannerAdd(RecentListActivity.this, RecentListActivity.this);

        AddMobUtils addMobUtils=CommonUtility.getAddMobInstance();
        addMobUtils.dispFacebookBannerAdd(RecentListActivity.this, RecentListActivity.this);

    }

    @Override
    public void onNewTabSelectd(Record record) {
        Intent intent=new Intent();
        intent.putExtra(CommonUtility.INTENT_DATA_KEY,record.getURL());
        setResult(CommonUtility.MOST_RECENT_REQUEST_CODE,intent);
        finish();
    }

    @Override
    public void onRecordDeleted(Record record) {

    }
}
