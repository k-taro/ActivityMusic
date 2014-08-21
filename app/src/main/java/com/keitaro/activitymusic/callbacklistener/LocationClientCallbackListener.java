package com.keitaro.activitymusic.callbacklistener;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by user1 on 2014/08/21.
 */
public class LocationClientCallbackListener implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
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
