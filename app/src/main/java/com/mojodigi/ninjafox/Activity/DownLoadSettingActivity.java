package com.mojodigi.ninjafox.Activity;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mojodigi.ninjafox.R;

public class DownLoadSettingActivity extends Activity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue_500)));
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_downlaod_settings);


    }
}
