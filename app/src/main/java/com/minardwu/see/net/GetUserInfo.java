package com.minardwu.see.net;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.minardwu.see.entity.User;
import com.minardwu.see.event.GetUserInfoEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017/6/26.
 */
public class GetUserInfo {

    static User user = new User();

    public static void getUserInfoByUserName(String username){
        AVQuery<AVObject> query = new AVQuery<>("_User");
        query.whereEqualTo("username",username);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(list.size()==0){
                    Log.d("getUserInfoByUserName","null");
                }
                for(AVObject avObject:list) {
                    if(e==null){
                        Log.d("getUserInfoByUserName","success");
                        Log.d("getUserInfoByUserName", avObject.toString());
                        try {
                            JSONObject root = new JSONObject(avObject.toString());
                            JSONObject serverData = root.getJSONObject("serverData");
                            JSONObject avatar = serverData.getJSONObject("avatar");
                            user.setUserid(root.getString("objectId"));
                            user.setUsername(serverData.getString("username"));
                            user.setAvatar(avatar.getString("url"));
                            Log.d("getUserInfoByUserName", root.getString("objectId"));
                            Log.d("getUserInfoByUserName", serverData.getString("username"));
                            Log.d("getUserInfoByUserName", avatar.getString("url"));
                            Log.d("getUserInfoByUserName", avObject.getAVFile("avatar").getUrl());
                        } catch (JSONException e1) {
                            Log.d("getUserInfoByUserId", e1.getMessage());
                        }
                    }else{
                        Log.d("getUserInfoByUserName","fail");
                        Log.d("getUserInfoByUserName",e.getMessage());
                    }
                }
            }
        });

    }

    public static void getUserInfoByUserId(String userid){
        AVQuery<AVObject> avQuery = new AVQuery<>("_User");
        avQuery.getInBackground(userid, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if(avObject==null){
                    Log.d("getUserInfoByUserId","null");
                }
                if(e==null){
                    Log.d("getUserInfoByUserId","success");
                    Log.d("getUserInfoByUserId",avObject.toString());
                    try {
                        JSONObject root = new JSONObject(avObject.toString());
                        JSONObject serverData = root.getJSONObject("serverData");
                        JSONObject avatar = serverData.getJSONObject("avatar");
                        user.setUserid(root.getString("objectId"));
                        user.setUsername(serverData.getString("username"));
                        user.setSex(serverData.getInt("sex"));
                        user.setAvatar(avatar.getString("url"));
                        Log.d("getUserInfoByUserId",root.getString("objectId"));
                        Log.d("getUserInfoByUserId",serverData.getString("username"));
                        Log.d("getUserInfoByUserId",serverData.getInt("sex")+"");
                        Log.d("getUserInfoByUserId",avatar.getString("url"));
                        EventBus.getDefault().post(new GetUserInfoEvent(user));
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                        Log.d("getUserInfoByUserId",e1.getMessage());
                    }
                }else{
                    Log.d("getUserInfoByUserId","fail");
                    Log.d("getUserInfoByUserId",e.getMessage());
                }
            }
        });
    }

    public static void getUserInfoByUserIdForNews(final String newsid, String userid){
        AVQuery<AVObject> avQuery = new AVQuery<>("_User");
        avQuery.getInBackground(userid, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if(avObject==null){
                    Log.d("getUserInfoByUserId","null");
                }
                if(e==null){
                    Log.d("getUserInfoByUserId","success");
                    Log.d("getUserInfoByUserId",avObject.toString());
                    try {
                        JSONObject root = new JSONObject(avObject.toString());
                        JSONObject serverData = root.getJSONObject("serverData");
                        JSONObject avatar = serverData.getJSONObject("avatar");
                        user.setUserid(newsid);
                        user.setUsername(serverData.getString("username"));
                        user.setAvatar(avatar.getString("url"));
                        Log.d("getUserInfoByUserId",root.getString("objectId"));
                        Log.d("getUserInfoByUserId",serverData.getString("username"));
                        Log.d("getUserInfoByUserId",serverData.getInt("sex")+"");
                        Log.d("getUserInfoByUserId",avatar.getString("url"));
                        EventBus.getDefault().post(new GetUserInfoEvent(user));
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                        Log.d("getUserInfoByUserId",e1.getMessage());
                    }
                }else{
                    Log.d("getUserInfoByUserId","fail");
                    Log.d("getUserInfoByUserId",e.getMessage());
                }
            }
        });
    }

}
