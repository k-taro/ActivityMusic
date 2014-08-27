package com.keitaro.activitymusic.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.keitaro.activitymusic.R;
import com.keitaro.activitymusic.fragment.HomeFragment;
import com.keitaro.activitymusic.fragment.TrackDataHomeFragment;
import com.keitaro.activitymusic.service.GoogleApiClientConnectService;


public class MyActivity extends FragmentActivity{

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mDrawerItemTitles;

    private String mTitle;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerItemTitles = getResources().getStringArray(R.array.drawerlisttitle_array);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list, mDrawerItemTitles));

        this.initActivityRecognition(getApplicationContext());

        selectItem(0);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view) {
                setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView) {
                setTitle(R.string.app_name);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    /**
     * デバッグ用のボタンの動作を設定する
     */
//    private void initButtonSetting(){
//        // ボタンをタップすると、データベース上の楽曲情報をログに表示する
//        Button b = (Button) this.findViewById(R.id.button1);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LinearLayout display = (LinearLayout) findViewById(R.id.data_display);
//                display.removeAllViews();
//                List<LocationData> itmes = new Select().from(LocationData.class).execute();
//                for (LocationData i : itmes) {
//                    TextView tv = new TextView(getApplicationContext());
//                    String data = "id : " + i.getId() + ", time : " + i.timestamp + ", lat : " + i.lat + ", lon : " + i.lon + ", accuracy : " + i.accuracy + ", activity : " + i.activity;
//                    tv.setText(data);
//                    Log.d("locdata", data);
//                    display.addView(tv);
//                }
//
//                List<MusicData> items = new Select().from(MusicData.class).execute();
//                for (MusicData i : items) {
//                    TextView tv = new TextView(getApplicationContext());
//                    String data = "id : " + i.getId() + ", artist : " + i.artist + ", album :" + i.album + ", track name : " + i.trackName + ", uri : " + i.uri;
//                    tv.setText(data);
//                    Log.d("musicdata", data);
//                    display.addView(tv);
//                }
//            }
//        });
//    }

    /**
     * 行動認識と位置情報を取得するための初期化
     * @param context
     */
    private void initActivityRecognition(Context context){
        Intent intent = new Intent(this, GoogleApiClientConnectService.class);
        this.startService(intent);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        // If the nav drawer is open, hide action items related to the content view
//        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title){
        getActionBar().setTitle(title);
    }

    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment;
        switch (position){
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new TrackDataHomeFragment();
                break;
            case 2:
            case 3:
            default:
                fragment = new HomeFragment();
                break;
        }


        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        mTitle = mDrawerItemTitles[position];
//        setTitle(mTitle);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private class DrawerItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            selectItem(position);
        }

    }

}
