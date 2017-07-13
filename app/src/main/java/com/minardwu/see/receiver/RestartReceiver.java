package com.minardwu.see.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.minardwu.see.service.GetShowPhotoService;
import com.minardwu.see.service.LockService;
import com.minardwu.see.util.AlarmHelper;

public class RestartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("RestartReceiver", "restart");
        if(intent.getStringExtra("resart").equals("LockService")){
            Log.d("RestartReceiver", "LockService");
            AlarmHelper.startService(context,LockService.class,5);
        }else if(intent.getStringExtra("resart").equals("GetShowPhotoService")){
            Log.d("RestartReceiver", "GetShowPhotoService");
            AlarmHelper.startService(context,GetShowPhotoService.class,5);
        }
    }
}
