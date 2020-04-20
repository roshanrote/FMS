package com.acquiscent.myapplication.Activity;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.util.Date;
import android.os.Binder;
import android.os.Bundle;
import java.text.SimpleDateFormat;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.acquiscent.myapplication.Controller.AppController;
import com.acquiscent.myapplication.database.DbConstants;
import com.acquiscent.myapplication.database.DbOperation;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Duke on 9/7/2015.
 */
public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;
    Cryptography_Android cryptography;
    ConnectivityCheck mTestConnectivity;
    String  lat,longs,driverid,vehicalSpeed,totalStopTime,deviceId,ot;

    SharedPreferences sharedpreferencesId;
    SharedPreferences.Editor editorId;
    public static final String MyPREFERENCEId = "preferenceId";

    SharedPreferences sharedpreferencesLatLong;
    SharedPreferences.Editor editorLatLong;
    public static final String MyPREFERENCELatLong = "preferenceServeice";

    static String _cipherKey = "P@SSW@RD@09";
    String encriptUserpass = "";
    String encriptString = "";
   // String url = "http://fms.terabhyte.com/FleetServices.asmx/After_Journey_Information";
    String url1 = "https://fms.terabhyte.com/FleetServices.asmx/SendMail";
    private static String TAG = GPSTracker.class.getSimpleName();

    boolean isGPSEnabled = false;
    boolean isSynced = false;
    GPSTracker gps;

    double latService;
    double longService;
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    Location location;
    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    protected LocationManager locationManager;
    IBinder mIBinder = new GPSTracker.LocalBinder();
    private Timer timer;
    DbOperation dbOperation;
    private String journeyid1, begin_kmstr, end_kmstr, end_timestr, end_datestr, cancle_timestr, cancle_datestr, commentstr, BookedBy, output, mOutput;
    private String driverIDL;
    private String journeyIDLL;
    private String SendDriver;
    private String journeyid2;


    private class LocalBinder extends Binder {
        GPSTracker getService() {
            return GPSTracker.this;
        }
    }

    public GPSTracker() {
        mContext = null;
    }

    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
    }


    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);


            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);


            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                this.canGetLocation = true;

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    //    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        if (ContextCompat.checkSelfPermission(this,
                                android.Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }

                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


    public void stopUsingGPS() {
        if (locationManager != null) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.removeUpdates(GPSTracker.this);
            }
        }

    }


    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
            Log.d("GpsTrackrLat", String.valueOf(latitude));
        }
        // return latitude
        return latitude;
    }


    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
            Log.d("GpsTrackrLong", String.valueOf(latitude));
        }
        return longitude;
    }


    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("Warning");
        alertDialog.setMessage("Please enabled GPS from settings");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }



    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mIBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //dbOperation = new DbOperation(GPSTracker.this);
        Context ctx = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        sharedpreferencesLatLong = getSharedPreferences(MyPREFERENCELatLong, Context.MODE_PRIVATE);

        editorLatLong = sharedpreferencesLatLong.edit();
        editorLatLong.putString("driverid", driverid);
        editorLatLong.commit();

        String latINC = sharedpreferencesLatLong.getString("LatIN","");
        Log.d("IDC",latINC);
        SendDriver = sharedpreferencesLatLong.getString("driverid", "");

        sharedpreferencesId = getSharedPreferences(MyPREFERENCEId, Context.MODE_PRIVATE);
        editorId = sharedpreferencesId.edit();
        gps = new GPSTracker(mContext);

        journeyid2 = sharedpreferencesId.getString("journeyid2", "");
        Log.d("journeyidINC",journeyid2);

      /*  String latINC = sharedpreferencesLatLong.getString("LatIN","");
        Log.d("latINS",latINC);*/

