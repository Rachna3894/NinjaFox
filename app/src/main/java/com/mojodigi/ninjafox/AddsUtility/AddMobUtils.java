package com.mojodigi.ninjafox.AddsUtility;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;


import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.SharedPrefs.AppConstants;
import com.mojodigi.ninjafox.SharedPrefs.SharedPreferenceUtil;
import com.mojodigi.ninjafox.View.jmmRelativeLayout;


public class AddMobUtils extends Activity {

    public AddMobUtils() {

    }


    public void dispFacebookBannerAdd(Context mContext, Activity layout ) {
        SharedPreferenceUtil prefs = new SharedPreferenceUtil(mContext);
        String bannerAddid = prefs.getStringValue(AppConstants.BANNER_ADD_ID, "0");
        boolean showAdd = prefs.getBoolanValue(AppConstants.SHOW_ADD, false);

        if (showAdd) {
            try {
                com.facebook.ads.AdView fbAdView = new com.facebook.ads.AdView(mContext, bannerAddid, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
                final LinearLayout adContainer = layout.findViewById(R.id.banner_container);
                adContainer.addView(fbAdView);
                fbAdView.setAdListener(new com.facebook.ads.AdListener() {
                    @Override
                    public void onError(Ad ad, AdError adError) {
                        // Ad error callback

                        Log.d("Fberror_banner", "" + adError.getErrorMessage());
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        // Ad loaded callback


                        adContainer.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAdClicked(Ad ad) {
                        // Ad clicked callback
                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {
                        // Ad impression logged callback
                    }
                });

                // Request an ad
                fbAdView.loadAd();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public  void dispFacebookBannerAdd(Context mContext, jmmRelativeLayout layout ) {

        SharedPreferenceUtil prefs = new SharedPreferenceUtil(mContext);
        String bannerAddid = prefs.getStringValue(AppConstants.BANNER_ADD_ID, "0");
        boolean showAdd = prefs.getBoolanValue(AppConstants.SHOW_ADD, false);

           if(showAdd) {
               try {
                   com.facebook.ads.AdView fbAdView = new com.facebook.ads.AdView(mContext, bannerAddid, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
                   final LinearLayout adContainer = layout.findViewById(R.id.banner_container);
                   adContainer.addView(fbAdView);
                   fbAdView.setAdListener(new com.facebook.ads.AdListener() {
                       @Override
                       public void onError(Ad ad, AdError adError) {
                           // Ad error callback

                           Log.d("Fberror_banner", "" + adError.getErrorMessage()+" Code-> "+adError.getErrorCode());
                       }

                       @Override
                       public void onAdLoaded(Ad ad) {
                           // Ad loaded callback

                           adContainer.setVisibility(View.VISIBLE);
                       }

                       @Override
                       public void onAdClicked(Ad ad) {
                           // Ad clicked callback
                       }

                       @Override
                       public void onLoggingImpression(Ad ad) {
                           // Ad impression logged callback
                       }
                   });

                   // Request an ad
                   fbAdView.loadAd();


               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
    }

    public void dispFacebookInterestialAdds(Context mContext) {
        SharedPreferenceUtil prefs = new SharedPreferenceUtil(mContext);
        String interestialAddid = prefs.getStringValue(AppConstants.INTERESTIAL_ADD_ID, "0");
        boolean showAdd = prefs.getBoolanValue(AppConstants.SHOW_ADD, false);


        if (showAdd) {
            final String TAG = AddMobUtils.class.getSimpleName();
            final com.facebook.ads.InterstitialAd interstitialAd = new com.facebook.ads.InterstitialAd(mContext, interestialAddid);

            // Set listeners for the Interstitial Ad
            interstitialAd.setAdListener(new com.facebook.ads.InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    // Interstitial ad displayed callback
                    Log.e(TAG, "Interstitial ad displayed.");
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    // Interstitial dismissed callback
                    Log.e(TAG, "Interstitial ad dismissed.");
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    // Ad error callback
                    Log.e(TAG, "Fberror_banner_Interstitial " + adError.getErrorMessage());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Interstitial ad is loaded and ready to be displayed
                    Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                    // Show the ad
                    interstitialAd.show();

                    //showAdWithDelay();
                }

                @Override
                public void onAdClicked(Ad ad) {
                    // Ad clicked callback
                    Log.d(TAG, "Interstitial ad clicked!");
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    // Ad impression logged callback
                    Log.d(TAG, "Interstitial ad impression logged!");
                }
            });

            // For auto play video ads, it's recommended to load the ad
            // at least 30 seconds before it is shown
            interstitialAd.loadAd();
        }

    }

    }

