package com.minardwu.see.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.minardwu.see.base.Config;
import com.minardwu.see.net.PhotoService;

public class GetShowPhotoService extends Service {

    public GetShowPhotoService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("getShowPhoto","onStartCommand");
        new Thread(){
            @Override
            public void run() {
                super.run();
                if(Config.you!=null){
                    PhotoService.getShowPhoto(Config.you.getUserid());
                }
            }
        }.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent();
        intent.setAction("android.intent.action.RESTART");
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.putExtra("resart","GetShowPhotoService");
        sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



}
