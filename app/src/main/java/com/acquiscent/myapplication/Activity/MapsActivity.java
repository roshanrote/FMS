package com.acquiscent.myapplication.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.DateFormat;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.acquiscent.myapplication.Adapter.MyRecyclerViewAdapter;
import com.acquiscent.myapplication.Controller.AppController;
import com.acquiscent.myapplication.Model.Data;
import com.acquiscent.myapplication.Model.LoginResponse;
import com.acquiscent.myapplication.R;
import com.acquiscent.myapplication.database.DbConstants;
import com.acquiscent.myapplication.database.DbOperation;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.widget.Toast.LENGTH_SHORT;
import static com.google.android.gms.location.LocationServices.*;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,  LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    //ConnectivityCheck mTestConnectivity;
    Chronometer simpleChronometer;
    String lat_currentstr1, long_currentstr1, end_timestr, end_datestr, prevlat, prevLong;
    Location mLastLocation1;
    LocationListener li;
    double speedNew;
    Location mLastLocation;
    Boolean value = true;
    LocationManager locationManager;
    String responseType = "";
    String result = "";


    double distance_travelled, distance;
    double latitude_prev = 0;
    double longitude_prev = 0;
    double total_dist = 0;
    double dist = 0;

    String now;
    String locationID = "";
    private SensorManager sensorManager;
    private Sensor accelorimetre;
    private GoogleMap mMap;
    double startTime = 0.0, endTime = 0.0, idleTime = 0;
    private static final double MIN_TIME = 60000000000.0;
    ArrayList<LatLng> MarkerPoints;
    CameraUpdate cup;
    public static final String TAG = "MAP DEMO";
    PolylineOptions lineOptions = null;
    private TextView speed1;
    private int mHour, mMinute;
    JSONObject obj;


    GoogleApiClient mGoogleApiClient;
    Marker mCurrLocationMarker, mLastLocationmarker;
    LocationRequest mLocationRequest;

    private ArrayList<LatLng> points; //added
    Polyline line; //added
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static final String MyPREFERENCE = "preference";

    SharedPreferences sharedpreferencesId;
    SharedPreferences.Editor editorId;
    public static final String MyPREFERENCEId = "preferenceId";

    SharedPreferences sharedpreferencesLatLong;
    SharedPreferences.Editor editorLatLong;
    public static final String MyPREFERENCELatLong = "preferenceLatLong";

    Cryptography_Android cryptography;
    static String _cipherKey = "P@SSW@RD@09";
    // String url2 = "http://fms.terabhyte.com/_trackingService.asmx/SaveNewVechileLocation";
    String url2 = "https://fms.terabhyte.com/_trackingService.asmx/SaveNewVechileLocation";
    String url = "https://fms.terabhyte.com/FleetServices.asmx/JourneystatusPaused";
    String login_url = "https://fms.terabhyte.com/FleetServices.asmx/IsLogin";

    String url1 = "http://fms.acquiscent.com/_trackingService.asmx/IdleTime";
    String output, outputPause, ot, moutput, mot, outputTrack;
    String vehicalSpeed, mvehicalSpeed;
    JSONObject object;
    String encriptString = "", pencriptString = "", journeyid1, mjourneyid1, deviceId, mdeviceId, driverid, mdriverid, mend_timestr, mend_datestr;
    String idealTimeS, journeyid, BookedBy, BookingRef;
    String lat;
    String longs, BeginKm;
    String mlat;
    String mlongs;

    Button end, cancal, pause, speed;
    private Timer timer;
    Handler handler;
    Runnable runnable;
    private static String TAG1 = MapsActivity.class.getSimpleName();
    public static final float DEFAULT_ZOOM_LEVEL = 9.0f;
    String lat_currentstr, long_currentstr;
    GPSTracker gps;

    float zoomLevel = 19; //This goes up to 21
    int nCounter = 0;
    TimerTask doAsynchronousTask;

    //private static final String TAG = "MainActivity";
    private static final long INTERVAL = 1000 * 60 * 1; //1 minute
    private static final long FASTEST_INTERVAL = 1000 * 60 * 1; // 1 minute
    private static final float SMALLEST_DISPLACEMENT = 0.25F; //quarter of a meter
    private double startOfSpeedZero = 0.0;
    static double totalStopTime = 0.0;
    static double mtotalStopTime = 0.0;

    DbOperation dbOperation;
    private double latitudeFromSer;
    private double longitudeFromSer;
    private String speedAsKmHr, Actualbeginkm;
    private String slat, sLong, nlat, nlong;
    private String isSend;
    private ArrayList<Data> list;
    private ArrayList<String> listB;
    private String provider;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setSmallestDisplacement(SMALLEST_DISPLACEMENT); //added
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    public void onBackPressed() {
        String value = "false";
        if (value.equals("false")) {
            Toast.makeText(MapsActivity.this, "Please End The Journey", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        // Getting the name of the provider that meets the criteria
        provider = locationManager.getBestProvider(criteria, false);
        sharedpreferences = getSharedPreferences(MyPREFERENCE, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        outputTrack = sharedpreferences.getString("output", "");

        System.out.print("journey id-----------" + journeyid);

        sharedpreferencesLatLong = getSharedPreferences(MyPREFERENCELatLong, Context.MODE_PRIVATE);
        editorLatLong = sharedpreferencesLatLong.edit();
        dbOperation = new DbOperation(MapsActivity.this);

        Intent intent = getIntent();
        BeginKm = intent.getStringExtra("beginkm");
       // BeginKm = sharedpreferencesId.getString("BeginKm", "");
        Actualbeginkm = intent.getStringExtra("Actualbeginkm");
      //  Actualbeginkm = sharedpreferencesId.getString("ActualBeginKm", "");
        sharedpreferencesId = getSharedPreferences(MyPREFERENCEId, Context.MODE_PRIVATE);
        editorId = sharedpreferencesId.edit();

        journeyid = sharedpreferencesId.getString("journeyid", "").toString();
        journeyid1 = sharedpreferencesId.getString("journeyid2", "");
        deviceId = sharedpreferencesId.getString("deviceId", "");
        BookedBy = sharedpreferencesId.getString("BookedBy", "").toString();
        driverid = sharedpreferencesId.getString("driverID", "");
        BookingRef = sharedpreferencesId.getString("BookingRef", "");
        // BeginKm = sharedpreferencesId.getString("beginkm", "");
        //BeginKm = ;
          speed1 = (TextView) findViewById(R.id.speed);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Initializing
        MarkerPoints = new ArrayList<>();
        points = new ArrayList<LatLng>(); //added

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        gps = new GPSTracker(MapsActivity.this);
        //startService(new Intent(MapsActivity.this, GPSTracker.class));
        //mTestConnectivity = new ConnectivityCheck(this);

        // check if GPS enabled
        if (gps.canGetLocation()) {
            latitudeFromSer = gps.getLatitude();
            longitudeFromSer = gps.getLongitude();
            Log.d("latIN", String.valueOf(latitudeFromSer));
            // \n is for new line
            //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();

        }

        if (provider != null && !provider.equals("")) {

            // Get the location from the given provider
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
         //   Location location = locationManager.getLastKnownLocation(provider);

            //locationManager.requestLocationUpdates(provider, 20000, 1, this);
/*

            if(location!=null)
                onLocationChanged(location);
            else
                Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();
*/

        }else{

            Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();

        }

       /* locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        li = new speed();*/
      /*
        mHour = c1.get(Calendar.HOUR_OF_DAY);
        mMinute = c1.get(Calendar.MINUTE);*/

        DateFormat df = new SimpleDateFormat("HH:mm"); //format time
        //end_timestr = df.format(Calendar.getInstance().getTime());

        DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy");//foramt date
        end_datestr = df1.format(Calendar.getInstance().getTime());
        end = (Button) findViewById(R.id.end);
        cancal = (Button) findViewById(R.id.cancel);
        pause = (Button) findViewById(R.id.pause);
        // speed =(Button) findViewById(R.id.speed);


        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog openDialog = new Dialog(MapsActivity.this);
              //  openDialog.setContentView(R.layout.custom_dailog_cancel);
                openDialog.setContentView(R.layout.custom_dailog_cancel);

                TextView dialogTextContent = (TextView) openDialog.findViewById(R.id.text);
                dialogTextContent.setText("Do you want End Journey?");
                Button YesButton = (Button) openDialog.findViewById(R.id.yes);
                Button NoButton = (Button) openDialog.findViewById(R.id.no);

                YesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mMap.clear();
                        sharedpreferencesLatLong = getSharedPreferences(MyPREFERENCELatLong, Context.MODE_PRIVATE);
                        editorLatLong = sharedpreferencesLatLong.edit();
                        editorLatLong.clear().apply();
//                        stopService(new Intent(MapsActivity.this, BackgroundService.class));
                        //stopTimerTask();

                        stopLocationUpdates();
                        // timer.cancel();
//                        timer.purge();
                        // doAsynchronousTask.cancel();
                        Intent i = new Intent(MapsActivity.this, JourneyDetails.class);
                        i.putExtra("beginkm", BeginKm);
                        i.putExtra("Actualbeginkm", Actualbeginkm);
                        i.putExtra("totalDistance", sharedpreferencesId.getString("distance_travelled", ""));
                        startActivity(i);
                        finish();
                        openDialog.dismiss();

                    }
                });

                NoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        openDialog.dismiss();

                    }
                });

                openDialog.show();
            }
        });

        cancal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog openDialog = new Dialog(MapsActivity.this);
                openDialog.setContentView(R.layout.custom_dailog_cancel);
                TextView dialogTextContent = (TextView) openDialog.findViewById(R.id.text);
                Button YesButton = (Button) openDialog.findViewById(R.id.yes);
                Button NoButton = (Button) openDialog.findViewById(R.id.no);

                YesButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        mMap.clear();
                        sharedpreferencesLatLong = getSharedPreferences(MyPREFERENCELatLong, Context.MODE_PRIVATE);
                        editorLatLong = sharedpreferencesLatLong.edit();
                        editorLatLong.clear().commit();
