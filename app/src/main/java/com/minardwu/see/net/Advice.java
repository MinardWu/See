package com.minardwu.see.net;

import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.minardwu.see.base.Config;
import com.minardwu.see.base.MyApplication;
import com.minardwu.see.event.PostAdviceEvent;
import com.minardwu.see.event.ResultCodeEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by MinardWu on 2017/7/14.
 */
public class Advice {

    public static void postAdvice(String advice){
        AVObject avObject = new AVObject("Advice");
        avObject.put("username", Config.me.getUsername());
        avObject.put("advice",advice);
        avObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e==null){
                    EventBus.getDefault().post(new PostAdviceEvent(1));
                }else {
                    EventBus.getDefault().post(new PostAdviceEvent(-1));
                }
            }
        });
    }

}
