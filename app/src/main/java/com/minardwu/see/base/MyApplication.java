package com.minardwu.see.base;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.minardwu.see.activity.NewsActivity;
import com.minardwu.see.activity.SplashActivity;
import com.minardwu.see.net.SaveInstallationId;

import java.io.File;

/**
 * Created by MinardWu on 2017/6/22.
 */
public class MyApplication extends Application {

    public static Context context;
    private  String dirPath = Environment.getExternalStorageDirectory() + "/light";
    public static boolean firstIn;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //Fresco初始化
        Fresco.initialize(this);
        //LeanCloud初始化
        AVOSCloud.initialize(this,"gh25r7M4yb5989YauDjwfPQb-gzGzoHsz","MV820glQWmHtVRCOX9mT9oDf");
        AVOSCloud.setDebugLogEnabled(true);
        //推送服务
        //SaveInstallationId.save();
        PushService.setDefaultPushCallback(this, SplashActivity.class);
        PushService.subscribe(this, "public", SplashActivity.class);
        PushService.subscribe(this, "private", SplashActivity.class);
        PushService.subscribe(this, "protected", SplashActivity.class);

        //文件夹初始化
        File fileDir = new File(dirPath);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        firstIn = true;
    }

    public static Context getAppContext(){
        return context;
    };
}
