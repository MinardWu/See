package com.minardwu.see.base;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.avos.avoscloud.AVOSCloud;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.File;

/**
 * Created by MinardWu on 2017/6/22.
 */
public class MyApplication extends Application {

    public static Context context;
    private  String dirPath = Environment.getExternalStorageDirectory() + "/light";


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Fresco.initialize(this);
        AVOSCloud.initialize(this,"gh25r7M4yb5989YauDjwfPQb-gzGzoHsz","MV820glQWmHtVRCOX9mT9oDf");
        AVOSCloud.setDebugLogEnabled(true);
        File fileDir = new File(dirPath);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
    }

    public static Context getAppContext(){
        return context;
    };
}
