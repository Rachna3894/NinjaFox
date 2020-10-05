package com.mojodigi.ninjafox.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
//import com.mojodigi.ninjafox.AddsUtility.AddMobUtils;
import com.mojodigi.ninjafox.AddsUtility.AddMobUtils;
import com.mojodigi.ninjafox.Database.Record;
import com.mojodigi.ninjafox.Database.RecordAction;
import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.SharedPrefs.AppConstants;
import com.mojodigi.ninjafox.Unit.CommonUtility;
import com.mojodigi.ninjafox.View.RecordAdapter_Rv_Mv;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MostVisistedActiity extends Activity implements RecordAdapter_Rv_Mv.adapterListener {

    Context mContext;
    Context mainAcitivtContext;

    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();

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
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stoptimertask();
    }

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();
        timer.schedule(timerTask, AppConstants.AddRequestInterval, AppConstants.AddRequestInterval); //
    }
    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                        //get the current timeStamp
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
                        final String strDate = simpleDateFormat.format(calendar.getTime());

                        //show the toast
                        int duration = Toast.LENGTH_SHORT;
                        // Toast toast = Toast.makeText(getApplicationContext(), strDate, duration);
                        Log.d("Fberror_banner_Int", ""+strDate);
                        //toast.show();
                        AddMobUtils addMobUtils=new AddMobUtils();
                        addMobUtils.dispFacebookInterestialAddsAtInterval(mContext);


                    }
                });
            }
        };
    }
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
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
