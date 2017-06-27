package com.minardwu.see.net;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.minardwu.see.event.LoginEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2017/6/26.
 */
public class Login {

    public static void login(String username, String password){
        final int[] result = new int[1];
        AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if(e==null){
                    Log.d("Login","success");
                    result[0] = 1;
                }else{
                    Log.d("Login",e.getMessage());
                    if(e.getMessage().equals("{\"code\":210,\"error\":\"The username and password mismatch.\"}")){
                        result[0] = -1;
                    }else if(e.getMessage().equals("{\"code\":211,\"error\":\"Could not find user\"}")){
                        result[0] = -2;
                    }else {
                        result[0] = -3;
                    }
                }
                Log.d("Login",result[0]+"");
                EventBus.getDefault().post(new LoginEvent(result[0]));
            }
        });
    }

}
