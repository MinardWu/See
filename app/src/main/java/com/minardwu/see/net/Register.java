package com.minardwu.see.net;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.minardwu.see.event.RegisterEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2017/6/26.
 */
public class Register {

    public static int register(String username, String password){
        final int[] result = new int[1];
        AVUser user = new AVUser();
        user.setUsername(username);
        user.setPassword(password);
        //user.put("mobilePhoneNumber", phonenumber);
        user.signUpInBackground(new SignUpCallback() {
            public void done(AVException e) {
                if (e == null) {
                    result[0] = 1;
                } else {
                    Log.d("Register",e.getMessage());
                    if(e.getMessage().equals("{\"code\":202,\"error\":\"Username has already been taken\"}")){
                        result[0] = -1;
                    }else if(e.getMessage().equals("{\"code\":214,\"error\":\"Mobile phone number has already been taken\"}")){
                        result[0] = -2;
                    } else {
                        result[0] = -3;
                    }
                }
                Log.d("Register",result[0]+"");
                EventBus.getDefault().post(new RegisterEvent(result[0]));
            }
        });
        return result[0];
    }
}
