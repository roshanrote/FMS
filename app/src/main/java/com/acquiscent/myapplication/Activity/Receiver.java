package com.acquiscent.myapplication.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by acquiscent on 30/1/18.
 */

public class Receiver extends BroadcastReceiver {
    public static final String NETWORK_AVAILABLE_ACTION = "com.autokatta.deepak.NetworkAvailable";
    public static final String IS_NETWORK_AVAILABLE = "isNetworkAvailable";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Intent networkStateIntent = new Intent(NETWORK_AVAILABLE_ACTION);
            networkStateIntent.putExtra(IS_NETWORK_AVAILABLE, isConnectedToInternet(context));
            LocalBroadcastManager.getInstance(context).sendBroadcast(networkStateIntent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
   Check Internet Connection...
    */
    public boolean isConnectedToInternet(Context context) {
        try {
            if (context != null) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(Receiver.class.getName(), e.getMessage());
            return false;
        }
    }
}
