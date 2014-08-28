package com.keitaro.activitymusic.util;

import android.os.Bundle;
import android.util.Log;

import java.util.Set;

/**
 * Created by user1 on 2014/08/28.
 */
public class LogUtil {
    public static void printBundle(String tag, Bundle bundle){
        Set<String> keySet = bundle.keySet();
        for(String key : keySet) {
            Log.d(tag, key + " : "+bundle.getString(key));
        }
    }
}
