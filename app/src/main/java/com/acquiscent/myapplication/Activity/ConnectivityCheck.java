package com.acquiscent.myapplication.Activity;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by acquiscent on 29/1/18.
 */

public class ConnectivityCheck {

    private Activity _context;

    public ConnectivityCheck(Activity _context) {
        this._context = _context;
    }

    public boolean isConnectedToInternet() {
        try {
            if (_context != null) {
                ConnectivityManager connectivityManager = (ConnectivityManager) _context.getSystemService(CONNECTIVITY_SERVICE);
                assert connectivityManager != null;
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
           // Log.e(Receiver.class.getName(), e.getMessage());
            return false;
        }
    }

    public boolean isConnectedToInternet(Context _context) {
        try {
            if (_context != null) {
                ConnectivityManager connectivityManager = (ConnectivityManager) _context.getSystemService(CONNECTIVITY_SERVICE);
                assert connectivityManager != null;
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
           // Log.e(Receiver.class.getName(), e.getMessage());
            return false;
        }
    }

}
