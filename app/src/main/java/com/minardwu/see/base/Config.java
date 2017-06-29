package com.minardwu.see.base;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import com.minardwu.see.entity.User;

/**
 * Created by Administrator on 2017/6/23.
 */
public class Config {
    public static User me = new User();
    public static User you = new User();
    public static String tempAvatarUrl = "https://avatars3.githubusercontent.com/u/11813425?v=3&s=460";
    public static String myTempAvatarUrl = "";
    public static String yourTempAvatarUrl = "";
    public static final WindowManager wm = (WindowManager) MyApplication.getAppContext().getSystemService(Context.WINDOW_SERVICE);
    public static final int screenWidth = wm.getDefaultDisplay().getWidth();
    public static final int screenHeight= wm.getDefaultDisplay().getHeight();
}
