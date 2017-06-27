package com.minardwu.see.net;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;

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

    public static void getNews(){
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
                            Log.v("News",avObject.toString());
                            JSONObject jsonObject = new JSONObject(avObject.toString());
                            JSONObject serverData = jsonObject.getJSONObject("serverData");
                            Log.v("News",jsonObject.getString("objectId"));
                            Log.v("News",serverData.getString("from"));
                        } catch (JSONException e1) {
                            Log.v("News",e1.getMessage());
                        }
                    }
                }else{
                    Log.d("getNews","fail");
                    Log.d("getNews",e.getMessage());
                }
            }
        });
    }

    public static void readNews(String newsId){
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

}
