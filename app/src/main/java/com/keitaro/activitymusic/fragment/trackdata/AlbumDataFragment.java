package com.keitaro.activitymusic.fragment.trackdata;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.activeandroid.query.Select;
import com.keitaro.activitymusic.R;
import com.keitaro.activitymusic.databese.model.LocationData;
import com.keitaro.activitymusic.databese.model.MusicData;
import com.keitaro.activitymusic.fragment.trackdata.customadapter.AlbumAdapter;
import com.keitaro.activitymusic.fragment.trackdata.customadapter.model.Album;
import com.keitaro.activitymusic.util.ActivityTypeTranslater;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by user1 on 2014/08/26.
 */
public class AlbumDataFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_albumdata, null);

        AlbumAdapter adapter = new AlbumAdapter(getActivity().getApplicationContext());

        GridView gridView = (GridView) rootView.findViewById(R.id.grid_albumdata);
        gridView.setAdapter(adapter);

        new GetTrackListTask(adapter).execute();

        return rootView;
    }

    class GetTrackListTask extends AsyncTask<Void, Void, List<Album>> {
        private final AlbumAdapter adapter;

        public GetTrackListTask(AlbumAdapter adapter){
            this.adapter = adapter;
        }

        @Override
        protected List<Album> doInBackground(Void... voids) {
            return this.getRecentActivity();
//            return new ArrayList<Album>();
        }

        @Override
        protected void onPostExecute(List<Album> list) {
            for(Album album : list) {
                adapter.add(album);
            }
        }

        private List<Album> getRecentActivity() {

            List<MusicData> albumList = new Select().from(MusicData.class).execute();
            List<Album> ret = new ArrayList<Album>();

            for(MusicData musicData : albumList){

                ContentResolver cr = getActivity().getApplicationContext().getContentResolver();
                String[] columns = new String[]{
                        MediaStore.Audio.Albums._ID,
                        MediaStore.Audio.Albums.ALBUM,
                        MediaStore.Audio.Albums.ARTIST,
                        MediaStore.Audio.Albums.ALBUM_ART
                };

                Cursor cursor = cr.query(
                        MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                        columns,
                        MediaStore.Audio.Albums.ALBUM + "=? and "+MediaStore.Audio.Albums.ARTIST + "=?",
                        new String[]{musicData.album, musicData.artist},
                        null
                );

                if(cursor.moveToFirst()) {
                    String albumArtPath = cursor.getString(cursor.getColumnIndex( MediaStore.Audio.Albums.ALBUM_ART));
                    ret.add(new Album(musicData.album,musicData.artist,albumArtPath));
                    do {
                        String albumArt = cursor.getString(cursor.getColumnIndex( MediaStore.Audio.Albums.ALBUM_ART));
                        Log.d("albumArt",albumArt);
                    } while (cursor.moveToNext());
                }else{
                    Log.d("hogehogehoge", "cursor is not hogehoge");
                }

            }

//            List<MusicData> albumList = new Select("DISTINCT album").from(MusicData.class).execute();
//            List<MusicData> albumList = new Select().from(MusicData.class).execute();
//            for(MusicData musicData : albumList){
//                ret.add(new Album(musicData.album, musicData.artist, musicData.uri));
//            }

            return ret;
        }
    }
}
