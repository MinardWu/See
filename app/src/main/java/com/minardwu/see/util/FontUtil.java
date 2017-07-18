package com.minardwu.see.util;

import android.content.res.AssetManager;
import android.graphics.Typeface;

import com.minardwu.see.base.MyApplication;

/**
 * Created by MinardWu on 2017/7/17.
 */
public class FontUtil {

    public static Typeface getSegoeUISemilight(){
        AssetManager assets = MyApplication.getAppContext().getAssets();
        Typeface font = Typeface.createFromAsset(assets, "fonts/SegoeUISemilight.ttf");
        return font;
    }

}
