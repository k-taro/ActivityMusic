package com.keitaro.activitymusic;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.activeandroid.query.Select;
import com.activeandroid.util.SQLiteUtils;
import com.keitaro.activitymusic.databese.MySQLiteOpenHelper;
import com.keitaro.activitymusic.databese.model.MusicData;

import java.io.File;
import java.util.List;


public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my);
 //       SQLiteUtils.execSql("drop database ActivityMusic");

        Button b = (Button) this.findViewById(R.id.button1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<MusicData> items = new Select().from(MusicData.class).execute();
                for (MusicData i : items) {
                    Log.d("musicdata", "id : " + i.getId() + ", track name : " + i.trackName);
                }
            }
        });

//        b = (Button) this.findViewById(R.id.button_delete);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MySQLiteOpenHelper hlpr = new MySQLiteOpenHelper(getApplicationContext());
//                SQLiteDatabase musicDatabase = hlpr.getWritableDatabase();
//                File file = new File(musicDatabase.getPath());
//                if(file.exists()) {
//                    file.delete();
//                }
//            }
//        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
