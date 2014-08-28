package com.keitaro.activitymusic.fragment.trackdata.customadapter.model;

import java.util.Arrays;

/**
 * Created by user1 on 2014/08/28.
 */
public class Artist {
    private final String name;
    private final byte[] artwork;
    private final String[] albums;

    public Artist(String name, byte[] artwork, String[] albums){
        this.albums = Arrays.copyOf(albums, albums.length);
        this.name = name;
        this.artwork = artwork;
    }

    public String getName() {
        return name;
    }

    public byte[] getArtwork() {
        return artwork;
    }

    public String[] getAlbums() {
        return Arrays.copyOf(albums, albums.length);
    }


}
