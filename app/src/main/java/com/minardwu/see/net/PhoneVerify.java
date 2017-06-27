package com.minardwu.see.net;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;

/**
 * Created by Administrator on 2017/6/26.
 */
public class PhoneVerify {

    public static int sendRequest(String phontnumber){
        final int[] result = new int[1];
        AVUser.requestMobilePhoneVerifyInBackground(phontnumber, new RequestMobileCodeCallback() {
            @Override
            public void done(AVException e) {
                if(e == null){
                    result[0] = 1;
                    Log.d("sendRequest","success");
                } else {
                    result[0] = -1;
                    Log.d("sendRequest","fail");
                    Log.d("sendRequest",e.getMessage());
                }
            }
        });
        return result[0];
    }

    public static int verifyRequest(String verifyCode){
        final int[] result = new int[1];
        AVUser.verifyMobilePhoneInBackground(verifyCode, new AVMobilePhoneVerifyCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    result[0] = 1;
                    Log.d("verifyRequest","success");
                } else {
                    result[0] = -1;
                    Log.d("verifyRequest","fail");
                    Log.d("verifyRequest",e.getMessage());
                }
            }
        });
        return result[0];
    }

}
