package com.keitaro.activitymusic.databese.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by user1 on 2014/08/19.
 *
 * 楽曲情報を表す Activeandroid 用データモデル
 */
@Table(name = "Musics")
public class MusicData extends Model {

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
        this.artist = artist;
        this.album = album;
        this.trackName = trackName;
    }

}
