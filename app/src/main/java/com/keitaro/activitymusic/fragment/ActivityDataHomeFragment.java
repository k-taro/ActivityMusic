package com.keitaro.activitymusic.fragment;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.location.DetectedActivity;
import com.keitaro.activitymusic.R;
import com.keitaro.activitymusic.databese.model.LocationData;
import com.keitaro.activitymusic.databese.model.MusicData;
import com.keitaro.activitymusic.fragment.activitydata.WalkDataFragment;
import com.keitaro.activitymusic.fragment.trackdata.AlbumDataFragment;
import com.keitaro.activitymusic.fragment.trackdata.ArtistDataFragment;
import com.keitaro.activitymusic.fragment.trackdata.TrackDataFragment;
import com.keitaro.activitymusic.util.ActivityTypeTranslater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user1 on 2014/08/26.
 */
public class ActivityDataHomeFragment extends Fragment implements ActionBar.TabListener {

    private ViewPager mViewPager;
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    private int tabPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tabPosition = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_activitydatahome, null);

        final ActionBar actionBar = getActivity().getActionBar();

        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getFragmentManager());

        mViewPager = (ViewPager) rootView.findViewById(R.id.activitidatahome_pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
//        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                // When swiping between different app sections, select the corresponding tab.
//                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
//                // Tab.
//                actionBar.setSelectedNavigationItem(position);
//                tabPosition = position;
//            }
//        });

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        mViewPager.setPageMargin(pageMargin);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabs_activitydatahome);
        tabs.setViewPager(mViewPager);
//        tabs.setIndicatorColor(getResources().getColor(R.color.accent));
//        tabs.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                // When swiping between different app sections, select the corresponding tab.
//                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
//                // Tab.
////                actionBar.setSelectedNavigationItem(position);
//                tabPosition = position;
//                mViewPager.setCurrentItem(position);
//            }
//        });

//        actionBar.removeAllTabs();
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // For each of the sections in the app, add a tab to the action bar.
//        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
//            actionBar.addTab(
//                    actionBar.newTab()
//                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
//                            .setTabListener(this));
//        }

//        actionBar.selectTab(actionBar.getTabAt(tabPosition));

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
//        ActionBar actionBar = getActivity().getActionBar();
//        //actionBar.removeAllTabs();
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//
//        super.onResume();
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

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

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        private final HashMap<Integer, Integer> activityMap;

        public AppSectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
            activityMap = new HashMap<Integer, Integer>();
            int i = 0;
            activityMap.put(i++, DetectedActivity.ON_FOOT);
//            activityMap.put(i++, DetectedActivity.RUNNING);
            activityMap.put(i++, DetectedActivity.ON_BICYCLE);
            activityMap.put(i++, DetectedActivity.IN_VEHICLE);
            activityMap.put(i++, DetectedActivity.STILL);
        }

        @Override
        public Fragment getItem(int i) {
            WalkDataFragment fragment = new WalkDataFragment();
            Bundle bundle = new Bundle();

            int type = activityMap.get(i);
            Log.d("ActivityDataHome", "position : " + i + ", type : " + type);

            bundle.putInt(WalkDataFragment.ACTIVITY, type);
            fragment.setArguments(bundle);

            return fragment;
        }

        @Override
        public int getCount() {
            return activityMap.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String str = null;
            try {
//                str = title[position];
                str = getActivityName(activityMap.get(position));
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }
            return str;
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
    }
}
