package com.keitaro.activitymusic.fragment.trackdata.customadapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.keitaro.activitymusic.R;
import com.keitaro.activitymusic.fragment.trackdata.customadapter.model.Album;
import com.keitaro.activitymusic.fragment.trackdata.customadapter.model.Artist;

import java.io.ByteArrayInputStream;

/**
 * Created by user1 on 2014/08/28.
 */
public class ArtistAdapter extends ArrayAdapter<Artist> {

    private final int resourceId;

    public ArtistAdapter(Context context) {
        super(context, R.layout.item_albumdata);
        this.resourceId = R.layout.item_albumdata;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resourceId, null);
        }

        Artist artist = getItem(position);

        TextView tv = (TextView) convertView.findViewById(R.id.album_name);
        tv.setText(artist.getName());

        ImageView imageView = (ImageView) convertView.findViewById(R.id.album_artwork);
        Bitmap artwork = BitmapFactory.decodeStream(new ByteArrayInputStream(artist.getArtwork()));
        imageView.setImageBitmap(artwork);

        return convertView;
    }
}
