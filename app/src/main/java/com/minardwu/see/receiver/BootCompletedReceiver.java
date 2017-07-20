package com.minardwu.see.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.minardwu.see.service.LockService;
import com.minardwu.see.util.AlarmHelper;

public class BootCompletedReceiver extends BroadcastReceiver {
    public BootCompletedReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("LockService", "收到开机启动广播");
        AlarmHelper.startService(context,LockService.class,5);
    }
}
