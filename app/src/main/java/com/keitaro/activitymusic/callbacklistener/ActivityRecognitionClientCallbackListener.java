package com.keitaro.activitymusic.callbacklistener;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionApi;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.Set;

/**
 * Created by user1 on 2014/08/20.
 */
public class ActivityRecognitionClientCallbackListener implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

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
