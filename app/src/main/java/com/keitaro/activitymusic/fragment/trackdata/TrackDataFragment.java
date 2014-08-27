package com.keitaro.activitymusic.fragment.trackdata;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.keitaro.activitymusic.R;
import com.keitaro.activitymusic.databese.model.LocationData;
import com.keitaro.activitymusic.databese.model.MusicData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user1 on 2014/08/26.
 */
public class TrackDataFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListView rootView = (ListView) inflater.inflate(R.layout.fragment_trackdata, null);

        new GetTrackListTask(this.getActivity().getApplicationContext(), rootView).execute(new Object());

        TextView tv = new TextView(container.getContext());
        tv.setText("hogehoge");
        rootView.addView(tv);

        return rootView;
    }

    class GetTrackListTask extends AsyncTask {
        private Context context;
        private ListView listView;
        private List<String> mList;

        public GetTrackListTask(Context context, ListView listView){
            this.context = context;
            this.listView = listView;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            mList = this.getTrackData();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            for(String str : mList) {
                TextView tv = new TextView(context);
                tv.setTextSize(18);
                tv.setText(str);
                tv.setTextColor(Color.BLACK);
                listView.addView(tv);
            }
        }

        private List<String> getTrackData() {
            List<String> ret = new ArrayList<String>();

            List<MusicData> trackList = new Select().from(LocationData.class).execute();

            for(MusicData md : trackList){
                ret.add(md.trackName);
            }

            return ret;
        }
    }
}