//        Log.d("CreateLat",String.valueOf(getLocation().getLatitude()));

       /* final Handler handler = new Handler();
        timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                end_timestr = sdf.format(new Date());
                                encriptString = "|||" + latService + "|||" + longService + "|||" + driverid + "|||" + journeyid1 + "|||" + vehicalSpeed + "|||" + totalStopTime + "|||" + deviceId + "|||" + end_timestr;
                             // Toast.makeText(get, "encriptString" +encriptString, Toast.LENGTH_LONG).show();

                                Cryptography_Android.Encrypt(encriptString, _cipherKey);
                               // System.out.print("encode data================================="+cryptography.Encrypt(encriptString,_cipherKey));
                                output = Cryptography_Android.Encrypt(encriptString, _cipherKey);
                                ot =  output.replaceAll("\n","");
                               // System.out.print("Maps"+ot);
                                //System.out.print("encriptStringOFMaps"+encriptString);
                                Log.i("MapsOfSendINService","" + ot);
                                Log.i("SendInService","" + encriptString);
                            Toast.makeText(getApplicationContext(),
                                  "encriptString"+encriptString,
                                    Toast.LENGTH_SHORT).show();

                            getData();

                        } catch (Exception e) {
                            Log.e("background", e.getMessage());

                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 2000);
        sendData();*/
    }

    private void sendData() {
        String SendDriver = sharedpreferencesLatLong.getString("driverid","");
        Log.d("SendDriver",SendDriver);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            end_timestr = sdf.format(new Date());
            encriptString = "|||" + Double.toString(getLocation().getLatitude()) + "|||" + Double.toString(getLocation().getLongitude()) + "|||" + driverid + "|||" + journeyid1 + "|||" + vehicalSpeed + "|||" + totalStopTime + "|||" + deviceId + "|||" + end_timestr;
            // Toast.makeText(get, "encriptString" +encriptString, Toast.LENGTH_LONG).show();

            Cryptography_Android.Encrypt(encriptString, _cipherKey);
            // System.out.print("encode data================================="+cryptography.Encrypt(encriptString,_cipherKey));
            output = Cryptography_Android.Encrypt(encriptString, _cipherKey);
            ot =  output.replaceAll("\n","");
            // System.out.print("Maps"+ot);
            //System.out.print("encriptStringOFMaps"+encriptString);
            Log.i("MapsOfSendINService","" + ot);
            Log.i("SendInService","" + encriptString);

                            Toast.makeText(getApplicationContext(),
                                  "encriptString"+encriptString,
                                    Toast.LENGTH_SHORT).show();
            //getData();

        } catch (Exception e) {
            Log.e("background", e.getMessage());
        }
    }


    private void sendToServer() {

        if (isNetworkAvailable()) {
            dbOperation.OPEN();
            Cursor cursor = dbOperation.getMyJournyData();
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {

                    System.out.println("@FM..journeyid1" + cursor.getString(cursor.getColumnIndex(DbConstants.journeyID)));
                    System.out.println("@FM..Begin_km" + cursor.getString(cursor.getColumnIndex(DbConstants.Begin_km)));
                    System.out.println("@FM..end_kmstr" + cursor.getString(cursor.getColumnIndex(DbConstants.End_km)));
                    System.out.println("@FM..end_timestr" + cursor.getString(cursor.getColumnIndex(DbConstants.End_Tim)));
                    System.out.println("@FM..end_datestr" + cursor.getString(cursor.getColumnIndex(DbConstants.End_date)));
                    System.out.println("@FM..cancle_timestr" + cursor.getString(cursor.getColumnIndex(DbConstants.Cancel_time)));
                    System.out.println("@FM..cancle_datestr" + cursor.getString(cursor.getColumnIndex(DbConstants.Cancel_date)));
                    System.out.println("@FM..commentstr" + cursor.getString(cursor.getColumnIndex(DbConstants.Comment)));
                    System.out.println("@FM..BookedBy" + cursor.getString(cursor.getColumnIndex(DbConstants.BookedBy)));
                    System.out.println("@FM..isSynced" + cursor.getString(cursor.getColumnIndex(DbConstants.isSynced)));

                    isSynced = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(DbConstants.isSynced)));
                    if (!isSynced) {
                        journeyid1 = cursor.getString(cursor.getColumnIndex(DbConstants.journeyID));
                        //Log.i("@FM.mLat", "" + mlat);
                        begin_kmstr = cursor.getString(cursor.getColumnIndex(DbConstants.Begin_km));
                        end_kmstr = cursor.getString(cursor.getColumnIndex(DbConstants.End_km));
                        end_timestr = cursor.getString(cursor.getColumnIndex(DbConstants.End_Tim));
                        end_datestr = cursor.getString(cursor.getColumnIndex(DbConstants.End_date));
                        cancle_timestr = cursor.getString(cursor.getColumnIndex(DbConstants.Cancel_time));
                        cancle_datestr = cursor.getString(cursor.getColumnIndex(DbConstants.Cancel_date));
                        commentstr = cursor.getString(cursor.getColumnIndex(DbConstants.Comment));
                        BookedBy = cursor.getString(cursor.getColumnIndex(DbConstants.BookedBy));
                        isSynced = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(DbConstants.isSynced)));
                        //Log.i("@FM.m", "" + mtotalStopTime);
                        // mdeviceId = cursor.getString(cursor.getColumnIndex(DbConstants.DeviceID));
                        try {

                            encriptUserpass = journeyid1 + "|||" + begin_kmstr + "|||" + end_kmstr + "|||" + end_timestr + "|||" + end_datestr + "|||" + cancle_timestr + "|||" + cancle_datestr + "|||" + commentstr + "|||" + BookedBy;
                            System.out.print("cocatination of Journy string--------" + encriptUserpass);
                            cryptography.Encrypt(encriptUserpass, _cipherKey);
                            System.out.print("Jencode data=================================" + cryptography.Encrypt(encriptUserpass, _cipherKey));
                            Log.i("jrnycoded data", "" + cryptography.Encrypt(encriptUserpass, _cipherKey));
                            output = cryptography.Encrypt(encriptUserpass, _cipherKey);
                            mOutput = output.replaceAll("\n", "");
                            System.out.print("output journy========" + mOutput);
                            Log.i("jrnycoded data", "" + mOutput);

                            //getData();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
                while (cursor.moveToNext()) ;
            }

            dbOperation.CLOSE();
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

      //  Log.d("CommaandLat",String.valueOf(getLocation().getLatitude()));
        if(intent != null)
            {
                  /*lat = intent.getStringExtra("lat");
                  longs = intent.getStringExtra("longs");
                  driverid = intent.getStringExtra("driverid");
                  journeyid1 = intent.getStringExtra("journeyid1");
                  vehicalSpeed = intent.getStringExtra("vehicalSpeed");
                  totalStopTime = intent.getStringExtra("totalStopTime");
                  deviceId = intent.getStringExtra("deviceId");

                editorLatLong.putString("driverid", driverid);
                editorLatLong.putString("journeyid1", journeyid1);
                editorLatLong.putString("vehicalSpeed", vehicalSpeed);
                editorLatLong.putString("totalStopTime", totalStopTime);
                editorLatLong.commit();*/

              /*  String latINS = sharedpreferencesLatLong.getString("driverid","");
                Log.d("latINS",String.valueOf(location.getLongitude()));*/
               // sendData();
             /*   Toast.makeText(getApplicationContext(),
                        "ServiceIntent",
                        Toast.LENGTH_SHORT).show();*/

                    final Handler handler = new Handler();
                    timer = new Timer();
                 TimerTask doAsynchronousTask = new TimerTask() {
                 @Override
                public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                             /*   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                end_timestr = sdf.format(new Date());
                                encriptString = "|||" + gps.getLatitude() + "|||" + gps.getLongitude() ;
                             // Toast.makeText(get, "encriptString" +encriptString, Toast.LENGTH_LONG).show();

                                Cryptography_Android.Encrypt(encriptString, _cipherKey);
                               // System.out.print("encode data================================="+cryptography.Encrypt(encriptString,_cipherKey));
                                output = Cryptography_Android.Encrypt(encriptString, _cipherKey);
                                ot =  output.replaceAll("\n","");

                                Toast.makeText(getApplicationContext(),
                                    "encriptString"+encriptString,
                                    Toast.LENGTH_SHORT).show();
                               // System.out.print("Maps"+ot);
                                //System.out.print("encriptStringOFMaps"+encriptString);
                                Log.i("MapsOfSendINService","" + ot);
                                Log.i("SendInService","" + encriptString);
*/
                                //getData();

                            Toast.makeText(getApplicationContext(),
                                    "data",
                                    Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            Log.e("background", e.getMessage());
                        }
                    }
                });
            }
        };
            timer.schedule(doAsynchronousTask, 0, 55000);


            }

        return START_STICKY;
    }

    @Override
    public void onLocationChanged(Location location) {

        latService = location.getLatitude();
        longService = location.getLongitude();
        Log.d("ServiceLat", String.valueOf(latService));
        //journeyid2 = sharedpreferencesId.getString("journeyid2", "");
        // Log.d("DriverLocation",journeyid2);

//        editorLatLong.putString("LatIN", String.valueOf(latService));
        // editorLatLong.putString("LongIN", journeyid1);
        final Handler handler = new Handler();
        timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            end_timestr = sdf.format(new Date());
                            encriptString = "|||" + latService + "|||" + longService ;
                            // Toast.makeText(get, "encriptString" +encriptString, Toast.LENGTH_LONG).show();

                            Cryptography_Android.Encrypt(encriptString, _cipherKey);
                            // System.out.print("encode data================================="+cryptography.Encrypt(encriptString,_cipherKey));
                            output = Cryptography_Android.Encrypt(encriptString, _cipherKey);
                            ot =  output.replaceAll("\n","");

                            // System.out.print("Maps"+ot);
                            //System.out.print("encriptStringOFMaps"+encriptString);
                            Log.i("MapsOfSendINService","" + ot);
                            Log.i("SendInService","" + encriptString);
                       /*     Toast.makeText(getApplicationContext(),
                                    "encriptString"+encriptString,
                                    Toast.LENGTH_SHORT).show();*/

                                //getData();

                        } catch (Exception e) {
                            Log.e("background", e.getMessage());

                        }
                    }
                });
            }
        };

        timer.schedule(doAsynchronousTask, 0, 2000);
//        Toast.makeText(getApplicationContext(), "My Location is \n" + latService + "\n" + longService, Toast.LENGTH_SHORT).show();
        getLocation();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            timer.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        stopSelf();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void getData() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // response
                try {
                    Toast.makeText(getApplicationContext(),
                            "Service",
                            Toast.LENGTH_SHORT).show();
                Log.i("@Loc ResponseMaps----", response);
                  JSONObject  obj = new JSONObject(response);

                } catch (JSONException e)

                {
                    if (e.toString().equalsIgnoreCase("NoConnectionError"))
                        //GlobalUtils.show_ToastCenter("Please check network", context);
                        //Toast.makeText(MapsActivity.this, "Please check network", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        //Log.d("Error.Response", error.getMessage());
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                /*params.put("locationString", ot);
                System.out.print("deviceId=============" + ot);*/
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(postRequest);
    }

    private void deleteItems() {
        dbOperation.OPEN();
        dbOperation.deleteSyncStatus();
    /*    Cursor cursor = dbOperation.getMyJournyData();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

            } while (cursor.moveToNext());
        }
*/
    }

}
