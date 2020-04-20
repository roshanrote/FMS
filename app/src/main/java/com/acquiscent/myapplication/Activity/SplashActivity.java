package com.acquiscent.myapplication.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.acquiscent.myapplication.R;

public class SplashActivity extends AppCompatActivity {
    Context con;
    GPSTracker gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        con = this;
        startAppThread();
    }

    @Override
    protected void onResumeFragments()
    {
        super.onResumeFragments();
        startAppThread();
    }


    private void marshmallowSupport() {
        if (ContextCompat.checkSelfPermission(con, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(con, android.Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(con, android.Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(con, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(con, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(con, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) con, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale((Activity) con, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale((Activity) con, Manifest.permission.INTERNET) ||
                    ActivityCompat.shouldShowRequestPermissionRationale((Activity) con, Manifest.permission.ACCESS_WIFI_STATE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale((Activity) con, android.Manifest.permission.ACCESS_NETWORK_STATE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale((Activity) con, android.Manifest.permission.CAMERA)) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(con);
                builder.setMessage(getString(R.string.app_name) + " needs the following permissions.").setCancelable(false).setPositiveButton("Allow", new DialogInterface.OnClickListener()
                {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        ActivityCompat.requestPermissions((Activity) con,
                                new String[]{
                                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                        android.Manifest.permission.INTERNET,
                                        android.Manifest.permission.ACCESS_WIFI_STATE,
                                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        android.Manifest.permission.ACCESS_NETWORK_STATE,
                                        android.Manifest.permission.CAMERA}
                                , 11);
                    }
                }).setNegativeButton("Reject", new DialogInterface.OnClickListener()
                {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        finish();
                    }
                });
                final AlertDialog alert = builder.create();
                alert.show();
            }
            else {
                ActivityCompat.requestPermissions((Activity) con,
                        new String[]{
                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.ACCESS_WIFI_STATE,
                                android.Manifest.permission.INTERNET,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                android.Manifest.permission.ACCESS_NETWORK_STATE,
                                android.Manifest.permission.CAMERA
                        }, 11);
            }

        }
        else {

            startAppThread();

        }
    }

    private void startAppThread() {
        new CountDownTimer(200, 200)
        {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        // Start your app main activity
                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        gps = new GPSTracker(SplashActivity.this);
                        startService(new Intent(SplashActivity.this, GPSTracker.class));
                        startActivity(i);
                        // close this activity
                        finish();

                    }
                }, 200);
            }
        }.start();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isBackgroundServiceRunning(GPSTracker.class)) {
            startService(new Intent(SplashActivity.this, GPSTracker.class));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private boolean isBackgroundServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}

