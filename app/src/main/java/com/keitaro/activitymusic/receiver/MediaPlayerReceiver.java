package com.keitaro.activitymusic.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.activeandroid.query.Select;
import com.keitaro.activitymusic.R;
import com.keitaro.activitymusic.databese.model.MusicData;

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
            mMusicData.uri = bundle.getString("TRACK_URI");

            if(!isExist(mMusicData)) {
                mMusicData.save();
            }
        }else if(action.equals("com.sonyericsson.music.playbackcontrol.ACTION_PAUSED")){
            mMusicData = null;
        }else if(action.equals("com.sonyericsson.music.TRACK_COMPLETED")){
            mMusicData = null;
        }else if(action.equals("com.sonyericsson.music.playbackcontrol.ACTION_TRACK_STARTED")) {
//            mMusicData = new MusicData();
//            mMusicData.album = bundle.getString("album");
//            mMusicData.artist = bundle.getString("artist");
//            mMusicData.trackName = bundle.getString("track");
//            //mMusicData.uri = bundle.getString("uri");
//
//
//            if (!isExist(mMusicData)) {
//                mMusicData.save();
//            }
            Set<String> key = bundle.keySet();
            for(String str : key){
                Log.d("key",str);
            }
        }else if(action.equals("com.sonyericsson.music.playbackcontrol.ACTION_PAUSED")){
            mMusicData = null;
        }else if(action.equals("com.sonyericsson.music.TRACK_COMPLETED")){
            mMusicData = null;
        }

        //this.showNotification(context, musicData);

    }

    /**
     * Bundle に含まれるデータをすべて表示する（デバッグ用）
     * @param bundle
     */
    private void showBundleData(Bundle bundle){
        Set<String> keySet = bundle.keySet();
        for(String key : keySet) {
            Log.d("receiveMusic", key);
            Log.d("receiveMusic", "    : "+bundle.getString(key));
        }
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
