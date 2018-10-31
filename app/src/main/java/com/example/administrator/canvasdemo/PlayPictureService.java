package com.example.administrator.canvasdemo;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;



public class PlayPictureService extends Service {
    private PlayPictureBinder playPictureBinder;
    public PlayPictureService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("PlayPictureService","onCreate");
        playPictureBinder=new PlayPictureBinder();

    }


    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("PlayPictureService","onStartCommand");
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("PlayPictureService","onBind");
       return playPictureBinder;
    }

    public class PlayPictureBinder extends Binder {
        //连接socket
         public void connect(String ip, int ch){

         }
         //请求图片流
         public void requestPicture(){

         }
         //关闭socket连接
         public void disConnect(){

         }
    }







}
