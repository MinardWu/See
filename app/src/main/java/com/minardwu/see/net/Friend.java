package com.minardwu.see.net;

import android.util.Log;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.minardwu.see.base.Config;
import com.minardwu.see.event.AddFriendEvent;
import com.minardwu.see.event.GetFriendEvent;
import com.minardwu.see.event.ResultCodeEvent;
import com.minardwu.see.event.SetUserInfoEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/6/26.
 */
public class Friend {

    //有好友而且删除失败则post：-3，没有好友或者删除成功的话则调用addFriend，在里面post相关信息
    public static int addFriendWithCheck(final String user1id, final String user2id){
        final int[] result = new int[1];
        final int[] count = {0};
        final AVQuery<AVObject> firstQuery = new AVQuery<>("Friend");
        firstQuery.whereEqualTo("user1id",user1id);
        final AVQuery<AVObject> secondQuery = new AVQuery<>("Friend");
        secondQuery.whereEqualTo("user2id",user1id);
        final AVQuery<AVObject> thirdQuery = new AVQuery<>("Friend");
        thirdQuery.whereEqualTo("user1id",user2id);
        final AVQuery<AVObject> fourthQuery = new AVQuery<>("Friend");
        fourthQuery.whereEqualTo("user2id",user2id);
        AVQuery<AVObject> query = AVQuery.or(Arrays.asList(firstQuery, secondQuery,thirdQuery,fourthQuery));
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(final List<AVObject> list, AVException e) {
                if(list.size()!=0){
                    for (AVObject avObject:list){
                        try {
                            Log.v("deleteFriend",avObject.toString());
                            JSONObject jsonObject = new JSONObject(avObject.toString());
                            Log.v("deleteFriend",jsonObject.getString("objectId"));
                            String temp_objectId = jsonObject.getString("objectId");
                            AVQuery.doCloudQueryInBackground("delete from Friend where objectId='"+temp_objectId+"'", new CloudQueryCallback<AVCloudQueryResult>() {
                                @Override
                                public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                                    if(e==null){
                                        Log.v("deleteFriend","delete success");
                                        count[0]++;
                                        Log.v("deleteFriend",""+count[0]);
                                        //两个或四个好友记录都删除了（即一个人有好友或两个人有好友）
                                        if(count[0]==list.size()){
                                            addFriend(user1id,user2id);
                                        }
                                    }else {
                                        Log.v("deleteFriend","delete fail");
                                        Log.v("deleteFriend",e.getMessage());
                                        EventBus.getDefault().post(new AddFriendEvent(-3));
                                    }
                                }
                            });
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                }else {
                    Log.v("deleteFriend","you have no friend");
                    addFriend(user1id,user2id);
                }
            }
        });
        return 0;
    }

    public static int addFriend(final String user1id, final String user2id){
        final int[] result = new int[1];
        final AVObject user1 = AVObject.createWithoutData("_User",user1id);
        final AVObject user2 = AVObject.createWithoutData("_User",user2id);
        AVObject friend1 = new AVObject("Friend");
        friend1.put("user1",user1);
        friend1.put("user2",user2);
        friend1.put("user1id",user1id);
        friend1.put("user2id",user2id);
        friend1.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e==null){
                    Log.v("addFriend","one step success");
                    AVObject friend2 = new AVObject("Friend");
                    friend2.put("user1",user2);
                    friend2.put("user2",user1);
                    friend2.put("user1id",user2id);
                    friend2.put("user2id",user1id);
                    friend2.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if(e==null){
                                Log.v("addFriend","two step success");
                                EventBus.getDefault().post(new AddFriendEvent(1));
                            }else {
                                Log.v("addFriend","two step fail");
                                Log.v("addFriend",e.getMessage().toString());
                                EventBus.getDefault().post(new AddFriendEvent(-2));
                            }
                        }
                    });
                }else {
                    EventBus.getDefault().post(new AddFriendEvent(-1));
                    Log.v("addFriend","one step fail");
                    Log.v("addFriend",e.getMessage().toString());
                }
            }
        });
        return result[0];
    }

    public static int deleteFriend(String userid){
        final int[] result = new int[1];
        final int[] count = {0};
        final AVQuery<AVObject> firstQuery = new AVQuery<>("Friend");
        firstQuery.whereEqualTo("user1id",userid);
        final AVQuery<AVObject> secondQuery = new AVQuery<>("Friend");
        secondQuery.whereEqualTo("user2id",userid);
        AVQuery<AVObject> query = AVQuery.or(Arrays.asList(firstQuery, secondQuery));
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(list.size()!=0){
                    for (AVObject avObject:list){
                        try {
                            Log.v("deleteFriend",avObject.toString());
                            JSONObject jsonObject = new JSONObject(avObject.toString());
                            Log.v("deleteFriend",jsonObject.getString("objectId"));
                            final String temp_objectId = jsonObject.getString("objectId");
                            AVQuery.doCloudQueryInBackground("delete from Friend where objectId='"+temp_objectId+"'", new CloudQueryCallback<AVCloudQueryResult>() {
                                @Override
                                public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                                    if(e==null){
                                        Log.v("deleteFriend","delete success");
                                        count[0]++;
                                        //两个好友记录都删除了
                                        if(count[0]==2){
                                            result[0] = 1;
                                            EventBus.getDefault().post(new SetUserInfoEvent(4,result[0]));
                                        }
                                    }else {
                                        Log.v("deleteFriend","delete fail");
                                        Log.v("deleteFriend",e.getMessage());
                                        result[0] = -1;
                                    }
                                }
                            });
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                }else {
                    Log.v("deleteFriend","you have no friend");
                }
            }
        });
        return result[0];
    }

    public static void getFriendid(){
        AVQuery<AVObject> query = new AVQuery<>("Friend");
        query.whereEqualTo("user1id", AVUser.getCurrentUser().getObjectId());
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                    if(e==null){
                        Log.d("getFriendid","success");
                        if(avObject==null){
                            EventBus.getDefault().post(new GetFriendEvent("0"));
                        }else {
                            Log.d("getFriendid",avObject.toString());
                            try {
                                JSONObject jsonObject = new JSONObject(avObject.toString());
                                JSONObject serverData = jsonObject.getJSONObject("serverData");
                                Log.d("getFriendid",serverData.getString("user2id"));
                                EventBus.getDefault().post(new GetFriendEvent(serverData.getString("user2id")));
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }else{
                        Log.d("getFriendid","fail");
                        Log.d("getFriendid",e.getMessage());
                        EventBus.getDefault().post(new GetFriendEvent("error"));
                    }

            }
        });
    }

}
