package com.acquiscent.myapplication.Activity;

/**
 * Created by acquiscent on 15/9/17.
 */

import android.location.Location;

public interface GPSCallback {
    public abstract void onGPSUpdate(Location location);
}
