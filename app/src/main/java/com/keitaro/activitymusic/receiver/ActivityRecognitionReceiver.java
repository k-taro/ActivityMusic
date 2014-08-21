package com.keitaro.activitymusic.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.keitaro.activitymusic.databese.model.LocationData;

/**
 * Created by user1 on 2014/08/20.
 */
public class ActivityRecognitionReceiver extends BroadcastReceiver {

    public static String ACTIVITY = "ACTION_ACTIVITY_RECOGNITION";
    public static String LOCATION = "ACTION_LOCATION_SERVICE";

    private static LocData tmpLocData;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        if(intent.getAction().equals(ACTIVITY)) {
            ActivityRecognitionResult result = (ActivityRecognitionResult) bundle.get("com.google.android.location.internal.EXTRA_ACTIVITY_RESULT");
            saveActvtyRecgResult(result);

            return;
        }

        if(intent.getAction().equals(LOCATION)){
//            Set<String> set = bundle.keySet();

//            for(String s : set){
//                Log.d("LocationServices", s + " : " + bundle.get(s));
//            }

            Location loc = (Location) bundle.get("com.google.android.location.LOCATION");
            this.tmpLocData = new LocData();
            this.tmpLocData.timestamp = loc.getTime();
            this.tmpLocData.lat = loc.getLatitude();
            this.tmpLocData.lon = loc.getLongitude();
            Log.d("LocationServices", "(" + loc.getLatitude() + ", " + loc.getLongitude() +")");
        }

    }

    private void saveActvtyRecgResult(ActivityRecognitionResult result){
        Log.d("LocationServeceReceiverReceiver", this.getTypeName(result.getMostProbableActivity().getType()));

        if(this.tmpLocData == null){
            Log.d("LocationServeceReceiverReceiver", "loc is null!!");
            return;
        }

        Log.d("LocationServeceReceiverReceiver", "save");

        LocationData locationData = new LocationData();
        locationData.timestamp = this.tmpLocData.timestamp;
        locationData.lat = this.tmpLocData.lat;
        locationData.lon = this.tmpLocData.lon;
        locationData.activity = result.getMostProbableActivity().getType();
        locationData.save();
    }

    private void readLocationData(){

    }

    private String getTypeName(int activityType) {
        switch(activityType) {
            case DetectedActivity.IN_VEHICLE:
                return "車で移動中";
            case DetectedActivity.ON_BICYCLE:
                return "自転車で移動中";
            case DetectedActivity.ON_FOOT:
                return "徒歩で移動中";
            case DetectedActivity.STILL:
                return "待機中";
            case DetectedActivity.UNKNOWN:
                return "不明";
            case DetectedActivity.TILTING:
                return "デバイスが傾き中";
        }
        return null;
    }

    private class LocData{
        private long timestamp;
        private double lat;
        private double lon;
    }

}
