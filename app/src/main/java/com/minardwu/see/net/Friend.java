package com.minardwu.see.net;

import android.util.Log;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.FindCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/6/26.
 */
public class Friend {

    public static int addFriend(String user1id, String user2id){
        AVObject user1 = AVObject.createWithoutData("_User",user1id);
        AVObject user2 = AVObject.createWithoutData("_User",user2id);
        AVObject friend1 = new AVObject("Friend");
        friend1.put("user1",user1);
        friend1.put("user2",user2);
        friend1.put("user1id",user1id);
        friend1.put("user2id",user2id);
        friend1.saveInBackground();

        AVObject friend2 = new AVObject("Friend");
        friend2.put("user1",user2);
        friend2.put("user2",user1);
        friend2.put("user1id",user2id);
        friend2.put("user2id",user1id);
        friend2.saveInBackground();
        return 0;
    }

    public static int deleteFriend(String userid){
        final AVQuery<AVObject> firstQuery = new AVQuery<>("Friend");
        firstQuery.whereEqualTo("user1id",userid);
        final AVQuery<AVObject> secondQuery = new AVQuery<>("Friend");
        secondQuery.whereEqualTo("user2id",userid);
        AVQuery<AVObject> query = AVQuery.or(Arrays.asList(firstQuery, secondQuery));
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                for (AVObject avObject:list){
                    try {
                        Log.v("Friend",avObject.toString());
                        JSONObject jsonObject = new JSONObject(avObject.toString());
                        Log.v("Friend",jsonObject.getString("objectId"));
                        String temp_objectId = jsonObject.getString("objectId");
                        AVQuery.doCloudQueryInBackground("delete from Friend where objectId='"+temp_objectId+"'", new CloudQueryCallback<AVCloudQueryResult>() {
                            @Override
                            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                                if(e==null){
                                    Log.v("Friend","delete success");
                                }else {
                                    Log.v("Friend",e.getMessage());
                                }
                            }
                        });
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        return 0;
    }

}
