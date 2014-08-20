package com.keitaro.activitymusic;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.keitaro.activitymusic.databese.model.LocationData;

/**
 * Created by user1 on 2014/08/20.
 */
public class MyLocationListener implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        LocationData locData = new LocationData();
        locData.timestamp = location.getTime();
        locData.lon = location.getLongitude();
        locData.lat = location.getLatitude();
        locData.save();
        Log.d("onLocationChanged", "timestamp : " + locData.timestamp + ", lon : " + locData.lon + ", lat : " + locData.lat);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d("MyLocationListener", "onProviderEnabled");
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d("MyLocationListener", "onProviderDisabled");
    }

}
