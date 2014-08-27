package com.keitaro.activitymusic.fragment.trackdata.customadapter.model;

/**
 * Created by user1 on 2014/08/27.
 */
public class Album {
    private final String name;
    private final String artist;
    private final String artworkPath;

    public Album(String name, String artist, String artworkPath){
        this.name=name;
        this.artist=artist;
        this.artworkPath=artworkPath;
    }

    public String getArtworkPath() {
        return artworkPath;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

}
