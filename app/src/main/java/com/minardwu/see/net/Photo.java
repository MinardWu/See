package com.minardwu.see.net;

import android.util.Log;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/6/26.
 */
public class Photo {

    public static void uploadPhoto(final String path) {

        new Thread(){
            @Override
            public void run() {
                AVFile file = new AVFile("mi.png","https://avatars3.githubusercontent.com/u/11813425?v=3&s=460", new HashMap<String, Object>());
//        AVFile file = null;
//        try {
//            file = AVFile.withAbsoluteLocalPath(System.currentTimeMillis() + ".jpg", path);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

                AVObject avObject = new AVObject("Photo");
                avObject.put("userid", AVUser.getCurrentUser().getObjectId());
                avObject.put("photo", file);
                avObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e==null){
                            Log.d("uploadPhoto","success");
                        }else{
                            Log.d("uploadPhoto","fail");
                            Log.d("uploadPhoto",e.getMessage());
                        }
                    }
                });

            }
        }.start();
    }

    public static void uploadPhotoAndShow(final String path) {
        new Thread(){
            @Override
            public void run() {
                AVFile file = new AVFile("mingln.png","https://avatars3.githubusercontent.com/u/11813425?v=3&s=460", new HashMap<String, Object>());
//                AVFile file = null;
//                try {
//                    file = AVFile.withAbsoluteLocalPath(System.currentTimeMillis() + ".jpg", path);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }

                final AVObject avObject = new AVObject("Photo");
                avObject.put("userid", AVUser.getCurrentUser().getObjectId());
                avObject.put("photo", file);
                avObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e==null){
                            Log.d("uploadPhotoAndShow","success");
                            Log.d("uploadPhotoAndShow",avObject.getObjectId());
                            setShowPhoto(avObject.getObjectId());
                        }else{
                            Log.d("uploadPhotoAndShow","fail");
                            Log.d("uploadPhotoAndShow",e.getMessage());
                        }
                    }
                });
            }
        }.start();
    }

    public static void getPhoto(String userid) {
        AVQuery<AVObject> query = new AVQuery<>("Photo");
        query.whereEqualTo("userid",userid);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(e==null){
                    Log.d("getPhoto","success");
                    for (AVObject avObject:list){
                        try {
                            Log.v("getPhoto",avObject.toString());
                            JSONObject jsonObject = new JSONObject(avObject.toString());
                            JSONObject serverData = jsonObject.getJSONObject("serverData");
                            JSONObject photo = serverData.getJSONObject("photo");
                            Log.v("getPhoto",jsonObject.getString("objectId"));
                            Log.v("getPhoto",photo.getString("url"));
                        } catch (JSONException e1) {
                            Log.v("getPhoto",e1.getMessage());
                        }
                    }
                }else {
                    Log.d("getPhoto","fail");
                    Log.v("getPhoto",e.getMessage());
                }
            }
        });
    }

    public static void deletePhoto(String objectId) {
        AVQuery.doCloudQueryInBackground("delete from Photo where objectId='"+objectId+"'", new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                if(e==null){
                    Log.d("deletePhoto","success");
                }else{
                    Log.d("deletePhoto","fail");
                    Log.d("deletePhoto",e.getMessage());
                }
            }
        });
    }

    public static void setShowPhoto(String objectId) {
        AVObject showPhoto = AVObject.createWithoutData("Photo", objectId);
        long showtime = System.currentTimeMillis();
        showPhoto.put("showtime",showtime);
        showPhoto.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e==null){
                    Log.d("setShowPhoto","success");
                }else {
                    Log.d("setShowPhoto","fail");
                    Log.d("setShowPhoto",e.getMessage());
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
                        Log.v("getShowPhoto",jsonObject.getString("objectId"));
                        Log.v("getShowPhoto",photo.getString("url"));
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
