package com.keitaro.activitymusic.databese;

import android.util.Log;

import com.activeandroid.query.Select;
import com.keitaro.activitymusic.databese.model.LocationData;
import com.keitaro.activitymusic.databese.model.MusicData;

import java.util.List;

/**
 * Created by user1 on 2014/08/25.
 */
public class DataBaseAccessWrapper {
    public List<LocationData> getRecentActivity() {
        List<LocationData> itmes = new Select().from(LocationData.class).execute();
        for (LocationData i : itmes) {
            String data = "id : " + i.getId() + ", time : " + i.timestamp + ", lat : " + i.lat + ", lon : " + i.lon + ", accuracy : " + i.accuracy + ", activity : " + i.activity;
            Log.d("locdata", data);
        }

        List<MusicData> items = new Select().from(MusicData.class).execute();
        for (MusicData i : items) {
            String data = "id : " + i.getId() + ", artist : " + i.artist + ", album :" + i.album + ", track name : " + i.trackName + ", uri : " + i.uri;
            Log.d("musicdata", data);
        }
        return null;
    }

}
