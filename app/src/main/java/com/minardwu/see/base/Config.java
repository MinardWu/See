package com.minardwu.see.base;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Administrator on 2017/6/23.
 */
public class Config {
    public static final String tempUrl = "https://avatars3.githubusercontent.com/u/11813425?v=3&s=460";
    public static final WindowManager wm = (WindowManager) MyApplication.getAppContext().getSystemService(Context.WINDOW_SERVICE);
    public static final int screenWidth = wm.getDefaultDisplay().getWidth();;
    public static final int screenHeight= wm.getDefaultDisplay().getHeight();;
}
