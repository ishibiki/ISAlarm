package com.ishibiki.isalarm;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

public class StartServiceSampleService extends Service {
     // Timerオブジェクト
     private Timer timer = null;
     // 経過時間
     private int countTime;
     // 終了時間
     private int stopTime;
     // ハンドラー用サービスオブジェクト
     private static Service serviceObj;

     private MediaPlayer mp;

     @Override
     public IBinder onBind(Intent arg0) {
        return null;
     }
     // onCreateメソッド(サービス初期起動イベントハンドラ)
     // サービス起動メッセージ
     @Override
     public void onCreate() {

        // トースト表示
        Toast.makeText(this,
               "サービスを起動します。",
               Toast.LENGTH_SHORT).show();

        // タイマーと経過時間初期化
        timer = new Timer();
        countTime = 0;

        // ハンドラー用サービスオブジェクトを保持
        serviceObj = this;
     }

     // onStartCommandメソッド(サービス開始イベントハンドラ)
     // サービス開始メッセージ TimeerTaskを生成し、10秒ごとにrunメソッドを呼び出す
     @Override
     public int onStartCommand(Intent intent, int flags, int startId) {

        // トースト表示
        Toast.makeText(this,
               "サービスを開始します。",
               Toast.LENGTH_SHORT).show();

        // タイマー設定（１０秒ごとにrunメソッド呼び出し）
        timer.schedule(task, 10000, 10000);

        // 終了時間取得
        Bundle bundle = intent.getExtras();
        stopTime = Integer.parseInt(bundle.getString("STOPTIME"));

        // 処理中断時（強制終了）時に処理の再開方法について指定
         return START_NOT_STICKY;
    }
     // onDestroyメソッド（サービス終了イベントハンドラ）
     @Override
      public void onDestroy() {

         // トースト表示
        Toast.makeText(this,
               "サービスを終了します。",
               Toast.LENGTH_SHORT).show();
        // ここに音を入れる！！
        mp = MediaPlayer.create(this,R.raw.music);
        mp.start();


         // タイマー設定解除
         timer.cancel();
         timer.purge();
     }

     // ハンドラー生成 メッセージ処理
     private static Handler handler = new Handler(){
         // メッセージ表示
         public void handleMessage(Message msg) {
               Toast.makeText(serviceObj,
                           (String)msg.obj,
                               Toast.LENGTH_SHORT).show();
         }
      };
      // TimerTaskオブジェクト生成
      /*タイマーカウントアップ(10秒ごとにコールされる)
       * 終了時間であればサービス終了
       * そうでなければメッセージを組み立てハンドラに渡す
      */
      private TimerTask task = new TimerTask() {
          @Override
          public void run() {
                // 10秒ごとにカウントアップ
                 countTime += 10;

                 if(countTime / 60 == stopTime){
                       // サービス終了
                       stopSelf();
                 }else{
                       // handlerにメッセージ送信
                       handler.sendMessage(
                              Message.obtain(handler,
                                                 0,
                                                 countTime / 60 +
                                                 "分"+
                                                 countTime % 60+"秒経過しました！"));
                  }
             }
       };

 }

