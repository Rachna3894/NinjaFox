package com.mojodigi.ninjafox.Activity;

import android.app.Activity;
import android.content.Intent;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.Unit.CommonUtility;


public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_layout);
        TextView versionTxt=findViewById(R.id.versionTxt);
        versionTxt.setTypeface(CommonUtility.typeFace_Calibri_Regular(SplashActivity.this) );
        String version="";
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        versionTxt.setText(getResources().getString(R.string.setting_title_version)+" : "+version);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                redirectActivity(MainActivity.class, true);
                //redirectActivity(SignInActivity.class, true);

            }
        },  800);

    }

    private void redirectActivity(final Class<? extends Activity> ActivityToOpen, boolean finish)
    {
        Intent i  = new Intent(getBaseContext(), ActivityToOpen);
        startActivity(i);
        if(finish)
            finish();
    }
}
