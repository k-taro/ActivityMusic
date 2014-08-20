package com.keitaro.activitymusic.databese.model;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by user1 on 2014/08/19.
 */
@Table(name = "Musics")
public class MusicData extends Model {
//    @Column(name = "id", notNull = true, unique = true)
//    public int id;

    @Column(name = "artist")
    public String artist;

    @Column(name = "album")
    public String album;

    @Column(name = "trackName")
    public String trackName;

    public MusicData(){
        super();
    }

    public MusicData(String artist, String album, String trackName){
//        this.id = id;
        this.artist = artist;
        this.album = album;
        this.trackName = trackName;
    }

}
