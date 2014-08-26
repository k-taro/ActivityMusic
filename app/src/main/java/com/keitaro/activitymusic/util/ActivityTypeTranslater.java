package com.keitaro.activitymusic.util;

import com.google.android.gms.location.DetectedActivity;

/**
 * Created by user1 on 2014/08/26.
 */
public class ActivityTypeTranslater {
    public static String getTypeName(int activityType) {
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
                return "不明な行動中";
            case DetectedActivity.TILTING:
                return "デバイスが傾き中";
        }
        return null;
    }
}