//                        stopService(new Intent(MapsActivity.this, BackgroundService.class));
                        //stopTimerTask();
                        stopLocationUpdates();
                        dbOperation.createTable();
                        dbOperation.CLOSE();
                        //  timer.cancel();
//                        timer.purge();
                        Intent i = new Intent(MapsActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        openDialog.dismiss();

                    }
                });

                NoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openDialog.dismiss();

                    }
                });

                openDialog.show();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog openDialog = new Dialog(MapsActivity.this);
                openDialog.setContentView(R.layout.custom_dilog_paused);
                TextView dialogTextContent = (TextView) openDialog.findViewById(R.id.text);
                Button YesButton = (Button) openDialog.findViewById(R.id.yes);
                Button NoButton = (Button) openDialog.findViewById(R.id.no);

                YesButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        mMap.clear();
                        try {

                            pencriptString = journeyid1 + "|||" + "true";
                            System.out.print("cocatination of string--------" + pencriptString);
                            cryptography.Encrypt(pencriptString, _cipherKey);
                            System.out.print("encode data=================================" + cryptography.Encrypt(encriptString, _cipherKey));
                            outputPause = cryptography.Encrypt(pencriptString, _cipherKey);
                            System.out.print("outputJourny==================================" + output);

                            dbOperation.OPEN();
                            dbOperation.addPauseOfflineData(journeyid1,"true");

                            pauseJourney();

                        } catch (Exception e) {

                            e.printStackTrace();

                        }
                        stopLocationUpdates();
                        openDialog.dismiss();
                    }
                });

                NoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openDialog.dismiss();

                    }
                });
                openDialog.show();
            }
        });

       /* final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        final LocationListener locationListener = new LocationListener() {

            public void onLocationChanged(Location location) {
                location.getLatitude();
                Toast.makeText(MapsActivity.this, "Current speed:" + location.getSpeed(), Toast.LENGTH_SHORT).show();
            }
            public void onStatusChanged(String provider, int status, Bundle extras)
            { }
            public void onProviderEnabled(String provider)
            { }
            public void onProviderDisabled(String provider)
            { }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        };*/


        class speed implements android.location.LocationListener {

            @Override
            public void onLocationChanged(Location loc) {
                Float thespeed = loc.getSpeed();
                Toast.makeText(MapsActivity.this, String.valueOf(thespeed), Toast.LENGTH_LONG).show();
                Log.i("Speed", "" + thespeed);

                // speed1.setText(String.valueOf(thespeed));
            }

            @Override
            public void onProviderDisabled(String arg0) {
            }

            @Override
            public void onProviderEnabled(String arg0) {

            }

            @Override
            public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

            }

        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        StartService();

    }


    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }

    }

    private void pauseJourney() {

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // response
                Log.d("Response", response);

                Intent i = new Intent(MapsActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("stringBookingDetails", outputPause);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(postRequest);

    }

    private void getLoaction() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //Place current location marker
                final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                editorLatLong.putString("latitude", String.valueOf(location.getLatitude()));
                editorLatLong.putString("longitude", String.valueOf(location.getLongitude()));
                editorLatLong.commit();

                lat_currentstr = sharedpreferencesLatLong.getString("latitude", "");
                long_currentstr = sharedpreferencesLatLong.getString("longitude", "");
                System.out.print("latitude------mapsactivity-----" + lat_currentstr);

                long result1 = 0;
                dbOperation.OPEN();
                String Slat = String.valueOf(location.getLatitude());
                String Slong = String.valueOf(location.getLongitude());
                result1 = dbOperation.addSlatlongData(Slat, Slong, "false");
                Log.i("SLatlongrslt", "" + String.valueOf(result1));
                if (result1 > 0) Log.i("Data not inserted", "");
                else Log.i("Data is inserted", "");
                dbOperation.CLOSE();

                double latitude = Double.parseDouble(lat_currentstr);
                double longitude = Double.parseDouble(long_currentstr);

                //Place current location marker
                final LatLng latLng_current = new LatLng(latitude, longitude);

                if (MarkerPoints.size() > 1) {
                    MarkerPoints.clear();
                    mMap.clear();
                }

                MarkerPoints.add(latLng_current);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng_current);
                markerOptions.title("Current Position");
                mCurrLocationMarker = mMap.addMarker(markerOptions);

        final LatLng latLngdestination = new LatLng(Double.parseDouble(getSharedPreferences(MyPREFERENCEId, MODE_PRIVATE).getString("Latitude", "")),
                Double.parseDouble(getSharedPreferences(MyPREFERENCEId, MODE_PRIVATE).getString("Longitude", "")));


        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLngdestination);
        markerOption.title("Destination Position");
        mLastLocationmarker = mMap.addMarker(markerOption);
        MarkerPoints.add(latLngdestination);

        if (MarkerPoints.size() == 1) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        } else if (MarkerPoints.size() == 2) {

            markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

        }

        mMap.getUiSettings().setMapToolbarEnabled(true);
        // mMap.animateCamera(CameraUpdateFactory.zoomTo(5));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng_current));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngdestination));
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng_current,10));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngdestination, 10));
        mMap.isTrafficEnabled();

        // Checks, whether start and end locations are captured
        if (MarkerPoints.size() >= 2) {
            LatLng origin = MarkerPoints.get(0);
            LatLng dest = MarkerPoints.get(1);

            // Getting URL to the Google Directions API
            String url = getUrl(origin, dest);
            Log.d("onMapClick", url.toString());
            FetchUrl FetchUrl = new FetchUrl();

            // Start downloading json data from Google Directions API
            FetchUrl.execute(url);
            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }

        mMap.addMarker(markerOption);
                //Toast.makeText(MapsActivity.this, "Current speed:" + location.hasSpeed(), Toast.LENGTH_SHORT).show();
                // Toast.makeText(MapsActivity.this, "Current speed:" + location.getSpeed(), Toast.LENGTH_SHORT).show();
        Log.i("Speed",""+location.hasSpeed());
        Log.i("@Location System time","------------------------------------------------");
        Log.i("@Location Speed1",""+location.getSpeed());
        Log.i("@Location Elpstime",""+location.getElapsedRealtimeNanos());
        Log.i("@Location LocatigetTime",""+location.getTime());
        Log.i("@Location System time",""+System.currentTimeMillis());
        Log.i("@Location System time",""+new Date().getTime());

                // speed1.setText(String.valueOf(location.getSpeed()));
                vehicalSpeed = String.valueOf((location.getSpeed() * 3600) / 1000);

                String s = "False";
                if (location.getSpeed() == 0.0) {
                    s = "True";
                } else {
                    s = "False";
                }
                Log.i("@Location 1", "location.getSpeed() == 0.0 : " + s);

                String ss = "False";
                if (startOfSpeedZero == 0.0) {
                    ss = "True";
                } else {
                    ss = "False";
                }
                Log.i("@Location 1.1", "startOfSpeedZero == 0.0 : " + ss);


                String sss = "False";
                if (startOfSpeedZero != 0.0) {
                    sss = "True";
                } else {
                    sss = "False";
                }
                Log.i("@Location 1.1", "startOfSpeedZero != 0.0 : " + sss);

                if (location.getSpeed() == 0.0 && startOfSpeedZero == 0.0) {

                    startOfSpeedZero = location.getTime();
                    Log.i("@Location starSpeedZero", "" + startOfSpeedZero);
                } else if (startOfSpeedZero != 0.0 && location.getSpeed() != 0.0) {
                    double diffInTime = location.getTime() - startOfSpeedZero;
                    diffInTime = diffInTime / (60 * 1000);
                    Log.i("@Location diffInTime", "" + diffInTime);

                    startOfSpeedZero = 0;
                    if (diffInTime >= 15) {
                        totalStopTime = +diffInTime;
                        idealTimeS = String.valueOf(totalStopTime);
                        Log.i("@Location idealTimeS", "" + idealTimeS);
                        //Toast.makeText(getApplicationContext(), "15 Min Stope >= " + diffInTime, Toast.LENGTH_LONG).show();
                    }

                } else {
                    if (startOfSpeedZero != 0.0) {
                        double diffInTime = location.getTime() - startOfSpeedZero;
                        diffInTime = diffInTime / (60 * 1000);
                        //  diffInTime = TimeUnit.MILLISECONDS.toMinutes(diffInTime);
                        Log.i("@Location diffInTime", "" + diffInTime);
                        if (diffInTime >= 15) {
                            totalStopTime = +diffInTime;
                            idealTimeS = String.valueOf(totalStopTime);
                            Log.i("@Location totalStopTime", "" + diffInTime);

                            // Toast.makeText(getApplicationContext(), "15 Min Stope >= " + diffInTime, Toast.LENGTH_LONG).show();
                        }
                    }
                }

                Log.i("@Location System time", "------------------------------------------------");

                double lat2 = location.getLatitude();
                double lon2 = location.getLongitude();

                //  Toast.makeText(MapsActivity.this, "lat2 :" + lat2, Toast.LENGTH_SHORT).show();
                //Toast.makeText(MapsActivity.this, "lon2:" +  lon2, Toast.LENGTH_SHORT).show();

                Log.v("The current Latitude", String.valueOf(lat_currentstr));
                Log.v("The current Longitude", String.valueOf(long_currentstr));

                Log.v("The previous Latitude", String.valueOf(latitude_prev));
                Log.v("The previous Longitude", String.valueOf(longitude_prev));

                if (latitude_prev == 0 && longitude_prev == 0) {
                    latitude_prev = lat2;
                    longitude_prev = lon2;
                    distance_travelled = 0;

                } else {
                   /* get the distance covered from point A to point B*/

                    dbOperation.OPEN();
                    Cursor cursor = dbOperation.getSlatlongData();
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();

                        System.out.println("@FM..Slatitude" + cursor.getString(cursor.getColumnIndex(DbConstants.Slatitude)));
                        System.out.println("@FM..Slongitude" + cursor.getString(cursor.getColumnIndex(DbConstants.Slongitudes)));

                        Log.i("SLAt", cursor.getString(cursor.getColumnIndex(DbConstants.Slatitude)));
                        Log.i("SLOng", cursor.getString(cursor.getColumnIndex(DbConstants.Slongitudes)));
                        slat = cursor.getString(cursor.getColumnIndex(DbConstants.Slatitude));
                        sLong = cursor.getString(cursor.getColumnIndex(DbConstants.Slongitudes));

                    }

                    dbOperation.CLOSE();

            Location startPoint=new Location("");
            startPoint.setLatitude(Double.valueOf(slat));
            startPoint.setLongitude(Double.valueOf(slat));

            Location endPoint=new Location("");
            endPoint.setLatitude(Double.valueOf(slat));
            endPoint.setLongitude(Double.valueOf(slat));

            double distance=startPoint.distanceTo(endPoint);

                    distance_travelled = distanceTravelled(Double.valueOf(slat), Double.valueOf(sLong), lat2, lon2);
                    getSharedPreferences("preferenceId", MODE_PRIVATE).edit().putString("distance_travelled", String.valueOf(distance_travelled)).apply();

                    //  distance_travelled = distanceTravelled(Double.valueOf(lat_currentstr), Double.valueOf(long_currentstr), lat2, lon2);
                    Log.d("distance_travelled", String.valueOf(distance_travelled));
                /*    set previous latitude and longitude to the last location*/
                    // Toast.makeText(MapsActivity.class,+ distance_travelled,Toast.LENGTH_SHORT).show();
                    latitude_prev = lat2;
                    longitude_prev = lon2;

                    if ((String.format("%.2f", ((location.getSpeed() * 3600) / 1000))).equals("null")) {
                        speedAsKmHr = "0";
                    } else {
                        speedAsKmHr = String.format("%.2f", ((location.getSpeed() * 3600) / 1000));
                    }
                    //speedNew = Float.valueOf((location.getSpeed()*3600)/1000);
                    DecimalFormat df = new DecimalFormat("##.##");
                    speedNew = Double.valueOf(df.format(speedNew));
