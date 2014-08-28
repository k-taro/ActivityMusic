package com.keitaro.activitymusic.fragment;

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
import com.keitaro.activitymusic.databese.model.LocationData;
import com.keitaro.activitymusic.databese.model.MusicData;
import com.keitaro.activitymusic.util.ActivityTypeTranslater;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user1 on 2014/08/25.
 */
public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, null);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1);
        ListView listView = (ListView) rootView.findViewById(R.id.list_recent_activity);
        listView.setAdapter(arrayAdapter);

        new GetRecentActivityTask(arrayAdapter).execute();

        return rootView;
    }

    class GetRecentActivityTask extends AsyncTask<Void, Void, List<String>>{
        private final ArrayAdapter<String> adapter;

        public GetRecentActivityTask(ArrayAdapter adapter){
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
            List<String> ret = new ArrayList<String>();

            List<LocationData> locList = new Select().from(LocationData.class).orderBy("timestamp DESC").execute();

            int index = 0;

            if (locList.size() <= 0) {
                return ret;
            }

            for (int count = 0; count < 5; count++) {
                if (index >= locList.size()) {
                    return ret;
                }
                LocationData locData = locList.get(index++);
                MusicData musicData = new Select().from(MusicData.class).where("ID = ?", locData.music_id).executeSingle();

                while (true) {
                    if (index >= locList.size()) {
                        return ret;
                    }
                    LocationData tmp = locList.get(index++);
                    if (tmp.music_id == musicData.getId()) {

                    } else {
                        ret.add("緯度経度座標(" + locData.lat + ", " + locData.lon + ")周辺で"
                                + ActivityTypeTranslater.getTypeName(locData.activity) + "に"
                                + musicData.trackName + "を聴いていました。");
                        index--;
                        break;
                    }
                }
            }
            return ret;
        }
    }
}
