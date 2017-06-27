package com.minardwu.see.base;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by MinardWu on 2017/6/22.
 */
public class MyApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Fresco.initialize(this);
        AVOSCloud.initialize(this,"gh25r7M4yb5989YauDjwfPQb-gzGzoHsz","MV820glQWmHtVRCOX9mT9oDf");
        AVOSCloud.setDebugLogEnabled(true);
    }

    public static Context getAppContext(){
        return context;
    };
}
