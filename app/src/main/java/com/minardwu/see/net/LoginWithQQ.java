package com.minardwu.see.net;

import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.LogInCallback;
import com.minardwu.see.entity.UserForQQ;
import com.minardwu.see.event.LoginWithQQEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by MinardWu on 2017/7/24.
 */
public class LoginWithQQ {

    public static void login(final UserForQQ userForQQ){
        int sex;
        if(userForQQ.getSex().equals("男")){
            sex = 1;
        }else {
            sex = 0;
        }
        final int finalSex = sex;

        String qqId = userForQQ.getQqID();
        AVQuery<AVObject> query = new AVQuery<>("_User");
        query.whereEqualTo("qqId",qqId);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(e==null){
                    if(list.size()==0){
                        AVQuery.doCloudQueryInBackground("insert into _User(username, password, qqId ,sex) values('"+userForQQ.getUsername()+"', '0', '"+userForQQ.getQqID()+"',"+ finalSex +")", new CloudQueryCallback<AVCloudQueryResult>() {
                            @Override
                            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                                if(e==null){
                                    AVUser.logInInBackground(userForQQ.getUsername(),"0", new LogInCallback<AVUser>() {
                                        @Override
                                        public void done(AVUser avUser, AVException e) {
                                            if(e==null){
                                                Log.d("LoginWithQQ","success1");
                                                EventBus.getDefault().post(new LoginWithQQEvent(1));
                                            }else{
                                                Log.v("LoginWithQQ","fail1");
                                                EventBus.getDefault().post(new LoginWithQQEvent(-1));
                                            }
                                        }
                                    });
                                }else{
                                    if(e.getMessage().equals("{\"code\":202,\"error\":\"Username has already been taken\"}")){
                                        Log.v("LoginWithQQ","fail2");
                                        EventBus.getDefault().post(new LoginWithQQEvent(-2));
                                    } else {
                                        Log.v("LoginWithQQ","fail3");
                                        EventBus.getDefault().post(new LoginWithQQEvent(-3));
                                    }
                                }
                            }
                        });
                    }else {
                        Log.v("LoginWithQQ","user:"+list.get(0).getString("username"));
                        AVUser.logInInBackground(list.get(0).getString("username"),"0", new LogInCallback<AVUser>() {
                            @Override
                            public void done(AVUser avUser, AVException e) {
                                if(e==null){
                                    Log.v("LoginWithQQ","success2");
                                    EventBus.getDefault().post(new LoginWithQQEvent(2));
                                }else{
                                    Log.v("LoginWithQQ","fail4");
                                    EventBus.getDefault().post(new LoginWithQQEvent(-4));
                                }
                            }
                        });
                    }
                }else{
                    Log.v("LoginWithQQ",e.getMessage().toString());
                    EventBus.getDefault().post(new LoginWithQQEvent(-5));
                }
            }
        });
    }

}
