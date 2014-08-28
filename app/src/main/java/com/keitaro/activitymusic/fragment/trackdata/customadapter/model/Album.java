package com.keitaro.activitymusic.fragment.trackdata.customadapter.model;

import android.graphics.Bitmap;

import java.io.ByteArrayInputStream;

/**
 * Created by user1 on 2014/08/27.
 */
public class Album {
    private final String name;
    private final String artist;
    private final byte[] artwork;

    public Album(String name, String artist, byte[] artwork){
        this.name=name;
        this.artist=artist;
        this.artwork=artwork;
    }

    public byte[] getArtwork() {
        return artwork;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

}
