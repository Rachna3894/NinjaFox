package com.mojodigi.ninjafox.Activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.Unit.CommonUtility;
import com.mojodigi.ninjafox.Unit.IntentUtility;

public class FeedBackActivity extends Activity {

    Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue_500)));
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_feedback);

        mContext=this;
        EditText edit_Subject=findViewById(R.id.edit_Subject);
        EditText edit_Text=findViewById(R.id.edit_Text);
        Button btn_Submit=findViewById(R.id.button_Submit);


        edit_Subject.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        edit_Text.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        btn_Submit.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
