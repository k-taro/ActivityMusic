package com.keitaro.activitymusic.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.activeandroid.query.Select;
import com.keitaro.activitymusic.R;
import com.keitaro.activitymusic.databese.model.MusicData;
import com.keitaro.activitymusic.fragment.trackdata.customadapter.model.Album;
import com.keitaro.activitymusic.util.LogUtil;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Set;

/**
 * Created by user1 on 2014/08/18.
 *
 * 音楽プレイヤー(walkman)アプリの操作があった場合に楽曲情報を取得するレシーバクラス
 */
public class MediaPlayerReceiver extends BroadcastReceiver{

    private static MusicData mMusicData;
    public synchronized static MusicData getMusicData(){
        return mMusicData;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();

        String action = intent.getAction();

        if(action.equals("com.sonyericsson.music.playbackcontrol.ACTION_TRACK_STARTED")){
            mMusicData = new MusicData();
            mMusicData.album = bundle.getString("ALBUM_NAME");
            mMusicData.artist = bundle.getString("ARTIST_NAME");
            mMusicData.trackName = bundle.getString("TRACK_NAME");

            if(!isExist(mMusicData)) {
                mMusicData.artwork = getArtWork(context, mMusicData);
                if(mMusicData.artwork == null){
                    Log.d("MediaPlayerReceiver", "artwork bitmap is null!!!");
                }else{
                    Log.d("MediaPlayerReceiver", "bitmap : " + mMusicData.artwork);
                }
                mMusicData.save();
            }

//            LogUtil.printBundle("MediaPlayerReceiver", bundle);

        }else if(action.equals("com.sonyericsson.music.playbackcontrol.ACTION_PAUSED")){
            mMusicData = null;
        }else if(action.equals("com.sonyericsson.music.TRACK_COMPLETED")){
            mMusicData = null;
        }else if(action.equals("com.android.music.metachanged")) {
//            mMusicData = null;
//            Log.d("アクション",action);
//            Set<String> key = bundle.keySet();
//            for(String str : key){
//                Log.d("key",str + " : " + bundle.get(str));
//            }
//        }else if(action.equals("com.android.music.playbackcomplete")){
//            Log.d("アクション",action);
//            mMusicData = null;
//            Set<String> key = bundle.keySet();
//            for(String str : key){
//                Log.d("key",str + " : " + bundle.get(str));
//            }
//        }else if(action.equals("com.android.music.playstatechanged")){
//                        Log.d("アクション",action);
//            mMusicData = null;
//            Set<String> key = bundle.keySet();
//            for(String str : key){
//                Log.d("key",str + " : " + bundle.get(str));
//            }
//
        }

    }

    private byte[] getArtWork(Context context, MusicData musicData) {
        ContentResolver cr = context.getContentResolver();
        String[] columns = new String[]{
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.ALBUM_ART
        };

        Cursor cursor = cr.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                columns,
                MediaStore.Audio.Albums.ALBUM + "=? and "+MediaStore.Audio.Albums.ARTIST + "=?",
                new String[]{musicData.album, musicData.artist},
                null
        );

        byte[] ret;
        if(cursor.moveToFirst()) {
            String albumArtPath = cursor.getString(cursor.getColumnIndex( MediaStore.Audio.Albums.ALBUM_ART));
            File file = new File(albumArtPath);
            InputStream fis = null;
            try {
                fis = new FileInputStream(file);
                ret = getBytes(fis);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                ret = getHeadPhoneImage(context);
            }finally {
                if(fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
//            bitmap = BitmapFactory.decodeFile(albumArtPath);
//            bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_action_headphones);
        }else{
            Log.d("MediaPlayerReceiver", "cursor can not moveToFirst");
            ret = getHeadPhoneImage(context);
        }
        cursor.close();

        return ret;
    }

    private byte[] getHeadPhoneImage(Context context){
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_action_headphones);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    private byte[] getBytes(InputStream is) {
        // byte型の配列を出力先とするクラス。
        // 通常、バイト出力ストリームはファイルやソケットを出力先とするが、
        // ByteArrayOutputStreamクラスはbyte[]変数、つまりメモリを出力先とする。
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        OutputStream os = new BufferedOutputStream(b);
        int c;
        try {
            while ((c = is.read()) != -1) {
                os.write(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // 書き込み先はByteArrayOutputStreamクラス内部となる。
        // この書き込まれたバイトデータをbyte型配列として取り出す場合には、
        // toByteArray()メソッドを呼び出す。
        return b.toByteArray();
    }


    /**
     * 楽曲情報がデータベースに格納されている場合 true を返す
     * @param md 楽曲情報
     * @return データがある場合は true
     */
    private boolean isExist(MusicData md){
        return new Select().from(MusicData.class).where("artist = ? and album = ? and trackName = ?", md.artist, md.album, md.trackName).exists();
    }

    /**
     * デバッグ用メソッド
     * 通知領域に引数の楽曲データを表示する
     * @param context
     * @param musicData
     */
    private void showNotification(Context context, MusicData musicData){
        PendingIntent contentIntent = PendingIntent.getActivity(
                context, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);

        // NotificationBuilderを作成
        Notification.Builder builder = new Notification.Builder(
                context);
        builder.setContentIntent(contentIntent);
        // ステータスバーに表示されるテキスト
        builder.setTicker("Music Info");
        // アイコン
        builder.setSmallIcon(R.drawable.ic_launcher);
        // Notificationを開いたときに表示されるタイトル
        builder.setContentTitle("Music Info");
        // Notificationを開いたときに表示されるサブタイトル
        builder.setContentText("Artist : "+ musicData.artist + ", Album : " + musicData.album + ", Track Name : " + musicData.trackName);
        // Notificationを開いたときに表示されるアイコン
//        builder.setLargeIcon(R.drawable.ic_launcher);
        // 通知するタイミング
        builder.setWhen(System.currentTimeMillis());
        // 通知時の音・バイブ・ライト
        builder.setDefaults(Notification.DEFAULT_SOUND
                | Notification.DEFAULT_LIGHTS);
        // タップするとキャンセル(消える)
        builder.setAutoCancel(true);

        // NotificationManagerを取得
        NotificationManager manager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        // Notificationを作成して通知
        manager.notify(0, builder.build());
    }

}
