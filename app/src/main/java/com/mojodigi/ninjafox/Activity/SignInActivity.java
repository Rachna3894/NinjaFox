package com.mojodigi.ninjafox.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.SharedPrefs.AppConstants;
import com.mojodigi.ninjafox.SharedPrefs.SharedPreferenceUtil;
import com.mojodigi.ninjafox.Task.ApiRequestTask;
import com.mojodigi.ninjafox.Unit.CommonUtility;
import com.mojodigi.ninjafox.Unit.JsonParser;
import com.mojodigi.ninjafox.View.jmmToast;

import org.json.JSONException;
import org.json.JSONObject;

import static com.mojodigi.ninjafox.SharedPrefs.AppConstants.forgotPasswordApiRequestCode;

public class SignInActivity extends AppCompatActivity implements ApiRequestTask.JsonLoadListener {



    Context mContext;
    SignInActivity mInstance;
    //a constant for detecting the login intent result
    private static final int RC_SIGN_IN = 234;
    //Tag for the logs optional
    private static final String TAG = "Jmm";
    //creating a GoogleSignInClient object
    GoogleSignInClient mGoogleSignInClient;
    //And also a Firebase Auth object
    FirebaseAuth mAuth;


    SharedPreferenceUtil mPrefs;

    AlertDialog dialog_forgot_password;
    private TextView api_msg_txt;

