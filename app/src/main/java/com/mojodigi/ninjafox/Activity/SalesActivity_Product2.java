package com.mojodigi.ninjafox.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.Unit.CommonUtility;
import com.mojodigi.ninjafox.View.jmmToast;

public class SalesActivity_Product2 extends Activity {

    Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext =this;




        setContentView( R.layout.activity_sale_product2);



        TextView title_txt = findViewById(R.id.title_txt);
        LinearLayout backButton = findViewById(R.id.backButton);

        title_txt.setText("Power Mantra");
        title_txt.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        ImageView prodImage=findViewById(R.id.prodImage);

        TextView prod_head=findViewById(R.id.prod_head);
        TextView prodSdesc=findViewById(R.id.prodSdesc);
        TextView prodLdesc=findViewById(R.id.prodLdesc);
        TextView prodqtyTxt=findViewById(R.id.prodqtyTxt);
        TextView prodcapBox=findViewById(R.id.prodcapBox);
        TextView txtName=findViewById(R.id.txtName);
        TextView txtAddress=findViewById(R.id.txtAddress);
        TextView txtPin=findViewById(R.id.pinTxt);
        TextView txtMobile=findViewById(R.id.txtMobile);



        final EditText edit_Name=findViewById(R.id.edit_Name);
        final EditText edit_address=findViewById(R.id.edit_address);
        final EditText edit_pin=findViewById(R.id.edit_pin);
        final EditText edit_mobile=findViewById(R.id.edit_mobile);



        RadioButton  rad1=findViewById(R.id.rad1);
        RadioButton  rad2=findViewById(R.id.rad1);
        RadioButton  rad3=findViewById(R.id.rad1);


        Button buttonPay=findViewById(R.id.buttonPay);


        prod_head.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        prodcapBox.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        prodLdesc.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        prodSdesc.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        prodqtyTxt.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));

        txtAddress.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        txtMobile.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        txtName.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        txtPin.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));

        edit_Name.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        edit_address.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        edit_mobile.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        edit_pin.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));

        rad1.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        rad2.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        rad3.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        buttonPay.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));

        rad1.setChecked(true);



        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 if(edit_Name.getText().toString().length()==0)
                {
                    jmmToast.show(mContext,"Please write name");
                    return;
                }
                else if(edit_address.getText().toString().length()==0)
                {
                    jmmToast.show(mContext, "Please fill address");
                    return;
                }
                 else  if(edit_pin.getText().toString().length()==0 || edit_pin.getText().toString().length()<6)
                 {
                     jmmToast.show(mContext, "Please write correct pin");
                     return;
                 }

                 else if(edit_mobile.getText().toString().length()<10)
                {
                    jmmToast.show(mContext, "Please write 10 digit mobile number ");
                    return;
                }





                jmmToast.show(mContext, "Coming soon...");


            }
        });


    }
}
