package com.keitaro.activitymusic.databese;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user1 on 2014/08/19.
 *
 * 楽曲情報のデータベース用の SQLiteOpenHelper。
 * Activeandroid を用いることにしたので使われない。
 *
 */
@Deprecated
public class MySQLiteOpenHelper extends SQLiteOpenHelper{
    static final String DB = "aaaactivityMusic.db";
    static final int DB_VERSION = 1;
    static final String CREATE_TABLE = //"create table Locations "
            //+ "( id integer primary key autoincrement, lat real not null ,"
            //+ "long real not null, activity text, music_id integer nit null);\n"
            //+ "create table Musics (id, integer primarykey autoincrement, artist, text, title text);";
    "";
    static final String DROP_TABLE = "drop table location; drop table music;";

    public MySQLiteOpenHelper(Context c) {
        super(c, DB, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}
