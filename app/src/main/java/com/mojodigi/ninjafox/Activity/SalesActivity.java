package com.mojodigi.ninjafox.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.Unit.CommonUtility;

public class SalesActivity  extends Activity {

    Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

          setContentView( R.layout.activity_sales);

          mContext=this;
        TextView title_txt = findViewById(R.id.title_txt);
        LinearLayout backButton = findViewById(R.id.backButton);

        title_txt.setText("Mantra products");
        title_txt.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));




        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });



        TextView buyP1=findViewById(R.id.buyP1);
        TextView buyP2=findViewById(R.id.buyP2);
        TextView buyP3=findViewById(R.id.buyP3);

        buyP1.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        buyP2.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        buyP3.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));


        buyP1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent=new Intent(SalesActivity.this,SalesActivity_Product2.class);
                startActivity(intent);

            }
        });

        buyP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(SalesActivity.this,SalesActivity_Product.class);
                startActivity(intent);


            }
        });

        buyP3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(SalesActivity.this,SalesActivity_Product3.class);
                startActivity(intent);
            }
        });



    }
}
