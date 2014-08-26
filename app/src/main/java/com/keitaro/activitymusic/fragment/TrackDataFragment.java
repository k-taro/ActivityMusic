package com.keitaro.activitymusic.fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.keitaro.activitymusic.R;
import com.keitaro.activitymusic.databese.model.LocationData;
import com.keitaro.activitymusic.databese.model.MusicData;
import com.keitaro.activitymusic.util.ActivityTypeTranslater;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user1 on 2014/08/26.
 */
public class TrackDataFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, null);

        LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.list_recent_activity);

        new GetRecentActivityTask(this.getActivity().getApplicationContext(), linearLayout).execute(new Object());

        return rootView;
    }

    class GetRecentActivityTask extends AsyncTask {
        private Context context;
        private LinearLayout linearLayout;
        private List<String> mList;

        public GetRecentActivityTask(Context context, LinearLayout linearLayout){
            this.context = context;
            this.linearLayout = linearLayout;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            mList = this.getRecentActivity();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            for(String str : mList) {
                TextView tv = new TextView(context);
                tv.setTextSize(18);
                tv.setText(str);
                tv.setTextColor(Color.BLACK);
                linearLayout.addView(tv);
            }
        }

        private List<String> getRecentActivity() {
            List<String> ret = new ArrayList<String>();

            List<LocationData> locList = new Select().from(LocationData.class).orderBy("ID DESC").execute();

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
