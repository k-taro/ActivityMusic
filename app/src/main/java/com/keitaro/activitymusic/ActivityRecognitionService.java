package com.keitaro.activitymusic;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;

/**
 * Created by user1 on 2014/08/20.
 */
public class ActivityRecognitionService extends IntentService {
    private final String TAG = "ActivityRecognitionService";

    public ActivityRecognitionService() {
        super("ActivityRecognitionSerVice");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
//            if(Config.TESTER_VERSION){
                Log.d(TAG, "ActivityRecognitionResult: "+result.getMostProbableActivity().getType());
                Log.d(TAG, result.toString());
//            }

        }
    }
}
