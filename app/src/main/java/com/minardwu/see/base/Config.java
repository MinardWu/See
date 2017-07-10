package com.minardwu.see.base;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import com.minardwu.see.entity.Photo;
import com.minardwu.see.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/23.
 */
public class Config {
    public static User me;
    public static User you;
    public static List<Photo> myPhotos = new ArrayList<Photo>();
    public static List<Photo> yourPhotos= new ArrayList<Photo>();
    public static String tempAvatarUrl = "https://avatars3.githubusercontent.com/u/11813425?v=3&s=460";
    public static String myTempAvatarUrl = "";
    public static String yourTempAvatarUrl = "";
    public static final WindowManager wm = (WindowManager) MyApplication.getAppContext().getSystemService(Context.WINDOW_SERVICE);
    public static final int screenWidth = wm.getDefaultDisplay().getWidth();
    public static final int screenHeight= wm.getDefaultDisplay().getHeight();

    public static boolean changeFriend = false;
    public static boolean deletePhoto = false;

    public static void resetFriend(){
        me.setFriendid("0");
        you.setUserid("0");
        you.setUsername("");
        you.setAvatar("");
        you.setSex(0);
    }
}
