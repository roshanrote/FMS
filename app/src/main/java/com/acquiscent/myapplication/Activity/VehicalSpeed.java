package com.acquiscent.myapplication.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.acquiscent.myapplication.R;


public class VehicalSpeed extends Activity {
    LocationManager locManager;
    LocationListener li;
    Context context;
    String lat_currentstr, long_currentstr;
    TextView text;
    SharedPreferences.Editor editorLatLong;
    SharedPreferences sharedpreferencesLatLong;
    Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehical);
        // Acquire a reference to the system Location Manager

        // LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        li = new speed();

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
        locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, li);
      //  text=(TextView) findViewById(R.id.gps_info);

        // Define a listener that responds to location updates


       /* LocationListener locationListener = new LocationListener()
        {
            public void onLocationChanged(Location location)
            {
                mLastLocation=location;
                final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                editorLatLong.putString("latitude", String.valueOf(location.getLatitude()));
                editorLatLong.putString("longitude", String.valueOf(location.getLongitude()));
                editorLatLong.commit();

                lat_currentstr = sharedpreferencesLatLong.getString("latitude", "").toString();
                long_currentstr = sharedpreferencesLatLong.getString("longitude", "").toString();
                System.out.print("latitude------mapsactivity-----" + lat_currentstr);

                double latitud = Double.parseDouble(lat_currentstr);
                double longitud = Double.parseDouble(long_currentstr);

                System.out.print("latitude--------" + location.getLatitude());
                System.out.print("longitude--------" + location.getLongitude());

                location.getLatitude();
                location.getLongitude();

                //text.setText(String.valueOf(location.getSpeed()));

             //   Toast.makeText(context, "Current speed:" + location.getSpeed(), Toast.LENGTH_SHORT).show();
                  Toast.makeText(context, "Current speed:" + location.hasSpeed(), Toast.LENGTH_SHORT).show();
            }

            public void onStatusChanged(String provider, int status, Bundle extras)
            {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {

        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, locationListener);

    }*/

}
    class speed implements LocationListener{
        @Override
        public void onLocationChanged(Location loc) {
            Float thespeed=loc.getSpeed();
            Toast.makeText(VehicalSpeed.this,String.valueOf(thespeed), Toast.LENGTH_LONG).show();
            Log.i("Speed","" +thespeed);
        }
        @Override
        public void onProviderDisabled(String arg0) {}
        @Override
        public void onProviderEnabled(String arg0) {}
        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}

    }

}



