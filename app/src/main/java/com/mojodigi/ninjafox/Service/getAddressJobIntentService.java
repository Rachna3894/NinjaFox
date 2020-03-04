package com.mojodigi.ninjafox.Service;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import java.util.List;
import java.util.Locale;


public class getAddressJobIntentService extends JobIntentService {

    /**
     * Unique job ID for this service.
     */
    public static volatile boolean shouldStop = false;
    static final int JOB_ID = 1000;
    final String TAG = "MyJobIntenetService";
    Context mContext;
    private ResultReceiver addressResultReceiver;

    /**
     * Convenience method for enqueuing work in to this service.
     */
   public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, getAddressJobIntentService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        // We have received work to do.  The system or framework is already
        // holding a wake lock for us at this point, so we can just go.
        // Log.d(TAG, "Start LockScreenService called in JobService >= 26 ->>");
        String msg = "";
        //get result receiver from intent
        if(intent != null) {
            addressResultReceiver = intent.getParcelableExtra("add_receiver");
        }
        if (addressResultReceiver == null) {
            Log.e("GetAddressIntentService",
                    "No receiver, not processing the request further");
            return;
        }


        Location location = intent.getParcelableExtra("add_location");

        //send no location error to results receiver
        if (location == null) {
            msg = "No location, can't go further without location";
            sendResultsToReceiver(0, msg, "", "");
            return;
        }
        //call GeoCoder getFromLocation to get address
        //returns list of addresses, take first one and send info to result receiver
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1);
        } catch (Exception ioException) {
            Log.e("", "Error in getting address for the location");
        }

        if (addresses == null || addresses.size() == 0) {
            msg = "No address found for the location";
            sendResultsToReceiver(1, msg, "", "");
        } else {
            Address address = addresses.get(0);
            StringBuffer addressDetails = new StringBuffer();

            addressDetails.append(" " + address.getFeatureName());
            //addressDetails.append("\n");

            addressDetails.append(" " + address.getThoroughfare());
            // addressDetails.append("\n");

            addressDetails.append(" Locality: ");
            addressDetails.append(" " + address.getLocality());
            String city = address.getLocality();
            // addressDetails.append("\n");

            addressDetails.append(" County: ");
            addressDetails.append(" " + address.getSubAdminArea());
            // addressDetails.append("\n");

            addressDetails.append(" State: ");
            addressDetails.append(" " + address.getAdminArea());
            String state = address.getAdminArea();
            // addressDetails.append("\n");

            addressDetails.append(" Country: ");
            addressDetails.append(" " + address.getCountryName());
            //addressDetails.append("\n");

            addressDetails.append(" Postal Code: ");
            addressDetails.append(" " + address.getPostalCode());
            //addressDetails.append("\n");


            sendResultsToReceiver(2, addressDetails.toString(), city, state);


        }
    }

    //to send results to receiver in the source activity
    private void sendResultsToReceiver(int resultCode, String message,String city,String state) {
        Bundle bundle = new Bundle();
        bundle.putString("address_result", message);
        bundle.putString("state", state);
        bundle.putString("city", city);
        addressResultReceiver.send(resultCode, bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.wtf(TAG, "All work complete");

    }

}