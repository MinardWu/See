package com.minardwu.see.net;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.minardwu.see.base.Config;
import com.minardwu.see.event.SetUserInfoEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by MinardWu on 2017/7/20.
 */
public class SaveInstallationId {

    public static void save(){
        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            public void done(AVException e) {
                if (e == null) {
                    // 保存成功
                    String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
                    // 关联installationId 到用户表
                    if(AVUser.getCurrentUser()!=null){
                        AVUser.getCurrentUser().put("installationId",installationId);
                        AVUser.getCurrentUser().saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if(e==null){
                                    Log.d("SaveInstallationId","success");
                                }else {
                                    Log.d("SaveInstallationId",e.getMessage());
                                }
                            }
                        });
                    }
                } else {
                    Log.d("SaveInstallationId",e.getMessage());
                }
            }
        });
    }

}
