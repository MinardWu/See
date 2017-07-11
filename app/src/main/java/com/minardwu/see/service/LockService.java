package com.minardwu.see.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.minardwu.see.activity.LockActivity;

public class LockService extends Service {

//    private ScreenOnReceiver screenOnReceiver;
    private ScreenOffReceiver screenOffReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("LockService","onCreate");
//        screenOnReceiver=new ScreenOnReceiver();
//        IntentFilter screenOnFilter=new IntentFilter();
//        screenOnFilter.addAction("android.intent.action.SCREEN_ON");
//        registerReceiver(screenOnReceiver, screenOnFilter);
        screenOffReceiver=new ScreenOffReceiver();
        IntentFilter screenOffFilter=new IntentFilter();
        screenOffFilter.addAction("android.intent.action.SCREEN_OFF");
        registerReceiver(screenOffReceiver, screenOffFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("RestartReceiver","onStartCommand");
        return Service.START_STICKY;
    }

//    //监听屏幕变亮的广播接收器，变亮就屏蔽系统锁屏
//    private class ScreenOnReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action=intent.getAction();
//            if(action.equals("android.intent.action.SCREEN_ON")){
//                Log.e("NowScreenState", "on");
////                KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
////                KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock("");
////                lock.disableKeyguard();
//                Intent screnenOffIntent=new Intent(context,LockActivity.class);
//                screnenOffIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(screnenOffIntent);
//            }
//        }
//
//    }

    //监听屏幕变暗的广播接收器，变暗就启动应用锁屏界面activity
    private class ScreenOffReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals("android.intent.action.SCREEN_OFF")){
                Log.e("NowScreenState", "off");
                Intent screnenOffIntent=new Intent(context,LockActivity.class);
                screnenOffIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(screnenOffIntent);
            }
        }
    }


    @Override
    public void onDestroy() {
        Log.v("RestartReceiver","onDestroy");
        super.onDestroy();
        unregisterReceiver(screenOffReceiver);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.RESTART");
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
