package com.minardwu.see.net;

import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.minardwu.see.base.MyApplication;
import com.minardwu.see.entity.Photo;
import com.minardwu.see.event.DeletePhotoEvent;
import com.minardwu.see.event.GetShowPhotoEvent;
import com.minardwu.see.event.GetUserPhotoEvent;
import com.minardwu.see.event.ResultCodeEvent;
import com.minardwu.see.event.SetShowPhotoEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/6/26.
 */
public class PhotoService {

    public static void uploadPhoto(final String path) {
        new Thread(){
            @Override
            public void run() {
                AVFile file = null;
                try {
                    file = AVFile.withAbsoluteLocalPath(System.currentTimeMillis() + ".jpg", path);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                AVObject avObject = new AVObject("Photo");
                avObject.put("userid", AVUser.getCurrentUser().getObjectId());
                avObject.put("photo", file);
                avObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e==null){
                            Log.d("uploadPhoto","success");
                            EventBus.getDefault().post(new ResultCodeEvent(1));
                        }else{
                            Log.d("uploadPhoto","fail");
                            Log.d("uploadPhoto",e.getMessage());
                            EventBus.getDefault().post(new ResultCodeEvent(-1));
                        }
                    }
                });

            }
        }.start();
    }

    public static void getPhoto(final String userid) {
        final List<Photo> photoList = new ArrayList<Photo>();
        AVQuery<AVObject> query = new AVQuery<>("Photo");
        query.whereEqualTo("userid",userid);
        query.orderByDescending("updatedAt");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(e==null){
                    Log.d("getPhoto","success");
                    if(list.size()==0){
                        EventBus.getDefault().post(new GetUserPhotoEvent(userid,photoList));
                    }else {
                        for (AVObject avObject:list){
                            try {
                                Log.v("getPhoto",avObject.toString());
                                JSONObject jsonObject = new JSONObject(avObject.toString());
                                JSONObject serverData = jsonObject.getJSONObject("serverData");
                                JSONObject photo = serverData.getJSONObject("photo");
                                String photoid = jsonObject.getString("objectId");
                                String userid = serverData.getString("userid");
                                String photourl = photo.getString("url");
                                String photoinfo = serverData.getString("photoinfo");
                                Log.v("getPhoto",photoid);
                                Log.v("getPhoto",userid);
                                Log.v("getPhoto",photourl);
                                Log.v("getPhoto",photoinfo);
                                photoList.add(new Photo(photoid,userid,photourl,photoinfo,0));
                            } catch (JSONException e1) {
                                Log.v("getPhoto",e1.getMessage());
                            }
                        }
                        EventBus.getDefault().post(new GetUserPhotoEvent(userid,photoList));
                    }
                }else {
                    Log.d("getPhoto","fail");
                    Log.v("getPhoto",e.getMessage());
                }
            }
        });
    }

    public static void deletePhoto(final String photoid) {
        AVQuery.doCloudQueryInBackground("delete from Photo where objectId='"+photoid+"'", new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                if(e==null){
                    Log.d("deletePhoto","success");
                    EventBus.getDefault().post(new DeletePhotoEvent(1,photoid));
                }else{
                    Log.d("deletePhoto","fail");
                    Log.d("deletePhoto",e.getMessage());
                    EventBus.getDefault().post(new DeletePhotoEvent(-1,photoid));
                }
            }
        });
    }

    public static void setShowPhoto(final String photoid) {
        AVObject showPhoto = AVObject.createWithoutData("Photo", photoid);
        long showtime = System.currentTimeMillis();
        showPhoto.put("showtime",showtime);
        showPhoto.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e==null){
                    Log.d("setShowPhoto","success");
                    EventBus.getDefault().post(new SetShowPhotoEvent(1,photoid));
                }else {
                    Log.d("setShowPhoto","fail");
                    Log.d("setShowPhoto",e.getMessage());
                    EventBus.getDefault().post(new SetShowPhotoEvent(0,photoid));
                }
            }
        });
    }

    public static void getShowPhoto(String userid) {
        AVQuery<AVObject> query = new AVQuery<>("Photo");
        query.whereEqualTo("userid",userid);
        query.orderByDescending("updatedAt");
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if(e==null){
                    try {
                        Log.d("getShowPhoto","success");
                        JSONObject jsonObject = new JSONObject(avObject.toString());
                        JSONObject serverData = jsonObject.getJSONObject("serverData");
                        JSONObject photo = serverData.getJSONObject("photo");
                        String photoid = jsonObject.getString("objectId");
                        String userid = serverData.getString("userid");
                        String photourl = photo.getString("url");
                        String photoinfo = serverData.getString("photoinfo");
                        Log.v("getShowPhoto",photoid);
                        Log.v("getShowPhoto",userid);
                        Log.v("getShowPhoto",photourl);
                        Log.v("getShowPhoto",photoinfo);
                        EventBus.getDefault().post(new GetShowPhotoEvent(new Photo(photoid,userid,photourl,photoinfo,1)));
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }else {
                    Log.d("getShowPhoto","fail");
                    Log.d("getShowPhoto",e.getMessage());
                }
            }
        });
    }
}