//             Double value   =  Double.parseDouble(new DecimalFormat("##.##").format(vehicalSpeed));
                    speed1.setText(speedAsKmHr + "km/hr");
                }

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng point) {

                        double latitude = Double.parseDouble(lat_currentstr);
                        double longitude = Double.parseDouble(long_currentstr);

                        //Place current location marker
                        final LatLng latLng_current = new LatLng(latitude, longitude);

                        if (MarkerPoints.size() > 1) {
                            MarkerPoints.clear();
                            mMap.clear();
                        }

                        MarkerPoints.add(latLng_current);
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng_current);
                        markerOptions.title("Current Position");

                        mCurrLocationMarker = mMap.addMarker(markerOptions);

                        final LatLng latLngdestination = new LatLng(Double.parseDouble(getSharedPreferences(MyPREFERENCEId, MODE_PRIVATE).getString("Latitude", "")), Double.parseDouble(getSharedPreferences(MyPREFERENCEId, MODE_PRIVATE).getString("Longitude", "")));

                        MarkerOptions markerOption = new MarkerOptions();
                        markerOption.position(latLngdestination);
                        markerOption.title("Destination Position");
                        mLastLocationmarker = mMap.addMarker(markerOption);
                        MarkerPoints.add(latLngdestination);

                        if (MarkerPoints.size() == 1) {
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                        } else if (MarkerPoints.size() == 2) {
                            markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                        }

                        mMap.getUiSettings().setMapToolbarEnabled(true);
                        // mMap.animateCamera(CameraUpdateFactory.zoomTo(5));
                        mMap.getUiSettings().setZoomControlsEnabled(true);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng_current));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngdestination));
                        mMap.getUiSettings().setScrollGesturesEnabled(true);
                        mMap.getUiSettings().setCompassEnabled(true);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng_current, 10));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngdestination, 10));
                        mMap.isTrafficEnabled();

                        // Checks, whether start and end locations are captured
                        if (MarkerPoints.size() >= 2) {
                            LatLng origin = MarkerPoints.get(0);
                            LatLng dest = MarkerPoints.get(1);

                            // Getting URL to the Google Directions API
                            String url = getUrl(origin, dest);
                            Log.d("onMapClick", url.toString());
                            FetchUrl FetchUrl = new FetchUrl();
                            // Start downloading json data from Google Directions API
                            FetchUrl.execute(url);
                            //move map camera
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                        }
                        mMap.addMarker(markerOption);

                    }
                });

                if (location != null) {
                    try {

                        dbOperation.OPEN();
                        Cursor cursor = dbOperation.getnewlatlongData();
                        if (cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            StringBuffer buffer = new StringBuffer();

                            while (cursor.moveToNext()){
                                nlat = cursor.getString(cursor.getColumnIndex(DbConstants.Slatitude));
                                nlong = cursor.getString(cursor.getColumnIndex(DbConstants.Slongitudes));
                                isSend = cursor.getString(cursor.getColumnIndex(DbConstants.isSend));
                                buffer.append("Id :" + cursor.getString(0) + "\n");
                                buffer.append("latData :" + cursor.getString(cursor.getColumnIndex(DbConstants.Slatitude)) + "\n");
                                buffer.append("longData :" + cursor.getString(cursor.getColumnIndex(DbConstants.Slongitudes)) + "\n");
                                buffer.append("send :" + cursor.getString(cursor.getColumnIndex(DbConstants.isSend)) + "\n");
                                // showMessage("data", buffer.toString());
                                if (isSend.equals("false")) {
                                    final Calendar c1 = Calendar.getInstance();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    end_timestr = sdf.format(new Date());
                                    //String dateTime = end_timestr + "|" + end_datestr;
                                    lat = String.valueOf(location.getLatitude());
                                    // double i2= location.getLatitude() /60000;

                                    //   double lat = location.getLatitude();
                                    DecimalFormat df = new DecimalFormat("##.##");
                                    // lat = Double.valueOf(df.format(lat));

                                    longs = String.valueOf(location.getLongitude());
                                    encriptString = "|||" + nlat + "|||" + nlong + "|||" + driverid + "|||" + journeyid1 + "|||" + speedAsKmHr + "|||" + totalStopTime + "|||" + deviceId + "|||" + end_timestr;
                                    System.out.print("latitude--------" + location.getLatitude());
                                    System.out.print("longitude--------" + location.getLongitude());
                                    Log.i("driverid", "" + driverid);
                                    Log.i("deviceId", "" + deviceId);
                                    //System.out.print("cocatination Map of string--------"+encriptString);
                                    //Toast.makeText(MapsActivity.this, "encriptString" +encriptString, Toast.LENGTH_LONG).show();

                                    Cryptography_Android.Encrypt(encriptString, _cipherKey);
                                    // System.out.print("encode data================================="+cryptography.Encrypt(encriptString,_cipherKey));
                                    output = Cryptography_Android.Encrypt(encriptString, _cipherKey);
                                    ot = output.replaceAll("\n", "");
                                    // System.out.print("Maps"+ot);
                                    //System.out.print("encriptStringOFMaps"+encriptString);
                                    Log.i("MapsOfSend", "" + ot);
                                    Log.i("Send", "" + encriptString);

                                    dbOperation.OPEN();
                                    dbOperation.addLatLongOfflineData(journeyid1,ot,"0");
                                    dbOperation.CLOSE();

                                    long resultNew = 0;
                                    String Prevlat = String.valueOf(location.getLatitude());
                                    String Prevlong = String.valueOf(location.getLongitude());
                                    dbOperation.OPEN();
                                    resultNew = dbOperation.addPrevlatlongData(Prevlat, Prevlong);
                                    Log.i("PrevLatlongrslt", "" + String.valueOf(result1));
                                    if (resultNew > 0) Log.i("Data not inserted", "");
                                    else Log.i("Data is inserted", "");

                                    Cursor cursor1 = dbOperation.getPrevlatlongData();
                                    if (cursor1.getCount() > 0) {
                                        cursor1.moveToFirst();
                                        System.out.println("@FM..Prevlatitude" + cursor1.getString(cursor1.getColumnIndex(DbConstants.Prevlatitude)));
                                        System.out.println("@FM..Prevlongitude" + cursor1.getString(cursor1.getColumnIndex(DbConstants.Prevlongitudes)));

                                        //  Log.i("PrevLAt", cursor.getString(cursor.getColumnIndex(DbConstants.Prevlatitude)));
                                        //   Log.i("PrevLOng", cursor.getString(cursor.getColumnIndex(DbConstants.Prevlongitudes)));
                                        prevlat = cursor1.getString(cursor1.getColumnIndex(DbConstants.Prevlatitude));
                                        prevLong = cursor1.getString(cursor1.getColumnIndex(DbConstants.Prevlongitudes));
                                    }

                                    distance = distanceCoverred(Double.parseDouble(prevlat), Double.valueOf(prevLong), lat2, lon2);
                                    int meterConversion = 1000;
                                    Double newDist = distance * meterConversion;
                                    if (!nlat.equals(null) && !nlong.equals(null)) {

                                        if (value == true) {
                                            getData();
                                            value = false;
                                            dbOperation.updatelatlongData(cursor.getString(cursor.getColumnIndex(DbConstants.SId)));
                                        }

                                        else if (value == false) {

                                            if (newDist > 50 ) {
                                                getData();

                                                long Updateresult = 0;
                                                Updateresult = dbOperation.updatePrevlatlongData(String.valueOf(lat2), String.valueOf(lon2));

                                                if (Updateresult > 0) Log.i("Data not inserted", "");
                                                else Log.i("Data is inserted", "");
                                                dbOperation.updatelatlongData(cursor.getString(cursor.getColumnIndex(DbConstants.SId)));
                                            }
                                        }
                                    }
                                }

                            }

                        }

                        dbOperation.CLOSE();
                        checkTrackId();

                    } catch (Exception e)
                    {
                            // Toast.makeText(getApplicationContext(), "catch null pointer", Toast.LENGTH_SHORT).show();// (location.getSpeed()*3600)/1000;
                    }

                }

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {


            }

            @Override
            public void onProviderEnabled(String s) {


            }

            @Override
            public void onProviderDisabled(String s) {


            }

        });

    }

    private void checkTrackId() {

        retrofit2.Retrofit mRetrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://fms.terabhyte.com/FleetServices.asmx/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(initLog().build())
                .build();

        ServiceInterface mServiceApi = mRetrofit.create(ServiceInterface.class);
        retrofit2.Call<LoginResponse> mLoginCall = mServiceApi._login(outputTrack);
        mLoginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<LoginResponse> call, @NonNull retrofit2.Response<LoginResponse> response) {
                list = new ArrayList<>();
                Data listname = new Data();
                // String data = response.body();
              //  Toast.makeText(getApplicationContext(), response.body().toString(), Toast.LENGTH_SHORT).show();

                LoginResponse country1 = (LoginResponse) response.body();
                if (country1.getResponseType() != null) {
                    //   obj = new JSONObject(response.body());
                    responseType = country1.getResponseType();
                    //  responseType = obj.getString("responseType");
                    System.out.print("type+++++++++++++++" + responseType);
                    //result = obj.getString("response");
                    result = country1.getResponse();
                    System.out.print("result+++++++++++++++" + result);

                    if (responseType.equalsIgnoreCase("Success")) {
                        try {
                            object = new JSONObject(result);
                            //  System.out.print("result------------------" + userId);
                            JSONArray array = object.getJSONArray("BookingList");

            /*JSONObject userObject = object.getJSONObject("loginDetails");

            userId =journeyid String.valueOf(userObject.getInt("userId"));
            System.out.print("user info-------------------" + userId);*/

                            for (int i = 0; i < array.length(); i++) {

                                listname = new Data();
                                JSONObject objectjson = (JSONObject) array.get(i);
                                //  listname.setJourney_id(objectjson.getString("ID"));
                           /* listname.setVehicleType(objectjson.getString("VehicleType"));
                            listname.setBookDate(objectjson.getString("BookDate"));
                            listname.setBookedBy(objectjson.getString("BookedBy"));
                            listname.setPassenger(objectjson.getString("Passenger"));
                            Log.d("latandlonginfo", (objectjson.getString("Latitude") + objectjson.getString("Longitude")));
                            listname.setJourneyStartDate(objectjson.getString("JourneyStartDate"));
                            listname.setJourneyEndDate(objectjson.getString("JourneyEndDate"));
                            listname.setDestinationCode(objectjson.getString("DestinationCode"));
                            listname.setProposedEndTime(objectjson.getString("ProposedEndTime"));
                            listname.setLati(objectjson.getString("Latitude"));
                            listname.setLongi(objectjson.getString("Longitude"));
                            listname.setDeviceID(objectjson.getString("DeviceID"));
                            listname.setDriverID(objectjson.getString("DriverID"));*/
                                listname.setBookingRef(objectjson.getString("BookingRef"));
                                //listname.setBeginKm(objectjson.getString("BeginKm"));
                                list.add(listname);

                            }

                        } catch (JSONException e) {
                            if (e.toString().equalsIgnoreCase("NoConnectionError"))
                                //GlobalUtils.show_ToastCenter("Please check network", context);
                                Toast.makeText(MapsActivity.this, "Please check network", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    } else {
                        //  mprogressBar.setVisibility(View.GONE);
                      //  Toast.makeText(MapsActivity.this, "Enter correct username and password", Toast.LENGTH_SHORT).show();
                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {

            }
        });

        listB = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            listB.add(list.get(i).getBookingRef());
        }

        if(!listB.contains(BookingRef))
        {
            //Toast.makeText(MapsActivity.this, "Track end", Toast.LENGTH_SHORT).show();
            final Dialog openDialog = new Dialog(MapsActivity.this);
            openDialog.setContentView(R.layout.track_end_layout);

            TextView dialogTextContent = (TextView) openDialog.findViewById(R.id.text);
            dialogTextContent.setText("Journey Ended By Admin");
            Button YesButton = (Button) openDialog.findViewById(R.id.ok);
           // Button NoButton = (Button) openDialog.findViewById(R.id.no);

            YesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MapsActivity.this, MainActivity.class);
                  /*  i.putExtra("beginkm", BeginKm);
                    i.putExtra("Actualbeginkm", Actualbeginkm);
                    i.putExtra("totalDistance", sharedpreferencesId.getString("distance_travelled", ""));
*/
                    editor.putString("trackEnd", "trackEnd");
                    editor.commit();
                    i.putExtra("trackEnd", "trackEnd");
                    startActivity(i);
                    finish();
                    openDialog.dismiss();
                }

            });

            openDialog.show();

        }
    }

    private void StartService() {

        Intent intent = new Intent(this, GPSTracker.class);
        intent.putExtra("lat", latitudeFromSer);
        intent.putExtra("longs", long_currentstr);
        intent.putExtra("driverid", driverid);
        intent.putExtra("journeyid1", journeyid1);
        intent.putExtra("vehicalSpeed", "0");
        intent.putExtra("totalStopTime", totalStopTime);
        intent.putExtra("deviceId", deviceId);
        this.startService(intent);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                getLoaction();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            getLoaction();
            mMap.setMyLocationEnabled(true);
        }
    }


    private String getUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;

    }


    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }

    /**
     * A method to download json data from url
     */

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    private class FetchUrl extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";
            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }

    }


    @Override
    public void onLocationChanged(Location location) {

     /*   mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        editorLatLong.putString("latitude", String.valueOf(location.getLatitude()));
        editorLatLong.putString("longitude", String.valueOf(location.getLongitude()));
        editorLatLong.apply();
        editorLatLong.commit();

        lat_currentstr = sharedpreferencesLatLong.getString("latitude", "");
        long_currentstr = sharedpreferencesLatLong.getString("longitude", "");
        System.out.print("latitude------mapsactivity-----" + lat_currentstr);

        long result1 = 0;
        dbOperation.OPEN();
        String Slat = String.valueOf(location.getLatitude());
        String Slong = String.valueOf(location.getLongitude());
        result1 = dbOperation.addSlatlongData(Slat, Slong, "false");
        Log.i("SLatlongrslt", "" + String.valueOf(result1));
        if (result1 > 0) Log.i("Data not inserted", "");
        else Log.i("Data is inserted", "");
        dbOperation.CLOSE();

        double latitude = Double.parseDouble(lat_currentstr);
        double longitude = Double.parseDouble(long_currentstr);

        //Place current location marker
        final LatLng latLng_current = new LatLng(latitude, longitude);

        if (MarkerPoints.size() > 1) {
            MarkerPoints.clear();
            mMap.clear();
        }

        MarkerPoints.add(latLng_current);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng_current);
        markerOptions.title("Current Position");


        mCurrLocationMarker = mMap.addMarker(markerOptions);



        // speed1.setText(String.valueOf(location.getSpeed()));
        vehicalSpeed = String.valueOf((location.getSpeed() * 3600) / 1000);

        String s = "False";
        if (location.getSpeed() == 0.0) {
            s = "True";
        } else {
            s = "False";
        }
        Log.i("@Location 1", "location.getSpeed() == 0.0 : " + s);

        String ss = "False";
        if (startOfSpeedZero == 0.0) {
            ss = "True";
        } else {
            ss = "False";
        }
        Log.i("@Location 1.1", "startOfSpeedZero == 0.0 : " + ss);


        String sss = "False";
        if (startOfSpeedZero != 0.0) {
            sss = "True";
        } else {
            sss = "False";
        }
        Log.i("@Location 1.1", "startOfSpeedZero != 0.0 : " + sss);

        if (location.getSpeed() == 0.0 && startOfSpeedZero == 0.0) {

            startOfSpeedZero = location.getTime();
            Log.i("@Location starSpeedZero", "" + startOfSpeedZero);
        } else if (startOfSpeedZero != 0.0 && location.getSpeed() != 0.0) {
            double diffInTime = location.getTime() - startOfSpeedZero;
            diffInTime = diffInTime / (60 * 1000);
            Log.i("@Location diffInTime", "" + diffInTime);

            startOfSpeedZero = 0;
            if (diffInTime >= 15) {
                totalStopTime = +diffInTime;
                idealTimeS = String.valueOf(totalStopTime);
                Log.i("@Location idealTimeS", "" + idealTimeS);
                //Toast.makeText(getApplicationContext(), "15 Min Stope >= " + diffInTime, Toast.LENGTH_LONG).show();
            }

        } else {
            if (startOfSpeedZero != 0.0) {
                double diffInTime = location.getTime() - startOfSpeedZero;
                diffInTime = diffInTime / (60 * 1000);
                //  diffInTime = TimeUnit.MILLISECONDS.toMinutes(diffInTime);
                Log.i("@Location diffInTime", "" + diffInTime);
                if (diffInTime >= 15) {
                    totalStopTime = +diffInTime;
                    idealTimeS = String.valueOf(totalStopTime);


                    Log.i("@Location totalStopTime", "" + diffInTime);

                    // Toast.makeText(getApplicationContext(), "15 Min Stope >= " + diffInTime, Toast.LENGTH_LONG).show();
                }
            }

        }

        Log.i("@Location System time", "------------------------------------------------");

        double lat2 = location.getLatitude();
        double lon2 = location.getLongitude();

        //  Toast.makeText(MapsActivity.this, "lat2 :" + lat2, Toast.LENGTH_SHORT).show();
        //Toast.makeText(MapsActivity.this, "lon2:" +  lon2, Toast.LENGTH_SHORT).show();

        Log.v("The current Latitude", String.valueOf(lat_currentstr));
        Log.v("The current Longitude", String.valueOf(long_currentstr));

        Log.v("The previous Latitude", String.valueOf(latitude_prev));
        Log.v("The previous Longitude", String.valueOf(longitude_prev));

        if (latitude_prev == 0 && longitude_prev == 0) {
            latitude_prev = lat2;
            longitude_prev = lon2;
            distance_travelled = 0;

        } else {
            *//*get the distance covered from point A to point B*//*

            dbOperation.OPEN();
            Cursor cursor = dbOperation.getSlatlongData();
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                System.out.println("@FM..Slatitude" + cursor.getString(cursor.getColumnIndex(DbConstants.Slatitude)));
                System.out.println("@FM..Slongitude" + cursor.getString(cursor.getColumnIndex(DbConstants.Slongitudes)));

                Log.i("SLAt", cursor.getString(cursor.getColumnIndex(DbConstants.Slatitude)));
                Log.i("SLOng", cursor.getString(cursor.getColumnIndex(DbConstants.Slongitudes)));
                slat = cursor.getString(cursor.getColumnIndex(DbConstants.Slatitude));
                sLong = cursor.getString(cursor.getColumnIndex(DbConstants.Slongitudes));

            }

            dbOperation.CLOSE();

         *//*   Location startPoint=new Location("");
            startPoint.setLatitude(Double.valueOf(slat));
            startPoint.setLongitude(Double.valueOf(slat));

            Location endPoint=new Location("");
            endPoint.setLatitude(Double.valueOf(slat));
            endPoint.setLongitude(Double.valueOf(slat));

            double distance=startPoint.distanceTo(endPoint);*//*

            distance_travelled = distanceTravelled(Double.valueOf(slat), Double.valueOf(sLong), lat2, lon2);
            getSharedPreferences("preferenceId", MODE_PRIVATE).edit().putString("distance_travelled", String.valueOf(distance_travelled)).apply();

            //  distance_travelled = distanceTravelled(Double.valueOf(lat_currentstr), Double.valueOf(long_currentstr), lat2, lon2);
            Log.d("distance_travelled", String.valueOf(distance_travelled));
            *//*set previous latitude and longitude to the last location*//*
            // Toast.makeText(MapsActivity.class,+ distance_travelled,Toast.LENGTH_SHORT).show();
            latitude_prev = lat2;
            longitude_prev = lon2;


            if ((String.format("%.2f", ((location.getSpeed() * 3600) / 1000))).equals("null")) {
                speedAsKmHr = "0";
            } else {
                speedAsKmHr = String.format("%.2f", ((location.getSpeed() * 3600) / 1000));
            }
            //speedNew = Float.valueOf((location.getSpeed()*3600)/1000);
            DecimalFormat df = new DecimalFormat("##.##");
            speedNew = Double.valueOf(df.format(speedNew));
//             Double value   =  Double.parseDouble(new DecimalFormat("##.##").format(vehicalSpeed));
            speed1.setText(speedAsKmHr + "km/hr");


        }


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {

                double latitude = Double.parseDouble(lat_currentstr);
                double longitude = Double.parseDouble(long_currentstr);

                //Place current location marker
                final LatLng latLng_current = new LatLng(latitude, longitude);

                if (MarkerPoints.size() > 1) {
                    MarkerPoints.clear();
                    mMap.clear();
                }

                MarkerPoints.add(latLng_current);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng_current);
                markerOptions.title("Current Position");

                mCurrLocationMarker = mMap.addMarker(markerOptions);

                final LatLng latLngdestination = new LatLng(Double.parseDouble(getSharedPreferences(MyPREFERENCEId, MODE_PRIVATE).getString("Latitude", "")), Double.parseDouble(getSharedPreferences(MyPREFERENCEId, MODE_PRIVATE).getString("Longitude", "")));

                MarkerOptions markerOption = new MarkerOptions();
                markerOption.position(latLngdestination);
                markerOption.title("Destination Position");
                mLastLocationmarker = mMap.addMarker(markerOption);
                MarkerPoints.add(latLngdestination);

                if (MarkerPoints.size() == 1) {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                } else if (MarkerPoints.size() == 2) {
                    markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                }

                mMap.getUiSettings().setMapToolbarEnabled(true);
                // mMap.animateCamera(CameraUpdateFactory.zoomTo(5));
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng_current));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngdestination));
                mMap.getUiSettings().setScrollGesturesEnabled(true);
                mMap.getUiSettings().setCompassEnabled(true);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng_current, 10));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngdestination, 10));
                mMap.isTrafficEnabled();

                // Checks, whether start and end locations are captured
                if (MarkerPoints.size() >= 2) {
                    LatLng origin = MarkerPoints.get(0);
                    LatLng dest = MarkerPoints.get(1);

                    // Getting URL to the Google Directions API
                    String url = getUrl(origin, dest);
                    Log.d("onMapClick", url.toString());
                    FetchUrl FetchUrl = new FetchUrl();
                    // Start downloading json data from Google Directions API
                    FetchUrl.execute(url);
                    //move map camera
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                }
                mMap.addMarker(markerOption);

            }
        });

        if (location != null) {

            try {

                dbOperation.OPEN();
                Cursor cursor = dbOperation.getnewlatlongData();
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    StringBuffer buffer = new StringBuffer();

                    while (cursor.moveToNext()){
                        nlat = cursor.getString(cursor.getColumnIndex(DbConstants.Slatitude));
                        nlong = cursor.getString(cursor.getColumnIndex(DbConstants.Slongitudes));
                        isSend = cursor.getString(cursor.getColumnIndex(DbConstants.isSend));
                        buffer.append("Id :" + cursor.getString(0) + "\n");
                        buffer.append("latData :" + cursor.getString(cursor.getColumnIndex(DbConstants.Slatitude)) + "\n");
                        buffer.append("longData :" + cursor.getString(cursor.getColumnIndex(DbConstants.Slongitudes)) + "\n");
                        buffer.append("send :" + cursor.getString(cursor.getColumnIndex(DbConstants.isSend)) + "\n");
                        // showMessage("data", buffer.toString());
                        if (isSend.equals("false")) {
                            final Calendar c1 = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            end_timestr = sdf.format(new Date());
                            //String dateTime = end_timestr + "|" + end_datestr;
                            lat = String.valueOf(location.getLatitude());
                            // double i2= location.getLatitude() /60000;

                            //   double lat = location.getLatitude();
                            DecimalFormat df = new DecimalFormat("##.##");
                            // lat = Double.valueOf(df.format(lat));

                            longs = String.valueOf(location.getLongitude());
                            encriptString = "|||" + nlat + "|||" + nlong + "|||" + driverid + "|||" + journeyid1 + "|||" + speedAsKmHr + "|||" + totalStopTime + "|||" + deviceId + "|||" + end_timestr;
                            System.out.print("latitude--------" + location.getLatitude());
                            System.out.print("longitude--------" + location.getLongitude());
                            Log.i("driverid", "" + driverid);
                            Log.i("deviceId", "" + deviceId);
                            //System.out.print("cocatination Map of string--------"+encriptString);
                            //Toast.makeText(MapsActivity.this, "encriptString" +encriptString, Toast.LENGTH_LONG).show();

                            Cryptography_Android.Encrypt(encriptString, _cipherKey);
                            // System.out.print("encode data================================="+cryptography.Encrypt(encriptString,_cipherKey));
                            output = Cryptography_Android.Encrypt(encriptString, _cipherKey);
                            ot = output.replaceAll("\n", "");
                            // System.out.print("Maps"+ot);
                            //System.out.print("encriptStringOFMaps"+encriptString);
                            Log.i("MapsOfSend", "" + ot);
                            Log.i("Send", "" + encriptString);

                            dbOperation.OPEN();
                            dbOperation.addLatLongOfflineData(journeyid1,ot,"0");
                            dbOperation.CLOSE();

                            long resultNew = 0;
                            String Prevlat = String.valueOf(location.getLatitude());
                            String Prevlong = String.valueOf(location.getLongitude());
                            dbOperation.OPEN();
                            resultNew = dbOperation.addPrevlatlongData(Prevlat, Prevlong);
                            Log.i("PrevLatlongrslt", "" + String.valueOf(result1));
                            if (resultNew > 0) Log.i("Data not inserted", "");
                            else Log.i("Data is inserted", "");

                            Cursor cursor1 = dbOperation.getPrevlatlongData();
                            if (cursor1.getCount() > 0) {
                                cursor1.moveToFirst();
                                System.out.println("@FM..Prevlatitude" + cursor1.getString(cursor1.getColumnIndex(DbConstants.Prevlatitude)));
                                System.out.println("@FM..Prevlongitude" + cursor1.getString(cursor1.getColumnIndex(DbConstants.Prevlongitudes)));

                                //  Log.i("PrevLAt", cursor.getString(cursor.getColumnIndex(DbConstants.Prevlatitude)));
                                //   Log.i("PrevLOng", cursor.getString(cursor.getColumnIndex(DbConstants.Prevlongitudes)));
                                prevlat = cursor1.getString(cursor1.getColumnIndex(DbConstants.Prevlatitude));
                                prevLong = cursor1.getString(cursor1.getColumnIndex(DbConstants.Prevlongitudes));

                            }

                            distance = distanceCoverred(Double.parseDouble(prevlat), Double.valueOf(prevLong), lat2, lon2);
                            int meterConversion = 1000;
                            Double newDist = distance * meterConversion;

                            if (!nlat.equals(null) && !nlong.equals(null)) {

                                if (value == true) {
                                    getData();
                                    value = false;
                                    dbOperation.updatelatlongData(cursor.getString(cursor.getColumnIndex(DbConstants.SId)));
                                }

                                else if (value == false) {

                                    if (newDist > 5) {
                                        getData();

                                        long Updateresult = 0;
                                        Updateresult = dbOperation.updatePrevlatlongData(String.valueOf(lat2), String.valueOf(lon2));

                                        if (Updateresult > 0) Log.i("Data not inserted", "");
                                        else Log.i("Data is inserted", "");
                                        dbOperation.updatelatlongData(cursor.getString(cursor.getColumnIndex(DbConstants.SId)));

                                    }

                                }

                            }


                        }

                    }

                }

                dbOperation.CLOSE();

                checkTrackId();

            } catch (Exception e)
            {

                // Toast.makeText(getApplicationContext(), "catch null pointer", Toast.LENGTH_SHORT).show();// (location.getSpeed()*3600)/1000;
            }

      }
*/
    }


    public Boolean startTimeEndTimeValidation(String startTime, String endTime) {

        Boolean flag;

        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm:ss", Locale.getDefault());
        Date starttm = null;
        Date endtm = null;
        try {
            starttm = parseFormat.parse(startTime);
            endtm = parseFormat.parse(endTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String sttime = displayFormat.format(starttm);
        String edTime = displayFormat.format(endtm);

        if (sttime.compareTo(edTime) > 0) flag = false;
        else if (sttime.compareTo(edTime) == 0) flag = false;
        else flag = true;

        return flag;
    }


  /*  private void addCameraToMap(LatLng latLng_current)
    {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng_current)
                .zoom(8)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void addCameraToMapp(LatLng latLngdestination)
    {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLngdestination)
                .zoom(8)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }*/

    public void stopLocationUpdates() {
        //stop location updates
        if (mGoogleApiClient != null) {
            FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }

    }


    public void stopTimerTask() {

        timer.cancel();

//        if (doAsynchronousTask != null) {
//
//            Log.d("TIMER", "timer canceled");
//            doAsynchronousTask.cancel();
//            nCounter = 0;
//
//        }
    }

    private void redrawLine() {

        //mMap.clear();  //clears all Markers and Polylines

        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
        }
        //addMarker(); //add Marker in current position
        line = mMap.addPolyline(options); //add Polyline
    }

    public void getData() {

        StringRequest postRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // response
                try {
                    Log.i("ResponseDatabase", "" + response);
                    JSONObject obj = new JSONObject(response);
                    //Log.i("@Loc ResponseMaps----", obj.getString());
                } catch (JSONException e)

                {
                    if (e.toString().equalsIgnoreCase("NoConnectionError"))
                        //GlobalUtils.show_ToastCenter("Please check network", context);
                        //Toast.makeText(MapsActivity.this, "Please check network", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error

                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //Log.d("Error.Response", error.getMessage());

            }
        })

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("locationString", ot);
                System.out.print("deviceId=============" + ot);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(postRequest);

    }


    private Double distanceTravelled(double lat1, double lng1, double lat2, double lng2) {

      /*  double earthRadius = 6371;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist =  earthRadius * c;
        total_dist = total_dist + dist;
*/

        double latA = Math.toRadians(lat1);
        double lonA = Math.toRadians(lng1);
        double latB = Math.toRadians(lat2);
        double lonB = Math.toRadians(lng2);
        double cosAng = (Math.cos(latA) * Math.cos(latB) * Math.cos(lonB - lonA)) + (Math.sin(latA) * Math.sin(latB));
        double ang = Math.acos(cosAng);
        total_dist = ang * 6371;

        dbOperation.OPEN();
        long Updated = 0;
        Updated = dbOperation.updateSlatlongData(String.valueOf(lat2), String.valueOf(lng2));

        if (Updated > 0) Log.i("Data not inserted", "");
        else Log.i("Data is inserted", "");

        dbOperation.CLOSE();

        dist = dist + total_dist;
        return dist;
        /*//just in case it is needed to be converted in meters use this part
            int meterConversion = 1000;
            return Double.valueOf(dist * meterConversion);*/

        //  return total_dist;
    }

    private Double distanceCoverred(double lat1, double lng1, double lat2, double lng2) {
      /*  double earthRadius = 6371;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist =  earthRadius * c;


        total_dist = total_dist + dist;
*/
        double latA = Math.toRadians(lat1);
        double lonA = Math.toRadians(lng1);
        double latB = Math.toRadians(lat2);
        double lonB = Math.toRadians(lng2);
        double cosAng = (Math.cos(latA) * Math.cos(latB) * Math.cos(lonB - lonA)) + (Math.sin(latA) * Math.sin(latB));
        double ang = Math.acos(cosAng);
        total_dist = ang * 6371;

      /*  dbOperation.OPEN();
        long Updated = 0;
        Updated = dbOperation.updateSlatlongData(String.valueOf(lat2),String.valueOf(lng2));

        if (Updated>0)
            Log.i("Data not inserted","");
        else
            Log.i("Data is inserted","");

        dbOperation.CLOSE();

        dist = dist + total_dist;*/
        return total_dist;
        /*//just in case it is needed to be converted in meters use this part
            int meterConversion = 1000;
            return Double.valueOf(dist * meterConversion);*/

        //  return total_dist;
    }


    public void mgetData() {

        StringRequest postRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // response
                try {
                    Log.i("@Loc ResponseMaps-", response);
                    JSONObject obj = new JSONObject(response);

                } catch (JSONException e)

                {
                    if (e.toString().equalsIgnoreCase("NoConnectionError"))
                        //GlobalUtils.show_ToastCenter("Please check network", context);
                        //Toast.makeText(MapsActivity.this, "Please check network", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error

                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //Log.d("Error.Response", error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("locationString", mot);
                System.out.print("mOTT=============" + mot);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(postRequest);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(API).build();
        mGoogleApiClient.connect();
    }

    public void showMessage(String title, String Message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {


    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }

            return false;

        } else {

            return true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

         switch (requestCode) {

             case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {
                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }

                return;
            }
         }

    }

    private boolean isBackgroundServiceRunning(Class<?> serviceClass) {

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void deleteItems() {
        dbOperation.OPEN();
        dbOperation.clearDatabase();
    }

    public static OkHttpClient.Builder initLog() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS);
        // add your other interceptors …
        // add logging as last interceptor
        httpClient.addInterceptor(logging).build();
        return httpClient;
    }

}
