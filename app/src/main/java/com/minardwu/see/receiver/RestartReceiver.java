package com.minardwu.see.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.minardwu.see.service.LockService;
import com.minardwu.see.util.AlarmHelper;

public class RestartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("RestartReceiver", "restart");
        if (intent.getAction().equals("android.intent.action.RESTART")) {
            AlarmHelper.startService(context,LockService.class,5);
        }
    }
}
