package com.mojodigi.ninjafox.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.Unit.CommonUtility;

public class SuggestedAppsActivity extends AppCompatActivity {

    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;

        setContentView(R.layout.activity_suggested_apps);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (mContext == null) {
            mContext = SuggestedAppsActivity.this;
        }

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(CommonUtility.getString(mContext, R.string.suggested_apps));
        }


        RelativeLayout fileHuntLayout=findViewById(R.id.app1Layout);
        RelativeLayout videoPlayer=findViewById(R.id.app2Layout);
        RelativeLayout khulasaLite=findViewById(R.id.app3Layout);
        RelativeLayout selfiePro=findViewById(R.id.app4Layout);
        RelativeLayout screenlockLayout=findViewById(R.id.app5Layout);
        RelativeLayout scanner=findViewById(R.id.app6Layout);
        RelativeLayout khulasaNews=findViewById(R.id.app7Layout);

        TextView txtApp1=findViewById(R.id.txtApp1);
        TextView txtApp2=findViewById(R.id.txtApp2);
        TextView txtApp3=findViewById(R.id.txtApp3);
        TextView txtApp4=findViewById(R.id.txtApp4);
        TextView txtApp5=findViewById(R.id.txtApp5);
        TextView txtApp6=findViewById(R.id.txtApp6);
        TextView txtApp7=findViewById(R.id.txtApp7);


        txtApp1.setTypeface( CommonUtility.typeFace_Calibri_Regular(mContext));
        txtApp2.setTypeface( CommonUtility.typeFace_Calibri_Regular(mContext));
        txtApp3.setTypeface( CommonUtility.typeFace_Calibri_Regular(mContext));
        txtApp4.setTypeface( CommonUtility.typeFace_Calibri_Regular(mContext));
        txtApp5.setTypeface( CommonUtility.typeFace_Calibri_Regular(mContext));
        txtApp6.setTypeface( CommonUtility.typeFace_Calibri_Regular(mContext));
        txtApp7.setTypeface( CommonUtility.typeFace_Calibri_Regular(mContext));


        fileHuntLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String appPackageName = "com.mojodigi.filehunt"; // package name of the app
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });


        videoPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String appPackageName = "com.mojodigi.videoplayer"; // package name of the app
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });




        khulasaLite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String appPackageName = "com.mojodigi.khulasaNewsLite"; // package name of the app
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        khulasaNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = "com.mojodigi.khulasanews"; // package name of the app
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        selfiePro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String appPackageName = "com.mojodigi.selfiepro"; // package name of the app
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });



        screenlockLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String appPackageName = "com.mojodigi.screenlock"; // package name of the app
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String appPackageName = "com.mojodigi.smartcamscanner"; // package name of the app
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



    @Override
    public void onBackPressed() {

        super.onBackPressed();
        return;

    }
}