package com.mojodigi.ninjafox.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.SharedPrefs.AppConstants;
import com.mojodigi.ninjafox.Task.ApiRequestTask;
import com.mojodigi.ninjafox.Unit.CommonUtility;
import com.mojodigi.ninjafox.Unit.JsonParser;
import com.mojodigi.ninjafox.View.jmmToast;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity implements ApiRequestTask.JsonLoadListener {


  Context mContext;
  SignUpActivity instnace;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mContext=this;
        instnace=this;
        //setupToolbar(getResources().getString(R.string.signup));
        initLayoutComponent();

    }


    private void initLayoutComponent() {


        TextView title_txt = findViewById(R.id.title_txt);
        LinearLayout backButton = findViewById(R.id.backButton);
        title_txt.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        final EditText edit_mobile=findViewById(R.id.edit_mobile);
        final EditText edit_Name=findViewById(R.id.edit_Name);
        final EditText edit_email=findViewById(R.id.edit_email);
        final EditText edit_password=findViewById(R.id.edit_password);
        final EditText edit_cpasswor=findViewById(R.id.edit_cpasswor);
        final Button signUpButton=findViewById(R.id.signUpButton);

        /*set typeface*/
        edit_cpasswor.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        edit_Name.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        edit_email.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        edit_mobile.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        edit_password.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        signUpButton.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));



        edit_mobile.setText(CommonUtility.getCountryDialCode(mContext));


        edit_mobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                        edit_mobile.setSelection(edit_mobile.getText().length());
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if( !TextUtils.isEmpty(edit_Name.getText().toString().trim())&& !TextUtils.isEmpty(edit_email.getText().toString()) && !TextUtils.isEmpty(edit_mobile.getText().toString()) && !TextUtils.isEmpty(edit_password.getText().toString()) && !TextUtils.isEmpty(edit_cpasswor.getText().toString())) {

                    if(!CommonUtility.isValidEmailId(edit_email.getText().toString().trim()))
                    {
                        jmmToast.show(mContext, mContext.getString(R.string.invalid_email));
                        return;
                    }

                    if(!TextUtils.equals(edit_cpasswor.getText().toString(), edit_password.getText().toString()))
                    {
                        jmmToast.show(mContext, mContext.getString(R.string.password_not_match));
                        return;
                    }

                    JSONObject signUpObject = new JSONObject();
                    String encodedPassword = CommonUtility.enCodeString(edit_password.getText().toString().trim());
                    System.out.println("" + encodedPassword);


                    try {
                        signUpObject.put("name", edit_Name.getText().toString());
                        signUpObject.put("cPassword", encodedPassword.trim());
                        signUpObject.put("mobileNo", edit_mobile.getText().toString());
                        signUpObject.put("email", edit_email.getText().toString().trim());
                        signUpObject.put("password",encodedPassword.trim());
                        signUpObject.put("source", "normal");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    new ApiRequestTask(mContext, instnace, CommonUtility.registerApi, false,true,mContext.getString(R.string.create_user), signUpObject.toString(), AppConstants.registerApiRequest).execute();
                }
                else {
                    jmmToast.show(mContext, mContext.getString(R.string.fill_details));
                }


            }
        });


    }

    /*private void setupToolbar(String title) {

        TextView activityTitle=findViewById(R.id.activityTitle);
        ImageView backButton=findViewById(R.id.backArrow);
        ImageView searchButton=findViewById(R.id.search);
        activityTitle.setText(title);
        activityTitle.setTypeface(FontUtility.typeFace_Calibri_Regular(mContext));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        searchButton.setVisibility(View.GONE);


    }
*/
    @Override
    public void onJsonLoad(String json, int mRequestCode) {

        if(mRequestCode==AppConstants.registerApiRequest)
        {
            try {
                JSONObject registerResponse= new JSONObject(json);
                String msg= JsonParser.getkeyValue_Str(registerResponse, "message");
                  jmmToast.show(mContext, msg);

                  finish();
                  /*if(registerResponse.has("status"))
                  {
                      String status=JsonParser.getkeyValue_Str(registerResponse, "status");

                  }*/


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }

    @Override
    public void onLoadFailed(String msg) {

    }
}
