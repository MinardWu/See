package com.minardwu.see.net;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SendCallback;
import com.minardwu.see.base.Config;

import java.util.List;

/**
 * Created by MinardWu on 2017/7/20.
 */
public class PushNews {

    public static void push(String userid){
        AVQuery<AVObject> query = new AVQuery<AVObject>("_User");
        query.whereEqualTo("objectId",userid);
        query.findInBackground(new FindCallback<AVObject>() {
            public void done(List<AVObject> avObjects, AVException e) {
                if (e == null) {
                    final String installationID = avObjects.get(0).getString("installationId");
                    AVQuery pushQuery = AVInstallation.getQuery();
                    pushQuery.whereEqualTo("installationId", installationID);
                    AVPush.sendMessageInBackground(Config.me.getUsername()+" 请求添加你为好友",pushQuery, new SendCallback() {
                        @Override
                        public void done(AVException e) {
                            if(e==null){
                                Log.v("PushNews","success");
                            }else {
                                Log.v("PushNews",e.getMessage().toString());
                            }
                        }
                    });
                }else {
                    Log.v("PushNews",e.getMessage().toString());
                }
            }
        });
    }

}