    EditText edit_email,edit_mobile;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in);


     mContext=this;
     mInstance=this;



     mPrefs=new SharedPreferenceUtil(mContext);


        initLayoutComponents();
        FirebaseApp.initializeApp(this);

        //first we intialized the FirebaseAuth object
        mAuth = FirebaseAuth.getInstance();

        //Then we need a GoogleSignInOptions object
        //And we need to build it as below
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Now we will attach a click listener to the sign_in_button
        //and inside onClick() method we are calling the signIn() method that will open
        //google sign in intent
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    private void initLayoutComponents() {

        TextView title_txt = findViewById(R.id.title_txt);
        LinearLayout backButton = findViewById(R.id.backButton);
        title_txt.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        final Button login_button=findViewById(R.id.login_button);
        final EditText edit_password=findViewById(R.id.edit_password);
        edit_email=findViewById(R.id.edit_email);
        edit_mobile=findViewById(R.id.edit_mobile);
        TextView gmailLoginTxt=findViewById(R.id.gmailLoginTxt);
        TextView signUp=findViewById(R.id.signUp);
        TextView forgotPassword=findViewById(R.id.forgotPassword);

        /*typeface*/
        signUp.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        forgotPassword.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        login_button.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        edit_email.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        edit_mobile.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        edit_password.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        gmailLoginTxt.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        /*typeface*/


        edit_mobile.setText(CommonUtility.getCountryDialCode(mContext));

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName=getUserName();

                if (userName!=null && !TextUtils.isEmpty(edit_password.getText().toString())) {

                    String encodedPassword = CommonUtility.enCodeString(edit_password.getText().toString().trim());
                    System.out.println("" + encodedPassword);

                    JSONObject loginObject = new JSONObject();

                    try {
                        loginObject.put("email",userName);
                        loginObject.put("password", encodedPassword.trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (loginObject != null)
                        new ApiRequestTask(mContext, mInstance, CommonUtility.loginApi, false, true, mContext.getString(R.string.login_msg), loginObject.toString(), AppConstants.loginApiRequest).execute();

                }
                else
                {
                    jmmToast.show(mContext, mContext.getString(R.string.fill_details));
                }
            }


            {

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent =new Intent(mContext,SignUpActivity.class);
              startActivity(intent);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dispforgotPasswordDialog();


            }
        });
    }

    private String getUserName() {

        if(!TextUtils.isEmpty(edit_email.getText().toString()))
        {
            if(!CommonUtility.isValidEmailId(edit_email.getText().toString().trim()))
            {
                jmmToast.show(mContext, mContext.getResources().getString(R.string.invalid_email));
                 return  null ;
            }
            else {
                return edit_email.getText().toString();
            }
        }
        else if(!TextUtils.isEmpty(edit_mobile.getText().toString()))
        {
            return  edit_mobile.getText().toString().trim();
        }
        else
            return null;

    }

    private void dispforgotPasswordDialog() {


        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(true);
        final LinearLayout dialog_forgot_password_layout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_forgot_password, null, false);
        builder.setView(dialog_forgot_password_layout);

        dialog_forgot_password=builder.create();

        final TextView okTxt=dialog_forgot_password_layout.findViewById(R.id.okTxt);
        TextView cancelTxt=dialog_forgot_password_layout.findViewById(R.id.cancel_txt);
        api_msg_txt=dialog_forgot_password_layout.findViewById(R.id.api_msg_txt);
        final EditText emailEdit=dialog_forgot_password_layout .findViewById(R.id.edit_Url);


        emailEdit.setSelection(emailEdit.getText().length());
        emailEdit.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
        okTxt.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext) );
        cancelTxt.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext) );


        okTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(CommonUtility.isValidEmailId(emailEdit.getText().toString().trim()))
                {
                     if(emailEdit.getText().toString().length()>=1)
                     {

                             JSONObject object = new JSONObject();
                try {
                    object.put("email", emailEdit.getText().toString().trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //display  dialog
                new ApiRequestTask(mContext, mInstance, CommonUtility.API_FORGOT_PASSWORD, false, true, mContext.getResources().getString(R.string.msg_wait),object.toString() , forgotPasswordApiRequestCode).execute();

                     }
                     else {
                         jmmToast.show(mContext, mContext.getResources().getString(R.string.write_email));
                     }
                }
                else
                {
                    jmmToast.show(mContext,mContext.getResources().getString(R.string.invalid_email));
                }



            }
        });





        cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_forgot_password.dismiss();
            }
        });


        dialog_forgot_password.show();

    }


    @Override
    protected void onStart() {
        super.onStart();

         if(!CommonUtility.isLoggedInUser(mPrefs))
         {
             mAuth.signOut();
         }


        //if the user is already signed in
        //we will close this activity
        //and take the user to profile activity
        if (mAuth.getCurrentUser() != null) {
           // finish();
            //startActivity(new Intent(this, ProfileActivity.class));

            //jmmToast.show(mContext, "Already login");



        }





    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // here  get details of account
                //authenticating with firebase
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                String string = e.getMessage();
                Log.d("ErrorCode", ""+string);
                Toast.makeText(SignInActivity.this, string, Toast.LENGTH_SHORT).show();
            }
        }


        }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
       // jmmToast.show(mContext, acct.getId());
        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                             // get all  details  here of the user  from  user class and hoit api  on backend ;

                               JSONObject gObject=new JSONObject();
                               String name=user.getDisplayName();
                               String email=user.getEmail();
                               String mobile=user.getPhoneNumber();
                               String photoUrl= String.valueOf(user.getPhotoUrl());
                               String token=acct.getId();
                               if(mobile==null)
                                   mobile="";

                            try {
                                gObject.put("name", ""+name);
                                gObject.put("email", email);
                                gObject.put("mobileNo", mobile);
                                gObject.put("token", token);
                                gObject.put("source", "gmail");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                             System.out.print(""+gObject);
                            new ApiRequestTask(mContext,mInstance , CommonUtility.registerApi, false,true,mContext.getString(R.string.create_user), gObject.toString(), AppConstants.registerApiRequest).execute();

                          //  Toast.makeText(SignInActivity.this, "User Signed In", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }


    // https://medium.com/@ihimanshurawat/google-login-for-android-with-firebase-af9743c26d3e

    private void signIn() {
        //getting the google signin intent
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

   /* private void setupToolbar(String title) {

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


    }*/

    @Override
    public void onJsonLoad(String json, int mRequestCode) {

        if(mRequestCode==AppConstants.loginApiRequest)
        {


            //response Json structure
            /*
            *      {"status":true,"message":"success","data":[{"userid":"14","email":"xyz@gmail.com","mobile":"1111111111","password":"dGtz","registrationTime":"2019-10-14 12:11:11","token":"pfCoORdLIN"}]}
            * */
            //response Json structure



            try {
                JSONObject mainJson=new JSONObject(json);

                if(mainJson.has("status") && mainJson.has("message"))
                {
                    String status =JsonParser.getkeyValue_Str(mainJson, "status");
                    String msg =JsonParser.getkeyValue_Str(mainJson, "message");
                    jmmToast.show(mContext, msg);
                    if(status.equalsIgnoreCase("true"))
                    {

                             if(mainJson.has("data"))
                             {
                                 JSONObject dataObject=mainJson.getJSONObject("data");
                                 System.out.print(""+dataObject);
                                 String token= JsonParser.getkeyValue_Str(dataObject, "token");
                                 String userId=JsonParser.getkeyValue_Str(dataObject, "userId");
                                 String eMail=JsonParser.getkeyValue_Str(dataObject, "email");
                                 String totalEarnedPoints=JsonParser.getkeyValue_Str(dataObject, "totalEarnedPoints");
                                 System.out.print(""+token+userId+eMail);
                                 if(mPrefs!=null)
                                 {
                                     mPrefs.setValue(AppConstants.PREFS_USER_ID, userId);
                                     mPrefs.setValue(AppConstants.PREFS_EMAIL,eMail);
                                     mPrefs.setValue(AppConstants.PREFS_TOKEN,token);
                                     finish();


                                 }


                             }

                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        else  if(mRequestCode==AppConstants.registerApiRequest)
        {
            try {
                JSONObject mainJson=new JSONObject(json);

                if(mainJson.has("status") &&mainJson.has("message"))
                {
                    String status =JsonParser.getkeyValue_Str(mainJson, "status");
                    String msg =JsonParser.getkeyValue_Str(mainJson, "message");
                    jmmToast.show(mContext, msg);
                    if(status.equalsIgnoreCase("true"))
                    {

                        if(mainJson.has("data"))
                        {
                            JSONObject dataObject=mainJson.getJSONObject("data");
                            System.out.print(""+dataObject);
                            String token=JsonParser.getkeyValue_Str(dataObject, "token");
                            String userId=JsonParser.getkeyValue_Str(dataObject, "userId");
                            String eMail=JsonParser.getkeyValue_Str(dataObject, "email");
                            //String totalEarnedPoints=JsonParser.getkeyValue_Str(dataObject, "totalEarnedPoints");
                            System.out.print(""+token+userId+eMail);
                            if(mPrefs!=null)
                            {
                                mPrefs.setValue(AppConstants.PREFS_USER_ID, userId);
                                mPrefs.setValue(AppConstants.PREFS_EMAIL,eMail);
                                mPrefs.setValue(AppConstants.PREFS_TOKEN,token);
                                finish();

                            }


                        }

                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else  if(mRequestCode==forgotPasswordApiRequestCode)
        {
            //jmmToast.show(mContext, json.toString());

            try {
                JSONObject object = new JSONObject(json);
                  if(object.has("message"))
                  {

                      String message=JsonParser.getkeyValue_Str(object, "message");
                      if(api_msg_txt!=null) {
                              api_msg_txt.setVisibility(View.VISIBLE);
                              api_msg_txt.setTypeface(CommonUtility.typeFace_Calibri_Regular(mContext));
                              api_msg_txt.setText(message);

                      }
                  }


            } catch (JSONException e) {
                e.printStackTrace();
            }




        }

    }

    @Override
    public void onLoadFailed(String msg) {

    }


}


