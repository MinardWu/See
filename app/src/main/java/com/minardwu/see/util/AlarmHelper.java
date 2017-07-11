package com.minardwu.see.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by MinardWu on 2017/7/11.
 */
public class AlarmHelper {

    public static void startService(Context context,Class<?> aClass,int seconds) {
        //获取AlarmManager系统服务
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //包装需要执行Service的Intent
        Intent intent = new Intent(context, aClass);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //触发服务
        long start = System.currentTimeMillis();
        manager.setRepeating(AlarmManager.RTC_WAKEUP, start,seconds * 1000, pendingIntent);
    }


    public static void stopService(Context context, Class<?> cls) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getService(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        //取消正在执行的服务
        manager.cancel(pendingIntent);
    }

}
