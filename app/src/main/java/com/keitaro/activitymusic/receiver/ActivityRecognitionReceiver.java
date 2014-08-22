package com.keitaro.activitymusic.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.LocationServices;
import com.keitaro.activitymusic.MyActivity;
import com.keitaro.activitymusic.databese.model.LocationData;

/**
 * Created by user1 on 2014/08/20.
 *
 * 行動認識と位置の情報を取得するブロードキャストレシーバ。
 * 取得したデータはデータベースに格納する。
 */
public class ActivityRecognitionReceiver extends BroadcastReceiver {

    private static GoogleApiClient mGoogleApiClient;


    public static String ACTIVITY = "ACTION_ACTIVITY_RECOGNITION";
//    public static String LOCATION = "ACTION_LOCATION_SERVICE";

    private static Location tmpLocData; // 行動認識の情報とまとめてデータベースに格納するために、フィールド変数として位置情報を持つ

    public static synchronized void setGoogleApiClient(GoogleApiClient googleApiClient) {
        ActivityRecognitionReceiver.mGoogleApiClient = googleApiClient;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        // 行動認識された場合
        if(intent.getAction().equals(ACTIVITY)) {
            ActivityRecognitionResult result
                    = (ActivityRecognitionResult) bundle.get("com.google.android.location.internal.EXTRA_ACTIVITY_RESULT");

            if(mGoogleApiClient == null){
                return;
            }

            tmpLocData = LocationServices.FusedLocationApi.getLastLocation(this.mGoogleApiClient);
            if(tmpLocData == null){
                System.out.println("tmpLocData is null!");
                if(!this.mGoogleApiClient.isConnected()){
                    System.out.println("gac is not connected!");
                }
            }

            saveActvtyRecgResult(result, tmpLocData);

            return;
        }

    }

    /**
     * 行動認識の情報を位置情報とまとめてデータベースに格納する
     * @param result 行動認識の結果
     */
    private void saveActvtyRecgResult(ActivityRecognitionResult result, Location location){
        Log.d("LocationServeceReceiverReceiver", this.getTypeName(result.getMostProbableActivity().getType()));

        if(this.tmpLocData == null){
            Log.d("LocationServeceReceiverReceiver", "loc is null!!");

            return;
        }

        LocationData locationData = new LocationData();
//        locationData.timestamp = location.getTime();  // 位置情報を取得した時刻を格納
        locationData.timestamp = System.currentTimeMillis(); // 位置情報を取得した時刻ではなくて現在の時刻を格納
        locationData.lat = location.getLatitude();
        locationData.lon = location.getLongitude();
        locationData.activity = result.getMostProbableActivity().getType();
        locationData.accuracy = location.getAccuracy();
        locationData.save(); // データベースに格納
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

}
