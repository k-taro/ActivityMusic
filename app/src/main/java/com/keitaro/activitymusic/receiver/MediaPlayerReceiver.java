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


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        String album = bundle.getString("ALBUM_NAME");
        String artist = bundle.getString("ARTIST_NAME");
        String track = bundle.getString("TRACK_NAME");


        Log.d("receiveMusic", intent.getAction());
        //this.showBundleData(bundle);

        PendingIntent contentIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

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
        builder.setContentText("Artist : "+ artist + ", Album : " + album + ", Track Name : " +track);
        // Notificationを開いたときに表示されるアイコン
//        builder.setLargeIcon(R.drawable.ic_launcher);
        // 通知するタイミング
        builder.setWhen(System.currentTimeMillis());
        // 通知時の音・バイブ・ライト
        builder.setDefaults(Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE
                | Notification.DEFAULT_LIGHTS);
        // タップするとキャンセル(消える)
        builder.setAutoCancel(true);

        // NotificationManagerを取得
        NotificationManager manager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        // Notificationを作成して通知
        manager.notify(0, builder.build());

        MusicData musicData = new MusicData();
        musicData.artist = artist;
        musicData.album = album;
        musicData.trackName = track;

        if(!isExist(musicData)){
            musicData.save();
        }
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
}
