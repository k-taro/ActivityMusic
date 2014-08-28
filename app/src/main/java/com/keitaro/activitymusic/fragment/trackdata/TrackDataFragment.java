package com.keitaro.activitymusic.fragment.trackdata;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.activeandroid.query.Select;
import com.keitaro.activitymusic.R;
import com.keitaro.activitymusic.databese.model.MusicData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by user1 on 2014/08/26.
 */
public class TrackDataFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListView rootView = (ListView) inflater.inflate(R.layout.fragment_trackdata, null);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1);
        ListView listView = (ListView) rootView.findViewById(R.id.list_trackdata);
        listView.setAdapter(arrayAdapter);

        new GetTrackListTask(arrayAdapter).execute();

        return rootView;
    }

    class GetTrackListTask extends AsyncTask<Void, Void, List<String>>{
        private final ArrayAdapter<String> adapter;

        public GetTrackListTask(ArrayAdapter adapter){
            this.adapter = adapter;
        }

        @Override
        protected List<String> doInBackground(Void... voids) {
            return this.getRecentActivity();
        }

        @Override
        protected void onPostExecute(List<String> list) {
            for(String s : list) {
                adapter.add(s);
            }
        }

        private List<String> getRecentActivity() {
//            PriorityQueue<String> queue = new PriorityQueue<String>();
            ArrayList<String> ret = new ArrayList<String>();
            List<MusicData> musicList = new Select().from(MusicData.class).execute();

            Collections.sort(musicList,new Comparator<MusicData>() {
                @Override
                public int compare(MusicData musicData, MusicData musicData2) {
                    return musicData.trackName.compareTo(musicData2.trackName);
                }
            });
            for(MusicData md : musicList){
                if(md.trackName == null){
                    ret.add("不明な曲");
                }else {
                    ret.add(md.trackName);
                }
            }
            return ret;
        }
    }
}
