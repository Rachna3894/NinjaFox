package com.mojodigi.ninjafox.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.mojodigi.ninjafox.View.RecordAdapter_Rv_Mv;

import java.util.List;

public class MostVisistedActiity extends Activity implements RecordAdapter_Rv_Mv.adapterListener {

    Context mContext;
    Context mainAcitivtContext;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_most_visited);
        mContext=this;
        mainAcitivtContext=new MainActivity();

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
        List<Record> list=action.listMostVisisted();

        listView.setEmptyView(textView);
        RecordAdapter_Rv_Mv adapter = new RecordAdapter_Rv_Mv(mContext, R.layout.record_item, list,0,this);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


//        AddMobUtils addMobUtils=new AddMobUtils();
//        addMobUtils.dispFacebookBannerAdd(MostVisistedActiity.this, MostVisistedActiity.this);

        AddMobUtils addMobUtils=CommonUtility.getAddMobInstance();
        addMobUtils.dispFacebookBannerAdd(MostVisistedActiity.this, MostVisistedActiity.this);
    }

    @Override
    public void onNewTabSelectd(Record record) {

        Intent intent=new Intent();
        intent.putExtra(CommonUtility.INTENT_DATA_KEY,record.getURL());
        setResult(1001,intent);
        finish();
    }

    @Override
    public void onRecordDeleted(Record record) {



    }
}
