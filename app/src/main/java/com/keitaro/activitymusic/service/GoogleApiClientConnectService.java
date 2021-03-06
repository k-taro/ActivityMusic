package com.keitaro.activitymusic.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationServices;
import com.keitaro.activitymusic.receiver.ActivityRecognitionReceiver;

/**
 * Created by user1 on 2014/08/21.
 *
 * 位置情報と楽曲情報を取得するために GoogleApiClient に接続するサービス。
 * "サービスから GoogleApiClient に接続する"ことでアクティビティが死んでもデータを取得し続けることができる。
 */
public class GoogleApiClientConnectService extends Service
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @Override
    public void onCreate() {
        super.onCreate();
        new ConnectThread().start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /**
     * GoogleApiClient に接続するためのスレッド
     * GoogleApiClient#blockingConnect で接続する必要があるけど
     * UI スレッド中ではブロッキング処理ができないため別スレッドで動かす。
     */
    private class ConnectThread extends Thread{
        @Override
        public void run() {
            GoogleApiClient googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addApi(ActivityRecognition.API)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(GoogleApiClientConnectService.this)
                    .addOnConnectionFailedListener(GoogleApiClientConnectService.this)
                    .build();

            googleApiClient.blockingConnect();

            ActivityRecognitionReceiver.setGoogleApiClient(googleApiClient);

            if(googleApiClient.isConnected()){
                Log.d("GoogleApiClientConnectService","client is Connected");
            }else{
                Log.d("GoogleApiClientConnectService","client is not Connected!!");
            }

            //Intent i = new Intent(getApplicationContext(), ActivityRecognitionReceiver.class);
            Intent i = new Intent();
            i.setAction(ActivityRecognitionReceiver.ACTIVITY);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, i, 0);

            ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(googleApiClient, 0, pendingIntent);
        }
    }
}
