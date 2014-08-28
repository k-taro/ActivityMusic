package com.keitaro.activitymusic.fragment.trackdata;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.activeandroid.query.Select;
import com.keitaro.activitymusic.R;
import com.keitaro.activitymusic.databese.model.MusicData;
import com.keitaro.activitymusic.fragment.trackdata.customadapter.AlbumAdapter;
import com.keitaro.activitymusic.fragment.trackdata.customadapter.ArtistAdapter;
import com.keitaro.activitymusic.fragment.trackdata.customadapter.model.Album;
import com.keitaro.activitymusic.fragment.trackdata.customadapter.model.Artist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user1 on 2014/08/26.
 */
public class ArtistDataFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_albumdata, null);

        ArtistAdapter adapter = new ArtistAdapter(getActivity().getApplicationContext());

        GridView gridView = (GridView) rootView.findViewById(R.id.grid_albumdata);
        gridView.setAdapter(adapter);

        new GetTrackListTask(adapter).execute();

        return rootView;
    }

    class GetTrackListTask extends AsyncTask<Void, Void, List<Artist>> {
        private final ArtistAdapter adapter;

        public GetTrackListTask(ArtistAdapter adapter){
            this.adapter = adapter;
        }

        @Override
        protected List<Artist> doInBackground(Void... voids) {
            return this.getRecentActivity();
//            return new ArrayList<Album>();
        }

        @Override
        protected void onPostExecute(List<Artist> list) {
            for(Artist album : list) {
                adapter.add(album);
            }
        }

        private List<Artist> getRecentActivity() {

//            List<MusicData> albumList = new Select().from(MusicData.class).where("in " + new Select("DISTINCT artist").from(MusicData.class).toSql()).execute();
            List<MusicData> artistList
                    = new Select().from(MusicData.class)
                    .where("artist IN (" + new Select("DISTINCT artist").from(MusicData.class).toSql() + ")").execute();
            List<Artist> ret = new ArrayList<Artist>();

            for(MusicData musicData : artistList){
                String artist_name = musicData.artist;
                ArrayList<String> albums = new ArrayList<String>();
                byte[] artwork = null;

                String subq = new Select("DISTINCT album").from(MusicData.class).where("artist='" + artist_name + "'").toSql();

                List<MusicData> albumList
                        = new Select().from(MusicData.class)
//                        .where("IN (" + new Select("DISTINCT album").from(MusicData.class).where("artist = ?", artist_name).toSql() +")")
                        .where("album IN (" + subq + ")")
                        .execute();

                artwork = albumList.get(0).artwork;

                for(MusicData album : albumList){
                    albums.add(album.album);
                }
                ret.add(new Artist(artist_name, artwork, (String[]) albums.toArray(new String[0])));
            }

            return ret;
        }
    }
}
