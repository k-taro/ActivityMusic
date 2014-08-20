package com.keitaro.activitymusic;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.activeandroid.query.Select;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.keitaro.activitymusic.databese.model.MusicData;

import java.util.List;


public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my);

        this.initButtonSetting();

        this.initLocationListener();

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

    private void initButtonSetting(){
        // ボタンをタップすると、データベース上の楽曲情報をログに表示する
        Button b = (Button) this.findViewById(R.id.button1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<MusicData> items = new Select().from(MusicData.class).execute();
                for (MusicData i : items) {
                    Log.d("musicdata", "id : " + i.getId() + ", track name : " + i.trackName);
                }
            }
        });
    }

    private void initLocationListener(){
        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);

        String provider = locationManager.getBestProvider(criteria, true);

        Log.d("initLocation", "provider : " + provider);

        locationManager.requestLocationUpdates(provider,10,0,new MyLocationListener());
    }

    private void initActivityRecognition(Context context){
        ActivityRecognitionCallbackListener l = new ActivityRecognitionCallbackListener();
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(l)
                .addOnConnectionFailedListener(l)
                .build();

        googleApiClient.connect();
    }
}
