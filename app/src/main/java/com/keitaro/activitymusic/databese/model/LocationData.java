package com.keitaro.activitymusic.databese.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by user1 on 2014/08/19.
 * 位置情報を表す Activeandroid 用データモデル
 */
@Table(name = "Locations")
public class LocationData extends Model{
//    @Column(name = "id", unique = true, notNull = true)
//    public int id;

    @Column(name = "timestamp", notNull = true)
    public long timestamp;

    @Column(name = "lat", notNull = true)
    public double lat;

    @Column(name = "lon", notNull = true)
    public double lon;

    @Column(name = "activity")
    public Integer activity;

    @Column(name = "accuracy")
    public float accuracy;

    @Column(name = "musicID", notNull = true)
    public Long music_id;

    public LocationData(){
        super();
    }

    public LocationData(long timestamp, int lat, int lon, int activity, float accuracy){
        this.timestamp = timestamp;
        this.lat = lat;
        this.lon = lon;
        this.activity = activity;
        this.accuracy = accuracy;
    }

}
