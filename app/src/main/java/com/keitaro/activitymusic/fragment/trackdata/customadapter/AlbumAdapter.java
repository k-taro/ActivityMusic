package com.keitaro.activitymusic.fragment.trackdata.customadapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.keitaro.activitymusic.R;
import com.keitaro.activitymusic.fragment.trackdata.customadapter.model.Album;

import org.w3c.dom.Text;

import java.io.FileDescriptor;
import java.util.List;

/**
 * Created by user1 on 2014/08/27.
 */
public class AlbumAdapter extends ArrayAdapter<Album> {

    private final int resourceId;

    public AlbumAdapter(Context context) {
        super(context, R.layout.item_albumdata);
        this.resourceId = R.layout.item_albumdata;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resourceId, null);
        }

        Album album = getItem(position);

        TextView tv = (TextView) convertView.findViewById(R.id.album_name);
        tv.setText(album.getName());

        ImageView imageView = (ImageView) convertView.findViewById(R.id.album_artwork);


        imageView.setImageBitmap(BitmapFactory.decodeFile(album.getArtworkPath()));
//        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
//        Uri uri = Uri.parse(album.getArtworkPath());
//        mmr.setDataSource(getContext(),uri);
//
//        mmr.setDataSource(album.getArtworkPath());
//
//
//        byte[] data = mmr.getEmbeddedPicture();
//
//        if (null != data) {
//            imageView.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
//        }else{
//            imageView.setImageBitmap(BitmapFactory.decodeResource(convertView.getResources(), R.drawable.ic_action_headphones));
//        }

        return convertView;
    }
}
