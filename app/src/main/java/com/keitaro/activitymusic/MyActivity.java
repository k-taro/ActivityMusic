package com.keitaro.activitymusic;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.keitaro.activitymusic.databese.model.LocationData;
import com.keitaro.activitymusic.databese.model.MusicData;
import com.keitaro.activitymusic.receiver.ActivityRecognitionReceiver;

import java.util.List;


public class MyActivity extends Activity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my);

        this.initButtonSetting();

        this.initActivityRecognition(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * デバッグ用のボタンの動作を設定する
     */
    private void initButtonSetting(){
        // ボタンをタップすると、データベース上の楽曲情報をログに表示する
        Button b = (Button) this.findViewById(R.id.button1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout display = (LinearLayout)findViewById(R.id.data_display);
                display.removeAllViews();
                List<LocationData> itmes = new Select().from(LocationData.class).execute();
                for(LocationData i : itmes){
                    TextView tv = new TextView(getApplicationContext());
                    tv.setText("id : " + i.getId() +", time : " + i.timestamp +", lat : " +  i.lat + ", lon : " + i.lon + ", activity : " + i.activity);
                    Log.d("locdata", "id : " + i.getId() + ", time : " + i.timestamp + ", lat : " + i.lat + ", lon : " + i.lon + ", activity : " + i.activity);
                    display.addView(tv);
                }

                List<MusicData> items = new Select().from(MusicData.class).execute();
                for (MusicData i : items) {
                    TextView tv = new TextView(getApplicationContext());
                    tv.setText("id : " + i.getId() + ", track name : " + i.trackName);
                    Log.d("musicdata","id : " + i.getId() + ", track name : " + i.trackName);
                    display.addView(tv);
                }
            }
        });
    }

    /**
     * 行動認識と位置情報を取得するための初期化
     * @param context
     */
    private void initActivityRecognition(Context context){

        //final GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(ActivityRecognition.API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        new Thread(){
            @Override
            public void run(){
                mGoogleApiClient.blockingConnect();

                Intent intent = new Intent();
                intent.setAction(ActivityRecognitionReceiver.ACTIVITY);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

                ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mGoogleApiClient, 0, pendingIntent);

                intent = new Intent();
                intent.setAction(ActivityRecognitionReceiver.LOCATION);
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

                LocationRequest locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY).setFastestInterval(500);
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, pendingIntent);
            }
        }.start();

    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("ActivityRecognitionClientCallbackListener", "OnConnected");

        if (bundle == null){
            Log.d("ActivityRecognitionClientCallbackListener", "bundle is null!!");
            return;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
