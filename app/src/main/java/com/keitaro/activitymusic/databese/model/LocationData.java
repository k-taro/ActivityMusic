package com.keitaro.activitymusic.databese.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by user1 on 2014/08/19.
 */
@Table(name = "Locations")
public class LocationData extends Model{
//    @Column(name = "id", unique = true, notNull = true)
//    public int id;

    @Column(name = "time", notNull = true)
    public Timestamp timestamp;

    @Column(name = "lat", notNull = true)
    public int lat;

    @Column(name = "lon", notNull = true)
    public int lon;

    @Column(name = "activity")
    public String activity;

//    @Column(name = "musicId")
//    public long musicId;

    public List<MusicData> musics() {
        return getMany(MusicData.class, "id");
    }

}
