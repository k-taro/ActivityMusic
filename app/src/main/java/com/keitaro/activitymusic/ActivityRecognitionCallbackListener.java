package com.keitaro.activitymusic;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Set;

/**
 * Created by user1 on 2014/08/20.
 */
public class ActivityRecognitionCallbackListener implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @Override
    public void onConnected(Bundle bundle) {
        Set<String> set = bundle.keySet();
        for(String s : set){
            Log.d("ActivityRecognition", s + " : " + bundle.getSerializable(s));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
