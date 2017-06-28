package com.minardwu.see.net;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.minardwu.see.entity.NewsEntity;
import com.minardwu.see.entity.User;
import com.minardwu.see.event.GetNewsEvent;
import com.minardwu.see.event.GetUserInfoEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/6/26.
 */
public class News {

    public static void sendNews(String targetid){
        AVObject news = new AVObject("News");
        news.put("from",AVUser.getCurrentUser().getObjectId());
        news.put("to",targetid);
        news.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e==null){
                    Log.d("sendNews","success");
                }else{
                    Log.d("sendNews","fail");
                    Log.d("sendNews",e.getMessage());
                }
            }
        });
    }

    public void getNews(){
        EventBus.getDefault().register(this);
        final AVQuery<AVObject> firstQuery = new AVQuery<>("News");
        firstQuery.whereEqualTo("to",AVUser.getCurrentUser().getObjectId());
        final AVQuery<AVObject> secondQuery = new AVQuery<>("News");
        secondQuery.whereEqualTo("status",1);
        AVQuery<AVObject> query = AVQuery.and(Arrays.asList(firstQuery, secondQuery));
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(e==null){
                    Log.d("getNews","success");
                    for (AVObject avObject:list){
                        try {
                            Log.v("getNews",avObject.toString());
                            JSONObject jsonObject = new JSONObject(avObject.toString());
                            JSONObject serverData = jsonObject.getJSONObject("serverData");
                            String newsid = jsonObject.getString("objectId");
                            Log.v("getNews",jsonObject.getString("objectId"));
                            Log.v("getNews",serverData.getString("from"));
                            //利用得到的from获取用户的资料，这里为了将特定的消息与用户绑定在一起用了getUserInfoByUserIdForNews这个稍微改造的方法
                            GetUserInfo.getUserInfoByUserIdForNews(newsid,serverData.getString("from"));
                        } catch (JSONException e1) {
                            Log.v("getNews",e1.getMessage());
                        }
                    }
                }else{
                    Log.d("getNews","getnews fail");
                    Log.d("getNews",e.getMessage());
                }
            }
        });
    }

    public void readNews(String newsId){
        AVObject changeState = AVObject.createWithoutData("News",newsId);
        changeState.put("status",0);
        changeState.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e==null){
                    Log.d("readNews","success");
                }else{
                    Log.d("readNews","fail");
                    Log.d("readNews",e.getMessage());
                }
            }
        });
    }

    @Subscribe
    public void onGetUserInfoEvent(GetUserInfoEvent event){
        User user = event.getUser();
        if(user!=null){
            Log.v("getNews",user.getUsername());
            EventBus.getDefault().post(new GetNewsEvent(new NewsEntity(user.getUserid(),user.getUsername(),user.getAvatar())));
        }else {
            Log.d("getNews","getuser fail");
        }
    };

}
