package com.keitaro.activitymusic.fragment.activitydata;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.google.android.gms.location.DetectedActivity;
import com.keitaro.activitymusic.R;
import com.keitaro.activitymusic.databese.model.LocationData;
import com.keitaro.activitymusic.databese.model.MusicData;
import com.keitaro.activitymusic.fragment.trackdata.customadapter.model.Album;
import com.keitaro.activitymusic.util.ActivityTypeTranslater;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user1 on 2014/08/25.
 */
public class WalkDataFragment extends Fragment {
    public static final String ACTIVITY = "ACTIVITY";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_activitydata, null);

        Bundle bundle = getArguments();
        int aType = bundle.getInt(ACTIVITY);

//        if(savedInstanceState == null){
//            aType = DetectedActivity.ON_FOOT;
//        }else{
//            aType = savedInstanceState.getInt(this.ACTIVITY);
//        }

        ActivityAdapter adapter = new ActivityAdapter(getActivity().getApplicationContext());
        ListView listView = (ListView) rootView.findViewById(R.id.list_activity_data);
        listView.setAdapter(adapter);

        new GetRecentActivityTask(adapter).execute(aType);

        return rootView;
    }

    class GetRecentActivityTask extends AsyncTask<Integer, Void, List<MusicData>>{
        private final ActivityAdapter adapter;

        public GetRecentActivityTask(ActivityAdapter adapter){
            this.adapter = adapter;
        }

        @Override
        protected List<MusicData> doInBackground(Integer... integers) {
            return this.getActivityRank(integers[0]);
        }

        @Override
        protected void onPostExecute(List<MusicData> list) {
            for(MusicData md : list) {
                adapter.add(md);
            }
        }

        private List<MusicData> getActivityRank(final int type) {
            List<MusicData> ret = new ArrayList<MusicData>();

            From query = new Select("musicID, COUNT(*) AS sum")
                    .from(LocationData.class).where("activity="+type).groupBy("musicID")
                    .having("COUNT(*) > 1").orderBy("COUNT(*)");

            Cursor cursor = Cache.openDatabase().rawQuery(query.toSql(),query.getArguments());

            if(cursor.moveToFirst()){
                int i = 0;
                do{
                    long musicId = cursor.getInt(cursor.getColumnIndex("musicID"));
                    long count = cursor.getInt(cursor.getColumnIndex("sum"));
                    Log.d("WalkDataFragment", "count : " + count);

                    MusicData musicDataList = new Select().from(MusicData.class).where("ID=?",musicId).executeSingle();
                    ret.add(musicDataList);
                    i++;
                }while(cursor.moveToNext() && i<5);
            }else{
                Log.d("WalkDataFragment","cursor can not move to first!!" + type);
            }

            return ret;
        }
    }

    private String getActivityName(int i){
        switch(i) {
            case DetectedActivity.IN_VEHICLE:
                return "乗り物";
            case DetectedActivity.ON_BICYCLE:
                return "自転車";
            case DetectedActivity.ON_FOOT:
                return "徒歩";
            case DetectedActivity.STILL:
                return "待機中";
            case DetectedActivity.UNKNOWN:
                return "不明な行動中";
            case DetectedActivity.TILTING:
                return "デバイスが傾き中";
            default:
                return "不明な行動中";
        }
    }

    private class ActivityAdapter extends ArrayAdapter<MusicData>{
        private final int resourceId;

        public ActivityAdapter(Context context) {
            super(context, R.layout.item_activitydata);
            this.resourceId = R.layout.item_activitydata;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resourceId, null);
            }

            MusicData musicData = getItem(position);

            TextView rank = (TextView) convertView.findViewById(R.id.textview_rank);

            rank.setText(position + 1 + " 位");

            ImageView imageView = (ImageView) convertView.findViewById(R.id.view_artwork_item_activitydata);
            Bitmap artwork = BitmapFactory.decodeByteArray(musicData.artwork,0,musicData.artwork.length);
            imageView.setImageBitmap(artwork);

            TextView tv = (TextView) convertView.findViewById(R.id.artist_name_activitydata);
            tv.setText(musicData.artist);

            tv = (TextView) convertView.findViewById(R.id.album_name_activitydata);
            tv.setText(musicData.album);

            tv = (TextView) convertView.findViewById(R.id.track_name_activitydata);
            tv.setText(musicData.trackName);

            return convertView;
        }
    }

}
