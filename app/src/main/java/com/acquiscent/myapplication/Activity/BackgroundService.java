package com.acquiscent.myapplication.Activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.acquiscent.myapplication.Controller.AppController;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)

public class BackgroundService extends Service {

    SharedPreferences sharedpreferencesId;

    SharedPreferences.Editor editorId;
    public static final String MyPREFERENCEId = "preferenceId";

    SharedPreferences sharedpreferencesLatLong;
    SharedPreferences.Editor editorLatLong;
    public static final String MyPREFERENCELatLong = "preferenceLatLong";

    private final IBinder mBinder = new LocalBinder();
    MapsActivity map;
    private Timer timer;
    String driverid, journeyid, latitude, longitude;

    Cryptography_Android cryptography;
    static String _cipherKey = "P@SSW@RD@09";
    //  String url = "http://fms.terabhyte.com/_trackingService.asmx/SaveNewVechileLocation";
    String output;
    String encriptString = "";

    private class LocalBinder extends Binder {
        BackgroundService getService()
        {
            return BackgroundService.this;
        }
    }

    @Override
    public void onCreate() {
        /***********************NOTIFICATION**********************/

        sharedpreferencesId = getSharedPreferences(MyPREFERENCEId, Context.MODE_PRIVATE);
        editorId = sharedpreferencesId.edit();

        journeyid = sharedpreferencesId.getString("journeyid", "").toString();
        driverid = sharedpreferencesId.getString("driverid", "").toString();
        System.out.print("journey id-----------" + journeyid);

        sharedpreferencesLatLong = getSharedPreferences(MyPREFERENCELatLong, Context.MODE_PRIVATE);
        editorLatLong = sharedpreferencesLatLong.edit();

        latitude = sharedpreferencesLatLong.getString("latitude", "").toString();
        longitude = sharedpreferencesLatLong.getString("longitude", "").toString();
        System.out.print("latitude-----------" + latitude);


        if (isNetworkAvailable()) {

            try {

                encriptString = "|||" + latitude + "|||" + longitude + "|||" + driverid + "|||" + journeyid;

                System.out.print("cocatination of string--------" + encriptString);
                cryptography.Encrypt(encriptString, _cipherKey);
                System.out.print("encode data=================================" + cryptography.Encrypt(encriptString, _cipherKey));
                output = cryptography.Encrypt(encriptString, _cipherKey);
                System.out.print("output========" + output);

            } catch (Exception e) {
                e.printStackTrace();
            }

            final Handler handler = new Handler();
            timer = new Timer();
            TimerTask doAsynchronousTask = new TimerTask() {
                @Override
                public void run() {
                    handler.post( new Runnable() {
                        public void run() {
                            try {
                                //getData();
                                System.out.print("===========service started=========");
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "catch null pointer", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            };
            timer.schedule(doAsynchronousTask, 0, 2000);
        }

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public void onDestroy() {
        try {
            timer.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        stopSelf();
    }

  /*  public void getData() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // response
                Log.d("Response", response);


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("locationString", output);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(postRequest);
    }*/

    /**
     * Checks whether the network is available.
     */
    public boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }


    /***********************
     * SELL NOTIFICATION
     ***********************/

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("NewApi")

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

}